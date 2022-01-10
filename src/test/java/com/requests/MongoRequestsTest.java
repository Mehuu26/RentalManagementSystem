package com.requests;

import com.api.Client;
import com.mongodb.MongoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MongoRequestsTest {
    private MongoRequests mongoRequests;
    
    @BeforeEach
    private void beforeEach(){
        mongoRequests = new MongoRequests();
    }

    @Test
    void getEmployeeReturnFalse() {
        //given
        String login = "";
        String password = "";

        //when
        boolean result = MongoRequests.getEmployee(login, password);

        //then
        Assertions.assertFalse(result);
    }

    @Test
    void getEmployeeReturnTrue() {
        //given
        String login = "admin";
        String password = "admin";

        //when
        boolean result = MongoRequests.getEmployee(login, password);

        //then
        Assertions.assertTrue(result);
    }

    @Test
    void deleteEmployeeCatchException() {
        //given
        String user = "";
        Exception testException;

        //when
        testException = Assertions.assertThrows(IllegalArgumentException.class, () ->  MongoRequests.deleteEmployee(user));
        System.out.println(testException);

        //then
        Assertions.assertNotEquals(testException, null, "should caught illegal argument exception");
    }

    @Test
    void addEquipmentReturnFalse() {
        //given
        String type = "";
        String producer = "";
        String model = "";
        String size = "";
        String productId = "";

        //when
        boolean result = MongoRequests.addEquipment(type, producer, model, size, productId);

        //then
        Assertions.assertFalse(result);
    }

    @Test
    void getClicentFalse() {
        //given
        String user = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
        Client client;

        //when
        client = MongoRequests.getClient(user);

        //then
        Assertions.assertNull(client);
    }
}