package com.example.coronavirus.foreignDataSource.lmao;

import com.example.coronavirus.foreignDataSource.exception.ResourceNotAvailableException;
import com.example.coronavirus.model.DailyStatistic;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Oleg Pavlyukov
 * on 30.03.2020
 * cpabox777@gmail.com
 */
class LmaoApiDSTest {

    private LmaoApiDS lmaoApiDS = new LmaoApiDS();

    @Test
    void getStatsByAllCountries() throws ResourceNotAvailableException {
        List<DailyStatistic> dailyStatisticList = lmaoApiDS.getStatsByAllCountries();

        assertNotNull(dailyStatisticList);
    }

    @Test
    void getStatsByCountry() throws ResourceNotAvailableException {
        List<DailyStatistic> russianStatList = lmaoApiDS.getStatsByCountry("russia");

        assertNotNull(russianStatList);
        assertEquals("russia", russianStatList.get(0).getCountry().getName().toLowerCase());
    }
}
