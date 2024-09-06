package com.east2west.controllers;



import org.springframework.beans.factory.annotation.Autowired;
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
import java.io.UnsupportedEncodingException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.east2west.models.DTO.*;
import com.east2west.models.Entity.Homestay;
import com.east2west.models.Entity.HomestayAvailability;
import com.east2west.models.Entity.User;
import com.east2west.service.EmailService;
import com.east2west.service.HomestayService;
import com.east2west.service.UserService;
import jakarta.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    public Rental saveRental(@RequestBody RentalDTO rentalDTO) {
        return rentalCarService.saveRental(rentalDTO);
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
        vnp_Params.put("vnp_Amount", String.valueOf((int) (amount * 100))); // Convert to VND
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang: " + rentalDTO.toParamString());
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_ReturnUrl", PaymentConfig.vnp_ReturnUrl);
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
            // Lưu thông tin thanh toán thành công vào cơ sở dữ liệu
            // Xử lý các bước như gửi email nếu cần
        }
        String responseUrl = "http://localhost:3000/rentals";
        response.sendRedirect(responseUrl);
    }
}
