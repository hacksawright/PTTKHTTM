<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.ImportInvoice8,java.time.LocalDate" %>
<% request.setAttribute("title", "Chi tiết phiếu nhập"); %>
<%@ include file="_header.jspf" %>
<%
    ImportInvoice8 invoice = (ImportInvoice8) request.getAttribute("importInvoice");
    LocalDate startDate = (LocalDate) request.getAttribute("startDate");
    LocalDate endDate = (LocalDate) request.getAttribute("endDate");
%>
<a class="back" href="<%=ctx%>/view/SupplierImportInvoiceListUI8.jsp?supplierId=<%=Display.h(request.getParameter("supplierId"))%>&amp;startDate=<%=startDate%>&amp;endDate=<%=endDate%>">← Các lần nhập hàng</a>
<% if (invoice == null) { %>
<h1>Không tìm thấy phiếu nhập trong kỳ</h1>
<% } else { %>
<h1>Chi tiết phiếu nhập <%=invoice.code()%></h1>
<section class="panel"><h2>Thông tin phiếu nhập</h2><dl>
    <dt>Ngày nhập</dt><dd><%=Display.date(invoice.date())%></dd>
    <dt>Nhà cung cấp</dt><dd><%=Display.h(invoice.supplier().name())%></dd>
    <dt>Người lập</dt><dd><%=Display.h(invoice.createdBy().fullname())%></dd>
</dl></section>
<h2>Sản phẩm nhập</h2>
<div class="table-wrap"><table>
<thead><tr><th>Mã hàng</th><th>Sản phẩm</th><th>Đơn vị</th><th class="number">Số lượng</th><th class="number">Đơn giá nhập</th><th class="number">Thành tiền</th></tr></thead>
<tbody>
<% for (var line : invoice.details()) { var product = line.product(); %>
<tr><td><%="SP%03d".formatted(product.id())%></td>
    <td><div class="product"><img src="<%=ctx%>/images/<%=Display.image(product.image())%>" alt="" width="48" height="48"><span><%=Display.h(product.name())%></span></div></td>
    <td><%=Display.h(product.unit())%></td><td class="number"><%=line.quantity()%></td><td class="number"><%=Display.money(line.unitPrice())%></td>
    <td class="number"><%=Display.money(line.amount())%></td></tr>
<% } %>
</tbody><tfoot><tr><th colspan="3">Tổng cộng</th><td class="number"><strong><%=invoice.totalQuantity()%></strong></td><td></td><td class="number"><strong><%=Display.money(invoice.totalAmount())%></strong></td></tr></tfoot>
</table></div>
<% } %>
<%@ include file="_footer.jspf" %>
