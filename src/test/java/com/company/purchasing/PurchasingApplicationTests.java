package com.company.purchasing;

import com.company.purchasing.domain.entities.Attribute;
import com.company.purchasing.domain.entities.Product;
import com.company.purchasing.domain.entities.PurchaseOrder;
import com.company.purchasing.domain.services.PurchaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.script.ScriptException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PurchasingApplicationTests {


    @Autowired
    PurchaseService purchaseService;


    @Test
    void scoreProductsAndAllSelected() throws ScriptException, IOException {

        Product product1 = new Product();
        List<Attribute> product1Attributes = Arrays.asList(
                new Attribute("name", "Jeans"),
                new Attribute("type", "Clothing"),
                new Attribute("color", "BLUE"),
                new Attribute("price", "12"),
                new Attribute("quantity", "20")
        );
        product1.setAttributes(product1Attributes);

        Product product2 = new Product();
        List<Attribute> product2Attributes = Arrays.asList(
                new Attribute("name", "trousers"),
                new Attribute("type", "Clothing"),
                new Attribute("color", "BLUE"),
                new Attribute("price", "34"),
                new Attribute("quantity", "1000")
        );
        product2.setAttributes(product2Attributes);

        List<Product> products = Arrays.asList(product1, product2);
        PurchaseOrder purchaseOrder = purchaseService.getPurchaseOrder(products);

        assertThat(purchaseOrder.getAveragePrice()).isEqualTo(23.0);
        assertThat(purchaseOrder.getTotalPrice()).isEqualTo(46.0);
    }


    @Test
    void scoreProductsAndOnlyOnePass() throws ScriptException, IOException {

        Product product1 = new Product();
        List<Attribute> product1Attributes = Arrays.asList(
                new Attribute("name", "Jeans"),
                new Attribute("type", "Clothing"),
                new Attribute("color", "BLUE"),
                new Attribute("price", "45"),
                new Attribute("quantity", "11")
        );
        product1.setAttributes(product1Attributes);

        Product product2 = new Product();
        List<Attribute> product2Attributes = Arrays.asList(
                new Attribute("name", "trousers"),
                new Attribute("type", "Clothing"),
                new Attribute("color", "BLUE"),
                new Attribute("price", "34"),
                new Attribute("quantity", "1000")
        );
        product2.setAttributes(product2Attributes);

        List<Product> products = Arrays.asList(product1, product2);
        PurchaseOrder purchaseOrder = purchaseService.getPurchaseOrder(products);

        assertThat(purchaseOrder.getAveragePrice()).isEqualTo(34);
        assertThat(purchaseOrder.getTotalPrice()).isEqualTo(34);
    }


}
