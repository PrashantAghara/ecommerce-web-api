package api.ecommerce.startapp.services.implementation;
import api.ecommerce.startapp.dao.UserDao;
import api.ecommerce.startapp.entities.CartItems;
import api.ecommerce.startapp.entities.Product;
import api.ecommerce.startapp.entities.User;
import api.ecommerce.startapp.exceptions.UnauthorizedAccessException;
import api.ecommerce.startapp.services.CartItemsService;
import api.ecommerce.startapp.services.ProductService;
import api.ecommerce.startapp.services.UserService;
import api.ecommerce.startapp.responses.CartItemsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    ProductService productService;
    @Autowired
    CartItemsService cartItemsService;

    public User validate(String username,String password){
        User user = this.findByUserName(username);
        if (user != null){
            String userPassword = user.getPassWord();
            if (userPassword.equals(password)){
                user.setLogin(true);
                return userDao.save(user);
            }
            else {
                return null;
            }
        }else {
            return null;
        }
    }

    public boolean isUserLogin(Integer user_id){
        User user = this.findUserById(user_id);
        if(user != null && user.isLogin()){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public User findByUserName(String userName) {
        return userDao.findByUserName(userName);
    }

    @Override
    public User findUserById(Integer id) {
        return userDao.findById(id).get();
    }

    @Override
    public User saveUser(User user) throws IllegalArgumentException{
        if(user.getPassWord().length() < 8){
            throw new IllegalArgumentException("Password length is less than 8");
        }else if (this.findByUserName(user.getUserName())!=null){
            throw new IllegalArgumentException("User Name Already Exists");
        }
        else {
            return userDao.save(user);
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    public List<Product> getAllProducts(Integer user_id) throws UnauthorizedAccessException {
        if (this.isUserLogin(user_id)){
            return productService.getAllProducts();
        }else {
            throw new UnauthorizedAccessException("Please Login First");
        }

    }

    @Override
    public CartItemsResponse getProductsInCart(Integer user_id) throws UnauthorizedAccessException {
        if (this.isUserLogin(user_id)){
            return cartItemsService.getProductsInCart(user_id);
        }else {
            throw new UnauthorizedAccessException("Please Login First");

        }

    }

    @Override
    public CartItems addProduct(Integer user_id, Integer product_id) throws NoSuchElementException, UnauthorizedAccessException {
        if (this.isUserLogin(user_id)){
            return cartItemsService.addProduct(user_id,product_id);
        }else {
            throw new UnauthorizedAccessException("Please Login First");
        }

    }

    @Override
    public CartItems removeProduct(Integer user_id, Integer product_id) throws UnauthorizedAccessException {
        if (this.isUserLogin(user_id)){
            return cartItemsService.removeProduct(user_id,product_id);
        }else {
            throw new UnauthorizedAccessException("Please Login First");
        }
    }

    private void updateUser(User user){
        User existUser = this.findUserById(user.getUser_id());
        if (existUser != null){
            userDao.save(existUser);
        }
    }

    @Override
    public boolean logout(Integer user_id) {
        User user = this.findUserById(user_id);
        if (user != null && user.isLogin()){
            user.setLogin(false);
            this.updateUser(user);
            return true;
        }else {
            return false;
        }
    }
}
