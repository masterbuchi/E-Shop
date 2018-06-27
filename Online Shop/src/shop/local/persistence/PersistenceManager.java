package shop.local.persistence;

import java.io.IOException;

import shop.local.domain.exceptions.ArtikelNichtVorhandenException;
import shop.local.domain.exceptions.NutzerNichtVorhandenException;
import shop.local.valueobjects.Artikel;
import shop.local.valueobjects.Ereignis;
import shop.local.valueobjects.Kunde;
import shop.local.valueobjects.Mitarbeiter;




public interface PersistenceManager {

	public void openForReading(String datenquelle) throws IOException;
	
	public void openForWriting(String datenquelle) throws IOException;
	
	public boolean close();


	public Artikel ladeArtikel() throws IOException;


	public void speichereArtikel(Artikel artikel) throws IOException;

	public int ladeAnzahl() throws IOException;
	
	public void speichereAnzahl(int anzahl) throws IOException;
	
	public Kunde ladeKunde() throws IOException;

	public void speichereKunde(Kunde kunde) throws IOException;
	
	public Mitarbeiter ladeMitarbeiter() throws IOException;

	public void speichereMitarbeiter(Mitarbeiter mitarbeiter) throws IOException;

	public Ereignis ladeEreignis() throws IOException;

	public void speichereEreignis(Ereignis ereignis) throws IOException;
	
	

}