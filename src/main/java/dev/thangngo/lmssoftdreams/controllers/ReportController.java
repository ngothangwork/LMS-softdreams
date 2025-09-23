package dev.thangngo.lmssoftdreams.controllers;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class ReportController {

    @GetMapping("/report")
    public ResponseEntity<byte[]> exportReport() throws Exception {
        // 1. Tạo thiết kế Jasper (report template) bằng code
        JasperDesign jasperDesign = new JasperDesign();
        jasperDesign.setName("DemoReport");
        jasperDesign.setPageWidth(595);
        jasperDesign.setPageHeight(842);

        // 2. Thêm Title band
        JRDesignBand band = new JRDesignBand();
        band.setHeight(50);

        JRDesignStaticText staticText = new JRDesignStaticText();
        staticText.setText("Hello JasperReports + Spring Boot!");
        staticText.setX(100);
        staticText.setY(20);
        staticText.setWidth(400);
        staticText.setHeight(30);

        band.addElement(staticText);
        jasperDesign.setTitle(band);

        // 3. Compile report
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        // 4. Fill dữ liệu (ở đây dùng rỗng cho demo)
        JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport,
                new HashMap<>(),
                new JREmptyDataSource()
        );

        // 5. Xuất PDF ra mảng byte
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, out);

        byte[] pdfBytes = out.toByteArray();

        // 6. Trả về file PDF trong response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=demo.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(pdfBytes);
    }
}

