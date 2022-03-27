package api.ecommerce.startapp.services.implementation;

import api.ecommerce.startapp.dao.AdminDao;
import api.ecommerce.startapp.entities.Admin;
import api.ecommerce.startapp.entities.Category;
import api.ecommerce.startapp.entities.Product;
import api.ecommerce.startapp.entities.User;
import api.ecommerce.startapp.exceptions.UnauthorizedAccessException;
import api.ecommerce.startapp.services.CategoryService;
import api.ecommerce.startapp.services.ProductService;
import api.ecommerce.startapp.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdminServiceImplTest {

    AdminDao adminDao = Mockito.mock(AdminDao.class);
    ProductService productService = Mockito.mock(ProductServiceImpl.class);
    CategoryService categoryService = Mockito.mock(CategoryServiceImpl.class);
    UserService userService = Mockito.mock(UserService.class);
    AdminServiceImpl adminService = new AdminServiceImpl();
    Admin admin = new Admin(1, "admin", "admin", false);
    Product product = new Product(1, "product", 100, "desc", new Category(1, "category"));

    @BeforeEach
    public void initialize() {
        adminService.adminDao = adminDao;
        adminService.productService = productService;
        adminService.userService = userService;
        adminService.categoryService = categoryService;
    }

    @Test
    public void testAdminValidate() {
        Mockito.doReturn(admin).when(adminDao).findByUserName("admin");
        assertEquals(admin, adminService.validate("admin", "admin"));
    }

    @Test
    public void testGetProduct() {
        AdminServiceImpl adminService1 = Mockito.spy(adminService);
        Mockito.doReturn(true).when(adminService1).isAdminLogin(1);
        Product product = new Product(1, "product", 100, "desc", new Category(1, "category"));
        Mockito.doReturn(product).when(productService).getProduct(1);
        Product product1 = new Product();
        try {
            product1 = adminService1.getProduct(1, 1);
        } catch (UnauthorizedAccessException e) {
            e.printStackTrace();
        }
        assertEquals(product, product1);
    }

    @Test
    public void testGetProductWithoutLogin() {
        AdminServiceImpl adminService1 = Mockito.spy(adminService);
        Mockito.doReturn(false).when(adminService1).isAdminLogin(1);
        assertThrows(UnauthorizedAccessException.class, () -> {
            adminService1.getProduct(1, 1);
        });
    }

    @Test
    public void testSaveProductWithLogin() {
        AdminServiceImpl adminService1 = Mockito.spy(adminService);
        Mockito.doReturn(true).when(adminService1).isAdminLogin(1);
        Mockito.doReturn(product).when(productService).saveProduct(product);
        Product product1 = new Product();
        try {
            product1 = adminService1.saveProduct(product, 1);
        } catch (UnauthorizedAccessException e) {
            e.printStackTrace();
        }
        assertEquals(product, product1);
    }

    @Test
    public void testSaveProductWithoutLogin() {
        AdminServiceImpl adminService1 = Mockito.spy(adminService);
        Mockito.doReturn(false).when(adminService1).isAdminLogin(1);
        assertThrows(UnauthorizedAccessException.class, () -> {
            adminService1.saveProduct(product, 1);
        });
    }

    @Test
    public void testGetAllProductsWithLogin() {
        AdminServiceImpl adminService1 = Mockito.spy(adminService);
        Mockito.doReturn(true).when(adminService1).isAdminLogin(1);
        List<Product> products = new ArrayList<>();
        products.add(product);
        Mockito.when(productService.getAllProducts()).thenReturn(products);
        List<Product> gotProducts = new ArrayList<>();
        try {
            gotProducts = adminService1.getAllProducts(1);
        } catch (UnauthorizedAccessException unauthorizedAccessException) {
            unauthorizedAccessException.printStackTrace();
        }
        assertEquals(products, gotProducts);
    }

    @Test
    public void testGetALlProductsWithoutLogin() {
        AdminServiceImpl adminService1 = Mockito.spy(adminService);
        Mockito.doReturn(false).when(adminService1).isAdminLogin(1);
        assertThrows(UnauthorizedAccessException.class, () -> {
            adminService1.getAllProducts(1);
        });
    }

    @Test
    public void testDeleteProductWithoutLogin() {
        AdminServiceImpl adminService1 = Mockito.spy(adminService);
        Mockito.doReturn(false).when(adminService1).isAdminLogin(1);
        assertThrows(UnauthorizedAccessException.class, () -> {
            adminService1.deleteProduct(1, 1);
        });
    }

    @Test
    public void testDeleteProductsWithLogin() throws UnauthorizedAccessException {
        AdminServiceImpl adminService1 = Mockito.spy(adminService);
        Mockito.doReturn(true).when(adminService1).isAdminLogin(1);
        assertTrue(adminService1.deleteProduct(1, 1));
    }

    @Test
    public void testGetAllCategoriesWithLogin() throws UnauthorizedAccessException {
        AdminServiceImpl adminService1 = Mockito.spy(adminService);
        Mockito.doReturn(true).when(adminService1).isAdminLogin(1);
        Category category = new Category(1, "category");
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(category);
        Mockito.when(categoryService.getAllCategories()).thenReturn(categoryList);
        assertEquals(categoryList, adminService1.getAllCategories(1));
    }

    @Test
    public void testGetAllCategoriesWithoutLogin() {
        AdminServiceImpl adminService1 = Mockito.spy(adminService);
        Mockito.doReturn(false).when(adminService1).isAdminLogin(1);
        assertThrows(UnauthorizedAccessException.class, () -> {
            adminService1.getAllCategories(1);
        });
    }

    @Test
    public void testInsertCategoryWithLogin() throws UnauthorizedAccessException {
        AdminServiceImpl adminService1 = Mockito.spy(adminService);
        Mockito.doReturn(true).when(adminService1).isAdminLogin(1);
        Category category = new Category(1, "category");
        Mockito.when(categoryService.insertCategory(category)).thenReturn(category);
        assertEquals(category, adminService1.insertCategory(category, 1));
    }

    @Test
    public void testInsertCategoryWithoutLogin() {
        AdminServiceImpl adminService1 = Mockito.spy(adminService);
        Mockito.doReturn(false).when(adminService1).isAdminLogin(1);
        Category category = new Category(1, "category");
        assertThrows(UnauthorizedAccessException.class, () -> {
            adminService1.insertCategory(category, 1);
        });
    }

    @Test
    public void testFindByUserIdWithLogin() throws UnauthorizedAccessException {
        AdminServiceImpl adminService1 = Mockito.spy(adminService);
        Mockito.doReturn(true).when(adminService1).isAdminLogin(1);
        User user = new User(1, "user", "user@email", "user1234", false);
        Mockito.doReturn(user).when(userService).findUserById(1);
        assertEquals(user, adminService1.findUserById(1, 1));
    }

    @Test
    public void testFindByUserIdWithoutLogin() {
        AdminServiceImpl adminService1 = Mockito.spy(adminService);
        Mockito.doReturn(false).when(adminService1).isAdminLogin(1);
        assertThrows(UnauthorizedAccessException.class, () -> {
            adminService1.findUserById(1, 1);
        });
    }

    @Test
    public void testGetAllUsersWithLogin() throws UnauthorizedAccessException {
        AdminServiceImpl adminService1 = Mockito.spy(adminService);
        Mockito.doReturn(true).when(adminService1).isAdminLogin(1);
        User user = new User(1, "user", "user@email", "user1234", false);
        List<User> list = new ArrayList<>();
        list.add(user);
        Mockito.doReturn(list).when(userService).getAllUsers();
        assertEquals(list, adminService1.findAllUsers(1));
    }

    @Test
    public void testGetAllUsersWithoutLogin() {
        AdminServiceImpl adminService1 = Mockito.spy(adminService);
        Mockito.doReturn(false).when(adminService1).isAdminLogin(1);
        assertThrows(UnauthorizedAccessException.class, () -> {
            adminService1.findAllUsers(1);
        });
    }
}