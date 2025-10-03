package dev.thangngo.lmssoftdreams.controllers;

import dev.thangngo.lmssoftdreams.dtos.request.book.BookSearchRequest;
import dev.thangngo.lmssoftdreams.dtos.request.borrow.BorrowCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.borrow.BorrowSearchRequest;
import dev.thangngo.lmssoftdreams.dtos.request.borrow.BorrowUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.ApiResponse;
import dev.thangngo.lmssoftdreams.dtos.response.PageResponse;
import dev.thangngo.lmssoftdreams.dtos.response.borrow.BorrowResponse;
import dev.thangngo.lmssoftdreams.services.BorrowService;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.type.*;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.hibernate.query.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/borrows")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BorrowResponse>> createBorrow(
            @Valid @RequestBody BorrowCreateRequest request) {
        System.out.println("Borrow request = " + request);
        BorrowResponse response = borrowService.createBorrow(request);
        return ResponseEntity.ok(ApiResponse.<BorrowResponse>builder()
                .success(true)
                .code(200)
                .message("Borrow created successfully")
                .result(response)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BorrowResponse>> getBorrowById(@PathVariable Long id) {
        BorrowResponse response = borrowService.getBorrowById(id);
        return ResponseEntity.ok(ApiResponse.<BorrowResponse>builder()
                .success(true)
                .code(200)
                .message("Borrow fetched successfully")
                .result(response)
                .build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BorrowResponse>> updateBorrow(
            @PathVariable Long id,
            @Valid @RequestBody BorrowUpdateRequest request) {

        BorrowResponse response = borrowService.updateBorrow(id, request);
        return ResponseEntity.ok(ApiResponse.<BorrowResponse>builder()
                .success(true)
                .code(200)
                .message("Borrow updated successfully")
                .result(response)
                .build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<BorrowResponse>> updateBorrowStatus(
            @PathVariable Long id,
            @Valid
            @RequestBody BorrowUpdateRequest request) {
        BorrowResponse response = borrowService.updateBorrowStatus(id, request.getStatus().toString());
        return ResponseEntity.ok(ApiResponse.<BorrowResponse>builder()
                .success(true)
                .code(200)
                .message("Borrow status updated successfully")
                .result(response)
                .build());

    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<BorrowResponse>>> searchBorrow (
            @RequestBody @Valid BorrowSearchRequest request,
            @PageableDefault(size = 5, sort = "name", direction = Sort.Direction.ASC) Pageable pageable
    ){
        PageResponse<BorrowResponse> result = borrowService.searchBorrow(request, pageable);
        return ResponseEntity.ok(
                ApiResponse.<PageResponse<BorrowResponse>>builder()
                        .success(true)
                        .code(200)
                        .message("")
                        .result(result)
                        .build()
        );

    }

    @PostMapping("/export")
    public ResponseEntity<byte[]> exportBorrowsPdf(
            @RequestBody @Valid BorrowSearchRequest request,
            @PageableDefault(size = 5, sort = "name", direction = Sort.Direction.ASC) Pageable pageable
    ) throws Exception {
        List<BorrowResponse> borrows = borrowService.searchBorrows(request, pageable);

        JasperDesign jasperDesign = new JasperDesign();
        jasperDesign.setName("BorrowsReport");
        jasperDesign.setPageWidth(595);
        jasperDesign.setPageHeight(842);

        JRDesignBand titleBand = new JRDesignBand();
        titleBand.setHeight(40);

        JRDesignStaticText title = new JRDesignStaticText();
        title.setText("Borrows Report");
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
        JRDesignTextField borrowDateField = createField("borrowDate", java.time.LocalDate.class, 50, 90);
        borrowDateField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        detailBand.addElement(borrowDateField);

        JRDesignTextField returnDateField = createField("returnDate", java.time.LocalDate.class, 140, 90);
        returnDateField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
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

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, out);
        byte[] pdfBytes = out.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=borrows.pdf");

        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }


    @PostMapping("/export-excel")
    public ResponseEntity<byte[]> exportBorrowsExcel(
            @RequestBody @Valid BorrowSearchRequest request,
            @PageableDefault(size = 5, sort = "name", direction = Sort.Direction.ASC) Pageable pageable
    ) throws Exception {
        List<BorrowResponse> borrows = borrowService.searchBorrows(request, pageable);

        JasperDesign jasperDesign = new JasperDesign();
        jasperDesign.setName("BorrowsExcel");
        jasperDesign.setPageWidth(595);
        jasperDesign.setPageHeight(842);

        JRDesignBand titleBand = new JRDesignBand();
        titleBand.setHeight(30);
        JRDesignStaticText title = new JRDesignStaticText();
        title.setText("Borrows Report (Excel)");
        title.setX(200);
        title.setY(5);
        title.setWidth(200);
        title.setHeight(20);
        title.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        titleBand.addElement(title);
        jasperDesign.setTitle(titleBand);

        JRDesignBand columnHeader = new JRDesignBand();
        columnHeader.setHeight(20);
        columnHeader.addElement(createHeader("ID", 0, 50));
        columnHeader.addElement(createHeader("Ngày mươn", 50, 90));
        columnHeader.addElement(createHeader("Ngày trả", 140, 90));
        columnHeader.addElement(createHeader("Tên sách", 230, 70));
        columnHeader.addElement(createHeader("Người mượn", 400, 90));
        columnHeader.addElement(createHeader("Trạng thái", 490, 80));
        jasperDesign.setColumnHeader(columnHeader);

        JRDesignBand detailBand = new JRDesignBand();
        detailBand.setHeight(20);

        JRDesignTextField idField = createField("id", Long.class, 0, 50);
        idField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        detailBand.addElement(idField);

        JRDesignTextField borrowDateField = createField("borrowDate", java.time.LocalDate.class, 50, 90);
        borrowDateField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        detailBand.addElement(borrowDateField);

        JRDesignTextField returnDateField = createField("returnDate", java.time.LocalDate.class, 140, 90);
        returnDateField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
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

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));

        exporter.exportReport();
        byte[] excelBytes = out.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=borrows.xlsx");

        return ResponseEntity.ok().headers(headers).body(excelBytes);
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


    private JRDesignField createJRField(String name, Class<?> clazz) {
        JRDesignField field = new JRDesignField();
        field.setName(name);
        field.setValueClass(clazz);
        return field;
    }


}
