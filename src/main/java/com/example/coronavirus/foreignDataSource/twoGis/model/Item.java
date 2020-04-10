package com.example.coronavirus.foreignDataSource.twoGis.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {

private String countryCode;
private I18nCountryNames i18nCountryNames;
private String _long;
private String lat;
private Integer confirmed;
private Integer recovered;
private Integer deaths;
private Integer confirmedInc;
private Integer recoveredInc;
private Integer deathsInc;

public String getCountryCode() {
return countryCode;
}

public void setCountryCode(String countryCode) {
this.countryCode = countryCode;
}

public I18nCountryNames getI18nCountryNames() {
return i18nCountryNames;
}

public void setI18nCountryNames(I18nCountryNames i18nCountryNames) {
this.i18nCountryNames = i18nCountryNames;
}

public String getLong() {
return _long;
}

public void setLong(String _long) {
this._long = _long;
}

public String getLat() {
return lat;
}

public void setLat(String lat) {
this.lat = lat;
}

public Integer getConfirmed() {
return confirmed;
}

public void setConfirmed(Integer confirmed) {
this.confirmed = confirmed;
}

public Integer getRecovered() {
return recovered;
}

public void setRecovered(Integer recovered) {
this.recovered = recovered;
}

public Integer getDeaths() {
return deaths;
}

public void setDeaths(Integer deaths) {
this.deaths = deaths;
}

public Integer getConfirmedInc() {
return confirmedInc;
}

public void setConfirmedInc(Integer confirmedInc) {
this.confirmedInc = confirmedInc;
}

public Integer getRecoveredInc() {
return recoveredInc;
}

public void setRecoveredInc(Integer recoveredInc) {
this.recoveredInc = recoveredInc;
}

public Integer getDeathsInc() {
return deathsInc;
}

public void setDeathsInc(Integer deathsInc) {
this.deathsInc = deathsInc;
}

    @Override
    public String toString() {
        return "Item{" +
                "countryCode='" + countryCode + '\'' +
                ", countryNames=" + i18nCountryNames +
                ", _long='" + _long + '\'' +
                ", lat='" + lat + '\'' +
                ", confirmed=" + confirmed +
                ", recovered=" + recovered +
                ", deaths=" + deaths +
                ", confirmedInc=" + confirmedInc +
                ", recoveredInc=" + recoveredInc +
                ", deathsInc=" + deathsInc +
                '}';
    }
}
