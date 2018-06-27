package shop.local.domain;

import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.IOException;
import java.util.Map;

import shop.local.domain.comparators.ArtikelNameComparator;
import shop.local.domain.exceptions.ArtikelBereitsVorhandenException;
import shop.local.domain.exceptions.ArtikelNichtVorhandenException;
import shop.local.domain.exceptions.NichtGenugArtikelException;
import shop.local.persistence.PersistenceManager;
import shop.local.valueobjects.Artikel;
import shop.local.valueobjects.Nutzer;
import shop.local.valueobjects.Ereignis.EreignisTyp;

public class Logistik {

	// Erzeugung der Lagerbestandliste
	private Map<Artikel, Integer> inhalt;
	private Ereignisverwaltung ereignisverwaltung;

	// Konstruktor
	public Logistik(Ereignisverwaltung ereignisverwaltung) {
		this.ereignisverwaltung = ereignisverwaltung;
		inhalt = new TreeMap<Artikel, Integer>(new ArtikelNameComparator());

	}

	public void liesArtikel(String datei, PersistenceManager pm) throws IOException {
		// PersistenceManager zum Lesen öffnen
		pm.openForReading(datei);
		Artikel artikel;
		do {
			artikel = pm.ladeArtikel();
			if (artikel != null) {
				int anzahl = pm.ladeAnzahl();
				if (Artikel.maxId.get() < artikel.getNummer()) {
					Artikel.maxId.set(artikel.getNummer());
				}
				inhalt.put(artikel, anzahl);
			}
		} while (artikel != null);

		// Schließen
		pm.close();
	}

	public void schreibeArtikel(String datei, PersistenceManager pm) throws IOException {
		// PersistenceManager zum Lesen öffnen
		pm.openForWriting(datei);

		for (Map.Entry<Artikel, Integer> entry : inhalt.entrySet()) {
			pm.speichereArtikel(entry.getKey());
			pm.speichereAnzahl(entry.getValue());
		}

		// Persistenzmanager schließen
		pm.close();
	}

	// Gibt den Lagerbestand aus
	public Map<Artikel, Integer> getLagerBestand() {
		return inhalt;
	}

	public void hinzufuegen(Artikel neuerArtikel, int anzahl, Nutzer nutzer) throws ArtikelBereitsVorhandenException {

		if (!inhalt.isEmpty()) {
			if (inhalt.containsKey(neuerArtikel)) {
				throw new ArtikelBereitsVorhandenException(neuerArtikel);
			}
		}
		inhalt.put(neuerArtikel, anzahl);

		ereignisverwaltung.ereignis(null, EreignisTyp.NEU, neuerArtikel, anzahl, nutzer);
	}

	// Löscht einen Artikel aus der Map
	public boolean loeschen(Artikel artikel, Nutzer nutzer) {
		if (inhalt.containsKey(artikel)) {
			inhalt.remove(artikel);
			ereignisverwaltung.ereignis(null, EreignisTyp.LOESCHUNG, artikel, 0, nutzer);
			return true;
		}
		return false;
	}

	// Legt die Anzahl eines Artikels neu fest indem der vorherige Wert durch die
	// neue Angabe ergänzt wird.
	public void anzahl(Artikel artikel, int anzahlNeu, Nutzer nutzer)
			throws ArtikelNichtVorhandenException, NichtGenugArtikelException {
		if (inhalt.containsKey(artikel)) {
			if (inhalt.get(artikel) >= -(anzahlNeu)) {
				inhalt.put(artikel, (inhalt.get(artikel) + anzahlNeu));
				ereignisverwaltung.ereignis(null, EreignisTyp.EINLAGERUNG, artikel, anzahlNeu, nutzer);
			} else {
				throw new NichtGenugArtikelException(artikel);
			}
		} else {
			throw new ArtikelNichtVorhandenException();
		}
	}

	public Integer gibAnzahlZurueck(Artikel artikel) {
		return inhalt.get(artikel);
	}

	public Artikel sucheArtikelName(String artikelName) throws ArtikelNichtVorhandenException {

		if (inhalt != null) {
			for (Map.Entry<Artikel, Integer> entry : inhalt.entrySet()) {
				if (entry.getKey().getName().equals(artikelName)) {
					return entry.getKey();
				}
			}
		}
		throw new ArtikelNichtVorhandenException();
	}

	public Map<Artikel, Integer> sucheArtikelNamenTeil(String artikelName) throws ArtikelNichtVorhandenException {
		Map<Artikel, Integer> gesuchtes = new TreeMap<Artikel, Integer>(new ArtikelNameComparator());
		if (inhalt != null) {
			for (Map.Entry<Artikel, Integer> entry : inhalt.entrySet()) {
				if (((entry.getKey().getName()).indexOf(artikelName)) != -1) {
					gesuchtes.put(entry.getKey(), entry.getValue());

				}
			}
			return gesuchtes;
		}
		throw new ArtikelNichtVorhandenException();
	}

	public Ereignisverwaltung getEreignisverwaltung() {
		return this.ereignisverwaltung;
	}

	public void setArtikelGelistet(String name, boolean selected) {
		Artikel zwischenSpeicherKey;
		int zwischenSpeicherValue;
		if (inhalt != null) {
			for (Map.Entry<Artikel, Integer> entry : inhalt.entrySet()) {
				if ((entry.getKey().getName().equals(name))) {
					zwischenSpeicherKey = entry.getKey();
					zwischenSpeicherValue = entry.getValue();
					zwischenSpeicherKey.setGelistet(selected);
					inhalt.remove(entry.getKey());
					inhalt.put(zwischenSpeicherKey, zwischenSpeicherValue);
					break;
				}
			}
		}
	}

	public void setPreis(String name, float preis) {
		Artikel zwischenSpeicherKey;
		int zwischenSpeicherValue;
		if (inhalt != null) {
			for (Map.Entry<Artikel, Integer> entry : inhalt.entrySet()) {
				if ((entry.getKey().getName().equals(name))) {
					zwischenSpeicherKey = entry.getKey();
					zwischenSpeicherValue = entry.getValue();
					zwischenSpeicherKey.setPreis(preis);
					inhalt.remove(entry.getKey());
					inhalt.put(zwischenSpeicherKey, zwischenSpeicherValue);
					break;
				}
			}
		}
	}

}
