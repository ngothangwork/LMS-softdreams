package dev.thangngo.lmssoftdreams.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ApiResponse<T> {
    private boolean success;
    private int code;
    private String message;
    private T result;
}
