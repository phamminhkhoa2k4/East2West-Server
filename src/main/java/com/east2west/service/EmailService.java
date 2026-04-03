package com.east2west.service;


import com.east2west.models.DTO.CarDTO;
import com.east2west.models.DTO.UserEmail;
import com.east2west.models.Entity.Car;
import com.east2west.util.DateUtil;
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
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class EmailService {


    private final SpringTemplateEngine templateEngine;

    private final  JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    public EmailService(SpringTemplateEngine templateEngine, JavaMailSender mailSender) {
        this.templateEngine = templateEngine;
        this.mailSender = mailSender;
    }


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

                String htmlContent = templateEngine.process("welcome", context);
                String subject = StringUtils
                        .join(Arrays.asList("Invoice Booking of", usr.getFirstName(), usr.getLastName(), "!!!"), ' ');
                sendEmail(usr.getEmail(),username,subject,htmlContent);
            } catch (MailException ex) {
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


    public void sendQuoteViaEmail(String email, CarDTO car,LocalDate dropOffDate,LocalDate pickUpDate,String pickUpTime,String dropOffTime) {
        Context context = new Context();
        int days = (int) DateUtil.getDiffInDays(pickUpDate, dropOffDate);

        DateTimeFormatter formatterFull = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH);

        String[] fuels = car.getFuel().split(",");

        List<Map<String, String>> fields = new ArrayList<>();


            fields.add(Map.of("icon", "https://res.cloudinary.com/djddnvjpi/image/upload/v1757260318/east2west/aoho2doum7vn4ieohnim.png", "label", car.getSeatCapacity() + " seats"));
            fields.add(Map.of("icon", "https://res.cloudinary.com/djddnvjpi/image/upload/v1757254283/east2west/gscyufyjmladvl8lw8vb.png", "label", car.getCargearbox()));
            fields.add(Map.of("icon", "https://res.cloudinary.com/djddnvjpi/image/upload/v1757261972/east2west/wuwhlpqqbh9r2phricwd.png", "label", car.getFueltankcapacity() + "L"));
        for (String fuel : fuels) {
            String icon;
            if(fuel.equals("electric")){
                icon = "https://res.cloudinary.com/djddnvjpi/image/upload/v1757260253/east2west/zfi4xv005xlcqfkqaqre.png";
            } else {
                icon = "https://res.cloudinary.com/djddnvjpi/image/upload/v1757260185/east2west/iegxmfdlkihlkghdcfln.png";
            }
            fields.add(Map.of("icon", icon, "label", fuel.trim()));
        }
        if (car.isAirConditioned()) {
            fields.add(Map.of("icon", "https://res.cloudinary.com/djddnvjpi/image/upload/v1757260150/east2west/zw8x8eb9jmcnwbfb5ogj.png", "label", "Air Condition"));
        }
        if (car.isFourDoorsOrMore()) {
            fields.add(Map.of("icon", "https://res.cloudinary.com/djddnvjpi/image/upload/v1757260202/east2west/yayscevybuynoanffuyp.png", "label", "+4 Doors"));
        }

        if (car.getLargeLuggage() > 0) {
            fields.add(Map.of("icon", "https://res.cloudinary.com/djddnvjpi/image/upload/v1757260080/east2west/i0xejpwsnubehij5lmh3.png", "label", car.getLargeLuggage() + " Large Luggage"));
        }

        if (car.getSmallLuggage() > 0) {
            fields.add(Map.of("icon", "https://res.cloudinary.com/djddnvjpi/image/upload/v1757259454/east2west/u0jlmysdqpyxpr8ffapt.png", "label", car.getSmallLuggage() + " Small Luggage"));
        }


            fields.add(Map.of("icon", "https://res.cloudinary.com/djddnvjpi/image/upload/v1757259684/east2west/xaytutweqkyvyasfomxi.png", "label", car.getMiles() + " Mileage"));
        String location =car.getLocation() + ", " + car.getWardName() + ", " + car.getDistrictName() + ", " + car.getCityProvinceName();

        List<List<Map<String, String>>> rows = new ArrayList<>();
        for (int i = 0; i < fields.size(); i += 2) {
            rows.add(fields.subList(i, Math.min(i + 2, fields.size())));
        }
        context.setVariable("rows", rows);
        context.setVariable("thumbnail", car.getThumbnail().get(0));
        context.setVariable("makeLogo", car.getMake().getLogo());
        context.setVariable("engineSystem", car.getEngineSystem());
        context.setVariable("engineSystemIcon", "https://res.cloudinary.com/djddnvjpi/image/upload/v1757260339/east2west/efpbpiwgqkvs0qbi6zbu.png");
        context.setVariable("typeName", car.getType().getTypename());
        context.setVariable("typeIcon", car.getType().getTypeicon());
        context.setVariable("days",days);
        context.setVariable("pickUpDate", pickUpDate.format(formatterFull));
        context.setVariable("dropOffDate", dropOffDate.format(formatterFull));
        context.setVariable("price",car.getPricePerDay() * days);
        context.setVariable("dropOffTime",dropOffTime);
        context.setVariable("pickUpTime", pickUpTime);
        context.setVariable("location",location);
        String htmlContent = templateEngine.process("car-quote-template", context);
        sendEmail(email,username,"Quote Form East2West",htmlContent);
    }


    private void sendEmail(String to,String from, String subject,String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setSubject(subject);
            helper.setTo(to);
            helper.setFrom(from);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}


