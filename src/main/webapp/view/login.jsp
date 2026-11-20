<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng nhập</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="login-container">
    <h2>Đăng nhập hệ thống</h2>
    <p>Vui lòng nhập thông tin tài khoản</p>

    <form action="login.jsp" method="post">

        <label> Tên đăng nhập *</label>
        <input type="text" name="username" placeholder="Nhập tên đăng nhập" required>

        <label> Mật khẩu *</label>
        <input type="password" name="password" placeholder="Nhập mật khẩu" required>

        <button type="submit">Đăng nhập</button>
    </form>
</div>
</body>
</html>
