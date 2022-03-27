package api.ecommerce.startapp.services;

import api.ecommerce.startapp.entities.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    List<Category> getAllCategories();
    Category insertCategory(Category category);

}
