package ru.dingo.apiKeyRotator;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class KeyPackTest {

    @Test
    void getKey() {
        KeyPack keyPack = new KeyPack();
        keyPack.addKey(new Key("testKey1", "testValue1"));
        keyPack.addKey(new Key("testKey2", "testValue2"));

        ArrayList<TimeCondition> timeConditions = TimeCondition.parseTimeConditions("2/5,3/10");

        assertNotNull(
                keyPack.getKey("testUrl", timeConditions)
        );
        assertNotNull(
                keyPack.getKey("testUrl", timeConditions)
        );
        assertNotNull(
                keyPack.getKey("testUrl", timeConditions)
        );
        assertNotNull(
                keyPack.getKey("testUrl", timeConditions)
        );
        assertNull(
                keyPack.getKey("testUrl", timeConditions)
        );

        // Sleep 6 seconds
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertNotNull(
                keyPack.getKey("testUrl", timeConditions)
        );
        assertNotNull(
                keyPack.getKey("testUrl", timeConditions)
        );
        assertNull(
                keyPack.getKey("testUrl", timeConditions)
        );
    }
}