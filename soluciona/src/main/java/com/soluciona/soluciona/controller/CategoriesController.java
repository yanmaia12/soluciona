package com.soluciona.soluciona.controller;

import com.soluciona.soluciona.model.Categories;
import com.soluciona.soluciona.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {

    @Autowired
    private CategoriesService categoriesService;

    @PostMapping
    public ResponseEntity<Categories> createCategory(@RequestBody Categories categories){
        Categories newCategory = categoriesService.createCategory(categories);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Categories> listCategories(){
        return categoriesService.listAllCategories();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        categoriesService.deleteCategory(id);
        String mensagem = "Categoria apagada com sucesso!";
        return ResponseEntity.ok(mensagem);
    }

}
