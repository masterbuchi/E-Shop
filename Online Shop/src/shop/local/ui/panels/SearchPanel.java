package shop.local.ui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import shop.local.domain.*;
import shop.local.domain.comparators.ArtikelNameComparator;
import shop.local.domain.exceptions.ArtikelNichtVorhandenException;
import shop.local.ui.werkzeuge.Listener.GUIListener;
import shop.local.valueobjects.Artikel;

public class SearchPanel extends JPanel {

	private JTextField searchTextField;
	private JButton searchButton = null;
	private Shop shop = null;
	private GUIListener guiListener = null;
	private JFrame guiframe;

	// Konstruktor
	public SearchPanel(Shop shop, JFrame guiframe, GUIListener listener) {
		guiListener = listener;
		this.shop = shop;
		this.guiframe = guiframe;
		setUpSearch();
		setUpSearchEvents();

	}

	public void setUpSearch() {
		// setBorder(BorderFactory.createTitledBorder("Suche"));
		this.searchTextField = new JTextField();
		this.searchButton = new JButton("Suche");

		this.setLayout(new MigLayout());

		this.add(searchTextField, "left, width 50%:100%");
		this.add(searchButton);
		guiframe.getRootPane().setDefaultButton(searchButton);

	}

	private void setUpSearchEvents() {
		searchButton.addActionListener(new SearchListener());
	}

	// Lokale Klasse f�r Reaktion auf Such-Klick
	class SearchListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			Map<Artikel, Integer> ergebnis = new TreeMap<Artikel, Integer>(new ArtikelNameComparator());
			if (ae.getSource().equals(searchButton)) {
				String suchbegriff = searchTextField.getText();

				if (suchbegriff.isEmpty()) {
					ergebnis = shop.alleArtikelAusgeben();
					guiListener.suchErgebnisse(ergebnis);

				} else {
					try {
						ergebnis = shop.sucheNachWarenTeil(suchbegriff);
						guiListener.suchErgebnisse(ergebnis);
					} catch (ArtikelNichtVorhandenException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}
	}

	public String getTextSuche() {
		return searchTextField.getText();
	}

	public void textFeldLoeschen() {
		searchTextField.setText("");
	}

}