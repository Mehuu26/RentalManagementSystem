package com.api;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Equipment {
    private SimpleStringProperty type;
    private SimpleStringProperty producer;
    private SimpleStringProperty model;
    private SimpleStringProperty size;
    private SimpleStringProperty productId;

    public Equipment(String type, String producer, String model, String size, String productId){
        this.type = new SimpleStringProperty(type);
        this.producer = new SimpleStringProperty(producer);
        this.model = new SimpleStringProperty(model);
        this.size = new SimpleStringProperty(size);
        this.productId = new SimpleStringProperty(productId);
    }






}
