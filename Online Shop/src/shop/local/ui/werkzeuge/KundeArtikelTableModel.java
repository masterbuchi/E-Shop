package shop.local.ui.werkzeuge;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.table.AbstractTableModel;
import shop.local.valueobjects.Artikel;

import shop.local.domain.comparators.ArtikelNummerComparator;

public class KundeArtikelTableModel extends AbstractTableModel {
	private Map<Artikel, Integer> artikelliste;
	private String[] spaltenNamen = { "Artikel", "Preis", "Derzeit Verfügbar", "Menge im Warenkorb" };
	private String[][] mengen;
	Map<Artikel, Integer> warenkorbMap;

	public KundeArtikelTableModel(Map<Artikel, Integer> orginalArtikelliste, Map<Artikel, Integer> warenkorbMap) {
		super();
		artikelliste = new TreeMap<Artikel, Integer>(new ArtikelNummerComparator());
		artikelliste.putAll(orginalArtikelliste);
		this.warenkorbMap = warenkorbMap;

		mengen = new String[artikelliste.size()][spaltenNamen.length];
		int i = 0;
		int value;
		if (warenkorbMap != null) {
			for (Map.Entry<Artikel, Integer> entry : artikelliste.entrySet()) {
				Artikel warenkorbArtikel = entry.getKey();

				if (warenkorbMap.get(warenkorbArtikel) != null) {
					value = warenkorbMap.get(warenkorbArtikel);
					mengen[i][3] = String.valueOf(value);

				} 
				i++;
			}
		}

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
						return (Math.rint((entry.getKey().getPreis() * 1.19) * 100) / 100) + " €";
					case 2:
						if (entry.getValue() <= 0) {
							return "Derzeit nicht verfügbar";
						} else {
							return entry.getValue();
						}
					case 3:
						return mengen[row][col];
					default:
						return null;

					}

				}
				i++;
			}
		}

		return null;
	}

	public void setValueAt(Object value, int row, int col) {

		if (col == 3) {
			mengen[row][col] = (String) value;
			fireTableCellUpdated(row, col);
		}
	}

	public void refresh() {
		fireTableDataChanged();
	}

	public boolean isCellEditable(int row, int column) {

		if (column == 3) {
			return true;
		} else {
			return false;
		}
	}

}
