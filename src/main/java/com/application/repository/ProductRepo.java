package com.application.repository;

import com.application.entity.ProductEntity;
import com.application.mapper.ProductResponseView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByProductName(String productName);

    List<ProductEntity> findByManufacturerNameAndProductName(String manufacturerName, String productName);

    @Query(value = "SELECT * FROM product order by product_name desc limit 0,1", nativeQuery = true)
    ProductEntity getHighestValue();

    @Query(value = "SELECT * FROM product order by product_name asc limit 0,1", nativeQuery = true)
    ProductEntity getLowestValue();

    @Query(value = "SELECT COUNT(*) FROM product where created_on BETWEEN :startDate AND :endDate", nativeQuery = true)
    Long getRecentlyAdded(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<ProductEntity> findByManufacturerName(String manufacturerName);

    @Query(value = "SELECT DISTINCT p.product_name as ProductName, p.product_id as ProductId FROM product p", nativeQuery = true)
    List<ProductResponseView> getProductNames();
}
