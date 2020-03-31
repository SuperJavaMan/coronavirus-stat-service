package com.example.coronavirus.dataParser;

import com.example.coronavirus.dataParser.exception.ResourceNotAvailableException;
import com.example.coronavirus.model.DailyStatistic;

import java.util.List;

/**
 * @author Oleg Pavlyukov
 * on 29.03.2020
 * cpabox777@gmail.com
 */
public interface ForeignDataSource {
    List<DailyStatistic> getCurrentDayWorldStat() throws ResourceNotAvailableException;
    List<DailyStatistic> getStatsByAllCountries() throws ResourceNotAvailableException;
    List<DailyStatistic> getStatsByCountry(String country) throws ResourceNotAvailableException;
}
