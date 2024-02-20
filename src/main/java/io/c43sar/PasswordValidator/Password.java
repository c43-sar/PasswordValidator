package io.c43sar.PasswordValidator;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.stream.IntStream;

/**
 * @author Soumyajit Kolay
 * @version 0.1
 */
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

    private boolean[] decToBin(int n) {
        ArrayList<Boolean> binArray = new ArrayList<Boolean>();
        for (int i = 0; i < 32; i++) {
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
            dec.addAndGet(((int) (Boolean.valueOf(binArray[i])).compareTo(true)) * (int)(Math.pow(2d, (double) i)));
        });
        return  dec.get();
    }

    /**
     * calculates the sum of ASCII values of each character present in the @passwordString and store it in private hashStore.
     */
    public void calcHashOne() {
        final AtomicIntegerArray asciiArray = new AtomicIntegerArray(passwordString.length());
        IntStream.range(0, passwordString.length()).parallel().forEach(i -> {
            asciiArray.set(i, ((int) passwordString.charAt(i)));
        });
        final AtomicInteger asciiSum = new AtomicInteger(0);
        IntStream.range(0, passwordString.length()).parallel().forEach(i -> {
            asciiSum.addAndGet(asciiArray.get(i));
        });
        hashStore.put(1, asciiSum.get());
        if (verboseMessages) {
            System.out.println("DBG: calcHashOne:\t" + hashStore.get(1));
        }
    }

    /**
     * Calculates "Rule 90" hash and store it in private hashStore
     * Rule 90 Hash Function:
     * i. Compute the sum of ASCII values of each character in X. Then, store this sum in variable W.
     * ii. Convert W to binary of n bits (n = 16 or 32 or 64 bits) and store in S, where $S = \{s_0, s_1, ... , s_{14}\}$.
     * iii. Using S, calculate $S^{t+1}$, where each element might be calculated as:
     * ${s_i^{(t+1)} = s_{i-1}^{(t)} \oplus d_i \cdot s_{i-1}^{(t)} \oplus s_{i+1}^{(t)}}$,
     * where ${d_i = 0}$.
     * iv. Convert $S^{t+1}$ to decimal and store in private hashStore
     */
    public void calcHashTwo() {
        calcHashOne();
        boolean[] W = decToBin(Integer.valueOf(hashStore.get(1)));
        boolean[] resArray = new boolean[W.length];
        IntStream.range(0, W.length).parallel().forEach(i -> {
//            resArray[i] = ((Boolean) W[i]).booleanValue();
            if (i == 0) {
                resArray[i] = false ^ (false && W[i]) ^ W[i + 1];
            } else if (i == (W.length - 1)) {
                resArray[i] = W[i - 1] ^ (false && W[i]) ^ false;
            } else {
                resArray[i] = W[i - 1] ^ (false && W[i]) ^ W[i + 1];
            }
        });
        hashStore.put(2,binToDec(resArray));
        if (verboseMessages) {
            System.out.println("DBG: calcHashTwo:\t" + hashStore.get(2));
        }
    }

    /**
     * Calculate "Rule 150" hash and store it in private hashStore
     * Rule 150 Hash Function:
     * i. Compute the sum of ASCII values of each character in X. Then, store this sum in variable W.
     * ii. Convert W to binary of n bits (n = 16 or 32 or 64 bits) and store in S, where $S = \{s_0, s_1, ... , s_{14}\}$.
     * iii. Using S, calculate $S^{t+1}$, where each element might be calculated as:
     * ${s_i^{(t+1)} = s_{i-1}^{(t)} \oplus d_i \cdot s_{i-1}^{(t)} \oplus s_{i+1}^{(t)}}$,
     * where ${d_i = 1}$.
     * iv. Convert $S^{t+1}$ to decimal and store in private hashStore
     */
    public void calcHashThree() {
        calcHashOne();
        boolean[] W = decToBin(Integer.valueOf(hashStore.get(1)));
        boolean[] resArray = new boolean[W.length];
        IntStream.range(0, W.length).parallel().forEach(i -> {
//            resArray[i] = ((Boolean) W[i]).booleanValue();
            if (i == 0) {
                resArray[i] = false ^ (true && W[i]) ^ W[i + 1];
            } else if (i == (W.length - 1)) {
                resArray[i] = W[i - 1] ^ (true && W[i]) ^ false;
            } else {
                resArray[i] = W[i - 1] ^ (true && W[i]) ^ W[i + 1];
            }
        });
        hashStore.put(3,binToDec(resArray));
        if (verboseMessages) {
            System.out.println("DBG: calcHashThree:\t" + hashStore.get(3));
        }
    }

    public void setPasswordString(String passwordString) {
        this.passwordString = passwordString;
    }

    public Map<Integer, Integer> getHashStore() {
        return hashStore;
    }

    public int[] getHashArray() {
        return new int[] {hashStore.get(1), hashStore.get(2), hashStore.get(3)};
    }

    public boolean verboseMessagesStatus() {
        return verboseMessages;
    }

    public void setVerboseMessages(boolean verboseMessages) {
        this.verboseMessages = verboseMessages;
    }
}
