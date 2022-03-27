package api.ecommerce.startapp.services.implementation;

import api.ecommerce.startapp.dao.CartItemsDao;
import api.ecommerce.startapp.entities.CartItems;
import api.ecommerce.startapp.entities.Category;
import api.ecommerce.startapp.entities.Product;
import api.ecommerce.startapp.entities.User;
import api.ecommerce.startapp.responses.CartItemsResponse;
import api.ecommerce.startapp.services.ProductService;
import api.ecommerce.startapp.services.UserService;
import api.ecommerce.startapp.utils.CartProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartItemsServiceImplTest {

    CartItemsServiceImpl cartItemsService = new CartItemsServiceImpl();
    UserService userService = Mockito.mock(UserService.class);
    CartItemsDao cartItemsDao = Mockito.mock(CartItemsDao.class);
    ProductService productService = Mockito.mock(ProductService.class);
    User expUser = new User(1,"user","user@email","user1234",false);
    Product product = new Product(1,"product",100,"desc",new Category(1,"category"));
    @BeforeEach
    public void beforeEachTest(){
        cartItemsService.productService = productService;
        cartItemsService.userService = userService;
        cartItemsService.cartItemsDao = cartItemsDao;
    }

    @Test
    public void testGetProductsInCart(){
        User user = new User();
        user.setUser_id(1);
        CartItems cartItems = new CartItems(expUser,product,1,100);
        List<CartItems> cartItemsList = new ArrayList<>();
        cartItemsList.add(cartItems);
        Mockito.doReturn(cartItemsList).when(cartItemsDao).findByUser(user);
        List<CartProduct> cartProducts = new ArrayList<>();
        CartProduct cartProduct = new CartProduct(product,100,1);
        cartProducts.add(cartProduct);
        CartItemsResponse cartItemsResponse = new CartItemsResponse();
        cartItemsResponse.setData(cartProducts);
        assertEquals(cartItemsResponse,cartItemsService.getProductsInCart(1));
    }

    @Test
    public void testAddProductIfProductPresent(){
        User user = new User();
        user.setUser_id(1);
        Product product1 = new Product();
        product1.setProduct_id(1);
        CartItems cartItems = new CartItems(expUser,product,1,100);
        Mockito.when(cartItemsDao.findByUserAndProduct(user,product1)).thenReturn(cartItems);
        cartItems.setQuantity(cartItems.getQuantity()+1);
        cartItems.setProduct(cartItems.getProduct());
        cartItems.setPrice(cartItems.getPrice() + cartItems.getPrice());
        cartItems.setUser(cartItems.getUser());
        Mockito.when(cartItemsDao.save(cartItems)).thenReturn(cartItems);
        assertEquals(cartItems,cartItemsService.addProduct(1,1));
    }

    @Test
    public void testAddProductIfProductNotPresent(){
        User user = new User();
        user.setUser_id(1);
        Product product1 = new Product();
        product1.setProduct_id(1);
        CartItems cartItems = new CartItems(expUser,product,1,100);
        Mockito.when(cartItemsDao.findByUserAndProduct(user,product1)).thenReturn(null);
        Mockito.when(cartItemsDao.save(cartItems)).thenReturn(cartItems);
        Mockito.when(userService.findUserById(1)).thenReturn(expUser);
        Mockito.when(productService.getProduct(1)).thenReturn(product);
        assertEquals(cartItems,cartItemsService.addProduct(1,1));
    }

    @Test
    public void testRemoveProductFromCartIfProductNotPresent(){
        User user = new User();
        user.setUser_id(1);
        Product product1 = new Product();
        product1.setProduct_id(1);
        Mockito.when(cartItemsDao.findByUserAndProduct(user,product1)).thenReturn(null);
        assertNull(cartItemsService.removeProduct(1, 1));
    }

    @Test
    public void testRemoveProductFromCartIfProductPresentQuantityGreaterThanOne(){
        User user = new User();
        user.setUser_id(1);
        Product product1 = new Product();
        product1.setProduct_id(1);
        CartItems cartItems = new CartItems(expUser,product,2,200);
        Mockito.when(cartItemsDao.findByUserAndProduct(user,product1)).thenReturn(cartItems);
        cartItems.setQuantity(cartItems.getQuantity()-1);
        Mockito.when(productService.getProduct(1)).thenReturn(product);
        cartItems.setPrice(cartItems.getPrice() - product.getPrice());
        Mockito.when(cartItemsDao.save(cartItems)).thenReturn(cartItems);
        assertEquals(cartItems,cartItemsService.removeProduct(1,1));
    }

    @Test
    public void testRemoveProductFromCartIfProductPresentQuantityEqualToOne(){
        User user = new User();
        user.setUser_id(1);
        Product product1 = new Product();
        product1.setProduct_id(1);
        CartItems cartItems = new CartItems(expUser,product,1,100);
        Mockito.when(cartItemsDao.findByUserAndProduct(user,product1)).thenReturn(cartItems);
        assertEquals(cartItems,cartItemsService.removeProduct(1,1));
    }
}