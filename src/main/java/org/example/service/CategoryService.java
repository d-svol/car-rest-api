package org.example.service;

import org.example.model.Category;
import org.example.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private CategoryRepository categoryRepository;
    private Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Transactional
    public Category saveCategory(Category category) {
        categoryRepository.save(category);
        logger.info("Saved category with id: {}", category.getId());
        return category;
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    public Optional<Category> getByName(String categoryName) {
        return categoryRepository.findByName(categoryName);
    }

    @Transactional
    public Category update(Category category, Long categoryId) {
        category.setId(categoryId);
        categoryRepository.save(category);
        logger.info("Updated category with id: {}", category.getId());
        return category;
    }

    @Transactional
    public void deleteById(Long categoryId) {
        categoryRepository.deleteById(categoryId);
        logger.info("Deleted category with id: {}", categoryId);
    }
}
