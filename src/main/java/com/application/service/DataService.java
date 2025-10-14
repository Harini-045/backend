package com.application.service;

import com.application.dto.CityDto;
import com.application.dto.OverviewResponse;
import com.application.dto.StateDto;
import com.application.exception.BusinessGlobalException;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface DataService {

    List<StateDto> getStates(String countryName);

    List<CityDto> getCities(String stateName);

    ResponseEntity<Resource> generateXcelSheet(String dataType, HttpServletRequest request) throws BusinessGlobalException;

    OverviewResponse getOverview();

}
