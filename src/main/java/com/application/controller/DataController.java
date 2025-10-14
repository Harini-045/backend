package com.application.controller;

import com.application.dto.*;
import com.application.exception.BusinessGlobalException;
import com.application.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/api/data")
@RequiredArgsConstructor
public class DataController {

    private final DataService dataService;

    private final ProductCategoryService productCategoryService;

    private final MongoFileService mongoFileService;

    @GetMapping(path = "/getStates")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<StateDto> getStateDetails(@RequestParam("countryName") String countryName) {
        return dataService.getStates(countryName);
    }

    @GetMapping(path = "/getCities")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<CityDto> getCityDetails(@RequestParam("stateName") String stateName) {
        return dataService.getCities(stateName);
    }

    @GetMapping(path = "/generateXcel/{dataType}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Resource> generateXcel(@PathVariable("dataType") String dataType,
                                                 HttpServletRequest request) throws BusinessGlobalException {
        return dataService.generateXcelSheet(dataType, request);
    }

    @GetMapping(path = "/getCategory")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<ProductCategoryDto> getCategory() {
        return productCategoryService.getProductCategories();
    }

    @GetMapping(path = "/getOverview")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public OverviewResponse getOverview() {
        return dataService.getOverview();
    }

    @PostMapping(path = "/uploadFile")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String uploadFile(@RequestParam("file") MultipartFile file, @Param("manufacturerId") int manufacturerId,
                             @Param("manufacturerName") String manufacturerName) throws IOException {
        return mongoFileService.addFile(file, manufacturerId, manufacturerName);
    }

    @GetMapping(path = "/downloadFile")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<ByteArrayResource> downloadFile(@Param("fileId") String fileId) throws IOException {
        LoadFile loadFile = mongoFileService.downloadFile(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(loadFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + loadFile.getFilename() + "\"")
                .body(new ByteArrayResource(loadFile.getFile()));
    }

}
