package com.company.purchasing.adapters.repositories;


import com.company.purchasing.domain.entities.Rule;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
public class RuleRepository {
    private ObjectMapper objectMapper;

    @Autowired
    ResourceLoader resourceLoader;

    public RuleRepository(){
        this.objectMapper = new ObjectMapper();
    }

    public List<Rule> getRules() throws IOException {

        List<Rule> rules = this.objectMapper.readValue(resourceLoader.getResource("classpath:data/rules.json").getFile(),  new TypeReference<List<Rule>>(){});
        return rules;

    }

}
