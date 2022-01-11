package com.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CryptTest extends Crypt{
    Crypt crypt;

    @BeforeEach
    void beforeEach(){
        crypt = new Crypt();
    }

    @Test
    void checkEncrypted() {
        //given
        String tempMessage = "test";
        String tempPasword = "password";
        String tempEncrypted = "b69490e5bF82A1c53AFc0D3ccD8A7A25XOdQg2h9ZAgq3K5PwCSjVQ6360A6101b7DD64012750b89F19D92b4";
        String result;
        String decryptedValue;

        //when
        Crypt.init();
        result = Crypt.encrypt(tempPasword, tempMessage);
        System.out.println(result);
        decryptedValue = Crypt.decrypt(tempPasword, result);

        //then
        Assertions.assertTrue(decryptedValue.equals(tempMessage));
        System.out.println(decryptedValue.equals(tempMessage));
    }

    @Test
    void checkDecrypted() {
        //given
        String tempMessage = "test";
        String tempPasword = "password";
        String tempEncyrpted = "4304605e7D025bc4eD2855437Fc2e578ILLtgQSFwCBgf0Z2VEK0xQA4b145AFbD81be7F17A3300F98FAA9ec";
        String result;

        System.out.println(tempPasword);
        System.out.println(tempEncyrpted);

        //when
        Crypt.init();
        result = Crypt.decrypt(tempPasword, tempEncyrpted);
        System.out.println(result);

        //then
        Assertions.assertTrue(tempMessage.equals(result));
        System.out.println(tempMessage.equals(result));
    }
}