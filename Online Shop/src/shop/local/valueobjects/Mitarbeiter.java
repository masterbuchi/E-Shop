package shop.local.valueobjects;


/**
 * 
 * Klasse zur Repräsentation aller Mitarbeiter, erbt von Nutzer
 * 
 * @author Error404
 *
 */


public class Mitarbeiter extends Nutzer {
	
	
	public Mitarbeiter(String nutzerName, String vorName, String nachName, String passwort) {
		super(nutzerName, vorName, nachName, passwort);
	}
	
	public Mitarbeiter(String nutzerName, String vorName, String nachName) {
		super(nutzerName, vorName, nachName);
	}
	
	
	
	@Override
	public boolean equals(Object anderesObjekt) {
		if (anderesObjekt instanceof Mitarbeiter) {
			Mitarbeiter mitarbeiter = (Mitarbeiter) anderesObjekt;
			return (this.getNutzerName().equals(mitarbeiter.getNutzerName()));

		} else
			return false;
	}
	
				
}

