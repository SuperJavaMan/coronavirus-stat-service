package com.example.coronavirus.foreignDataSource.config;

import com.example.coronavirus.foreignDataSource.ForeignDSProxy;
import com.example.coronavirus.foreignDataSource.ForeignDataSource;
import com.example.coronavirus.foreignDataSource.lmao.JohnHopkinsApiDS;
import com.example.coronavirus.foreignDataSource.lmao.LmaoApiDS;
import com.example.coronavirus.foreignDataSource.rapidApiCovid193.Covid193DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * @author Oleg Pavlyukov
 * on 30.03.2020
 * cpabox777@gmail.com
 */
@Configuration
@Slf4j
public class ForeignDataSourceConfig {

    @Bean
    public ForeignDataSource foreignDataSource() {
        ForeignDataSource[] foreignDataSources = {new Covid193DS(), new LmaoApiDS(), new JohnHopkinsApiDS()};
        log.info("Active foreign data sources -> " + Arrays.toString(foreignDataSources));
        return new ForeignDSProxy(foreignDataSources);
    }
}
