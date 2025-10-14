package com.application.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "transaction")
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class TransactionEntity implements Serializable {

    //delivery details, serial number, product box numbers
    //after payment success, add product with correct info into product table;

    private static final long serialVersionUID = 2222593343104168066L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Column(nullable = false)
    private String transactionType;

    @Column(nullable = false)
    private Long manufacturerId;

    @Column(nullable = false)
    private String manufacturerName;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String productCategoryName;

    @Column
    private double unitCost;

    @Column(nullable = false)
    private long noOfUnits;

    @Column(nullable = false)
    private double weightOfUnit;

    //TotalWeight = noOfUnits * weightOfUnit
    @Column
    private double totalWeight;

    //Amount Paid and productValue has to be same
    @Column
    private double productValue;

    @Column
    private double amountPaid;

    @Column
    private String paymentMethod;

    @Column
    private String payeeName;

    @Column
    private String paymentMetaInfo;

    @Column
    private String phoneNumber;

    @Column
    private String emailAddress;

    @CreatedDate
    private Date createdOn;

    @LastModifiedDate
    private Date lastModifiedDate;

    @Column
    private String operation;

    @PrePersist
    public void OnPrePersist() {
        System.out.println("Product Data Saving...");
        System.out.println(this);
        audit("SAVE");
    }

    @PreUpdate
    public void OnPreUpdate() {
        System.out.println("Product Data is been Updating...");
        audit("UPDATE");
    }

    @PreRemove
    public void onPreRemove() {
        System.out.println("Product Data is been Removing...");
        audit("DELETE");
    }

    private void audit(String operation) {
        this.setOperation(operation);
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

}
