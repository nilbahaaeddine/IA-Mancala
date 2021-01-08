package mancala;

//cette classe reconnait l'etat du jeu : nbr de graines, leur position dans le tableau
public class Awale extends Jeu implements Cloneable, java.io.Serializable {
	private int nbrGraines;
	private int[] plateau;

	private GameManagerAwale gameManagerAwale;

	//constructeur :
	Awale(String nomJeu, String regles) {
		super(nomJeu, regles);
	}

	//setters & getters :
	int getNbrGraines() {
		return this.nbrGraines;
	}

	void setNbrGraines(int nbrGrainesEnJeu) {
		this.nbrGraines = nbrGrainesEnJeu;
	}

	int[] getPlateau() {
		return this.plateau;
	}

	void setPlateau(int nouvelleValeur, int caseModifiee) {
		this.plateau[caseModifiee] = nouvelleValeur;
	}

	void modifierPlateau(int[] plateau) {
		this.plateau = plateau;
	}

	public Awale clone() {
		Awale clone = new Awale(this.getNomJeu(), this.getRegles());
		clone.plateau = this.plateau.clone();
		clone.nbrGraines = this.nbrGraines;
		return clone;
	}

	//methods :
	int[] etatActuel() {//permet de savoir la disposition du plateau actuel
		return this.getPlateau();
	}

	@Override
	public void initialisationJeu() {
		this.plateau = new int[]{4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4};
		setNbrGraines(48);

	}

	//Unused methods
	public void printPlateau() {
		System.out.print("[");
		for (int i = 0; i < this.plateau.length; i++) {
			System.out.print(this.plateau[i] + " ");
			if (i == 6) {
				System.out.print(" | ");
			}
		}
		System.out.print("]");
	}

	public void setPlateau(int[] p) {
		if (p.length == 6) {
			this.plateau = p;
		}
	}

	public void stockerJoueur(JoueurAwale joueur1, JoueurAwale joueur2) {// pouvoir reprendre une partie plus tard
		gameManagerAwale.loadJoueur1(joueur1);
		gameManagerAwale.loadJoueur2(joueur2);
	}
}
