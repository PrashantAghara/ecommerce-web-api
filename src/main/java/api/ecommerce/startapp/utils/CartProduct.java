package api.ecommerce.startapp.utils;

import api.ecommerce.startapp.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartProduct {
    private Product product;
    private Integer price;
    private Integer quantity;
}
