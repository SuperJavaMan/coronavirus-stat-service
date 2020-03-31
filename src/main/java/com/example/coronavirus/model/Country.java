package com.example.coronavirus.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Oleg Pavlyukov
 * on 29.03.2020
 * cpabox777@gmail.com
 */
@Entity
@Data
public class Country {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
}
