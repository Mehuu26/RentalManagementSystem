package com.api;

import javafx.beans.property.SimpleStringProperty;

public class Client {
    private SimpleStringProperty name;
    private SimpleStringProperty surname;
    private SimpleStringProperty phone;
    private SimpleStringProperty idCard;

    public Client(String name, String surname, String phone, String idCard){
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.phone = new SimpleStringProperty(phone);
        this.idCard = new SimpleStringProperty(idCard);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getSurname() {
        return surname.get();
    }

    public SimpleStringProperty surnameProperty() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public String getPhone() {
        return phone.get();
    }

    public SimpleStringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getIdCard() {
        return idCard.get();
    }

    public SimpleStringProperty idCardProperty() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard.set(idCard);
    }


}
