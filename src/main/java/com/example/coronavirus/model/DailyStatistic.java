package com.example.coronavirus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

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
@Table( name = "daily_statistic",
        uniqueConstraints = { @UniqueConstraint( columnNames = { "date", "country_id" } ) } )
public class DailyStatistic {
    @Id
    @GeneratedValue
    private Long id;
    @Column(columnDefinition = "DATE")
    private LocalDate date;
    @ManyToOne
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @JoinColumn(name = "country_id")
    private Country country;
    private int cases;
    private int deaths;
    private int recovered;
    private int tested;
}
