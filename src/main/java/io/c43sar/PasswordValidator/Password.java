package io.c43sar.PasswordValidator;

import java.util.*;
public class Password {
    private String passwordString;

    private Dictionary<Integer, Integer> hashArray;

    Password() {}

    Password(String passwordString) {
        setPasswordString(passwordString);
    }

    Password(char[] charArray) {
        setPasswordString(String.valueOf(charArray));
    }

    public void setPasswordString(String passwordString) {
        this.passwordString = passwordString;
    }
}
