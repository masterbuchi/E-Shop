package shop.local.ui.werkzeuge;

import java.util.Map;
import java.util.TreeMap;
import javax.swing.table.AbstractTableModel;
import shop.local.valueobjects.Artikel;

import shop.local.domain.comparators.ArtikelNummerComparator;

public class ArtikelListeTableModel extends AbstractTableModel {
	private Map<Artikel, Integer> artikelliste;
	private String[] spaltenNamen = { "Artikel", "Preis", "Derzeit Verfügbar" };

	public ArtikelListeTableModel(Map<Artikel, Integer> orginalArtikelliste) {
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
						return (Math.rint((entry.getKey().getPreis() * 1.19) * 100) / 100) + " €";
					case 2:
						if (entry.getValue() <= 0) {
							return "Derzeit nicht verfügbar";
						} else {
							return entry.getValue();
						}
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
