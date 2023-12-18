package ru.dingo.apiKeyRotator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ProxyStorage {
    String filename = "proxies.json";
    @Getter
    ArrayList<ProxyEndpoint> proxyEndpoints = new ArrayList<>();

    private static ProxyStorage instance = null;
    private ProxyStorage() {
        loadFromFile();
    }
    public static ProxyStorage getInstance() {
        if (instance == null) {
            instance = new ProxyStorage();
        }
        return instance;
    }

    public void addProxyEndpoint(ProxyEndpoint proxyEndpoint) {
        proxyEndpoints.add(proxyEndpoint);
    }

    public void loadFromFile() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File(filename);
            if (file.exists()) {
                proxyEndpoints = mapper.readValue(file, new TypeReference<ArrayList<ProxyEndpoint>>(){});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToFile() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            mapper.writeValue(new File(filename), proxyEndpoints);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        proxyEndpoints.clear();
    }

    public void runBackgroundSaver() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    saveToFile();
                    try {
                        Thread.sleep(1000 * 3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    public ProxyEndpoint getProxyEndpoint(String endpointName) {
        for (ProxyEndpoint proxyEndpoint : proxyEndpoints) {
            if (proxyEndpoint.getFriendlyName().equals(endpointName)) {
                return proxyEndpoint;
            }
        }
        return null;
    }
}
