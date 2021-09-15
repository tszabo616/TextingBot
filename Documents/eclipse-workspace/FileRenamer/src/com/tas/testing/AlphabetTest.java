package com.tas.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.tas.dataModel.Alphabet;

public class AlphabetTest {

	@Test
	public void GetLetter4Lower() {
		String letter = Alphabet.getLetter(Alphabet.Case.Lower, 4);
		assertEquals("d", letter);
	}
	
	@Test
	public void GetLetter4Upper() {
		String letter = Alphabet.getLetter(Alphabet.Case.Upper, 4);
		assertEquals("D", letter);
	}
	
	@Test
	public void GetLetter27Lower() {
		String letter = Alphabet.getLetter(Alphabet.Case.Lower, 27);
		assertEquals("a", letter);
	}
	
	@Test
	public void GetLetter27Upper() {
		String letter = Alphabet.getLetter(Alphabet.Case.Upper, 27);
		assertEquals("A", letter);
	}
	
	@Test
	public void GetLabel4Lower() {
		String label = Alphabet.getLabel(Alphabet.Case.Lower, 4);
		assertEquals("d", label);
	}
	
	@Test
	public void GetLabel4Upper() {
		String label = Alphabet.getLabel(Alphabet.Case.Upper, 4);
		assertEquals("D", label);
	}
	
	@Test
	public void GetLabel27Lower() {
		String label = Alphabet.getLabel(Alphabet.Case.Lower, 27);
		assertEquals("aa", label);
	}
	
	@Test
	public void GetLabel27Upper() {
		String label = Alphabet.getLabel(Alphabet.Case.Upper, 27);
		assertEquals("AA", label);
	}
	
	@Test
	public void GetLabel30Lower() {
		String label = Alphabet.getLabel(Alphabet.Case.Lower, 30);
		assertEquals("ad", label);
	}
	
	@Test
	public void GetLabel30Upper() {
		String label = Alphabet.getLabel(Alphabet.Case.Upper, 30);
		assertEquals("AD", label);
	}
}
