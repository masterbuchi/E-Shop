package shop.local.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


import java.awt.BorderLayout;
import net.miginfocom.swing.MigLayout;
import shop.local.domain.Shop;
import shop.local.domain.exceptions.ArtikelNichtVorhandenException;
import shop.local.domain.exceptions.NutzerNichtVorhandenException;
import shop.local.ui.Listener.RegListener;
import shop.local.ui.Listener.SuchListener;
import shop.local.ui.Listener.MainListener;
import shop.local.ui.panels.ArtikellistePanel;
import shop.local.ui.panels.ButtonsPanel;
import shop.local.ui.panels.RegistrierenPanel;
import shop.local.ui.panels.SearchPanel;
import shop.local.ui.panels.WarenkorbPanel;
import shop.local.ui.werkzeuge.ButtonColumn;
import shop.local.valueobjects.Artikel;
import shop.local.valueobjects.Nutzer;

public class ClientGUI extends JFrame implements RegListener, MainListener, SuchListener {

	// Deklaration
	private Shop shop;
	private BufferedReader in;
	private boolean login = false;
	private boolean mitarbeiter = false;
	private boolean kunde = false;
	UUID aktuelleUUID;
	private Nutzer nutzer;
	private ArtikellistePanel artikellistePanel;
	
	
	JPanel header = new JPanel();
	JPanel content = new JPanel();
	SearchPanel searchPanel;

	int speicher = 0;

	public ClientGUI(String datei) throws IOException, ArtikelNichtVorhandenException, NutzerNichtVorhandenException {
		shop = new Shop(datei);

		start();


		// Stream-Objekt fuer Texteingabe ueber Konsolenfenster erzeugen
		in = new BufferedReader(new InputStreamReader(System.in));
		this.setLayout(new MigLayout());
		
		//Content
		this.add(content, "width 100%, height 100%");
		content.setLayout(new MigLayout("al center center"));
		
		
		//Header
		this.add(header, "dock north, span");
		header.setLayout(new MigLayout());

		mainPanel();
		
		//this.add(registrierenPanel);
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
					ClientGUI gui = new ClientGUI("Shop");
				} catch (IOException | ArtikelNichtVorhandenException | NutzerNichtVorhandenException e) {
					// TODO Auto-generated catch block
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
		RegistrierenPanel registrierenPanel = new RegistrierenPanel(suchinhalt, shop, this);
		content.add(registrierenPanel, "width 50%:100%, height 50%:100%");
		this.revalidate();
	}
	
	@Override
	public void mainPanel() {
		header.removeAll();
		content.removeAll();
		searchPanel = new SearchPanel(shop, this);
		header.add(searchPanel, "width 100%");
		header.add(new ButtonsPanel("Main", this));
		JTable tabelle = new JTable();
		artikellistePanel = new ArtikellistePanel(shop.alleArtikelAusgeben());
		content.add(new JScrollPane(artikellistePanel), "width 50%:100%, height 50%:100%");
		
		this.revalidate();
		
	}

	@Override
	public void suchErgebnisse(Map<Artikel, Integer> map) {
		artikellistePanel.setArtikellisteNeu(map);
		searchPanel.textFeldLoeschen();
	}
}
