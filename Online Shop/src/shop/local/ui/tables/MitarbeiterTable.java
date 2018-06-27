package shop.local.ui.tables;

import java.util.Map;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import shop.local.domain.exceptions.ArtikelNichtVorhandenException;
import shop.local.ui.werkzeuge.MitarbeiterArtikelTableModel;
import shop.local.ui.werkzeuge.WarenkorbTableModel;
import shop.local.ui.werkzeuge.Listener.GUIListener;
import shop.local.valueobjects.Artikel;

public class MitarbeiterTable extends JTable {

	private Map<Artikel, Integer> map;
	private GUIListener guiListener;

	public MitarbeiterTable(Map<Artikel, Integer> map, GUIListener guiListener) {
		super();
		this.map = map;
		this.guiListener = guiListener;
		this.setModel(new MitarbeiterArtikelTableModel(map));

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

					try {
						guiListener
								.MitarbeiterDetailAnzeigenPanel(table.getValueAt(table.getSelectedRow(), 1).toString());
					} catch (ArtikelNichtVorhandenException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});

	}

	public void setMitarbeiterArtikelNeu(Map<Artikel, Integer> map) throws ArtikelNichtVorhandenException {
		MitarbeiterArtikelTableModel model = (MitarbeiterArtikelTableModel) getModel();
		model.setMitarbeiterArtikelNeu(map);
		guiListener.MitarbeiterDetailAnzeigenPanel(this.getValueAt(this.getSelectedRow(), 1).toString());
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