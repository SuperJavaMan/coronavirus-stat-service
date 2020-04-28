package com.example.coronavirus.foreignDataSource.rapidApiCovid193.model;

import java.util.ArrayList;
import java.util.List;

public class Covid193Dto {

private String get;
private List<Object> parameters = new ArrayList<>();
private List<Object> errors = new ArrayList<>();
private Integer results;
private List<Response> response = new ArrayList<>();

public String getGet() {
return get;
}

public void setGet(String get) {
this.get = get;
}

public List<Object> getParameters() {
return parameters;
}

public void setParameters(List<Object> parameters) {
this.parameters = parameters;
}

public List<Object> getErrors() {
return errors;
}

public void setErrors(List<Object> errors) {
this.errors = errors;
}

public Integer getResults() {
return results;
}

public void setResults(Integer results) {
this.results = results;
}

public List<Response> getResponse() {
return response;
}

public void setResponse(List<Response> response) {
this.response = response;
}

}
