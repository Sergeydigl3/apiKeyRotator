<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Selected Keypack</title>
  <link rel="stylesheet" href="/styles/selectedKeypack.css">
</head>
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
    <div class="selected-keypack">
      <div class="key-card">
        <h2>Current pack: ${keyPackName}</h2>
      </div>
      <div class="keys">
        <h3>Keys</h3>

        <c:forEach items="${keyPackKeys}" var="keyPackKey" varStatus="status">
            <div class="card">
              <div class="key-info">
                <div class="key-number">${status.index+1}</div>
                <span class="c-pill c-pill--${keyPackKey.isEnabled() ? 'success' : 'danger'}">${keyPackKey.isEnabled() ? 'Enable' : 'Disable'}</span>
                <div class="key-details">
                  ${keyPackKey.getValue()}
                </div>
                <div class="usage-count">${keyPackKey.getUsedCount()} uses</div>
                <button onclick="deleteKey('${keyPackKey.getValue()}')" class="delete-btn">Delete</button>
              </div>
            </div>
            <br/>
        </c:forEach>

      </div>
    </div>
    <button onclick="createKeyDialog()" class="add-key-btn">Add Key</button>
    <button onclick="deleteKeypack()" class="remove-btn">Remove this pack</button>
            <script>
                function createKeyDialog() {
                    const url = '/admin/keypack/';
                    const projectTitle = prompt(
                        "Enter new keypacks title"
                    );
                    if(projectTitle === null || projectTitle === "") {
                        alert("Title cannot be empty");
                        return;
                    }
                    let params = {
                        type: "key",
                        keypack: "${keyPackName}",
                        key: projectTitle,
                    };
                    const searchParams = new URLSearchParams(params).toString();

                    const xhr = new XMLHttpRequest();

                    xhr.open('POST', url, false); // Synchronous request

                    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded'); // Set the necessary Content-Type
                    xhr.send(searchParams);
                    if(xhr.status===200 && xhr.responseText !== "-1"){
                        window.location.href = '/admin/keypack/' + xhr.responseText;
                    } else alert(":(");
                }

                function deleteKeypack() {
                                    const url = '/admin/keypacks';

                                    let params = {
                                        _method: "DELETE",
                                        keypack: "${keyPackName}",
                                    };
                                    const searchParams = new URLSearchParams(params).toString();

                                    const xhr = new XMLHttpRequest();

                                    xhr.open('POST', url, false); // Synchronous request

                                    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded'); // Set the necessary Content-Type
                                    xhr.send(searchParams);
                                    if(xhr.status===200 && xhr.responseText !== "-1"){
                                        window.location.href = '/admin/keypacks';
                                    } else alert(":(");
                                }


                function deleteKey(value) {
                                const url = '/admin/keypack/';

                                let params = {
                                    _method: "DELETE",
                                    keypack: "${keyPackName}",
                                    key: value,
                                };
                                const searchParams = new URLSearchParams(params).toString();

                                const xhr = new XMLHttpRequest();

                                xhr.open('POST', url, false); // Synchronous request

                                xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded'); // Set the necessary Content-Type
                                xhr.send(searchParams);
                                if(xhr.status===200 && xhr.responseText !== "-1"){
                                    location.reload();
                                } else alert(":(");
                            }
            </script>
  </main>
</div>
</body>
</html>
