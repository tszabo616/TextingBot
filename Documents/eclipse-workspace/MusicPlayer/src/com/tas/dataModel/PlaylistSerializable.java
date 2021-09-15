package com.tas.dataModel;

import java.io.Serializable;

public class PlaylistSerializable implements Serializable {
	private String name;
	private static final long serialVersionUID = 1L;  // for Serialization
	
	public PlaylistSerializable() {
	}
	
	public PlaylistSerializable(String name) {
		this();
		this.name = name;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
