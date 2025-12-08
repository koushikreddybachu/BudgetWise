package com.koushik.expansetracker.service.finance.implementations;

import com.koushik.expansetracker.entity.finance.Category;
import com.koushik.expansetracker.repository.finance.CategoryRepository;
import com.koushik.expansetracker.service.finance.interfaces.CategoryServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryServiceInterface {

    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getUserCategories(Long userId) {
        return categoryRepository.findByUserId(userId);
    }

    @Override
    public List<Category> getAllCategoriesIncludingDefault(Long userId) {
        return categoryRepository.findByUserIdOrUserIdIsNull(userId);
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
    }

    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
