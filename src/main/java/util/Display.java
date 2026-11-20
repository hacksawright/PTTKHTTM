package util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public final class Display {
    public static final String ALL = "ALL";
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private Display() {}

    public static Map<String, String> orderTabs() {
        var tabs = new LinkedHashMap<String, String>();
        tabs.put(ALL, "Tất cả");
        tabs.put("PENDING", "Chờ xác nhận");
        tabs.put("READY_FOR_PICKUP", "Chờ lấy hàng");
        tabs.put("DELIVERING", "Chờ giao hàng");
        tabs.put("COMPLETED", "Hoàn thành");
        return tabs;
    }

    public static String orderStatus(String status) {
        return status == null ? "—" : orderTabs().getOrDefault(status, status);
    }

    public static String paymentStatus(String status) {
        if ("PAID".equals(status)) return "Đã thanh toán";
        if ("UNPAID".equals(status)) return "Chưa thanh toán";
        return status == null ? "—" : status;
    }

    public static String paymentMethod(String method) {
        if ("COD".equals(method)) return "Thanh toán khi nhận hàng";
        if ("BANK_TRANSFER".equals(method)) return "Chuyển khoản";
        return method == null ? "—" : method;
    }

    public static String date(LocalDate date) { return date == null ? "—" : DATE.format(date); }
    public static String dateTime(LocalDateTime dateTime) { return dateTime == null ? "—" : TIME.format(dateTime); }

    public static String money(BigDecimal value) {
        return NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN")).format(value) + " đ";
    }

    public static String h(Object value) {
        if (value == null) return "—";
        return value.toString().replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;");
    }

    public static String image(String value) {
        if (value != null && value.matches("[a-z0-9-]+\\.svg")) return value;
        return "product.svg";
    }
}
