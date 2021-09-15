package com.tas.dataModel;

import java.io.Serializable;

public class Album  implements Serializable{
	private String albumTitle;
	private String albumTitleSort;
	private String albumArtist;
	private String year;
	private String tracksTotal;
	private byte[] albumArtworkBytes;
	private static final long serialVersionUID = 1L;  // for Serialization
	
	public Album(String albumTitle, String albumArtist, String year) {
		this.albumTitle = albumTitle;
		this.albumArtist = albumArtist;
		this.year = year;
	}
	
	public Album() {
	}

	public String getAlbumTitle() {
		return albumTitle;
	}
	public void setAlbumTitle(String albumTitle) {
		this.albumTitle = albumTitle;
	}
	public String getAlbumTitleSort() {
		return albumTitleSort;
	}
	public void setAlbumTitleSort(String albumTitleSort) {
		this.albumTitleSort = albumTitleSort;
	}
	public String getAlbumArtist() {
		return albumArtist;
	}
	public void setAlbumArtist(String albumArtist) {
		this.albumArtist = albumArtist;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getTracksTotal() {
		return tracksTotal;
	}
	public void setTracksTotal(String tracksTotal) {
		this.tracksTotal = tracksTotal;
	}
	public byte[] getAlbumArtworkBytes() {
		return albumArtworkBytes;
	}
	public void setAlbumArtworkBytes(byte[] albumArtworkBytes) {
		this.albumArtworkBytes = albumArtworkBytes;
	}

	public void updateAlbum(String albumTitle, String albumArtist, String year) {
		this.albumTitle = albumTitle;
		this.albumArtist = albumArtist;
		this.year = year;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((albumArtist == null) ? 0 : albumArtist.hashCode());
		result = prime * result + ((albumTitle == null) ? 0 : albumTitle.hashCode());
		result = prime * result + ((year == null) ? 0 : year.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Album other = (Album) obj;
		if (albumArtist == null) {
			if (other.albumArtist != null)
				return false;
		} else if (!albumArtist.equals(other.albumArtist))
			return false;
		if (albumTitle == null) {
			if (other.albumTitle != null)
				return false;
		} else if (!albumTitle.equals(other.albumTitle))
			return false;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Album Title: " + albumTitle + "\n" +
				"Album Artist: " + albumArtist + "\n" +
				"Year: " + year;
	}
	
}
