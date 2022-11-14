package com.company.purchasing.domain.services;


import com.company.purchasing.domain.entities.*;
import com.company.purchasing.adapters.repositories.RuleRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PurchaseService implements IPurchaseService {


    private static final double PRODUCT_CUTOFF = 50.0;


    public PurchaseService() {
        this.factory = new ScriptEngineManager();
    }

    @Autowired
    private RuleRepository ruleRepository;

    private ScriptEngineManager factory;


    /**
     * This method will return all the products  that scored highly enough and will include the total and average price
     * @param products
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    public PurchaseOrder getPurchaseOrder(List<Product> products) throws ScriptException, IOException {

        List<Rule> rules = this.ruleRepository.getRules();
        List<Product> selectedProducts = scoreProducts(products, rules);

        double averagePrice = 0;
        double totalPrice = 0;
        if (selectedProducts.size() > 0) {
            averagePrice = selectedProducts.stream().mapToInt(p -> Integer.parseInt(p.getAttributes().stream().filter(attribute -> attribute.getName().equals("price")).findFirst().get().getValue())).average().getAsDouble();
            totalPrice = selectedProducts.stream().mapToInt(p -> Integer.parseInt(p.getAttributes().stream().filter(attribute -> attribute.getName().equals("price")).findFirst().get().getValue())).sum();
        }

        return PurchaseOrder.builder().date(LocalDateTime.now()).totalPrice(totalPrice).averagePrice(averagePrice).products(selectedProducts).build();
    }

    /**
     * This method will score each product against all the provided rules and check if they pass the defined threshold.
     * The maximum score that can be granted to a product is represented by the absolute value of the sum of all the score rules.
     *
     * @param products
     * @param rules
     * @return
     * @throws ScriptException
     */
    private List<Product> scoreProducts(List<Product> products, List<Rule> rules) throws ScriptException {


        int maxRulesScore = rules.stream().mapToInt(rule -> Math.abs(rule.getScore())).sum();
        List<Product> selectedProducts = new ArrayList<Product>();
        for (Product product : products) {

            Map<String, String> productAttributes = product.getAttributes().stream().collect(Collectors.toMap(Attribute::getName, Attribute::getValue));
            double rulesScore = 0.0;
            for (Rule rule : rules) {
                rulesScore = rulesScore + evaluateRule(rule, productAttributes);
            }

            double productScore = (rulesScore * 100) / maxRulesScore;
            if (productScore >= PRODUCT_CUTOFF) {
                selectedProducts.add(product);
            }
        }
        return selectedProducts;

    }


    /**
     * This method will evaluate each condition of a given rule against the product attribute.
     * In case that the condition matches with the product attribute a proportional part of the score of the rule will be given to the product as a reward.
     * If the condition is not a match and the score of the rule is negative a proportional part of the score of the rule will be given to the product as a reward.
     * @param rule
     * @param productAttributes
     * @return
     * @throws ScriptException
     */
    private double evaluateRule(Rule rule, Map<String, String> productAttributes) throws ScriptException {
        double ruleScore = 0.0;
        String stringFormat = "\"%s\" %s \"%s\"";
        String numericFormat = "%s %s %s";
        ScriptEngine engine = this.factory.getEngineByName("nashorn");
        int numberOfConditions = rule.getConditions().size();
        double conditionWeight = (100.0 / numberOfConditions) / 100.0;
        for (Condition condition : rule.getConditions()) {
            String productAttributeValue = productAttributes.get(condition.getAttribute().getName());
            String conditionToEvaluate = String.format(StringUtils.isNumeric(productAttributeValue) ? numericFormat : stringFormat,
                    productAttributeValue,
                    condition.getComparisonOperator(),
                    condition.getAttribute().getValue()
            );
            Boolean isConditionMatched = (Boolean) engine.eval(conditionToEvaluate);
            double conditionScore = 0.0;
            if (isConditionMatched) {
                conditionScore = conditionWeight * rule.getScore();
            } else if (rule.getScore() < 0) {
                conditionScore = Math.abs(conditionWeight * rule.getScore());
            }
            ruleScore = ruleScore + conditionScore;

        }
        return ruleScore;
    }

}
