
package shop.local.domain.exceptions;


public class ArtikelNichtVorhandenException extends Exception {


	/**
	 * Exception darauf, dass ein Artikel nicht vorhanden ist.
	 * 
	 * @author Error404
	 */

	public ArtikelNichtVorhandenException() {
		super("Dieser Artikel ist nicht gelistet.");
	}

}
