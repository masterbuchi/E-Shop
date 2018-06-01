package shop.local.domain;

import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.IOException;
import java.util.Map;

import shop.local.domain.comparators.ArtikelNameComparator;
import shop.local.domain.exceptions.ArtikelBereitsVorhandenException;
import shop.local.domain.exceptions.ArtikelNichtVorhandenException;
import shop.local.persistence.PersistenceManager;
import shop.local.valueobjects.Artikel;
import shop.local.valueobjects.Nutzer;
import shop.local.valueobjects.Timestamp.EreignisTyp;

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
		int hoechsterWert = 0;
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

	
	
	public void schreibeArtikel(String datei, PersistenceManager pm) throws IOException  {
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
		
		ereignisverwaltung.timestamp(null, EreignisTyp.NEU, neuerArtikel, anzahl, nutzer);
	}



	// Löscht einen Artikel aus der Liste
	public boolean loeschen(Artikel artikel, Nutzer nutzer) {
		if (inhalt.containsKey(artikel)) {
			inhalt.remove(artikel);
			ereignisverwaltung.timestamp(null, EreignisTyp.LOESCHUNG, artikel, 0, nutzer);
			return true;
		}
		return false;
	}

	// Legt die Anzahl eines Artikels neu fest indem der vorherige Wert durch die neue Angabe ergänzt wird.
	public void anzahl(Artikel artikel, int anzahlNeu, Nutzer nutzer) throws ArtikelNichtVorhandenException {
		if (inhalt.containsKey(artikel)) {
			inhalt.put(artikel, (inhalt.get(artikel) + anzahlNeu));
			ereignisverwaltung.timestamp(null, EreignisTyp.EINLAGERUNG, artikel, anzahlNeu, nutzer);
		} else {
			throw new ArtikelNichtVorhandenException();
		}
	}
	
	
	public Integer gibAnzahlZurueck (Artikel artikel) {
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
	
	public Ereignisverwaltung getEreignisverwaltung() {
		return this.ereignisverwaltung;
	}



	
	
}
