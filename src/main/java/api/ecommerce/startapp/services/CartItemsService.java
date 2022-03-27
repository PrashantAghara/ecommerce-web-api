package api.ecommerce.startapp.services;

import api.ecommerce.startapp.entities.CartItems;
import api.ecommerce.startapp.responses.CartItemsResponse;
import org.springframework.stereotype.Service;
@Service
public interface CartItemsService {
    CartItemsResponse getProductsInCart(Integer user_id);
    CartItems addProduct(Integer user_id,Integer product_id);
    CartItems removeProduct(Integer user_id,Integer product_id);
}
