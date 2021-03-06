package api.ecommerce.startapp.dao;

import api.ecommerce.startapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserDao extends JpaRepository<User,Integer> {
    User findByUserName(String userName);
}
