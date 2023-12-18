<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Админ Панель</title>
  <link rel="stylesheet" href="/styles/endpoints.css">
</head>
<body>
  <div class="admin-panel">
    <nav class="sidebar">
      <div class="sidebar-header">
        Навигация
      </div>
      <ul class="sections">
        <a style="text-decoration: none; color: inherit;" href="/admin/endpoints"><li>ENDPOINTS</li></a>
        <a style="text-decoration: none; color: inherit;" href="/admin/endpoints"><li>KEYS PACKS</a></li></a>
      </ul>
      <button class="logout-btn">Logout</button>
    </nav>
    <main class="content">
        <c:forEach var="endpoint" items="${proxyEndpoints}" varStatus="status">
        <a href="/admin/endpoint/${endpoint.getFriendlyName()}" class="card-links">
                <div class="card">
                        <div class="card-header">
                          <div class="card-title">
                            <div class="card-number">${status.index+1}</div>
                            <div class="card-name">${endpoint.getFriendlyName()}</div>
                          </div>
                        </div>
                        <div class="card-details">
                          <div class="detail">
                            <span class="detail-label">FROM:</span>
                            <span class="detail-value">${endpoint.getUrlFrom()}</span>
                          </div>
                          <div class="detail">
                            <span class="detail-label">TO:</span>
                            <span class="detail-value">${endpoint.getUrlTo()}</span>
                          </div>
                          <div class="detail">
                            <span class="detail-label">Keypack:</span>
                            <span class="detail-value">${endpoint.getKeyPackToUse()}</span>
                          </div>
                        </div>
                      </div>
        </a>

        </c:forEach>

      <form class="endpoint-form" method="post">
        <label for="endpoint-name">Название Endpoint:</label>
        <input type="text" id="endpoint-name" name="endpoint-name">
        <button type="submit">Создать</button>
      </form>

    </main>
  </div>
</body>
</html>