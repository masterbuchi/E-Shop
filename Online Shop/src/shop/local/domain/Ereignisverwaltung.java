package shop.local.domain;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import shop.local.domain.exceptions.ArtikelNichtVorhandenException;
import shop.local.domain.exceptions.NutzerNichtVorhandenException;
import shop.local.persistence.PersistenceManager;
import shop.local.valueobjects.Artikel;
import shop.local.valueobjects.Nutzer;
import shop.local.valueobjects.Timestamp;
import shop.local.valueobjects.Timestamp.EreignisTyp;

public class Ereignisverwaltung {

	private List<Timestamp> ereignisse;

	public Ereignisverwaltung() {
		ereignisse = new Vector<Timestamp>();
	}

	public void liesTimestamp(String datei, PersistenceManager pm) throws IOException, ArtikelNichtVorhandenException, NutzerNichtVorhandenException {
		// PersistenceManager zum Lesen öffnen
		pm.openForReading(datei);

		Timestamp timestamp;
		do {
			timestamp = pm.ladeTimestamp();
			if (timestamp != null) {
				ereignisse.add(timestamp);
			}
		} while (timestamp != null);

		// Schließen
		pm.close();
	}

	public void schreibeTimestamp(String datei, PersistenceManager pm) throws IOException {
		// PersistenceManager zum Lesen öffnen
		pm.openForWriting(datei);

		for (Timestamp timestamp : ereignisse) {
			pm.speichereTimestamp(timestamp);
			}

		// Persistenzmanager schließen
		pm.close();
	}

	public void timestamp(String zeit, EreignisTyp info, Artikel artikel, int anzahl, Nutzer user) {

		Timestamp ts = new Timestamp(zeit, info, artikel, anzahl, user);
		ereignisse.add(ts);

	}

	// Timestampliste ausgeben
	public List<Timestamp> ereignisseAusgeben() {
		return (ereignisse);
	}

}
