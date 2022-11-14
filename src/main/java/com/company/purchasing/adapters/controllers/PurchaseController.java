package com.company.purchasing.adapters.controllers;

import com.company.purchasing.domain.entities.Product;
import com.company.purchasing.domain.entities.PurchaseOrder;
import com.company.purchasing.domain.services.IPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("company/api/v1")
public class PurchaseController {


    @Autowired
    private IPurchaseService purchaseService;

    @PostMapping("/purchase/score-products")
    public PurchaseOrder scoreProducts(@RequestBody List<Product> products) {
        try {
            return purchaseService.getPurchaseOrder(products);
        } catch (ScriptException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

}



