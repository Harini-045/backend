package com.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class ManufacturerDto implements Serializable {

    private static final long serialVersionUID = 4625872131587365313L;

    private Long manufacturerId;

    private String manufacturerCompanyName;

    private String companyEmailAddress;

    private Date dateOfReg;

    private String regtdAt;

    private String phoneNumber;

    private String companyGSTIN;

    private String street;

    private String city;

    private String address;

    private String state;

    private String country;

    private Date updatedOn;

    private Date createdOn;

    @JsonIgnore
    private String privilegeCode;

    @JsonIgnore
    private boolean enabled;

}
