package shop.local.ui;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;
import shop.local.domain.Shop;
import shop.local.domain.exceptions.ArtikelNichtVorhandenException;
import shop.local.ui.panels.AnmeldenPanel;
import shop.local.ui.panels.ButtonsPanel;
import shop.local.ui.panels.KaufenPanel;
import shop.local.ui.panels.KundeArtikelPanel;
import shop.local.ui.panels.KundeDetailPanel;
import shop.local.ui.panels.MitarbeiterArtikelPanel;
import shop.local.ui.panels.MitarbeiterDetailPanel;
import shop.local.ui.panels.RechnungsPanel;
import shop.local.ui.panels.RegistrierenPanel;
import shop.local.ui.panels.SearchPanel;
import shop.local.ui.panels.WarenkorbPanel;
import shop.local.ui.tables.ArtikelTable;
import shop.local.ui.werkzeuge.Listener.GUIListener;
import shop.local.valueobjects.Artikel;
import shop.local.valueobjects.Nutzer;

public class ClientGUI extends JFrame implements GUIListener {

	// Deklaration
	private Shop shop;
	private boolean login = false;
	private boolean mitarbeiter = false;
	private boolean kunde = false;
	private UUID aktuelleUUID;
	private Nutzer nutzer;
	private ArtikelTable artikelTable;
	private WarenkorbPanel warenkorbPanel;
	private KundeDetailPanel kundeDetailPanel;
	private MitarbeiterDetailPanel mitarbeiterDetailPanel;
	private KundeArtikelPanel kundeArtikelPanel;
	private MitarbeiterArtikelPanel mitarbeiterArtikelPanel;
	private KaufenPanel kaufenPanel;
	private RechnungsPanel rechnungsPanel;

	JPanel header = new JPanel();
	JPanel content = new JPanel();
	SearchPanel searchPanel;

	int speicher = 0;

	public ClientGUI(String datei) throws IOException {
		shop = new Shop(datei);

		
		//Java bietet durch einen Shutdown-Hook die Möglichkeit, das Abbruchsignal zu erkennen und sauber Ressourcen freizugeben.
		// Speichern vor dem Schließen
		// Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
		// public void run() {
		// try {
		// shop.speichern();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		// }, "Shutdown-thread"));

		start();

		// Stream-Objekt fuer Texteingabe ueber Konsolenfenster erzeugen
		this.setLayout(new MigLayout());

		// Content
		this.add(content, "width 100%, height 100%");
		content.setLayout(new MigLayout("debug, al center center"));

		// Header
		this.add(header, "dock north, span");
		header.setLayout(new MigLayout());

		mainPanel();

		this.pack();
		this.setVisible(true);
		this.setSize(800, 600);

	}

