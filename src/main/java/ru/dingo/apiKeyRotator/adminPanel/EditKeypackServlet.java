package ru.dingo.apiKeyRotator.adminPanel;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.dingo.apiKeyRotator.*;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "EditKeypack", value = "/admin/keypack/*")
public class EditKeypackServlet extends HttpServlet {

    private KeysStorage keysStorage;

    public void init() {
        keysStorage = KeysStorage.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // parse endpoint name from url
        // example: /admin/endpoint/endpointName
        String endpointName = req.getPathInfo().substring(1);
        System.out.println(endpointName);
        KeyPack keyPack = keysStorage.getKeyPack(endpointName);
        if (keyPack == null) {
            System.out.println("KeyPack not found");
            return;
        }
        req.setAttribute("keyPackKeys", keyPack.getKeys());
//        keyPack.getKeys().forEach(key -> System.out.println(key.isEnabled()));
        req.setAttribute("keyPackName", endpointName);
        getServletContext().getRequestDispatcher("/oneKeypack.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.printf("\n\n\nPOST request to %s%n", request.getRequestURI());
        // Check if DELETE request
        if (request.getParameter("_method") != null && request.getParameter("_method").equals("DELETE")) {
            doDelete(request, response);
            return;
        }

        // Retrieving parameters from the form
        String keypack = request.getParameter("keypack");
        String keyValue = request.getParameter("key");

        // Check all for not null
        if (keypack == null || keyValue == null) {
            System.out.println("Some parameters are null");
            return;
        }
        // Check for empty
        if (keypack.isEmpty() || keyValue.isEmpty()) {
            System.out.println("Some parameters are empty");
            return;
        }

        System.out.println("KeyPack: " + keypack);
        System.out.println("Key: " + keyValue);

        // Create key for keypack
        KeyPack keyPack = keysStorage.getKeyPack(keypack);
        if (keyPack == null) {
            System.out.println("KeyPack not found");
            return;
        }
        Key key = new Key(keyValue, "Key");
        keyPack.addKey(key);

        // Redirect to same page
//        response.sendRedirect("/admin/endpoint/" + keypack);
        response.getWriter().println(keypack);
//        response.sendRedirect("/admin/endpoints");
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Delete key from keypack

        String keypack = request.getParameter("keypack");
        String key = request.getParameter("key");

        if (keypack == null || key == null) {
            System.out.println("Some parameters are null");
            return;
        }
        if (keypack.isEmpty() || key.isEmpty()) {
            System.out.println("Some parameters are empty");
            return;
        }

        KeyPack keyPack = keysStorage.getKeyPack(keypack);
        if (keyPack == null) {
            System.out.println("KeyPack not found");
            return;
        }

        keyPack.getKeys().removeIf(key1 -> key1.getValue().equals(key));
    }

}
