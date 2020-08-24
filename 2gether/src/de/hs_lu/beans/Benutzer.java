package de.hs_lu.beans;

public class Benutzer {
	
	String vorname;
	String nachname;
	int benutzeridVonArrayList;
	String grund;
	
	public Benutzer() {
	}
	
	public Benutzer(int benutzeridVonArrayList) {
	this.benutzeridVonArrayList = benutzeridVonArrayList;
	
	}
	
	public Benutzer(int benutzeridVonArrayList, String vorname, String nachname) {
		this.vorname = vorname;
		this.nachname = nachname;
		this.benutzeridVonArrayList = benutzeridVonArrayList;
	}
	
	public Benutzer(int benutzeridVonArrayList, String vorname, String nachname, String grund) {
		this.vorname = vorname;
		this.nachname = nachname;
		this.benutzeridVonArrayList = benutzeridVonArrayList;
		this.grund = grund;
	}
	
	
	
	public String getGrund() {
		return grund;
	}

	public void setGrund(String grund) {
		this.grund = grund;
	}

	public String getVorname() {
		return vorname;
	}
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}
	public String getNachname() {
		return nachname;
	}
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public int getBenutzeridVonArrayList() {
		return benutzeridVonArrayList;
	}

	public void setBenutzeridVonArrayList(int benutzeridVonArrayList) {
		this.benutzeridVonArrayList = benutzeridVonArrayList;
	}

	
	
	

}
