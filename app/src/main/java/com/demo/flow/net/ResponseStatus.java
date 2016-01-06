package com.demo.flow.net;


public interface ResponseStatus {

    int PHONE_ALREADY_REGISTER = 1001;
    int PHONE_FORMAT_NOT_CORRECT = 1002;
    int PASSWORD_FORMAT_NOT_CORRECT = 2001;
    int VERIFICATION_CODE_NOT_MATCH = 3001;
    int USER_NOT_EXIST = 4001;
}
