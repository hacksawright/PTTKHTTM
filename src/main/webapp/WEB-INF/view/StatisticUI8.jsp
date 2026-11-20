<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setAttribute("title", "Thống kê"); %>
<%@ include file="_header.jspf" %>
<a class="back" href="<%=ctx%>/view/ManagerMainUI8.jsp">← Trang chính</a>
<h1>Thống kê</h1>
<div class="menu-grid">
  <a class="menu-card" href="<%=ctx%>/view/SupplierStatisticUI8.jsp">
    <strong>Thống kê nhà cung cấp theo lượng nhập hàng</strong>
    <span>Xem tổng lượng, giá trị và số lần nhập trong kỳ. →</span>
  </a>
  <div class="menu-card disabled"><strong>Thống kê hàng nhập</strong><span>Chưa triển khai</span></div>
  <div class="menu-card disabled"><strong>Thống kê hàng xuất</strong><span>Chưa triển khai</span></div>
  <div class="menu-card disabled"><strong>Thống kê khách hàng</strong><span>Chưa triển khai</span></div>
</div>
<%@ include file="_footer.jspf" %>
