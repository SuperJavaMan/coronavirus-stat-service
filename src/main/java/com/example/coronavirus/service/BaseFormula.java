package com.example.coronavirus.service;

import com.example.coronavirus.exception.NoDataException;
import com.example.coronavirus.model.Country;
import com.example.coronavirus.model.DailyStatistic;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Oleg Pavlyukov
 * on 03.04.2020
 * cpabox777@gmail.com
 */
@Slf4j
public class BaseFormula implements FormulaStrategy{

    private List<DailyStatistic> baseList;

    public BaseFormula(List<DailyStatistic> baseList) {
        log.debug("Base formula template list = " + baseList);
        this.baseList = baseList;
    }

    @Override
    public List<DailyStatistic> getForecast(int forDays) throws NoDataException {
        if (baseList == null || baseList.size() < 2)
            throw new NoDataException("Not enough data for forecast.");
        baseList.sort(Comparator.comparing(DailyStatistic::getDate,
                Comparator.nullsLast(Comparator.reverseOrder())).reversed());
        int[] casesArray = baseList.stream().mapToInt(DailyStatistic::getCases).toArray();
        int[] deathsArray = baseList.stream().mapToInt(DailyStatistic::getDeaths).toArray();
        int[] recoveredArray = baseList.stream().mapToInt(DailyStatistic::getRecovered).toArray();

        final double casesGrowthQ = calcAverageGrowthQ(casesArray);
        final double deathsGrowthQ = calcAverageGrowthQ(deathsArray);
        final double recoveredGrowthQ = calcAverageGrowthQ(recoveredArray);

        List<DailyStatistic> forecastList = new ArrayList<>();
        baseList.sort(Comparator.comparing(DailyStatistic::getDate,
                                            Comparator.nullsLast(Comparator.reverseOrder())));
        DailyStatistic baseDs = baseList.get(0);
        Country country = baseDs.getCountry();
        for (int i = 0; i < forDays; i++) {

            DailyStatistic ds = new DailyStatistic();
            ds.setCountry(country);
            ds.setDate(baseDs.getDate().plusDays(1));
            ds.setCases(calcFutureValue(baseDs.getCases(), casesGrowthQ));
            ds.setDeaths(calcFutureValue(baseDs.getDeaths(), deathsGrowthQ));
            ds.setRecovered(calcFutureValue(baseDs.getRecovered(), recoveredGrowthQ));
            forecastList.add(ds);
            baseDs = ds;
        }

        return forecastList;
    }

    private int calcFutureValue(int base, double growthQ) {
        return (int) (base + (base * growthQ));
    }

    private double calcAverageGrowthQ(int[] data) {
        double sumQ = 0;
        for (int i = 1; i < data.length; i++) {
            double first = data[i - 1];
            double second = data[i];
            sumQ += (second - first) / first;
        }
        return sumQ / (data.length - 1);
    }
}
