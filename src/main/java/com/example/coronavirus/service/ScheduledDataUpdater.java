package com.example.coronavirus.service;

import com.example.coronavirus.dataParser.ForeignDataSource;
import com.example.coronavirus.dataParser.exception.ResourceNotAvailableException;
import com.example.coronavirus.exception.DataInitException;
import com.example.coronavirus.exception.NoDataException;
import com.example.coronavirus.model.DailyStatistic;
import com.example.coronavirus.repository.DailyStatRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Oleg Pavlyukov
 * on 31.03.2020
 * cpabox777@gmail.com
 */
@Component
@Data
public class ScheduledDataUpdater {

    private DailyStatRepository repository;
    private ForeignDataSource foreignDataSource;

    @Autowired
    public ScheduledDataUpdater(DailyStatRepository repository, ForeignDataSource foreignDataSource) {
        this.repository = repository;
        this.foreignDataSource = foreignDataSource;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initDataBase() {
        List<DailyStatistic> dailyStatisticList = repository.findAll();
        if (dailyStatisticList == null || dailyStatisticList.size() < 1) {
            try {
                List<DailyStatistic> freshDataList = foreignDataSource.getStatsByAllCountries();
                repository.saveAll(freshDataList);
            } catch (ResourceNotAvailableException e) {
                throw new DataInitException("Fatal error during init db. No data available.", e);
            }
        }
    }

    @Scheduled(fixedRateString = "${app.schedule.rate}")
    public void refreshCurrentDayData() {
        List<DailyStatistic> updateList = new ArrayList<>();
        try {
            List<DailyStatistic> repositoryDsList = repository.findAllByDate(LocalDate.now());
            List<DailyStatistic> foreignDsList = foreignDataSource.getCurrentDayWorldStat();
            for (int i = 0; i < foreignDsList.size() - 1; i++) {
                for (int j = 0; j < repositoryDsList.size() - 1; j++) {
                    DailyStatistic fds = foreignDsList.get(i);
                    DailyStatistic rds = repositoryDsList.get(j);
                    if (fds.getCountry().getName().equals(rds.getCountry().getName())) {
                        if (fds.getDate().isEqual(rds.getDate())) {
                            if (fds.getCases() != rds.getCases()
                                    || fds.getRecovered() != rds.getRecovered()
                                    || fds.getDeaths() != rds.getDeaths()) {
                                rds.setCases(fds.getCases());
                                rds.setRecovered(fds.getRecovered());
                                rds.setDeaths(fds.getDeaths());
                                updateList.add(j, rds);
                            }
                        }
                    }
                }
            }
            repository.saveAll(updateList);
        } catch (ResourceNotAvailableException e) {
            throw new DataInitException("Refresh data error", e);
        }
    }
}
