package de.hs_lu.beans;

public class Nachricht {
	
	int nachricht_id;
	String nachrichtentext;
	int sender_id;
	int empfaenger_id;
	String sender_vorname;
	String sender_nachname;
	
	
	public Nachricht() {
	}
	
	
	public Nachricht(int nachricht_id, String nachrichtentext, int sender_id, int empfaenger_id) {
	
	this.nachricht_id = nachricht_id;
	this.nachrichtentext = nachrichtentext;
	this.sender_id = sender_id;
	this.empfaenger_id = empfaenger_id;
	
	}
	
	public Nachricht(int nachricht_id, String nachrichtentext, int sender_id) {
	
	this.nachricht_id = nachricht_id;
	this.nachrichtentext = nachrichtentext;
	this.sender_id = sender_id;
	
	}
	
	public Nachricht(int nachricht_id) {
		
		this.nachricht_id = nachricht_id;
	
		}
	

	public Nachricht(int nachricht_id, int sender_id, String vorname, String nachname) {
		
		this.nachricht_id = nachricht_id;
		this.sender_id = sender_id;
		this.sender_vorname = vorname;
		this.sender_nachname = nachname;
		}


	
	public int getNachricht_id() {
		return nachricht_id;
	}


	public void setNachricht_id(int nachricht_id) {
		this.nachricht_id = nachricht_id;
	}


	public String getNachrichtentext() {
		return nachrichtentext;
	}


	public void setNachrichtentext(String nachrichtentext) {
		this.nachrichtentext = nachrichtentext;
	}


	public int getSender_id() {
		return sender_id;
	}


	public void setSender_id(int sender_id) {
		this.sender_id = sender_id;
	}


	public int getEmpfaenger_id() {
		return empfaenger_id;
	}


	public void setEmpfaenger_id(int empfaenger_id) {
		this.empfaenger_id = empfaenger_id;
	}


	public String getSender_vorname() {
		return sender_vorname;
	}


	public void setSender_vorname(String sender_vorname) {
		this.sender_vorname = sender_vorname;
	}


	public String getSender_nachname() {
		return sender_nachname;
	}


	public void setSender_nachname(String sender_nachname) {
		this.sender_nachname = sender_nachname;
	}


	
	

}
