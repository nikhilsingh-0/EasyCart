package com.ecom.product.service;

import com.ecom.product.dto.CategoryRequest;
import com.ecom.product.dto.CategoryResponse;
import com.ecom.product.model.Category;
import com.ecom.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponse createCategory(CategoryRequest request) {
        categoryRepository.findByNameIgnoreCase(request.getName()).ifPresent(c -> {
            throw new RuntimeException("Category already exists");
        });

        Category category = new Category();
        category.setName(request.getName());

        Category saved = categoryRepository.save(category);

        return new CategoryResponse(saved.getId(), saved.getName());
    }

    public CategoryResponse getCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .filter(Category::getActive)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return new CategoryResponse(category.getId(), category.getName());
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findByActiveTrue()
                .stream()
                .map(c -> new CategoryResponse(c.getId(), c.getName()))
                .toList();
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(request.getName());
        Category updated = categoryRepository.save(category);

        return new CategoryResponse(updated.getId(), updated.getName());
    }

    public void deleteCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setActive(false);
        categoryRepository.save(category);
    }
}
