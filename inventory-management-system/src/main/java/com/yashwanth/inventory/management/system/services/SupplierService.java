package com.yashwanth.inventory.management.system.services;

import com.yashwanth.inventory.management.system.dtos.Response;
import com.yashwanth.inventory.management.system.dtos.SupplierDTO;

public interface SupplierService {

    Response addSupplier(SupplierDTO supplierDTO);

    Response updateSupplier(Long id, SupplierDTO supplierDTO);

    Response getAllSupplier();

    Response getSupplierById(Long id);

    Response deleteSupplier(Long id);

}
