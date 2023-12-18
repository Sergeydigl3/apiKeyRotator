package ru.dingo.apiKeyRotator;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class KeysStorageTest {

    @Test
    void addKeyPack() {
        KeysStorage keysStorage = KeysStorage.getInstance();
        KeyPack keyPack = new KeyPack();
        keyPack.addKey(new Key("testKey1", "testValue1"));
        keyPack.addKey(new Key("testKey2", "testValue2"));
        keysStorage.addKeyPack("testKeyPack", keyPack);

        ArrayList<TimeCondition> timeConditions = TimeCondition.parseTimeConditions("2/5,3/10");

        assertNotNull(
                keysStorage.getKeyPack("testKeyPack").getKey("testUrl", timeConditions)
        );
        assertNotNull(
                keysStorage.getKeyPack("testKeyPack").getKey("testUrl", timeConditions)
        );
        assertNotNull(
                keysStorage.getKeyPack("testKeyPack").getKey("testUrl", timeConditions)
        );
        assertNotNull(
                keysStorage.getKeyPack("testKeyPack").getKey("testUrl", timeConditions)
        );
        assertNull(
                keysStorage.getKeyPack("testKeyPack").getKey("testUrl", timeConditions)
        );

        // Sleep 6 seconds
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertNotNull(
                keysStorage.getKeyPack("testKeyPack").getKey("testUrl", timeConditions)
        );
        assertNotNull(
                keysStorage.getKeyPack("testKeyPack").getKey("testUrl", timeConditions)
        );
        assertNull(
                keysStorage.getKeyPack("testKeyPack").getKey("testUrl", timeConditions)
        );

    }
}