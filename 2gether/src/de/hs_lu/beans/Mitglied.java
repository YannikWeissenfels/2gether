package de.hs_lu.beans;

public class Mitglied {
	
	String vorname;
	String nachname;
	int gruppe_id;
	int mitglied_id;
	
	public Mitglied(int mitglied_id, String vorname, String nachname, int gruppe_id) {
		this.vorname = vorname;
		this.nachname = nachname;
		this.gruppe_id = gruppe_id;
		this.mitglied_id = mitglied_id;
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

	public int getGruppe_id() {
		return gruppe_id;
	}

	public void setGruppe_id(int gruppe_id) {
		this.gruppe_id = gruppe_id;
	}

	public int getMitglied_id() {
		return mitglied_id;
	}

	public void setMitglied_id(int mitglied_id) {
		this.mitglied_id = mitglied_id;
	}


}