package com.tas.testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.tas.dataModel.Labels;

public class LabelsTest {
	@Test
	public void GetLabelLower4() {
		Labels label = new Labels("a, b, c, d...");
		String labelStr = label.getLabel(4);
		assertEquals("d", labelStr);
	}
	
	@Test
	public void GetLabelLower31() {
		Labels label = new Labels("a, b, c, d...");
		String labelStr = label.getLabel(31);
		assertEquals("ae", labelStr);
	}
	
	@Test
	public void GetLabelLower53() {
		Labels label = new Labels("a, b, c, d...");
		String labelStr = label.getLabel(53);
		assertEquals("ba", labelStr);
	}
	
	@Test
	public void GetLabelUpper4() {
		Labels label = new Labels("A, B, C, D...");
		String labelStr = label.getLabel(4);
		assertEquals("D", labelStr);
	}
	
	@Test
	public void GetLabelUpper31() {
		Labels label = new Labels("A, B, C, D...");
		String labelStr = label.getLabel(31);
		assertEquals("AE", labelStr);
	}
	
	@Test
	public void GetLabelUpper53() {
		Labels label = new Labels("A, B, C, D...");
		String labelStr = label.getLabel(53);
		assertEquals("BA", labelStr);
	}
	
	@Test
	public void GetLabelLowerRoman4() {
		Labels label = new Labels("i, ii, iii, iv...");
		String labelStr = label.getLabel(4);
		assertEquals("iv", labelStr);
	}
	
	@Test
	public void GetLabelLowerRoman107() {
		Labels label = new Labels("i, ii, iii, iv...");
		String labelStr = label.getLabel(107);
		assertEquals("cvii", labelStr);
	}
	
	@Test
	public void GetLabelLowerRoman1078() {
		Labels label = new Labels("i, ii, iii, iv...");
		String labelStr = label.getLabel(1078);
		assertEquals("mlxxviii", labelStr);
	}
	
	@Test
	public void GetLabelUpperRoman4() {
		Labels label = new Labels("I, II, III, IV...");
		String labelStr = label.getLabel(4);
		assertEquals("IV", labelStr);
	}
	
	@Test
	public void GetLabelUpperRoman107() {
		Labels label = new Labels("I, II, III, IV...");
		String labelStr = label.getLabel(107);
		assertEquals("CVII", labelStr);
	}
	
	@Test
	public void GetLabelUpperRoman1078() {
		Labels label = new Labels("I, II, III, IV...");
		String labelStr = label.getLabel(1078);
		assertEquals("MLXXVIII", labelStr);
	}
}
