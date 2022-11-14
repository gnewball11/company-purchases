package com.company.purchasing.domain.entities;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PurchaseOrder {
    private LocalDateTime date;
    private double totalPrice;
    private double averagePrice;
    private List<Product> products;
}
