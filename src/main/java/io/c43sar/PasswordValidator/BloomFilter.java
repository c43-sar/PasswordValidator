package io.c43sar.PasswordValidator;

import java.util.*;

public class BloomFilter {
    private ArrayList<Boolean> bloomArray;
    private boolean verboseMessages;
    BloomFilter() {
        bloomArray = new ArrayList<Boolean>();
        verboseMessages = false;
    }
    BloomFilter(boolean[] initialBloomFilter) {
        bloomArray = new ArrayList<Boolean>();
        for (int i = 0; i < initialBloomFilter.length; i++) {
            bloomArray.add(Boolean.valueOf(initialBloomFilter[i]));
        }
        verboseMessages = false;
    }

    BloomFilter(String initialBloomFilter) {
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

    public void enableVerboseMessages() {
        verboseMessages = true;
    }

    public void disableVerboseMessages() {
        verboseMessages = false;
    }

    public void setVerboseMessages(boolean b) {
        verboseMessages = b;
    }

    public static void main(String[] args) {
        // Test Statements
        BloomFilter obj = new BloomFilter(new boolean[] {true, false, true});
    }
}
