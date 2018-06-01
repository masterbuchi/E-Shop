package shop.local.ui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import shop.local.ui.Listener.RegListener;

public class ButtonsPanel extends JPanel {
	
	
	private JButton anmeldungButton = null;
	private JButton registrierenButton = null;
	private JButton alleArtikelButton = null;
	private JButton logoutButton = null;
	private RegListener regListener = null;

	// Konstruktor
	public ButtonsPanel(String buttons, RegListener listener) {
		regListener = listener;
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
			anmeldungButton.addActionListener(new AnmeldungListener());
			registrierenButton.addActionListener(new RegistrierenListener());
			break;

		case "Warenkorb":
			alleArtikelButton.addActionListener(new AlleArtikelListener());
			logoutButton.addActionListener(new LogoutListener());
			break;
		case "registrieren":
			//To do
		}
	}

	// Lokale Klasse für Reaktion auf Anmeldungs-Klick
	class AnmeldungListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource().equals(anmeldungButton)) {
				// TO-DO
			}
		}
	}

	// Lokale Klasse für Reaktion auf Registrieren-Klick
	class RegistrierenListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource().equals(registrierenButton)) {
				regListener.regPanel();
			}
		}

	}

	// Lokale Klasse für Reaktion auf AlleArtikel-Klick
	class AlleArtikelListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource().equals(alleArtikelButton)) {
				// TO-DO
			}
		}
	}

	// Lokale Klasse für Reaktion auf Logout-Klick
	class LogoutListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource().equals(logoutButton)) {
				// TO-DO
			}
		}
	}
}