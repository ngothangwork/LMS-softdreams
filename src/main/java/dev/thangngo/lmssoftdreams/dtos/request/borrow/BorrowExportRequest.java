package dev.thangngo.lmssoftdreams.dtos.request.borrow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowExportRequest {
    private BorrowSearchRequest searchRequest;
    private Pageable pageable;
    private String exportType;
    private String correlationId;
}
