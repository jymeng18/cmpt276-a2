package com.example.cmpt276.a2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.cmpt276.a2.model.StaffRating;
import java.util.Optional;

public interface StaffRatingRepository extends JpaRepository<StaffRating, Long> {
  Optional<StaffRating> findByEmail(String email);
}
