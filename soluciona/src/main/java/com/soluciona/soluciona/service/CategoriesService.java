package com.soluciona.soluciona.service;

import com.soluciona.soluciona.model.Categories;
import com.soluciona.soluciona.repository.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriesService {

    @Autowired
    private CategoriesRepository categoriesRepository;

    public Categories createCategory(Categories category){
        if (categoriesRepository.findBySlug(category.getSlug()).isPresent()){
            throw new RuntimeException("Slug já existe!");
        }
        return categoriesRepository.save(category);
    }

    public List<Categories> listAllCategories(){
        return categoriesRepository.findAll();
    }

    public Optional<Categories> findCategoryById(Long id){
        return categoriesRepository.findById(id);
    }

    public void deleteCategory(Long id){
        categoriesRepository.deleteById(id);
    }

}
