package com.irem.filmAddicts.repository;

import com.irem.filmAddicts.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
