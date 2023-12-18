package ru.dingo.apiKeyRotator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@WebServlet(name = "ReverseProxy", value = "/")
public class ReverseProxyServlet extends HttpServlet {
    String remoteHost;
    ProxyStorage proxyStorage;
    KeysStorage keysStorage;

    public void init() {
        proxyStorage = ProxyStorage.getInstance();
        keysStorage = KeysStorage.getInstance();
        keysStorage.runBackgroundSaver();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Reverse proxy


        // create request to remote server

        ProxyEndpoint proxyEndpoint = null;
        for (ProxyEndpoint tempProxyEndpoint : proxyStorage.proxyEndpoints) {
            if (!tempProxyEndpoint.isEnabled()) continue;
            if (request.getRequestURI().startsWith(tempProxyEndpoint.getUrlFrom())) {
                proxyEndpoint = tempProxyEndpoint;
                break;
            }
        }
        boolean isReferer = false;
        if (proxyEndpoint == null && request.getHeader("Referer") != null) {
            String referer = request.getHeader("Referer");
            URL refererUrl = new URL(referer);
            for (ProxyEndpoint tempProxyEndpoint : proxyStorage.proxyEndpoints) {
                if (!tempProxyEndpoint.isEnabled()) continue;
                if (refererUrl.getFile().startsWith(tempProxyEndpoint.getUrlFrom())) {
                    proxyEndpoint = tempProxyEndpoint;
                    isReferer = true;
                    break;
                }
            }
        }

        if (proxyEndpoint == null) {
            // check for referer
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String finalUrl;
        String queryString = request.getQueryString();
        KeyPack keyPack = keysStorage.getKeyPack(proxyEndpoint.getKeyPackToUse());
        String token = "errorToken";
        if (keyPack == null) {
            if (proxyEndpoint.getWhereKey() == WhereKeys.HEADER || proxyEndpoint.getWhereKey() == WhereKeys.HEADER_BEARER) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        } else {
            Key key = keyPack.getKey(proxyEndpoint.urlTo, proxyEndpoint.getTimeConditions());
            if (key == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            token = key.getValue();
        }


        if (queryString == null) queryString = "";
        if (proxyEndpoint.getWhereKey() == WhereKeys.PARAM) {
            queryString = replaceParamOrAdd(queryString, proxyEndpoint.getKeyName(), token);
        }
        String urlPath = request.getRequestURI() + (queryString.isEmpty() ? "" : "?" + queryString);

        if (!isReferer) {
            finalUrl = proxyEndpoint.getUrlTo() + urlPath.substring(proxyEndpoint.getUrlFrom().length());
        } else {
            finalUrl = proxyEndpoint.getUrlTo() + urlPath;
        }



        URL url = new URL(finalUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();


        // Preserve the original request method
        connection.setRequestMethod(request.getMethod());

        // Copy request headers
        request.getHeaderNames().asIterator()
                .forEachRemaining(headerName -> {
                    connection.setRequestProperty(headerName, request.getHeader(headerName));
                });

        if (proxyEndpoint.getWhereKey() == WhereKeys.HEADER) {
            connection.setRequestProperty(proxyEndpoint.getKeyName(), token);
        } else if (proxyEndpoint.getWhereKey() == WhereKeys.HEADER_BEARER) {
            connection.setRequestProperty("Authorization", "Bearer " + token);
        }

        // Copy request content for POST, PUT, etc.
        if (request.getMethod().equals("POST") || request.getMethod().equals("PUT")) {
            connection.setDoOutput(true);
            try (OutputStream out = connection.getOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                InputStream requestInput = request.getInputStream();
                while ((bytesRead = requestInput.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
        }

        // Copy response headers
        for (String header : connection.getHeaderFields().keySet()) {
            if (header != null) {
                for (String value : connection.getHeaderFields().get(header)) {
                    response.addHeader(header, value);
                }
            }
        }

        // Copy response content
        try (InputStream in = connection.getInputStream();
             OutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
    }

    public void destroy() {
    }

    private String replaceParamOrAdd(String url, String param, String value) {
        if (url.contains(param)) {
            return url.replaceAll(param + "=[^&]*", param + "=" + value);
        } else {
            return url + (url.contains("?") ? "?" : "&") + param + "=" + value;
        }
    }
}