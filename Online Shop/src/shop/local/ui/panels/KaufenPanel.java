package shop.local.ui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;
import shop.local.domain.Shop;
import shop.local.domain.comparators.ArtikelNummerComparator;
import shop.local.domain.exceptions.NichtGenugArtikelException;
import shop.local.ui.tables.WarenkorbTable;
import shop.local.ui.werkzeuge.Listener.GUIListener;
import shop.local.valueobjects.Artikel;
import shop.local.valueobjects.Kunde;

public class KaufenPanel extends JPanel {

	private JButton bestellenButton = null;
	private JButton adresseAendernButton = null;
	JLabel textWarnungLabel = new JLabel();
	WarenkorbTable warenkorbtable;
	GUIListener guiListener;
	Map<Artikel, Integer> map;
	Kunde kunde;
	UUID uuid;
	Shop shop;
	private Map<Artikel, Integer> warenkorbZwischenspeicher = new TreeMap<Artikel, Integer>(new ArtikelNummerComparator());;
	

	public KaufenPanel(Shop shop, Map<Artikel, Integer> map, GUIListener guiListener, UUID uuid) {
		this.shop = shop;
		this.guiListener = guiListener;
		this.map = map;
		this.uuid = uuid;
		kunde = (Kunde) shop.getUser(uuid);
		setUpKaufen();
		setUpKaufenEvents();

	}

	public void setUpKaufen() {

		this.setLayout(new MigLayout());

//		JLabel hinweisLabel = new JLabel("Hier steht jetzt was anderes");
//		this.add(hinweisLabel, "wrap");

		warenkorbtable = new WarenkorbTable("kaufen", map, guiListener);
		this.add(new JScrollPane(warenkorbtable));
		
		JPanel informationen = new JPanel();
		informationen.setLayout(new MigLayout());
		this.add(informationen);
		
		
		JLabel anschriftueberschrift = new JLabel("Anschrift:");
		JLabel kNamen = new JLabel(kunde.getVorName() + " " + kunde.getNachName());
		JLabel kStrasse = new JLabel(kunde.getStrasse());
		JLabel kWohnort = new JLabel(kunde.getPlz() + " " + kunde.getWohnort());
		
		informationen.add(anschriftueberschrift, "center, wrap");
		informationen.add(kNamen, "center, wrap");
		informationen.add(kStrasse, "center, wrap");
		informationen.add(kWohnort, "center, wrap");
		
		
		
		adresseAendernButton = new JButton("Rechnungsadresse ändern");
		informationen.add(adresseAendernButton, "wrap");

		bestellenButton = new JButton("Waren aus Warenkorb kaufen");
		informationen.add(bestellenButton);
		
	}

	private void setUpKaufenEvents() {
		
			bestellenButton.addActionListener(new BestellenButtonListener());
			adresseAendernButton.addActionListener(new AdresseAendernButtonListener());
	}

	// Lokale Klasse fuer Reaktion auf Bestellen-Klick
	class BestellenButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource().equals(bestellenButton)) {
				try {
					warenkorbZwischenspeicher.putAll(shop.getWarenkorb(uuid).getMap());
					shop.kaufen(uuid);
				} catch (NichtGenugArtikelException e) {
					textWarnungLabel
					.setText("Die gewünschte Menge eines Produktes ist nicht (mehr) vorhanden. Bitte wählen sie eine andere Menge");
				}
				
				guiListener.rechnungsPanel(warenkorbZwischenspeicher);
			}
		}
	}

	
	// Lokale Klasse fuer Reaktion auf Adresse-aendern-Klick
		class AdresseAendernButtonListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent ae) {
				if (ae.getSource().equals(adresseAendernButton)) {
					// TO-DO
				}
			}
		}

}
