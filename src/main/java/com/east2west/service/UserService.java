package com.east2west.service;

import com.east2west.models.DTO.SignupRequest;
import com.east2west.models.DTO.VerificationCodeData;
import com.east2west.models.Entity.ERole;
import com.east2west.models.Entity.Role;
import com.east2west.models.payload.request.UpdateProfileRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.east2west.models.Entity.User;
import com.east2west.repository.*;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    private final PasswordResetTokenRepository tokenRepository;

    private final JavaMailSender mailSender;

    @Value("${app.verification-code.expiry-minutes}")
    private int codeExpiryMinutes;


    @Value("${app.url}")
    private String appUrl;

    private final Map<String, VerificationCodeData> verificationCodes = new ConcurrentHashMap<>();

    // sms

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.tokenRepository = tokenRepository;
        this.mailSender = mailSender;
    }

    // forgot password

    public void sendVerificationCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String code = generateVerificationCode();
        verificationCodes.put(email, new VerificationCodeData(code, LocalDateTime.now()));

        sendVerificationEmail(email, code);
    }

    public boolean verifyCode(String email, String code) {
        VerificationCodeData savedCode = verificationCodes.get(email);
        if (savedCode == null) {
            return false;
        }

        if (LocalDateTime.now().isAfter(savedCode.getCreatedAt().plusMinutes(codeExpiryMinutes))) {
            verificationCodes.remove(email);
            return false;
        }

        return savedCode.getCode().equals(code);
    }

    public void resetPassword(String email, String code, String newPassword, User user) {
        if (!verifyCode(email, code)) {
            throw new RuntimeException("Invalid verification code");
        }

        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);

        verificationCodes.remove(email);
    }

    private String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    private void sendVerificationEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Mã xác thực đặt lại mật khẩu");
        message.setText("Mã xác thực của bạn là: " + code + "\nMã này sẽ hết hạn sau " + codeExpiryMinutes + " phút.");
        mailSender.send(message);
    }



    // change password


    public void createPasswordResetTokenForUser(User user) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken myToken = new PasswordResetToken();
        myToken.setUser(user);
        myToken.setToken(token);
        tokenRepository.save(myToken);

        // Gửi email
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Đặt lại mật khẩu");
        email.setText("Để đặt lại mật khẩu, vui lòng nhấp vào link sau:\n"
                + appUrl + "/reset-password?token=" + token);
        mailSender.send(email);
    }

    public String validatePasswordResetToken(String token) {
        final PasswordResetToken passToken = tokenRepository.findByToken(token);

        if (passToken == null) {
            return "invalidToken";
        }

        final Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            tokenRepository.delete(passToken);
            return "expired";
        }

        return null;
    }

    @Transactional
    public void changeUserPassword(User user, String newPassword) {
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(user);
        cleanupUserTokens(user);
    }

    public User getUserByPasswordResetToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if (resetToken == null) {
            throw new TokenNotFoundException("Invalid password reset token");
        }
        return resetToken.getUser();
    }



    public static class TokenNotFoundException extends RuntimeException {
        public TokenNotFoundException(String message) {
            super(message);
        }
    }

    // Chạy lúc 00:00 mỗi ngày
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupExpiredTokens() {
        Date now = new Date();
        tokenRepository.deleteByExpiryDateLessThan(now);
        log.info("Cleaned up expired password reset tokens");
    }

    // Xóa tất cả token khi user thay đổi mật khẩu thành công
    public void cleanupUserTokens(User user) {
        tokenRepository.deleteByUser(user);
        log.info("Cleaned up all password reset tokens for user: {}", user.getEmail());
    }

    // sms
    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhone(phoneNumber);
    }
    public void sendOtp(String toPhoneNumber, String otp) throws UnsupportedEncodingException {
        Twilio.init(accountSid, authToken);
        Message.creator(
                new PhoneNumber("+84787998419"),
                new PhoneNumber("+17409084843"),
                "Your OTP is: " + otp
        ).create();
//        String twimlUrl = "http://twimlets.com/echo?Twiml=" +
//                java.net.URLEncoder.encode(
//                        "<Response><Say language='vi-VN' voice='alice'>Mã OTP của bạn là " + otp + ". Vui lòng không chia sẻ mã này với bất kỳ ai.</Say></Response>",
//                        "UTF-8"
//                );
//
//        // Tạo cuộc gọi
//        Call call = Call.creator(
//                new PhoneNumber("+84 787 998 419"), // Số điện thoại nhận cuộc gọi
//                new PhoneNumber("+17409084843"), // Số Twilio (Mỹ)
//                URI.create(twimlUrl)              // Nội dung cuộc gọi
//        ).create();
//
//        System.out.println("Cuộc gọi OTP đã được thực hiện, SID: " + call.getSid());
    }

    private final Map<String, String> otpStorage = new HashMap<>();

    public void storeOtp(String phoneNumber, String otp) {
        otpStorage.put(phoneNumber, otp);
    }

    public String getOtp(String phoneNumber) {
        return otpStorage.get(phoneNumber);
    }

    public void clearOtp(String phoneNumber) {
        otpStorage.remove(phoneNumber);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public Boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public Boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public boolean existsByPhone(String phone){
        return userRepository.existsByPhone(phone);
    }

    public void registerUser(SignupRequest signUpRequest){
        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(encoder.encode(signUpRequest.getPassword()))
                .firstname(signUpRequest.getFirstname())
                .lastname(signUpRequest.getLastname())
                .phone(signUpRequest.getPhone())
                .address(signUpRequest.getAddress())
                .build();

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole  = roleRepository.findByRoleName(ERole.USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            switch (strRoles.iterator().next()) {
                case "business":
                    Role businessRole = roleRepository.findByRoleName(ERole.BUSINESS)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(businessRole);
                    break;
                case "mod":
                    Role modRole = roleRepository.findByRoleName(ERole.MODERATOR)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(modRole);
                    break;
                case "employee":
                    Role employeeRole = roleRepository.findByRoleName(ERole.EMPLOYEE)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(employeeRole);
                    break;
                default:
                    Role userRole = roleRepository.findByRoleName(ERole.USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
            }
        }

        user.setRoles(roles);
        userRepository.save(user);
    }

    public void updateUser(User existingUser, UpdateProfileRequest updateRequest) {
        existingUser.setUsername(updateRequest.getUsername());
        existingUser.setFirstname(updateRequest.getFirstname());
        existingUser.setLastname(updateRequest.getLastname());
        existingUser.setEmail(updateRequest.getEmail());
        existingUser.setPhone(updateRequest.getPhone());
        existingUser.setAddress(updateRequest.getAddress());
        userRepository.save(existingUser);
    }

    public  Optional<User> findByEmail(String email) {
       return userRepository.findByEmail(email);
    }

    public Role findByRoleName(ERole eRole) {
        Optional<Role> role = roleRepository.findByRoleName(eRole);
        return role.orElse(null);
    }

    public User save(User user) {
        return userRepository.save(user);
    }



}
