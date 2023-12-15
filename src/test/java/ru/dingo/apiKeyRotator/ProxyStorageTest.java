package ru.dingo.apiKeyRotator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProxyStorageTest {
    ProxyStorage proxyStorage;

    @BeforeEach
    void setUp() {
        proxyStorage = ProxyStorage.getInstance();
    }

    @Test
    @Order(1)
    void saveToFile() {
        proxyStorage.clear();
        assertEquals(0, proxyStorage.proxyEndpoints.size());

        ProxyEndpoint proxyEndpoint = new ProxyEndpoint();
        proxyEndpoint.setEnabled(true);
        proxyEndpoint.setFriendlyName("Spotify");
        proxyEndpoint.setUrlFrom("/spotify");
        proxyEndpoint.setUrlTo("https://api.spotify.com/v1");
        proxyEndpoint.setWhereKey(WhereKeys.HEADER);
        proxyEndpoint.setKeyName("Authorization");
        proxyStorage.addProxyEndpoint(proxyEndpoint);
        proxyStorage.saveToFile();
        assertEquals(1, proxyStorage.proxyEndpoints.size());
    }

    @Test
    @Order(2)
    void loadFromFile() {
        proxyStorage.loadFromFile();
        assertEquals(1, proxyStorage.proxyEndpoints.size());
    }
}