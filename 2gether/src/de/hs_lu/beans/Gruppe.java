package de.hs_lu.beans;

public class Gruppe {
	
	String gruppe_name;
	String beschreibung;
	int gruppe_id;
	int ersteller_id;
	
	String interesse;
	String grund;
	
	public Gruppe(int gruppe_id, String gruppe_name, String beschreibung, int ersteller_id) {
		this.gruppe_name = gruppe_name;
		this.beschreibung = beschreibung;
		this.gruppe_id = gruppe_id;
		this.ersteller_id = ersteller_id;
	}
	
	public Gruppe(int gruppe_id, String gruppe_name, String interesse, String grund) {
		this.gruppe_id = gruppe_id;
		this.gruppe_name = gruppe_name;
		this.interesse = interesse;
		this.grund = grund;
		
	}

	public String getGruppe_name() {
		return gruppe_name;
	}

	public void setGruppe_name(String gruppe_name) {
		this.gruppe_name = gruppe_name;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public int getGruppe_id() {
		return gruppe_id;
	}

	public void setGruppe_id(int gruppe_id) {
		this.gruppe_id = gruppe_id;
	}

	public int getErsteller_id() {
		return ersteller_id;
	}

	public void setErsteller_id(int ersteller_id) {
		this.ersteller_id = ersteller_id;
	}

	public String getInteresse() {
		return interesse;
	}

	public void setInteresse(String interesse) {
		this.interesse = interesse;
	}

	public String getGrund() {
		return grund;
	}

	public void setGrund(String grund) {
		this.grund = grund;
	}
	
	
	
}