package shop.local.ui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import shop.local.domain.Shop;
import shop.local.domain.exceptions.KundeBereitsVorhandenException;
import shop.local.domain.exceptions.MitarbeiterBereitsVorhandenException;
import shop.local.ui.Listener.MainListener;
import shop.local.ui.Listener.RegListener;
import shop.local.valueobjects.Artikel;
import shop.local.valueobjects.Kunde;
import shop.local.valueobjects.Mitarbeiter;

public class RegistrierenPanel extends JPanel {

	private String mitarbeiterOderKunde;
	private Shop shop;
	private MainListener mainListener = null;


	// Konstruktor
	public RegistrierenPanel(String mitarbeiter, Shop shop, MainListener listener) {
		this.mainListener = listener;
		this.shop = shop;
		if (mitarbeiter.equals("Mitarbeiter")) {
			mitarbeiterOderKunde = "Mitarbeiter";
			setUpUIM();
			setUpEvents();
		} else {
			mitarbeiterOderKunde = "Kunde";
			setUpUIK();
			setUpEvents();
		}
	}

	// Attribute
	JLabel nutzername = new JLabel("Nutzername");
	JLabel vorname = new JLabel("Vorname");
	JLabel nachname = new JLabel("Nachname");
	JLabel passwort = new JLabel("Passwort");
	JLabel strasseHausnr = new JLabel("Straße, Hausnummer");
	JLabel plzOrt = new JLabel("Posteitzahl, Wohnort");
	JLabel fehler = new JLabel("");

	JTextField nutzernameText = new JTextField(30);
	JTextField vornameText = new JTextField(30);
	JTextField nachnameText = new JTextField(30);
	JTextField passwortText = new JTextField(30);
	JTextField strasseText = new JTextField(25);
	JTextField hausnrText = new JTextField(4);
	JTextField plzText = new JTextField(8);
	JTextField ortText = new JTextField(21);
	JButton registrierButton = new JButton("Registrieren");
	JButton zurueckButton = new JButton("Zurück");
	JLabel space = new JLabel(" ");
	JButton spaceB = new JButton(" ");

	// Layout Mitarbeiter
	public void setUpUIM() {
		setBorder(BorderFactory.createTitledBorder("Registrieren"));

		this.setLayout(new MigLayout("center, center", "[]30[]"));

		this.add(vorname);
		this.add(nutzername, "wrap");
		this.add(vornameText);
		this.add(nutzernameText, "wrap");
		this.add(nachname);
		this.add(passwort, "wrap");
		this.add(nachnameText);
		this.add(passwortText, "wrap");
		this.add(space, "span");
		this.add(fehler, "span");
		this.add(zurueckButton);
		this.add(registrierButton);

	}

	private void setUpEvents() {
		registrierButton.addActionListener(new RegistrierListener());
		zurueckButton.addActionListener(new ZurueckListener());
	}

	// Layout kunde
	public void setUpUIK() {
		setBorder(BorderFactory.createTitledBorder("Registrieren"));

		this.setLayout(new MigLayout("center, center", "[][]30[][]"));
		this.add(vorname, "span 2");
		this.add(nachname, "wrap, span 2");
		this.add(vornameText, "span 2");
		this.add(nachnameText, "wrap, span 2");

		this.add(nutzername, "span 2");
		this.add(passwort, "wrap, span 2");
		this.add(nutzernameText, "span 2");
		this.add(passwortText, "wrap, span 2");
		this.add(strasseHausnr, "span 2");
		this.add(plzOrt, "wrap");
		this.add(strasseText);
		this.add(hausnrText);
		this.add(plzText, "split 2");
		this.add(ortText);

		this.add(space, "span");
		this.add(fehler, "span");
		this.add(zurueckButton, "span 2");
		this.add(registrierButton, "span 2");

	}

	class ZurueckListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource().equals(zurueckButton)) {
				mainListener.mainPanel();
			}
		}
	}

	// Lokale Klasse fÃ¼r Reaktion auf Registrieren-Klick
	class RegistrierListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource().equals(registrierButton)) {

				boolean erfolgreich = true;
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

				if (mitarbeiterOderKunde.equals("Mitarbeiter")) {
					try {

						nutzerName = nutzernameText.getText();
						vorName = vornameText.getText();
						nachName = nachnameText.getText();
						passwort = passwortText.getText();
						// Mitarbeiterinformationen wird an Shop zum Einfügen übergeben
						try {
							shop.mitarbeiterHinzufuegen(nutzerName, vorName, nachName, passwort);

							// falls die Fehlermeldung für einen doppelten Namen geworfen wird
						} catch (MitarbeiterBereitsVorhandenException e) {
							erfolgreich = false;
							// Bei gleichem Mitarbeiternamen:
							fehler.setText("Ein Mitarbeiter mit dem Benutzernamen " + e.getMitarbeiter().getNutzerName()
									+ " ist bereits vorhanden. Bitte wählen sie einen anderen Benutzernamen.");
						}
						// Bei fehlerhafter Eingabe von Eingaben (falsche Zahl, Kommazahl etc.)
					} catch (NumberFormatException n) {
						erfolgreich = false;
						fehler.setText(
								"Fehlerhafte Eingabe, bitte beginnen Sie erneut. Für weitere Hilfe, richten Sie sich bitte an die Kundenberatung.");
					}

				} else {
					try {
						nutzerName = nutzernameText.getText();
						vorName = vornameText.getText();
						nachName = nachnameText.getText();
						passwort = passwortText.getText();

						strasse = strasseText.getText() + hausnrText.getText();
						postleitzahl = plzText.getText();
						plz = Integer.parseInt(postleitzahl);
						wohnort = ortText.getText();

						// Kundeninformationen wird an Shop zum Einfügen übergeben
						try {
							shop.kundeHinzufuegen(nutzerName, vorName, nachName, passwort, strasse, plz, wohnort);
							// falls die Fehlermeldung für einen doppelten Namen geworfen wird
						} catch (KundeBereitsVorhandenException e) {
							erfolgreich = false;
							// Bei gleichem Kundennamen:
							fehler.setText("Ein Kunde mit dem Benutzernamen " + e.getKunde().getNutzerName()
									+ " ist bereits vorhanden. Bitte wählen sie einen anderen Benutzernamen.");
						}
						// Bei fehlerhafter Eingabe von Eingaben (falsche Zahl, Kommazahl etc.)
					} catch (NumberFormatException n) {
						erfolgreich = false;
						fehler.setText(
								"Fehlerhafte Eingabe, bitte beginnen Sie erneut. Für weitere Hilfe, richten Sie sich bitte an die Kundenberatung.");
					}

				}

 if (erfolgreich) {
	 mainListener.mainPanel();
 }

			}
		}

	}
}