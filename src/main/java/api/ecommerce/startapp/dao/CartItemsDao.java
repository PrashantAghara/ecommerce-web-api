package api.ecommerce.startapp.dao;

import api.ecommerce.startapp.entities.CartItems;
import api.ecommerce.startapp.entities.Product;
import api.ecommerce.startapp.entities.User;
import api.ecommerce.startapp.utils.UserProductKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CartItemsDao extends JpaRepository<CartItems, UserProductKey> {
    List<CartItems> findByUser(User user);
    CartItems findByUserAndProduct(User user, Product product);
}
