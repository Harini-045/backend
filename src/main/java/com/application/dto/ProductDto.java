package com.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@ToString
public class ProductDto implements Serializable {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private static final long serialVersionUID = 3297721292670708403L;

    private Long productId;

    private String productName;

    private String productDesc;

    private String productCategory;

    private Long manufacturerId;

    private String manufacturerName;

    private Long noOfUnits;

    private Double weightOfUnit;

    private Double totalWeightOfUnits;

    private Double unitCost;

    private Double landedCost;

    //ActualUnitCost + landedCost = totalCost
    private Double totalCost;

    private Double totalProductValue;

    private Date createdOn;

    private Date lastUpdated;

    private Date productReceived;

    private String productLocation;

    @JsonIgnore
    private String privilegeCode;

    @JsonIgnore
    private boolean enabled;

}
