package shop.local.valueobjects;

import java.util.TreeMap;

import shop.local.domain.comparators.ArtikelNameComparator;
import shop.local.domain.exceptions.ArtikelNichtVorhandenException;

import java.util.Map;

public class Warenkorb {

	Map<Artikel, Integer> inhalt;
	
	public Warenkorb() {
		inhalt = new TreeMap<Artikel, Integer>(new ArtikelNameComparator());
	}

	public void hinzufuegen(Artikel artikel, int anzahl) {
		inhalt.put(artikel, anzahl);
	}

	public void loeschen(Artikel artikel) {
		inhalt.remove(artikel);
	}

	public void WarenkorbLeeren() {
		inhalt.clear();
	}

	public Artikel sucheArtikelName(String artikelName) throws ArtikelNichtVorhandenException {
		if (!inhalt.isEmpty()) {
			for (Map.Entry<Artikel, Integer> entry : inhalt.entrySet()) {
				if (entry.getKey().getName().equals(artikelName)) {
					return entry.getKey();
				}
			}
		}
		throw new ArtikelNichtVorhandenException();
	}

	public int gibAnzahlZurueck(String name) {
		int anzahl = 0;
		if (!inhalt.isEmpty()) {
			for (Map.Entry<Artikel, Integer> entry : inhalt.entrySet()) {
				if (entry.getKey().getName().equals(name)) {
					anzahl = entry.getValue();
				}
			}
		}
		return anzahl;
	}

	public Map<Artikel, Integer> getMap() {
		return inhalt;
	}

	public void setArtikelValueMehr(Artikel artikel, int anzahl) {
		inhalt.put(artikel, inhalt.get(artikel) + anzahl);
	}

	public void setArtikelValueWeniger(Artikel artikel, int anzahl) {
		inhalt.put(artikel, inhalt.get(artikel) - anzahl);
	}
}
