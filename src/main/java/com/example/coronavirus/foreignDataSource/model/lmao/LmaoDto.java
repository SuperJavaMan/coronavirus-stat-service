package com.example.coronavirus.foreignDataSource.model.lmao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LmaoDto {
 private String country;
 private String province = null;
 private Timeline TimelineObject;


 // Getter Methods 

 public String getCountry() {
  return country;
 }

 public String getProvince() {
  return province;
 }

 public Timeline getTimeline() {
  return TimelineObject;
 }

 // Setter Methods 

 public void setCountry(String country) {
  this.country = country;
 }

 public void setProvince(String province) {
  this.province = province;
 }

 public void setTimeline(Timeline timelineObject) {
  this.TimelineObject = timelineObject;
 }

 @Override
 public String toString() {
  return "Codebeautify{" +
          "country='" + country + '\'' +
          ", province='" + province + '\'' +
          ", TimelineObject=" + TimelineObject +
          '}';
 }
}
