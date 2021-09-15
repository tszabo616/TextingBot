package com.tas.dataModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Renamer {
	private String fileName;
	private String prefixToRemove;
	private String suffixToRemove;
	private String prefixToAdd;
	private String suffixToAdd;
	
	public Renamer(String fileName, String prefixToRemove, String suffixToRemove, String prefixToAdd,
			String suffixToAdd) {
		super();
		this.fileName = fileName;
		this.prefixToRemove = prefixToRemove;
		this.suffixToRemove = suffixToRemove;
		this.prefixToAdd = prefixToAdd;
		this.suffixToAdd = suffixToAdd;
	}
	public Renamer(String fileName) {
		this.fileName = fileName;
	}
	public Renamer() {
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getPrefixToRemove() {
		return prefixToRemove;
	}
	public void setPrefixToRemove(String prefixToRemove) {
		this.prefixToRemove = prefixToRemove;
	}
	public String getSuffixToRemove() {
		return suffixToRemove;
	}
	public void setSuffixToRemove(String suffixToRemove) {
		this.suffixToRemove = suffixToRemove;
	}
	public String getPrefixToAdd() {
		return prefixToAdd;
	}
	public void setPrefixToAdd(String prefixToAdd) {
		this.prefixToAdd = prefixToAdd;
	}
	public String getSuffixToAdd() {
		return suffixToAdd;
	}
	public void setSuffixToAdd(String suffixToAdd) {
		this.suffixToAdd = suffixToAdd;
	}
	
	public String newFileName() {
		String prefix = updateStringToRemove(prefixToRemove);
		String suffix = updateStringToRemove(suffixToRemove);
		
		String REGEX = ".*[.].*";
		Pattern pattern = Pattern.compile(REGEX);
		Matcher matcher = pattern.matcher(fileName);
		
		if(matcher.matches()) {  // Is a file
			REGEX = "(^.*?" + prefix + ")(.*)(" + suffix + ".*)([.].+$)";  // (prefix)(body)(suffix)(file type)
		} else {  // Is a folder
			REGEX = "(^.*?" + prefix + ")(.*)(" + suffix + ".*)()";  // (prefix)(body)(suffix)(file type)
		}
		
		pattern = Pattern.compile(REGEX);
		matcher = pattern.matcher(fileName);
		
		if(matcher.matches()) {
			String baseFileName = matcher.group(2);
			String fileType = matcher.group(4);
			return prefixToAdd + baseFileName + suffixToAdd + fileType;
		} else {
			return fileName;
		}
	}
	
	private String updateStringToRemove(String prefixToRemove) {
		// Updates a string that contains special regex characters so that string can be used as a regular expression
		StringBuilder result = new StringBuilder("");
		
		for(int i = 0; i < prefixToRemove.length(); i++){
			if(prefixToRemove.substring(i, i+1).equals(")")) {
				result.append("[");
				result.append(prefixToRemove.substring(i, i+1));
				result.append("]");
			} else if(prefixToRemove.substring(i, i+1).equals("'")) {
				result.append("[");
				result.append(prefixToRemove.substring(i, i+1));
				result.append("]");
			} else if(prefixToRemove.substring(i, i+1).equals("\"")) {
				result.append("[");
				result.append(prefixToRemove.substring(i, i+1));
				result.append("]");
			} else if(prefixToRemove.substring(i, i+1).equals("\\")) {
				result.append("[");
				result.append(prefixToRemove.substring(i, i+1));
				result.append("]");
			} else {
				result.append(prefixToRemove.substring(i, i+1));
			}
		}
		return result.toString();
	}
}
