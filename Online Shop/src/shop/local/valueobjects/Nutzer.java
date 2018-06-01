package shop.local.valueobjects;

/**
 * 
 * Abstrakte Klasse zur Repräsentation aller Nutzer
 * 
 * @author Error404
 *
 */

public abstract class Nutzer {

	
	private String nutzerName;
	private String vorname;
	private String nachname;
	private String passwort;

	public Nutzer(String nutzerName, String vorname, String nachname, String passwort) {
		this.nutzerName = nutzerName;
		this.vorname = vorname;
		this.nachname = nachname;
		this.passwort = passwort;
	}
	
	public Nutzer(String nutzerName, String vorname, String nachname) {
		this.nutzerName = nutzerName;
		this.vorname = vorname;
		this.nachname = nachname;
	}



	public String getNutzerName() {
		return nutzerName;
	}

	public void setNutzerName(String nutzerName) {
		this.nutzerName = nutzerName;
	}
	
	
	public String getVorName() {
		return vorname;
	}

	public void setVorName(String vorname) {
		this.vorname = vorname;
	}
	
	public String getNachName() {
		return nachname;
	}

	public void setNachName(String nachname) {
		this.nachname = nachname;
	}
	
	public String getPasswort() {
	 
		
		// SICHERHEITSVORKEHRUNG
		return passwort;
	}

	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}

	public boolean passwortUeberpruefen (String name, String passwort) {
		if (this.passwort.equals(passwort) && this.nutzerName.equals(name)) {
			return true;
		} else
			return false;
		
}

}
