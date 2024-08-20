package com.example.DroolsTC.controller;

import com.example.DroolsTC.model.Rule;
import com.example.DroolsTC.repository.DroolRulesRepo;
import com.example.DroolsTC.service.KieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RulesController {

    private final DroolRulesRepo rulesRepo;
    private final KieService kieService;

    @PostMapping("/rule")
    public void addRule (@RequestBody Rule rule) {
        kieService.setRules(rule);
    }

    @GetMapping("/rules")
    public List<Rule> getRules () {
        List<Rule> rules = new ArrayList<Rule>();
        rulesRepo.findAll().forEach(rules::add);
        return rules;
    }

    @DeleteMapping("/rule/{id}")
    public ResponseEntity<?> deleteRule(@PathVariable("id") Integer id){
        rulesRepo.deleteById(id);
        return ResponseEntity.ok("Rule deleted");
    }
}