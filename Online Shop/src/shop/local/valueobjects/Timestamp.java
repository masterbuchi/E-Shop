package shop.local.valueobjects;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Timestamp {

	public enum EreignisTyp {
		NEU, KAUF, EINLAGERUNG, AUSLAGERUNG, LOESCHUNG;
	}

	private EreignisTyp ereignisTyp;
	private Artikel artikel;
	private int anzahl;
	private Nutzer user;

	LocalDateTime now = LocalDateTime.now();
	DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy kk:mm");
	String zeit = (String) now.format(df);

	public Timestamp(String zeit, EreignisTyp ereignis, Artikel artikel, int anzahl, Nutzer user) {
		this.ereignisTyp = ereignis;
		this.artikel = artikel;
		this.anzahl = anzahl;
		if (zeit != null) {
			this.zeit = zeit;
		}

		if (user instanceof Mitarbeiter) {
			this.user = (Mitarbeiter) user;
		}

		if (user instanceof Kunde) {
			this.user = (Kunde) user;
		}

	}
	
	public String getZeit() {
		return zeit;
	}

	public EreignisTyp getEreignisTyp() {
		return ereignisTyp;
	}

	public void setEreignisTyp(EreignisTyp ereignisTyp) {
		this.ereignisTyp = ereignisTyp;
	}

	public Artikel getArtikel() {
		return artikel;
	}

	public void setArtikel(Artikel artikel) {
		this.artikel = artikel;
	}

	public int getAnzahl() {
		return anzahl;
	}

	public void setAnzahl(int anzahl) {
		this.anzahl = anzahl;
	}

	public Nutzer getUser() {
		return user;
	}

	public void setUser(Nutzer user) {
		this.user = user;
	}

	
	@Override
	public String toString() {
		
		if (artikel.getName().equals("leer")) {
			if (user instanceof Mitarbeiter) {
				return (zeit + " " + user.getNutzerName() + ", " + user.getVorName() + " " + user.getNachName() + " wurde als neuer Mitarbeiter hinzugefügt");

			} else {
				return (zeit + " " + user.getNutzerName() + ", " + user.getVorName() + " " + user.getNachName() + " wurde als neuer Kunde hinzugefügt");
			}
		} else {

		return (zeit + " " + ereignisTyp + " " + artikel.getName() + " " + anzahl + " durchgeführt von: " + user.getNutzerName());
		}
		}
}
