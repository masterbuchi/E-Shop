package shop.local.ui.tables;

import java.util.Map;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import shop.local.domain.exceptions.ArtikelNichtVorhandenException;
import shop.local.ui.werkzeuge.ArtikelListeTableModel;
import shop.local.ui.werkzeuge.ButtonColumn;
import shop.local.ui.werkzeuge.KundeArtikelTableModel;
import shop.local.ui.werkzeuge.Listener.GUIListener;
import shop.local.valueobjects.Artikel;

public class KundeArtikelTable extends JTable {

	Map<Artikel, Integer> artikelMap;

	public KundeArtikelTable(Map<Artikel, Integer> artikelMap, GUIListener guiListener, Map<Artikel, Integer> warenkorbMap) {

		super();
		this.artikelMap = artikelMap;
		this.setModel(new KundeArtikelTableModel(artikelMap, warenkorbMap));

		// keine Linien anzeigen lassen
		this.setShowGrid(false);

		// Höhe einer Zelle definieren
		this.setRowHeight(this.getRowHeight() + 10);

		// Nur eine Zeile kann ausgewählt werden
		this.setSelectionModel(new ForcedListSelectionModel());

		// Das Austauschen von Spalten wird deaktiviert
		this.getTableHeader().setReorderingAllowed(false);

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

	public void setKundeArtikelNeu(Map<Artikel, Integer> map) {
		KundeArtikelTableModel model = (KundeArtikelTableModel) getModel();
		model.setArtikellisteNeu(map);
	}

}