
package shop.local.domain.exceptions;

import shop.local.valueobjects.Artikel;

public class NichtGenugArtikelException extends Exception {

	private Artikel artikel; 
	
	/**
	 * Exception darauf, dass ein Artikel nicht genug vorhanden ist.
	 * 
	 * @author Error404
	 */

	public NichtGenugArtikelException(Artikel artikel) {
		super("Der Artikel " + artikel.getName() + " ist leider nicht mehr ausreichend genug vorhanden.");
		this.artikel = artikel;
	}

	public Artikel getArtikel() {
		return artikel;
	}
}
