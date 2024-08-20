package com.example.DroolsTC.service;

import com.example.DroolsTC.configuration.DroolsConfig;
import com.example.DroolsTC.model.Order;
import com.example.DroolsTC.model.Rule;
import com.example.DroolsTC.repository.DroolRulesRepo;
import lombok.RequiredArgsConstructor;
import org.drools.template.ObjectDataCompiler;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final KieContainer kieContainer;
    private final DroolRulesRepo rulesRepo;

    public Order getDiscountForOrder(Order order) {
        KieSession session = kieContainer.newKieSession();
        session.insert(order);
        session.fireAllRules();
        session.dispose();
        return order;
    }

    public Order getDiscountForOrderV2(Order order) throws FileNotFoundException {
        List<Rule> ruleAttributes = new ArrayList<>(rulesRepo.findAll());

        ObjectDataCompiler compiler = new ObjectDataCompiler();
        String generatedDRL = compiler.compile(ruleAttributes, Thread.currentThread().getContextClassLoader().getResourceAsStream(DroolsConfig.RULES_TEMPLATE_FILE));
        KieServices kieServices = KieServices.Factory.get();

        KieHelper kieHelper = new KieHelper();

        //multiple such resoures/rules can be added
        byte[] b1 = generatedDRL.getBytes();
        Resource resource1 = kieServices.getResources().newByteArrayResource(b1);
        kieHelper.addResource(resource1, ResourceType.DRL);

        KieBase kieBase = kieHelper.build();

        KieSession kieSession = kieBase.newKieSession();
        kieSession.insert(order);
        kieSession.fireAllRules();
        kieSession.dispose();

        return order;
    }

    public Order getDiscountForOrderV3(Order order){
        KieSession kieSession = kieContainer.newKieSession();
        kieSession.insert(order);
        kieSession.fireAllRules();
        kieSession.dispose();
        return order;
    }
}