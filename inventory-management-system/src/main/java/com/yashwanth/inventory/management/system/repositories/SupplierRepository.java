package com.yashwanth.inventory.management.system.repositories;

import com.yashwanth.inventory.management.system.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
