package com.example.coronavirus.foreignDataSource.rapidApiCovid193.model;

public class Cases {

private String _new;
private Integer active;
private Integer critical;
private Integer recovered;
private Integer total;

public String getNew() {
return _new;
}

public void setNew(String _new) {
this._new = _new;
}

public Integer getActive() {
return active;
}

public void setActive(Integer active) {
this.active = active;
}

public Integer getCritical() {
return critical;
}

public void setCritical(Integer critical) {
this.critical = critical;
}

public Integer getRecovered() {
return recovered;
}

public void setRecovered(Integer recovered) {
this.recovered = recovered;
}

public Integer getTotal() {
return total;
}

public void setTotal(Integer total) {
this.total = total;
}

}
