package com.application.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "manufacturer")
@Entity
@ToString
@Data
public class ManufacturerEntity implements Serializable {

    private static final long serialVersionUID = 8908299509371222908L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long manufacturerId;

    @Column
    private String manufacturerCompanyName;

    @Column
    private String companyEmailAddress;

    @Column
    private Date dateOfReg;

    @Column
    private String regtdAt;

    @Column(length = 12)
    private String phoneNumber;

    @Column(length = 15)
    private String companyGSTIN;

    @Column
    private String street;

    @Column
    private String city;

    @Column
    private String address;

    @Column	
    private String country;

    @Column
    private String state;

    @Column
    private String privilegeCode;

    @Column
    private boolean enabled;

    @Column
    private Date updatedOn;

    @Column
    private Date createdOn;

    @Column
    private String fileId;
}
