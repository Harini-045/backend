package com.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.io.Serializable;

@Data
@NoArgsConstructor
@ToString
public class TransactionDto implements Serializable {

    private static final long serialVersionUID = -2683865501922998906L;

    private Long transactionId;

    private String transactionType;

    //this has to be valid manufacturerId(nullable = false)
    private Long manufacturerId;

    private String manufacturerName;

    //this has to be valid productId(nullable = false)
    private Long productId;

    private String productName;

    private String productCategoryName;

    private double unitCost;

    private long noOfUnits;

    private double weightOfUnit;

    private double totalWeight;

    //Amount Paid and productValue has to be same
    private double productValue;

    private double amountPaid;

    private String paymentMethod;

    private String payeeName;

    private String phoneNumber;

    private String emailAddress;

    private String paymentMetaInfo;

}
