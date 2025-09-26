package dev.thangngo.lmssoftdreams.controllers;

import dev.thangngo.lmssoftdreams.dtos.request.book.BookCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.book.BookSearchRequest;
import dev.thangngo.lmssoftdreams.dtos.request.book.BookUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.ApiResponse;
import dev.thangngo.lmssoftdreams.dtos.response.PageResponse;
import dev.thangngo.lmssoftdreams.dtos.response.book.BookDetailResponse;
import dev.thangngo.lmssoftdreams.dtos.response.book.BookResponse;
import dev.thangngo.lmssoftdreams.services.BookService;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookResponse>> createBook(
            @Valid @RequestBody BookCreateRequest request) {
        BookResponse response = bookService.createBook(request);
        return ResponseEntity
                .created(URI.create("/books/" + response.getId()))
                .body(ApiResponse.<BookResponse>builder()
                        .message("Book created successfully")
                        .code(201)
                        .success(true)
                        .result(response)
                        .build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookUpdateRequest request) {
        BookResponse response = bookService.updateBook(id, request);
        return ResponseEntity.ok(ApiResponse.<BookResponse>builder()
                .message("Book updated successfully")
                .code(200)
                .success(true)
                .result(response)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookDetailResponse>> getBookById(@PathVariable Long id) {
        BookDetailResponse response = bookService.getBookById(id);
        return ResponseEntity.ok(ApiResponse.<BookDetailResponse>builder()
                .message("Book fetched successfully")
                .code(200)
                .success(true)
                .result(response)
                .build());
    }

    @GetMapping("/update/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> getBookForUpdate(@PathVariable Long id) {
        BookResponse response = bookService.getBookUpdateById(id);
        return ResponseEntity.ok(ApiResponse.<BookResponse>builder()
                .code(200)
                .message("get book update successfully")
                .result(response)
                .success(true)
                .build());
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<BookResponse>>> getBooks(
            @RequestParam(required = false) String name) {
        List<BookResponse> response = (name != null)
                ? bookService.getBooksByName(name)
                : bookService.getAllBooks();

        return ResponseEntity.ok(ApiResponse.<List<BookResponse>>builder()
                .message("Books fetched successfully")
                .code(200)
                .success(true)
                .result(response)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .message("Book deleted successfully")
                .code(200)
                .success(true)
                .build());
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<BookResponse>>> searchBooks(
            @RequestBody @Valid BookSearchRequest request,
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        PageResponse<BookResponse> result = bookService.filterBooks(request, pageable);
        return ResponseEntity.ok(
                ApiResponse.<PageResponse<BookResponse>>builder()
                        .success(true)
                        .code(200)
                        .message("Search books successfully")
                        .result(result)
                        .build()
        );
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportBooksReport() throws Exception {
        // 1. Lấy dữ liệu từ service
        List<BookResponse> books = bookService.getAllBooks();

        // 2. Tạo thiết kế Jasper
        JasperDesign jasperDesign = new JasperDesign();
        jasperDesign.setName("BooksReport");
        jasperDesign.setPageWidth(595);
        jasperDesign.setPageHeight(842);

        // Title
        JRDesignBand titleBand = new JRDesignBand();
        titleBand.setHeight(40);

        JRDesignStaticText title = new JRDesignStaticText();
        title.setText("Books Report");
        title.setX(200);
        title.setY(10);
        title.setWidth(200);
        title.setHeight(30);
        title.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        titleBand.addElement(title);

        jasperDesign.setTitle(titleBand);

        // Column Header
        JRDesignBand columnHeader = new JRDesignBand();
        columnHeader.setHeight(20);

        columnHeader.addElement(createHeader("ID", 0, 50));
        columnHeader.addElement(createHeader("Name", 50, 200));
        columnHeader.addElement(createHeader("ISBN", 250, 150));
        columnHeader.addElement(createHeader("PublisherId", 400, 100));

        jasperDesign.setColumnHeader(columnHeader);

        // Detail band
        JRDesignBand detailBand = new JRDesignBand();
        detailBand.setHeight(20);

        detailBand.addElement(createField("id", Long.class, 0, 50));
        detailBand.addElement(createField("name", String.class, 50, 200));
        detailBand.addElement(createField("isbn", String.class, 250, 150));
        detailBand.addElement(createField("publisherId", Long.class, 400, 100));

        ((JRDesignSection) jasperDesign.getDetailSection()).addBand(detailBand);

        // Khai báo field
        jasperDesign.addField(createJRField("id", Long.class));
        jasperDesign.addField(createJRField("name", String.class));
        jasperDesign.addField(createJRField("isbn", String.class));
        jasperDesign.addField(createJRField("publisherId", Long.class));

        // 3. Compile & Fill
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(books);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);

        // 4. Export PDF
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, out);
        byte[] pdfBytes = out.toByteArray();

        // 5. Trả về response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        // inline = mở trực tiếp trên browser | attachment = tải về file
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=books.xlsx");

        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }

    // --- helper methods ---
    private JRDesignStaticText createHeader(String text, int x, int width) {
        JRDesignStaticText header = new JRDesignStaticText();
        header.setText(text);
        header.setX(x);
        header.setY(0);
        header.setWidth(width);
        header.setHeight(20);
        header.setBold(true);
        header.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        header.setBackcolor(new Color(220, 220, 220)); // xám nhạt
        header.setMode(ModeEnum.OPAQUE);
        header.getLineBox().getPen().setLineWidth(1f);
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

        field.getLineBox().getPen().setLineWidth(1f);

        return field;
    }

    private JRDesignField createJRField(String name, Class<?> clazz) {
        JRDesignField field = new JRDesignField();
        field.setName(name);
        field.setValueClass(clazz);
        return field;
    }

    @GetMapping("/export-excel")
    public ResponseEntity<byte[]> exportBooksExcel() throws Exception {
        List<BookResponse> books = bookService.getAllBooks();

        // thiết kế Jasper giống như phần PDF
        JasperDesign jasperDesign = new JasperDesign();
        jasperDesign.setName("BooksExcel");
        jasperDesign.setPageWidth(595);
        jasperDesign.setPageHeight(842);

        // Title
        JRDesignBand titleBand = new JRDesignBand();
        titleBand.setHeight(30);
        JRDesignStaticText title = new JRDesignStaticText();
        title.setText("Books Report (Excel)");
        title.setX(200);
        title.setY(5);
        title.setWidth(200);
        title.setHeight(20);
        title.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        titleBand.addElement(title);
        jasperDesign.setTitle(titleBand);

        // Column Header
        JRDesignBand columnHeader = new JRDesignBand();
        columnHeader.setHeight(20);
        columnHeader.addElement(createHeader("ID", 0, 50));
        columnHeader.addElement(createHeader("Name", 50, 200));
        columnHeader.addElement(createHeader("ISBN", 250, 150));
        columnHeader.addElement(createHeader("PublisherId", 400, 100));
        jasperDesign.setColumnHeader(columnHeader);

        // Detail
        JRDesignBand detailBand = new JRDesignBand();
        detailBand.setHeight(20);
        detailBand.addElement(createField("id", Long.class, 0, 50));
        detailBand.addElement(createField("name", String.class, 50, 200));
        detailBand.addElement(createField("isbn", String.class, 250, 150));
        detailBand.addElement(createField("publisherId", Long.class, 400, 100));
        ((JRDesignSection) jasperDesign.getDetailSection()).addBand(detailBand);

        jasperDesign.addField(createJRField("id", Long.class));
        jasperDesign.addField(createJRField("name", String.class));
        jasperDesign.addField(createJRField("isbn", String.class));
        jasperDesign.addField(createJRField("publisherId", Long.class));

        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(books);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);

        // Export ra Excel
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));

        SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
        config.setDetectCellType(true); // định dạng cell tự động
        config.setCollapseRowSpan(false);
        exporter.setConfiguration(config);

        exporter.exportReport();

        byte[] excelBytes = out.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=books.xlsx");

        return ResponseEntity.ok().headers(headers).body(excelBytes);
    }


}
