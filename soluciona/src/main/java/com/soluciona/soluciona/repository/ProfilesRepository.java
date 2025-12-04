package com.soluciona.soluciona.repository;

import com.soluciona.soluciona.model.Profiles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProfilesRepository extends JpaRepository<Profiles, UUID> {
}
