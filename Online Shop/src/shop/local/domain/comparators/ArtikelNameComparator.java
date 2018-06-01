package shop.local.domain.comparators;

import java.util.Comparator;

import shop.local.valueobjects.Artikel;

//Vergleicht Artikel - Namen miteinander

public class ArtikelNameComparator implements Comparator<Artikel> {

	
	@Override
	public int compare(Artikel artikel1, Artikel artikel2) {
		if (artikel1 == null) {
			return -1;
		}
		if (artikel2 == null) {
			return 1;
		}
		if (artikel1.getName().equals( artikel2.getName())) {
			return 0;
		}
		return artikel1.getName().compareTo(artikel2.getName());
	}
}