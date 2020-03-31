package com.example.coronavirus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@AllArgsConstructor
@NoArgsConstructor
public class Country {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
}
