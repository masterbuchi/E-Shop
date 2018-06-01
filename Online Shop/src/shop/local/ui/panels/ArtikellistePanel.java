package shop.local.ui.panels;

import java.util.List;
import java.util.Map;

import javax.swing.JTable;

import shop.local.ui.ArtikelListe;
import shop.local.ui.WarenkorbTableModel;
import shop.local.ui.werkzeuge.ButtonColumn;
import shop.local.valueobjects.Artikel;

public class ArtikellistePanel extends JTable {
	private Map<Artikel, Integer> map;

	public ArtikellistePanel(Map<Artikel, Integer> map) {
		super();
		this.map = map;
		this.setModel(new ArtikelListe(map));
		

	}
	
	public void setArtikellisteNeu(Map<Artikel, Integer> map) {
		ArtikelListe model = (ArtikelListe) getModel();
		model.setArtikellisteNeu(map);
	}

}
