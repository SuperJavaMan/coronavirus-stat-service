package com.example.coronavirus.foreignDataSource;

import com.example.coronavirus.foreignDataSource.exception.ResourceNotAvailableException;
import com.example.coronavirus.model.DailyStatistic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Oleg Pavlyukov
 * on 30.03.2020
 * cpabox777@gmail.com
 */
@Data
@AllArgsConstructor
@Slf4j
public class ForeignDSProxy implements ForeignDataSource {

    private ForeignDataSource[] foreignDataSources;

    @Override
    public List<DailyStatistic> getCurrentDayWorldStat() throws ResourceNotAvailableException {
        List<DailyStatistic> dailyStatisticList = null;

        for (ForeignDataSource dataSource : foreignDataSources) {
            try {
                dailyStatisticList = dataSource.getCurrentDayWorldStat();
            } catch (ResourceNotAvailableException e) {
                log.warn("DS not available", e);
            }
            if (dailyStatisticList != null) {
                return dailyStatisticList;
            }
        }
        log.error("All foreign resources are not available! Check internet connection or list of DS");
        throw new ResourceNotAvailableException("All resources are not available");
    }

    @Override
    public List<DailyStatistic> getStatsByAllCountries() throws ResourceNotAvailableException {
        List<DailyStatistic> dailyStatisticList = null;

        for (ForeignDataSource dataSource : foreignDataSources) {
            try {
                dailyStatisticList = dataSource.getStatsByAllCountries();
            } catch (ResourceNotAvailableException e) {
                log.warn("DS not available", e);
            }
            if (dailyStatisticList != null) {
                return dailyStatisticList;
            }
        }
        log.error("All foreign resources are not available! Check internet connection or list of DS");
        throw new ResourceNotAvailableException("All resources is not available");
    }

    @Override
    public List<DailyStatistic> getStatsByCountry(String country) throws ResourceNotAvailableException {
        List<DailyStatistic> dailyStatisticList = null;

        for (ForeignDataSource dataSource : foreignDataSources) {
            try {
                dailyStatisticList = dataSource.getStatsByCountry(country);
            } catch (ResourceNotAvailableException e) {
                log.warn("DS not available", e);
            }
            if (dailyStatisticList != null) {
                return dailyStatisticList;
            }
        }
        log.error("All foreign resources are not available! Check internet connection or list of DS");
        throw new ResourceNotAvailableException("All resources is not available");
    }
}
