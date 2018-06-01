package shop.local.ui;

import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;
import shop.local.valueobjects.Artikel;

import shop.local.domain.comparators.ArtikelNummerComparator;
import shop.local.ui.panels.ArtikellistePanel;
import shop.local.valueobjects.Artikel;

public class ArtikelListe extends AbstractTableModel {
	private Map<Artikel, Integer> artikelliste;
	private String[] spaltenNamen = { "Artikel", "Preis", "Anzahl" };

	public ArtikelListe(Map<Artikel, Integer> orginalArtikelliste) {
		super();
		artikelliste = new TreeMap<Artikel, Integer>(new ArtikelNummerComparator());
		artikelliste.putAll(orginalArtikelliste);
	}

	public void setArtikellisteNeu(Map<Artikel, Integer> map) {
		artikelliste.clear();
		artikelliste.putAll(map);
		fireTableDataChanged();
		
	}
	
	@Override
	public int getColumnCount() {
		return spaltenNamen.length;
	}
	
	@Override
	public String getColumnName(int col) {
		return spaltenNamen[col];
	}

	@Override
	public int getRowCount() {
		return (artikelliste.size());
	}

	@Override
	public Object getValueAt(int row, int col) {

		int i = 0;
		if (artikelliste != null) {
			for (Map.Entry<Artikel, Integer> entry : artikelliste.entrySet()) {
				if (i == row) {
					switch (col) {
					case 0:
						return entry.getKey().getName();
					case 1:
						return (Math.rint((entry.getKey().getPreis() * 1.19) * 100) / 100 )+ " €";
					case 2:
						return entry.getValue();
					default:
						return null;

					}

				}
				i++;
			}
		}

		return null;
	}

}
