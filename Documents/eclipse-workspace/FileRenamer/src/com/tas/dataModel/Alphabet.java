package com.tas.dataModel;

import java.util.TreeMap;

public class Alphabet {
	private final static TreeMap<Integer, String> map = new TreeMap<Integer, String>();

    static {
        map.put(1, "A");
        map.put(2, "B");
        map.put(3, "C");
        map.put(4, "D");
        map.put(5, "E");
        map.put(6, "F");
        map.put(7, "G");
        map.put(8, "H");
        map.put(9, "I");
        map.put(10, "J");
        map.put(11, "K");
        map.put(12, "L");
        map.put(13, "M");
        map.put(14, "N");
        map.put(15, "O");
        map.put(16, "P");
        map.put(17, "Q");
        map.put(18, "R");
        map.put(19, "S");
        map.put(20, "T");
        map.put(21, "U");
        map.put(22, "V");
        map.put(23, "W");
        map.put(24, "X");
        map.put(25, "Y");
        map.put(26, "Z");
    }
    
    public static enum Case {
    	Upper,
    	Lower
    }
    
    public static String getLetter(Case CASE, int index) {
    	int mod = index % 26;
    	int num = (mod == 0) ? 26 : mod;
    	
    	if(CASE == Case.Lower) {
    		return map.get(num).toLowerCase(); 
    	} else {
    		return map.get(num);
    	}
    }
    
    public static String getLabel(Case CASE, int index) {
    	if(index <= 26) {
    		return getLetter(CASE, index);
    	} else {
    		int mod = index % 26;
        	int num = (mod == 0) ? 26 : mod;
        	int mult = (index - 1) / 26;
    		return getLabel(CASE, mult) + getLetter(CASE, num);
    	}
    }

}
