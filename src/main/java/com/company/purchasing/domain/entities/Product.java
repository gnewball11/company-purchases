package com.company.purchasing.domain.entities;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class Product {
    private List<Attribute> attributes;
}
