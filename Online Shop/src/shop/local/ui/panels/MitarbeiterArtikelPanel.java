package shop.local.ui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;
import shop.local.domain.Shop;
import shop.local.domain.exceptions.ArtikelNichtVorhandenException;
import shop.local.ui.tables.MitarbeiterTable;
import shop.local.ui.werkzeuge.Listener.GUIListener;
import shop.local.valueobjects.Artikel;
import shop.local.valueobjects.Nutzer;

public class MitarbeiterArtikelPanel extends JPanel {
	JButton neuButton;
	JLabel textWarnungLabel = new JLabel();
	MitarbeiterTable mitarbeiterTable;
	GUIListener guiListener;
	Map<Artikel, Integer> artikelMap;
	Nutzer nutzer;
	UUID uuid;
	Shop shop;

	public MitarbeiterArtikelPanel(Shop shop, Map<Artikel, Integer> artikelMap, GUIListener guiListener, UUID uuid) {
		this.setLayout(new MigLayout("left, center"));
		this.artikelMap = artikelMap;
		this.uuid = uuid;
		this.guiListener = guiListener;
		this.shop = shop;
		nutzer = shop.getUser(uuid);

		setUpMitarbeiterArtikelPanel();
		setUpMitarbeiterArtikelPanelEvents();
	}

	public void setUpMitarbeiterArtikelPanel() {

		JLabel hinweisLabel = new JLabel("Zur Ergänzung der Details bitte den Artikel in der Liste auswählen");
		this.add(hinweisLabel, "wrap");

		mitarbeiterTable = new MitarbeiterTable(artikelMap, guiListener);
		this.add(new JScrollPane(mitarbeiterTable), "width 50%:100%, height 30%:50%, wrap");

		neuButton = new JButton("Neuer Artikel");
		this.add(neuButton, "wrap");
		this.add(textWarnungLabel);

	}

	private void setUpMitarbeiterArtikelPanelEvents() {
		neuButton.addActionListener(new NeuButtonListener());
	}

	public void refreshMitarbeiterArtikelTable(Map<Artikel, Integer> map) throws ArtikelNichtVorhandenException {
		mitarbeiterTable.setMitarbeiterArtikelNeu(map);
	}

	// Lokale Klasse für Reaktion auf Artikel-Bearbeiten-Klick
	class NeuButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource().equals(neuButton)) {

			}
		}
	}

}
