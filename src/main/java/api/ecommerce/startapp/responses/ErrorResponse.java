package api.ecommerce.startapp.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private Integer StatusCode;
    private String message;
}
