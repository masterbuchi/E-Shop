package shop.local.ui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class SearchPanel extends JPanel {

	private JTextField searchTextField;
	private JButton searchButton = null;

	// Konstruktor
	public SearchPanel() {
		setUpSearch();
		setUpSearchEvents();

	}

	public void setUpSearch() {
		// setBorder(BorderFactory.createTitledBorder("Suche"));
		this.searchTextField = new JTextField();
		this.searchButton = new JButton("Suche");

		this.setLayout(new MigLayout());

		this.add(searchTextField, "left, width 50%:100%");
		this.add(searchButton);

	}

	private void setUpSearchEvents() {
		searchButton.addActionListener(new SearchListener());
	}

	// Lokale Klasse für Reaktion auf Such-Klick
	class SearchListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource().equals(searchButton)) {
				
				
			}
		}

	}

	public String getTextSuche() {
		return searchTextField.getText();
	}
}