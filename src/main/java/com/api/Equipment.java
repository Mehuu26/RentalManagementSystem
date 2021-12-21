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

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getProducer() {
        return producer.get();
    }

    public SimpleStringProperty producerProperty() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer.set(producer);
    }

    public String getModel() {
        return model.get();
    }

    public SimpleStringProperty modelProperty() {
        return model;
    }

    public void setModel(String model) {
        this.model.set(model);
    }

    public String getSize() {
        return size.get();
    }

    public SimpleStringProperty sizeProperty() {
        return size;
    }

    public void setSize(String size) {
        this.size.set(size);
    }

    public String getProductId() {
        return productId.get();
    }

    public SimpleStringProperty productIdProperty() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId.set(productId);
    }
}
