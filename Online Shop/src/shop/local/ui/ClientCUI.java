package shop.local.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import shop.local.domain.*;
import shop.local.domain.exceptions.*;
import shop.local.valueobjects.*;

public class ClientCUI {

	// Deklaration
	private Shop shop;
	private BufferedReader in;
	private boolean login = false;
	private boolean mitarbeiter = false;
	private boolean kunde = false;
	UUID aktuelleUUID;
	private Nutzer nutzer;

	// Konstruktor
	public ClientCUI(String datei) throws IOException, ArtikelNichtVorhandenException, NutzerNichtVorhandenException {

		shop = new Shop(datei);

		// Stream-Objekt fuer Texteingabe ueber Konsolenfenster erzeugen
		in = new BufferedReader(new InputStreamReader(System.in));
	}

	// Main-Methode
	public static void main(String[] args) throws ArtikelNichtVorhandenException, NutzerNichtVorhandenException {
		ClientCUI cui;
		try {
			cui = new ClientCUI("Shop");
			cui.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Darstellung der möglichen Befehle vor dem Einloggen
	private void menueBefehleVorEinloggen() {
		System.out.print("Herzlich Willkommen \n");
		System.out.println("");
		System.out.print("Befehle: \n  Als Mitarbeiter registrieren: 'm'");
		System.out.print("         \n  Als Kunden registrieren: 'k'");
		System.out.print("         \n  Einloggen: 'l'");
		System.out.print("         \n  Alle Artikel listen:  'a'");
		System.out.print("         \n  Artikel suchen:  'suche'");
		System.out.print("         \n  Daten sichern:  'save'");
		System.out.print("         \n  ---------------------");
		System.out.println("         \n  Beenden:        'q'");
		System.out.print("> ");
		System.out.flush();
	}

	// Darstellung der Befehle nach Mitarbeiter-Login
	private void menueBefehleNachEinloggenMitarbeiter() {
		System.out.println("");
		System.out.print("         \n  Alle Artikel listen:  'a'");
		System.out.print("         \n  Artikel einfügen: 'e'");
		System.out.print("         \n  Artikel löschen: 'd'");
		System.out.print("         \n  Neue Artikelmenge einlagern (Ergänzung): 'w'");
		System.out.print("         \n  Artikel suchen:  'suche'");
		System.out.print("         \n  Ausloggen:  'logout'");
		System.out.print("         \n  Daten sichern:  'save'");
		System.out.print("         \n  Ereignisse anzeigen:  'time'");
		System.out.print("         \n  ---------------------");
		System.out.println("         \n  Beenden:        'q'");
		System.out.print("> ");
		System.out.flush();
	}

	// Darstellung der Befehle nach Kundenlogin
	private void menueBefehleNachEinloggenKunde() {

		System.out.println("");
		System.out.print("         \n  Alle Artikel listen:  'a'");
		System.out.print("         \n  Warenkorb anzeigen:  'warenkorb'");
		System.out.print("         \n  Artikel suchen:  'suche'");
		System.out.print("         \n  Artikel zum Warenkorb hinzufügen:  'neu'");
		System.out.print("         \n  Artikel aus Warenkorb löschen:  'delete'");
		System.out.print("         \n  Artikelmenge in Warenkorb ändern:  'change'");
		System.out.print("         \n  Warenkorb leeren:  'clearall'");
		System.out.print("         \n  Artikel aus Warenkorb kaufen:  'buy'");
		System.out.print("         \n  Ausloggen:  'logout'");
		System.out.print("         \n  Daten sichern:  'save'");
		System.out.print("         \n  ---------------------");
		System.out.println("         \n  Beenden:        'q'");
		System.out.print("> ");
		System.out.flush();
	}

	// Methode zum starten der CUI
	public void run() throws IOException {
		String eingabe = "";

		// wenn noch nicht eingeloggt
		do {
			if (!login) {
				menueBefehleVorEinloggen();
				try {
					eingabe = liesEingabe();
					System.out.println("");
					verarbeiteVorEinloggenEingabe(eingabe);
				} catch (IOException e) {
					e.printStackTrace();
				}

				// wenn eingeloggt
			} else if (login) {
				// wenn als Mitarbeiter eingeloggt
				if (mitarbeiter) {
					menueBefehleNachEinloggenMitarbeiter();
					try {
						eingabe = liesEingabe();
						System.out.println("");
						verarbeiteNachEinloggenMitarbeiterEingabe(eingabe);
					} catch (IOException e) {
						e.printStackTrace();
					}
					// wenn als Kunde eingeloggt
				} else if (kunde) {

					menueBefehleNachEinloggenKunde();
					try {
						eingabe = liesEingabe();
						System.out.println("");
						verarbeiteNachEinloggenKundeEingabe(eingabe);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			// bis "q" eingegeben wird
		} while (!eingabe.equals("q"));
		shop.speichern();
	}

	// Eingabe lesen
	private String liesEingabe() throws IOException {
		return in.readLine();
	}

	// Auswahlmöglichkeiten vor dem Login
	private void verarbeiteVorEinloggenEingabe(String line) throws IOException {

		// Deklaration
		String nutzerName;
		String vorName;
		String nachName;
		String passwort;
		String strasse;
		String postleitzahl;
		int plz;
		String wohnort;

		String artikelName;

		Map<Artikel, Integer> lagerBestand;
		Artikel artikel;

		// Eingabe bearbeiten:
		switch (line) {

		// Ein neuer Mitarbeiter wird hinzugefügt
		case "m":
			// lies die notwendigen Parameter, einzeln pro Zeile
			try {
				System.out.print("Benutzername  > ");
				nutzerName = liesEingabe();
				System.out.print("Vorname  > ");
				vorName = liesEingabe();
				System.out.print("Nachname  > ");
				nachName = liesEingabe();
				System.out.print("Passwort  > ");
				passwort = liesEingabe();
				// Mitarbeiterinformationen wird an Shop zum Einfügen übergeben
				try {
					shop.mitarbeiterHinzufuegen(nutzerName, vorName, nachName, passwort);
					System.out.println("Einfügen ok");

					// falls die Fehlermeldung für einen doppelten Namen geworfen wird
				} catch (MitarbeiterBereitsVorhandenException e) {
					// Bei gleichem Mitarbeiternamen:
					System.out.println("Ein Mitarbeiter mit dem Benutzernamen " + e.getMitarbeiter().getNutzerName()
							+ " ist bereits vorhanden.");
					System.out.println("Bitte wählen sie einen anderen Benutzernamen. \n \n");
				}
				// Bei fehlerhafter Eingabe von Eingaben (falsche Zahl, Kommazahl etc.)
			} catch (NumberFormatException n) {
				System.out.println("Fehlerhafte Eingabe, bitte beginnen Sie erneut.");
				System.out.println("Für weitere Hilfe, richten Sie sich bitte an die Kundenberatung.");
			}
			break;

		// Ein neuer Kunde wird hinzugefügt
		case "k":
			// lies die notwendigen Parameter, einzeln pro Zeile
			try {
				System.out.print("Benutzername  > ");
				nutzerName = liesEingabe();
				System.out.print("Vorname  > ");
				vorName = liesEingabe();
				System.out.print("Nachname  > ");
				nachName = liesEingabe();
				System.out.print("Passwort  > ");
				passwort = liesEingabe();
				System.out.print("Straße > ");
				strasse = liesEingabe();
				System.out.print("Postleitzahl  > ");
				postleitzahl = liesEingabe();
				plz = Integer.parseInt(postleitzahl);
				System.out.print("Wohnort > ");
				wohnort = liesEingabe();
				// Kundeninformationen wird an Shop zum Einfügen übergeben
				try {
					shop.kundeHinzufuegen(nutzerName, vorName, nachName, passwort, strasse, plz, wohnort);
					System.out.println("Kunde wurde erfolgreich erzeugt");
					// falls die Fehlermeldung für einen doppelten Namen geworfen wird
				} catch (KundeBereitsVorhandenException e) {
					// Bei gleichem Kundennamen:
					System.out.println("Ein Kunde mit dem Benutzernamen " + e.getKunde().getNutzerName()
							+ " ist bereits vorhanden.");
					System.out.println("Bitte wählen sie einen anderen Benutzernamen. \n \n");
				}
				// Bei fehlerhafter Eingabe von Eingaben (falsche Zahl, Kommazahl etc.)
			} catch (NumberFormatException n) {
				System.out.println("Fehlerhafte Eingabe, bitte beginnen Sie erneut.");
				System.out.println("Für weitere Hilfe, richten Sie sich bitte an die Kundenberatung.");
			}
			break;

		// Alle Artikel werden ausgegeben
		case "a":
			lagerBestand = shop.alleArtikelAusgeben();
			gibArtikelWieder(lagerBestand);

			// Abfrage zur Sortierung der Artikel

			System.out.println("");
			System.out.println("Moechten Sie die Artikel nach ihrer ID sortieren?");
			System.out.println("Dann drücken sie 'b'");
			System.out.println("");
			System.out.println("Drücken Sie eine beliebige andere Taste um ins Menü zurückzukehren");

			// Eingabefeld

			System.out.print("> ");
			String eingabe = in.readLine();

			// Sortierung nach ID?

			if (lagerBestand != null) {
				if (eingabe.equals("b")) {
					System.out.println("");

					// Numerische Sortierung

					Map<Artikel, Integer> lager = shop.sortiereArtikelNachNummer();
					gibArtikelWieder(lager);
				}
			}

			break;

		// Einloggen
		case "l":
			try {
				System.out.print("Benutzername  > ");
				nutzerName = liesEingabe();
				System.out.print("Passwort  > ");
				passwort = liesEingabe();

				// Wenn die Logindaten mit einem Account übereinstimmen und es ein Mitarbeiter
				// ist

				aktuelleUUID = shop.login(nutzerName, passwort);

				if (aktuelleUUID == null) {
					// Kein erfolgreicher Login
					System.out.println("Es ist ein Fehler beim Login aufgetreten");
					System.out.println("Bitte überprüfen Sie ihren Benutzernamen und Ihr Passwort.");
				} else {

					// Login überprüfen ob Mitarbeiter oder Kunde

					if (shop.mitarbeiterOderKunde(aktuelleUUID) == 1) {
						login = true;
						mitarbeiter = true;
						nutzer = shop.getUser(aktuelleUUID);

						System.out.println("\n Login Erfolgreich \n Herzlich Willkomen, "
								+ ((Mitarbeiter) nutzer).getVorName() + " " + ((Mitarbeiter) nutzer).getNachName());

						// wenn es ein Kunde ist, der sich eingeloggt hat
					} else if (shop.mitarbeiterOderKunde(aktuelleUUID) == 2) {
						login = true;
						kunde = true;
						nutzer = shop.getUser(aktuelleUUID);

						System.out.println("\n Login Erfolgreich \n Herzlich Willkomen, "
								+ ((Kunde) nutzer).getVorName() + " " + ((Kunde) nutzer).getNachName());

					}
				}
			} catch (NumberFormatException n) {
				System.out.println("Fehlerhafte Eingabe, bitte beginnen Sie erneut.");
				System.out.println("Für weitere Hilfe, richten Sie sich bitte an die Kundenberatung.");
			}
			break;

		case "suche":
			try {
				System.out.print("Artikelname > ");
				artikelName = liesEingabe();
				artikel = shop.sucheNachWare(artikelName);
				System.out.print(artikel);
				System.out.println(shop.gibAnzahl(artikel) + " mal vorhanden.");
			} catch (NumberFormatException n) {
				System.out.println("Fehlerhafte Eingabe, bitte beginnen Sie erneut.");
				System.out.println("Für weitere Hilfe, richten Sie sich bitte an die Kundenberatung.");
			} catch (ArtikelNichtVorhandenException e) {
				System.out.println("Dieser Artikel ist nicht vorhanden.");
			}
			break;

		case "save":
			shop.speichern();
			break;
		}

	}

	// Auswahlmöglichkeiten nach dem Mitarbeiter-Login
	private void verarbeiteNachEinloggenMitarbeiterEingabe(String line) throws IOException {
		String name;
		String preis;
		float prs = 0;
		String anzahl;
		int anz = 0;
		String antwort;

		String artikelName;
		Map<Artikel, Integer> lagerBestand;

		List<Timestamp> ereignisse;

		// Eingabe bearbeiten:
		switch (line) {

		// Alle Artikel werden ausgegeben
		case "a":
			try {
				lagerBestand = shop.alleArtikelAusgeben();
				gibArtikelWieder(lagerBestand);

				// Abfrage zur Sortierung der Artikel

				System.out.println("");
				System.out.println("Moechten Sie die Artikel nach ihrer ID sortieren?");
				System.out.println("Dann drücken sie 'b'");
				System.out.println("");
				System.out.println("Drücken Sie eine beliebige andere Taste um ins Menü zurückzukehren");

				// Eingabefeld

				System.out.print("> ");
				String eingabe = in.readLine();

				// Sortierung nach ID?

				if (lagerBestand != null) {
					if (eingabe.equals("b")) {
						System.out.println("");

						// Numerische Sortierung

						Map<Artikel, Integer> lager = shop.sortiereArtikelNachNummer();
						gibArtikelWieder(lager);
					}
				}

			} catch (NumberFormatException n) {
				System.out.println("Fehlerhafte Eingabe, bitte beginnen Sie erneut.");
				System.out.println("Für weitere Hilfe, richten Sie sich bitte an die Kundenberatung.");
			}
			break;
		// einen Artikel löschen
		case "d":
			try {
				// lies die notwendigen Parameter, einzeln pro Zeile
				System.out.print("Artikelname > ");
				name = liesEingabe();
				shop.artikelLoeschen(name, aktuelleUUID);
			} catch (NumberFormatException n) {
				System.out.println("Fehlerhafte Eingabe, bitte beginnen Sie erneut.");
				System.out.println("Für weitere Hilfe, richten Sie sich bitte an die Kundenberatung.");
			} catch (ArtikelNichtVorhandenException e) {
				System.out.println("Dieser Artikel ist nicht vorhanden.");
			}
			break;

		// Anzahl eines Artikels anpassen
		case "w":
			try {
				System.out.print("Artikelname > ");
				name = liesEingabe();
				System.out.print("Neue Lieferung > ");
				anzahl = liesEingabe();
				anz = Integer.parseInt(anzahl);
				try {
					shop.anzahlAendern(name, anz, aktuelleUUID);
				} catch (ArtikelNichtVorhandenException v) {
					System.out.println(v);
				}
			} catch (NumberFormatException n) {
				System.out.println("Fehlerhafte Eingabe, bitte beginnen Sie erneut.");
				System.out.println("Für weitere Hilfe, richten Sie sich bitte an die Kundenberatung.");
			}
			break;

		// einen neuen Artikel erzeugen
		case "e":
			// lies die notwendigen Parameter, einzeln pro Zeile
			try {
				System.out.print("Name des Artikels  > ");
				name = liesEingabe();
				System.out.print("Preis > ");

				while (prs <= 0) {
					preis = liesEingabe();

					// Fehlerhafte, aber übliche Preisangaben mit Komma und € werden in
					// float-Schreibweise geändert

					preis = preis.replace(",", ".");
					preis = preis.replace("€", "");
					prs = Float.parseFloat(preis);
					if (prs <= 0) {
						System.out.println("Bitte geben Sie ausschließlich positive Werte für den Preis an.");
					}
				}

				System.out.print("Anzahl der hinzuzufügenden Artikel > ");
				while (anz <= 0) {
					anzahl = liesEingabe();
					anz = Integer.parseInt(anzahl);
					if (anz <= 0) {
						System.out.println("Bitte geben Sie ausschließlich positive Werte für die Anzahl an.");
					}
				}
				// Artikel wird an Shop zum Einfügen übergeben
				try {
					shop.artikelEinfuegen(name, prs, anz, aktuelleUUID);
					System.out.println("Einfügen ok");

				} catch (ArtikelBereitsVorhandenException e) {
					// Bei doppeltem Artikel:
					System.out.println("Der Artikel " + e.getArtikel().getName() + " ist bereits vorhanden.");
					System.out.println("Möchten Sie die Menge zum aktuellen Stand hinzufügen?");
					System.out.print("Zum Bestätigen drücken Sie 'y': ");
					antwort = liesEingabe();

					if (antwort.equals("y")) {
						try {
							shop.anzahlAendern(name, anz, aktuelleUUID);
						} catch (ArtikelNichtVorhandenException e1) {
							e1.printStackTrace();
						}
					}

					// Bei fehlerhafter Eingabe von Eingaben (falsche Zahl, Kommazahl etc.)
				}
			} catch (NumberFormatException n) {
				System.out.println("Fehlerhafte Eingabe, bitte beginnen Sie erneut.");
				System.out.println("Für weitere Hilfe, richten Sie sich bitte an die Kundenberatung.");
			}
			break;

		case "suche":
			try {
				System.out.print("Artikelname > ");
				artikelName = liesEingabe();
				Artikel artikel = shop.sucheNachWare(artikelName);
				System.out.print(artikel);
				System.out.println(shop.gibAnzahl(artikel) + " mal vorhanden.");
			} catch (NumberFormatException n) {
				System.out.println("Fehlerhafte Eingabe, bitte beginnen Sie erneut.");
				System.out.println("Für weitere Hilfe, richten Sie sich bitte an die Kundenberatung.");
			} catch (ArtikelNichtVorhandenException e) {
				System.out.println("Dieser Artikel ist nicht vorhanden.");
			}
			break;

		case "time":
			ereignisse = shop.ereignisseAusgeben();
			gibEreignisseWieder(ereignisse);

			break;

		case "save":
			shop.speichern();
			break;

		case "logout":
			login = false;
			mitarbeiter = false;
			kunde = false;
			shop.uuidLoeschen(aktuelleUUID);
			aktuelleUUID = null;
			break;
		}
	}

	// Auswahl nach dem Kunden-Login
	private void verarbeiteNachEinloggenKundeEingabe(String line) throws IOException {

		String artikelName;
		String name = null;
		String anzahl;
		String antwort;
		int anz = 0;

		Warenkorb warenkorb;

		Map<Artikel, Integer> lagerBestand;

		// Eingabe bearbeiten:
		switch (line) {

		// Alle Artikel werden ausgegeben

		case "a":
			try {
				lagerBestand = shop.alleArtikelAusgeben();
				gibArtikelWieder(lagerBestand);

				// Abfrage zur Sortierung der Artikel

				System.out.println("");
				System.out.println("Moechten Sie die Artikel nach ihrer ID sortieren?");
				System.out.println("Dann drücken sie 'b'");
				System.out.println("");
				System.out.println("Drücken Sie eine beliebige andere Taste um ins Menü zurückzukehren");

				// Eingabefeld

				System.out.print("> ");
				String eingabe = in.readLine();

				// Sortierung nach ID?

				if (lagerBestand != null) {
					if (eingabe.equals("b")) {
						System.out.println("");

						// Numerische Sortierung

						Map<Artikel, Integer> lager = shop.sortiereArtikelNachNummer();
						gibArtikelWieder(lager);
					}
				}

			} catch (NumberFormatException n) {
				System.out.println("Fehlerhafte Eingabe, bitte beginnen Sie erneut.");
				System.out.println("Für weitere Hilfe, richten Sie sich bitte an die Kundenberatung.");
			}
			break;

		case "warenkorb":
			try {
				warenkorb = shop.getWarenkorb(aktuelleUUID);
				if (warenkorb != null) {
					gibArtikelWieder(warenkorb.getMap());
				}

				// Abfrage zur Sortierung der Waren

				System.out.println("");
				System.out.println("Moechten Sie die Artikel nach ihrer ID sortieren?");
				System.out.println("Dann drücken sie 'b'");
				System.out.println("");
				System.out.println("Drücken Sie eine beliebige andere Taste um ins Menü zurückzukehren");

				// Eingabefeld

				System.out.print("> ");
				String eingabe = in.readLine();

				// Sortierung nach ID?

				if (warenkorb != null) {
					if (eingabe.equals("b")) {
						System.out.println("");

						// Numerische Sortierung

						Map<Artikel, Integer> waren = shop.sortiereWarenNachNummer(aktuelleUUID);
						gibArtikelWieder(waren);
					}
				}

			} catch (NumberFormatException n) {
				System.out.println("Fehlerhafte Eingabe, bitte beginnen Sie erneut.");
				System.out.println("Für weitere Hilfe, richten Sie sich bitte an die Kundenberatung.");
			}
			break;

		case "suche":
			try {

				/// EXCEPTION WERFEN LASSEN
				System.out.print("Artikelname > ");
				artikelName = liesEingabe();
				Artikel artikel = shop.sucheNachWare(artikelName);
				System.out.print(artikel);
				System.out.println(shop.gibAnzahl(artikel) + " mal vorhanden.");
			} catch (NumberFormatException n) {
				System.out.println("Fehlerhafte Eingabe, bitte beginnen Sie erneut.");
				System.out.println("Für weitere Hilfe, richten Sie sich bitte an die Kundenberatung.");
			} catch (ArtikelNichtVorhandenException e) {
				System.out.println(e.getMessage());
			}
			break;

		case "neu":
			// lies die notwendigen Parameter, einzeln pro Zeile
			try {
				try {
					System.out.print("Name des Artikels  > ");
					name = liesEingabe();
					System.out.print("Anzahl > ");

					while (anz <= 0) {
						anzahl = liesEingabe();
						anz = Integer.parseInt(anzahl);
						if (anz <= 0) {
							System.out.println("Bitte geben Sie ausschließlich positive Werte für die Anzahl an.");
						}
					}
					// Hinzuzufügende Ware wird übergeben

					shop.wareInWarenkorb(aktuelleUUID, name, anz);
					System.out.println("Ware wurde erfolgreich eingefügt");

				} catch (ArtikelBereitsVorhandenException e) {
					System.out.println(e.getMessage());
					System.out.println("Möchten Sie die Menge zum aktuellen Stand hinzufügen?");
					System.out.print("Zum Bestätigen drücken Sie 'y': ");
					antwort = liesEingabe();

					if (antwort.equals("y")) {
						shop.wareAnzahlAendern(aktuelleUUID, name, anz);
					}
				}
			} catch (NumberFormatException n) {
				System.out.println("Fehlerhafte Eingabe, bitte beginnen Sie erneut.");
				System.out.println("Für weitere Hilfe, richten Sie sich bitte an die Kundenberatung.");
			} catch (ArtikelNichtVorhandenException | NichtGenugArtikelException e1) {
				System.out.println(e1);
			}
			break;

		case "change":
			// lies die notwendigen Parameter, einzeln pro Zeile
			try {
				System.out.print("Name des Artikels  > ");
				name = liesEingabe();
				System.out.print("Hinzuzufügende Anzahl > ");
				while (anz <= 0) {
					anzahl = liesEingabe();
					anz = Integer.parseInt(anzahl);
					if (anz <= 0) {
						System.out.println("Bitte geben Sie ausschließlich positive Werte für die Anzahl an.");
					}
				}

				// Die zu ändernde Ware wird übergeben
				shop.wareAnzahlAendern(aktuelleUUID, name, anz);
				System.out.println("Anzahl der Ware wurde erfolgreich geändert.");

				// Bei fehlerhafter Eingabe von Eingaben (falsche Zahl, Kommazahl etc.)
			} catch (NumberFormatException n) {
				System.out.println("Fehlerhafte Eingabe, bitte beginnen Sie erneut.");
				System.out.println("Für weitere Hilfe, richten Sie sich bitte an die Kundenberatung.");
				// Falls der Artikel nicht im Lager vorhanden ist oder nicht genug Artikel im
				// Lager sind.
			} catch (ArtikelNichtVorhandenException | NichtGenugArtikelException e) {
				System.out.println(e);
			}
			break;

		// leert den Warenkorb
		case "clearall":
			shop.warenKorbLeeren(aktuelleUUID);
			System.out.println("Warenkorb wurde geleert.");
			break;

		// löscht einen Artikel aus dem Warenkorb (vollständig)
		case "delete":
			// lies die notwendigen Parameter, einzeln pro Zeile
			try {
				System.out.print("Name des Artikels  > ");
				name = liesEingabe();

				// Hinzuzufügende Ware wird übergeben

				shop.wareLoeschen(aktuelleUUID, name);

				// EXCEPTION FALLS DER ARTIKEL GAR NICHT VORHANDEN?
				System.out.println("Artikel wurde gelöscht");
				// Bei fehlerhafter Eingabe von Eingaben (falsche Zahl, Kommazahl etc.)
			} catch (NumberFormatException n) {
				System.out.println("Fehlerhafte Eingabe, bitte beginnen Sie erneut.");
				System.out.println("Für weitere Hilfe, richten Sie sich bitte an die Kundenberatung.");
			} catch (ArtikelNichtVorhandenException e) {
				System.out.println(e);
			}
			break;

		case "buy":
			String best;
			// lies die notwendigen Parameter, einzeln pro Zeile
			try {
				try {

					String strasse;
					String postleitzahl;
					int plz = 0;
					String wohnort;
					float summe = 0;
					float summeUndMehrwertsteuer;

					warenkorb = shop.getWarenkorb(aktuelleUUID);
					gibArtikelWieder(warenkorb.getMap());

					System.out.println("Möchten Sie diese Artikel jetzt bestellen? ");
					System.out.print("[y] / [n] > ");
					best = liesEingabe();

					if (best.equals("y")) {

						System.out.println(" ");
						System.out.println("Rechnungsadresse: ");
						nutzer = shop.getUser(aktuelleUUID);
						System.out.println(" " + ((Kunde) nutzer).getStrasse());
						System.out.println(" " + ((Kunde) nutzer).getPlz() + " " + ((Kunde) nutzer).getPlz());

						System.out.println("");
						System.out.println("Möchten Sie die Produkte an diese Adresse senden?");
						System.out.print(" [n] / Press any Button for yes > ");
						best = liesEingabe();

						
						if (best.equals("n")) {

							System.out.println("");
							System.out.println("Bitte geben Sie die neue Adresse ein:");
							System.out.print("Straße (mit Hausnummer) > ");
							strasse = liesEingabe();
							System.out.print("Postleitzahl  > ");

							while (plz <= 0) {
								postleitzahl = liesEingabe();
								plz = Integer.parseInt(postleitzahl);
								if (plz <= 0) {
									System.out.println("Bitte geben Sie eine korrekte Angabe für die Postleitzahl an.");
								}

							}
							System.out.print("Wohnort  > ");
							wohnort = liesEingabe();

							System.out.println("Möchten Sie diese Adresse dauerhaft speichern?");
							System.out.print("y/n >");
							best = liesEingabe();
							if (best.equals("y")) {

								shop.adresseAendern(((Kunde) nutzer).getNutzerName(), strasse, plz, wohnort);

							}

							// Wenn irgendeine Eingabe außer Nein (n) aber mit neuer Adresse

							System.out.println("---------------------------------------");

							System.out.println("Rechnung: ");
							System.out.println(
									" " + ((Kunde) nutzer).getVorName() + " " + ((Kunde) nutzer).getNachName());
							System.out.println(" " + strasse);
							System.out.println(" " + plz + " " + wohnort);
							System.out.println(" ");

							warenkorb = shop.getWarenkorb(aktuelleUUID);
							gibArtikelWieder(warenkorb.getMap());

							System.out.println("________________________________________");

							summe = shop.summe(aktuelleUUID);
							System.out.println(summe + " €");
							summeUndMehrwertsteuer = (float) (summe + (summe * 0.19));
							System.out.println("Mit Mehrwertsteuer: " + summeUndMehrwertsteuer + " €");

							System.out.println("");

							System.out.println("---------------------------------------");

							shop.kaufen(aktuelleUUID);

							break;

						}

						System.out.println("---------------------------------------");

						System.out.println("Rechnung: ");
						System.out.println(" " + ((Kunde) nutzer).getVorName() + " " + ((Kunde) nutzer).getNachName());
						System.out.println(" " + ((Kunde) nutzer).getStrasse());
						System.out.println(" " + ((Kunde) nutzer).getPlz() + " " + ((Kunde) nutzer).getWohnort());
						System.out.println(" ");

						warenkorb = shop.getWarenkorb(aktuelleUUID);
						gibArtikelWieder(warenkorb.getMap());

						System.out.println("________________________________________");

						summe = shop.summe(aktuelleUUID);
						System.out.println(shop.summe(aktuelleUUID) + " €");
						summeUndMehrwertsteuer = (float) (summe + (summe * 0.14));
						System.out.println("Mit Mehrwertsteuer: " + summeUndMehrwertsteuer + " €");

						System.out.println("");

						System.out.println("---------------------------------------");

						shop.kaufen(aktuelleUUID);

						break;

						// Bei fehlerhafter Eingabe von Eingaben (falsche Zahl, Kommazahl etc.)
					} else {
						break;
					}

				} catch (NumberFormatException n) {
					System.out.println("Fehlerhafte Eingabe, bitte beginnen Sie erneut.");
					System.out.println("Für weitere Hilfe, richten Sie sich bitte an die Kundenberatung.");
				}
				break;
			} catch (NichtGenugArtikelException k) {
				System.out.println(k);
			}

		case "save":
			shop.speichern();
			break;

		// Kunde loggt sich aus
		case "logout":
			login = false;
			mitarbeiter = false;
			kunde = false;
			shop.uuidLoeschen(aktuelleUUID);
			aktuelleUUID = null;
			break;
		}

	}

	private void gibArtikelWieder(Map<Artikel, Integer> map) {
		if (map.isEmpty()) {
			System.out.println("Es sind keine Artikel vorhanden.");
		}
		for (Entry<Artikel, Integer> entry : map.entrySet()) {
			System.out.print(entry.getKey());
			if (entry.getValue() == 0) {
				System.out.println("Dieser Artikel ist derzeit nicht verfügbar.");
			} else {
				System.out.println("Anzahl: " + entry.getValue());
				System.out.println("");
			}
		}
	}

	// Ereignisse auslesen und ausgeben
	private void gibEreignisseWieder(List<Timestamp> ereignisse) {
		if (ereignisse == null) {
			System.out.println("Es sind keine Ereignisse vorhanden");
		} else {
			for (Timestamp timestamp : ereignisse) {
				System.out.println(timestamp);
			}
		}
	}

}
