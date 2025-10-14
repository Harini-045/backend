package com.application.service;

import com.application.dto.ProductCategoryDto;

import java.util.List;

public interface ProductCategoryService {

    List<ProductCategoryDto> getProductCategories();

    int getProductCategoryCount();

}
