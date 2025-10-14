package com.application.controller;

import com.application.dto.OrderRequest;
import com.application.dto.TransactionDto;
import com.application.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/api/stock")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping(path = "/buy")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<TransactionDto> buyOrder(@RequestBody OrderRequest orderDetails, HttpServletRequest request) {
        return transactionService.buyOrder(orderDetails, request);
    }

}
