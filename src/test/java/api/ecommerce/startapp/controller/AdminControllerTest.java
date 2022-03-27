package api.ecommerce.startapp.controller;

import api.ecommerce.startapp.entities.Admin;
import api.ecommerce.startapp.entities.Category;
import api.ecommerce.startapp.entities.Product;
import api.ecommerce.startapp.entities.User;
import api.ecommerce.startapp.exceptions.UnauthorizedAccessException;
import api.ecommerce.startapp.responses.*;
import api.ecommerce.startapp.services.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AdminControllerTest {

    AdminService adminService = Mockito.mock(AdminService.class);
    AdminController adminController = new AdminController();
    Admin admin = new Admin(1, "admin", "admin", false);
    Product product = new Product(1, "product", 100, "desc", new Category(1, "category"));
    ErrorResponse loginError = new ErrorResponse(403, "Please Login First");
    Category category = new Category(1, "category");
    User user = new User(1, "user", "user@email", "user1234", false);

    @BeforeEach
    public void beforeEachTestCase() {
        adminController.adminService = adminService;
    }

    @Test
    public void testLoginSuccess() {
        Mockito.when(adminService.validate("admin", "admin")).thenReturn(admin);
        AdminResponse adminResponse = new AdminResponse(1, "admin", 200, "Login Success");
        ResponseEntity<?> response = new ResponseEntity<>(adminResponse, HttpStatus.OK);
        assertEquals(response, adminController.login(admin));
    }

    @Test
    public void testLoginFailure() {
        Mockito.when(adminService.validate("admin", "admin")).thenReturn(null);
        ErrorResponse errorResponse = new ErrorResponse(404, "Invalid Username or password");
        ResponseEntity<?> response = new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        assertEquals(response.getStatusCode(), adminController.login(admin).getStatusCode());
    }

    @Test
    public void testGetProductSuccess() throws UnauthorizedAccessException {
        Mockito.when(adminService.getProduct(1, 1)).thenReturn(product);
        ProductResponse productResponse = new ProductResponse(product, 200, "Success");
        ResponseEntity<?> response = new ResponseEntity<>(productResponse, HttpStatus.OK);
        assertEquals(response.getStatusCode(), adminController.getProduct(1, 1).getStatusCode());
    }

    @Test
    public void testGetProductFailureDueToLoginFailed() throws UnauthorizedAccessException {
        Mockito.when(adminService.getProduct(1, 1)).thenThrow(UnauthorizedAccessException.class);
        ResponseEntity<?> response = new ResponseEntity<>(loginError, HttpStatus.FORBIDDEN);
        assertEquals(response.getStatusCode(), adminController.getProduct(1, 1).getStatusCode());
    }

    @Test
    public void testGetProductFailureDueToNoProductFound() throws UnauthorizedAccessException {
        Mockito.when(adminService.getProduct(1, 1)).thenReturn(null);
        ErrorResponse errorResponse = new ErrorResponse(404, "Product Not Found");
        ResponseEntity<?> response = new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        assertEquals(response.getStatusCode(), adminController.getProduct(1, 1).getStatusCode());
    }

    @Test
    public void testSaveProductSuccess() throws UnauthorizedAccessException {
        Mockito.when(adminService.saveProduct(product, 1)).thenReturn(product);
        SuccessResponse successResponse = new SuccessResponse(200, "Inserted Product");
        ResponseEntity<?> response = new ResponseEntity<>(successResponse, HttpStatus.OK);
        assertEquals(response.getStatusCode(), adminController.saveProduct(product, 1).getStatusCode());
    }

    @Test
    public void testSaveProductFailureDueToLoginFailed() throws UnauthorizedAccessException {
        Mockito.when(adminService.saveProduct(product, 1)).thenThrow(UnauthorizedAccessException.class);
        ResponseEntity<?> response = new ResponseEntity<>(loginError, HttpStatus.FORBIDDEN);
        assertEquals(response.getStatusCode(), adminController.saveProduct(product, 1).getStatusCode());
    }

    @Test
    public void testSaveProductWithUserError() throws UnauthorizedAccessException {
        Product failedProduct = new Product(1, null, 0, "desc", new Category(1, "category"));
        Mockito.when(adminService.saveProduct(failedProduct, 1)).thenThrow(IllegalArgumentException.class);
        ResponseEntity<?> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        assertEquals(response.getStatusCode(), adminController.saveProduct(failedProduct, 1).getStatusCode());
    }

    @Test
    public void testSaveProductWithServerError() throws UnauthorizedAccessException {
        Mockito.when(adminService.saveProduct(product, 1)).thenReturn(null);
        ErrorResponse errorResponse = new ErrorResponse(500, "Internal Server Error");
        ResponseEntity<?> response = new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        assertEquals(response.getStatusCode(), adminController.saveProduct(product, 1).getStatusCode());
    }

    @Test
    public void testDeleteProductWithLoginFailed() throws UnauthorizedAccessException {
        Mockito.when(adminService.deleteProduct(1, 1)).thenThrow(UnauthorizedAccessException.class);
        ResponseEntity<?> response = new ResponseEntity<>(loginError, HttpStatus.FORBIDDEN);
        assertEquals(response.getStatusCode(), adminController.deleteProduct(1, 1).getStatusCode());
    }

    @Test
    public void testDeleteProductWithInternalServerError() throws UnauthorizedAccessException {
        Mockito.when(adminService.deleteProduct(1, 1)).thenReturn(false);
        ErrorResponse errorResponse = new ErrorResponse(500, "Internal Server Error");
        ResponseEntity<?> response = new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        assertEquals(response.getStatusCode(), adminController.deleteProduct(1, 1).getStatusCode());
    }

    @Test
    public void testDeleteProductSuccess() throws UnauthorizedAccessException {
        Mockito.when(adminService.deleteProduct(1, 1)).thenReturn(true);
        SuccessResponse successResponse = new SuccessResponse(200, "Product Deleted");
        ResponseEntity<?> response = new ResponseEntity<>(successResponse, HttpStatus.OK);
        assertEquals(response.getStatusCode(), adminController.deleteProduct(1, 1).getStatusCode());
    }

    @Test
    public void testGetAllProductsWithLoginFailed() throws UnauthorizedAccessException {
        Mockito.when(adminService.getAllProducts(1)).thenThrow(UnauthorizedAccessException.class);
        ResponseEntity<?> response = new ResponseEntity<>(loginError, HttpStatus.FORBIDDEN);
        assertEquals(response.getStatusCode(), adminController.getAllProducts(1).getStatusCode());
    }

    @Test
    public void testGetAllProductsNoProductFound() throws UnauthorizedAccessException {
        Mockito.when(adminService.getAllProducts(1)).thenReturn(null);
        ErrorResponse errorResponse = new ErrorResponse(404, "No Products found");
        ResponseEntity<?> response = new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        assertEquals(response.getStatusCode(), adminController.getAllProducts(1).getStatusCode());
    }

    @Test
    public void testGetAllProductsSuccess() throws UnauthorizedAccessException {
        List<Product> list = new ArrayList<>();
        list.add(product);
        Mockito.when(adminService.getAllProducts(1)).thenReturn(list);
        ListResponse listResponse = new ListResponse(list, 200, "Success");
        ResponseEntity<?> response = new ResponseEntity<>(listResponse, HttpStatus.OK);
        assertEquals(response.getStatusCode(), adminController.getAllProducts(1).getStatusCode());
    }

    @Test
    public void testGetAllCategoriesSuccess() throws UnauthorizedAccessException {
        List<Category> categories = new ArrayList<>();
        categories.add(category);
        Mockito.when(adminService.getAllCategories(1)).thenReturn(categories);
        ListResponse listResponse = new ListResponse(categories, 200, "Success");
        ResponseEntity<?> response = new ResponseEntity<>(listResponse, HttpStatus.OK);
        assertEquals(response.getStatusCode(), adminController.getAllCategories(1).getStatusCode());
    }

    @Test
    public void testGetAllCategoriesNoCategoryFound() throws UnauthorizedAccessException {
        Mockito.when(adminService.getAllCategories(1)).thenReturn(null);
        ErrorResponse errorResponse = new ErrorResponse(404, "No Categories found");
        ResponseEntity<?> response = new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        assertEquals(response.getStatusCode(), adminController.getAllCategories(1).getStatusCode());
    }

    @Test
    public void testGetAllCategoriesLoginFailed() throws UnauthorizedAccessException {
        Mockito.when(adminService.getAllCategories(1)).thenThrow(UnauthorizedAccessException.class);
        ResponseEntity<?> response = new ResponseEntity<>(loginError, HttpStatus.FORBIDDEN);
        assertEquals(response.getStatusCode(), adminController.getAllCategories(1).getStatusCode());
    }

    @Test
    public void testSaveCategorySuccess() throws UnauthorizedAccessException {
        Mockito.when(adminService.insertCategory(category, 1)).thenReturn(category);
        SuccessResponse successResponse = new SuccessResponse(200, "Inserted Category");
        ResponseEntity<?> response = new ResponseEntity<>(successResponse, HttpStatus.OK);
        assertEquals(response.getStatusCode(), adminController.saveCategory(category, 1).getStatusCode());
    }

    @Test
    public void testSaveCategoryWithLoginFailed() throws UnauthorizedAccessException {
        Mockito.when(adminService.insertCategory(category, 1)).thenThrow(UnauthorizedAccessException.class);
        ResponseEntity<?> response = new ResponseEntity<>(loginError, HttpStatus.FORBIDDEN);
        assertEquals(response.getStatusCode(), adminController.saveCategory(category, 1).getStatusCode());
    }

    @Test
    public void testSaveCategoryInternalServerError() throws UnauthorizedAccessException {
        Mockito.when(adminService.insertCategory(category, 1)).thenReturn(null);
        ErrorResponse errorResponse = new ErrorResponse(500, "Internal Server Error");
        ResponseEntity<?> response = new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        assertEquals(response.getStatusCode(), adminController.saveCategory(category, 1).getStatusCode());
    }

    @Test
    public void testGetAllUsersSuccess() throws UnauthorizedAccessException {
        List<User> users = new ArrayList<>();
        users.add(user);
        Mockito.when(adminService.findAllUsers(1)).thenReturn(users);
        ListResponse listResponse = new ListResponse(users, 200, "Success");
        ResponseEntity<?> response = new ResponseEntity<>(listResponse, HttpStatus.OK);
        assertEquals(response.getStatusCode(), adminController.getAllUsers(1).getStatusCode());
    }

    @Test
    public void testGetAllUsersNoUserFound() throws UnauthorizedAccessException {
        Mockito.when(adminService.findAllUsers(1)).thenReturn(null);
        ErrorResponse errorResponse = new ErrorResponse(404, "No Users found");
        ResponseEntity<?> response = new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        assertEquals(response.getStatusCode(), adminController.getAllUsers(1).getStatusCode());
    }

    @Test
    public void testGetAllUsersWithoutLogin() throws UnauthorizedAccessException {
        Mockito.when(adminService.findAllUsers(1)).thenThrow(UnauthorizedAccessException.class);
        ResponseEntity<?> response = new ResponseEntity<>(loginError, HttpStatus.FORBIDDEN);
        assertEquals(response.getStatusCode(), adminController.getAllUsers(1).getStatusCode());
    }

    @Test
    public void testGetUserWithoutLogin() throws UnauthorizedAccessException {
        Mockito.when(adminService.findUserById(1, 1)).thenThrow(UnauthorizedAccessException.class);
        ResponseEntity<?> response = new ResponseEntity<>(loginError, HttpStatus.FORBIDDEN);
        assertEquals(response.getStatusCode(), adminController.getUser(1, 1).getStatusCode());
    }

    @Test
    public void testGetUserSuccess() throws UnauthorizedAccessException {
        Mockito.when(adminService.findUserById(1, 1)).thenReturn(user);
        UserResponse userResponse = new UserResponse(user.getUser_id(), 200, user.getUserName(), user.getEmail(), "Success");
        ResponseEntity<?> response = new ResponseEntity<>(userResponse, HttpStatus.OK);
        assertEquals(response.getStatusCode(), adminController.getUser(1, 1).getStatusCode());
    }

    @Test
    public void testGetUserNotFound() throws UnauthorizedAccessException {
        Mockito.when(adminService.findUserById(1, 1)).thenReturn(null);
        ErrorResponse errorResponse = new ErrorResponse(404, "User Not found");
        ResponseEntity<?> response = new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        assertEquals(response.getStatusCode(), adminController.getUser(1, 1).getStatusCode());
    }

    @Test
    public void testLogoutSuccess() {
        Mockito.when(adminService.logout(1)).thenReturn(true);
        SuccessResponse successResponse = new SuccessResponse(200, "LogOut Success");
        ResponseEntity<?> response = new ResponseEntity<>(successResponse, HttpStatus.OK);
        assertEquals(response.getStatusCode(), adminController.logOut(1).getStatusCode());
    }

    @Test
    public void testLogOutFail() {
        Mockito.when(adminService.logout(1)).thenReturn(false);
        ErrorResponse errorResponse = new ErrorResponse(500, "Internal Server Error");
        ResponseEntity<?> response = new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        assertEquals(response.getStatusCode(), adminController.logOut(1).getStatusCode());
    }
}