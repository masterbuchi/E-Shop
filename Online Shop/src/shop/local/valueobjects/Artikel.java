package shop.local.valueobjects;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * Klasse zur Repräsentation einzelner Artikel.
 * 
 * @author Error404
 *
 */

public class Artikel {

	// --- Eigenschaften ---

	// public static AtomicInteger nextId = new AtomicInteger();
	public static AtomicInteger maxId = new AtomicInteger(0);

	private int id;
	private String name;
	private float preis;
	private String preistext;

	// --- Ergänzbare Eigenschaften ---

	public Artikel(int id, String name, float preis) {

		// Nummer wird normalerweise nicht durch User gesetzt, sondern automatisch
		// generiert.
		if (id == 0) {
			this.id = maxId.incrementAndGet();
		} else if (id == -1) {
			
		} else {
			this.id = id;

		}
		this.name = name;
		this.preis = preis;

		preistext = Float.toString(preis);
		preistext = preistext.replace(".", ",");

	}

	// --- Dienste der Artikel - Objekte ---

	@Override
	public String toString() {
		return (id + " " + "Name: " + name + "\n Preis: " + preistext + " € \n ");
	}

	@Override
	public boolean equals(Object anderesObjekt) {
		if (anderesObjekt instanceof Artikel) {
			Artikel artikel = (Artikel) anderesObjekt;
			return (this.id == (artikel.getNummer()));

		} else
			return false;
	}

	// --- Accessor - Methoden ---

	public int getNummer() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPreis() {
		return preis;
	}

	public void setPreis(float preis) {
		this.preis = preis;
	}

}
