package api.ecommerce.startapp.services.implementation;
import api.ecommerce.startapp.dao.CartItemsDao;
import api.ecommerce.startapp.entities.CartItems;
import api.ecommerce.startapp.entities.Product;
import api.ecommerce.startapp.entities.User;
import api.ecommerce.startapp.services.CartItemsService;
import api.ecommerce.startapp.services.ProductService;
import api.ecommerce.startapp.services.UserService;
import api.ecommerce.startapp.responses.CartItemsResponse;
import api.ecommerce.startapp.utils.CartProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class CartItemsServiceImpl implements CartItemsService {
    @Autowired
    CartItemsDao cartItemsDao;
    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;

    @Override
    public CartItemsResponse getProductsInCart(Integer user_id) {
        User user = new User();
        user.setUser_id(user_id);
        List<CartItems> myCartItems = cartItemsDao.findByUser(user);
        List<CartProduct> list = new ArrayList<>();
        for (CartItems cartItems : myCartItems){
            CartProduct cartProduct = new CartProduct(cartItems.getProduct(),cartItems.getPrice(),cartItems.getQuantity());
            list.add(cartProduct);
        }
        CartItemsResponse cartItemsResponse = new CartItemsResponse();
        cartItemsResponse.setData(list);
        return cartItemsResponse;
    }

    @Override
    public CartItems addProduct(Integer user_id, Integer product_id) throws NoSuchElementException {
        User user = new User();
        user.setUser_id(user_id);
        Product product = new Product();
        product.setProduct_id(product_id);
        CartItems cartItems = new CartItems();
        CartItems existItem = cartItemsDao.findByUserAndProduct(user,product);
        if (existItem == null){
            CartItems newItem = new CartItems(userService.findUserById(user_id),productService.getProduct(product_id),1,productService.getProduct(product_id).getPrice());
            return cartItemsDao.save(newItem);
        }else {
            cartItems.setUser(userService.findUserById(existItem.getUser().getUser_id()));
            cartItems.setProduct(productService.getProduct(existItem.getProduct().getProduct_id()));
            cartItems.setQuantity(existItem.getQuantity()+1);
            cartItems.setPrice(existItem.getPrice() + existItem.getPrice());
        }
        return cartItemsDao.save(cartItems);
    }

    @Override
    public CartItems removeProduct(Integer user_id, Integer product_id){
        User user = new User();
        user.setUser_id(user_id);
        Product product = new Product();
        product.setProduct_id(product_id);
        CartItems existItem = cartItemsDao.findByUserAndProduct(user,product);
        if (existItem == null){
            return null;
        }else{
            if (existItem.getQuantity() > 1){
                int qty = existItem.getQuantity() - 1;
                int price = existItem.getPrice() - productService.getProduct(existItem.getProduct().getProduct_id()).getPrice();
                existItem.setQuantity(qty);
                existItem.setPrice(price);
                return cartItemsDao.save(existItem);
            }else {
                cartItemsDao.delete(existItem);
                return existItem;
            }
        }
    }
}
