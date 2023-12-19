package ru.dingo.apiKeyRotator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class KeysStorage {
    String filename = "keys.json";
    @Getter
    HashMap<String, KeyPack> keyPacks = new HashMap<>();

    private static KeysStorage instance = null;
    private KeysStorage() {
        loadFromFile();
//        runBackgroundSaver();
    }
    public static KeysStorage getInstance() {
        if (instance == null) {
            instance = new KeysStorage();
        }
        return instance;
    }

    public void addKeyPack(String keyPackName, KeyPack keyPack) {
        keyPacks.put(keyPackName, keyPack);
    }

    public KeyPack getKeyPack(String keyPackName) {
        return keyPacks.get(keyPackName);
    }


    public void saveToFile() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            mapper.writeValue(new File(filename), keyPacks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File(filename);
            if (file.exists()) {
                keyPacks = mapper.readValue(file, new TypeReference<HashMap<String, KeyPack>>(){});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void runBackgroundSaver() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    saveToFile();
                    try {
                        Thread.sleep(1000 * 1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }
}
