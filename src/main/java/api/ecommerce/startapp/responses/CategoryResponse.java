package api.ecommerce.startapp.responses;

import api.ecommerce.startapp.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryResponse {
    private Category category;
    private Integer StatusCode;
    private String message;
}
