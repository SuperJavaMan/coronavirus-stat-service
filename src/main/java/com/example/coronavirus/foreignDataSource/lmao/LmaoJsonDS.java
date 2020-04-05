package com.example.coronavirus.foreignDataSource.lmao;

import com.example.coronavirus.foreignDataSource.exception.ResourceNotAvailableException;
import com.example.coronavirus.model.DailyStatistic;

import java.util.List;

/**
 * @author Oleg Pavlyukov
 * on 30.03.2020
 * cpabox777@gmail.com
 */
@Deprecated
public class LmaoJsonDS extends AbstractLmaoDS {
    @Override
    public List<DailyStatistic> getCurrentDayWorldStat() throws ResourceNotAvailableException {
        return null;
    }

    @Override
    public List<DailyStatistic> getStatsByAllCountries() throws ResourceNotAvailableException {
        throw new ResourceNotAvailableException("LmaoJsonDS not available");
    }

    @Override
    public List<DailyStatistic> getStatsByCountry(String country) {
        return null;
    }
}
