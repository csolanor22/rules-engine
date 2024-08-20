package com.example.DroolsTC.service;

import com.example.DroolsTC.configuration.DroolsConfig;
import com.example.DroolsTC.model.Order;
import com.example.DroolsTC.model.Rule;
import com.example.DroolsTC.repository.DroolRulesRepo;
import lombok.RequiredArgsConstructor;
import org.drools.template.ObjectDataCompiler;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
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
public class KieService {

    private final DroolRulesRepo ruleRepository;
    private final KieContainer kieContainer;
    private final KieServices kieServices;

    public void setRules(Rule rule) {
        ruleRepository.save(rule);
        List<Rule> ruleAttributes = new ArrayList<>(ruleRepository.findAll());

        ObjectDataCompiler compiler = new ObjectDataCompiler();
        String generatedDRL = compiler.compile(ruleAttributes, Thread.currentThread().getContextClassLoader().getResourceAsStream(DroolsConfig.RULES_TEMPLATE_FILE));

        KieHelper kieHelper = new KieHelper();

        byte[] b1 = generatedDRL.getBytes();
        Resource resource1 = kieServices.getResources().newByteArrayResource(b1);
        kieHelper.addResource(resource1, ResourceType.DRL);

        kieHelper.build();

        kieContainer.updateToVersion(kieServices.getRepository().getDefaultReleaseId());

    }
    public void loadRules() {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        List<Rule> rules = ruleRepository.findAll();

        for (Rule rule : rules) {
            kieFileSystem.write(kieServices.getResources()
                    .newByteArrayResource(rule.getRuleContent().getBytes())
                    .setResourceType(ResourceType.DRL)
                    .setSourcePath(rule.getRuleName()));
        }

        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll();
        if (kieBuilder.getResults().hasMessages(Message.Level.ERROR)) {
            throw new RuntimeException("Error building rules: " + kieBuilder.getResults().toString());
        }

        kieContainer.updateToVersion(kieServices.getRepository().getDefaultReleaseId());
    }

}
