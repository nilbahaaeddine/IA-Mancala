package mancala;

//Contient le nom du joueur, son score.
//c'est ici que se feront les methodes pour jouer un coup
public abstract class Joueur implements java.io.Serializable {
	private String nomJoueur;
	int score;
	int numeroJoueur;
	private int min;
	private int max;
	private int nbrGraineJoueur;

	//constructeurs :
	Joueur(String nomJoueur, int score, int numeroJoueur, int min, int max, int nbrGraineJoueur) {
		this.nomJoueur = nomJoueur;
		this.score = score;
		this.numeroJoueur = numeroJoueur;
		this.min = min;
		this.max = max;
		this.nbrGraineJoueur = nbrGraineJoueur;
	}

	Joueur() {
	}

	//getters & setters :
	String getNom() {
		return this.nomJoueur;
	}

	void setNomJoueur(String nomJoueur) {
		this.nomJoueur = nomJoueur;
	}

	int getScore() {
		return this.score;
	}

	void setScore(int score) {
		this.score = score;
	}

	int getNumeroJoueur() {
		return this.numeroJoueur;
	}

	void setNumeroJoueur(int numeroJoueur) {
		this.numeroJoueur = numeroJoueur;
		if (numeroJoueur == 1) {
			setMin(0);
			setMax(5);
		} else if (numeroJoueur == 2) {
			setMin(6);
			setMax(11);
		}
	}

	int getMin() {
		return this.min;
	}

	private void setMin(int min) {
		this.min = min;
	}

	int getMax() {
		return this.max;
	}

	private void setMax(int max) {
		this.max = max;
	}

	int getNbrGraineJoueur() {
		return this.nbrGraineJoueur;
	}

	void setNbrGraineJoueur(int nbrGraineJoueur) {
		this.nbrGraineJoueur = nbrGraineJoueur;
	}

	public void reset() {
		this.score = 0;
		this.nbrGraineJoueur = 24;
	}

	//methods :
	//public abstract void jouerUnCoup(int caseJouee,GameManagerAwale gameManagerAwale);//mise a� jour des valeurs du plateau
	public abstract int choisirUnCoup(GameManagerAwale gameManagerAwale);
}