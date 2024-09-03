package com.east2west.service;


import com.east2west.models.DTO.UserEmail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class SimpleEmailService implements EmailService {

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String username;



    @Override
    public String sendEmail(UserEmail user) {
        log.info("Sending emails...");
        final AtomicInteger counter = new AtomicInteger(0);
        user.getEmails().forEach(usr -> {
            try {
                Context context = new Context();
//                Map<String, Object> map = new HashMap<>();
//                map.put("avatar",);
//                map.put("username",usr.getUsername());
//                map.put("firstname", usr.getFirstName());
//                map.put("lastname", usr.getLastName());
//                map.put("phone", usr.getPhone());
//                map.put("address", usr.getPhone());
//                map.put("checkin", usr.getCheckin());
//                map.put("checkout", usr.getCheckout());
//                map.put("numberofguest", usr.getNumberofguest());
//                map.put("bookingdate", usr.getPhone());


//                map.put("fullname", StringUtils.join(Arrays.asList(usr.getFirstName(), usr.getLastName()), ' '));
//                map.put("photos",usr.getPhotos());
//                map.put("title",usr.getTitle());
//                map.put("location",usr.getLocation());
//                map.put("sign", StringUtils.join(Arrays.asList(user.getFirstName(), user.getLastName()), ' '));
//                map.put("location", user.getLocation());
//                map.put("uniqueid", UUID.randomUUID().toString());

//                context.setVariables(map);
                context.setVariable("username", usr.getUsername());
                context.setVariable("firstname", usr.getFirstName());
                context.setVariable("lastname", usr.getLastName());
                context.setVariable("phone", usr.getPhone());
                context.setVariable("address", usr.getAddress()); // Changed from usr.getPhone() to usr.getAddress()
                context.setVariable("checkin", usr.getCheckin());
                context.setVariable("checkout", usr.getCheckout());
                context.setVariable("numberofguest", usr.getNumberofguest());
                context.setVariable("fullname", StringUtils.join(Arrays.asList(usr.getFirstName(), usr.getLastName()), ' '));
                context.setVariable("photos", usr.getPhotos());
                context.setVariable("email", usr.getEmail());
                context.setVariable("title", usr.getTitle());
                context.setVariable("location", usr.getLocation());
                context.setVariable("feemount", usr.getFeeamount());
                context.setVariable("totalprice", usr.getTotalPrice());
                context.setVariable("pricepernight", usr.getPricepernight());
                context.setVariable("cleaningFee", usr.getCleaningfee());
                context.setVariable("totalpriceallday", getTotalPriceAllDays(usr.getCheckin(),usr.getCheckout(),usr.getPricepernight()) );
                context.setVariable("numberofday", getNumberOfDays(usr.getCheckin(),usr.getCheckout()));


                context.setVariable("sign", StringUtils.join(Arrays.asList(usr.getFirstName(), usr.getLastName()), ' '));



                String process = springTemplateEngine.process("welcome", context);
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
                String subject = StringUtils
                        .join(Arrays.asList("Invoice Booking of", usr.getFirstName(), usr.getLastName(), "!!!"), ' ');
                helper.setSubject(subject);
                helper.setText(process, true);
                helper.setTo(usr.getEmail());
                helper.setFrom(username);
                javaMailSender.send(mimeMessage);
            } catch (MessagingException | MailException ex) {
                log.error("Exception occured while sending email to: {} {}, due to: {}", usr.getFirstName(),
                        usr.getLastName(), ex.getMessage());
                counter.incrementAndGet();
            }
        });
        if (counter.intValue() > 0) {
            return counter.intValue() + " email(s) sending failed. Please verify logs...!!!";
        }
        return "Email(s) sent successfully, Please check your inbox...!!!";
    }

    public long getNumberOfDays(Timestamp checkin , Timestamp checkout) {
        if (checkin == null || checkout == null) {
            throw new IllegalArgumentException("Checkin and checkout dates must not be null");
        }

        LocalDate checkinDate = checkin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate checkoutDate = checkout.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return  ChronoUnit.DAYS.between(checkinDate, checkoutDate);
    }

    public BigDecimal getTotalPriceAllDays(Timestamp checkin, Timestamp checkout,BigDecimal pricepernight ) {
        if (checkin == null || checkout == null || pricepernight == null) {
            throw new IllegalArgumentException("Checkin, checkout dates, and price per night must not be null");
        }

        long numberOfDays = getNumberOfDays(checkin, checkout);
        BigDecimal totalPrice = BigDecimal.valueOf(numberOfDays).multiply(pricepernight);

        return totalPrice.setScale(2, RoundingMode.HALF_UP);
    }
}


