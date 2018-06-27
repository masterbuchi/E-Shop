package shop.local.ui.panels;

import java.util.Map;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import net.miginfocom.swing.MigLayout;
import shop.local.domain.Shop;
import shop.local.ui.tables.WarenkorbTable;
import shop.local.ui.werkzeuge.Listener.GUIListener;
import shop.local.valueobjects.Artikel;
import shop.local.valueobjects.Kunde;

public class RechnungsPanel extends JPanel {

	GUIListener guiListener;
	Map<Artikel, Integer> map;
	Kunde nutzer;
	UUID uuid;

	private JLabel lieferzeit;
	private JLabel lieferinfos1;
	WarenkorbTable warenkorbtable;

	JLabel platzhalter = new JLabel("");
	private Shop shop;
	private JLabel adressierung = new JLabel("An: ");
	private JLabel kNamen;
	private JLabel kStrasse;
	private JLabel kWohnort;

	public RechnungsPanel(Map<Artikel, Integer> map, Shop shop, GUIListener guiListener, UUID uuid) {
		this.guiListener = guiListener;
		this.shop = shop;
		this.uuid = uuid;
		this.map = map;

		setUpRechnung();

	}

	public void setUpRechnung() {
		setBorder(BorderFactory.createTitledBorder("Rechnung"));
		this.setLayout(new MigLayout());

		this.lieferinfos1 = new JLabel(
				"Vielen Dank für Ihre Bestellung. Wir werden Sie benachrichtigen, sobald Ihr(e) Artikel versandt wurde(n). ");

		this.add(lieferinfos1, "center, dock north");

		this.add(platzhalter, "wrap");
		this.add(platzhalter, "wrap");
		this.add(platzhalter, "wrap");

		warenkorbtable = new WarenkorbTable("kaufen", map, guiListener);
		this.add(new JScrollPane(warenkorbtable));

		
		//RechnungsadressenPanel
		JPanel rechnungsAdresse = new JPanel();
		rechnungsAdresse.setLayout(new MigLayout());
		this.add(rechnungsAdresse);

		this.nutzer = (Kunde) shop.getUser(uuid);
		this.kNamen = new JLabel(nutzer.getVorName() + " " + nutzer.getNachName());

		this.kStrasse = new JLabel(nutzer.getStrasse());
		this.kWohnort = new JLabel(nutzer.getPlz() + " " + nutzer.getWohnort());

		rechnungsAdresse.add(adressierung, "center, wrap");
		rechnungsAdresse.add(kNamen, "center, wrap");
		rechnungsAdresse.add(kStrasse, "center, wrap");
		rechnungsAdresse.add(kWohnort, "center, wrap");

		this.lieferzeit = new JLabel("Lieferung nach 3 - 4 Werktagen");
		this.add(lieferzeit, "dock south");
	}

}
