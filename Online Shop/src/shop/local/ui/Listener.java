package shop.local.ui;

import java.util.Map;

import shop.local.valueobjects.Artikel;

public class Listener {
	public interface RegListener{
		public void regPanel();

	}
	public interface MainListener{
		public void mainPanel();
	}
	
	public interface SuchListener{
		public void suchErgebnisse(Map<Artikel, Integer> map);
	}
}
