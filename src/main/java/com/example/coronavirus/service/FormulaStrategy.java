package com.example.coronavirus.service;

import com.example.coronavirus.exception.NoDataException;
import com.example.coronavirus.model.DailyStatistic;

import java.util.List;

/**
 * @author Oleg Pavlyukov
 * on 03.04.2020
 * cpabox777@gmail.com
 */
public interface FormulaStrategy {

    List<DailyStatistic> getForecast(int forDays) throws NoDataException;
}
