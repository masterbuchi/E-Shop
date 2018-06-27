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

	public MitarbeiterDetailPanel(Shop shop, UUID uuid, String name, GUIListener guiListener)
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
	JLabel verfuegbarMengeLabel = new JLabel();
	JLabel gelistetLabel = new JLabel();
	JCheckBox gelistetCheckBox = new JCheckBox();
	JLabel aendernLabel = new JLabel();
	JTextField aendernTextField = new JTextField(5);

	JButton aktualisierenButton = new JButton();
	JButton loeschenButton = new JButton();
	JLabel textWarnungLabel = new JLabel();

	public void setUpUI() throws ArtikelNichtVorhandenException {
		aktuellerArtikel = shop.sucheNachWare(this.name);

		setBorder(BorderFactory.createTitledBorder("Details"));

		this.setLayout(new MigLayout());

		nameLabel.setText("Produkt: ");
		nameInhaltLabel.setText(aktuellerArtikel.getName());
		verfuegbarLabel.setText("Verfügbare Menge");
		gelistetLabel.setText("Gelistet");
		aendernLabel.setText("Ergänzen/Entfernen");
		// aendernTextField.setText("+/-");

		gelistetCheckBox.setSelected(aktuellerArtikel.getGelistet());

		verfuegbarMengeLabel.setText(String.valueOf(shop.alleArtikelAusgeben().get(aktuellerArtikel)));

		aktualisierenButton.setText("Aktualisieren");
		loeschenButton.setText("Artikel enfernen");

		// Labels hinzufügen
		this.add(nameLabel);
		this.add(nameInhaltLabel, "wrap");
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

	class AktualisierenListener implements ActionListener {
		boolean abbruch = false;

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource().equals(aktualisierenButton)) {
				// Hier kommt die Aktualisierung der Werte in der Map hin
				try {
					String anz = aendernTextField.getText();
					if (!anz.equals("")) {
						neueAnzahl = Integer.parseInt(anz);
					}
					// Die zu ändernde Ware wird übergeben
					if (abbruch == false) {
						shop.anzahlAendern(name, neueAnzahl, uuid);
						shop.setArtikelGelistet(name, gelistetCheckBox.isSelected());
						guiListener.refreshMitarbeiterArtikel(shop.alleArtikelAusgeben());
					}
					// Bei fehlerhafter Eingabe von Eingaben (falsche Zahl, Kommazahl etc.)
				} catch (NumberFormatException n) {
					textWarnungLabel.setText(
							"Fehlerhafte Eingabe, bitte beginnen Sie erneut. Für weitere Hilfe, richten Sie sich bitte an die Kundenberatung.");
					// Falls der Artikel nicht im Lager vorhanden ist.
				}
				// catch (ArtikelNichtVorhandenException f) {
				// textWarnungLabel.setText("Dieser Artikel ist nicht mehr in unserem Bestand
				// vorhanden");
				// // Falls nicht genug Artikel im Lager sind.
				// } catch (NichtGenugArtikelException e) {
				// textWarnungLabel
				// .setText("Diese Menge ist nicht (mehr) vorhanden. Bitte wählen sie eine
				// andere Menge");
				// }
				catch (ArtikelNichtVorhandenException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NichtGenugArtikelException e) {
					textWarnungLabel.setText("Für diese Änderung sind nicht genug Artikel im Lager.");
				}

			}
		}
	}

	// Lokale Klasse für die Reaktion auf Löschen-Klick
	class LoeschenButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
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
