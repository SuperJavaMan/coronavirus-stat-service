package com.example.coronavirus.foreignDataSource.lmao.model.johnHopkins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JohnHopkinsDto {
 private String country;
 private String province;
 private String updatedAt;
 private Stats StatsObject;


 // Getter Methods 

 public String getCountry() {
  return country;
 }

 public String getProvince() {
  return province;
 }

 public String getUpdatedAt() {
  return updatedAt;
 }

 public Stats getStats() {
  return StatsObject;
 }

 // Setter Methods 

 public void setCountry(String country) {
  this.country = country;
 }

 public void setProvince(String province) {
  this.province = province;
 }

 public void setUpdatedAt(String updatedAt) {
  this.updatedAt = updatedAt;
 }

 public void setStats(Stats statsObject) {
  this.StatsObject = statsObject;
 }
}

