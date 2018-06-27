package shop.local.ui.werkzeuge;

import java.util.Map;
import java.util.TreeMap;

import javax.swing.table.AbstractTableModel;

import shop.local.domain.comparators.ArtikelNummerComparator;
import shop.local.valueobjects.Artikel;

public class RechnungTableModel extends AbstractTableModel {
	private Map<Artikel, Integer> artikelliste;
	private String[] spaltenNamen = { "Artikel", "Preis", "Derzeit Verfügbar" };
	private double summe;
	private double artikelPreis;

	public RechnungTableModel(Map<Artikel, Integer> orginalArtikelliste) {
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
		return (artikelliste.size() + 1);
	}

	@Override
	public Object getValueAt(int row, int col) {

		int i = 0;
		if (artikelliste != null) {

			if (row == artikelliste.size()) {

				switch (col) {
				case 2:

					// Gesamtsumme returnen

					summe = 0;

					for (Map.Entry<Artikel, Integer> entry : artikelliste.entrySet()) {
						summe += Math.rint((entry.getKey().getPreis() * 1.19) * 100) / 100 * entry.getValue();
					}

					summe = summe* 100;
					String summeString = Double.toString(summe /100);
					summeString = summeString.replace(".", ",");
					summeString = summeString + " €";
					return summeString;

				default:
					return null;
				}
			} else {

				for (Map.Entry<Artikel, Integer> entry : artikelliste.entrySet()) {
					if (i == row) {
						switch (col) {
						case 0:
							return entry.getKey().getName();
						case 1:
							
							artikelPreis = Math.rint((entry.getKey().getPreis() * 1.19) * 100) / 100;
							artikelPreis = artikelPreis * 100;
							
							String artikelPreisString = Double.toString(artikelPreis / 100);
							artikelPreisString = artikelPreisString.replace(".", ",");
							artikelPreisString = artikelPreisString + " €";
							
							return artikelPreisString;
						case 2:
							if (entry.getValue() <= 0) {
								return "Derzeit nicht verfügbar";
							} else {
								
								
								
								return entry.getValue() ;
							}
						default:
							return null;

						}

					}
					i++;
				}
			}
		}

		return null;
	}

}
