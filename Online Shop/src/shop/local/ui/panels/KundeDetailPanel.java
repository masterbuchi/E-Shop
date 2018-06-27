package shop.local.ui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import shop.local.domain.Shop;
import shop.local.domain.exceptions.ArtikelNichtVorhandenException;
import shop.local.domain.exceptions.NichtGenugArtikelException;
import shop.local.ui.panels.RegistrierenPanel.RegistrierListener;
import shop.local.ui.werkzeuge.Listener.GUIListener;
import shop.local.valueobjects.Artikel;

public class KundeDetailPanel extends JPanel {

	String name;
	int anzahl;
	Shop shop;
	UUID uuid;
	Artikel aktuellerArtikel;
	GUIListener guiListener;

	public KundeDetailPanel(Shop shop, UUID uuid, String name, GUIListener guiListener)
			throws ArtikelNichtVorhandenException {
		this.name = name;
		this.shop = shop;
		this.uuid = uuid;
		this.guiListener = guiListener;
		setUpUI();
		setUpEvents();
	}

	JLabel nameLabel = new JLabel();
	JLabel nameInhaltLabel = new JLabel();
	JLabel verfuegbarLabel = new JLabel();
	
	JLabel anzahlLabel = new JLabel();
	JTextField anzahlTextField;
	JLabel verfuegbarMengeLabel = new JLabel();
	JLabel textWarnungLabel = new JLabel();
	JButton aktualisierenButton = new JButton();
	
	JButton loeschenButton = new JButton();

	public void setUpUI() throws ArtikelNichtVorhandenException {
		aktuellerArtikel = shop.sucheNachWare(this.name);

		setBorder(BorderFactory.createTitledBorder("Details"));

		this.setLayout(new MigLayout());

		nameLabel.setText("Produkt: ");
		nameInhaltLabel.setText(aktuellerArtikel.getName());
		verfuegbarLabel.setText("Verfügbare Menge");

		anzahlLabel.setText("Menge: ");
		anzahlTextField = new JTextField(5);
		anzahlTextField.setText(String.valueOf(shop.getWarenkorb(uuid).gibAnzahlZurueck(name)));
		verfuegbarMengeLabel.setText(String.valueOf(shop.gibAnzahl(aktuellerArtikel)));
		

		aktualisierenButton.setText("Aktualisieren");
		loeschenButton.setText("Artikel aus Warenkorb entfernen");

		// Labels hinzufügen
		this.add(nameLabel);
		this.add(nameInhaltLabel);
		this.add(verfuegbarLabel, "wrap");
		this.add(anzahlLabel);
		this.add(anzahlTextField);
		this.add(verfuegbarMengeLabel, "wrap");
		this.add(aktualisierenButton);
		this.add(loeschenButton, "wrap");
		this.add(textWarnungLabel);

	}

	private void setUpEvents() {
		aktualisierenButton.addActionListener(new AktualisierenListener());
		loeschenButton.addActionListener(new LoeschenButtonListener());

	}

	// Lokale Klasse für die Reaktion auf Löschen-Klick
	class LoeschenButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				shop.wareLoeschen(uuid, name);
				guiListener.warenkorbPanel();
			} catch (ArtikelNichtVorhandenException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}

	// Lokale Klasse für die Reaktion auf Aktualisieren-Klick
	class AktualisierenListener implements ActionListener {
		boolean abbruch = false;

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource().equals(aktualisierenButton)) {

				// Hier kommt die Aktualisierung der Werte in der Map hin
				try {
					String anz = anzahlTextField.getText();
					if (!anz.equals("")) {
						anzahl = Integer.parseInt(anz);
						if (anzahl <= 0) {
							textWarnungLabel
									.setText("Bitte geben Sie ausschließlich positive Werte für die Menge an.");
							abbruch = true;
						}
					}

					// Die zu ändernde Ware wird übergeben
					if (abbruch == false) {
						shop.wareAnzahlAendern(uuid, aktuellerArtikel.getName(), anzahl);
						guiListener.refreshWarenkorb(shop.getWarenkorb(uuid).getMap());
						textWarnungLabel.setText("Ihr Warenkorb wurde erfolgreich aktualisiert");
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

}
