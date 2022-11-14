package com.company.purchasing.domain.entities;

import lombok.Data;

import java.util.List;

@Data
public class Rule {

    private int score;
    private List<Condition> conditions;

}
