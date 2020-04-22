package com.example.coronavirus.foreignDataSource.rapidApiCovid193;

import com.example.coronavirus.foreignDataSource.ForeignDataSource;
import com.example.coronavirus.foreignDataSource.rapidApiCovid193.model.Covid193Dto;
import com.example.coronavirus.foreignDataSource.rapidApiCovid193.model.Response;
import com.example.coronavirus.model.Country;
import com.example.coronavirus.model.DailyStatistic;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Oleg Pavlyukov
 * on 15.04.2020
 * cpabox777@gmail.com
 */
@Slf4j
public abstract class AbstractCovid193 implements ForeignDataSource {

    protected List<DailyStatistic> convertToDailyStatistic(Covid193Dto dto) {
        List<Response> countryStatList = dto.getResponse();
        List<DailyStatistic> dailyStatisticList = new ArrayList<>();

        for (Response countryStat : countryStatList) {
            DailyStatistic ds = new DailyStatistic();
            LocalDate date = LocalDate.parse(countryStat.getDay());

            String countryName = countryStat.getCountry().replaceAll("-", " ");
            Country country = new Country();
            country.setName(countryName);

            ds.setCountry(country);
            ds.setDate(date);
            ds.setCases(countryStat.getCases().getTotal());
            ds.setDeaths(countryStat.getDeaths().getTotal());
            ds.setRecovered(countryStat.getCases().getRecovered());
            ds.setTested(countryStat.getTests().getTotal());
            dailyStatisticList.add(ds);
        }

        return dailyStatisticList;
    }

}
