package com.example.coronavirus.dataParser.lmao;

import com.example.coronavirus.dataParser.exception.ResourceNotAvailableException;
import com.example.coronavirus.model.DailyStatistic;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
        assertEquals("Afghanistan", dailyStatisticList.get(0).getCountry().getName());
    }

    @Test
    void getStatsByCountry() throws ResourceNotAvailableException {
        List<DailyStatistic> russianStatList = lmaoApiDS.getStatsByCountry("russia");

        assertNotNull(russianStatList);
        assertEquals("russia", russianStatList.get(0).getCountry().getName().toLowerCase());
    }
}
