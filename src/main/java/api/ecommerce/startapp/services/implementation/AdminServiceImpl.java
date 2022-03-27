package api.ecommerce.startapp.services.implementation;
import api.ecommerce.startapp.dao.AdminDao;
import api.ecommerce.startapp.entities.Admin;
import api.ecommerce.startapp.entities.Category;
import api.ecommerce.startapp.entities.Product;
import api.ecommerce.startapp.entities.User;
import api.ecommerce.startapp.exceptions.UnauthorizedAccessException;
import api.ecommerce.startapp.services.AdminService;
import api.ecommerce.startapp.services.CategoryService;
import api.ecommerce.startapp.services.ProductService;
import api.ecommerce.startapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminDao adminDao;
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    UserService userService;

    @Override
    public Admin validate(String username, String password) {
        Admin user = adminDao.findByUserName(username);
        if (user != null) {
            String userPassword = user.getPassWord();
            if (userPassword.equals(password)) {
                user.setLogin(true);
                adminDao.save(user);
                return user;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public boolean isAdminLogin(Integer adminId) {
        Admin admin = adminDao.getById(adminId);
        return admin != null && admin.isLogin();
    }

    @Override
    public Admin findByUserName(String userName) {
        return adminDao.findByUserName(userName);
    }

    @Override
    public Product getProduct(Integer id, Integer adminId) throws UnauthorizedAccessException, NoSuchElementException {
        if (this.isAdminLogin(adminId)) {
            return productService.getProduct(id);
        } else {
            throw new UnauthorizedAccessException("Please Login First");
        }

    }

    @Override
    public Product saveProduct(Product product, Integer adminId) throws UnauthorizedAccessException ,IllegalArgumentException{
        if (this.isAdminLogin(adminId)) {
            return productService.saveProduct(product);
        } else {
            throw new UnauthorizedAccessException("Please Login First");
        }
    }

    @Override
    public List<Product> getAllProducts(Integer adminId) throws UnauthorizedAccessException {
        if (this.isAdminLogin(adminId))
            return productService.getAllProducts();
        else
            throw new UnauthorizedAccessException("Please Login First");
    }

    @Override
    public boolean deleteProduct(Integer id, Integer adminId) throws UnauthorizedAccessException, EmptyResultDataAccessException {
        if (this.isAdminLogin(adminId)){
            productService.deleteProduct(id);
            return true;
        }
        else
            throw new UnauthorizedAccessException("Please Login First");
    }

    @Override
    public List<Category> getAllCategories(Integer adminId) throws UnauthorizedAccessException {
        if (this.isAdminLogin(adminId)) {
            return categoryService.getAllCategories();
        } else {
            throw new UnauthorizedAccessException("Please Login First");
        }
    }

    @Override
    public Category insertCategory(Category category, Integer adminId) throws UnauthorizedAccessException {
        if (this.isAdminLogin(adminId))
            return categoryService.insertCategory(category);
        else
            throw new UnauthorizedAccessException("Please Login First");
    }

    @Override
    public User findUserById(Integer id, Integer adminId) throws UnauthorizedAccessException {
        if (this.isAdminLogin(adminId))
            return userService.findUserById(id);
        else
            throw new UnauthorizedAccessException("Please Login First");
    }

    @Override
    public List<User> findAllUsers(Integer adminId) throws UnauthorizedAccessException {
        if (this.isAdminLogin(adminId))
            return userService.getAllUsers();
        else
            throw new UnauthorizedAccessException("Please Login First");
    }

    @Override
    public boolean logout(Integer user_id) {
        Admin admin = adminDao.findById(user_id).get();
        if (admin != null && admin.isLogin()) {
            admin.setLogin(false);
            adminDao.save(admin);
            return true;
        } else
            return false;
    }

    
}
