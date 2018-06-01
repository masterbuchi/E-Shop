package shop.local.valueobjects;


/**
 * 
 * Klasse zur Repräsentation aller Kunden, erbt von Nutzer
 * 
 * @author Error404
 *
 */

public class Kunde extends Nutzer {

	private float guthaben = 0.00f;
	private String strasse = "";
	private int plz;
	private String wohnort = "";
	private Warenkorb korb;

	public Kunde(String nutzerName, String vorName, String nachName, String passwort, String strasse, int plz, String wohnort) {
		super(nutzerName, vorName, nachName, passwort);
		// this.nutzerName = nutzerName;
		// this.vorName = vorName;
		// this.nachName = nachName;
		// this.passwort = passwort;
		this.strasse = strasse;
		this.plz = plz;
		this.wohnort = wohnort;
		this.korb = new Warenkorb();
	}
	
	public Kunde(String nutzerName, String vorName, String nachName, String strasse, int plz, String wohnort, Warenkorb warenkorb) {
		super(nutzerName, vorName, nachName);
		// this.nutzerName = nutzerName;
		// this.vorName = vorName;
		// this.nachName = nachName;
		this.strasse = strasse;
		this.plz = plz;
		this.wohnort = wohnort;
		this.korb = warenkorb;
	}
	
	

	
	
	public Warenkorb getWarenkorb() {
		return korb;
	}


	public void setWarenkorb(Warenkorb korb) {
		this.korb = korb;
	}



	public float getGuthaben() {
		return guthaben;
	}

	public void setGuthaben(float guthaben) {
		this.guthaben = guthaben;
	}

	public String getStrasse() {
		return strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public int getPlz() {
		return plz;
	}

	public void setPlz(int plz) {
		this.plz = plz;
	}

	public String getWohnort() {
		return wohnort;
	}

	public void setWohnort(String wohnort) {
		this.wohnort = wohnort;
	}

	// private List<Artikel> warenkorb = new Vector<Artikel>();

	@Override
	public boolean equals(Object anderesObjekt) {
		if (anderesObjekt instanceof Kunde) {
			Kunde kunde = (Kunde) anderesObjekt;
			return (this.getNutzerName().equals(kunde.getNutzerName()));

		} else
			return false;
	}

}
