package shop.local.ui.werkzeuge;

import java.util.Map;
import java.util.UUID;

import shop.local.domain.exceptions.ArtikelNichtVorhandenException;
import shop.local.valueobjects.Artikel;
import shop.local.valueobjects.Nutzer;

public class Listener {
	
	public interface GUIListener {
		public void regPanel();
		public void mainPanel();
		public void anmPanel();
		public void suchErgebnisse(Map<Artikel, Integer> map);
		public void mitarbeiterMainPanel(UUID uuid, Nutzer nutzer);
		public void kundeMainPanel(UUID uuid, Nutzer nutzer);
		public void warenkorbPanel ();
		public void logoutPanel ();
		public void KundeDetailAnzeigenPanel (String string) throws ArtikelNichtVorhandenException;
		public void kaufenPanel();
		public void rechnungsPanel(Map<Artikel, Integer> warenkorbZwischenspeicher);
		public void refreshWarenkorb(Map<Artikel, Integer> map);
		public void MitarbeiterDetailAnzeigenPanel(String string) throws ArtikelNichtVorhandenException;
		public void refreshMitarbeiterArtikel(Map<Artikel, Integer> alleArtikelAusgeben);
	}
	
}
