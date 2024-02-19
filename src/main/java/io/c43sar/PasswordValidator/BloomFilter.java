package io.c43sar.PasswordValidator;

import java.util.*;
import java.util.stream.IntStream;

/**
 * @author Soumyajit Kolay
 * @version 0.1
 */
public final class BloomFilter {
    private ArrayList<Boolean> bloomArray;
    private boolean verboseMessages;
    BloomFilter() {
        bloomArray = new ArrayList<Boolean>();
        verboseMessages = false;
    }
    public BloomFilter(boolean[] initialBloomFilter) {
        bloomArray = new ArrayList<Boolean>();
        for (int i = 0; i < initialBloomFilter.length; i++) {
            bloomArray.add(Boolean.valueOf(initialBloomFilter[i]));
        }
        verboseMessages = false;
    }

    public BloomFilter(String initialBloomFilter) {
        bloomArray = new ArrayList<Boolean>();
        for (char ch: initialBloomFilter.toCharArray()) {
            if (ch == '0') { bloomArray.add(Boolean.FALSE); continue; }
            if (ch == '1') { bloomArray.add(Boolean.TRUE); continue; }
            if (verboseMessages) {
                System.out.println("WARNING: Unexpected Character "+String.valueOf(ch)+" while handling String.");
                System.out.println("WARNING: Element ignored.");
            }
//            throw new Exception("Unidentified Character "+String.valueOf(ch)+" while handling String");
        }
        Collections.reverse(bloomArray);
        verboseMessages = false;
    }

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
//    }
}
