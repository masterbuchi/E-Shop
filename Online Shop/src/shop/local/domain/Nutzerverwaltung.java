package shop.local.domain;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import shop.local.domain.exceptions.*;
import shop.local.persistence.PersistenceManager;
import shop.local.valueobjects.*;
import shop.local.valueobjects.Ereignis.EreignisTyp;

/**
 * 
 * Klasse zur Verwaltung von Nutzern
 * 
 * @author Error404
 *
 */

public class Nutzerverwaltung {
	private List<Kunde> kundenListe = new Vector<Kunde>();
	private List<Mitarbeiter> mitarbeiterListe = new Vector<Mitarbeiter>();
	private Map<UUID, Mitarbeiter> eingeloggteMitarbeiter = new HashMap<UUID, Mitarbeiter>();
	private Map<UUID, Kunde> eingeloggteKunden = new HashMap<UUID, Kunde>();
	private Ereignisverwaltung ereignisverwaltung;

	public Nutzerverwaltung(Ereignisverwaltung ereignisverwaltung) {
		this.ereignisverwaltung = ereignisverwaltung;

	}

	public void liesKunde(String datei, PersistenceManager pm) throws IOException {
		// PersistenceManager zum Lesen öffnen
		pm.openForReading(datei);

		Kunde kunde;
		do {
			kunde = pm.ladeKunde();
			if (kunde != null) {
				kundenListe.add(kunde);
			}
		} while (kunde != null);

		// Schließen
		pm.close();
	}

	public void schreibeKunde(String datei, PersistenceManager pm) throws IOException {
		// PersistenceManager zum Lesen öffnen
		pm.openForWriting(datei);

		for (Kunde kunde : kundenListe) {
			pm.speichereKunde(kunde);
		}

		// Persistenzmanager schließen
		pm.close();
	}

	public void liesMitarbeiter(String datei, PersistenceManager pm) throws IOException {
		// PersistenceManager zum Lesen öffnen
		pm.openForReading(datei);

		Mitarbeiter mitarbeiter;
		do {
			mitarbeiter = pm.ladeMitarbeiter();
			if (mitarbeiter != null) {
				mitarbeiterListe.add(mitarbeiter);
			}
		} while (mitarbeiter != null);

		// Schließen
		pm.close();
	}

	public void schreibeMitarbeiter(String datei, PersistenceManager pm) throws IOException {
		// PersistenceManager zum Lesen öffnen
		pm.openForWriting(datei);

		for (Mitarbeiter mitarbeiter : mitarbeiterListe) {
			pm.speichereMitarbeiter(mitarbeiter);
		}

		// Persistenzmanager schließen
		pm.close();
	}

	// Erzeugung der Listen

	// Mitarbeiter hinzufügen
	public void hinzufuegen(Mitarbeiter m1, Kunde test) throws MitarbeiterBereitsVorhandenException {

		if (mitarbeiterListe.contains(m1)) {
			throw new MitarbeiterBereitsVorhandenException(m1);
		} else if (kundenListe.contains(test)) {
			throw new MitarbeiterBereitsVorhandenException(m1);
		}
		Artikel artikel = new Artikel(-1, "leer", 0f, false);
		mitarbeiterListe.add(m1);
		ereignisverwaltung.ereignis(null, EreignisTyp.NEU, artikel, 0, m1);
	}

	// Kunde hinzufügen
	public void hinzufuegen(Kunde k1, Mitarbeiter test) throws KundeBereitsVorhandenException {

		if (kundenListe.contains(k1)) {
			throw new KundeBereitsVorhandenException(k1);
		} else if (mitarbeiterListe.contains(test)) {
			throw new KundeBereitsVorhandenException(k1);
		}
		Artikel artikel = new Artikel(-1, "leer", 0f, false);
		kundenListe.add(k1);
		ereignisverwaltung.ereignis(null, EreignisTyp.NEU, artikel, 0, k1);
	}

	// Die übergebenen Logindaten werden mit den beiden Listen (Mitarbeiter und
	// Kunden) verglichen
	public UUID loginUeberpruefen(String name, String passwort) {
		UUID uuid = null;
		for (Kunde kunde : kundenListe) {
			if (kunde.passwortUeberpruefen(name, passwort)) {

				uuid = UUID.randomUUID();
				eingeloggteKunden.put(uuid, kunde);
				break;
			}
		}

		for (Mitarbeiter mitarbeiter : mitarbeiterListe) {
			if (mitarbeiter.passwortUeberpruefen(name, passwort)) {

				uuid = UUID.randomUUID();
				eingeloggteMitarbeiter.put(uuid, mitarbeiter);
				break;
			}
		}

		return uuid;

	}

	// UUID eingeben und Informationen über Kunden oder Mitarbeiter bekommen

	public Nutzer aktuellerMitarbeiterOderKunde(UUID uuid) {

		if (eingeloggteMitarbeiter.get(uuid) != null) {
			Mitarbeiter mitarbeiterLight = new Mitarbeiter (eingeloggteMitarbeiter.get(uuid).getNutzerName(), eingeloggteMitarbeiter.get(uuid).getVorName(), eingeloggteMitarbeiter.get(uuid).getNachName());
			return mitarbeiterLight;
			
		} else if (eingeloggteKunden.get(uuid) != null) {
			Kunde kundeLight = new Kunde (eingeloggteKunden.get(uuid).getNutzerName(), eingeloggteKunden.get(uuid).getVorName(), eingeloggteKunden.get(uuid).getNachName(), eingeloggteKunden.get(uuid).getStrasse(), eingeloggteKunden.get(uuid).getPlz(), eingeloggteKunden.get(uuid).getWohnort(), eingeloggteKunden.get(uuid).getWarenkorb());
			
			return kundeLight;
		} else {
			return null;
		}
	}

	public Nutzer getKundeOderMitarbeiter(String name) throws NutzerNichtVorhandenException {
		for (Kunde kunde : kundenListe) {
			if (kunde.getNutzerName().equals(name)) {
				return kunde;
			}
		}
		for (Mitarbeiter mitarbeiter : mitarbeiterListe) {
			if (mitarbeiter.getNutzerName().equals(name)) {
				return mitarbeiter;
			}
		}
		throw new NutzerNichtVorhandenException();
	}

	// Gibt die Kundenliste zurück
	public List<Kunde> getKundenListe() {
		return kundenListe;
	}

	// Gibt die Mitarbeiterliste zurück
	public List<Mitarbeiter> getMitarbeiterListe() {
		return mitarbeiterListe;
	}

	public Map<UUID, Mitarbeiter> getEingeloggteMitarbeiter() {
		return eingeloggteMitarbeiter;
	}

	public Map<UUID, Kunde> getEingeloggteKunden() {
		return eingeloggteKunden;
	}

}
