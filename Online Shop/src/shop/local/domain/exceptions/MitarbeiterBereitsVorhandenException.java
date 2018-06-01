
package shop.local.domain.exceptions;

import shop.local.valueobjects.Mitarbeiter;

public class MitarbeiterBereitsVorhandenException extends Exception {

	private Mitarbeiter mitarbeiter; 
	
	/**
	 * Exception darauf, dass ein Mitarbeiter bereits gelistet ist.
	 * 
	 * @author Error404
	 */

	public MitarbeiterBereitsVorhandenException(Mitarbeiter mitarbeiter) {
		super("Ein Mitarbeiter mit dem Benutzernamen " + mitarbeiter.getNutzerName() + " existiert bereits!");
		this.mitarbeiter = mitarbeiter;
	}

	public Mitarbeiter getMitarbeiter() {
		return mitarbeiter;
	}
}
