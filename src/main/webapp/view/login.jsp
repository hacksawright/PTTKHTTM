<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="util.Display" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Đăng nhập | ShopMan</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="login-container">
    <span class="login-brand">ShopMan</span>
    <h2>Đăng nhập hệ thống</h2>
    <p>Vui lòng nhập thông tin tài khoản</p>

    <% if (request.getAttribute("error") != null) { %>
        <p class="login-error" role="alert"><%=Display.h(request.getAttribute("error"))%></p>
    <% } %>
    <form action="${pageContext.request.contextPath}/login" method="post">

        <label for="username">Tên đăng nhập</label>
        <input id="username" type="text" name="username" autocomplete="username" maxlength="100" required>

        <label for="password">Mật khẩu</label>
        <input id="password" type="password" name="password" autocomplete="current-password" maxlength="200" required>

        <button type="submit">Đăng nhập</button>
    </form>
</div>
</body>
</html>
