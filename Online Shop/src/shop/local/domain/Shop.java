package shop.local.domain;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import shop.local.domain.exceptions.*;
import shop.local.persistence.FilePersistenceManager;
import shop.local.persistence.PersistenceManager;
import shop.local.valueobjects.*;
import shop.local.domain.comparators.ArtikelNummerComparator;

/**
 * 
 * Klasse zur Gesamtverwaltung des Shops
 * 
 * @author Error404
 *
 */

public class Shop {

	// Deklaration
	private Logistik logistik;
	private Nutzerverwaltung nutzerverwaltung;
	private ShoppingService shoppingService;
	private Ereignisverwaltung ereignisverwaltung;
	private String datei = "";

	// Persistenz-Schnittstelle
	private PersistenceManager pm;

	// Konstruktor
	public Shop(String datei) throws IOException, ArtikelNichtVorhandenException, NutzerNichtVorhandenException {
		ereignisverwaltung = new Ereignisverwaltung();
		logistik = new Logistik(ereignisverwaltung);
		nutzerverwaltung = new Nutzerverwaltung(ereignisverwaltung);
		shoppingService = new ShoppingService(logistik);
		pm = new FilePersistenceManager(logistik, nutzerverwaltung);
		this.datei = datei;
		logistik.liesArtikel(datei+"_Artikel.txt", pm);
		nutzerverwaltung.liesKunde(datei+"_Kunden.txt", pm);
		nutzerverwaltung.liesMitarbeiter(datei+"_Mitarbeiter.txt", pm);
		ereignisverwaltung.liesTimestamp(datei+"_Ereignisse.txt", pm);

	}
	
	public void speichern() throws IOException {
		
		logistik.schreibeArtikel(datei+"_Artikel.txt", pm);
		nutzerverwaltung.schreibeKunde(datei+"_Kunden.txt", pm);
		nutzerverwaltung.schreibeMitarbeiter(datei+"_Mitarbeiter.txt", pm);
		ereignisverwaltung.schreibeTimestamp(datei+"_Ereignisse.txt", pm);
	}
	

	// Alle Artikel aus der Logistik werden abgerufen und der CUI zurückgegeben
	public Map<Artikel, Integer> alleArtikelAusgeben() {
		// einfach delegieren an die Logistik
		return (logistik.getLagerBestand());
	}

	// Mit den Informationen der CUI wird ein Artikel erstellt und an die Logistik
	// weitergegeben
	public void artikelEinfuegen(String name, float preis, int anzahl, UUID uuid)
			throws ArtikelBereitsVorhandenException {
		Artikel a = new Artikel(0, name, preis);
		logistik.hinzufuegen(a, anzahl, nutzerverwaltung.aktuellerMitarbeiterOderKunde(uuid));

	}

	// Einen Artikel aus dem Lager heraussuchen
	public Artikel sucheNachWare(String name) throws ArtikelNichtVorhandenException {
		return logistik.sucheArtikelName(name);
	}
	
	// Einen Artikel aus dem Lager heraussuchen
		public Map<Artikel, Integer> sucheNachWarenTeil(String name) throws ArtikelNichtVorhandenException {
			return logistik.sucheArtikelNamenTeil(name);
		}

	// Anzahl eines Artikel zurückgeben
	public Integer gibAnzahl(Artikel artikel) {
		return logistik.gibAnzahlZurueck(artikel);
	}

	// Artikel in der Logstik löschen
	public boolean artikelLoeschen(String name, UUID uuid) throws ArtikelNichtVorhandenException {

		Artikel gesuchterArtikel = logistik.sucheArtikelName(name);
		return (logistik.loeschen(gesuchterArtikel, nutzerverwaltung.aktuellerMitarbeiterOderKunde(uuid)));
	}

	// Anzahl in der Logistik verändern
	public void anzahlAendern(String name, int anzahlneu, UUID uuid) throws ArtikelNichtVorhandenException {
		Artikel gesuchterArtikel = logistik.sucheArtikelName(name);
		logistik.anzahl(gesuchterArtikel, anzahlneu, nutzerverwaltung.aktuellerMitarbeiterOderKunde(uuid));
	}

	// Artikel nach ID sortieren
	public Map<Artikel, Integer> sortiereArtikelNachNummer() {

		Map<Artikel, Integer> sortiertNummer = new TreeMap<Artikel, Integer>(new ArtikelNummerComparator());

		sortiertNummer.putAll(logistik.getLagerBestand());

		return sortiertNummer;
	}

	// Adresse des Kunden ändern
	public void adresseAendern(String name, String strasse, int postleitzahl, String wohnort) {
		for (Kunde kunde : nutzerverwaltung.getKundenListe()) {
			if (kunde.getNutzerName().equals(name)) {
				kunde.setStrasse(strasse);
				kunde.setPlz(postleitzahl);
				kunde.setWohnort(wohnort);
			}
		}
	}

