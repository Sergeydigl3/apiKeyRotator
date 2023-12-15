package ru.dingo.apiKeyRotator;

import lombok.Data;

@Data
public class ProxyEndpoint {
    boolean enabled;
    String friendlyName;
    String urlFrom; // Example: "/spotify"
    String urlTo; // Example: "https://api.spotify.com/v1"

    WhereKeys whereKey;
    String keyName; // not used for headerBearer

    // here should be some collected data storage
}

