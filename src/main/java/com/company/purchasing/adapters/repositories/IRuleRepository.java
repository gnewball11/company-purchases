package com.company.purchasing.adapters.repositories;

import com.company.purchasing.domain.entities.Rule;

import java.io.IOException;
import java.util.List;

public interface IRuleRepository {

    List<Rule> getRules() throws IOException;
}
