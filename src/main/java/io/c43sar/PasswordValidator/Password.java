package io.c43sar.PasswordValidator;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.stream.IntStream;

public final class Password {
    private String passwordString;

    private boolean verboseMessages;

    private Map<Integer, Integer> hashStore = new HashMap<Integer,Integer>();

    public Password() {
        verboseMessages = false;
    }

    public Password(String passwordString) {
        setPasswordString(passwordString);
    }

    public Password(char[] charArray) {
        setPasswordString(String.valueOf(charArray));
    }

    private boolean[] decToBin(long n) {
        ArrayList<Boolean> binArray = new ArrayList<Boolean>();
        for (int i = 0; i < 63; i++) {
            long k = n >> i;
            if ((k & 1) > 0) { binArray.add(true); }
            else { binArray.add(false); }
        }
        Object[] tempArray = binArray.toArray();
        boolean[] resArray = new boolean[tempArray.length];
        IntStream.range(0, tempArray.length).parallel().forEach(i -> {
            resArray[i] = ((Boolean) tempArray[i]).booleanValue();
        });
        return resArray;
    }

    private boolean[] decToBin(int n) {
        return decToBin((long) n);
    }

    public void calcHashOne() {
        final AtomicIntegerArray asciiArray = new AtomicIntegerArray(passwordString.length());
        IntStream.range(0, passwordString.length()).parallel().forEach(i -> {
            asciiArray.set(i, ((int) passwordString.charAt(i)));
        });
        final AtomicInteger asciiSum = new AtomicInteger(0);
        IntStream.range(0, passwordString.length()).parallel().forEach(i -> {
            asciiSum.addAndGet(asciiArray.get(i));
        });
        if (verboseMessages) {
            System.out.println("DBG: calcHashOne:\t" + asciiSum.get());
        }
        hashStore.put(1, asciiSum.get());
        if (verboseMessages) {
            System.out.println("DBG: calcHashOne:\t" + hashStore.get(1));
        }
    }

    public void setPasswordString(String passwordString) {
        this.passwordString = passwordString;
    }

    public boolean verboseMessagesStatus() {
        return verboseMessages;
    }

    public void setVerboseMessages(boolean verboseMessages) {
        this.verboseMessages = verboseMessages;
    }
}
