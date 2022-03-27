package api.ecommerce.startapp.controller;

import api.ecommerce.startapp.entities.CartItems;
import api.ecommerce.startapp.entities.Category;
import api.ecommerce.startapp.entities.Product;
import api.ecommerce.startapp.entities.User;
import api.ecommerce.startapp.exceptions.UnauthorizedAccessException;
import api.ecommerce.startapp.responses.*;
import api.ecommerce.startapp.services.UserService;
import api.ecommerce.startapp.utils.CartProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserControllerTest {

    UserService userService = Mockito.mock(UserService.class);
    UserController userController = new UserController();
    User user = new User(1, "user", "user@email", "user1234", false);
    Product product = new Product(1, "product", 100, "desc", new Category(1, "category"));
    ErrorResponse loginError = new ErrorResponse(403, "Please Login First");

    @BeforeEach
    public void beforeEachTestCase() {
        userController.userService = userService;
    }

    @Test
    public void testLoginSuccess() {
        Mockito.when(userService.validate("user", "user1234")).thenReturn(user);
        UserResponse userResponse = new UserResponse(1, 200, user.getUserName(), user.getEmail(), "Login Success");
        ResponseEntity<?> response = new ResponseEntity<>(userResponse, HttpStatus.OK);
        assertEquals(response, userController.login(user));
    }

    @Test
    public void testLoginFailure() {
        Mockito.when(userService.validate("user", "user12")).thenReturn(null);
        ErrorResponse errorResponse = new ErrorResponse(404, "Invalid Username or password");
        ResponseEntity<?> response = new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        assertEquals(response.getStatusCode(), userController.login(user).getStatusCode());
    }

    @Test
    public void testLogoutSuccess() {
        Mockito.when(userService.logout(1)).thenReturn(true);
        SuccessResponse successResponse = new SuccessResponse(200, "LogOut Success");
        ResponseEntity<?> response = new ResponseEntity<>(successResponse, HttpStatus.OK);
        assertEquals(response.getStatusCode(),userController.logOut(1).getStatusCode());
    }

    @Test
    public void testLogOutFail() {
        Mockito.when(userService.logout(1)).thenReturn(false);
        ErrorResponse errorResponse = new ErrorResponse(500, "Internal Server Error");
        ResponseEntity<?> response = new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        assertEquals(response.getStatusCode(), userController.logOut(1).getStatusCode());
    }

    @Test
    public void testRegisterSuccess(){
        Mockito.when(userService.saveUser(user)).thenReturn(user);
        UserResponse userResponse = new UserResponse(user.getUser_id(), 200, user.getUserName(), user.getEmail(), "Registration Successfully");
        ResponseEntity<?> response = new ResponseEntity<>(userResponse, HttpStatus.OK);
        assertEquals(response.getStatusCode(), userController.register(user).getStatusCode());
    }

    @Test
    public void testRegisterFailUserError(){
        User failUser = new User(1,"user","user@email","user",false);
        Mockito.when(userService.saveUser(failUser)).thenThrow(IllegalArgumentException.class);
        ResponseEntity<?> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        assertEquals(response.getStatusCode(),userController.register(failUser).getStatusCode());
    }

    @Test
    public void testRegisterIntServerErr(){
        Mockito.when(userService.saveUser(user)).thenReturn(null);
        ErrorResponse errorResponse = new ErrorResponse(500, "Internal Server Error");
        ResponseEntity<?> response = new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        assertEquals(response.getStatusCode(), userController.register(user).getStatusCode());
    }

    @Test
    public void testGetAllProductsWithLoginFailed() throws UnauthorizedAccessException {
        Mockito.when(userService.getAllProducts(1)).thenThrow(UnauthorizedAccessException.class);
        ResponseEntity<?> response = new ResponseEntity<>(loginError, HttpStatus.FORBIDDEN);
        assertEquals(response.getStatusCode(), userController.getAllProducts(1).getStatusCode());
    }

    @Test
    public void testGetAllProductsNoProductFound() throws UnauthorizedAccessException {
        Mockito.when(userService.getAllProducts(1)).thenReturn(null);
        ErrorResponse errorResponse = new ErrorResponse(404, "No Products found");
        ResponseEntity<?> response = new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        assertEquals(response.getStatusCode(), userController.getAllProducts(1).getStatusCode());
    }

    @Test
    public void testGetAllProductsSuccess() throws UnauthorizedAccessException {
        List<Product> list = new ArrayList<>();
        list.add(product);
        Mockito.when(userService.getAllProducts(1)).thenReturn(list);
        ListResponse listResponse = new ListResponse(list, 200, "Success");
        ResponseEntity<?> response = new ResponseEntity<>(listResponse, HttpStatus.OK);
        assertEquals(response.getStatusCode(), userController.getAllProducts(1).getStatusCode());
    }

    @Test
    public void testAddProductToCartSuccess() throws UnauthorizedAccessException{
        SuccessResponse successResponse = new SuccessResponse(200, "Product Added to Cart");
        ResponseEntity<?> response = new ResponseEntity<>(successResponse, HttpStatus.OK);
        CartItems cartItems = new CartItems(user,product,1,100);
        Mockito.when(userService.addProduct(1,1)).thenReturn(cartItems);
        assertEquals(response.getStatusCode(),userController.addToCart(1,1).getStatusCode());
    }

    @Test
    public void testAddProductToCartLoginFailed() throws UnauthorizedAccessException{
        Mockito.when(userService.addProduct(1,1)).thenThrow(UnauthorizedAccessException.class);
        ResponseEntity<?> response = new ResponseEntity<>(loginError, HttpStatus.FORBIDDEN);
        assertEquals(response.getStatusCode(), userController.addToCart(1,1).getStatusCode());
    }

    @Test
    public void testAddProductLoginFailed() throws UnauthorizedAccessException{
        Mockito.when(userService.addProduct(1,1)).thenReturn(null);
        ErrorResponse errResponse = new ErrorResponse(403, "Forbidden! Please Login");
        ResponseEntity<?> response = new ResponseEntity<>(errResponse, HttpStatus.FORBIDDEN);
        assertEquals(response.getStatusCode(), userController.addToCart(1,1).getStatusCode());
    }

    @Test
    public void testAddProductNotFound() throws UnauthorizedAccessException{
        Mockito.when(userService.addProduct(1,1)).thenThrow(NoSuchElementException.class);
        ErrorResponse response = new ErrorResponse(404, "Product Not Found");
        ResponseEntity<?> responseEntity = new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        assertEquals(responseEntity.getStatusCode(),userController.addToCart(1,1).getStatusCode());
    }

    @Test
    public void testGetCartProducts() throws UnauthorizedAccessException{
        CartProduct cartProduct = new CartProduct(product,100,1);
        List<CartProduct> cartProducts = new ArrayList<>();
        cartProducts.add(cartProduct);
        CartItemsResponse cartItemsResponse = new CartItemsResponse();
        cartItemsResponse.setData(cartProducts);
        cartItemsResponse.setMessage("Success");
        cartItemsResponse.setStatusCode(200);
        ResponseEntity<?> response = new ResponseEntity<>(cartItemsResponse,HttpStatus.OK);
        Mockito.when(userService.getProductsInCart(1)).thenReturn(cartItemsResponse);
        assertEquals(response.getStatusCode(),userController.getCartProducts(1).getStatusCode());
    }

    @Test
    public void testGetCartProductsLoginFails() throws UnauthorizedAccessException{
        Mockito.when(userService.getProductsInCart(1)).thenThrow(UnauthorizedAccessException.class);
        ResponseEntity<?> response = new ResponseEntity<>(loginError, HttpStatus.FORBIDDEN);
        assertEquals(response.getStatusCode(), userController.getCartProducts(1).getStatusCode());
    }

    @Test
    public void testGetCartProductsIfEmpty() throws UnauthorizedAccessException{
        CartItemsResponse cartItemsResponse = new CartItemsResponse();
        List<CartProduct> list = new ArrayList<>();
        cartItemsResponse.setData(list);
        Mockito.when(userService.getProductsInCart(1)).thenReturn(cartItemsResponse);
        ErrorResponse errorResponse = new ErrorResponse(404, "No Products In Cart");
        ResponseEntity<?> response = new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
        assertEquals(response.getStatusCode(),userController.getCartProducts(1).getStatusCode());
    }

    @Test
    public void testRemoveProducts() throws UnauthorizedAccessException{
        CartItems cartItems = new CartItems(user,product,1,100);
        SuccessResponse successResponse = new SuccessResponse(200, "Product Removed from Cart");
        ResponseEntity<?> response = new ResponseEntity<>(successResponse,HttpStatus.OK);
        Mockito.when(userService.removeProduct(1,1)).thenReturn(cartItems);
        assertEquals(response.getStatusCode(),userController.removeProductFromCart(1,1).getStatusCode());
    }

    @Test
    public void testRemoveProductsLoginFails() throws UnauthorizedAccessException{
        Mockito.when(userService.removeProduct(1,1)).thenThrow(UnauthorizedAccessException.class);
        ResponseEntity<?> response = new ResponseEntity<>(loginError, HttpStatus.FORBIDDEN);
        assertEquals(response.getStatusCode(), userController.removeProductFromCart(1,1).getStatusCode());
    }

    @Test
    public void testRemoveProductsIfEmpty() throws UnauthorizedAccessException{
        Mockito.when(userService.removeProduct(1,1)).thenReturn(null);
        ErrorResponse errorResponse = new ErrorResponse(404, "No Products In Cart");
        ResponseEntity<?> response = new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
        assertEquals(response.getStatusCode(),userController.removeProductFromCart(1,1).getStatusCode());
    }

}