	private void start() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}

	public static void main(String[] args) {

		// Swing-UI auf dem GUI-Thread initialisieren
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					new ClientGUI("Shop");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	public void regPanel() {

		String suchinhalt = searchPanel.getTextSuche();
		header.removeAll();
		content.removeAll();
		this.revalidate();
		RegistrierenPanel registrierenPanel = new RegistrierenPanel(suchinhalt, shop, this, this);
		content.add(registrierenPanel, "width 50%:100%, height 50%:100%");
		this.revalidate();
	}

	@Override
	public void mainPanel() {
		header.removeAll();
		content.removeAll();
		this.revalidate();
		searchPanel = new SearchPanel(shop, this, this);
		header.add(searchPanel, "width 100%");
		header.add(new ButtonsPanel(shop, "Main", aktuelleUUID, this));
		artikelTable = new ArtikelTable(shop.alleArtikelAusgeben());
		content.add(new JScrollPane(artikelTable), "width 50%:100%, height 50%:80%");
		getRootPane().setBorder(BorderFactory.createTitledBorder("The Shop - Der weltweit beliebteste Versandhändler"));
		this.revalidate();

	}

	@Override
	public void suchErgebnisse(Map<Artikel, Integer> map) {
		if (!login) {
			artikelTable.setArtikellisteNeu(map);
		} else {

			if (kunde) {
				kundeArtikelPanel.refreshKundeArtikelTable(map);
			}
			// Derzeit noch für Mitarbeiter
			if (mitarbeiter) {
				artikelTable.setArtikellisteNeu(map);
			}

		}
		searchPanel.textFeldLoeschen();
		this.revalidate();
	}

	@Override
	public void refreshWarenkorb(Map<Artikel, Integer> map) {
		warenkorbPanel.refreshWarenkorbTable(map);
	}

	@Override
	public void refreshMitarbeiterArtikel(Map<Artikel, Integer> map) {
		try {
			mitarbeiterArtikelPanel.refreshMitarbeiterArtikelTable(map);
		} catch (ArtikelNichtVorhandenException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void anmPanel() {
		header.removeAll();
		content.removeAll();
		this.revalidate();
		AnmeldenPanel anmeldenPanel = new AnmeldenPanel(shop, this, this);
		content.add(anmeldenPanel, "width 50%:100%, height 50%:100%");
		this.revalidate();
	}

	@Override
	public void kundeMainPanel(UUID uuid, Nutzer nutzer) {
		this.login = true;
		this.aktuelleUUID = uuid;
		if (nutzer != null) {
			this.nutzer = nutzer;
		}
		kunde = true;
		getRootPane().setBorder(BorderFactory
				.createTitledBorder("Eingeloggt als " + this.nutzer.getVorName() + " " + this.nutzer.getNachName()));
		header.removeAll();
		content.removeAll();
		this.revalidate();

		searchPanel = new SearchPanel(shop, this, this);
		header.add(searchPanel, "width 100%");
		header.add(new ButtonsPanel(shop, "Kunde", aktuelleUUID, this), "push, al right, wrap");

		kundeArtikelPanel = new KundeArtikelPanel(shop, shop.alleArtikelAusgeben(), this, aktuelleUUID);
		content.add(kundeArtikelPanel, "width 50%:100%, height 50%:100%");

		this.revalidate();
	}

	@Override
	public void mitarbeiterMainPanel(UUID uuid, Nutzer nutzer) {
		login = true;
		aktuelleUUID = uuid;
		if (nutzer != null) {
			this.nutzer = nutzer;
		}
		mitarbeiter = true;

		getRootPane().setBorder(BorderFactory.createTitledBorder(
				"Eingeloggt als Mitarbeiter " + this.nutzer.getVorName() + " " + this.nutzer.getNachName()));

		header.removeAll();
		content.removeAll();
		this.revalidate();
		searchPanel = new SearchPanel(shop, this, this);
		header.add(searchPanel, "width 100%");
		header.add(new ButtonsPanel(shop, "Mitarbeiter", aktuelleUUID, this), "push, al right, wrap");

		mitarbeiterArtikelPanel = new MitarbeiterArtikelPanel(shop, shop.alleArtikelAusgeben(), this, aktuelleUUID);
		content.add(mitarbeiterArtikelPanel, "width 50%:100%, height 30%:70%");
		this.revalidate();
	}

	@Override
	public void warenkorbPanel() {
		header.removeAll();
		content.removeAll();
		this.revalidate();
		header.add(new ButtonsPanel(shop, "Warenkorb", aktuelleUUID, this), "push, al right, wrap");
		warenkorbPanel = new WarenkorbPanel(shop, shop.getWarenkorb(aktuelleUUID).getMap(), this, aktuelleUUID);
		content.add(warenkorbPanel);

		getRootPane().setBorder(BorderFactory
				.createTitledBorder("Eingeloggt als " + this.nutzer.getVorName() + " " + this.nutzer.getNachName()));
		this.revalidate();
	}

	@Override
	public void kaufenPanel() {
		header.removeAll();
		content.removeAll();
		this.revalidate();
		header.add(new ButtonsPanel(shop, "Warenkorb", aktuelleUUID, this), "push, al right, wrap");

		kaufenPanel = new KaufenPanel(shop, shop.getWarenkorb(aktuelleUUID).getMap(), this, aktuelleUUID);
		content.add(kaufenPanel);
		this.revalidate();
	}

	public void rechnungsPanel(Map<Artikel, Integer> warenkorbZwischenspeicher) {
		header.removeAll();
		content.removeAll();
		this.revalidate();
		header.add(new ButtonsPanel(shop, "Warenkorb", aktuelleUUID, this), "push, al right, wrap");
		rechnungsPanel = new RechnungsPanel(warenkorbZwischenspeicher, shop, this, aktuelleUUID);
		content.add(rechnungsPanel);
		this.revalidate();
	}

	@Override
	public void logoutPanel() {
		this.login = false;
		shop.uuidLoeschen(aktuelleUUID);
		this.aktuelleUUID = null;
		this.nutzer = null;
		this.kunde = false;
		this.mitarbeiter = false;
		mainPanel();
		this.revalidate();
	}

	@Override
	public void KundeDetailAnzeigenPanel(String name) throws ArtikelNichtVorhandenException {
		if (kundeDetailPanel != null) {
			content.remove(kundeDetailPanel);
		}
		kundeDetailPanel = new KundeDetailPanel(shop, aktuelleUUID, name, this);
		content.add(kundeDetailPanel);
		this.revalidate();
	}

	@Override
	public void MitarbeiterDetailAnzeigenPanel(String name) throws ArtikelNichtVorhandenException {
		if (mitarbeiterDetailPanel != null) {
			mitarbeiterArtikelPanel.remove(mitarbeiterDetailPanel);
			this.revalidate();
		}
		if (name != null) {
			mitarbeiterDetailPanel = new MitarbeiterDetailPanel(null, shop, aktuelleUUID, name, this);
			mitarbeiterArtikelPanel.add(mitarbeiterDetailPanel);
		} else {
			mitarbeiterDetailPanel = new MitarbeiterDetailPanel("neu", shop, aktuelleUUID, name, this);
			mitarbeiterArtikelPanel.add(mitarbeiterDetailPanel);
		}
		this.revalidate();

	}

}
