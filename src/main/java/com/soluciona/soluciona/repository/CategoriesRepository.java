package com.soluciona.soluciona.repository;

import com.soluciona.soluciona.model.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long> {

    Optional<Categories> findBySlug(String slug);
}
