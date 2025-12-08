package com.koushik.expansetracker.service.finance.interfaces;

import com.koushik.expansetracker.entity.finance.Category;

import java.util.List;

public interface CategoryServiceInterface {

    Category createCategory(Category category);

    List<Category> getUserCategories(Long userId);

    List<Category> getAllCategoriesIncludingDefault(Long userId);

    Category getCategoryById(Long categoryId);

    void deleteCategory(Long categoryId);
}
