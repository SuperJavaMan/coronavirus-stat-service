package com.example.coronavirus.foreignDataSource.twoGis;

import com.example.coronavirus.foreignDataSource.exception.ResourceNotAvailableException;
import com.example.coronavirus.model.Country;
import com.example.coronavirus.model.DailyStatistic;
import com.example.coronavirus.repository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Oleg Pavlyukov
 * on 10.04.2020
 * cpabox777@gmail.com
 */
//@SpringBootTest
//class TwoGisDSTest {
//
//    @Autowired
//    CountryRepository countryRepository;
//    @Test
//    void getCurrentDayWorldStat() throws ResourceNotAvailableException {
//        TwoGisDS twoGisDS = new TwoGisDS();
//        List<DailyStatistic> dailyStatisticList = twoGisDS.getCurrentDayWorldStat();
//        assertNotNull(dailyStatisticList);
//        for (DailyStatistic ds : dailyStatisticList) {
//            Country detachCountry;
//            detachCountry = countryRepository.findCountryByName(ds.getCountry().getName());
//            if (detachCountry == null)
//             detachCountry = countryRepository.findCountryByNameLike( "%" + ds.getCountry().getName() + "%");
//            if (detachCountry != null) {
//                detachCountry.setInterNames(ds.getCountry().getInterNames());
//                countryRepository.save(detachCountry);
//            } else {
//                System.out.println("Country not found -> " + ds.getCountry().getName());
//            }
//        }
//    }
//}
