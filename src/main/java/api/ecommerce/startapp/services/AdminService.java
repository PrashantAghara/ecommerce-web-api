package api.ecommerce.startapp.services;
import api.ecommerce.startapp.entities.Admin;
import api.ecommerce.startapp.entities.Category;
import api.ecommerce.startapp.entities.Product;
import api.ecommerce.startapp.entities.User;
import api.ecommerce.startapp.exceptions.UnauthorizedAccessException;
import api.ecommerce.startapp.responses.UserResponse;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface AdminService {
    Admin validate(String username,String password);
    Admin findByUserName(String userName);
    Product getProduct(Integer id,Integer adminId) throws UnauthorizedAccessException;
    Product saveProduct(Product product,Integer adminId) throws UnauthorizedAccessException;
    List<Product> getAllProducts(Integer adminId) throws UnauthorizedAccessException;
    boolean deleteProduct(Integer id,Integer adminId) throws UnauthorizedAccessException;
    List<Category> getAllCategories(Integer adminId) throws UnauthorizedAccessException;
    Category insertCategory(Category category,Integer adminId) throws UnauthorizedAccessException;
    User findUserById(Integer id,Integer adminId) throws UnauthorizedAccessException;
    List<User> findAllUsers(Integer adminId) throws UnauthorizedAccessException;
    boolean logout(Integer user_id);
}