	// Mit den Informationen der CUI wird ein Mitarbeiter erstellt und an die
	// Nutzerverwaltung weitergegeben
	public void mitarbeiterHinzufuegen(String nutzerName, String vorName, String nachName, String passwort)
			throws MitarbeiterBereitsVorhandenException {
		Mitarbeiter m = new Mitarbeiter(nutzerName, vorName, nachName, passwort);
		Kunde test = new Kunde(nutzerName, vorName, nachName, passwort, "test", 123, "test");

		nutzerverwaltung.hinzufuegen(m, test);
	}

	// Mit den Informationen der CUI wird ein Kunde erstellt und an die
	// Nutzerverwaltung weitergegeben
	public void kundeHinzufuegen(String nutzerName, String vorName, String nachName, String passwort, String strasse,
			int plz, String wohnort) throws KundeBereitsVorhandenException {
		Kunde k = new Kunde(nutzerName, vorName, nachName, passwort, strasse, plz, wohnort);
		Mitarbeiter test = new Mitarbeiter(nutzerName, vorName, nachName, passwort);

		nutzerverwaltung.hinzufuegen(k, test);
	}

	// Die Login-Daten der CUI werden an die Nutzerverwaltung zur Prüfung
	// weitergegeben
	public UUID login(String nutzerName, String passwort) {
		return nutzerverwaltung.loginUeberpruefen(nutzerName, passwort);
	}

	// GETTER FÜR ALLE FUNKTIONEN DER MITARBEITER UND KUNDEN

	// Mitarbeiter oder Kunde abfragen; Übergabe an die Nutzerverwaltung

	public int mitarbeiterOderKunde(UUID uuid) {
		if (nutzerverwaltung.aktuellerMitarbeiterOderKunde(uuid) instanceof Mitarbeiter) {
			return 1;
		} else if (nutzerverwaltung.aktuellerMitarbeiterOderKunde(uuid) instanceof Kunde) {
			return 2;
		} else {
			return 0;
		}
	}


	public Nutzer getUser(UUID uuid) {
		return nutzerverwaltung.aktuellerMitarbeiterOderKunde(uuid);
	}


	public Warenkorb getWarenkorb(UUID uuid) {
		Kunde kunde = (Kunde) nutzerverwaltung.aktuellerMitarbeiterOderKunde(uuid);
		return kunde.getWarenkorb();
	}


	public void uuidLoeschen(UUID uuid) {
		nutzerverwaltung.getEingeloggteKunden().remove(uuid);
		nutzerverwaltung.getEingeloggteMitarbeiter().remove(uuid);
	}

	// ShoppingService

	public void wareInWarenkorb(UUID uuid, String name, int anzahl)
			throws ArtikelNichtVorhandenException, NichtGenugArtikelException, ArtikelBereitsVorhandenException {
		Kunde kunde = (Kunde) nutzerverwaltung.aktuellerMitarbeiterOderKunde(uuid);
		shoppingService.wareHinzufuegen(kunde, name, anzahl);
	}

	public Map<Artikel, Integer> sortiereWarenNachNummer(UUID uuid) {

		Map<Artikel, Integer> sortiertNummer = new TreeMap<Artikel, Integer>(new ArtikelNummerComparator());

		sortiertNummer.putAll(this.getWarenkorb(uuid).getMap());

		return sortiertNummer;
	}

	public void wareLoeschen(UUID uuid, String name) throws ArtikelNichtVorhandenException {
		Kunde kunde = (Kunde) nutzerverwaltung.aktuellerMitarbeiterOderKunde(uuid);
		shoppingService.wareLoeschen(kunde, name);
	}

	public void warenKorbLeeren(UUID uuid) {
		Kunde kunde = (Kunde) nutzerverwaltung.aktuellerMitarbeiterOderKunde(uuid);
		shoppingService.warenKorbLeeren(kunde);
	}

	public void wareAnzahlAendern(UUID uuid, String name, int anzahl)
			throws ArtikelNichtVorhandenException, NichtGenugArtikelException {
		Kunde kunde = (Kunde) nutzerverwaltung.aktuellerMitarbeiterOderKunde(uuid);
		shoppingService.wareAnzahlAendern(kunde, name, anzahl);
	}

	public void kaufen(UUID uuid) throws NichtGenugArtikelException {
		Kunde kunde = (Kunde) nutzerverwaltung.aktuellerMitarbeiterOderKunde(uuid);
		shoppingService.kaufen(kunde);
	}

	public float summe(UUID uuid) {
		Kunde kunde = (Kunde) nutzerverwaltung.aktuellerMitarbeiterOderKunde(uuid);
		return (shoppingService.summe(kunde));
	}

	// Ereignisverwaltung

	public List<Timestamp> ereignisseAusgeben() {
		return ereignisverwaltung.ereignisseAusgeben();
	}

}
