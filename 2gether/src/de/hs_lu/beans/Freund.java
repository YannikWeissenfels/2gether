package de.hs_lu.beans;

public class Freund {
	
	String vorname;
	String nachname;
	int freund_id;
	
	public Freund() {
		
	}
	
	public Freund(int freund_id, String vorname, String nachname) {
		this.vorname = vorname;
		this.nachname = nachname;
		this.freund_id = freund_id;
	}
	
	public Freund(int freund_id) {
		this.freund_id = freund_id;
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

	public int getFreund_id() {
		return freund_id;
	}

	public void setFreund_id(int freund_id) {
		this.freund_id = freund_id;
	}
	
	

}
