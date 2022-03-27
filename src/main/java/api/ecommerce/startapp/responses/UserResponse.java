package api.ecommerce.startapp.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private Integer user_id;
    private Integer StatusCode;
    private String userName;
    private String email;
    private String message;
}
