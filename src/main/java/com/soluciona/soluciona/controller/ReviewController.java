package com.soluciona.soluciona.controller;

import com.soluciona.soluciona.model.Review;
import com.soluciona.soluciona.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    // Listar avaliações de um serviço
    @GetMapping("/service/{serviceId}")
    public List<Review> getReviewsByService(@PathVariable Long serviceId) {
        return reviewRepository.findByServiceId(serviceId);
    }

    // Criar nova avaliação
    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        return ResponseEntity.ok(reviewRepository.save(review));
    }
}
