package com.east2west.controllers;


import com.east2west.config.PaymentConfig;
import com.east2west.models.DTO.*;
import com.east2west.models.Entity.Homestay;
import com.east2west.models.Entity.HomestayAvailability;
import com.east2west.models.Entity.User;
import com.east2west.service.EmailService;
import com.east2west.service.HomestayService;
import com.east2west.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/api/homestays/bookings")
public class BookingHomestayController {

    private final HomestayService homestayService;


    private final EmailService emailService;


    private  final UserService userService;

    @Autowired
    public BookingHomestayController(HomestayService homestayService, EmailService emailService, UserService userService) {
        this.homestayService = homestayService;
        this.emailService = emailService;
        this.userService = userService;
    }
    @PostMapping
    public ResponseEntity<String> createBookingHomestay(@RequestBody BookingHomestayDTO bookingHomestayDTO) {
        homestayService.createBooking(bookingHomestayDTO);
        return ResponseEntity.ok("Booking Homestay created successfully!");
    }
    @PostMapping("/create_payment/{amount}")
    public ResponseEntity<?> createPayment(@PathVariable long amount , @RequestBody BookingHomestayDTO bookingHomestayDTO) throws UnsupportedEncodingException {



        String orderType = "other";
//        long amount = Integer.parseInt(req.getParameter("amount"))*100;
//        String bankCode = req.getParameter("bankCode");
        String bankCode = "NCB";


        String vnp_TxnRef = PaymentConfig.getRandomNumber(8);
//        String vnp_IpAddr = Config.getIpAddress(req);

        String vnp_TmnCode = PaymentConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", PaymentConfig.vnp_Version);
        vnp_Params.put("vnp_Command", PaymentConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
        vnp_Params.put("vnp_CurrCode", "VND");

        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + bookingHomestayDTO.toParamString());
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_OrderType", orderType);

//        String locate = req.getParameter("language");
//        if (locate != null && !locate.isEmpty()) {
//            vnp_Params.put("vnp_Locale", locate);
//        } else {
//            vnp_Params.put("vnp_Locale", "vn");
//        }
        vnp_Params.put("vnp_ReturnUrl", PaymentConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", "127.0.0.1");

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
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

        // Logging for debugging
        System.out.println("VNP Params: " + vnp_Params);
        System.out.println("Hash Data: " + hashData.toString());
        System.out.println("Query URL: " + queryUrl);
        System.out.println("Payment URL: " + paymentUrl);

        return ResponseEntity.status(HttpStatus.OK).body(paymentResDTO);
    }


    @GetMapping("/payment_infor")
    public void transaction(
            @RequestParam(value = "vnp_Amount") String amount,
            @RequestParam(value = "vnp_BankCode") String bankCode,
            @RequestParam(value = "vnp_OrderInfo") String order,
            @RequestParam(value = "vnp_ResponseCode", required = false) String responseCode,
            HttpServletResponse response
    ) throws IOException, MessagingException {


        if (responseCode != null && responseCode.equals("00")) {
            BookingHomestayDTO bookingHomestayDTO = parseOrderInfo(order);
            homestayService.createBooking(bookingHomestayDTO);
            Optional<User> userAuth =  userService.getUserById(bookingHomestayDTO.getUserId());
            Optional<HomestayAvailability> homestayAvailability = homestayService.getHomeAvailabilityById(bookingHomestayDTO.getHomestayavailabilityId());
            Homestay homestay = homestayService.getHomestayById(homestayAvailability.get().getHomestay().getHomestayid());
            if(userAuth.isPresent()){
                UserEmail userEmail = getUserEmail(userAuth , bookingHomestayDTO , homestay ,homestayAvailability);
                emailService.sendEmail(userEmail);
            }

        }
        String responseUrl = "http://localhost:3000/homestays";
        response.sendRedirect(responseUrl);
    }

    private static UserEmail getUserEmail(Optional<User> userAuth , BookingHomestayDTO bookingInfo , Homestay homestay, Optional<HomestayAvailability> homestayAvailability) {
        User userEntity = userAuth.get();
        UserEmail userEmail = new UserEmail();
        userEmail.setFirstName(userEntity.getFirstname());
        userEmail.setLastName(userEntity.getLastname());


        TargetEmail targetEmail = new TargetEmail(userEntity.getFirstname(), userEntity.getLastname(), userEntity.getEmail(), userEntity.getUsername(),userEntity.getPhone(),userEntity.getAddress(),bookingInfo.getCheckin(),bookingInfo.getCheckout(),bookingInfo.getFeeamount(),bookingInfo.getNumberofguest(),bookingInfo.getTotalPrice(), homestay.getPhotos().get(0),homestay.getAddress(),homestay.getAddress(),homestay.getCleaningfee(),homestayAvailability.get().getPricepernight());
        List<TargetEmail> emailList = new ArrayList<>();
        emailList.add(targetEmail);
        userEmail.setEmails(emailList);
        return userEmail;
    }


    private BookingHomestayDTO parseOrderInfo(String orderInfo) {

        BookingHomestayDTO dto = new BookingHomestayDTO();

        Pattern pattern = Pattern.compile("Homestay Availability ID: (\\d+), User ID: (\\d+), Check-in: ([^,]+), Check-out: ([^,]+), Fee Amount: ([^,]+), Status: ([^,]*), Number of Guests: (\\d+), Total Price: ([\\d]+)");
        Matcher matcher = pattern.matcher(orderInfo);

        if (matcher.find()) {
            dto.setHomestayavailabilityId(Integer.parseInt(matcher.group(1)));
            dto.setUserId(Integer.parseInt(matcher.group(2)));
            dto.setCheckin(Timestamp.valueOf(matcher.group(3)));
            dto.setCheckout(Timestamp.valueOf(matcher.group(4)));
            dto.setFeeamount(new BigDecimal(matcher.group(5)));
            dto.setStatus(matcher.group(6));
            dto.setNumberofguest(Integer.parseInt(matcher.group(7)));
            dto.setTotalPrice(new BigDecimal(matcher.group(8)));


        }

        return dto;
    }

}
