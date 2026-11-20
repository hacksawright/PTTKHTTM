# ShopMan

Dự án Java 17, Servlet, JSP và MySQL cung cấp hai chức năng tra cứu:

- **Khách hàng** mở **Đơn hàng của tôi**, xem các đơn của mình với tab mặc định **Tất cả**, lọc theo trạng thái và mở chi tiết đơn.
- **Nhân viên quản lý** mở **Thống kê → Thống kê nhà cung cấp theo lượng nhập hàng**, chọn ngày, mở danh sách phiếu của một nhà cung cấp rồi xem chi tiết phiếu.

Các mục thống kê khác xuất hiện ở menu để khớp giao diện chọn loại báo cáo. Chúng không có luồng xử lý.

## Chạy dự án

1. Khởi động **MySQL 8** tại cổng **3306**. Mặc định ứng dụng dùng database **pttkht**, tài khoản **root** và mật khẩu MySQL **1234** của môi trường bài tập.
2. Các tệp SQL ở thư mục [SQL_ShopMan](../SQL_ShopMan/) bên cạnh `prj_shopman`.
   - Nếu máy chưa có schema, chạy `01_schema_pttkht.sql` trước.
   - Chạy `02_seed_pttkht.sql` một lần trên schema mẫu trống. Script dùng ID cố định để khớp báo cáo. Nếu các tài khoản demo đã có thì bỏ qua bước này. Không chạy với tùy chọn `--force`.
   - Có thể mở từng tệp trong MySQL Workbench và thực thi toàn bộ tệp theo thứ tự.
3. Trong thư mục `prj_shopman`, chạy `mvn package`. Maven tạo `target/prj_shopman.war`.
4. Deploy WAR lên **Tomcat 10.1** với context path `/prj_shopman`. Có thể dùng IntelliJ **Tomcat Server → Local → Deployment** và chọn artifact WAR, hoặc đặt WAR vào thư mục `webapps` của Tomcat.
5. Mở [ShopMan tại localhost:8080](http://localhost:8080/prj_shopman/). Nếu cấu hình Tomcat dùng cổng khác, đổi phần cổng trong đường dẫn.

Có thể ghi đè thông tin MySQL bằng các biến môi trường của **tiến trình Tomcat**: `SHOPMAN_DB_URL`, `SHOPMAN_DB_USER` và `SHOPMAN_DB_PASSWORD`. Tài khoản đăng nhập web tách biệt với tài khoản kết nối MySQL.

## Tài khoản và dữ liệu mẫu

| Tên đăng nhập | Mật khẩu mẫu | Kết quả để đối chiếu |
|---|---|---|
| `customer8` | `ShopMan8!` | HD003, HD002, HD001 theo ngày mới nhất trước. Tab **Chờ xác nhận** có HD003. Chi tiết HD003 có hai sản phẩm và tổng **59.000 đ**. |
| `manager8` | `ShopMan8!` | Từ **01/07/2026 đến 30/09/2026**, NCC005 có **592 đơn vị**, **20.802.000 đ** và **3 phiếu**. PN0015 có 4 dòng, tổng **230 đơn vị** và **8.753.000 đ**. |
| `customer2` | `ShopMan8!` | Chỉ thấy đơn HD004, dùng để đối chiếu việc tách đơn theo tài khoản. |

Seed lưu mật khẩu web dưới dạng PBKDF2. Ảnh minh họa sản phẩm nằm trong `src/main/webapp/images`.

## Đối chiếu với sơ đồ thiết kế

| JSP | Lớp và lời gọi DAO |
|---|---|
| `InvoiceListUI8.jsp` | `OrderDAO8.getInvoiceListByCustomer(int customerId, String statusFilter)` → `Order8[]` |
| `InvoiceDetailUI8.jsp` | `DetailOrderDAO8.getInvoiceDetail(int orderId, int customerId)` → `Order8` |
| `SupplierStatisticUI8.jsp` | `SupplierDAO8.getSupplierStatisticsByImportQuantity(LocalDate startDate, LocalDate endDate)` → `SupplierStatistics8[]` |
| `SupplierImportInvoiceListUI8.jsp` | `ImportInvoiceDAO8.getImportInvoicesBySupplier(int supplierId, LocalDate startDate, LocalDate endDate)` → `ImportInvoice8[]` |
| `ImportInvoiceDetailUI8.jsp` | `ImportInvoiceDetailDAO8.getImportInvoiceDetail(int importInvoiceId)` → `ImportInvoice8` |

Các trang vào là `CustomerMainUI8.jsp` cho khách hàng và `ManagerMainUI8.jsp → StatisticUI8.jsp` cho quản lý. File JSP của hai module đặt trong `WEB-INF/view`, còn URL giữ tên tương ứng ở `/view/`.

### Điểm cần cập nhật nếu vẽ đúng từng đối tượng chạy trong code

- `view.ShopManServlet` nhận yêu cầu, kiểm tra phiên đăng nhập và dữ liệu đầu vào, gọi đúng DAO trong bảng, rồi chuyển kết quả đến JSP. Giữ nguyên năm chữ ký DAO. Nếu sơ đồ tuần tự thể hiện từng đối tượng triển khai, thêm lifeline `ShopManServlet` giữa thao tác giao diện và DAO. Các bước mô tả “Trang JSP gọi DAO” trong V3 tương ứng với servlet phục vụ trang đó.
- `DAO` dùng `#openConnection() : Connection`. Mỗi truy vấn đóng kết nối sau khi dùng. Đổi thuộc tính `-con : Connection` trên lớp cha DAO thành thao tác này nếu muốn mô hình khớp code. DAO có thể ném `SQLException`, được servlet xử lý.
- `MemberDAO8.authenticate(String username, String password)` phục vụ đăng nhập. Đăng nhập là tiền điều kiện của hai sơ đồ tuần tự module.
- `Order8` và `ImportInvoice8` nhận tổng tiền đã tính từ dữ liệu chi tiết. Tổng tiền không có cột riêng trong CSDL. Model tra cứu chỉ dùng các quan hệ phù hợp với hai module. Chẳng hạn `Order8` không ánh xạ nhân viên xử lý đơn, và thông tin xác thực được xử lý trong `MemberDAO8` thay vì lưu trong đối tượng phiên đăng nhập.

## Quy tắc dữ liệu

- `ALL` chỉ là giá trị bộ lọc. Các trạng thái đơn lưu trong MySQL gồm `PENDING` (Chờ xác nhận), `READY_FOR_PICKUP` (Chờ lấy hàng), `DELIVERING` (Chờ giao hàng) và `COMPLETED` (Hoàn thành).
- Truy vấn thống kê và danh sách phiếu cùng tính phiếu nhập `COMPLETED` trong khoảng bao gồm cả hai ngày biên. Khi mở lịch sử và quay lại, khoảng ngày đã áp dụng được giữ trong tham số URL.
- Mã `HD003`, `NCC005` và `PN0015` được định dạng từ ID. Chi tiết đơn dùng địa chỉ trên đơn và tên, số điện thoại từ tài khoản khách hàng theo schema hiện tại.
