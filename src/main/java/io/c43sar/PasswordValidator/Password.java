package io.c43sar.PasswordValidator;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
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
        for (int i = 0; i < 64; i++) {
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

    private int binToDec(boolean[] binArray) {
        AtomicInteger dec = new AtomicInteger(0);
        IntStream.range(0, binArray.length).parallel().forEach(i -> {
            dec.addAndGet(((int) (new Boolean(binArray[i])).compareTo(true)) * (int)(Math.pow(2d, (double) i)));
        });
        return  dec.get();
    }

    private long binToDecLong(boolean[] binArray) {
        AtomicLong dec = new AtomicLong(0l);
        IntStream.range(0, binArray.length).parallel().forEach(i -> {
            dec.addAndGet(((long) (new Boolean(binArray[i])).compareTo(true)) * (long) (Math.pow(2d, (double) i)));
        });
        return  dec.get();
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
