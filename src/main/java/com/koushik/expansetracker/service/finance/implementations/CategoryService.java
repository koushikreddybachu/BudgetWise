package com.koushik.expansetracker.service.finance.implementations;

import com.koushik.expansetracker.entity.finance.Category;
import com.koushik.expansetracker.repository.finance.CategoryRepository;
import com.koushik.expansetracker.security.OwnershipValidator;
import com.koushik.expansetracker.service.finance.interfaces.CategoryServiceInterface;
import com.koushik.expansetracker.util.SecurityUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements CategoryServiceInterface {

    private final CategoryRepository categoryRepository;
    private final OwnershipValidator ownershipValidator;

    public CategoryService(CategoryRepository categoryRepository,
                           OwnershipValidator ownershipValidator) {
        this.categoryRepository = categoryRepository;
        this.ownershipValidator = ownershipValidator;
    }

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
        // Assuming default categories have userId = null
        return categoryRepository.findByUserIdOrUserIdIsNull(userId);
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        ownershipValidator.validateCategory(categoryId, currentUserId);

        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        ownershipValidator.validateCategory(categoryId, currentUserId);
        categoryRepository.deleteById(categoryId);
    }
}
