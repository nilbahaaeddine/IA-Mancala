package mancala;

//contient les regles du jeu, le mode de difficulte et le nom du Jeu
public abstract class Jeu implements java.io.Serializable {

	private String nomJeu;
	private String regles;

	//constructeurs :
	Jeu(String nomJeu, String regles) {
		this.nomJeu = nomJeu;
		this.regles = regles;
	}

	//setters & getters :
	String getNomJeu() {
		return this.nomJeu;
	}

	String getRegles() {
		return this.regles;
	}

	//methods :
	public abstract void initialisationJeu();//permet de tout remettre a  0

	//Unused methods
	public void setRegles(String regles) {
		this.regles = regles;
	}

	public void setNomJeu(String nomJeu) {
		this.nomJeu = nomJeu;
	}
}
