
package shop.local.domain.exceptions;

import shop.local.valueobjects.Kunde;

public class KundeBereitsVorhandenException extends Exception {

	private Kunde kunde; 
	
	/**
	 * Exception darauf, dass ein Kunde bereits gelistet ist.
	 * 
	 * @author Error404
	 */

	public KundeBereitsVorhandenException(Kunde kunde) {
		super("Ein Kunde mit dem Benutzernamen " + kunde.getNutzerName() + " existiert bereits!");
		this.kunde = kunde;
	}

	public Kunde getKunde() {
		return kunde;
	}
}
