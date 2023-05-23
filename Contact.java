package application;

public class Contact {
	private String nom;
	private String numero;
	private String mail;
	
	//Constructeur
	public Contact() {
		
	}
	public Contact(String nom, String numero, String mail) {
		this.nom = nom;
		this.numero = numero;
		this.mail = mail;
	}
	//Guetteur Setteur
	public String getNom() {
		return nom;
	}
	public String getNumero() {
		return numero;
	}
	public String getMail() {
		return mail;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	
	//Affiche les informations du contact
	@Override
	public String toString() {
		return "Contact [nom = " + nom + ", numero = 0" + numero + ", mail = " + mail + "]";
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
}
