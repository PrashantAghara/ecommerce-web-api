package api.ecommerce.startapp.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessResponse {
    private Integer StatusCode;
    private String message;
}
