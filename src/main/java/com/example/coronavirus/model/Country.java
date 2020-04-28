package com.example.coronavirus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Map;

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
    @Column(unique = true)
    private String name;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "inter_names")
    @MapKeyColumn(name = "lang")
    @Column(name = "value")
    private Map<String, String> interNames;
    private double longitude;
    private double latitude;

    public Country(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
