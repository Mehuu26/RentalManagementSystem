package com.api;

import javafx.beans.property.SimpleStringProperty;

public class TypeAndPriceList {
    private SimpleStringProperty type;
    private SimpleStringProperty hour;
    private SimpleStringProperty day;

    public TypeAndPriceList(String type, String hour, String day) {
        this.type = new SimpleStringProperty(type);
        this.hour = new SimpleStringProperty(hour);
        this.day = new SimpleStringProperty(day);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getHour() {
        return hour.get();
    }

    public SimpleStringProperty hourProperty() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour.set(hour);
    }

    public String getDay() {
        return day.get();
    }

    public SimpleStringProperty dayProperty() {
        return day;
    }

    public void setDay(String day) {
        this.day.set(day);
    }
}
