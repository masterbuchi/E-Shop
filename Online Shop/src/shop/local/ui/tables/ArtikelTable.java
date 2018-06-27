package shop.local.ui.tables;

import java.util.Map;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import shop.local.ui.werkzeuge.ArtikelListeTableModel;
import shop.local.valueobjects.Artikel;

public class ArtikelTable extends JTable {
	private Map<Artikel, Integer> map;

	public ArtikelTable(Map<Artikel, Integer> map) {
		super();
		this.map = map;
		this.setModel(new ArtikelListeTableModel(map));
		
		// keine Linien anzeigen lassen
		this.setShowGrid(false);

		// Höhe einer Zelle definieren
		this.setRowHeight(this.getRowHeight()+10);

		// Nur eine Zeile kann ausgewählt werden
		this.setSelectionModel(new ForcedListSelectionModel());

		// Das Austauschen von Spalten wird deaktiviert
		this.getTableHeader().setReorderingAllowed(false);
	}

	public void setArtikellisteNeu(Map<Artikel, Integer> map) {
		ArtikelListeTableModel model = (ArtikelListeTableModel) getModel();
		model.setArtikellisteNeu(map);
	}

	public class ForcedListSelectionModel extends DefaultListSelectionModel {

		public ForcedListSelectionModel() {
			setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}

		@Override
		public void clearSelection() {
		}

		@Override
		public void removeSelectionInterval(int index0, int index1) {
		}

	}

}
