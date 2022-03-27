package api.ecommerce.startapp.services.implementation;

import api.ecommerce.startapp.dao.CategoryDao;
import api.ecommerce.startapp.entities.Category;
import api.ecommerce.startapp.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryDao categoryDao;

    @Override
    public List<Category> getAllCategories() {
        return categoryDao.findAll();
    }

    @Override
    public Category insertCategory(Category category) {
        return categoryDao.save(category);
    }

}
