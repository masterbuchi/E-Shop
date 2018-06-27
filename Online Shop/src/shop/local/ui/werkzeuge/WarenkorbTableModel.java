package shop.local.ui.werkzeuge;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;

import shop.local.domain.comparators.ArtikelNummerComparator;
import shop.local.valueobjects.Artikel;

public class WarenkorbTableModel extends AbstractTableModel {

	private Map<Artikel, Integer> warenkorb;
	private String[] spaltenNamen = { "Artikel", "Preis", "Menge"};

	public WarenkorbTableModel(Map<Artikel, Integer> originalWarenkorb) {
		super();
		// Eine Kopie vom Warenkorb wird erstellt.
		warenkorb = new TreeMap<Artikel, Integer>(new ArtikelNummerComparator());
		warenkorb.putAll(originalWarenkorb);
	}

	public void setWarenkorbNeu(Map<Artikel, Integer> neuerWarenkorb) {
		warenkorb.clear();
		warenkorb.putAll(neuerWarenkorb);
		fireTableDataChanged();
	}

	/*
	 * Hier kommen die Einstellungen der Table
	 */

	@Override
	public int getRowCount() {
		return (warenkorb.size() + 1);
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
		
		if (warenkorb != null) {
			
			if (row == warenkorb.size()) {
				
				switch (col) {
				case 2: 
					return ("Summe: " + "HIER KOMMT DIE SUMME HIN");
				default: 
					return null;
				}
			} else {
				for (Map.Entry<Artikel, Integer> entry : warenkorb.entrySet()) {
					if (i == row) {
					
						
						switch (col) {
						case 0:
							return entry.getKey().getName();
						case 1:
							return (Math.rint((entry.getKey().getPreis() * 1.19) * 100) / 100) + " €";
						case 2:
							return entry.getValue();
						default:
							return null;
						}

					}

					i++;
				}

			}
		} else {
			return null;
		}
		return null;

	}
}