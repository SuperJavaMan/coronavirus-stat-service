package com.example.coronavirus.dataParser.config;

import com.example.coronavirus.dataParser.ForeignDSProxy;
import com.example.coronavirus.dataParser.ForeignDataSource;
import com.example.coronavirus.dataParser.johnHopkins.JohnHopkinsApiDS;
import com.example.coronavirus.dataParser.lmao.LmaoApiDS;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Oleg Pavlyukov
 * on 30.03.2020
 * cpabox777@gmail.com
 */
@Configuration
public class ForeignDataSourceConfig {

    @Bean
    public ForeignDataSource foreignDataSource() {
        ForeignDataSource[] foreignDataSources = {new LmaoApiDS(), new JohnHopkinsApiDS()};
        return new ForeignDSProxy(foreignDataSources);
    }
}
