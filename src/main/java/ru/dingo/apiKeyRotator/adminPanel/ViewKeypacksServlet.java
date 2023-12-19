package ru.dingo.apiKeyRotator.adminPanel;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import ru.dingo.apiKeyRotator.KeyPack;
import ru.dingo.apiKeyRotator.KeysStorage;
import ru.dingo.apiKeyRotator.ProxyEndpoint;
import ru.dingo.apiKeyRotator.ProxyStorage;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "ViewKeypacks", value = "/admin/keypacks")
public class ViewKeypacksServlet extends HttpServlet {
    private KeysStorage keysStorage;

    @SneakyThrows
    public void init() {
        keysStorage = KeysStorage.getInstance();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("MapKeyPacks", keysStorage.getKeyPacks());
        getServletContext().getRequestDispatcher("/Keypacks.jsp").forward(request, response);
    }

    // Create new endpoint
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (request.getParameter("_method") != null && request.getParameter("_method").equals("DELETE")) {
            doDelete(request, response);
            return;
        }
        // get param from post request
        String keypackName = request.getParameter("keypack-name");

//        ProxyStorage proxyStorage = ProxyStorage.getInstance();
        KeysStorage proxyStorage = KeysStorage.getInstance();
        KeyPack keyPack = new KeyPack();
        keyPack.setKeys(new ArrayList<>());


        if (proxyStorage.getKeyPack(keypackName) != null) {
            response.sendError(HttpServletResponse.SC_CONFLICT);
            return;
        }

        System.out.println(keypackName);
        proxyStorage.addKeyPack(keypackName, keyPack);
        response.sendRedirect("/admin/keypacks");
    }

    // Delete endpoint
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.printf("DELETE request to %s%n", request.getRequestURI());
        // parse endpoint name from url
        // example: /admin/endpoint/endpointName
        String keypack = request.getParameter("keypack");
        if (keypack == null) {
            System.out.println("keypack is null");
            return;
        }

        System.out.println(keypack);

        keysStorage.getKeyPacks().remove(keypack);

        response.sendRedirect("/admin/keypacks");
    }
}
