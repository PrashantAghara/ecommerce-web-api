package api.ecommerce.startapp.dao;

import api.ecommerce.startapp.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AdminDao extends JpaRepository<Admin,Integer> {
    Admin findByUserName(String userName);
}
