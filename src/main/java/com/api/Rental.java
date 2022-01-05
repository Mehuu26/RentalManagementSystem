package com.api;

import javafx.beans.property.SimpleStringProperty;

public class Rental {
    private SimpleStringProperty type;
    private SimpleStringProperty model;
    private SimpleStringProperty size;
    private SimpleStringProperty productId;
    private SimpleStringProperty userId;
    private SimpleStringProperty startDate;
    private SimpleStringProperty finishDate;
    private SimpleStringProperty status;
    private SimpleStringProperty _id;

    public Rental(String type, String model, String size, String productId, String userId, String startDate, String finishDate, String status, String _id){
        this.type = new SimpleStringProperty(type);
        this.model = new SimpleStringProperty(model);
        this.size = new SimpleStringProperty(size);
        this.productId = new SimpleStringProperty(productId);
        this.userId = new SimpleStringProperty(userId);
        this.startDate = new SimpleStringProperty(startDate);
        this.finishDate = new SimpleStringProperty(finishDate);
        this.status = new SimpleStringProperty(status);
        this._id = new SimpleStringProperty(_id);
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

    public String getUserId() {
        return userId.get();
    }

    public SimpleStringProperty userIdProperty() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId.set(userId);
    }

    public String getStartDate() {
        return startDate.get();
    }

    public SimpleStringProperty startDateProperty() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate.set(startDate);
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String get_id() {
        return _id.get();
    }

    public SimpleStringProperty _idProperty() {
        return _id;
    }

    public void set_id(String _id) {
        this._id.set(_id);
    }

    public String getFinishDate() {
        return finishDate.get();
    }

    public SimpleStringProperty finishDateProperty() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate.set(finishDate);
    }

}
