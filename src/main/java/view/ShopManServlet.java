package view;

import dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Customer8;
import model.Manager8;
import model.Member8;
import util.Display;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@WebServlet(urlPatterns = {
        "/login", "/logout", "/view/CustomerMainUI8.jsp", "/view/InvoiceListUI8.jsp",
        "/view/InvoiceDetailUI8.jsp", "/view/ManagerMainUI8.jsp", "/view/StatisticUI8.jsp",
        "/view/SupplierStatisticUI8.jsp", "/view/SupplierImportInvoiceListUI8.jsp",
        "/view/ImportInvoiceDetailUI8.jsp"
})
public final class ShopManServlet extends HttpServlet {
    private static final String CUSTOMER_MAIN = "CustomerMainUI8.jsp";
    private static final String MANAGER_MAIN = "ManagerMainUI8.jsp";
    private static final String INVOICE_LIST = "InvoiceListUI8.jsp";
    private static final String INVOICE_DETAIL = "InvoiceDetailUI8.jsp";
    private static final String STAT_MENU = "StatisticUI8.jsp";
    private static final String SUPPLIER_STAT = "SupplierStatisticUI8.jsp";
    private static final String SUPPLIER_HISTORY = "SupplierImportInvoiceListUI8.jsp";
    private static final String IMPORT_DETAIL = "ImportInvoiceDetailUI8.jsp";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        res.setHeader("Cache-Control", "no-store");
        if ("/logout".equals(req.getServletPath())) {
            var session = req.getSession(false);
            if (session != null) session.invalidate();
            res.sendRedirect(req.getContextPath() + "/view/login.jsp");
            return;
        }
        if (!"/login".equals(req.getServletPath())) {
            res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        try {
            String username = req.getParameter("username");
            Member8 member = username == null || username.isBlank() || username.length() > 100 ? null
                    : new MemberDAO8().authenticate(username.trim(), req.getParameter("password"));
            if (member == null) {
                req.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng.");
                req.getRequestDispatcher("/view/login.jsp").forward(req, res);
                return;
            }
            var previous = req.getSession(false);
            if (previous != null) previous.invalidate();
            req.getSession().setAttribute("member", member);
            res.sendRedirect(req.getContextPath() + "/view/" + (member instanceof Customer8 ? CUSTOMER_MAIN : MANAGER_MAIN));
        } catch (SQLException e) {
            unavailable(req, res, e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setHeader("Cache-Control", "no-store");
        String route = req.getServletPath();
        if ("/login".equals(route)) {
            res.sendRedirect(req.getContextPath() + "/view/login.jsp");
            return;
        }
        if ("/logout".equals(route)) {
            res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        var session = req.getSession(false);
        if (session == null || !(session.getAttribute("member") instanceof Member8 member)) {
            res.sendRedirect(req.getContextPath() + "/view/login.jsp");
            return;
        }
        String page = route.substring("/view/".length());
        boolean customerPage = CUSTOMER_MAIN.equals(page) || INVOICE_LIST.equals(page) || INVOICE_DETAIL.equals(page);
        if (customerPage ? !(member instanceof Customer8) : !(member instanceof Manager8)) {
            failure(req, res, 403, "Bạn không có quyền mở trang này.");
            return;
        }
        req.setAttribute("member", member);
        try {
            switch (page) {
                case INVOICE_LIST -> listOrders(req, member.id());
                case INVOICE_DETAIL -> showOrder(req, res, member.id());
                case SUPPLIER_STAT -> statistics(req);
                case SUPPLIER_HISTORY -> history(req, res);
                case IMPORT_DETAIL -> showImport(req, res);
                default -> { }
            }
            forward(req, res, page);
        } catch (IllegalArgumentException e) {
            failure(req, res, 400, e.getMessage());
        } catch (SQLException e) {
            unavailable(req, res, e);
        }
    }

    private void listOrders(HttpServletRequest req, int customerId) throws SQLException {
        String filter = orderFilter(req);
        req.setAttribute("statusFilter", filter);
        req.setAttribute("orders", new OrderDAO8().getInvoiceListByCustomer(customerId, filter));
    }

    private void showOrder(HttpServletRequest req, HttpServletResponse res, int customerId) throws SQLException {
        int orderId = positiveId(req, "orderId");
        var order = new DetailOrderDAO8().getInvoiceDetail(orderId, customerId);
        req.setAttribute("order", order);
        req.setAttribute("statusFilter", orderFilter(req));
        if (order == null) res.setStatus(404);
    }

    private static String orderFilter(HttpServletRequest req) {
        String filter = req.getParameter("statusFilter");
        return filter != null && Display.orderTabs().containsKey(filter) ? filter : Display.ALL;
    }

    private void statistics(HttpServletRequest req) throws SQLException {
        if (req.getParameter("startDate") == null && req.getParameter("endDate") == null) return;
        Period period;
        try {
            period = period(req);
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            return;
        }
        req.setAttribute("supplierStatistics", new SupplierDAO8()
                .getSupplierStatisticsByImportQuantity(period.startDate(), period.endDate()));
    }

    private void history(HttpServletRequest req, HttpServletResponse res) throws SQLException {
        int supplierId = positiveId(req, "supplierId");
        Period period = period(req);
        var invoices = new ImportInvoiceDAO8().getImportInvoicesBySupplier(supplierId, period.startDate(), period.endDate());
        req.setAttribute("importInvoices", invoices);
        if (invoices.length == 0) res.setStatus(404);
    }

    private void showImport(HttpServletRequest req, HttpServletResponse res) throws SQLException {
        int importInvoiceId = positiveId(req, "importInvoiceId");
        int supplierId = positiveId(req, "supplierId");
        Period period = period(req);
        var invoice = new ImportInvoiceDetailDAO8().getImportInvoiceDetail(importInvoiceId);
        boolean inScope = invoice != null && invoice.supplier().id() == supplierId
                && "COMPLETED".equals(invoice.status())
                && !invoice.date().isBefore(period.startDate()) && !invoice.date().isAfter(period.endDate());
        if (!inScope) res.setStatus(404);
        req.setAttribute("importInvoice", inScope ? invoice : null);
    }

    private static Period period(HttpServletRequest req) {
        String start = req.getParameter("startDate"), end = req.getParameter("endDate");
        if (start == null || end == null || start.isBlank() || end.isBlank()) {
            throw new IllegalArgumentException("Vui lòng nhập đủ ngày bắt đầu và ngày kết thúc.");
        }
        try {
            LocalDate startDate = LocalDate.parse(start), endDate = LocalDate.parse(end);
            if (startDate.isAfter(endDate)) throw new IllegalArgumentException("Ngày bắt đầu không được lớn hơn ngày kết thúc.");
            if (startDate.getYear() < 1000 || endDate.getYear() > 9999) {
                throw new IllegalArgumentException("Ngày thống kê nằm ngoài phạm vi được hỗ trợ.");
            }
            req.setAttribute("startDate", startDate);
            req.setAttribute("endDate", endDate);
            return new Period(startDate, endDate);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Ngày thống kê không hợp lệ.");
        }
    }

    private static int positiveId(HttpServletRequest req, String name) {
        try {
            int value = Integer.parseInt(req.getParameter(name));
            if (value > 0) return value;
        } catch (NumberFormatException ignored) {
            // Show the same validation message for missing and malformed IDs.
        }
        throw new IllegalArgumentException("Mã cần tra cứu không hợp lệ.");
    }

    private void unavailable(HttpServletRequest req, HttpServletResponse res, SQLException e)
            throws ServletException, IOException {
        log("ShopMan database request failed", e);
        failure(req, res, 503, "Không thể tải dữ liệu từ MySQL. Vui lòng thử lại.");
    }

    private static void failure(HttpServletRequest req, HttpServletResponse res, int status, String message)
            throws ServletException, IOException {
        res.setStatus(status);
        req.setAttribute("error", message);
        forward(req, res, "ErrorUI.jsp");
    }

    private static void forward(HttpServletRequest req, HttpServletResponse res, String page)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/view/" + page).forward(req, res);
    }

    private record Period(LocalDate startDate, LocalDate endDate) {}
}
