package ru.dingo.apiKeyRotator;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedList;


@Data
public class KeyPack {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    transient Object locker = new Object();
    ArrayList<Key> keys = new ArrayList<>();

    public Key getKey(String endpointUrl, ArrayList<TimeCondition> timeConditions) {
        for (Key key : keys) {
            synchronized (locker) {
                Key avalibleKey = key.useKey(endpointUrl, timeConditions);
                if (avalibleKey != null) {
                    return avalibleKey;
                }
            }
        }
        return null;
    }

    public void addKey(Key key) {
        keys.add(key);
    }
}
