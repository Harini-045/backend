package com.application.dto;

import com.application.config.Constants;
import lombok.Data;

@Data
public class RoleDto {

    private Integer roleId;

    private Constants.ERole name;

    private boolean isActive;

}
