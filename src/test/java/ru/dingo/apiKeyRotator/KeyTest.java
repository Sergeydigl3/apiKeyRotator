package ru.dingo.apiKeyRotator;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class KeyTest {

    @Test
    void useKey() {
        Key key = new Key("testKey", "testValue");
        ArrayList<TimeCondition> timeConditions = TimeCondition.parseTimeConditions("2/5,3/10");

        assertNotNull(
                key.useKey("testUrl", timeConditions)
        );
        assertNotNull(
                key.useKey("testUrl", timeConditions)
        );
        assertNull(
                key.useKey("testUrl", timeConditions)
        );

        // Sleep 6 seconds
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertNotNull(
                key.useKey("testUrl", timeConditions)
        );
        assertNull(
                key.useKey("testUrl", timeConditions)
        );
    }
}