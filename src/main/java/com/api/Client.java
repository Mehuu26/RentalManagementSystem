package com.api;

import javafx.beans.property.SimpleStringProperty;
import org.cryptonode.jncryptor.AES256JNCryptor;
import org.cryptonode.jncryptor.CryptorException;
import org.cryptonode.jncryptor.JNCryptor;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;

public class Client {
    private SimpleStringProperty name;
    private SimpleStringProperty surname;
    private SimpleStringProperty phone;
    private SimpleStringProperty idCard;
    private SimpleStringProperty _id;

    public Client(String name, String surname, String phone, String idCard, String _id){
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.phone = new SimpleStringProperty(phone);
        this.idCard = new SimpleStringProperty(idCard);
        this._id = new SimpleStringProperty(_id);
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
