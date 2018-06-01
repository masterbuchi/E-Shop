package shop.local.ui.panels;


import java.util.Map;

import javax.swing.JTable;


import shop.local.ui.WarenkorbTableModel;
import shop.local.ui.werkzeuge.ButtonColumn;
import shop.local.valueobjects.Artikel;


public class WarenkorbTable extends JTable{

		private Map<Artikel,Integer> map;
		
		public WarenkorbTable(Map<Artikel, Integer> map) {
			super();
			this.map = map;
			this.setModel(new WarenkorbTableModel(map));
			ButtonColumn buttonColumn = new ButtonColumn(this,null, 3);
//			buttonColumn.setMnemonic(KeyEvent.VK_D);
		}
		
	}