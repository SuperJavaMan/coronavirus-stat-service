package com.example.coronavirus.service;

import com.example.coronavirus.foreignDataSource.ForeignDataSource;
import com.example.coronavirus.foreignDataSource.exception.ResourceNotAvailableException;
import com.example.coronavirus.exception.DataInitException;
import com.example.coronavirus.model.DailyStatistic;
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

    private DailyStatRepository repository;
    private ForeignDataSource foreignDataSource;

    @Autowired
    public ScheduledDataUpdater(DailyStatRepository repository, ForeignDataSource foreignDataSource) {
        this.repository = repository;
        this.foreignDataSource = foreignDataSource;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initDataBase() {
        log.debug("Start initDataBase()");
        long recordsCount= repository.count();
        if (recordsCount < 1) {
            try {
                List<DailyStatistic> freshDataList = foreignDataSource.getStatsByAllCountries();
                Map<String, List<DailyStatistic>> mapToSave = freshDataList.stream().collect(Collectors.groupingBy(
                        key-> key.getCountry().getName()));
                int count = 1;
                for (List<DailyStatistic> dailyStatisticList : mapToSave.values()) {
                    repository.saveAll(dailyStatisticList);
                    Thread.sleep(10000);
                    System.out.println("Country num: " + ++count + ". Time: " + LocalDateTime.now());
                }
//                repository.saveAll(freshDataList);
            } catch (ResourceNotAvailableException | InterruptedException e) {
                log.error("Fatal error during init db. No data available.", e);
                throw new DataInitException("Fatal error during init db. No data available.", e);
            }
        }
    }

    @Scheduled(fixedRateString = "${app.schedule.rate}")
    public void refreshCurrentDayData() {
        log.info("Refreshing data at " + LocalDateTime.now());
        List<DailyStatistic> updateList = new ArrayList<>();
        try {
            if (repository.count() < 1) return;

            List<DailyStatistic> repositoryDsList = repository.findAllByDate(LocalDate.now());
            List<DailyStatistic> foreignDsList = foreignDataSource.getCurrentDayWorldStat();
            if (repositoryDsList == null || repositoryDsList.isEmpty()) {
                System.out.println("DB list empty");
                repository.saveAll(foreignDsList);
                return;
            }
            for (int i = 0; i < foreignDsList.size() - 1; i++) {
                for (int j = 0; j < repositoryDsList.size() - 1; j++) {
                    DailyStatistic fds = foreignDsList.get(i);
                    DailyStatistic rds = repositoryDsList.get(j);
                    System.out.println(rds + " " + fds.toString());
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
            if (!updateList.isEmpty()) {
                System.out.println("Update data");
                repository.saveAll(updateList);
            }
        } catch (ResourceNotAvailableException e) {
            log.error("Refresh data error", e);
            throw new DataInitException("Refresh data error", e);
        }
    }
}
