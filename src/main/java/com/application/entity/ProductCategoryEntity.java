package com.application.entity;

import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "productCategory")
@Entity
@ToString
public class ProductCategoryEntity implements Serializable {

    private static final long serialVersionUID = -5145962340365302571L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productCategoryId;

    @Column
    private String categoryName;

    @Column
    private Date lastUpdatedAt;

    // navigate to view after successfully save..
    // create/get  product category information from  fixed table..
    // transaction -> two option --  Purchase(Stock Purchase, Payment, Invoice and Email Generation, Daily report) & Sale(Sale of Stock)
    
}
