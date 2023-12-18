package ru.dingo.apiKeyRotator.adminPanel;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import ru.dingo.apiKeyRotator.ProxyEndpoint;
import ru.dingo.apiKeyRotator.ProxyStorage;

import java.io.IOException;

@WebServlet(name = "ViewEndpoints", value = "/admin/endpoints")
public class ViewEndpointsServlet extends HttpServlet {
    private ProxyStorage proxyStorage;

    @SneakyThrows
    public void init() {
        proxyStorage = ProxyStorage.getInstance();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("proxyEndpoints", proxyStorage.getProxyEndpoints());
        getServletContext().getRequestDispatcher("/endpoints.jsp").forward(request, response);
    }

    // Create new endpoint
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // get param from post request
        String endpointName = request.getParameter("endpoint-name");

        ProxyStorage proxyStorage = ProxyStorage.getInstance();

        ProxyEndpoint proxyEndpoint = new ProxyEndpoint();
        proxyEndpoint.setFriendlyName(endpointName);
        proxyEndpoint.setEnabled(false);

        if (proxyStorage.getProxyEndpoint(endpointName) != null) {
            response.sendError(HttpServletResponse.SC_CONFLICT);
            return;
        }
        proxyStorage.addProxyEndpoint(proxyEndpoint);
        System.out.println(endpointName);

        response.sendRedirect("/admin/endpoints");
    }
}
