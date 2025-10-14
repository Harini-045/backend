package com.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class StateDto {

    @JsonProperty("city_id")
    public long cityId;

    @JsonProperty("city_name")
    public String cityName;

    @JsonProperty("state_id")
    public long stateId;

    @JsonProperty("state_code")
    public String stateCode;

    @JsonProperty("state_name")
    public String stateName;

    @JsonProperty("country_id")
    public long countryId;

    @JsonProperty("country_code")
    public String countryCode;

    @JsonProperty("country_name")
    public String countryName;

    public String latitude;

    public String longitude;

    public StateDto(long cityId, String cityName, long stateId, String stateCode,
                    String stateName, long countryId, String countryCode, String countryName,
                    String latitude, String longitude) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.stateId = stateId;
        this.stateCode = stateCode;
        this.stateName = stateName;
        this.countryId = countryId;
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
