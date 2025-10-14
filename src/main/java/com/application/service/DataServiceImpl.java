package com.application.service;

import com.application.config.Constants;
import com.application.dto.*;
import com.application.exception.BusinessGlobalException;
import com.application.exception.ServerException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class DataServiceImpl implements DataService {

    @Autowired
    ManufacturerService manufacturerService;

    @Autowired
    ProductService productService;

    @Autowired
    ProductCategoryService productCategoryService;

    Comparator<StateDto> compareByStateName = Comparator.comparing(StateDto::getStateName);
    private List<StateDto> masterStateInfo = new ArrayList<>();
    private List<StateDto> indivStateDetails = new ArrayList<>();

    @Override
    public List<StateDto> getStates(String countryName) {
        log.info("Entered the getStates Method - " + countryName);
        if (masterStateInfo.isEmpty()) {
            masterStateInfo = parseCsvFile();
            return sortStateList();
        } else {
            if (indivStateDetails.isEmpty()) {
                return sortStateList();
            } else {
                return indivStateDetails;
            }
        }
    }

    private List<StateDto> parseCsvFile() {
        Resource resource = new ClassPathResource("cities.csv");
        try {
            CSVParser csvParser = new CSVParser(new FileReader(resource.getFile()), CSVFormat.DEFAULT
                    .withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                if (csvRecord.get("country_code").equalsIgnoreCase("IN") &&
                        csvRecord.get("country_name").equalsIgnoreCase("India")) {
                    StateDto stateDto = new StateDto(Long.parseLong(csvRecord.get("Id")), csvRecord.get("name"),
                            Long.parseLong(csvRecord.get("state_id")), csvRecord.get("state_code"), csvRecord.get("state_name"),
                            Long.parseLong(csvRecord.get("country_id")), csvRecord.get("country_code"), csvRecord.get("country_name"),
                            csvRecord.get("latitude"), csvRecord.get("longitude"));
                    masterStateInfo.add(stateDto);
                }
            }
            csvParser.close();
        } catch (Exception e) {
            log.info("Error has occurred while parsing the CSV file");
        }
        return masterStateInfo;
    }

    private List<StateDto> sortStateList() {
        Map<String, StateDto> stateSet = new HashMap<>();
        for (StateDto stateDto : masterStateInfo) {
            if (!stateDto.getStateName().isEmpty()) {
                stateSet.put(stateDto.getStateName(), stateDto);
            }
        }
        indivStateDetails = new ArrayList<>(stateSet.values());
        indivStateDetails.sort(compareByStateName);
        return indivStateDetails;
    }

    @Override
    public List<CityDto> getCities(String stateName) {
        log.info("Entered the getCities() method -- " + stateName);
        List<CityDto> cities = new ArrayList<>();
        if (masterStateInfo.isEmpty()) {
            indivStateDetails = getStates("India");
        }

        List<StateDto> stateList = masterStateInfo.stream()
                .filter(stateDto -> !stateDto.getStateName().isEmpty()
                        && stateDto.getStateName().equalsIgnoreCase(stateName))
                .collect(Collectors.toList());

        for (StateDto state : stateList) {
            cities.add(new CityDto(state.getCityId(), state.getCityName()));
        }

        log.info("Leaving the getCities() method");
        return cities;
    }

    @Override
    public ResponseEntity<Resource> generateXcelSheet(String dataType, HttpServletRequest request)
            throws BusinessGlobalException {
        log.info("Entered Generate Xcel Sheet Method()");
        if (dataType == null || dataType.isEmpty()) {
            throw new ServerException("Input DataType is Invalid", Constants.INVALID_INPUT,
                    request.getRequestURL().toString(), Constants.XCEL_SERVICE);
        }

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet(dataType);
        List<ManufacturerDto> manufacturerData;
        List<ProductDto> productData;

        if (dataType.equalsIgnoreCase("Manufacturer")) {
            manufacturerData = manufacturerService.getAll();
            setXcelHeaderRow(ManufacturerDto.class, spreadsheet);
            setXcelData(ManufacturerDto.class, manufacturerData.size(), spreadsheet, Collections.singletonList(manufacturerData));
        } else if (dataType.equalsIgnoreCase("Product")) {
            productData = productService.getAll();
            setXcelHeaderRow(ProductDto.class, spreadsheet);
            setXcelData(ProductDto.class, productData.size(), spreadsheet, Collections.singletonList(productData));
        } else {
            throw new ServerException("Input DataType is Invalid", Constants.INVALID_INPUT,
                    request.getRequestURL().toString(), Constants.XCEL_SERVICE);
        }

        try {
            String fileName = dataType + "_data.xlsx";
            File file = new File(fileName);

            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
            header.add("Cache-Control", "no-cache, no-store, must-revalidate");
            header.add("Pragma", "no-cache");
            header.add("Expires", "0");

            FileOutputStream out = new FileOutputStream(file);
            workbook.write(out);
            out.close();

            Path path = Paths.get(file.getAbsolutePath());
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
            log.info(fileName + " written successfully on disk.");

            ResponseEntity<Resource> responseEntity = ResponseEntity.ok()
                    .headers(header)
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);

            if (file.exists()) {
                Files.delete(file.toPath());
            }

            return responseEntity;
        } catch (Exception e) {
            throw new ServerException("File download service failed", Constants.SERVICE_ERROR,
                    request.getRequestURL().toString(), Constants.XCEL_SERVICE);
        }

    }

    private XSSFSheet setXcelHeaderRow(Class<? extends Serializable> c, XSSFSheet spreadsheet) {
        XSSFRow row = spreadsheet.createRow(0);
        int cellCount = 0;
        Field[] fields = c.getDeclaredFields();
        for (int fieldCount = 0; fieldCount < c.getDeclaredFields().length - 1; fieldCount++) {
            Cell cell = row.createCell(cellCount);
            cell.setCellValue(fields[fieldCount + 1].getName());
            cellCount++;
        }
        return spreadsheet;
    }

    private XSSFSheet setXcelData(Class<? extends Serializable> c, int dataSize, XSSFSheet spreadsheet, List<Object> data1) {
        ArrayList data = (ArrayList) data1.get(0);
        if (data == null || data.isEmpty()) {
            throw new ServerException("Invalid Data", Constants.SERVICE_ERROR,
                    Constants.GEN_XCEL_URL, "Setting XCEL Data");
        }

        for (int rowNo = 0; rowNo < dataSize; rowNo++) {
            XSSFRow row = spreadsheet.createRow(rowNo + 1);
            for (int cellNo = 0; cellNo < c.getDeclaredFields().length; cellNo++) {
                Cell cell = row.createCell(cellNo, CellType.STRING);

                Field[] fields = c.getDeclaredFields();
                try {
                    if (data.get(rowNo) == null) {
                        throw new ServerException("Invalid Row Data", Constants.SERVICE_ERROR,
                                Constants.GEN_XCEL_URL, "Setting Xcel Data");
                    }
                    if (data.get(rowNo) instanceof ManufacturerDto && cellNo < fields.length - 1) {
                        ManufacturerDto manufacturerDto = (ManufacturerDto) data.get(rowNo);
                        Field field = ManufacturerDto.class.getDeclaredField(fields[cellNo + 1].getName());
                        field.setAccessible(true);
                        if (field.get(manufacturerDto) != null) {
                            cell.setCellValue(field.get(manufacturerDto).toString());
                        } else {
                            cell.setCellValue("NA");
                        }
                    } else if (data.get(rowNo) instanceof ProductDto && cellNo < fields.length - 1) {
                        ProductDto productDto = (ProductDto) data.get(rowNo);
                        Field field = ProductDto.class.getDeclaredField(fields[cellNo + 1].getName());
                        field.setAccessible(true);
                        if (field.get(productDto) != null) {
                            cell.setCellValue(field.get(productDto).toString());
                        } else {
                            cell.setCellValue("NA");
                        }
                    }
                } catch (Exception e) {
                    throw new ServerException("Generating Xcel Service failed", Constants.SERVICE_ERROR,
                            Constants.GEN_XCEL_URL, "setXcelData Method");
                }

            }

        }
        return spreadsheet;
    }

    @Override
    public OverviewResponse getOverview() {
        OverviewResponse overviewResponse = new OverviewResponse();
        overviewResponse.setTotalManufacturers(manufacturerService.getManufacturerCount());
        overviewResponse.setTotalProducts(productService.getProductCount());
        overviewResponse.setTotalProductCategories(productCategoryService.getProductCategoryCount());
        ProductDto productDto = productService.getHighestProductValue();

        overviewResponse.setHighestProductName(productDto.getProductName());
        overviewResponse.setHighestProductValue(productDto.getTotalProductValue());

        productDto = productService.getLowestProductValue();
        overviewResponse.setLowestProductName(productDto.getProductName());
        overviewResponse.setLowestProductValue(productDto.getTotalProductValue());

        overviewResponse.setNoOfManufacturersRecAdded((int) manufacturerService.getRecentlyAddedCount());
        overviewResponse.setNoOfProductsRecAdded((int) productService.getRecentlyAddedCount());

        return overviewResponse;
    }

}
