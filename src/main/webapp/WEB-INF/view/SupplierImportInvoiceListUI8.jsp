<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.ImportInvoice8,java.time.LocalDate" %>
<% request.setAttribute("title", "Các lần nhập hàng của nhà cung cấp"); %>
<%@ include file="_header.jspf" %>
<%
    var importInvoices = (ImportInvoice8[]) request.getAttribute("importInvoices");
    LocalDate startDate = (LocalDate) request.getAttribute("startDate");
    LocalDate endDate = (LocalDate) request.getAttribute("endDate");
%>
<a class="back" href="<%=ctx%>/view/SupplierStatisticUI8.jsp?startDate=<%=startDate%>&amp;endDate=<%=endDate%>">← Kết quả thống kê</a>
<% if (importInvoices.length == 0) { %>
<h1>Không có phiếu nhập trong kỳ</h1>
<% } else { var supplier = importInvoices[0].supplier(); %>
<h1><%=Display.h(supplier.name())%></h1>
<p class="intro"><%=supplier.code()%> · Từ <%=Display.date(startDate)%> đến <%=Display.date(endDate)%></p>
<section class="panel"><h2>Thông tin nhà cung cấp</h2><dl>
    <dt>Điện thoại</dt><dd><%=Display.h(supplier.phone())%></dd>
    <dt>Email</dt><dd><%=Display.h(supplier.email())%></dd>
    <dt>Địa chỉ</dt><dd><%=Display.h(supplier.address())%></dd>
    <dt>Người liên hệ</dt><dd><%=Display.h(supplier.contactPerson())%></dd>
</dl></section>
<h2>Các lần nhập hàng (<%=importInvoices.length%>)</h2>
<div class="table-wrap"><table>
<thead><tr><th>Mã phiếu nhập</th><th>Ngày nhập</th><th class="number">Số lượng</th><th class="number">Tổng tiền</th></tr></thead>
<tbody>
<% for (var invoice : importInvoices) { %>
<tr><td><a href="<%=ctx%>/view/ImportInvoiceDetailUI8.jsp?importInvoiceId=<%=invoice.id()%>&amp;supplierId=<%=supplier.id()%>&amp;startDate=<%=startDate%>&amp;endDate=<%=endDate%>"><%=invoice.code()%></a></td>
    <td><%=Display.date(invoice.date())%></td><td class="number"><%=invoice.totalQuantity()%></td><td class="number"><%=Display.money(invoice.totalAmount())%></td></tr>
<% } %>
</tbody></table></div>
<% } %>
<%@ include file="_footer.jspf" %>
