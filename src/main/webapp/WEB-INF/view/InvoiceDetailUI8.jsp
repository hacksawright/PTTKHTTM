<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Order8" %>
<% request.setAttribute("title", "Chi tiết đơn hàng"); %>
<%@ include file="_header.jspf" %>
<% Order8 order = (Order8) request.getAttribute("order"); %>
<a class="back" href="<%=ctx%>/view/InvoiceListUI8.jsp?statusFilter=<%=Display.h(request.getAttribute("statusFilter"))%>">← Đơn hàng của tôi</a>
<% if (order == null) { %>
<h1>Không tìm thấy đơn hàng</h1>
<% } else { %>
<h1>Chi tiết đơn hàng <%=order.code()%></h1>
<div class="detail-grid">
    <section class="panel"><h2>Thông tin đơn</h2><dl>
        <dt>Ngày đặt</dt><dd><%=Display.dateTime(order.orderedAt())%></dd>
        <dt>Trạng thái</dt><dd><span class="badge"><%=Display.h(Display.orderStatus(order.status()))%></span></dd>
        <dt>Phương thức thanh toán</dt><dd><%=Display.h(Display.paymentMethod(order.payment().method()))%></dd>
        <dt>Trạng thái thanh toán</dt><dd><%=Display.h(Display.paymentStatus(order.payment().status()))%></dd>
    </dl></section>
    <section class="panel"><h2>Thông tin nhận hàng</h2><dl>
        <dt>Khách hàng</dt><dd><%=Display.h(order.customer().fullname())%></dd>
        <dt>Số điện thoại</dt><dd><%=Display.h(order.customer().phone())%></dd>
        <dt>Địa chỉ giao hàng</dt><dd><%=Display.h(order.shippingAddress())%></dd>
    </dl></section>
</div>
<h2>Sản phẩm trong đơn</h2>
<div class="table-wrap"><table>
    <thead><tr><th>Sản phẩm</th><th>Phân loại</th><th class="number">Số lượng</th><th class="number">Đơn giá</th><th class="number">Thành tiền</th></tr></thead>
    <tbody>
    <% for (var line : order.details()) { var product = line.product(); %>
    <tr><td><div class="product"><img src="<%=ctx%>/images/<%=Display.image(product.image())%>" alt="" width="48" height="48"><span><%=Display.h(product.name())%></span></div></td>
        <td><%=Display.h(product.category())%></td><td class="number"><%=line.quantity()%></td>
        <td class="number"><%=Display.money(line.unitPrice())%></td><td class="number"><%=Display.money(line.amount())%></td></tr>
    <% } %>
    </tbody>
    <tfoot><tr><th colspan="4">Phí giao hàng</th><td class="number"><%=Display.money(order.shippingFee())%></td></tr>
    <tr><th colspan="4">Tổng tiền</th><td class="number"><strong><%=Display.money(order.totalAmount())%></strong></td></tr></tfoot>
</table></div>
<% } %>
<%@ include file="_footer.jspf" %>
