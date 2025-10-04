package dev.thangngo.lmssoftdreams.dtos.response.borrow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowExportResult {
    private String correlationId;
    private String filePath;
    private String status;
}
