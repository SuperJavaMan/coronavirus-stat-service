package com.example.coronavirus.foreignDataSource.johnHopkins;

import com.example.coronavirus.foreignDataSource.exception.ResourceNotAvailableException;
import com.example.coronavirus.foreignDataSource.lmao.JohnHopkinsApiDS;
import com.example.coronavirus.model.DailyStatistic;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Oleg Pavlyukov
 * on 30.03.2020
 * cpabox777@gmail.com
 */
class JohnHopkinsApiDSTest {

    @Test
    void getCurrentDayWorldStat() throws ResourceNotAvailableException {
        JohnHopkinsApiDS johnHopkinsApiDS = new JohnHopkinsApiDS();

        List<DailyStatistic> dailyStatisticList = johnHopkinsApiDS.getCurrentDayWorldStat();

        assertNotNull(dailyStatisticList);
        assertTrue(dailyStatisticList.size() > 0);
    }
}
