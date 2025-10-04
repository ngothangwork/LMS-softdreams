package dev.thangngo.lmssoftdreams.consumer;

import dev.thangngo.lmssoftdreams.dtos.request.borrow.BorrowSearchRequest;
import dev.thangngo.lmssoftdreams.dtos.response.borrow.BorrowResponse;
import dev.thangngo.lmssoftdreams.services.BorrowService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.type.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import static dev.thangngo.lmssoftdreams.config.RabbitMQConfig.BORROW_EXPORT_QUEUE;

@Service
public class BorrowExportConsumer {

    private final BorrowService borrowService;

    public BorrowExportConsumer(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @RabbitListener(queues = BORROW_EXPORT_QUEUE)
    public void handleBorrowExport(BorrowSearchRequest request) {
        System.out.println("Nhận request: " + request);
        try {
            Pageable pageable = PageRequest.of(0, 1000);
            List<BorrowResponse> borrows = borrowService.searchBorrows(request, pageable);

            JasperDesign jasperDesign = new JasperDesign();
            jasperDesign.setName("BorrowsReport");
            jasperDesign.setPageWidth(595);
            jasperDesign.setPageHeight(842);

            JRDesignBand titleBand = new JRDesignBand();
            titleBand.setHeight(40);

            JRDesignStaticText title = new JRDesignStaticText();
            title.setText("Báo cáo mượn sách");
            title.setX(200);
            title.setY(10);
            title.setWidth(200);
            title.setHeight(30);
            title.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
            titleBand.addElement(title);

            jasperDesign.setTitle(titleBand);

            JRDesignBand columnHeader = new JRDesignBand();
            columnHeader.setHeight(20);
            columnHeader.addElement(createHeader("ID", 0, 50));
            columnHeader.addElement(createHeader("Borrow Date", 50, 90));
            columnHeader.addElement(createHeader("Return Date", 140, 90));
            columnHeader.addElement(createHeader("Book Name", 230, 170));
            columnHeader.addElement(createHeader("Username", 400, 90));
            columnHeader.addElement(createHeader("Status", 490, 80));
            jasperDesign.setColumnHeader(columnHeader);

            JRDesignBand detailBand = new JRDesignBand();
            detailBand.setHeight(20);
            JRDesignTextField idField = createField("id", Long.class, 0, 50);
            idField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
            detailBand.addElement(idField);
            JRDesignTextField borrowDateField = createFormattedDateField("borrowDate", 50, 90);
            detailBand.addElement(borrowDateField);

            JRDesignTextField returnDateField = createFormattedDateField("returnDate", 140, 90);
            detailBand.addElement(returnDateField);


            detailBand.addElement(createField("bookName", String.class, 230, 170));

            JRDesignTextField usernameField = createField("username", String.class, 400, 90);
            usernameField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
            detailBand.addElement(usernameField);

            detailBand.addElement(createField("status", String.class, 490, 80));
            ((JRDesignSection) jasperDesign.getDetailSection()).addBand(detailBand);

            jasperDesign.addField(createJRField("id", Long.class));
            jasperDesign.addField(createJRField("borrowDate", java.time.LocalDate.class));
            jasperDesign.addField(createJRField("returnDate", java.time.LocalDate.class));
            jasperDesign.addField(createJRField("bookName", String.class));
            jasperDesign.addField(createJRField("username", String.class));
            jasperDesign.addField(createJRField("status", String.class));

            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(borrows);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);

            File exportDir = new File("exports/borrows");
            if (!exportDir.exists()) exportDir.mkdirs();

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "borrows_" + timestamp + ".pdf";
            File outputFile = new File(exportDir, fileName);

            try (OutputStream out = new FileOutputStream(outputFile)) {
                JasperExportManager.exportReportToPdfStream(jasperPrint, out);
            }

            System.out.println(outputFile.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JRDesignStaticText createHeader(String text, int x, int width) {
        JRDesignStaticText header = new JRDesignStaticText();
        header.setText(text);
        header.setX(x);
        header.setY(0);
        header.setWidth(width);
        header.setHeight(20);
        header.setBold(true);
        header.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        header.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
        header.setBackcolor(new Color(220, 220, 220));
        header.setMode(ModeEnum.OPAQUE);
        header.getLineBox().getPen().setLineWidth(1f);

        header.setFontName("DejaVu Sans");
        header.setPdfEncoding("Identity-H");
        header.setPdfEmbedded(true);

        header.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        header.setPositionType(PositionTypeEnum.FLOAT);

        return header;
    }

    private JRDesignTextField createField(String fieldName, Class<?> clazz, int x, int width) {
        JRDesignTextField field = new JRDesignTextField();
        field.setX(x);
        field.setY(0);
        field.setWidth(width);
        field.setHeight(20);

        JRDesignExpression expression = new JRDesignExpression();
        expression.setText("$F{" + fieldName + "}");
        expression.setValueClass(clazz);
        field.setExpression(expression);
        field.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
        field.getLineBox().getPen().setLineWidth(1f);
        field.getLineBox().setLeftPadding(5);
        field.setFontName("DejaVu Sans");

        field.setPdfEncoding("Identity-H");
        field.setPdfEmbedded(true);

        field.setStretchWithOverflow(true);
        field.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        field.setPositionType(PositionTypeEnum.FLOAT);

        return field;
    }

    private JRDesignTextField createFormattedDateField(String fieldName, int x, int width) {
        JRDesignTextField field = new JRDesignTextField();
        field.setX(x);
        field.setY(0);
        field.setWidth(width);
        field.setHeight(20);

        JRDesignExpression expression = new JRDesignExpression();
        expression.setText(
                "($F{" + fieldName + "} == null) ? \"\" : new java.text.SimpleDateFormat(\"dd-MM-yyyy\").format(java.sql.Date.valueOf($F{" + fieldName + "}))"
        );
        expression.setValueClass(String.class);
        field.setExpression(expression);

        field.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        field.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
        field.getLineBox().getPen().setLineWidth(1f);
        field.getLineBox().setLeftPadding(5);
        field.setFontName("DejaVu Sans");
        field.setPdfEncoding("Identity-H");
        field.setPdfEmbedded(true);

        field.setStretchWithOverflow(true);
        field.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        field.setPositionType(PositionTypeEnum.FLOAT);

        return field;
    }

    private JRDesignField createJRField(String name, Class<?> clazz) {
        JRDesignField field = new JRDesignField();
        field.setName(name);
        field.setValueClass(clazz);
        return field;
    }
}
