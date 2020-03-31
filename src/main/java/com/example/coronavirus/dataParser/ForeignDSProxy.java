package com.example.coronavirus.dataParser;

import com.example.coronavirus.dataParser.exception.ResourceNotAvailableException;
import com.example.coronavirus.model.DailyStatistic;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author Oleg Pavlyukov
 * on 30.03.2020
 * cpabox777@gmail.com
 */
@Data
@AllArgsConstructor
public class ForeignDSProxy implements ForeignDataSource {
    private ForeignDataSource[] foreignDataSources;

    @Override
    public List<DailyStatistic> getCurrentDayWorldStat() throws ResourceNotAvailableException {
        List<DailyStatistic> dailyStatisticList = null;

        for (ForeignDataSource dataSource : foreignDataSources) {
            try {
                dailyStatisticList = dataSource.getCurrentDayWorldStat();
            } catch (ResourceNotAvailableException e) {
                e.printStackTrace();
            }
            if (dailyStatisticList != null) {
                return dailyStatisticList;
            }
        }
        throw new ResourceNotAvailableException("All resources is not available");
    }

    @Override
    public List<DailyStatistic> getStatsByAllCountries() throws ResourceNotAvailableException {
        List<DailyStatistic> dailyStatisticList = null;

        for (ForeignDataSource dataSource : foreignDataSources) {
            try {
                dailyStatisticList = dataSource.getStatsByAllCountries();
            } catch (ResourceNotAvailableException e) {
                e.printStackTrace();
            }
            if (dailyStatisticList != null) {
                return dailyStatisticList;
            }
        }
        throw new ResourceNotAvailableException("All resources is not available");
    }

    @Override
    public List<DailyStatistic> getStatsByCountry(String country) throws ResourceNotAvailableException {
        List<DailyStatistic> dailyStatisticList = null;

        for (ForeignDataSource dataSource : foreignDataSources) {
            try {
                dailyStatisticList = dataSource.getStatsByCountry(country);
            } catch (ResourceNotAvailableException e) {
                e.printStackTrace();
            }
            if (dailyStatisticList != null) {
                return dailyStatisticList;
            }
        }
        throw new ResourceNotAvailableException("All resources is not available");
    }
}
