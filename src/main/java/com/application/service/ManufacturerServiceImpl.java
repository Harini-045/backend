package com.application.service;

import com.application.config.Constants;
import com.application.dto.ManufacturerDto;
import com.application.entity.ManufacturerEntity;
import com.application.exception.ServerException;
import com.application.dto.ManufacturerDto.*;
import com.application.mapper.ManufacturerResponseView;
import com.application.mapper.ObjectUtils;
import com.application.repository.ManufacturerRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ManufacturerServiceImpl implements ManufacturerService {

    private final ManufacturerRepo manufacturerRepo;

    @Autowired
    private DataService dataService;

    @Autowired
    private MongoFileService mongoFileService;

    public ManufacturerServiceImpl(ManufacturerRepo manufacturerRepo) {
        this.manufacturerRepo = manufacturerRepo;
    }

    @Override
    public List<ManufacturerDto> getAll() {
        List<ManufacturerEntity> entities = manufacturerRepo.findAll();
        return entities.stream().map(manufacturerEntity -> (ManufacturerDto) ObjectUtils.map(manufacturerEntity, new ManufacturerDto())).collect(Collectors.toList());
    }

    @Override
    public ManufacturerDto getByManufacturerId(long manufacturerId) {
        return manufacturerRepo.findById(manufacturerId).map(manufacturerEntity -> (ManufacturerDto) ObjectUtils.map(manufacturerEntity, new ManufacturerDto())).orElse(null);
    }

    @Override
    public List<ManufacturerDto> getByManufacturerName(String manufacturerName) {
        return manufacturerRepo.findByManufacturerCompanyName(manufacturerName).stream().map(manufacturerEntity -> (ManufacturerDto) ObjectUtils.map(manufacturerEntity, new ManufacturerDto())).collect(Collectors.toList());
    }

    @Override
    public List<ManufacturerDto> getManufacturerNames() {
        List<ManufacturerResponseView> data = manufacturerRepo.findAllProjectedBy();
        return data.stream().map(manufacturerEntity -> (ManufacturerDto) ObjectUtils.map(manufacturerEntity, new ManufacturerDto())).collect(Collectors.toList());
    }

    @Override
    public List<ManufacturerDto> createOrUpdateData(String data, MultipartFile file) {
        ManufacturerDto manufacturerDto;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            manufacturerDto = objectMapper.readValue(data, ManufacturerDto.class);
        } catch (Exception e) {
            throw new ServerException("Invalid Request Body", Constants.INTERNAL_SERVER, Constants.MANF_CREATE_URL, Constants.MANF_CREATE_METHOD);
        }
        log.info("Manufacturer Data received -- " + manufacturerDto.toString());
        if (Boolean.TRUE.equals(ObjectUtils.isEmptyOrNull(manufacturerDto.getManufacturerCompanyName()))) {
            throw new ServerException("Pass Valid Company Name", Constants.INTERNAL_SERVER, Constants.MANF_CREATE_URL, Constants.MANF_CREATE_METHOD);
        } else {
            List<ManufacturerDto> manufacturerList = getByManufacturerName(manufacturerDto.getManufacturerCompanyName());
            if (manufacturerList.isEmpty()) {
                manufacturerDto.setEnabled(true);
                manufacturerDto.setCreatedOn(new Date());
                manufacturerDto.setUpdatedOn(new Date());
                ManufacturerEntity manufacturerEntity = (ManufacturerEntity) ObjectUtils.map(manufacturerDto, new ManufacturerEntity());
                manufacturerEntity = manufacturerRepo.save(manufacturerEntity);
                try {
                    if (file != null) {
                        String fileId = mongoFileService.addFile(file, manufacturerEntity.getManufacturerId().intValue(), manufacturerEntity.getManufacturerCompanyName());
                        if (Boolean.TRUE.equals(ObjectUtils.isEmptyOrNull(fileId))) {
                            throw new ServerException("Error occurred while uploading image", Constants.INTERNAL_SERVER, Constants.MANF_CREATE_URL, Constants.MANF_CREATE_METHOD);
                        } else {
                            manufacturerEntity.setFileId(fileId);
                        }
                    }
                } catch (Exception e) {
                    throw new ServerException("Error occurred while uploading image", Constants.INTERNAL_SERVER, Constants.MANF_CREATE_URL, Constants.MANF_CREATE_METHOD);
                }
                manufacturerRepo.save(manufacturerEntity);
                return getAll();
            } else {
                throw new ServerException("Company Name is already present in database", Constants.INTERNAL_SERVER, Constants.MANF_CREATE_URL, Constants.MANF_CREATE_METHOD);
            }
        }
    }

    @Override
    public int getManufacturerCount() {
        return (int) manufacturerRepo.count();
    }

    @Override
    public long getRecentlyAddedCount() {
        Date pastDate = new Date(System.currentTimeMillis() - 5L * 24 * 3600 * 1000);
        return manufacturerRepo.getRecentlyAdded(pastDate, new Date());
    }

}
