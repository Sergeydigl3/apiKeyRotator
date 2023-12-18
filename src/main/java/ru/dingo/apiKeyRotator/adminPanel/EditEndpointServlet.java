package ru.dingo.apiKeyRotator.adminPanel;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.dingo.apiKeyRotator.ProxyEndpoint;
import ru.dingo.apiKeyRotator.ProxyStorage;
import ru.dingo.apiKeyRotator.TimeCondition;
import ru.dingo.apiKeyRotator.WhereKeys;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "EditEndpoint", value = "/admin/endpoint/*")
public class EditEndpointServlet extends HttpServlet {

    private ProxyStorage proxyStorage;

    public void init() {
        proxyStorage = ProxyStorage.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // parse endpoint name from url
        // example: /admin/endpoint/endpointName
        String endpointName = req.getPathInfo().substring(1);
        System.out.println(endpointName);
        ProxyEndpoint proxyEndpoint = proxyStorage.getProxyEndpoint(endpointName);
        if (proxyEndpoint == null) {
            System.out.println("ProxyEndpoint not found");
            return;
        }
        req.setAttribute("proxyEndpoint", proxyEndpoint);
        getServletContext().getRequestDispatcher("/editEndpoint.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("_method") != null && request.getParameter("_method").equals("DELETE")) {
            doDelete(request, response);
            return;
        }

        // Retrieving parameters from the form
        String endpointName = request.getParameter("endpointName");
        String stateEndpoint = request.getParameter("state-endpoint");
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        String whereInsertStr = request.getParameter("where-insert");
        String keyName = request.getParameter("keyName");
        String timeConditionStr = request.getParameter("timeCondition");
        String keypack = request.getParameter("keypack");

        // Check all for not null
        if (endpointName == null || stateEndpoint == null || from == null || to == null || whereInsertStr == null || keyName == null || timeConditionStr == null || keypack == null) {
            System.out.println("Some parameter is null");
            return;
        }

        System.out.println("Endpoint Name: " + endpointName);
        System.out.println("Endpoint State: " + stateEndpoint);
        System.out.println("From: " + from);
        System.out.println("To: " + to);
        System.out.println("Where Insert Keys: " + whereInsertStr);
        System.out.println("Key Name: " + keyName);
        System.out.println("Time Condition: " + timeConditionStr);
        System.out.println("KeyPack: " + keypack);

        // Parse time condition
        ArrayList<TimeCondition> timeConditions = TimeCondition.parseTimeConditions(timeConditionStr);

        WhereKeys whereInsert;
        // Parse where insert
        switch (whereInsertStr) {
            case "PARAM":
                whereInsert = WhereKeys.PARAM;
                break;
            case "HEADER":
                whereInsert = WhereKeys.HEADER;
                break;
            case "HEADER_BEARER":
                whereInsert = WhereKeys.HEADER_BEARER;
                break;
            default:
                whereInsert = WhereKeys.NONE;
        }
        ProxyEndpoint proxyEndpoint = proxyStorage.getProxyEndpoint(endpointName);
        if (proxyEndpoint == null) {
            System.out.println("ProxyEndpoint not found");
            return;
        }
//        ProxyEndpoint proxyEndpoint = new ProxyEndpoint(
//                endpointName,
//                from,
//                to,
//                whereInsert,
//                keyName,
//                timeConditions,
//                keypack
//        );
        proxyEndpoint.setUrlFrom(from);
        proxyEndpoint.setUrlTo(to);
        proxyEndpoint.setWhereKey(whereInsert);
        proxyEndpoint.setKeyName(keyName);
        proxyEndpoint.setTimeConditions(timeConditions);
        proxyEndpoint.setKeyPackToUse(keypack);

        proxyEndpoint.setEnabled(stateEndpoint.equals("Enabled"));
//        proxyStorage.addProxyEndpoint(proxyEndpoint);
        System.out.println("ProxyEndpoint: " + proxyEndpoint.toString());

        // Redirect to same page
        response.sendRedirect("/admin/endpoint/" + endpointName);
//        response.sendRedirect("/admin/endpoints");
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieving parameters from the form
        String endpointName = request.getParameter("endpointName");
        if (endpointName == null) {
            System.out.println("Endpoint name is null");
            return;
        }
        System.out.println("Endpoint Name: " + endpointName);
        proxyStorage.getProxyEndpoints().remove(proxyStorage.getProxyEndpoint(endpointName));

        response.sendRedirect("/admin/endpoints");
    }
}
