package api.ecommerce.startapp.responses;

import api.ecommerce.startapp.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductResponse {
    private Product product;
    private Integer StatusCode;
    private String message;
}
