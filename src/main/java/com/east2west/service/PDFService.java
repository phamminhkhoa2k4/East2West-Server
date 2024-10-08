package com.east2west.service;

import com.east2west.models.Entity.Rental;
import com.east2west.models.Entity.User;
import com.east2west.repository.PaymentRepository;
import com.east2west.repository.RentalRepository;
import com.east2west.repository.UserRepository;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import java.util.Optional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PDFService {

    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private UserRepository userRepository;
    // public ByteArrayInputStream printRentalCarById(int id) throws IOException {
    // Optional<Rental> rentalOptional = rentalRepository.findById(id);

    // if (rentalOptional.isPresent()) {
    // return generateRentalPDF(rentalOptional.get());
    // } else {
    // // Handle the case where rental ID does not exist (you could throw an
    // exception or return a default PDF)
    // throw new RuntimeException("Rental with ID " + id + " not found.");
    // }
    // }
    public User getUser(int id) {
        return userRepository.getById(id);
    }

    public ByteArrayInputStream generateRentalPDF(int rentalId) throws IOException {
        // Giả sử bạn đã có logic để lấy đối tượng Rental từ cơ sở dữ liệu
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid rental ID"));
    
        // Tạo một ByteArrayOutputStream để chứa nội dung PDF
        ByteArrayOutputStream out = new ByteArrayOutputStream();
    
        try (PdfWriter writer = new PdfWriter(out);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {
    
            document.add(new Paragraph("Rental Details"));
            // Thêm thông tin chi tiết cho Rental vào PDF
            document.add(new Paragraph("Rental ID: " + rental.getRentalid()));
            document.add(new Paragraph("User Email: " + getUser(rental.getUserid()).getEmail()));
            // Tiếp tục thêm các trường khác...
    
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to generate PDF", e);
        }
    
        return new ByteArrayInputStream(out.toByteArray());
    }


    public LocalDateTime convertToLocalDateTime(java.sql.Date dateToConvert) {
        return dateToConvert.toLocalDate().atStartOfDay();
    }

    // Helper method to format dates
}