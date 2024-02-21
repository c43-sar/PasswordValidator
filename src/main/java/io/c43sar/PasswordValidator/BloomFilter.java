package io.c43sar.PasswordValidator;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.stream.IntStream;

/**
 * @author Soumyajit Kolay
 * @version 0.1
 */
public final class BloomFilter {
    /**
     * Serves as the Bloom Filter Array
     */
    private final ArrayList<Boolean> bloomArray = new ArrayList<Boolean>();

    /**
     * If enabled, prints debug-related info.
     */
    private boolean verboseMessages = false;

    /**
     * @param initialBloomFilter an existing Bloom Filter array
     */
    public BloomFilter(boolean[] initialBloomFilter) {
        for (boolean b : initialBloomFilter) {
            bloomArray.add(b);
        }
    }

    /**
     * @param initialBloomFilter an existing String of 0s and 1s to serve as an existing Bloom Filter array
     */
    public BloomFilter(String initialBloomFilter) {
        for (char ch: initialBloomFilter.toCharArray()) {
            if (ch == '0') { bloomArray.add(Boolean.FALSE); continue; }
            if (ch == '1') { bloomArray.add(Boolean.TRUE); continue; }
            if (verboseMessages) {
                System.out.println("WARNING: Unexpected Character "+ ch +" while handling String.");
                System.out.println("WARNING: Element ignored.");
            }
//            throw new Exception("Unidentified Character "+String.valueOf(ch)+" while handling String");
        }
        Collections.reverse(bloomArray);
    }

    /**
     * @param hashArray store the results of hash functions
     */
    public void storeHashes(int[] hashArray) {
        for (int i: hashArray) {
            try {
                bloomArray.get(i);
            } catch (IndexOutOfBoundsException e) {
                while(bloomArray.size() <= i) {
                    bloomArray.add(Boolean.FALSE);
                }
            }
        }
        IntStream.range(0, hashArray.length).parallel().forEach(i -> {
            bloomArray.set(hashArray[i], Boolean.TRUE);
            if (verboseMessages) { System.out.println("DBG: " + hashArray[i] + ":" + bloomArray.get(hashArray[i])); }
        });
    }

    /**
     * @param inputHashArray array of calculated hashes of a string/data
     * @return array stating whether the input hashes are present
     */
    public boolean[] checkHashes(int[] inputHashArray) {
        boolean[] results = new boolean[inputHashArray.length];
        IntStream.range(0, inputHashArray.length).parallel().forEach(i -> {
            results[i] = bloomArray.get(inputHashArray[i]);
        });
        return results;
    }

//    public boolean checkBloomFilterDT(int[] inputHashArray) {
//        AtomicBoolean result = new AtomicBoolean(true);
//        IntStream.range(0, inputHashArray.length).parallel().forEach(i -> {
//
//        });
//        return result.get();
//    }

    /**
     * @param inputHashArray - An array of hashes generated for a string/data whose presence has to be checked in the Bloom Filter.
     * @param defaultValue - if true, turns false if at least one index of Bloom Filter is true and vice versa.
     * @return boolean value that represents the presence of the data whose hashes are provided.
     */
    public boolean checkBloomFilter(int[] inputHashArray, boolean defaultValue) {
        AtomicBoolean result = new AtomicBoolean(false);
        IntStream.range(0, inputHashArray.length).parallel().forEach(i -> {
            if (defaultValue) {
                if (inputHashArray[i] > (bloomArray.size() - 1)) {
                    result.set(false);
                } else {
                    if (bloomArray.get(inputHashArray[i]) == Boolean.FALSE) {
                        result.set(false);
                    }
                }
            } else {
                if (
                        (inputHashArray[i] < (bloomArray.size() - 1))
                                && (bloomArray.get(inputHashArray[i]) == Boolean.TRUE)
                ) { result.set(true); }
            }
        });
        return result.get();
    }

    public boolean checkHash(int inputHash) { return bloomArray.get(inputHash); }

    public ArrayList<Boolean> getBloomFilter() {
        return bloomArray;
    }

    public boolean[] getBloomArray() {
        boolean[] arr = new boolean[bloomArray.size()];
        IntStream.range(0, bloomArray.size()).parallel().forEach(i -> {
            arr[i] = bloomArray.get(i);
        });
        return arr;
    }

    public void enableVerboseMessages() {
        verboseMessages = true;
    }

    public void disableVerboseMessages() {
        verboseMessages = false;
    }

    public void setVerboseMessages(boolean b) {
        verboseMessages = b;
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
    Password calcHashOne(Password pwd) {
        Map<Integer, Integer> hashStore = pwd.getHashStore();
        String passwordString = String.valueOf(pwd.getPasswordString());
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
        pwd.updateHashStore(hashStore);
        return pwd;
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
    Password calcHashTwo(Password pwd) {
        Map<Integer, Integer> hashStore = pwd.getHashStore();
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
        pwd.updateHashStore(hashStore);
        return pwd;
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
    Password calcHashThree(Password pwd) {
        Map<Integer, Integer> hashStore = pwd.getHashStore();
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
        pwd.updateHashStore(hashStore);
        return  pwd;
    }

    public void addEntry(Password pwd) {
        pwd = calcHashThree(calcHashTwo(calcHashOne(pwd)));
        storeHashes(pwd.getHashArray());
    }

//    public static void main(String[] args) {
//        // Test Statements
//        BloomFilter obj = new BloomFilter(new boolean[] {true, false, true});
//        obj.enableVerboseMessages();
//        obj.storeHashes(new int[] {0,76,2,1});
//        boolean[] res = obj.checkBloomFilter(new int[] {0,76,2,1});
//        for (boolean b: res) {
//            System.out.println(b);
//        }
//    }
}
