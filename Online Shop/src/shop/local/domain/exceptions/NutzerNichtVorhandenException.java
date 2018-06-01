package shop.local.domain.exceptions;

public class NutzerNichtVorhandenException extends Exception {


	/**
	 * Exception darauf, dass ein Nutzer nicht vorhanden ist.
	 * 
	 * @author Error404
	 */

	public NutzerNichtVorhandenException() {
		super("Dieser Nutzer wurde gelöscht.");
	}

}