<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setAttribute("title", "Trang chính quản lý"); %>
<%@ include file="_header.jspf" %>
<section class="hero">
    <div class="eyebrow">Tài khoản quản lý</div>
    <h1>Xin chào, <%=Display.h(activeMember.fullname())%></h1>
    <p>Xem lượng nhập theo nhà cung cấp và tra cứu các phiếu nhập trong kỳ.</p>
    <a class="button" href="<%=ctx%>/view/StatisticUI8.jsp">Thống kê <span aria-hidden="true">→</span></a>
</section>
<%@ include file="_footer.jspf" %>
