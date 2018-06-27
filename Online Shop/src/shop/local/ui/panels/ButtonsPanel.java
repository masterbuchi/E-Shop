package shop.local.ui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import shop.local.domain.Shop;
import shop.local.ui.werkzeuge.Listener.GUIListener;
import shop.local.valueobjects.Nutzer;

public class ButtonsPanel extends JPanel {
	
	
	private JButton anmeldungButton = null;
	private JButton registrierenButton = null;
	private JButton warenkorbButton = null;
	private JButton statistikButton = null;
	private JButton alleArtikelButton = null;
	private JButton logoutButton = null;
	private GUIListener guiListener = null;
	private UUID uuid;
	private Nutzer nutzer;
	private Shop shop;

	// Konstruktor
	public ButtonsPanel(Shop shop, String buttons, UUID uuid, GUIListener guiListener) {
		this.uuid = uuid;
		this.guiListener = guiListener;
		this.shop = shop;
		nutzer = shop.getUser(uuid);
		
		setUpButtons(buttons);
		setUpButtonsEvents(buttons);

	}

	public void setUpButtons(String buttons) {
		switch (buttons) {

		case "Main":

			this.anmeldungButton = new JButton("Anmeldung");
			this.registrierenButton = new JButton("Registrieren");

			this.setLayout(new MigLayout());

			this.add(anmeldungButton);
			this.add(registrierenButton);
			break;
			
		case "Kunde":
			this.warenkorbButton = new JButton("Warenkorb");
			this.logoutButton = new JButton("Logout");
			
			this.setLayout(new MigLayout());
			
			this.add(warenkorbButton);
			this.add(logoutButton);
			
			break;
		
		case "Mitarbeiter":
			this.statistikButton = new JButton("Statistiken");
			this.logoutButton = new JButton("Logout");
			
			this.setLayout(new MigLayout());
			
			this.add(statistikButton);
			this.add(logoutButton);
			
			break;

		case "Warenkorb":

			this.alleArtikelButton = new JButton("Alle Artikel");
			this.logoutButton = new JButton("Logout");

			this.setLayout(new MigLayout());

			this.add(alleArtikelButton);
			this.add(logoutButton);
			break;

		}

	}

	private void setUpButtonsEvents(String buttons) {

		switch (buttons) {

		case "Main":
			anmeldungButton.addActionListener(new AnmeldungButtonListener());
			registrierenButton.addActionListener(new RegistrierenButtonListener());
			break;
		
		case "Kunde":
			warenkorbButton.addActionListener(new WarenkorbButtonListener());
			logoutButton.addActionListener(new LogoutButtonListener());
			break;
			
		case "Mitarbeiter":
			//statistikButton.addActionListener(new WarenkorbButtonListener());
			logoutButton.addActionListener(new LogoutButtonListener());
			break;

		case "Warenkorb":
			alleArtikelButton.addActionListener(new AlleArtikelKundeButtonListener());
			logoutButton.addActionListener(new LogoutButtonListener());
			break;
			
			
		}
	}

	// Lokale Klasse für Reaktion auf Anmeldungs-Klick
	class AnmeldungButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource().equals(anmeldungButton)) {
				guiListener.anmPanel();
			}
		}
	}
	
	// Lokale Klasse f�r Reaktion auf Warenkorb-Klick
		class WarenkorbButtonListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent ae) {
				if (ae.getSource().equals(warenkorbButton)) {
					guiListener.warenkorbPanel();
				}
			}
		}


	// Lokale Klasse für Reaktion auf Registrieren-Klick
	class RegistrierenButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource().equals(registrierenButton)) {
				guiListener.regPanel();
			}
		}

	}

	// Lokale Klasse für Reaktion auf AlleArtikel-Klick
	class AlleArtikelKundeButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource().equals(alleArtikelButton)) {
				guiListener.kundeMainPanel(uuid, nutzer);
			}
		}
	}

	// Lokale Klasse für Reaktion auf Logout-Klick
	class LogoutButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource().equals(logoutButton)) {
				guiListener.logoutPanel();
			}
		}
	}
}