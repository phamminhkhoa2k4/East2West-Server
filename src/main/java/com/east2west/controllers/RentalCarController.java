package com.east2west.controllers;


import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.east2west.config.PaymentConfig;
import com.east2west.models.Entity.Rental;
import com.east2west.service.PDFService;
import com.east2west.service.RentalCarService;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.UnsupportedEncodingException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import com.east2west.models.DTO.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/rental")
public class RentalCarController {

    private final RentalCarService rentalCarService;
    private final PDFService pdfService;

    public RentalCarController(RentalCarService rentalCarService, PDFService pdfService) {
        this.rentalCarService = rentalCarService;
        this.pdfService = pdfService;
    }

    @GetMapping
    public List<RentalFetch> getAllBookings() {
        return rentalCarService.getAllRentals();
    }

    @PostMapping
    public ResponseEntity<?> saveRental(@RequestBody RentalDTO rentalDTO) {
        try {
            Rental rental = rentalCarService.saveRental(rentalDTO);
            return new ResponseEntity<>(rental, HttpStatus.CREATED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new ResponseEntity<>(Map.of("status", "error", "message", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Log the exception details for debugging
            e.printStackTrace();
            return new ResponseEntity<>(Map.of("status", "error", "message", "An unexpected error occurred."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{userId}")
    public List<Rental> getRentalsByUserId(@PathVariable int userId) {
        return rentalCarService.getRentalsByUserId(userId);
    }

    @PostMapping("/pdf/{rentalid}")
    public ResponseEntity<?> downloadRentalPDF(@PathVariable int rentalid) {
        Rental rental = rentalCarService.findById(rentalid);
        if (rental == null) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayInputStream bis;
        try {
            bis = pdfService.generateRentalPDF(rental);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error generating PDF: " + e.getMessage());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=rental_" + rentalid + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @PostMapping("/create_payment/{amount}")
    public ResponseEntity<?> createPayment(@PathVariable double amount, @RequestBody RentalDTO rentalDTO)
            throws UnsupportedEncodingException {
        String orderType = "other";
        String bankCode = "NCB";
        String vnp_TxnRef = PaymentConfig.getRandomNumber(8);
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", PaymentConfig.vnp_Version);
        vnp_Params.put("vnp_Command", PaymentConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", PaymentConfig.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf((int) (amount * 2200))); // Convert to VND
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang: " + rentalDTO.toParamString());
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_ReturnUrl", "http://localhost:8080/api/rental/payment_infor");
        vnp_Params.put("vnp_IpAddr", "127.0.0.1");

        Calendar cld = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString())).append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (!fieldName.equals(fieldNames.get(fieldNames.size() - 1))) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = PaymentConfig.hmacSHA512(PaymentConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = PaymentConfig.vnp_PayUrl + "?" + queryUrl;

        PaymentResDTO paymentResDTO = new PaymentResDTO();
        paymentResDTO.setStatus("OK");
        paymentResDTO.setMessage("Successfully");
        paymentResDTO.setURL(paymentUrl);

        return ResponseEntity.status(HttpStatus.OK).body(paymentResDTO);
    }

    @GetMapping("/payment_infor")
    public void transaction(
            @RequestParam(value = "vnp_Amount") String amount,
            @RequestParam(value = "vnp_BankCode") String bankCode,
            @RequestParam(value = "vnp_OrderInfo") String order,
            @RequestParam(value = "vnp_ResponseCode", required = false) String responseCode,
            HttpServletResponse response) throws IOException {

        if (responseCode != null && responseCode.equals("00")) {
            RentalDTO rentalDTO = processOrderInfo(order);

            // Gọi dịch vụ để lưu thông tin thanh toán vào cơ sở dữ liệu
            rentalCarService.saveRental(rentalDTO);
        }
        String responseUrl = "http://localhost:3000/rentals";
        response.sendRedirect(responseUrl);
    }

    public RentalDTO processOrderInfo(String orderInfo) {
        // In ra chuỗi orderInfo để kiểm tra định dạng
        System.out.println("Order Info: " + orderInfo);

        // Biểu thức chính quy để phân tích chuỗi orderInfo
        Pattern pattern = Pattern.compile(
            "User ID: (\\d+), Car ID: (\\d+), Payment ID: (\\d+), Rental Date: (\\d{14}), Return Date: (\\d{14}), Total Amount: ([\\d.]+)"
        );
        
        Matcher matcher = pattern.matcher(orderInfo);

        RentalDTO rentalDTO = new RentalDTO();

        if (matcher.find()) {
            try {
                // Lấy các nhóm khớp từ biểu thức chính quy
                rentalDTO.setUserId(Integer.parseInt(matcher.group(1)));
                rentalDTO.setCarId(Integer.parseInt(matcher.group(2)));
                rentalDTO.setPaymentId(Integer.parseInt(matcher.group(3)));

                // Chuyển đổi chuỗi ngày giờ từ định dạng yyyyMMddHHmmss thành java.sql.Timestamp
                rentalDTO.setRentalDate(convertToSqlDate(parseDate(matcher.group(4))));
                rentalDTO.setReturnDate(convertToSqlDate(parseDate(matcher.group(5))));

                // Chuyển đổi tổng số tiền thành double
                rentalDTO.setTotalAmount(Double.parseDouble(matcher.group(6)));
            } catch (NumberFormatException e) {
                // Xử lý lỗi khi không thể chuyển đổi giá trị thành số hoặc ngày giờ
                System.err.println("Error parsing value: " + e.getMessage());
            }
        } else {
            // Nếu không tìm thấy kết quả khớp với biểu thức chính quy
            System.err.println("No match found for the order info: " + orderInfo);
        }

        return rentalDTO;
    }

    private Timestamp parseDate(String dateStr) {   
        // Chuyển đổi định dạng yyyyMMddHHmmss thành Timestamp
        try {
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
            java.util.Date parsedDate = dateFormat.parse(dateStr);
            return new Timestamp(parsedDate.getTime());
        } catch (java.text.ParseException e) {
            System.err.println("Date parse error: " + e.getMessage());
            return null;
        }
    }

    private java.sql.Date convertToSqlDate(java.sql.Timestamp timestamp) {
        return new java.sql.Date(timestamp.getTime());
    }
}
