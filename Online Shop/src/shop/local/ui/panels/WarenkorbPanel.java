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
import shop.local.ui.tables.WarenkorbTable;
import shop.local.ui.werkzeuge.WarenkorbTableModel;
import shop.local.ui.werkzeuge.Listener.GUIListener;
import shop.local.valueobjects.Artikel;
import shop.local.valueobjects.Nutzer;

public class WarenkorbPanel extends JPanel {

	private JButton kaufenButton = null;
	private JButton zurueckButton = null;
	private JButton leerenButton = null;
	WarenkorbTable warenkorbTable;
	GUIListener guiListener;
	Map<Artikel, Integer> map;
	Nutzer nutzer;
	UUID uuid;
	Shop shop;

	public WarenkorbPanel(Shop shop, Map<Artikel, Integer> map, GUIListener guiListener, UUID uuid) {
		this.shop = shop;
		this.guiListener = guiListener;
		this.map = map;
		this.uuid = uuid;
//		nutzer = shop.getUser(uuid);
		setUpWarenkorb();
		setUpWarenkorbEvents();

	}

	public void setUpWarenkorb() {

		this.setLayout(new MigLayout());

		if (map.isEmpty()) {

			JLabel hinweisLabel = new JLabel("Ihr Warenkorb ist leer.");
			this.add(hinweisLabel, "wrap");

			zurueckButton = new JButton("Zurück");
			this.add(zurueckButton);

		} else {
			JLabel hinweisLabel = new JLabel("Zum Ändern der Details bitte auf das Produkt klicken.");
			this.add(hinweisLabel, "wrap");

			warenkorbTable = new WarenkorbTable("nicht kaufen", map, guiListener);
			this.add(new JScrollPane(warenkorbTable), "wrap");
			

			kaufenButton = new JButton("Waren aus Warenkorb kaufen");
			this.add(kaufenButton,"push, al right, wrap");
			
			leerenButton = new JButton("Warenkorb leeren");
			this.add(leerenButton, "push, al right");
			
		}
	}

	private void setUpWarenkorbEvents() {
		if (map.isEmpty()) {
			zurueckButton.addActionListener(new ZurueckButtonListener());
		} else {
			kaufenButton.addActionListener(new KaufenButtonListener());
			leerenButton.addActionListener(new LeerenButtonListener());
		}
	}
	
	public void refreshWarenkorbTable( Map<Artikel, Integer> map) {
		warenkorbTable.setWarenkorbNeu(map);
	}

	// Lokale Klasse fÃ¼r Reaktion auf Kaufen-Klick
	class KaufenButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource().equals(kaufenButton)) {
				guiListener.kaufenPanel();
			}
		}
	}
	
	// Lokale Klasse fÃ¼r Reaktion auf Kaufen-Klick
		class LeerenButtonListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent ae) {
				if (ae.getSource().equals(leerenButton)) {
					shop.warenKorbLeeren(uuid);
					guiListener.warenkorbPanel();
				
				}
			}
		}

	// Lokale Klasse fÃ¼r Reaktion auf Zurueck-Klick
	class ZurueckButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource().equals(zurueckButton)) {
				guiListener.kundeMainPanel(uuid, nutzer);
			}
		}
	}

}
