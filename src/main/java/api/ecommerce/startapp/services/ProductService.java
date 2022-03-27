package api.ecommerce.startapp.services;
import api.ecommerce.startapp.entities.Product;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface ProductService {
    Product getProduct(Integer id);
    Product saveProduct(Product product);
    List<Product> getAllProducts();
    void deleteProduct(Integer id);
}
