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
import shop.local.ui.werkzeuge.WarenkorbTableModel;
import shop.local.ui.werkzeuge.Listener.GUIListener;
import shop.local.ui.werkzeuge.RechnungTableModel;
import shop.local.valueobjects.Artikel;

public class WarenkorbTable extends JTable {

	private Map<Artikel, Integer> map;

	public WarenkorbTable(String type, Map<Artikel, Integer> map, GUIListener guiListener) {
		super();
		this.map = map;
		if (type.equals("kaufen")) {
			this.setModel(new RechnungTableModel(map));
		} else {
			this.setModel(new WarenkorbTableModel(map));
		}
		// keine Linien anzeigen lassen
		this.setShowGrid(false);

		// Höhe einer Zelle definieren
		this.setRowHeight(this.getRowHeight() + 10);

		// Nur eine Zeile kann ausgewählt werden
		this.setSelectionModel(new ForcedListSelectionModel());

		// Das Austauschen von Spalten wird deaktiviert
		this.getTableHeader().setReorderingAllowed(false);

		JTable table = this;
		// Erzeugt ein DetailPanel mit Informationen über die ausgewählte Zeile
		this.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				if (!event.getValueIsAdjusting()) {

					// Wenn nicht "kaufen" übergeben wurde, dann sind wir im Warenkorb
					// und das Detailpanel soll geöffnet werden
					if (!type.equals("kaufen")) {
						if (table.getRowCount() - 1 != table.getSelectedRow()) {
							try {
								guiListener.KundeDetailAnzeigenPanel(
										table.getValueAt(table.getSelectedRow(), 0).toString());
							} catch (ArtikelNichtVorhandenException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}
				}
			}
		});

	}

	public void setWarenkorbNeu(Map<Artikel, Integer> map) {
		WarenkorbTableModel model = (WarenkorbTableModel) getModel();
		model.setWarenkorbNeu(map);
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