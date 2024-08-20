package com.example.DroolsTC.repository;

import com.example.DroolsTC.model.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DroolRulesRepo extends JpaRepository<Rule, Integer> {
}