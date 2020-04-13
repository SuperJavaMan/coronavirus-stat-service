package com.example.coronavirus.service;

import com.example.coronavirus.foreignDataSource.ForeignDataSource;
import com.example.coronavirus.foreignDataSource.exception.ResourceNotAvailableException;
import com.example.coronavirus.exception.DataInitException;
import com.example.coronavirus.model.Country;
import com.example.coronavirus.model.DailyStatistic;
import com.example.coronavirus.repository.CountryRepository;
import com.example.coronavirus.repository.DailyStatRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Oleg Pavlyukov
 * on 31.03.2020
 * cpabox777@gmail.com
 */
@Component
@Data
@Slf4j
@EnableScheduling
public class ScheduledDataUpdater {

    private DailyStatRepository dsRepository;
    private ForeignDataSource foreignDataSource;
    private CountryRepository countryRepository;

    @Autowired
    public ScheduledDataUpdater(DailyStatRepository dsRepository,
                                ForeignDataSource foreignDataSource,
                                CountryRepository countryRepository) {
        this.dsRepository = dsRepository;
        this.foreignDataSource = foreignDataSource;
        this.countryRepository = countryRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initDataBase() {
        log.debug("Start initDataBase()");
        long recordsCount= dsRepository.count();
        if (recordsCount < 1) {
            try {
                List<DailyStatistic> freshDataList = foreignDataSource.getStatsByAllCountries();
                Map<String, List<DailyStatistic>> mapToSave = freshDataList.stream()
                        .collect(Collectors.groupingBy(
                            key-> key.getCountry().getName()));

                for (List<DailyStatistic> dailyStatisticList : mapToSave.values()) {
                    dsRepository.saveAll(dailyStatisticList);
                    Thread.sleep(10000);
                }
            } catch (ResourceNotAvailableException | InterruptedException e) {
                log.error("Fatal error during init db. No data available.", e);
                throw new DataInitException("Fatal error during init db. No data available.", e);
            }
        }
    }

    @Scheduled(fixedRateString = "${app.schedule.rate}")
    private void refreshCurrentDayData1() {
        log.info("Refreshing data at " + LocalDateTime.now());

        if (dsRepository.count() < 1) return;

        LocalDate currentDate = LocalDate.now().minusDays(1);
        try {
            List<DailyStatistic> foreignDataList = foreignDataSource.getCurrentDayWorldStat();
            List<DailyStatistic> repoDataList = dsRepository.findAllByDate(currentDate);
            if (repoDataList == null || repoDataList.isEmpty()) {
                for (DailyStatistic ds : foreignDataList) {
                    Country country = countryRepository.findCountryByName(ds.getCountry().getName());
                    if (country != null) {
                        ds.setCountry(country);
                        dsRepository.save(ds);
                    } else {
                        log.error("Country not found -> " + ds.getCountry().getName());
                    }
                }
            } else {
                for (DailyStatistic foreignDs : foreignDataList) {
                    String countryName = foreignDs.getCountry().getName();
                    Country country = countryRepository.findCountryByName(countryName);
                    if (country == null) {
                        country = countryRepository.findCountryByNameLike("%" + countryName + "%");
                    }
                    if (country != null) {
                        DailyStatistic repoDs = dsRepository.findByDateAndCountry(currentDate, country);
                        if (repoDs == null) {
                            foreignDs.setCountry(country);
                            dsRepository.save(foreignDs);
                        } else {
                            repoDs.setCases(foreignDs.getCases());
                            repoDs.setDeaths(foreignDs.getDeaths());
                            repoDs.setRecovered(foreignDs.getRecovered());
                            dsRepository.save(repoDs);
                        }
                    } else {
                        log.warn("Country not found -> " + countryName);
                    }
                }
            }
        } catch (ResourceNotAvailableException e) {
            String msg = "Refresh data error";
            log.error(msg, e);
            throw new DataInitException(msg, e);
        }
    }
}
