package shop.local.domain;

import java.util.Map;

import shop.local.domain.exceptions.ArtikelBereitsVorhandenException;
import shop.local.domain.exceptions.ArtikelNichtVorhandenException;
import shop.local.domain.exceptions.NichtGenugArtikelException;
import shop.local.valueobjects.Artikel;
import shop.local.valueobjects.Kunde;
import shop.local.valueobjects.Warenkorb;
import shop.local.valueobjects.Ereignis.EreignisTyp;

public class ShoppingService {

	private Logistik logistik;

	// Konstruktor
	public ShoppingService(Logistik logistik) {
		this.logistik = logistik;

	}

	public Warenkorb warenKorbAnzeigen(Kunde kunde) {
		return kunde.getWarenkorb();
	}

	// Fügt einen Artikel zum Warenkorb hinzu
	public void wareHinzufuegen(Kunde kunde, String name, int anzahl)
			throws ArtikelNichtVorhandenException, NichtGenugArtikelException, ArtikelBereitsVorhandenException {

		// passenden Artikel im Lagerbestand der Logistik suchen
		Artikel logistikArtikel = logistik.sucheArtikelName(name);

			Warenkorb korb = kunde.getWarenkorb();

			if (korb.getMap().containsKey(logistikArtikel)) {
				throw new ArtikelBereitsVorhandenException(logistikArtikel);
			}
			if (anzahl <= logistik.gibAnzahlZurueck(logistikArtikel)) {

				korb.hinzufuegen(logistikArtikel, anzahl);

			} else {
				throw new NichtGenugArtikelException(logistikArtikel);
			}
			kunde.setWarenkorb(korb);

	}

	// Ändert die Artikelmenge im Warenkorb auf einen neuen Wert
	
	
	public void wareAnzahlAendern(Kunde kunde, String name, int anzahl)
			throws NichtGenugArtikelException, ArtikelNichtVorhandenException {

			Warenkorb korb = kunde.getWarenkorb();

			Artikel korbArtikel = korb.sucheArtikelName(name);

			if (anzahl <= logistik.gibAnzahlZurueck(korbArtikel)) {

				korb.hinzufuegen(korbArtikel, anzahl);

			} else {
				throw new NichtGenugArtikelException(korbArtikel);
			}
			kunde.setWarenkorb(korb);
		
	}

	// Löscht einen Artikel aus dem Warenkorb
	public void wareLoeschen(Kunde kunde, String name) throws ArtikelNichtVorhandenException {

		Warenkorb korb = kunde.getWarenkorb();
		Artikel korbArtikel = korb.sucheArtikelName(name);

		if (korbArtikel != null) {
			korb.loeschen(korbArtikel);
		} else {
			throw new ArtikelNichtVorhandenException();
		}
		kunde.setWarenkorb(korb);

	}

	// Gibt die Summe für den kompletten Warenkorb zurück
	public float summe(Kunde kunde) {

		Warenkorb korb = kunde.getWarenkorb();

		float summe = 0f;

		Map<Artikel, Integer> map = korb.getMap();
		if (!map.isEmpty()) {
			for (Map.Entry<Artikel, Integer> entry : map.entrySet()) {
				summe = summe + entry.getValue() * entry.getKey().getPreis();
			}
		}

		summe = (float) (Math.rint(summe * 100) / 100);
		return summe;
	}

	// Warenkorb leeren
	public void warenKorbLeeren(Kunde kunde) {

		kunde.getWarenkorb().getMap().clear();
	}

	// Kaufmethode
	public void kaufen(Kunde kunde) throws NichtGenugArtikelException {

		Warenkorb warenkorb = kunde.getWarenkorb();

		Map<Artikel, Integer> map = warenkorb.getMap();
		if (!map.isEmpty()) {
			for (Map.Entry<Artikel, Integer> entry : map.entrySet()) {
				if (logistik.getLagerBestand().containsKey(entry.getKey())) {
					if (entry.getValue() <= logistik.getLagerBestand().get(entry.getKey())) {
						logistik.getLagerBestand().put(entry.getKey(),
								(logistik.getLagerBestand().get(entry.getKey()) - entry.getValue()));
						logistik.getEreignisverwaltung().ereignis(null, EreignisTyp.AUSLAGERUNG, entry.getKey(),
								entry.getValue(), kunde);
					} else {
						throw new NichtGenugArtikelException(entry.getKey());
					}
				}
			}
		}
		kunde.getWarenkorb().getMap().clear();

	}

}
