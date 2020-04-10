package com.example.coronavirus.foreignDataSource.twoGis.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class I18nCountryNames {

private String ar;
private String ru;
private String en;
private String uk;

public String getAr() {
return ar;
}

public void setAr(String ar) {
this.ar = ar;
}

public String getRu() {
return ru;
}

public void setRu(String ru) {
this.ru = ru;
}

public String getEn() {
return en;
}

public void setEn(String en) {
this.en = en;
}

public String getUk() {
return uk;
}

public void setUk(String uk) {
this.uk = uk;
}

    @Override
    public String toString() {
        return "CountryNames{" +
                "ar='" + ar + '\'' +
                ", ru='" + ru + '\'' +
                ", en='" + en + '\'' +
                ", uk='" + uk + '\'' +
                '}';
    }
}
