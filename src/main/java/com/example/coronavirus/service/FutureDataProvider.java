package com.example.coronavirus.service;

import com.example.coronavirus.exception.NoDataException;
import com.example.coronavirus.model.DailyStatistic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Oleg Pavlyukov
 * on 03.04.2020
 * cpabox777@gmail.com
 */
@Service
@Slf4j
public class FutureDataProvider {

    private final DataProvider dataProvider;

    @Autowired
    public FutureDataProvider(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Deprecated
    public List<DailyStatistic> getForecastByPreviousDays(Long countryId, int daysCount) throws NoDataException {
        log.debug("Input countryId = " + countryId + ", daysCount = " + daysCount);
        List<DailyStatistic> templateList = dataProvider.getCountryStatFromToDate(countryId,
                                                    LocalDate.now().minusDays(5),
                                                    LocalDate.now());
        FormulaStrategy formulaStrategy = new BaseFormula(templateList);
        return formulaStrategy.getForecast(daysCount);
    }
}
