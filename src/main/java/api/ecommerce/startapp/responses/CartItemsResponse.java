package api.ecommerce.startapp.responses;

import api.ecommerce.startapp.utils.CartProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemsResponse {
    List<CartProduct> data;
    private Integer StatusCode;
    private String message;
}
