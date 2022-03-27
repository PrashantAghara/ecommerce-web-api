package api.ecommerce.startapp.responses;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class ListResponse {
    private List<?> data;
    private Integer StatusCode;
    private String message;
}
