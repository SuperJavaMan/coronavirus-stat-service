package com.example.coronavirus.foreignDataSource.lmao.model.lmao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Timeline {
 private Map<String, String> casesObject;
 private Map<String, String> deathsObject;
 private Map<String, String> recovered;

 public Map<String, String> getCases() {
  return casesObject;
 }

 public Map<String, String> getDeaths() {
  return deathsObject;
 }

 public void setCases(Map<String, String> casesObject) {
  this.casesObject = casesObject;
 }

 public void setDeaths(Map<String, String> deathsObject) {
  this.deathsObject = deathsObject;
 }

 public Map<String, String> getRecovered() {
  return recovered;
 }

 public void setRecovered(Map<String, String> recovered) {
  this.recovered = recovered;
 }

 @Override
 public String toString() {
  return "Timeline{" +
          "casesObject=" + casesObject +
          ", deathsObject=" + deathsObject +
          '}';
 }
}
