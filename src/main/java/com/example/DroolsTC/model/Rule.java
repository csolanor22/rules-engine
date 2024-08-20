package com.example.DroolsTC.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rule_table")
@Getter
@Setter
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "ifcondition")
    private String ifcondition;
    @Column(name = "thencondition")
    private String thencondition;
    @Column(name = "version")
    private int version;

}