package com.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CityDto {

    @JsonProperty("city_id")
    public long cityId;

    @JsonProperty("city_name")
    public String cityName;

}

