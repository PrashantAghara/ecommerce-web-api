package api.ecommerce.startapp.services.implementation;

import api.ecommerce.startapp.dao.ProductDao;
import api.ecommerce.startapp.entities.Category;
import api.ecommerce.startapp.entities.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductServiceImplTest {

    ProductDao productDao = Mockito.mock(ProductDao.class);
    ProductServiceImpl productService = new ProductServiceImpl();

    @BeforeEach
    public void beforeEachTest() {
        productService.productDao = productDao;
    }

    @Test
    public void testGetProduct() {
        Product product = new Product(1, "product", 100, "desc", new Category(1, "category"));
        Optional<Product> optional = Optional.of(product);
        Mockito.when(productDao.findById(1)).thenReturn(optional);
        assertEquals(optional.get(), productService.getProduct(1));
    }

    @Test
    public void testSaveProduct() {
        Product product = new Product(1, "product", 100, "desc", new Category(1, "category"));
        Mockito.when(productDao.save(product)).thenReturn(product);
        assertEquals(product, productService.saveProduct(product));
    }

    @Test
    public void testSaveProductWithNameNullOrEmpty() {
        Product product = new Product(1, null, 100, "desc", new Category(1, "category"));
        assertThrows(IllegalArgumentException.class, () -> {
            productService.saveProduct(product);
        });
    }

    @Test
    public void testSaveProductWithPriceNullOrZero() {
        Product product = new Product(1, "product", 0, "desc", new Category(1, "category"));
        assertThrows(IllegalArgumentException.class, () -> {
            productService.saveProduct(product);
        });
    }

    @Test
    public void testGetAllProducts() {
        Product product = new Product(1, "product", 100, "desc", new Category(1, "category"));
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        Mockito.when(productDao.findAll()).thenReturn(productList);
        assertEquals(productList, productService.getAllProducts());
    }

}