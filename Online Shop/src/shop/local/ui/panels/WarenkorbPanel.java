package shop.local.ui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;
import shop.local.ui.Listener.RegListener;
import shop.local.valueobjects.Artikel;

public class WarenkorbPanel extends JPanel {

	
	private JButton kaufenButton = null;
	WarenkorbTable warenkorbtable;
//	private KundenMainListener kundenMainListener = new KundenMainListener;
	
		
		public WarenkorbPanel(Map<Artikel, Integer> map) {
//			kundenMainListener = listener;
			this.setLayout(new MigLayout("left, center"));
			this.setBorder(BorderFactory.createTitledBorder("Warenkorb"));
			
			setUpButtons();
			setUpButtonsEvents();
			warenkorbtable = new WarenkorbTable(map);
			
			this.add(new JScrollPane(warenkorbtable));
			kaufenButton = new JButton("Kaufen");
			this.add(kaufenButton);
		}

		public void setUpButtons() {
		
		}
		
		private void setUpButtonsEvents() {
			
		}
	
		// Lokale Klasse für Reaktion auf Anmeldungs-Klick
		class KaufenListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent ae) {
				if (ae.getSource().equals(kaufenButton)) {
					// TO-DO
				}
			}
		}

}
