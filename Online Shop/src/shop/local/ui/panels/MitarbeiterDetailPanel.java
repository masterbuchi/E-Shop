package shop.local.ui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import shop.local.domain.Shop;
import shop.local.domain.exceptions.ArtikelBereitsVorhandenException;
import shop.local.domain.exceptions.ArtikelNichtVorhandenException;
import shop.local.domain.exceptions.NichtGenugArtikelException;
import shop.local.ui.panels.KundeDetailPanel.AktualisierenListener;
import shop.local.ui.panels.KundeDetailPanel.LoeschenButtonListener;
import shop.local.ui.werkzeuge.Listener.GUIListener;
import shop.local.valueobjects.Artikel;

public class MitarbeiterDetailPanel extends JPanel {

	String name;
	int neueAnzahl;
	Shop shop;
	UUID uuid;
	Artikel aktuellerArtikel;
	GUIListener guiListener;

	public MitarbeiterDetailPanel(String neu, Shop shop, UUID uuid, String name, GUIListener guiListener)
			throws ArtikelNichtVorhandenException {
		if (neu == null) {
			this.name = name;
			this.shop = shop;
			this.uuid = uuid;
			this.guiListener = guiListener;
			setUpUI();
			setUpEvents();
		} else {
			this.shop = shop;
			this.uuid = uuid;
			this.guiListener = guiListener;
			setUpNeuUI();
			setUpNeuEvents();
		}
	}

	JLabel nameLabel = new JLabel();
	JLabel nameInhaltLabel = new JLabel();
	JTextField nameTextField = new JTextField(8);

	JLabel preisLabel = new JLabel();
	JTextField preisTextField = new JTextField(5);

	JLabel verfuegbarLabel = new JLabel();
	JLabel verfuegbarMengeLabel = new JLabel();
	JTextField verfuegbarTextField = new JTextField(5);

	JLabel massengutLabel = new JLabel();
	JTextField massengutTextField = new JTextField(5);

	JLabel gelistetLabel = new JLabel();
	JCheckBox gelistetCheckBox = new JCheckBox();

	JLabel aendernLabel = new JLabel();
	JTextField aendernTextField = new JTextField(5);

	JButton aktualisierenButton = new JButton();
	JButton hinzufuegenButton = new JButton();
	JButton loeschenButton = new JButton();
	JLabel textWarnungLabel = new JLabel();

	public void setUpUI() throws ArtikelNichtVorhandenException {
		aktuellerArtikel = shop.sucheNachWare(this.name);

		setBorder(BorderFactory.createTitledBorder("Details"));

		this.setLayout(new MigLayout());

		nameLabel.setText("Produkt");
		nameInhaltLabel.setText(aktuellerArtikel.getName());
		preisLabel.setText("Preis");
		String preisText = String.valueOf(aktuellerArtikel.getPreis());
		preisText = preisText.replace(".", ",");
		preisTextField.setText(preisText + " €");
		verfuegbarLabel.setText("Verfügbare Menge");
		gelistetLabel.setText("Gelistet");
		aendernLabel.setText("Ergänzen/Entfernen");

		gelistetCheckBox.setSelected(aktuellerArtikel.getGelistet());

		verfuegbarMengeLabel.setText(String.valueOf(shop.alleArtikelAusgeben().get(aktuellerArtikel)));

		aktualisierenButton.setText("Aktualisieren");
		loeschenButton.setText("Artikel enfernen");

		// Labels hinzufügen
		this.add(nameLabel);
		this.add(nameInhaltLabel, "wrap");
		this.add(preisLabel);
		this.add(preisTextField, "wrap");
		this.add(verfuegbarLabel);
		this.add(verfuegbarMengeLabel, "wrap");
		this.add(aendernLabel);
		this.add(aendernTextField, "wrap");
		this.add(gelistetLabel);
		this.add(gelistetCheckBox, "wrap");
		this.add(aktualisierenButton);
		this.add(loeschenButton, "wrap");
		this.add(textWarnungLabel, "span");

	}

	private void setUpEvents() {
		aktualisierenButton.addActionListener(new AktualisierenListener());
		loeschenButton.addActionListener(new LoeschenButtonListener());
	}

	public void setUpNeuUI() {
		setBorder(BorderFactory.createTitledBorder("Neuer Artikel"));

		this.setLayout(new MigLayout());

		nameLabel.setText("Produkt");
		preisLabel.setText("Preis");
		verfuegbarLabel.setText("Verfügbare Menge");
		massengutLabel.setText("Massengutartikelgröße");
		gelistetLabel.setText("Gelistet");

		hinzufuegenButton.setText("Artikel hinzufügen");

		// Labels hinzufügen
		this.add(nameLabel);
		this.add(nameTextField, "wrap");
		this.add(preisLabel);
		this.add(preisTextField, "wrap");
		this.add(verfuegbarLabel);
		this.add(verfuegbarTextField, "wrap");
		this.add(massengutLabel);
		this.add(massengutTextField, "wrap");
		this.add(gelistetLabel);
		this.add(gelistetCheckBox, "wrap");
		this.add(hinzufuegenButton, "wrap");
		this.add(textWarnungLabel, "span");
	}

