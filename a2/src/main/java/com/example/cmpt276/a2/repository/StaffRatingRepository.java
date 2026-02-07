package com.example.cmpt276.a2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.cmpt276.a2.model.StaffRating;

public interface StaffRatingRepository extends JpaRepository<StaffRating, Long> {
  // note: no custom methods needed for now
}
