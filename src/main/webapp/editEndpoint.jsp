<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Админ Панель</title>
    <link rel="stylesheet" href="/styles/edit.css">
</head>

<body>
    <div class="admin-panel">
        <nav class="sidebar">
            <div class="sidebar-header">
                Навигация
            </div>
            <ul class="sections">
                    <a style="text-decoration: none; color: inherit;" href="/admin/endpoints"><li>ENDPOINTS</li></a>
                    <a style="text-decoration: none; color: inherit;" href="/admin/keypacks"><li>KEYS PACKS</a></li></a>
                  </ul>
            <button class="logout-btn">Logout</button>
        </nav>
        <main class="content">
            <h1 class="main-title">Endpoint: ${proxyEndpoint.getFriendlyName()}</h1>
            <form class="endpoint-edit-form" method="post">
                <label for="state-endpoint">Endpoint state:</label>
                <select id="state-endpoint" name="state-endpoint">
                    <option value="Enabled" ${proxyEndpoint.isEnabled() ? 'selected' : ''} >Enabled</option>
                    <option value="Disabled" ${!proxyEndpoint.isEnabled() ? 'selected' : ''} >Disabled</option>
                </select><br><br>

                <input type="hidden" id="endpointName" name="endpointName" value="${proxyEndpoint.getFriendlyName()}">

                <label for="from">From: *</label>
                <input type="text" id="from" name="from" text value="${proxyEndpoint.getUrlFrom()}"required><br><br>

                <label for="to">To: *</label>
                <input type="text" id="to" name="to" value="${proxyEndpoint.getUrlTo()}"required><br><br>
                
                <label for="where-insert">Where insert keys:</label>
                <select id="where-insert" name="where-insert">
                    <option value="NONE">None</option>
                    <option value="PARAM">PARAM</option>
                    <option value="HEADER">HEADER</option>
                    <option value="HEADER_BEARER">HEADER BEARER</option>
                </select><br><br>
                <script>
                    document.getElementById('where-insert').value = '${proxyEndpoint.getWhereKey()}';
                </script>
                
                <label for="keyName">Key name:</label>
                <input type="text" id="keyName" name="keyName" value="${proxyEndpoint.getKeyName()}"><br><br>

                <label for="timeCondition">Time condition</label>
                <input type="text" id="timeCondition" name="timeCondition" placeholder="RPS. Example: 30/1" value="${proxyEndpoint.timeConditionsToString()}"><br><br>

                <label for="keypack">KeyPack:</label>
                <input type="text" id="keypack" name="keypack" value="${proxyEndpoint.getKeyPackToUse()}"><br><br>

                <!-- <label for="disable-keys">Disable keys:</label>
                <select id="disable-keys" name="disable-keys">
                    <option value="Enabled">Enabled</option>
                    <option value="Disabled">Disabled</option>
                </select><br>

                <label for="keyword">Keyword to disable key:</label>
                <input type="text" id="keyword" name="keyword"><br><br> -->

                <button type="submit">Update</button>
            </form>

            <form class="delete-form" method="post">
                <input type="hidden" name="_method" value="DELETE">
                <input type="hidden" id="endpointName" name="endpointName" value="${proxyEndpoint.getFriendlyName()}">
                <button type="submit" class="delete-btn">Delete endpoint</button>
            </form>

        </main>
    </div>
</body>

</html>