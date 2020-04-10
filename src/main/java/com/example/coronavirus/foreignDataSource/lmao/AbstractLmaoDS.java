package com.example.coronavirus.foreignDataSource.lmao;

import com.example.coronavirus.foreignDataSource.ForeignDataSource;
import com.example.coronavirus.foreignDataSource.lmao.model.lmao.LmaoDto;
import com.example.coronavirus.model.Country;
import com.example.coronavirus.model.DailyStatistic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Oleg Pavlyukov
 * on 29.03.2020
 * cpabox777@gmail.com
 */
public abstract class AbstractLmaoDS implements ForeignDataSource {

    protected List<DailyStatistic> convertToDailyStatistic(LmaoDto lmaoDto) {
        Country country = new Country();
        country.setName(lmaoDto.getCountry());

        return lmaoDto.getTimeline().getCases().keySet().stream()
                .map(key -> {
                    DailyStatistic dailyStatistic = new DailyStatistic();
                    dailyStatistic.setCountry(country);
                    dailyStatistic.setDate(parseDateString(key));
                    dailyStatistic.setCases(Integer.parseInt(lmaoDto.getTimeline().getCases().get(key)));
                    dailyStatistic.setDeaths(Integer.parseInt(lmaoDto.getTimeline().getDeaths().get(key)));
                    dailyStatistic.setRecovered(Integer.parseInt(lmaoDto.getTimeline().getRecovered().get(key)));

                    return dailyStatistic;
                }).collect(Collectors.toList());
    }

    private LocalDate parseDateString(String stringDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yy");
        TemporalAccessor ta = formatter.parse(stringDate);
        return LocalDate.from(ta);
    }
}
