package ru.dingo.apiKeyRotator;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;
import lombok.Data;
import lombok.Getter;

import javax.management.DescriptorKey;
import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


@Data
public class Key {
    String value;
    Integer usedCount;
    boolean enabled;
    String friendlyName;

    long maxTimeDelta;

    public Key(String value, String friendlyName) {
        this.value = value;
        this.friendlyName = friendlyName;
        this.usedCount = 0;
        this.enabled = true;
    }

    public Key() {
        this.usedCount = 0;
        this.enabled = true;
    }

    // Usage history
    // Key: endpoint url
    // Value: ArrayList of usage timestamps
    HashMap<String, LinkedList<Long>> usageHistory = new HashMap<>();

    public void useKey(String endpointUrl) {
        if (usageHistory.containsKey(endpointUrl)) {
            usageHistory.get(endpointUrl).add(System.currentTimeMillis());
        } else {
            LinkedList<Long> usageHistoryForEndpoint = new LinkedList<>();
            usageHistoryForEndpoint.add(System.currentTimeMillis());
            usageHistory.put(endpointUrl, usageHistoryForEndpoint);
        }
    }

    public boolean isKeyAvailable(String endpointUrl, ArrayList<TimeCondition> timeConditions) {
        if (usageHistory.containsKey(endpointUrl)) {
            LinkedList<Long> usageHistoryForEndpoint = usageHistory.get(endpointUrl);
            if (usageHistoryForEndpoint == null) {
                return true;
            }
            long currentTime = System.currentTimeMillis();

            // Remove old timestamps
            while (usageHistoryForEndpoint.size() > 0 && currentTime - usageHistoryForEndpoint.getFirst() > 1000 * maxTimeDelta) {
                usageHistoryForEndpoint.removeFirst();
            }

            // Check time conditions
            for (TimeCondition timeCondition : timeConditions) {
                int timeDelta = timeCondition.getSeconds() * 1000;
                int timeDeltaCount = 0;
                for (Long timestamp : usageHistoryForEndpoint) {
                    if (currentTime - timestamp < timeDelta) {
                        timeDeltaCount++;
                    }
                }
                if (timeDeltaCount >= timeCondition.getCount()) {
                    return false;
                }
            }
        }
        return true;
    }


    synchronized public Key useKey(String endpointUrl, ArrayList<TimeCondition> timeConditions) {
        for (TimeCondition timeCondition : timeConditions) {
            if (timeCondition.getSeconds() > maxTimeDelta) {
                maxTimeDelta = timeCondition.getSeconds();
            }
        }
        if (isKeyAvailable(endpointUrl, timeConditions)) {
            usedCount++;
            useKey(endpointUrl);
            return this;
        }
        return null;
    }
}
