package com.example.coronavirus.foreignDataSource.lmao.model.johnHopkins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Stats {
 private int confirmed;
 private int deaths;
 private int recovered;


}
