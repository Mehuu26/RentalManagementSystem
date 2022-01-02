package com.api;

import javafx.beans.property.SimpleStringProperty;

public class Reservation {
    private SimpleStringProperty name;
    private SimpleStringProperty surname;
    private SimpleStringProperty product;
    private SimpleStringProperty productId;
    private SimpleStringProperty start;
    private SimpleStringProperty end;

    public Reservation(String name, String surname, String userId, String product, String productId, String start, String end, String status) {
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.product = new SimpleStringProperty(product);
        this.productId = new SimpleStringProperty(productId);
        this.start = new SimpleStringProperty(start);
        this.end = new SimpleStringProperty(end);
        this.status = new SimpleStringProperty(status);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public void setProduct(String product) {
        this.product.set(product);
    }

    public void setProductId(String productId) {
        this.productId.set(productId);
    }

    public void setStart(String start) {
        this.start.set(start);
    }

    public void setEnd(String end) {
        this.end.set(end);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    private SimpleStringProperty status;

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getSurname() {
        return surname.get();
    }

    public SimpleStringProperty surnameProperty() {
        return surname;
    }

    public String getProduct() {
        return product.get();
    }

    public SimpleStringProperty productProperty() {
        return product;
    }

    public String getProductId() {
        return productId.get();
    }

    public SimpleStringProperty productIdProperty() {
        return productId;
    }

    public String getStart() {
        return start.get();
    }

    public SimpleStringProperty startProperty() {
        return start;
    }

    public String getEnd() {
        return end.get();
    }

    public SimpleStringProperty endProperty() {
        return end;
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }



}
