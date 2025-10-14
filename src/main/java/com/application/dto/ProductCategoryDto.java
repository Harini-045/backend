package com.application.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class ProductCategoryDto implements Serializable {

    private static final long serialVersionUID = -6298641096908510765L;

    private Long productCategoryId;

    private String categoryName;

    private Date lastUpdatedAt;

}
