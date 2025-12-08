package com.koushik.expansetracker.repository.finance;

import com.koushik.expansetracker.entity.finance.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUserIdOrUserIdIsNull(Long userId);

    List<Category> findByUserId(Long userId);
}
