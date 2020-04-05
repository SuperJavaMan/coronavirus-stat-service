package com.example.coronavirus.foreignDataSource;

import com.example.coronavirus.foreignDataSource.exception.ResourceNotAvailableException;
import com.example.coronavirus.foreignDataSource.lmao.JohnHopkinsApiDS;
import com.example.coronavirus.foreignDataSource.lmao.LmaoApiDS;
import com.example.coronavirus.foreignDataSource.lmao.LmaoJsonDS;
import com.example.coronavirus.model.DailyStatistic;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Oleg Pavlyukov
 * on 30.03.2020
 * cpabox777@gmail.com
 */
class ForeignDSProxyTest {
    @Test
    void getCurrentDayWorldStat() throws ResourceNotAvailableException {
        ForeignDataSource[] foreignDataSources = {new LmaoJsonDS(), new LmaoApiDS(), new JohnHopkinsApiDS()};
        ForeignDataSource dataSource = new ForeignDSProxy(foreignDataSources);

        List<DailyStatistic> dailyStatisticList = dataSource.getCurrentDayWorldStat();

        assertNotNull(dailyStatisticList);
        assertTrue(dailyStatisticList.size() > 0);
    }

    @Test
    void getStatsByAllCountries() throws ResourceNotAvailableException {
        ForeignDataSource[] foreignDataSources = {new LmaoJsonDS(), new LmaoApiDS()};
        ForeignDataSource dataSource = new ForeignDSProxy(foreignDataSources);

        List<DailyStatistic> dailyStatisticList = dataSource.getStatsByAllCountries();

        assertNotNull(dailyStatisticList);
        assertTrue(dailyStatisticList.size() > 0);
    }

    @Test
    void getStatsByCountry() throws ResourceNotAvailableException {
        String country = "russia";
        ForeignDataSource[] foreignDataSources = {new LmaoJsonDS(), new LmaoApiDS()};
        ForeignDataSource dataSource = new ForeignDSProxy(foreignDataSources);

        List<DailyStatistic> dailyStatisticList = dataSource.getStatsByCountry(country);

        assertNotNull(dailyStatisticList);
        assertEquals(country, dailyStatisticList.get(0).getCountry().getName().toLowerCase());
    }
}
