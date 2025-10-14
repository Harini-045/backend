package com.application.service;

import com.application.dto.ManufacturerDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ManufacturerService {

    List<ManufacturerDto> getAll();

    ManufacturerDto getByManufacturerId(long manufacturerId);

    List<ManufacturerDto> getByManufacturerName(String manufacturerName);

    List<ManufacturerDto> getManufacturerNames();

    List<ManufacturerDto> createOrUpdateData(String manufacturerDto, MultipartFile file);

    int getManufacturerCount();

    long getRecentlyAddedCount();

}
