package com.tas.dataModel;

public class Labels {
	private String labelType;
	private static final String number = "1, 2, 3, 4...";
	private static final String upperAlpha = "A, B, C, D...";
	private static final String lowerAlpha = "a, b, c, d...";
	private static final String upperRoman = "I, II, III, IV...";
	private static final String lowerRoman = "i, ii, iii, iv...";
	
	public Labels() {
	}
	public Labels(String labelType) {
		this.labelType = labelType;
	}
	
	public String getLabelType() {
		return labelType;
	}
	public void setLabelType(String labelType) {
		this.labelType = labelType;
	}
	
	public String getLabel(int index) {
		if(labelType == number) {
			return Integer.toString(index);
		} else if(labelType == upperAlpha) {
			return Alphabet.getLabel(Alphabet.Case.Upper, index);
		} else if(labelType == lowerAlpha) {
			return Alphabet.getLabel(Alphabet.Case.Lower, index);
		} else if(labelType == upperRoman) {
			return RomanNumeral.toRoman(RomanNumeral.Case.Upper, index);
		} else if(labelType == lowerRoman) {
			return RomanNumeral.toRoman(RomanNumeral.Case.Lower, index);
		} else {
			return "";
		}
	}
	
}