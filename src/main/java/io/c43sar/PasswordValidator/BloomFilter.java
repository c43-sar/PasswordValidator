package io.c43sar.PasswordValidator;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
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
