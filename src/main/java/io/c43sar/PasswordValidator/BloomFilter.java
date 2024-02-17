package io.c43sar.PasswordValidator;

import java.util.*;

public class BloomFilter {
    private ArrayList<Boolean> bloomArray;
    BloomFilter() {
        bloomArray = new ArrayList<Boolean>();
    }
    BloomFilter(boolean[] initialBloomFilter) {
        bloomArray = new ArrayList<Boolean>();
        for (int i = 0; i < initialBloomFilter.length; i++) {
            bloomArray.add(Boolean.valueOf(initialBloomFilter[i]));
        }
    }

    BloomFilter(String initialBloomFilter) {
        bloomArray = new ArrayList<Boolean>();
        for (char ch: initialBloomFilter.toCharArray()) {
            if (ch == '0') { bloomArray.add(Boolean.FALSE); continue; }
            if (ch == '1') { bloomArray.add(Boolean.TRUE); continue; }
//            throw new Exception("Unidentified Character "+String.valueOf(ch)+" while handling String");
        }
        Collections.reverse(bloomArray);
    }

    public static void main(String[] args) {
        // Test Statements
        BloomFilter obj = new BloomFilter(new boolean[] {true, false, true});
    }
}