	private void setUpNeuEvents() {
		hinzufuegenButton.addActionListener(new HinzufuegenListener());
	}

	class AktualisierenListener implements ActionListener {
		boolean abbruch = false;
		float preis;

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource().equals(aktualisierenButton)) {
				// Hier kommt die Aktualisierung der Werte in der Map hin
				try {
					String anz = aendernTextField.getText();
					if (!anz.equals("")) {
						neueAnzahl = Integer.parseInt(anz);
					}

					String preisString = preisTextField.getText();

					if (!preisString.equals("")) {
						preisString = preisString.replace(",", ".");
						preisString = preisString.replace("€", "");
						preis = Float.parseFloat(preisString);
					}

					// Die zu ändernde Ware wird übergeben
					if (abbruch == false) {
						shop.anzahlAendern(name, neueAnzahl, uuid);
						shop.setArtikelGelistet(name, gelistetCheckBox.isSelected());
						shop.setPreis(name, preis);
						guiListener.refreshMitarbeiterArtikel(shop.alleArtikelAusgeben());
					}
					// Bei fehlerhafter Eingabe von Eingaben (falsche Zahl, Kommazahl etc.)
				} catch (NumberFormatException n) {
					textWarnungLabel.setText(
							"Fehlerhafte Eingabe, bitte beginnen Sie erneut. Für weitere Hilfe, richten Sie sich bitte an die Kundenberatung.");
					// Falls der Artikel nicht im Lager vorhanden ist.
				} catch (ArtikelNichtVorhandenException f) {
					textWarnungLabel.setText("Dieser Artikel ist nicht mehr in unserem Bestand vorhanden");
					// Falls nicht genug Artikel im Lager sind.
				} catch (NichtGenugArtikelException e) {
					textWarnungLabel
							.setText("Diese Menge ist nicht (mehr) vorhanden. Bitte wählen sie eine andere Menge");
				}

			}
		}
	}

	// Lokale Klasse für die Reaktion auf Löschen-Klick
	class LoeschenButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource().equals(loeschenButton)) {
				try {
					shop.artikelLoeschen(name, uuid);
					guiListener.mitarbeiterMainPanel(uuid, null);
				} catch (ArtikelNichtVorhandenException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		}
	}

	class HinzufuegenListener implements ActionListener {
		boolean abbruch = false;
		String name;
		float preis;
		int menge;
		int massengut;
		boolean gelistet;

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource().equals(hinzufuegenButton)) {
				// Hier kommt die Aktualisierung der Werte in der Map hin
				try {
					try {
						name = nameTextField.getText();

						String preisString = preisTextField.getText();
						if (!preisString.equals("")) {
							preisString = preisString.replace(",", ".");
							preisString = preisString.replace("€", "");
							preis = Float.parseFloat(preisString);
						}

						String mengeString = verfuegbarTextField.getText();
						if (!mengeString.equals("")) {
							menge = Integer.parseInt(mengeString);
						}

						String massengutString = massengutTextField.getText();
						if (!massengutString.equals("")) {
							massengut = Integer.parseInt(massengutString);
						}

						gelistet = gelistetCheckBox.isSelected();

						if (preisString.equals("") || mengeString.equals("") || massengutString.equals("")) {
							abbruch = true;
						} else {
							abbruch = false;
						}
						// Die zu ändernde Ware wird übergeben
						if (abbruch == false) {
							shop.artikelEinfuegen(name, preis, menge, gelistet, uuid);
							guiListener.refreshMitarbeiterArtikel(shop.alleArtikelAusgeben());
						} else {
							textWarnungLabel.setText("Bitte füllen Sie alle Pflichtangaben ein.");
						}

						// Falls der Artikel nicht im Lager vorhanden ist.
					} catch (ArtikelBereitsVorhandenException e) {
						try {
							shop.anzahlAendern(name, menge, uuid);
							shop.setArtikelGelistet(name, gelistetCheckBox.isSelected());
							shop.setPreis(name, preis);
							
							guiListener.refreshMitarbeiterArtikel(shop.alleArtikelAusgeben());
						} catch (ArtikelNichtVorhandenException | NichtGenugArtikelException e1) {
							e1.printStackTrace();
						}
					}
					// Bei fehlerhafter Eingabe von Eingaben (falsche Zahl, Kommazahl etc.)
				} catch (NumberFormatException n) {
					textWarnungLabel.setText(
							"Fehlerhafte Eingabe, bitte beginnen Sie erneut. Für weitere Hilfe, richten Sie sich bitte an die Kundenberatung.");

				}
			}
		}
	}
}