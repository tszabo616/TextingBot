package com.tas.testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.tas.dataModel.RomanNumeral;

public class RomanNumeralTest {
	@Test
	public void toRoman3Lower() {
		String roman = RomanNumeral.toRoman(RomanNumeral.Case.Lower, 3);
		assertEquals("iii", roman);
	}
	
	@Test
	public void toRoman4Lower() {
		String roman = RomanNumeral.toRoman(RomanNumeral.Case.Lower, 4);
		assertEquals("iv", roman);
	}
	
	@Test
	public void toRoman5Lower() {
		String roman = RomanNumeral.toRoman(RomanNumeral.Case.Lower, 5);
		assertEquals("v", roman);
	}
	
	@Test
	public void toRoman10Lower() {
		String roman = RomanNumeral.toRoman(RomanNumeral.Case.Lower, 10);
		assertEquals("x", roman);
	}
	
	@Test
	public void toRoman92Lower() {
		String roman = RomanNumeral.toRoman(RomanNumeral.Case.Lower, 92);
		assertEquals("xcii", roman);
	}
	
	@Test
	public void toRoman106Lower() {
		String roman = RomanNumeral.toRoman(RomanNumeral.Case.Lower, 106);
		assertEquals("cvi", roman);
	}
	
	@Test
	public void toRoman1023Lower() {
		String roman = RomanNumeral.toRoman(RomanNumeral.Case.Lower, 1023);
		assertEquals("mxxiii", roman);
	}
	
	@Test
	public void toRoman3Upper() {
		String roman = RomanNumeral.toRoman(RomanNumeral.Case.Upper, 3);
		assertEquals("III", roman);
	}
	
	@Test
	public void toRoman4Upper() {
		String roman = RomanNumeral.toRoman(RomanNumeral.Case.Upper, 4);
		assertEquals("IV", roman);
	}
	
	@Test
	public void toRoman5Upper() {
		String roman = RomanNumeral.toRoman(RomanNumeral.Case.Upper, 5);
		assertEquals("V", roman);
	}
	
	@Test
	public void toRoman10Upper() {
		String roman = RomanNumeral.toRoman(RomanNumeral.Case.Upper, 10);
		assertEquals("X", roman);
	}
	
	@Test
	public void toRoman92Upper() {
		String roman = RomanNumeral.toRoman(RomanNumeral.Case.Upper, 92);
		assertEquals("XCII", roman);
	}
	
	@Test
	public void toRoman106Upper() {
		String roman = RomanNumeral.toRoman(RomanNumeral.Case.Upper, 106);
		assertEquals("CVI", roman);
	}
	
	@Test
	public void toRoman1023Upper() {
		String roman = RomanNumeral.toRoman(RomanNumeral.Case.Upper, 1023);
		assertEquals("MXXIII", roman);
	}
}
