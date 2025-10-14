package com.application.service;

import com.application.dto.ProductCategoryDto;
import com.application.mapper.ObjectUtils;
import com.application.repository.ProductCategoryRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepo productCategoryRepo;

    public ProductCategoryServiceImpl(ProductCategoryRepo productCategoryRepo) {
        this.productCategoryRepo = productCategoryRepo;
    }

    @Override
    public List<ProductCategoryDto> getProductCategories() {
        return productCategoryRepo.findAll().stream().map(
                productCategories -> (ProductCategoryDto) ObjectUtils.map(productCategories, new ProductCategoryDto()))
                .collect(Collectors.toList());
    }

    @Override
    public int getProductCategoryCount(){
        return (int)productCategoryRepo.count();
    }

}
