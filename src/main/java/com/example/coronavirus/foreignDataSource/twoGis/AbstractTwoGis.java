package com.example.coronavirus.foreignDataSource.twoGis;

import com.example.coronavirus.foreignDataSource.ForeignDataSource;
import com.example.coronavirus.foreignDataSource.twoGis.model.Item;
import com.example.coronavirus.foreignDataSource.twoGis.model.TwoGisDto;
import com.example.coronavirus.model.Country;
import com.example.coronavirus.model.DailyStatistic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Oleg Pavlyukov
 * on 10.04.2020
 * cpabox777@gmail.com
 */
public abstract class AbstractTwoGis implements ForeignDataSource {

    protected List<DailyStatistic> convertToDailyStatisticList(TwoGisDto dto) {
        LocalDate date = LocalDate.parse(dto.getDate());
        List<DailyStatistic> dailyStatisticList = new ArrayList<>(220);
        for (Item item : dto.getItems()) {
            Country country = new Country();
            String countryDtoName = item.getCountryCode();

            if (countryDtoName.equalsIgnoreCase("United States")) {
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
            } else if (countryDtoName.equalsIgnoreCase("Tanzania*")) {
                country.setName("Tanzania, United Republic of");
            } else if (countryDtoName.equalsIgnoreCase("Syria*")) {
                country.setName("Syrian Arab Republic");
            } else if (countryDtoName.equalsIgnoreCase("Libya*")) {
                country.setName("Libyan Arab Jamahiriya");
            } else {
                country.setName(item.getCountryCode());
            }
            Map<String, String> interNames = new HashMap<>();

            interNames.put("en", item.getI18nCountryNames().getEn());
            interNames.put("ar", item.getI18nCountryNames().getAr());
            interNames.put("ru", item.getI18nCountryNames().getRu());
            interNames.put("uk", item.getI18nCountryNames().getUk());

            country.setInterNames(interNames);
            country.setLongitude(Double.parseDouble(item.getLong()));
            country.setLatitude(Double.parseDouble(item.getLat()));

            DailyStatistic ds = new DailyStatistic();
            ds.setCountry(country);
            ds.setDate(date);
            ds.setCases(item.getConfirmed());
            ds.setRecovered(item.getRecovered());
            ds.setDeaths(item.getDeaths());
            dailyStatisticList.add(ds);
        }
        return dailyStatisticList;
    }
}
