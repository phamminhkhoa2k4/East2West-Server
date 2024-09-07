package com.east2west.service;

import com.east2west.models.Entity.Rental;
import com.east2west.models.Entity.User;
import com.east2west.repository.PaymentRepository;
import com.east2west.repository.RentalRepository;
import com.east2west.repository.UserRepository;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

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

    public ByteArrayInputStream generateRentalPDF(Rental rental) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Create PDF writer
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Add Title
        Paragraph title = new Paragraph("Rental Details")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(18)
                .setFontColor(ColorConstants.BLACK);
        document.add(title);

        // Add Table for Rental Details
        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        table.addCell("Field");
        table.addCell("Value");
        table.addCell("Rental ID");
        table.addCell(String.valueOf(rental.getRentalid()));
        table.addCell("User Email");
        table.addCell(String.valueOf(getUser(rental.getUserid()).getEmail()));

        table.addCell("Car");
        table.addCell(rental.getCar().getCarName());

        table.addCell("Payment Method");
        table.addCell(String.valueOf(rental.getPayment().getPaymentMethod()));

        table.addCell("Rental Date");
        table.addCell(String.valueOf(rental.getRentalDate()));

        table.addCell("Return Date");
        table.addCell(String.valueOf(rental.getReturnDate()));

        table.addCell("Total Amount");
        table.addCell(String.valueOf(rental.getTotalAmount()));

        document.add(table);

        document.close();
        return new ByteArrayInputStream(out.toByteArray());
    }
}