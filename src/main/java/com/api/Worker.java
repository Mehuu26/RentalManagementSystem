package com.api;

import javafx.beans.property.SimpleStringProperty;

public class Worker {
    private SimpleStringProperty user;
    private SimpleStringProperty name;
    private SimpleStringProperty surname;

    public Worker(String user, String name, String surname){
        this.user = new SimpleStringProperty(user);
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
    }

    public String getUser() {
        return user.get();
    }

    public SimpleStringProperty userProperty() {
        return user;
    }

    public void setUser(String user) {
        this.user.set(user);
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




}
