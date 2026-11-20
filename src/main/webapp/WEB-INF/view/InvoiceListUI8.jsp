<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Order8" %>
<% request.setAttribute("title", "Đơn hàng của tôi"); %>
<%@ include file="_header.jspf" %>
<%
    String statusFilter = (String) request.getAttribute("statusFilter");
    Order8[] orders = (Order8[]) request.getAttribute("orders");
%>
<a class="back" href="<%=ctx%>/view/CustomerMainUI8.jsp">← Trang chính</a>
<h1>Đơn hàng của tôi</h1>
<p class="intro">Chọn một đơn hàng để xem chi tiết và thông tin thanh toán.</p>
<nav class="tabs" aria-label="Lọc trạng thái đơn hàng">
<% for (var tab : Display.orderTabs().entrySet()) { %>
    <a <%=tab.getKey().equals(statusFilter) ? "class='active' aria-current='page'" : ""%>
       href="<%=ctx%>/view/InvoiceListUI8.jsp?statusFilter=<%=tab.getKey()%>"><%=tab.getValue()%></a>
<% } %>
</nav>
<% if (orders.length == 0) { %>
    <p class="notice"><%=Display.ALL.equals(statusFilter) ? "Bạn chưa có đơn hàng nào." : "Không có đơn hàng nào ở trạng thái đã chọn."%></p>
<% } else { %>
<div class="table-wrap"><table>
    <thead><tr><th>Mã hóa đơn</th><th>Ngày đặt</th><th>Trạng thái</th><th>Thanh toán</th><th class="number">Tổng tiền</th></tr></thead>
    <tbody>
    <% for (var order : orders) { %>
    <tr>
        <td><a href="<%=ctx%>/view/InvoiceDetailUI8.jsp?orderId=<%=order.id()%>&amp;statusFilter=<%=statusFilter%>"><%=order.code()%></a></td>
        <td><%=Display.dateTime(order.orderedAt())%></td>
        <td><span class="badge"><%=Display.h(Display.orderStatus(order.status()))%></span></td>
        <td><%=Display.h(Display.paymentStatus(order.payment().status()))%></td>
        <td class="number"><%=Display.money(order.totalAmount())%></td>
    </tr>
    <% } %>
    </tbody>
</table></div>
<% } %>
<%@ include file="_footer.jspf" %>
