
package shop.local.domain.exceptions;

import shop.local.valueobjects.Artikel;

public class ArtikelBereitsVorhandenException extends Exception {

	private Artikel artikel; 
	
	/**
	 * Exception darauf, dass ein Artikel bereits gelistet ist.
	 * 
	 * @author Error404
	 */

	public ArtikelBereitsVorhandenException(Artikel artikel) {
		super("Der Artikel " + artikel.getName() + " existiert bereit im Warenkorb!");
		this.artikel = artikel;
	}

	public Artikel getArtikel() {
		return artikel;
	}
}
