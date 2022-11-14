package com.company.purchasing.domain.entities;

import lombok.Data;

@Data
public class Condition {
    private Attribute attribute;
    private String comparisonOperator;
}
