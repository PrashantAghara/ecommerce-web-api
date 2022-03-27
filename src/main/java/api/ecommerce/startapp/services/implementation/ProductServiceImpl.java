package api.ecommerce.startapp.services.implementation;

import api.ecommerce.startapp.dao.ProductDao;
import api.ecommerce.startapp.entities.Product;
import api.ecommerce.startapp.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDao productDao;

    @Override
    public Product getProduct(Integer id) throws NoSuchElementException {
        return productDao.findById(id).get();
    }

    @Override
    public Product saveProduct(Product product) throws IllegalArgumentException {
        if (product.getProductName() == null || product.getProductName().isEmpty()) {
            throw new IllegalArgumentException("Product Name is Null/Empty");
        } else if (product.getPrice() == null || product.getPrice() == 0) {
            throw new IllegalArgumentException("Product Price is Null/Empty");
        } else if(product.getCategory() == null){
            throw new IllegalArgumentException("Category is Null/Empty");
        }
        return productDao.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productDao.findAll();
    }

    @Override
    public void deleteProduct(Integer id) throws EmptyResultDataAccessException {
        productDao.deleteById(id);
    }
}
