package ru.dingo.apiKeyRotator;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class ProxyEndpoint {
    boolean enabled = true;
    String friendlyName;
    String urlFrom; // Example: "/spotify"
    String urlTo; // Example: "https://api.spotify.com/v1"

    WhereKeys whereKey;
    String keyName; // not used for headerBearer

    ArrayList<TimeCondition> timeConditions = new ArrayList<>();

    String keyPackToUse;

    // here should be some collected data storage

    public ProxyEndpoint(String friendlyName, String urlFrom, String urlTo, WhereKeys whereKey, String keyName, ArrayList<TimeCondition> timeConditions, String keyPackToUse) {
        this.friendlyName = friendlyName;
        this.urlFrom = urlFrom;
        this.urlTo = urlTo;
        this.whereKey = whereKey;
        this.keyName = keyName;
        this.timeConditions = timeConditions;
        this.keyPackToUse = keyPackToUse;
    }

    public String timeConditionsToString() {
        return TimeCondition.timeConditionsToString(timeConditions);
    }
}

