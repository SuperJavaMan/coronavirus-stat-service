package com.example.coronavirus.foreignDataSource.rapidApiCovid193;

import com.example.coronavirus.foreignDataSource.exception.ResourceNotAvailableException;
import com.example.coronavirus.model.DailyStatistic;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Covid193DSTest {

    @Test
    void getCurrentDayWorldStat() throws ResourceNotAvailableException {
        Covid193DS covid193DS = new Covid193DS();
        List<DailyStatistic> dailyStatisticList = covid193DS.getCurrentDayWorldStat();

        assertNotNull(dailyStatisticList);
        assertFalse(dailyStatisticList.isEmpty());
    }
}