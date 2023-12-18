package ru.dingo.apiKeyRotator.utils;

import ru.dingo.apiKeyRotator.*;

import java.util.ArrayList;

public class CreateEndpoint {
    public static void main(String[] args) {
        ProxyStorage proxyStorage = ProxyStorage.getInstance();
        ArrayList<TimeCondition> timeConditions = new ArrayList<>();
        timeConditions.add(new TimeCondition(1, 1));

        ProxyEndpoint proxyEndpoint = new ProxyEndpoint(
                "Httpbin",
                "/bin",
                "https://httpbin.org",
                WhereKeys.PARAM,
                "token",
                timeConditions,
                "SpotifyKeys"
                );

        proxyStorage.addProxyEndpoint(proxyEndpoint);
        proxyStorage.saveToFile();

    }
}
