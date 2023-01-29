package com.wowmate.server.test;

import com.wowmate.server.user.domain.Gender;
import org.junit.jupiter.api.Test;

import static com.wowmate.server.user.domain.Gender.M;

public class test {

    @Test
    void contextTest(){

        String phoneNumber = "01034333292";

        phoneNumber = phone_format(phoneNumber);
        System.out.println(phoneNumber);
    }

    public String phone_format(String number) {
        String regEx = "(\\d{3})(\\d{4})(\\d{4})";
        return number.replaceAll(regEx, "$1-$2-$3");
    }


}
