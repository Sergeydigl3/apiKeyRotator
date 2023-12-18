package ru.dingo.apiKeyRotator.utils;

import ru.dingo.apiKeyRotator.Key;
import ru.dingo.apiKeyRotator.KeyPack;
import ru.dingo.apiKeyRotator.KeysStorage;
import ru.dingo.apiKeyRotator.TimeCondition;

import java.util.ArrayList;

public class CreateKeysTest {
    public static void main(String[] args) {
        KeysStorage keysStorage = KeysStorage.getInstance();
        KeyPack keyPack = new KeyPack();
        keyPack.addKey(new Key("key1", "key1"));
        keysStorage.addKeyPack("SpotifyKeys2", keyPack);
        keysStorage.saveToFile();
        ArrayList<TimeCondition> timeConditions = new ArrayList<>();
        timeConditions.add(new TimeCondition(1, 1));
        keyPack.getKey("https://httpbin.org",timeConditions);
        keysStorage.saveToFile();
//        keysStorage.addKeyPack();
    }
}
