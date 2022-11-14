package com.company.purchasing.domain.services;

import com.company.purchasing.domain.entities.Product;
import com.company.purchasing.domain.entities.PurchaseOrder;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.List;

public interface IPurchaseService {

    PurchaseOrder getPurchaseOrder(List<Product> products) throws ScriptException, IOException;
}
