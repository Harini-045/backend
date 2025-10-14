package com.application.service;

import com.application.config.Constants;
import com.application.dto.ManufacturerDto;
import com.application.dto.OrderRequest;
import com.application.dto.ProductDto;
import com.application.dto.TransactionDto;
import com.application.entity.TransactionEntity;
import com.application.exception.ServerException;
import com.application.mapper.ObjectUtils;
import com.application.repository.TransactionRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Log4j2
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepo transactionRepo;

    private final ManufacturerService manufacturerService;

    private final ProductService productService;

    Map<String, Long> manufacturerNames = new HashMap<>();
    Map<String, Long> productNames = new HashMap<>();

    public TransactionServiceImpl(TransactionRepo transactionRepo,
                                  final ManufacturerService manufacturerService,
                                  final ProductService productService) {
        this.transactionRepo = transactionRepo;
        this.manufacturerService = manufacturerService;
        this.productService = productService;
    }

    public void buyOrderRequestValidation(OrderRequest orderDetails, HttpServletRequest request) {
        if (orderDetails == null) {
            throw new ServerException("Invalid Request Payload", Constants.INVALID_INPUT,
                    request.getRequestURL().toString(), Constants.PLACE_ORDER_METHOD);
        }

        if (orderDetails.getOrderDetails() == null) {
            throw new ServerException("Invalid ManufacturerId/ProductId", Constants.INVALID_INPUT,
                    request.getRequestURL().toString(), Constants.PLACE_ORDER_METHOD);
        }
    }

    public void buyOrderRequestDataCheck(TransactionDto stockPurchaseDto, HttpServletRequest request) {
        if (ObjectUtils.isEmptyOrNull(stockPurchaseDto.getManufacturerName()) || ObjectUtils.isEmptyOrNull(stockPurchaseDto.getProductName())) {
            throw new ServerException("Invalid ManufacturerName/ProductName", Constants.INVALID_INPUT,
                    request.getRequestURL().toString(), Constants.PLACE_ORDER_METHOD);
        }

        if (manufacturerNames.isEmpty()) {
            getManufacturerNames();
        }

        if (productNames.isEmpty()) {
            getProductNames();
        }
    }

    @Override
    public List<TransactionDto> buyOrder(OrderRequest orderDetails, HttpServletRequest request) {
        List<TransactionDto> orderResponse = new ArrayList<>();
        buyOrderRequestValidation(orderDetails, request);

        for (TransactionDto transactionDto : orderDetails.getOrderDetails()) {
            buyOrderRequestDataCheck(transactionDto, request);

            long manufacturerId = fetchManufacturerId(transactionDto.getManufacturerName());
            long productId = fetchProductId(transactionDto.getProductName());

            transactionDto.setTransactionType(Constants.BUY_ORDER);
            transactionDto.setManufacturerId(manufacturerId);
            transactionDto.setProductId(productId);
            transactionDto.setProductCategoryName(transactionDto.getProductCategoryName());
            transactionDto.setTotalWeight(transactionDto.getTotalWeight());
            transactionDto.setProductValue(transactionDto.getProductValue());
            transactionDto.setAmountPaid(transactionDto.getProductValue());
            transactionDto.setPaymentMethod(Constants.ONLINE_PAYMENT);
            transactionDto.setPayeeName(Constants.PAYEE_NAME);

            saveProductData(transactionDto, request);
            TransactionEntity transactionEntity = (TransactionEntity) ObjectUtils.map(transactionDto, new TransactionEntity());
            try {
                transactionEntity = transactionRepo.save(transactionEntity);
            } catch (Exception e) {
                log.info(e.getMessage());
            }
            orderResponse.add((TransactionDto) ObjectUtils.map(transactionEntity, new TransactionDto()));
        }

        return orderResponse;
    }


    public void getManufacturerNames() {
        List<ManufacturerDto> data1 = manufacturerService.getManufacturerNames();
        if (ObjectUtils.listIsEmpty(data1).equals(Boolean.TRUE)) {
            throw new ServerException("Unable to fetch Manufacturer Data from DB",
                    Constants.INVALID_INPUT, "", Constants.PLACE_ORDER_METHOD);
        } else {
            for (ManufacturerDto manufacturerDto : data1) {
                manufacturerNames.put(manufacturerDto.getManufacturerCompanyName(), manufacturerDto.getManufacturerId());
            }
        }

    }

    public void getProductNames() {
        List<ProductDto> data1 = productService.getProductNames();
        if (ObjectUtils.listIsEmpty(data1).equals(Boolean.TRUE)) {
            throw new ServerException("Unable to fetch Product Data from DB",
                    Constants.INVALID_INPUT, "", Constants.PLACE_ORDER_METHOD);
        } else {
            for (ProductDto productDto : data1) {
                productNames.put(productDto.getProductName(), productDto.getProductId());
            }
        }
    }

    public long fetchManufacturerId(String manufacturerName) {
        if (ObjectUtils.isEmptyOrNull(manufacturerName).equals(Boolean.TRUE)) {
            throw new ServerException("Invalid Manufacturer name in request Data",
                    Constants.INVALID_INPUT, "", Constants.PLACE_ORDER_METHOD);
        }
        return manufacturerNames.get(manufacturerName);
    }

    public long fetchProductId(String productName) {
        if (ObjectUtils.isEmptyOrNull(productName).equals(Boolean.TRUE)) {
            throw new ServerException("Invalid Product name in request Data",
                    Constants.INVALID_INPUT, "", Constants.PLACE_ORDER_METHOD);
        }
        return productNames.get(productName);
    }

    public ProductDto saveProductData(TransactionDto transactionDto, HttpServletRequest request) {
        ProductDto productDto = new ProductDto();
        ObjectUtils.map(transactionDto, productDto);
        productDto.setLandedCost(0.0);
        productDto.setProductDesc(transactionDto.getProductCategoryName());
        productService.createOrUpdateData(productDto, request);
        return productDto;
    }

}
