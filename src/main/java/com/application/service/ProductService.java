package com.application.service;

import com.application.dto.ProductDto;
import com.application.exception.BusinessGlobalException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ProductService {

    List<ProductDto> getAll();

    ProductDto getByProductId(long manufacturerId);

    List<ProductDto> getByProductName(String manufacturerName);

    List<ProductDto> createOrUpdateData(ProductDto productDto,
                                        HttpServletRequest request) throws BusinessGlobalException;

    List<ProductDto> deleteIdById(String id);

    int getProductCount();

    List<ProductDto> getProductNames();

    ProductDto getHighestProductValue();

    ProductDto getLowestProductValue();

    long getRecentlyAddedCount();

    public List<ProductDto> getProductByManufacturerName(String manufacturerName);

}
