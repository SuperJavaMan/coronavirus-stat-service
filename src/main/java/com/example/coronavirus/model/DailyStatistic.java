package com.example.coronavirus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author Oleg Pavlyukov
 * on 29.03.2020
 * cpabox777@gmail.com
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyStatistic {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDate date;
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;
    private int cases;
    private int deaths;
    private int recovered;
}
