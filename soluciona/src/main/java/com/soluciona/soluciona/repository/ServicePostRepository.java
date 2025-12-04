package com.soluciona.soluciona.repository;

import com.soluciona.soluciona.model.ServicePost;
import com.soluciona.soluciona.enums.ServicePostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicePostRepository extends JpaRepository<ServicePost, Long> {
    List<ServicePost> findByStatus(ServicePostStatus status);
}