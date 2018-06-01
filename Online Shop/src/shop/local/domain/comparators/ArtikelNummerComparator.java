package shop.local.domain.comparators;

import java.util.Comparator;

import shop.local.valueobjects.Artikel;

// Vergleicht Artikel - Nummern miteinander

public class ArtikelNummerComparator implements Comparator<Artikel> {

	@Override
	public int compare(Artikel artikel1, Artikel artikel2) {
		return artikel1.getNummer() - artikel2.getNummer();
	}

}
