package de.hs_lu.beans;

public class Interesse {
	
	String art;
	String oberbegriff;

		
	
	
	public Interesse() {
	
		
	}
	public Interesse(String oberbegriff) {
		this.oberbegriff = oberbegriff;
		
	}

	
	public Interesse(String art, String oberbegriff) {
		this.art = art;
		this.oberbegriff = oberbegriff;
		
	}


	public String getArt() {
		return art;
	}


	public void setArt(String art) {
		this.art = art;
	}


	public String getOberbegriff() {
		return oberbegriff;
	}


	public void setOberbegriff(String oberbegriff) {
		this.oberbegriff = oberbegriff;
	}

	
}
