package com.application.controller;

import com.application.dto.ManufacturerDto;
import com.application.service.ManufacturerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/manufacturer")
@RequiredArgsConstructor
@Log4j2
public class ManufacturerController {

    private final ManufacturerService manufacturerService;

    @GetMapping(path = "/get")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<ManufacturerDto> getAll(HttpServletRequest request) {
        return manufacturerService.getAll();
    }

    @GetMapping(path = "/get/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ManufacturerDto getById(@PathVariable long id) {
        return manufacturerService.getByManufacturerId(id);
    }

    @GetMapping(path = "/getByName")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<ManufacturerDto> getByName(@RequestParam("manufacturerName") String name){
        return manufacturerService.getByManufacturerName(name);
    }

    @PostMapping(path = "/create-update", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<ManufacturerDto> createOrUpdateData(@RequestPart("manufacturerDto") String manufacturerDto,
                                                    @RequestPart("file") MultipartFile file) {
        return manufacturerService.createOrUpdateData(manufacturerDto, file);
    }

}
