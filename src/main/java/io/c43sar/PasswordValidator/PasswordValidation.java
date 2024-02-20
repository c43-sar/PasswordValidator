package io.c43sar.PasswordValidator;


import java.util.regex.*;
public class PasswordValidation {
    private String regex;
    private BloomFilter bloomObj;

    public PasswordValidation(String regexString) {
        setBloomObj();
        setRegex(regexString);
    }

    public PasswordValidation() {
        setBloomObj();
        setRegex("");
    }

    public PasswordValidation(BloomFilter bloomInput) {
        setBloomObj(bloomInput);
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public BloomFilter getBloomObj() {
        return bloomObj;
    }

    public boolean validate(Password pwd) {
        return Pattern.matches(getRegex(), pwd.getPasswordString().toString());
    }

    public void setBloomObj() {
        setBloomObj(new BloomFilter("00000000"));
    }

    public void setBloomObj(BloomFilter bloomObj) {
        this.bloomObj = bloomObj;
    }
}
