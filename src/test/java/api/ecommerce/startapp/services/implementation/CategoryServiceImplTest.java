package api.ecommerce.startapp.services.implementation;

import api.ecommerce.startapp.dao.CategoryDao;
import api.ecommerce.startapp.entities.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryServiceImplTest {
    CategoryServiceImpl categoryService = new CategoryServiceImpl();
    CategoryDao categoryDao = Mockito.mock(CategoryDao.class);
    Category category = new Category(1, "category");

    @BeforeEach
    public void beforeEachTest() {
        categoryService.categoryDao = categoryDao;
    }

    @Test
    public void testGetAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(category);
        Mockito.when(categoryDao.findAll()).thenReturn(categoryList);
        assertEquals(categoryList, categoryService.getAllCategories());
    }

    @Test
    public void testInsertCategory() {
        Mockito.when(categoryDao.save(category)).thenReturn(category);
        assertEquals(category, categoryService.insertCategory(category));
    }
}