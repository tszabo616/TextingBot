package com.tas.dataModel;

import java.util.TreeMap;

public class RomanNumeral {

    private final static TreeMap<Integer, String> map = new TreeMap<Integer, String>();

    static {
        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");
    }
    
    public static enum Case {
    	Upper,
    	Lower
    }

    public final static String toRoman(Case CASE, int num) {
        int i =  map.floorKey(num);
        if (num == i) {
        	if(CASE == Case.Lower) {
        		return map.get(num).toLowerCase(); 
        	} else {
        		return map.get(num);
        	}
        }
        
        if(CASE == Case.Lower) {
    		return map.get(i).toLowerCase() + toRoman(CASE, num-i); 
    	} else {
    		return map.get(i) + toRoman(CASE, num-i);
    	}
    }

}
