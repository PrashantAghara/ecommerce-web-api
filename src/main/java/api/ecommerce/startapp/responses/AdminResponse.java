package api.ecommerce.startapp.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminResponse {
    private Integer admin_id;
    private String userName;
    private Integer StatusCode;
    private String message;
}
