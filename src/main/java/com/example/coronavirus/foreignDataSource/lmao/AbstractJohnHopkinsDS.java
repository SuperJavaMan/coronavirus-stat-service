package com.example.coronavirus.foreignDataSource.lmao;

import com.example.coronavirus.foreignDataSource.ForeignDataSource;
import com.example.coronavirus.foreignDataSource.lmao.model.johnHopkins.JohnHopkinsDto;
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
        } else if (countryDtoName.equalsIgnoreCase("Bosnia and Herzegovina")) {
            country.setName("Bosnia");
        } else if (countryDtoName.equalsIgnoreCase("Korea, South")) {
            country.setName("S. Korea");
        } else if (countryDtoName.equalsIgnoreCase("North Macedonia")) {
            country.setName("Macedonia");
        } else if (countryDtoName.equalsIgnoreCase("Moldova")) {
            country.setName("Moldova, Republic of");
        } else if (countryDtoName.equalsIgnoreCase("United Arab Emirates")) {
            country.setName("UAE");
        } else if (countryDtoName.equalsIgnoreCase("Congo (Kinshasa)")) {
            country.setName("Congo");
        } else if (countryDtoName.equalsIgnoreCase("Laos")) {
            country.setName("Lao People\"s Democratic Republic");
        } else if (countryDtoName.equalsIgnoreCase("Taiwan*")) {
            country.setName("Taiwan");
        } else if (countryDtoName.equalsIgnoreCase("Tanzania")) {
            country.setName("Tanzania, United Republic of");
        } else if (countryDtoName.equalsIgnoreCase("Syria*")) {
            country.setName("Syrian Arab Republic");
        } else if (countryDtoName.equalsIgnoreCase("Libya*")) {
            country.setName("Libyan Arab Jamahiriya");
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
