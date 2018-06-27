package shop.local.domain;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Vector;

import shop.local.domain.exceptions.ArtikelNichtVorhandenException;
import shop.local.domain.exceptions.NutzerNichtVorhandenException;
import shop.local.persistence.PersistenceManager;
import shop.local.valueobjects.Artikel;
import shop.local.valueobjects.Nutzer;
import shop.local.valueobjects.Ereignis;
import shop.local.valueobjects.Ereignis.EreignisTyp;

public class Ereignisverwaltung {

	private List<Ereignis> ereignisse;

	public Ereignisverwaltung() {
		ereignisse = new Vector<Ereignis>();
	}

	public void liesEreignisse(String datei, PersistenceManager pm) throws IOException {
		// PersistenceManager zum Lesen öffnen
		pm.openForReading(datei);

		Ereignis ereignis;
		do {
			ereignis = pm.ladeEreignis();
			if (ereignis != null) {
				ereignisse.add(ereignis);
			}
		} while (ereignis != null);

		// Schließen
		pm.close();
	}

	public void schreibeEreignisse(String datei, PersistenceManager pm) throws IOException {
		// PersistenceManager zum Lesen öffnen
		pm.openForWriting(datei);

		for (Ereignis ereignis : ereignisse) {
			pm.speichereEreignis(ereignis);
			}

		// Persistenzmanager schließen
		pm.close();
	}

	public void ereignis(LocalDateTime zeit, EreignisTyp info, Artikel artikel, int anzahl, Nutzer user) {

		Ereignis ts = new Ereignis(zeit, info, artikel, anzahl, user);
		ereignisse.add(ts);

	}

	// Timestampliste ausgeben
	public List<Ereignis> ereignisseAusgeben() {
		return (ereignisse);
	}

}
