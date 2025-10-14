package com.application.dto;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class OverviewResponse implements Serializable {

    private static final long serialVersionUID = 2222593343104168066L;

    private int totalManufacturers;

    private int totalProducts;

    private int totalProductCategories;

    private String highestProductName;

    private Double highestProductValue;

    private String lowestProductName;

    private Double lowestProductValue;

    private int noOfManufacturersRecAdded;

    private int noOfProductsRecAdded;

}
