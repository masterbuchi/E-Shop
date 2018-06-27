package shop.local.ui.werkzeuge;

import java.util.Map;
import java.util.TreeMap;

import javax.swing.table.AbstractTableModel;

import shop.local.domain.comparators.ArtikelNummerComparator;
import shop.local.valueobjects.Artikel;

public class MitarbeiterArtikelTableModel extends AbstractTableModel {

	private Map<Artikel, Integer> lager;
	private String[] spaltenNamen = { "ID", "Artikel", "Preis", "Preis ink. Mehrwertsteuer", "Derzeit Verfügbar", "Gelistet" };

	public MitarbeiterArtikelTableModel(Map<Artikel, Integer> originalLager) {
		super();
		// Eine Kopie vom Warenkorb wird erstellt.
		lager = new TreeMap<Artikel, Integer>(new ArtikelNummerComparator());
		lager.putAll(originalLager);
	}

	public void setMitarbeiterArtikelNeu(Map<Artikel, Integer> originalLager) {
		lager.clear();
		lager.putAll(originalLager);
		fireTableDataChanged();
	}

	/*
	 * Hier kommen die Einstellungen der Table
	 */

	@Override
	public int getRowCount() {
		return (lager.size());
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
	public Object getValueAt(int row, int col) {

		int i = 0;

		if (lager != null) {

			for (Map.Entry<Artikel, Integer> entry : lager.entrySet()) {
				if (i == row) {
					switch (col) {
					case 0:
						return entry.getKey().getNummer();
					case 1:
						return entry.getKey().getName();
					case 2:
						return (Math.rint((entry.getKey().getPreis())* 100) / 100) + " €";
					case 3: 
						return (Math.rint((entry.getKey().getPreis() * 1.19) * 100) / 100) + " €";
					case 4:
						return entry.getValue();
					case 5:
						if (entry.getKey().getGelistet()) {
							return "Ja";
						} else {
							return "Nein";
						}
					default:
						return null;
					}
				}
				i++;
			}
		} else {
			return null;
		}
		return null;

	}
}