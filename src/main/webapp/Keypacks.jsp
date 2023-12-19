<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Keypacks</title>
    <link rel="stylesheet" href="/styles/keypack.css">
</head>
<body>
<div class="admin-panel">
    <nav class="sidebar">
        <div class="sidebar-header">
            Навигация
        </div>
        <ul class="sections">
            <a style="text-decoration: none; color: inherit;" href="/admin/endpoints"><li>ENDPOINTS</li></a>
            <a style="text-decoration: none; color: inherit;" href="/admin/keypacks"><li>Keypacks</li></a>
        </ul>
        <button class="logout-btn">Logout</button>
    </nav>
    <main class="content">
        <div class="keypacks">
            <h2>Keypacks</h2>
            <!-- Ваш список keypacks здесь -->
            <div class="keypacks-list">
                <c:forEach items="${MapKeyPacks}" var="tkeypack" varStatus="status">
                    <a style="text-decoration: none; color: inherit;" href="/admin/keypack/${tkeypack.key}" class="card">
                        <div class="card-header">
                            <div class="card-title">
                                <div class="card-number">${status.index+1}.</div>
                                <div class="card-name">${tkeypack.key}</div>
                            </div>
                        </div>
                    </a>
                </c:forEach>

            </div>
        </div>
        <form class="endpoint-form" method="post">
                <label for="keypack-name">Название Endpoint:</label>
                <input type="text" id="keypack-name" name="keypack-name">
                <button type="submit">Создать</button>
              </form>
    </main>
</div>
</body>
</html>
