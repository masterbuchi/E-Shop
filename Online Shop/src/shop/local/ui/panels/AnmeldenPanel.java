
package shop.local.ui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import shop.local.domain.Shop;
import shop.local.ui.werkzeuge.Listener.GUIListener;
import shop.local.valueobjects.Nutzer;

public class AnmeldenPanel extends JPanel {

	private Shop shop;
	private GUIListener guiListener = null;
	private JFrame guiframe;


	// Konstruktor
	public AnmeldenPanel(Shop shop, GUIListener guiListener, JFrame guiframe) {

		this.guiListener = guiListener;
		this.shop = shop;
		this.guiframe = guiframe;

		setUpUI();
		setUpEvents();
		
	}

	JLabel anmeldenNutzername = new JLabel("Nutzername");
	JLabel anmeldenPasswort = new JLabel("Passwort");
	JLabel textWarnung = new JLabel("");
	JTextField anmeldenNutzernameText = new JTextField(20);
	JTextField anmeldenPasswortText = new JTextField(20);
	JButton anmeldeButton = new JButton("Anmelden");
	JButton zurueckButton = new JButton("Zurück");
	
	

	public void setUpUI() {
		setBorder(BorderFactory.createTitledBorder("Anmelden"));

		this.setLayout(new MigLayout("flowy, center, center"));

		this.add(anmeldenNutzername);
		this.add(anmeldenNutzernameText);
		this.add(anmeldenPasswort);
		this.add(anmeldenPasswortText);

		JPanel buttons = new JPanel();
		buttons.setLayout(new MigLayout());
		buttons.add(zurueckButton);
		buttons.add(anmeldeButton);

		this.add(buttons);
		this.add(textWarnung);
		guiframe.getRootPane().setDefaultButton(anmeldeButton);
	}

	private void setUpEvents() {
		anmeldeButton.addActionListener(new AnmeldeListener());
		zurueckButton.addActionListener(new ZurueckListener());

	}

	class ZurueckListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource().equals(zurueckButton)) {
				guiListener.mainPanel();
			}
		}
	}

	class AnmeldeListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {

			if (ae.getSource().equals(anmeldeButton)) {

				String nutzerName;
				String passwort;
				UUID aktuelleUUID;

				nutzerName = anmeldenNutzernameText.getText();
				passwort = anmeldenPasswortText.getText();

				aktuelleUUID = shop.login(nutzerName, passwort);

				if (aktuelleUUID == null) {
					// Kein erfolgreicher Login
					textWarnung.setText("Fehlerhafte Eingabe! Bitte überprüfen Sie Passwort und Benutzername.");

				} else {

					// Login Überprüfen ob Mitarbeiter oder Kunde

					Nutzer nutzer;

					// wenn es ein Mitarbeiter ist, der sich eingeloggt hat
					if (shop.mitarbeiterOderKunde(aktuelleUUID) == 1) {
						nutzer = shop.getUser(aktuelleUUID);

						guiListener.mitarbeiterMainPanel(aktuelleUUID, nutzer);

						// wenn es ein Kunde ist, der sich eingeloggt hat
					} else if (shop.mitarbeiterOderKunde(aktuelleUUID) == 2) {
						nutzer = shop.getUser(aktuelleUUID);

						guiListener.kundeMainPanel(aktuelleUUID, nutzer);
					}
				}
			}
		}
	}
}