package com.application.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    List<TransactionDto> orderDetails;
}
