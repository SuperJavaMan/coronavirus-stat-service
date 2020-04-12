package com.example.coronavirus.repository;

import com.example.coronavirus.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Oleg Pavlyukov
 * on 01.04.2020
 * cpabox777@gmail.com
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    Country findCountryByName(String countryName);
    Country findCountryByNameLike(String countryName);
}
