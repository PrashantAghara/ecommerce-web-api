package api.ecommerce.startapp.services;

import api.ecommerce.startapp.entities.CartItems;
import api.ecommerce.startapp.entities.Product;
import api.ecommerce.startapp.entities.User;
import api.ecommerce.startapp.exceptions.UnauthorizedAccessException;
import api.ecommerce.startapp.responses.CartItemsResponse;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface UserService {
    User validate(String username,String password);
    User findByUserName(String userName);
    User findUserById(Integer id);
    User saveUser(User user);
    List<User> getAllUsers();
    List<Product> getAllProducts(Integer user_id) throws UnauthorizedAccessException;
    CartItemsResponse getProductsInCart(Integer user_id) throws UnauthorizedAccessException;
    CartItems addProduct(Integer user_id, Integer product_id) throws UnauthorizedAccessException;
    CartItems removeProduct(Integer user_id, Integer product_id) throws UnauthorizedAccessException;
    boolean logout(Integer user_id);
}
