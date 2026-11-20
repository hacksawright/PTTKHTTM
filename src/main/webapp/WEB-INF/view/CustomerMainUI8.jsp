<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setAttribute("title", "Trang chính khách hàng"); %>
<%@ include file="_header.jspf" %>
<section class="hero">
    <div class="eyebrow">Tài khoản khách hàng</div>
    <h1>Xin chào, <%=Display.h(activeMember.fullname())%></h1>
    <p>Xem các đơn hàng đã đặt và theo dõi trạng thái giao hàng, thanh toán của từng đơn.</p>
    <a class="button" href="<%=ctx%>/view/InvoiceListUI8.jsp">Đơn hàng của tôi <span aria-hidden="true">→</span></a>
</section>
<%@ include file="_footer.jspf" %>
