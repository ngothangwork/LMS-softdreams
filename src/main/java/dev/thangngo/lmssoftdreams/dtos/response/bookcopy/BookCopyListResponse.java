package dev.thangngo.lmssoftdreams.dtos.response.bookcopy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookCopyListResponse {
    private Long id;
    private String barcode;
    private String status;
}
