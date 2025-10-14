package com.application.service;

import com.application.dto.OrderRequest;
import com.application.dto.TransactionDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface TransactionService {

    List<TransactionDto> buyOrder(OrderRequest orderDetails, HttpServletRequest request);

}
