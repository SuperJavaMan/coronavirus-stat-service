package com.example.coronavirus.foreignDataSource.twoGis.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TwoGisDto {

private String date;
private List<Item> items = new ArrayList<>();

public String getDate() {
return date;
}

public void setDate(String date) {
this.date = date;
}

public List<Item> getItems() {
return items;
}

public void setItems(List<Item> items) {
this.items = items;
}

}
