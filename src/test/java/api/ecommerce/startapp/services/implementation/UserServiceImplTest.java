package api.ecommerce.startapp.services.implementation;

import api.ecommerce.startapp.dao.UserDao;
import api.ecommerce.startapp.entities.CartItems;
import api.ecommerce.startapp.entities.Category;
import api.ecommerce.startapp.entities.Product;
import api.ecommerce.startapp.entities.User;
import api.ecommerce.startapp.exceptions.UnauthorizedAccessException;
import api.ecommerce.startapp.responses.CartItemsResponse;
import api.ecommerce.startapp.services.CartItemsService;
import api.ecommerce.startapp.services.ProductService;
import api.ecommerce.startapp.utils.CartProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceImplTest {

    UserDao userDao = Mockito.mock(UserDao.class);
    ProductService productService = Mockito.mock(ProductService.class);
    CartItemsService cartItemsService = Mockito.mock(CartItemsService.class);
    UserServiceImpl userService = new UserServiceImpl();
    User user = new User(1, "user", "user@email", "user1234", false);
    Product product = new Product(1, "product", 100, "desc", new Category(1, "category"));

    @BeforeEach
    public void beforeEachTest() {
        userService.userDao = userDao;
        userService.productService = productService;
        userService.cartItemsService = cartItemsService;
    }

    @Test
    public void testUserValidate() {
        Mockito.doReturn(user).when(userDao).findByUserName("user");
        Mockito.doReturn(user).when(userDao).save(user);
        assertEquals(user, userService.validate("user", "user1234"));
    }

    @Test
    public void testFindByUserName() {
        Mockito.doReturn(user).when(userDao).findByUserName("user");
        assertEquals(user, userService.findByUserName("user"));
    }

    @Test
    public void testFindById() {
        Optional<User> optional = Optional.of(user);
        Mockito.doReturn(optional).when(userDao).findById(1);
        assertEquals(user, userService.findUserById(1));
    }

    @Test
    public void testSaveUser() {
        Mockito.doReturn(user).when(userDao).save(user);
        User user1 = new User(1, "user", "user@email", "user1234", false);
        assertEquals(user, userService.saveUser(user1));
    }

    @Test
    public void testSaveUserWithPasswordLessThanEightWord() {
        User user1 = new User(1, "user", "user@email", "user", false);
        assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUser(user1);
        });
    }

    @Test
    public void testSaveUserWithUserNameExists() {
        UserServiceImpl userService1 = Mockito.spy(userService);
        User user1 = new User(1, "user", "user@email", "user1234", false);
        Mockito.doReturn(user).when(userService1).findByUserName(user1.getUserName());
        assertThrows(IllegalArgumentException.class, () -> {
            userService1.saveUser(user1);
        });
    }

    @Test
    public void testGetAllUsers() {
        List<User> list = new ArrayList<>();
        list.add(user);
        Mockito.doReturn(list).when(userDao).findAll();
        assertEquals(list, userService.getAllUsers());
    }

    @Test
    public void testGetProductsInCartWithLogin() throws UnauthorizedAccessException {
        UserServiceImpl userService1 = Mockito.spy(userService);
        Mockito.doReturn(true).when(userService1).isUserLogin(1);
        List<Product> products = new ArrayList<>();
        products.add(product);
        Mockito.when(productService.getAllProducts()).thenReturn(products);
        assertEquals(products, userService1.getAllProducts(1));
    }

    @Test
    public void testGetProductsInCartWithoutLogin() throws UnauthorizedAccessException {
        UserServiceImpl userService1 = Mockito.spy(userService);
        Mockito.doReturn(false).when(userService1).isUserLogin(1);
        assertThrows(UnauthorizedAccessException.class, () -> {
            userService1.getAllProducts(1);
        });
    }

    @Test
    public void testGetProductsInCart() throws UnauthorizedAccessException {
        UserServiceImpl userService1 = Mockito.spy(userService);
        Mockito.doReturn(true).when(userService1).isUserLogin(1);
        CartProduct cartProduct = new CartProduct(product, 100, 1);
        List<CartProduct> cartProducts = new ArrayList<>();
        cartProducts.add(cartProduct);
        CartItemsResponse cartItemsResponse = new CartItemsResponse();
        cartItemsResponse.setData(cartProducts);
        Mockito.doReturn(cartItemsResponse).when(cartItemsService).getProductsInCart(1);
        assertEquals(cartItemsResponse, userService1.getProductsInCart(1));
    }

    @Test
    public void testAddProductsInCart() throws UnauthorizedAccessException {
        UserServiceImpl userService1 = Mockito.spy(userService);
        Mockito.doReturn(true).when(userService1).isUserLogin(1);
        CartItems cartItems = new CartItems(user, product, 1, 100);
        Mockito.doReturn(cartItems).when(cartItemsService).addProduct(1, 1);
        assertEquals(cartItems, userService1.addProduct(1, 1));
    }

    @Test
    public void testAddProductsInCartWithoutLogin() {
        UserServiceImpl userService1 = Mockito.spy(userService);
        Mockito.doReturn(false).when(userService1).isUserLogin(1);
        assertThrows(UnauthorizedAccessException.class, () -> {
            userService1.addProduct(1, 1);
        });
    }

    @Test
    public void testRemoveProductWithoutLogin() {
        UserServiceImpl userService1 = Mockito.spy(userService);
        Mockito.doReturn(false).when(userService1).isUserLogin(1);
        assertThrows(UnauthorizedAccessException.class, () -> {
            userService1.removeProduct(1, 1);
        });
    }

    @Test
    public void testRemoveProduct() throws UnauthorizedAccessException {
        UserServiceImpl userService1 = Mockito.spy(userService);
        Mockito.doReturn(true).when(userService1).isUserLogin(1);
        CartItems cartItems = new CartItems(user, product, 1, 100);
        Mockito.doReturn(cartItems).when(cartItemsService).removeProduct(1, 1);
        assertEquals(cartItems, userService1.removeProduct(1, 1));
    }
}