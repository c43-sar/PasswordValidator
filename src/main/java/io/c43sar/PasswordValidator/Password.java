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

//    private boolean[] decToBin(int n) {
//        ArrayList<Boolean> binArray = new ArrayList<Boolean>();
//        for (int i = 0; i < 32; i++) {
//            long k = n >> i;
//            if ((k & 1) > 0) { binArray.add(true); }
//            else { binArray.add(false); }
//        }
//        Object[] tempArray = binArray.toArray();
//        boolean[] resArray = new boolean[tempArray.length];
//        IntStream.range(0, tempArray.length).parallel().forEach(i -> {
//            resArray[i] = ((Boolean) tempArray[i]).booleanValue();
//        });
//        return resArray;
//    }
//
//    private int binToDec(boolean[] binArray) {
//        AtomicInteger dec = new AtomicInteger(0);
//        IntStream.range(0, binArray.length).parallel().forEach(i -> {
//            dec.addAndGet(((int) (Boolean.valueOf(binArray[i])).compareTo(true)) * (int)(Math.pow(2d, (double) i)));
//        });
//        return  dec.get();
//    }
//
//    public void calcHashOne() {
//        final AtomicIntegerArray asciiArray = new AtomicIntegerArray(passwordString.length());
//        IntStream.range(0, passwordString.length()).parallel().forEach(i -> {
//            asciiArray.set(i, ((int) passwordString.charAt(i)));
//        });
//        final AtomicInteger asciiSum = new AtomicInteger(0);
//        IntStream.range(0, passwordString.length()).parallel().forEach(i -> {
//            asciiSum.addAndGet(asciiArray.get(i));
//        });
//        hashStore.put(1, asciiSum.get());
//        if (verboseMessages) {
//            System.out.println("DBG: calcHashOne:\t" + hashStore.get(1));
//        }
//    }
//
//    public void calcHashTwo() {
//        calcHashOne();
//        boolean[] W = decToBin(Integer.valueOf(hashStore.get(1)));
//        boolean[] resArray = new boolean[W.length];
//        IntStream.range(0, W.length).parallel().forEach(i -> {
////            resArray[i] = ((Boolean) W[i]).booleanValue();
//            if (i == 0) {
//                resArray[i] = false ^ (false && W[i]) ^ W[i + 1];
//            } else if (i == (W.length - 1)) {
//                resArray[i] = W[i - 1] ^ (false && W[i]) ^ false;
//            } else {
//                resArray[i] = W[i - 1] ^ (false && W[i]) ^ W[i + 1];
//            }
//        });
//        hashStore.put(2,binToDec(resArray));
//        if (verboseMessages) {
//            System.out.println("DBG: calcHashTwo:\t" + hashStore.get(2));
//        }
//    }
//
//    public void calcHashThree() {
//        calcHashOne();
//        boolean[] W = decToBin(Integer.valueOf(hashStore.get(1)));
//        boolean[] resArray = new boolean[W.length];
//        IntStream.range(0, W.length).parallel().forEach(i -> {
////            resArray[i] = ((Boolean) W[i]).booleanValue();
//            if (i == 0) {
//                resArray[i] = false ^ (true && W[i]) ^ W[i + 1];
//            } else if (i == (W.length - 1)) {
//                resArray[i] = W[i - 1] ^ (true && W[i]) ^ false;
//            } else {
//                resArray[i] = W[i - 1] ^ (true && W[i]) ^ W[i + 1];
//            }
//        });
//        hashStore.put(3,binToDec(resArray));
//        if (verboseMessages) {
//            System.out.println("DBG: calcHashThree:\t" + hashStore.get(3));
//        }
//    }

    public void setPasswordString(String passwordString) {
        this.passwordString = passwordString;
    }

    StringBuffer getPasswordString() {
        return new StringBuffer(passwordString);
    }

    public Map<Integer, Integer> getHashStore() {
        return hashStore;
    }

    public void updateHashStore(Map<Integer, Integer> hashStore) { this.hashStore = hashStore; }

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
