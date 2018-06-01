package shop.local.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import shop.local.domain.Logistik;
import shop.local.domain.Nutzerverwaltung;
import shop.local.domain.exceptions.ArtikelNichtVorhandenException;
import shop.local.domain.exceptions.NutzerNichtVorhandenException;
import shop.local.valueobjects.Artikel;
import shop.local.valueobjects.Kunde;
import shop.local.valueobjects.Mitarbeiter;
import shop.local.valueobjects.Nutzer;
import shop.local.valueobjects.Timestamp;
import shop.local.valueobjects.Timestamp.EreignisTyp;
import shop.local.valueobjects.Warenkorb;

public class FilePersistenceManager implements PersistenceManager {

	private Logistik logistik;
	private Nutzerverwaltung nutzerverwaltung;

//	public FilePersistenceManager(Map<Artikel, Integer> artikelListe, List<Nutzer> nutzerListe) {
	public FilePersistenceManager(Logistik logistik, Nutzerverwaltung nutzerverwaltung) {
		this.logistik = logistik;
		this.nutzerverwaltung = nutzerverwaltung;
	}

	private BufferedReader reader = null;
	private PrintWriter writer = null;

	public void openForReading(String datei) throws FileNotFoundException {
		reader = new BufferedReader(new FileReader(datei));
	}

	public void openForWriting(String datei) throws IOException {
		writer = new PrintWriter(new BufferedWriter(new FileWriter(datei)));
	}

	public boolean close() {
		if (writer != null)
			writer.close();

		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				return false;
			}
		}

		return true;
	}

	public Artikel ladeArtikel() throws IOException {
		String idString = liesZeile();
		
		if (idString == null) {
			// keine Daten mehr vorhanden
						return null;
		}
		// in int konvertieren
		int id = Integer.parseInt(idString);
		
		
		// Name auslesen
		String name = liesZeile();
		// Preis einlesen ...
		String preisString = liesZeile();
		// in float konvertieren
		float preis = Float.parseFloat(preisString);

		// neuer Artikel wird erzeugt
		return new Artikel(id, name, preis);
	}

	public void speichereArtikel(Artikel artikel) throws IOException {
		
		schreibeZeile(artikel.getNummer() +"");
		
		// Name schreiben
		schreibeZeile(artikel.getName());

		schreibeZeile(artikel.getPreis() + "");

	}

	public int ladeAnzahl() throws IOException {
		// Anzahl einlesen ...
		String anzahlString = liesZeile();
		// in int konvertieren
		int anzahl = Integer.parseInt(anzahlString);
		return anzahl;
	}

	public void speichereAnzahl(int anzahl) throws IOException {
		schreibeZeile(anzahl + "");
	}

	public Kunde ladeKunde() throws IOException {
		// Name auslesen
		String nutzerName = liesZeile();
		if (nutzerName == null) {
			// keine Daten mehr vorhanden
			return null;
		}

		String vorName = liesZeile();
		String nachName = liesZeile();
		String passwort = liesZeile();
		String strasse = liesZeile();
		String postleitzahlString = liesZeile();
		int plz = Integer.parseInt(postleitzahlString);
		String wohnort = liesZeile();

		return new Kunde(nutzerName, vorName, nachName, passwort, strasse, plz, wohnort);
	}

	public void speichereKunde(Kunde kunde) throws IOException {
		schreibeZeile(kunde.getNutzerName());
		schreibeZeile(kunde.getVorName());
		schreibeZeile(kunde.getNachName());
		schreibeZeile(kunde.getPasswort());
		schreibeZeile(kunde.getStrasse());
		schreibeZeile(kunde.getPlz() + "");
		schreibeZeile(kunde.getWohnort());

	}

	public Mitarbeiter ladeMitarbeiter() throws IOException {
		// Name auslesen
		String nutzerName = liesZeile();
		if (nutzerName == null) {
			// keine Daten mehr vorhanden
			return null;
		}

		String vorName = liesZeile();
		String nachName = liesZeile();
		String passwort = liesZeile();
		return new Mitarbeiter(nutzerName, vorName, nachName, passwort);

	}

	public void speichereMitarbeiter(Mitarbeiter mitarbeiter) throws IOException {
		schreibeZeile(mitarbeiter.getNutzerName());
		schreibeZeile(mitarbeiter.getVorName());
		schreibeZeile(mitarbeiter.getNachName());
		schreibeZeile(mitarbeiter.getPasswort());
	}

	public Timestamp ladeTimestamp() throws IOException, ArtikelNichtVorhandenException, NutzerNichtVorhandenException {
		Artikel artikel;
		Nutzer nutzer;
		// Name auslesen
		String ereignistypString = liesZeile();
		if (ereignistypString == null) {
			return null;
		}
		EreignisTyp ereignistyp = EreignisTyp.valueOf(ereignistypString);

		// keine Daten mehr vorhanden

		String zeit = liesZeile();

		String nameVonArtikel = liesZeile();
		
		String anzahlString = liesZeile();
		int anz = Integer.parseInt(anzahlString);
		
		try {
			
			artikel = logistik.sucheArtikelName(nameVonArtikel);

		} catch (ArtikelNichtVorhandenException e) {
			
			artikel = new Artikel(-1, nameVonArtikel, anz);
		}



		String nameVonNutzer = liesZeile();

		try {
		nutzer = nutzerverwaltung.getKundeOderMitarbeiter(nameVonNutzer);
		
		} catch (NutzerNichtVorhandenException f){
			nutzer = new Mitarbeiter(nameVonNutzer, "", "", "");
		}

		return new Timestamp(zeit, ereignistyp, artikel, anz, nutzer);
	}

	public void speichereTimestamp(Timestamp timestamp) throws IOException {

		schreibeZeile(timestamp.getEreignisTyp().name());
		schreibeZeile(timestamp.getZeit());
		schreibeZeile(timestamp.getArtikel().getName());
		schreibeZeile(timestamp.getAnzahl() + "");
		schreibeZeile(timestamp.getUser().getNutzerName());

	}

	

	/*
	 * Private Hilfsmethoden
	 */

	private String liesZeile() throws IOException {
		if (reader != null)
			return reader.readLine();
		else
			return "";
	}

	private void schreibeZeile(String daten) {
		if (writer != null)
			writer.println(daten);
	}
}
