package com.example.coronavirus.dataParser.lmao;

import com.example.coronavirus.dataParser.ForeignDataSource;
import com.example.coronavirus.dataParser.model.johnHopkins.JohnHopkinsDto;
import com.example.coronavirus.model.Country;
import com.example.coronavirus.model.DailyStatistic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * @author Oleg Pavlyukov
 * on 30.03.2020
 * cpabox777@gmail.com
 */
public abstract class AbstractJohnHopkinsDS implements ForeignDataSource {

    protected DailyStatistic convertToDailyStatistic(JohnHopkinsDto dto) {
        Country country = new Country();
        String countryDtoName = dto.getCountry();

        if (countryDtoName.equalsIgnoreCase("US")) {
            country.setName("USA");
        } else if (countryDtoName.equalsIgnoreCase("United Kingdom")) {
            country.setName("UK");
        } else {
            country.setName(countryDtoName);
        }

        DailyStatistic dailyStatistic = new DailyStatistic();
        dailyStatistic.setCountry(country);
        dailyStatistic.setDate(parseDateString(dto.getUpdatedAt()));
        dailyStatistic.setCases(dto.getStats().getConfirmed());
        dailyStatistic.setRecovered(dto.getStats().getRecovered());
        dailyStatistic.setDeaths(dto.getStats().getDeaths());

        return dailyStatistic;
    }

    private LocalDate parseDateString(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        TemporalAccessor ta = formatter.parse(date.replaceAll("\\s(.*)", ""));
        return LocalDate.from(ta);
    }
}
