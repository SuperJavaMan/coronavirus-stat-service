package com.example.coronavirus.foreignDataSource.rapidApiCovid193;

import com.example.coronavirus.foreignDataSource.ForeignDataSource;
import com.example.coronavirus.foreignDataSource.rapidApiCovid193.model.Covid193Dto;
import com.example.coronavirus.foreignDataSource.rapidApiCovid193.model.Response;
import com.example.coronavirus.model.Country;
import com.example.coronavirus.model.DailyStatistic;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

            String countryDtoName = countryStat.getCountry().replaceAll("-", " ");
            Country country = new Country();
            if (countryDtoName.equalsIgnoreCase("Bosnia and Herzegovina")) {
                country.setName("Bosnia");
            } else if (countryDtoName.equalsIgnoreCase("S Korea")) {
                country.setName("S. Korea");
            } else if (countryDtoName.equalsIgnoreCase("North Macedonia")) {
                country.setName("Macedonia");
            } else if (countryDtoName.equalsIgnoreCase("Moldova")) {
                country.setName("Moldova, Republic of");
            } else if (countryDtoName.equalsIgnoreCase("Laos")) {
                country.setName("Lao People\"s Democratic Republic");
            } else if (countryDtoName.equalsIgnoreCase("Tanzania")) {
                country.setName("Tanzania, United Republic of");
            } else if (countryDtoName.equalsIgnoreCase("Syria")) {
                country.setName("Syrian Arab Republic");
            } else if (countryDtoName.equalsIgnoreCase("Libya")) {
                country.setName("Libyan Arab Jamahiriya");
            } else {
                country.setName(countryDtoName);
            }

            ds.setCountry(country);
            ds.setDate(date);
            ds.setCases(countryStat.getCases().getTotal());
            ds.setDeaths(countryStat.getDeaths().getTotal());
            ds.setRecovered(countryStat.getCases().getRecovered());
            Integer tested = countryStat.getTests().getTotal();
            if (tested == null) {
                ds.setTested(-1);
            } else {
                ds.setTested(tested);
            }
            dailyStatisticList.add(ds);
        }

        return dailyStatisticList;
    }

}
