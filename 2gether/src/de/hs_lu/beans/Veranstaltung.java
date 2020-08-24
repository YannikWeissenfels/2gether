package de.hs_lu.beans;

public class Veranstaltung {

	String veranstaltung_name;
	String beschreibung;
	int veranstaltung_id;
	int ersteller_id;
	String ort, datum, beginn, ende;
	
	String interesse;
	String grund;

	public Veranstaltung(int veranstaltung_id, String veranstaltung_name, String beschreibung, int ersteller_id, String ort, String datum, String beginn, String ende) {
		this.veranstaltung_name = veranstaltung_name;
		this.beschreibung = beschreibung;
		this.veranstaltung_id = veranstaltung_id;
		this.ersteller_id = ersteller_id;
		this.ort = ort;
		this.datum = datum;
		this.beginn = beginn;
		this.ende = ende;
	}
	
	
	public Veranstaltung(int veranstaltung_id, String veranstaltung_name, String grund) {
		this.veranstaltung_name = veranstaltung_name;
		this.veranstaltung_id = veranstaltung_id;
		this.grund = grund;
	
	}
	
	public Veranstaltung(int veranstaltung_id, String veranstaltung_name, String interesse, String grund) {
		this.veranstaltung_id = veranstaltung_id;
		this.veranstaltung_name = veranstaltung_name;
		this.interesse = interesse;
		this.grund = grund;
		
	}

	public String getVeranstaltung_name() {
		return veranstaltung_name;
	}

	public void setVeranstaltung_name(String veranstaltung_name) {
		this.veranstaltung_name = veranstaltung_name;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public int getVeranstaltung_id() {
		return veranstaltung_id;
	}

	public void setVeranstaltung_id(int veranstaltung_id) {
		this.veranstaltung_id = veranstaltung_id;
	}

	public int getErsteller_id() {
		return ersteller_id;
	}

	public void setErsteller_id(int ersteller_id) {
		this.ersteller_id = ersteller_id;
	}

	public String getOrt() {
		return ort;
	}

	public void setOrt(String ort) {
		this.ort = ort;
	}


	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

	public String getBeginn() {
		return beginn;
	}

	public void setBeginn(String beginn) {
		this.beginn = beginn;
	}

	public String getEnde() {
		return ende;
	}

	public void setEnde(String ende) {
		this.ende = ende;
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