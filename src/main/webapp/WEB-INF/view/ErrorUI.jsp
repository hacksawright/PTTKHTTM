<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setAttribute("title", "Thông báo"); %>
<%@ include file="_header.jspf" %>
<h1>Không thể mở trang</h1>
<p class="notice alert"><%=Display.h(request.getAttribute("error"))%></p>
<% if (activeMember != null) { %>
<a class="button" href="<%=ctx%>/view/<%=activeMember instanceof model.Customer8 ? "CustomerMainUI8.jsp" : "ManagerMainUI8.jsp"%>">Về trang chính</a>
<% } else { %>
<a class="button" href="<%=ctx%>/view/login.jsp">Về trang đăng nhập</a>
<% } %>
<%@ include file="_footer.jspf" %>
