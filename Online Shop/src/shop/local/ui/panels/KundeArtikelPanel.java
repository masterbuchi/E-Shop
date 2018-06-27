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
import shop.local.domain.exceptions.ArtikelBereitsVorhandenException;
import shop.local.domain.exceptions.ArtikelNichtVorhandenException;
import shop.local.domain.exceptions.NichtGenugArtikelException;
import shop.local.ui.tables.KundeArtikelTable;
import shop.local.ui.werkzeuge.ArtikelListeTableModel;
import shop.local.ui.werkzeuge.Listener.GUIListener;
import shop.local.valueobjects.Artikel;
import shop.local.valueobjects.Nutzer;

public class KundeArtikelPanel extends JPanel {
	JButton hinzufuegenButton;
	JLabel textWarnungLabel = new JLabel();
	KundeArtikelTable kundeArtikelTable;
	GUIListener guiListener;
	Map<Artikel, Integer> artikelMap;
	Map<Artikel, Integer> warenkorbMap;
	Nutzer nutzer;
	UUID uuid;
	Shop shop;
	

	public KundeArtikelPanel(Shop shop, Map<Artikel, Integer> artikelMap, GUIListener guiListener, UUID uuid) {
		this.setLayout(new MigLayout("left, center"));
		this.artikelMap = artikelMap;
		this.uuid = uuid;
		this.guiListener = guiListener;
		this.shop = shop;
		nutzer = shop.getUser(uuid);
		warenkorbMap =shop.getWarenkorb(uuid).getMap();
		
		setUpKundenArtikelPanel();
		setUpKundenArtikelPanelEvents();
	}

	public void setUpKundenArtikelPanel() {

		JLabel hinweisLabel = new JLabel(
				"Zum Einfügen von Waren in ihren Warenkorb bitte in der passenden Zeile die Menge eingeben.");
		this.add(hinweisLabel, "wrap");
		
		System.out.println(warenkorbMap);

		kundeArtikelTable = new KundeArtikelTable(artikelMap, guiListener, warenkorbMap);
		this.add(new JScrollPane(kundeArtikelTable), "width 50%:100%, height 30%:50%, wrap");

		hinzufuegenButton = new JButton("Dem Warenkorb hinzufügen");
		this.add(hinzufuegenButton, "wrap");
		this.add(textWarnungLabel);

	}

	private void setUpKundenArtikelPanelEvents() {
		hinzufuegenButton.addActionListener(new HinzufuegenButtonListener());
	}
	
	
	public void refreshKundeArtikelTable( Map<Artikel, Integer> map) {
		kundeArtikelTable.setKundeArtikelNeu(map);
	}

	// Lokale Klasse fÃ¼r Reaktion auf Hinzufuegen-Klick
	class HinzufuegenButtonListener implements ActionListener {

		String name;
		boolean error = false;
		String anzahl;
		int anz = 0;

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource().equals(hinzufuegenButton)) {

				if (kundeArtikelTable.isEditing())
					kundeArtikelTable.getCellEditor().stopCellEditing();
				
				for (int i = 0; i < kundeArtikelTable.getRowCount(); i++) {

					
					if (kundeArtikelTable.getValueAt(i, 3) != null) {
						try {
							try {
								name = (String) kundeArtikelTable.getValueAt(i, 0);

								anzahl = (String) kundeArtikelTable.getValueAt(i, 3);
								anz = Integer.parseInt(anzahl);
								if (anz <= 0) {
									textWarnungLabel.setText(
											"Bitte geben Sie ausschließlich positive Werte für die Anzahl an.");
									error = true;
								}
								// Hinzuzufügende Ware wird übergeben
								if (!error) {
									shop.wareInWarenkorb(uuid, name, anz);
									textWarnungLabel.setText("Ware wurde erfolgreich dem Warenkorb hinzugefügt.");
								}

							} catch (ArtikelBereitsVorhandenException e) {
								shop.wareAnzahlAendern(uuid, name, anz);
							}

						} catch (NumberFormatException n) {
							textWarnungLabel.setText(
									"Fehlerhafte Eingabe, bitte beginnen Sie erneut. Für weitere Hilfe, richten Sie sich bitte an die Kundenberatung.");
						} catch (ArtikelNichtVorhandenException | NichtGenugArtikelException e1) {
							textWarnungLabel.setText(
									"Von einem oder mehreren Produkten sind derzeit nicht genug Artikel vorhanden. Schauen Sie später erneut vorbei.");
						}
					}
				}

			}
		}
	}

}
