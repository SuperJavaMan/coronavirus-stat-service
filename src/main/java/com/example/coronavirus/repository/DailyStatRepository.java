package com.example.coronavirus.repository;

import com.example.coronavirus.model.Country;
import com.example.coronavirus.model.DailyStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query(value = "select -1 as id, " +
            "       current_date() as date,\n" +
            "       null as country_id,\n" +
            "       sum(cases) as cases,\n" +
            "       sum(deaths) as deaths,\n" +
            "       sum(recovered) as recovered\n" +
            "from daily_statistic\n" +
            "where (SELECT MAX(date) from daily_statistic) = date",
    nativeQuery = true)
    DailyStatistic getGlobalStatistic();

    long count();

}
