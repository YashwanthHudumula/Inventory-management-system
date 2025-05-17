package com.yashwanth.inventory.management.system.services;

import com.yashwanth.inventory.management.system.dtos.CategoryDTO;
import com.yashwanth.inventory.management.system.dtos.Response;

public interface CategoryService {

    Response createCategory(CategoryDTO categoryDTO);

    Response getAllCategories();

    Response getCategoryById(Long id);

    Response updateCategory(Long id, CategoryDTO categoryDTO);

    Response deleteCategory(Long id);
}