package io.c43sar.PasswordValidator;


import java.util.regex.*;
public class PasswordValidation {
    private String regex;
    private BloomFilter bloomObj;

    public final String[] weakPasswordList = new String[]{
            "******",
            "********",
            "**********",
            "*************",
            "000000",
            "101010",
            "10203",
            "102030",
            "10203040",
            "1029384756",
            "1111",
            "111111",
            "1111111",
            "11111111",
            "111111111",
            "1111111111",
            "1122",
            "112233",
            "11223344",
            "1122334455",
            "121212",
            "12121212",
            "123",
            "123123",
            "123123123",
            "123321",
            "1234",
            "12341234",
            "12344321",
            "12345",
            "1234512345",
            "1234554321",
            "123456",
            "1234561",
            "1234567",
            "12345678",
            "123456789",
            "123456789",
            "1234567890",
            "12345678900",
            "12345678901",
            "1234567891",
            "12345678910",
            "1234567899",
            "123456789a",
            "12345678a",
            "12345679",
            "1234567a",
            "123456a",
            "123456a@",
            "123456aA@",
            "12345qwert",
            "1234abcd",
            "1234qwer",
            "123654",
            "123654789",
            "123abc",
            "123meklozed",
            "123mudar",
            "123qwe",
            "123qweasd",
            "12qwaszx",
            "131313",
            "147258",
            "159357",
            "1a2b3c4d",
            "1q2w3e",
            "1q2w3e4r",
            "1q2w3e4r5t",
            "1qaz2wsx",
            "1qazxsw2",
            "202020",
            "222222",
            "2402301978",
            "54321",
            "555555",
            "654321",
            "666666",
            "7777777",
            "868689849",
            "87654321",
            "888888",
            "88888888",
            "987654",
            "987654321",
            "987654321",
            "999999",
            "ADMIN",
            "Aa102030",
            "Aa112233",
            "Aa123456",
            "Aa123456@",
            "Aa195043",
            "Aa@123456",
            "Abc@1234",
            "Abcd1234",
            "Abcd@123",
            "Abcd@1234",
            "Adam2312",
            "Admin",
            "Admin123",
            "Admin@123",
            "Ar123455",
            "Cindylee1",
            "Demo@123",
            "Eliska81",
            "Flores123",
            "Heslo1234",
            "India@123",
            "Kumar@123",
            "Menara",
            "Mm123456",
            "P@$$w0rd",
            "P@55w0rd",
            "P@ssw0rd",
            "Pass@123",
            "Pass@1234",
            "Passw0rd",
            "Password",
            "Password1",
            "Password123",
            "Password@123",
            "Qwerty123",
            "Test@123",
            "UNKNOWN",
            "Welcome1",
            "Welcome@123",
            "a123456",
            "a1234567",
            "a123456789",
            "a1b2c3d4",
            "aa123456",
            "aaaaaa",
            "aaaaaaaa",
            "abc123",
            "abc12345",
            "abc123456",
            "abcd1234",
            "admin",
            "admin1",
            "admin123",
            "admin1234",
            "admin12345",
            "admin123456",
            "admin@123",
            "adminHW",
            "adminadmin",
            "adminisp",
            "administrator",
            "admintelecom",
            "asd123",
            "asdasd",
            "asdf1234",
            "asdfghjkl",
            "azerty",
            "azerty123",
            "azertyuiop",
            "banned",
            "bismillah",
            "changeme",
            "demo",
            "err",
            "guest",
            "gvt12345",
            "iloveyou",
            "jimjim30",
            "master",
            "minecraft",
            "motorola",
            "p@ssw0rd",
            "pakistan",
            "pass",
            "password",
            "password1",
            "password123",
            "q1w2e3r4",
            "q1w2e3r4t5",
            "qwe123",
            "qwer1234",
            "qwerty",
            "qwerty123",
            "qwerty1234",
            "qwerty12345",
            "qwerty123456",
            "qwertyuiop",
            "root",
            "secret",
            "senha123",
            "student",
            "test",
            "test123",
            "theworldinyourhand",
            "ubnt",
            "undefined",
            "user",
            "user1234",
            "vodafone",
            "welcome",
            "zaq12wsx",
            "zxcvbnm"
    };

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

    public boolean validateRegex(Password pwd) {
        return Pattern.matches(getRegex(), pwd.getPasswordString().toString());
    }

    public boolean validateStrength(Password pwd) {
        pwd = bloomObj.calcHashThree(bloomObj.calcHashTwo(bloomObj.calcHashOne(pwd)));
        int[] hashArray = pwd.getHashArray();
        return bloomObj.checkBloomFilter(hashArray, false);
    }

    public void setBloomObj() {
        setBloomObj(new BloomFilter("00000000"));
    }

    public void setBloomObj(BloomFilter bloomObj) {
        this.bloomObj = bloomObj;
    }
}
