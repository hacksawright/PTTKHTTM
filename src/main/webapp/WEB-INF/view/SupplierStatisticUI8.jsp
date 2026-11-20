<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.SupplierStatistics8,java.time.LocalDate" %>
<% request.setAttribute("title", "Thống kê nhà cung cấp theo lượng nhập hàng"); %>
<%@ include file="_header.jspf" %>
<%
    var rows = (SupplierStatistics8[]) request.getAttribute("supplierStatistics");
    LocalDate startDate = (LocalDate) request.getAttribute("startDate");
    LocalDate endDate = (LocalDate) request.getAttribute("endDate");
    String startInput = request.getParameter("startDate");
    String endInput = request.getParameter("endDate");
%>
<a class="back" href="<%=ctx%>/view/StatisticUI8.jsp">← Thống kê</a>
<h1>Thống kê nhà cung cấp theo lượng nhập hàng</h1>
<form class="panel date-form" action="<%=ctx%>/view/SupplierStatisticUI8.jsp" method="get">
    <label for="startDate">Ngày bắt đầu<input id="startDate" name="startDate" type="date" value="<%=Display.h(startInput == null ? "" : startInput)%>" required></label>
    <label for="endDate">Ngày kết thúc<input id="endDate" name="endDate" type="date" value="<%=Display.h(endInput == null ? "" : endInput)%>" required></label>
    <button type="submit">Xem thống kê</button>
</form>
<% if (request.getAttribute("error") != null) { %>
    <p class="notice alert" role="alert"><%=Display.h(request.getAttribute("error"))%></p>
<% } else if (rows == null) { %>
    <p class="notice">Chọn khoảng thời gian và nhấn “Xem thống kê” để tải kết quả.</p>
<% } else if (rows.length == 0) { %>
    <p class="notice">Không có nhà cung cấp phát sinh nhập hàng trong khoảng thời gian đã chọn.</p>
<% } else { %>
    <p class="intro">Từ <strong><%=Display.date(startDate)%></strong> đến <strong><%=Display.date(endDate)%></strong> · <%=rows.length%> nhà cung cấp</p>
    <div class="table-wrap"><table>
    <thead><tr><th>Mã NCC</th><th>Nhà cung cấp</th><th class="number">Tổng số lượng nhập</th><th class="number">Tổng giá trị nhập</th><th class="number">Số lần nhập</th></tr></thead>
    <tbody>
    <% for (var row : rows) { %>
        <tr><td><%=row.supplier().code()%></td><td><a href="<%=ctx%>/view/SupplierImportInvoiceListUI8.jsp?supplierId=<%=row.supplier().id()%>&amp;startDate=<%=startDate%>&amp;endDate=<%=endDate%>"><%=Display.h(row.supplier().name())%></a></td>
        <td class="number"><%=row.totalImportQuantity()%></td><td class="number"><%=Display.money(row.totalImportAmount())%></td><td class="number"><%=row.totalImportCount()%></td></tr>
    <% } %>
    </tbody></table></div>
<% } %>
<%@ include file="_footer.jspf" %>
