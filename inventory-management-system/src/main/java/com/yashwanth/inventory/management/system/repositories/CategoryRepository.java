package com.yashwanth.inventory.management.system.repositories;

import com.yashwanth.inventory.management.system.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
