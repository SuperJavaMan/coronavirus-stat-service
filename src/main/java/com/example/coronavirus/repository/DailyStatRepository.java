package com.example.coronavirus.repository;

import com.example.coronavirus.model.Country;
import com.example.coronavirus.model.DailyStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Oleg Pavlyukov
 * on 29.03.2020
 * cpabox777@gmail.com
 */
@Repository
public interface DailyStatRepository extends JpaRepository<DailyStatistic, Long> {

    List<DailyStatistic> findAllByDate(LocalDate date);
    List<DailyStatistic> findAllByCountry(Country country);
    DailyStatistic findByDateAndCountry(LocalDate date, Country country);
    List<DailyStatistic> findAllByDateIsBetween(LocalDate from, LocalDate to);
    List<DailyStatistic> findAllByCountryAndDateBetween(Country country, LocalDate from, LocalDate to);

}
