package shop.local.ui.werkzeuge;

import java.util.Map;
import java.util.TreeMap;

import javax.swing.table.AbstractTableModel;

import shop.local.domain.comparators.ArtikelNummerComparator;
import shop.local.valueobjects.Artikel;

public class MitarbeiterArtikelTableModel extends AbstractTableModel {

	private Map<Artikel, Integer> lager;
	private double artikelPreis;
	private String[] spaltenNamen = { "ID", "Artikel", "Preis", "Derzeit Verfügbar", "Gelistet" };

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
						
						artikelPreis = Math.rint((entry.getKey().getPreis() * 1.19) * 100) / 100;
						artikelPreis = artikelPreis * 100;
						
						String artikelPreisString = Double.toString(artikelPreis / 100);
						artikelPreisString = artikelPreisString.replace(".", ",");
						artikelPreisString = artikelPreisString + " €";
						
						return artikelPreisString;
					case 3:
						return entry.getValue();
					case 4:
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