package com.example.DroolsTC.configuration;

import com.example.DroolsTC.model.Rule;
import com.example.DroolsTC.repository.DroolRulesRepo;
import org.drools.template.ObjectDataCompiler;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class DroolsConfig {
    public static final String RULES_CUSTOMER_RULES_DRL = "rules/customer-discount.drl";
    public static final String RULES_TEMPLATE_FILE = "rules/discount-template.drl";

    private final DroolRulesRepo droolRulesRepo;

    public DroolsConfig(DroolRulesRepo droolRulesRepo) {
        this.droolRulesRepo = droolRulesRepo;
    }

    @Bean
    public KieServices kieServices(){
        return KieServices.Factory.get();
    }

    @Bean
    public KieContainer kieContainer(KieServices kieServices) {
        List<Rule> ruleAttributes = new ArrayList<>(droolRulesRepo.findAll());
        ObjectDataCompiler compiler = new ObjectDataCompiler();
        String generatedDRL = compiler.compile(ruleAttributes, Thread.currentThread().getContextClassLoader().getResourceAsStream(DroolsConfig.RULES_TEMPLATE_FILE));
        byte[] b1 = generatedDRL.getBytes();
        Resource resource1 = kieServices.getResources().newByteArrayResource(b1).setResourceType(ResourceType.DRL);

        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

        kieFileSystem.write(resource1);
        KieBuilder kb = kieServices.newKieBuilder(kieFileSystem);
        kb.buildAll();
        KieModule kieModule = kb.getKieModule();
        return kieServices.newKieContainer(kieModule.getReleaseId());
    }

}
