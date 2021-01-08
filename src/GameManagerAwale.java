package mancala;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;


//gere le jeu en fonction des regles de l'awale
//il enregistre aussi l'historique de la partie
public class GameManagerAwale extends GameManager implements Cloneable, java.io.Serializable {
	private int nbrJoueursHumain;
	private ArrayList<int[]> historique;

	private JoueurAwale joueur1;
	private JoueurAwale joueur2;
	private Awale partie;

	private int tourActuel;

	private boolean vocal;//true pour parler, sinon chuuut

	private boolean partieArretee = false;
	private boolean animated = false;
	private boolean partiePaused = false;

	private boolean isPartiePaused() {
		return partiePaused;
	}

	void setPartiePaused(boolean partiePaused) {
		this.partiePaused = partiePaused;
	}

	boolean isAnimated() {
		return animated;
	}

	private void setAnimated(boolean animated) {
		this.animated = animated;
	}

	private boolean isPartieArretee() {
		return partieArretee;
	}

	private void setPartieArretee(boolean partieArretee) {
		this.partieArretee = partieArretee;
	}

	private static GameManagerAwale instance = null;

	private static void setInstance(GameManagerAwale instance) {
		GameManagerAwale.instance = instance;
	}

	static GameManagerAwale getInstance() {
		return instance;
	}

	//constructeurs :
	public GameManagerAwale() {
		this.nbrJoueursHumain = choisirModeJeu();
		this.tourActuel = 0;
		this.historique = new ArrayList<>();
		instance = this;
	}

	public GameManagerAwale(int nbrJoueursHumain, int tourActuel) {
		this.nbrJoueursHumain = nbrJoueursHumain;
		this.tourActuel = tourActuel;
		this.historique = new ArrayList<>();
		this.partie = new Awale("MonAwale", "MesRegles");
		this.getPartie().initialisationJeu();
	}

	public GameManagerAwale(int mode) {
		if (mode == 0)
			this.nbrJoueursHumain = choisirModeJeu();
		this.tourActuel = 0;
		this.historique = new ArrayList<>();
		instance = this;
	}

	/* Cette methode teste si un mode de jeu donne
	 *  en parametre est valide ou non. On peut donc gerer
	 *  toutes les valeurs que l'on accepte, facilement
	 */
	private boolean modeDeJeuValide(int modeJeu) {
		return !(modeJeu < 0 || modeJeu > 2);
	}


	/* Cette methode affiche les differents modes de jeux jouables,
	 *  et demande a l'utilisateur d'en choisir un
	 */
	private int choisirModeJeu() {
		int modeDeJeu;
		Scanner sc = new Scanner(System.in);

		System.out.println("\n----Choisissez le mode de jeu----");
		System.out.println("0. IA       VS   IA");
		System.out.println("1. IA       VS   Joueur");
		System.out.println("2. Joueur   VS   Joueur");

		do {
			System.out.print("\nVotre choix >> ");
			modeDeJeu = sc.nextInt();
		} while (!modeDeJeuValide(modeDeJeu));

		return modeDeJeu;
	}

	//getters & setters :
	boolean getVocal() {
		return this.vocal;
	}

	int getNbrJoueursHumain() {
		return this.nbrJoueursHumain;
	}

	void setNbrJoueursHumain(int nbrJoueursHumain) {
		this.nbrJoueursHumain = nbrJoueursHumain;
	}

	JoueurAwale getJoueur1() {
		return this.joueur1;
	}

	private void setJoueur1(String nomJoueur) {
		if (getNbrJoueursHumain() == 0) {
			this.joueur1 = new JoueurAwaleIA(nomJoueur, 0, 1, 0, 5, 24);
		} else if (getNbrJoueursHumain() >= 1) {
			this.joueur1 = new JoueurAwaleHumain(nomJoueur, 0, 1, 0, 5, 24);
		}
	}

	private void setJoueur1(String nomJoueur, int difficulte) {
		if (getNbrJoueursHumain() == 0) {
			this.joueur1 = new JoueurAwaleIA(nomJoueur, 0, 1, 0, 5, 24, difficulte);
		} else if (getNbrJoueursHumain() >= 1) {
			this.joueur1 = new JoueurAwaleHumain(nomJoueur, 0, 1, 0, 5, 24);
		}
	}

	private void setJoueur1(String nomJoueur, int difficulte, int profondeur, StringBuilder heuristique) {
		if (getNbrJoueursHumain() == 0) {
			this.joueur1 = new JoueurAwaleIA(nomJoueur, 0, 1, 0, 5, 24, difficulte);
			((JoueurAwaleIA) this.joueur1).setProfondeurMax(profondeur);
			((JoueurAwaleIA) this.joueur1).setHeuristique(String.valueOf(heuristique));
		} else if (getNbrJoueursHumain() >= 1) {
			this.joueur1 = new JoueurAwaleHumain(nomJoueur, 0, 1, 0, 5, 24);
		}
	}

	//Pour sauvegarder les joueurs dans l'optique d'une sauvegarde de partie
	void loadJoueur1(JoueurAwale joueur1) {
		this.joueur1 = joueur1;
	}

	JoueurAwale getJoueur2() {
		return this.joueur2;
	}

	private void setJoueur2(String nomJoueur) {
		if (getNbrJoueursHumain() <= 1) {
			this.joueur2 = new JoueurAwaleIA(nomJoueur, 0, 2, 6, 11, 24);
		} else if (getNbrJoueursHumain() == 2) {
			this.joueur2 = new JoueurAwaleHumain(nomJoueur, 0, 2, 6, 11, 24);
		}
	}

	private void setJoueur2(String nomJoueur, int difficulte) {
		if (getNbrJoueursHumain() <= 1) {
			this.joueur2 = new JoueurAwaleIA(nomJoueur, 0, 2, 6, 11, 24, difficulte);
		} else if (getNbrJoueursHumain() == 2) {
			this.joueur2 = new JoueurAwaleHumain(nomJoueur, 0, 2, 6, 11, 24);
		}
	}

	private void setJoueur2(String nomJoueur, int difficulte, int profondeur, StringBuilder heuristique) {
		if (getNbrJoueursHumain() <= 1) {
			this.joueur2 = new JoueurAwaleIA(nomJoueur, 0, 2, 6, 11, 24, difficulte);
			((JoueurAwaleIA) this.joueur2).setProfondeurMax(profondeur);
			((JoueurAwaleIA) this.joueur2).setHeuristique(String.valueOf(heuristique));
		} else if (getNbrJoueursHumain() == 2) {
			this.joueur2 = new JoueurAwaleHumain(nomJoueur, 0, 2, 6, 11, 24);
		}
	}

	//Pour sauvegarder les joueurs dans l'optique d'une sauvegarde de partie
	void loadJoueur2(JoueurAwale joueur2) {
		this.joueur2 = joueur2;
	}

	public Awale getPartie() {
		return this.partie;
	}

	int getTourActuel() {
		return this.tourActuel;
	}

	void setTourActuel(int tourActuel) {
		this.tourActuel = tourActuel;
	}

	int[] getPlateau() {
		return this.partie.getPlateau();
	}

	void initJoueurs(String J1, String J2) {
		setJoueur1(J1);
		setJoueur2(J2);
	}

	void initJoueurs(String J1, int difficulte1, String J2, int difficulte2, int profondeur1, int profondeur2, StringBuilder heuristique1, StringBuilder heuristique2) {

		setJoueur1(J1, difficulte1, profondeur1, heuristique1);
		setJoueur2(J2, difficulte2, profondeur2, heuristique2);
	}

	/* Methode qui produit une replique de la partie en cours
	 * chaque methode clone() des attributs non primitifs est appelee
	 * @see java.lang.Object#clone()
	 */
	public GameManagerAwale clone() {
		GameManagerAwale clone = new GameManagerAwale(this.nbrJoueursHumain, this.tourActuel);
		clone.historique = (ArrayList<int[]>) this.historique.clone();
		clone.partie = this.partie.clone();

		//On doit connaitre le type des joueurs 1 et 2 avant de les cloner proprement

		//Joueur 1
		clone.joueur1 = this.joueur1.clone();

		//Joueur2
		clone.joueur2 = this.joueur2.clone();


		return clone;
	}

	private void affichePlateau() {
		//se pencher sur ce bout de code plus tard -Bastien (note a moi meme)--------------

		this.stockerEtatMouvement(this.getPartie().etatActuel());

		if (vocal) {
			System.out.println();
			for (int i = 11; i > 5; i--) {
				System.out.print(this.getPartie().etatActuel()[i] + " | ");
			}
			System.out.println();
			for (int i = 0; i < 6; i++) {
				System.out.print(this.getPartie().etatActuel()[i] + " | ");
			}

			System.out.println();

			System.out.println("Nbr graines en jeu : " + this.getPartie().getNbrGraines());
		}
		//---------------------------------------------------------------------------------
	}

	/* Methode qui gere le deroulement d'une partie,
	 * les coups joues et par qui. Elle ne s'arrete que
	 * lorsqu'une (au moins) des conditions de fins de partie est remplie
	 */
	void commencerPartie(boolean vocal) {
		this.vocal = vocal;
		while (!this.finPartie()) {
			this.affichePlateau();

			if (vocal)
				System.out.println("JOUEUR ACTUEL : " + joueurActuel().getNom());

			int coupJoue = this.joueurActuel().choisirUnCoup(this);

			if (this.verifierCoupValide(this.joueurActuel(), coupJoue, this.getPartie().getPlateau())) {
				this.joueurActuel().jouerUnCoup(coupJoue, this, vocal);
			}

			this.setTourActuel(getTourActuel() + 1);

			//Pour attendre un peu
			this.delay(60);
		}
		this.affichePlateau();
	}

	/*Methode qui instancie le plateau de jeu et
	 * demarre la partie avec la difficulte adequate !
	 */
	void lancerUneNouvellePartie(boolean silence) {
		this.initJoueurs("joueur1", "joueur2");
		this.partie = new Awale("MonAwale", "MesRegles");
		this.getPartie().initialisationJeu();
		this.commencerPartie(silence);
	}

	private void lancerUneNouvellePartieGraphique(Partie window, boolean nouvelle) {
		if (nouvelle) {
			this.partie = new Awale("MonAwale", "MesRegles");
			this.getPartie().initialisationJeu();
		}
		this.commencerPartieGraphique(window);
	}

	private void commencerPartieGraphique(Partie window) {
		window.getNameList().get(0).setText(this.joueur1.getNom());
		window.getNameList().get(1).setText(this.joueur2.getNom());
		while (!this.finPartie() && !this.isPartieArretee()) {
			while (this.isPartiePaused())
				this.delay(1000 / 60);
			if (this.joueurActuel() == this.joueur1) {
				window.getNameList().get(0).setForeground(Theme.GREEN_EMERALD);
				window.getNameList().get(1).setForeground(Theme.RED_EMERALD);
			} else {
				window.getNameList().get(0).setForeground(Theme.RED_EMERALD);
				window.getNameList().get(1).setForeground(Theme.GREEN_EMERALD);
			}

			window.getScoreList().get(0).setText(String.valueOf(this.joueur1.getScore()));
			window.getScoreList().get(1).setText(String.valueOf(this.joueur2.getScore()));

			this.enableAll(window);
			this.disableCaseNonValide(window);

			int coupJoue;

			if (this.getNbrJoueursHumain() == 0) {
				coupJoue = this.joueurActuel().choisirUnCoup(instance);
			} else if (this.getNbrJoueursHumain() == 1) {
				if (this.getTourActuel() % 2 == 0) {
					coupJoue = this.joueurActuel().choisirUnCoup(instance);
				} else {
					do {
						coupJoue = window.getCoupActu();
						window.setCoupActu(-1);
						this.delay(1000 / 60);
					} while (!partieArretee && ((coupJoue == -1) || (!this.verifierCoupValide(this.joueurActuel(), coupJoue, this.getPartie().getPlateau()))));
				}
			} else {
				do {
					coupJoue = window.getCoupActu();
					window.setCoupActu(-1);
					this.delay(1000 / 60);
				} while (!partieArretee && ((coupJoue == -1) || (!this.verifierCoupValide(this.joueurActuel(), coupJoue, this.getPartie().getPlateau()))));
			}
			if (coupJoue == -1)
				break;

			int graineRestante = this.getPartie().etatActuel()[coupJoue];

			this.joueurActuel().jouerUnCoup(coupJoue, instance, true);

			for (int i = 0; i < 12; i++) {
				//window.getButtonListGame().get(i).setForeground(Color.WHITE);
				window.getButtonListGame().get(i).setBgColor(Theme.BLUE_EMERALD);
			}

			//window.getButtonListGame().get(coupJoue).setForeground(Color.RED);
			window.getButtonListGame().get(coupJoue).setBgColor(Theme.GREEN_EMERALD);

			setAnimated(true);
			for (int i = coupJoue; i < 12; i++) {
				if (graineRestante >= 0 && i != coupJoue) {
					//window.getButtonListGame().get(i).setForeground(Color.BLUE);
					//window.getButtonListGame().get(i).setBackground(Color.GREEN);
					window.getButtonListGame().get(i).setBgColor(Theme.GREEN_EMERALD);
				}
				if (this.getPartie().etatActuel()[i] != 0) {
					window.getButtonListGame().get(i).setEnabled(true);
				}
				window.getButtonListGame().get(i).setText("" + this.getPartie().etatActuel()[i]);
				this.delay(500);
				graineRestante--;
			}
			for (int i = 0; i < coupJoue; i++) {
				if (graineRestante >= 0 && i != coupJoue) {
					//window.getButtonListGame().get(i).setForeground(Color.BLUE);
					window.getButtonListGame().get(i).setBgColor(Theme.GREEN_EMERALD);
				}
				if (this.getPartie().etatActuel()[i] != 0) {
					window.getButtonListGame().get(i).setEnabled(true);
				}
				window.getButtonListGame().get(i).setText("" + this.getPartie().etatActuel()[i]);
				this.delay(500);
				graineRestante--;
			}
			setAnimated(false);

			this.setTourActuel(getTourActuel() + 1);
		}
	}

	void stockerEtatMouvement(int[] etatActuel) {//Historique
		this.historique.add(0, etatActuel.clone());
	}

	boolean verifierCoupValide(JoueurAwale joueur, int caseJouee, int[] plateau) {//bonne case avec bonnes regles
		//case non vide :
		if (plateau[caseJouee] != 0) {
			return caseJouee >= joueur.getMin() && caseJouee <= joueur.getMax() && interdictionAffamer(caseJouee);
		}
		return false;
	}

	@Override
	public JoueurAwale joueurActuel() { //decide de qui va jouer
		if (this.getTourActuel() % 2 == 0) return this.joueur2;
		return this.joueur1;
	}

	@Override
	public boolean finPartie() {//dire si c'est une fin de partie et arreter le jeu en fonction
		boolean finDePartie = false;
		String messageFinDePartie = "";

		if (this.getPartie().getNbrGraines() <= 1) {
			messageFinDePartie = " !! plus qu'une graine !! ";
			finDePartie = true;
		}
		//2*6*3 = 36, 2 pour les 2 joueurs, 6 pour le nombre de cases d'un cote, 3 pour le nombre de tour de plateau
		//36 >= 3 fois le tour du plateau redondant
		else if (nbRedondanceHistorique(36) >= 3) {
			messageFinDePartie = "Redondances dans les coups joues. La partie s'arrete.";
			finDePartie = true;
		} else if (this.joueurActuel().getNbrGraineJoueur() == 0) {
			messageFinDePartie = " !! plus de graines a jouer pour " + joueurActuel().getNom() + " !! ";
			finDePartie = true;
		}

		boolean affamerPartout = true;
		for (int i = this.joueurActuel().getMin(); i <= joueurActuel().getMax(); i++) {
			if (interdictionAffamer(i)) {
				affamerPartout = false;
			}
		}

		if (affamerPartout) {
			messageFinDePartie = "Adversaire affame.";
		}

		finDePartie = finDePartie || affamerPartout;

		if (finDePartie) {
			ajoutGains();
			if (vocal) {
				System.out.println();
				if (this.joueur1 instanceof JoueurAwaleIA && ((JoueurAwaleIA) this.joueur1).getDifficulte() != 0) {
					//DivByZero
					System.out.println("Temps de calcul moyen d'un coup : " + ((JoueurAwaleIA) this.joueur1).getTotalTime() / ((JoueurAwaleIA) this.joueur1).getNombreDeCoup());
					System.out.println("Nombre de noeuds parcourus en moyenne pour le calcul d'un coup : " + ((JoueurAwaleIA) this.joueur1).getTotalNode() / ((JoueurAwaleIA) this.joueur1).getNombreDeCoup());
					System.out.println();
				}

				if (this.joueur2 instanceof JoueurAwaleIA && ((JoueurAwaleIA) this.joueur2).getDifficulte() != 0) {
					System.out.println("Temps de calcul moyen d'un coup : " + ((JoueurAwaleIA) this.joueur2).getTotalTime() / ((JoueurAwaleIA) this.joueur2).getNombreDeCoup());
					System.out.println("Nombre de noeuds parcourus en moyenne pour le calcul d'un coup : " + ((JoueurAwaleIA) this.joueur2).getTotalNode() / ((JoueurAwaleIA) this.joueur2).getNombreDeCoup());
					System.out.println();
				}
				System.out.println(messageFinDePartie);
				System.out.println();
			}
		}

		return finDePartie;
	}

	@Override
	public void gestionTemps() {//gere le temps alloue a chaque joueur tour a  tour

	}

	@Override
	public JoueurAwale getGagnant() {
		int gagnant = 0;
		int score1 = getJoueur1().getScore();
		int score2 = getJoueur2().getScore();
		if (score1 == score2) {
			if (vocal)
				System.out.println(" Score Egaux ! " + score1);
		} else if (score1 > score2) {
			if (vocal)
				System.out.println("Joueur 1 a gagne");
			gagnant = 1;
		} else {
			if (vocal)
				System.out.println("Joueur 2 a gagne");
			gagnant = 2;
		}
		if (vocal)
			System.out.println("Score joueur 1: " + score1 + "\nScore joueur 2: " + score2);
		if (gagnant == 1) {
			return this.joueur1;
		} else if (gagnant == 2) {
			return this.joueur2;
		}
		return null;
	}

	void ajoutGains() {
		this.joueur2.setScore(this.joueur2.getScore() + this.getJoueur2().getNbrGraineJoueur());
		this.joueur1.setScore(this.joueur1.getScore() + this.getJoueur1().getNbrGraineJoueur());
	}

	ArrayList<Integer> determinerCoupPossible(JoueurAwale joueur, int[] plateau) {
		ArrayList<Integer> coupPossible = new ArrayList<>();
		for (int i = joueur.getMin(); i <= joueur.getMax(); i++) {
			if (verifierCoupValide(joueur, i, plateau)) {
				coupPossible.add(i);
			}
		}

		return coupPossible;
	}

	boolean interdictionAffamer(int caseJouee) {//renvoie vrai si on n'affame pas l'adversaire ou faux sinon
		if ((this.joueurActuel() == this.getJoueur1() && this.getJoueur2().getNbrGraineJoueur() == 0) || (this.joueurActuel() == this.getJoueur2() && this.getJoueur1().getNbrGraineJoueur() == 0)) {
			int nbrGrainesJouee = this.getPartie().getPlateau()[caseJouee];
			int resteADeposer = nbrGrainesJouee - (this.joueurActuel().getMax() - caseJouee);
			return resteADeposer > 0;
		}
		return true;
	}

	private boolean plateauxEgaux(int[] plateau1, int[] plateau2) {
		boolean equal = (plateau1.length == plateau2.length);
		int i = plateau1.length - 1;
		while (equal && i >= 0) {
			if (plateau1[i] != plateau2[i]) {
				equal = false;
			}
			i--;
		}
		return equal;
	}

	int nbRedondanceHistorique(int profondeur) {
		int redondances = 0;
		int profondeurEffective = Math.min(this.historique.size() - 1, profondeur);
		for (int i = 0; i < profondeurEffective; i++) {
			if (plateauxEgaux(this.historique.get(0), this.historique.get(i))) {
				redondances++;
			}
		}
		return redondances;
	}

	private void delay(int temps) {
		try {
			Thread.sleep(temps);
		} catch (InterruptedException ignored) {

		}
	}

	void resetPartie() {
		this.historique = new ArrayList<>();
		this.joueur1.reset();
		this.joueur2.reset();
		this.tourActuel = 0;
		this.partie.initialisationJeu();
	}

	private void enableAll(Partie window) {
		for (int i = 0; i < 12; i++) {
			window.getButtonListGame().get(i).setEnabled(true);
		}
	}

	private void disableCaseNonValide(Partie window) {
		for (int i = 0; i < 12; i++) {
			if (Integer.parseInt(window.getButtonListGame().get(i).getText()) == 0) {
				window.getButtonListGame().get(i).setEnabled(false);
			}
		}
	}

	private void checkDossierSauvegarde() {
		Path sauvegardes = Paths.get("saves");
		File dossier = new File(sauvegardes.toString());
		if (!Files.exists(sauvegardes)) {
			dossier.mkdir();
		}

	}

	private String getDateStr() {
		SimpleDateFormat date_format = new SimpleDateFormat("yyyy-dd-MM-HH-mm-ss");
		return date_format.format(new Date());
	}

	void sauvegarder(String nom) {
		checkDossierSauvegarde();
		try {
			String nomSauvegarde = "saves/" + (nom == null ? "sauvegarde" : nom) + "_" + getDateStr() + ".save";
			FileOutputStream ecritureSauvegarde = new FileOutputStream(nomSauvegarde);
			ObjectOutputStream streamOut = new ObjectOutputStream(ecritureSauvegarde);
			streamOut.writeObject(this);
			streamOut.close();
			ecritureSauvegarde.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static ArrayList<String> getSavedGames() {
		ArrayList<String> savedGames = new ArrayList<>();
		Path sauvegardes = Paths.get("saves");
		File dossier = new File(sauvegardes.toString());

		if (!Files.exists(sauvegardes)) {
			dossier.mkdir();
		}

		String[] fichiers = dossier.list();
		assert fichiers != null;
		for (String fichier : fichiers) {
			if (fichier.matches(".*.save")) {
				savedGames.add(fichier);
			}
		}

		return savedGames;
	}

	private static GameManagerAwale loadGame(int saveNumber) {
		GameManagerAwale retour = new GameManagerAwale(0, 0);
		ArrayList<String> saves = getSavedGames();
		if (saveNumber > -1 && saveNumber < saves.size()) {
			try {
				FileInputStream fichierIN = new FileInputStream("saves/" + saves.get(saveNumber));
				ObjectInputStream objetIN = new ObjectInputStream(fichierIN);
				retour = (GameManagerAwale) objetIN.readObject();
				objetIN.close();
				fichierIN.close();
				System.out.println("tour recup : " + retour.getTourActuel());
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("ERREUR : ce numéro de sauvegarde n'existe pas");
		}
		return retour;
	}

	int bla1(JComboBox comboBox) {
		ArrayList<String> savedGames = getSavedGames();
		String[] save = new String[savedGames.size()];
		savedGames.toArray(save);
		DefaultComboBoxModel modelSave = new DefaultComboBoxModel(save);
		comboBox.setModel(modelSave);
		return savedGames.size();
	}

	void chargerPartieGraphique(int numSave) {
		setInstance(loadGame(numSave));
		this.partie = instance.getPartie();
	}

	void ehem(GameManagerAwale hehe) {
		Partie partie = new Partie(hehe.joueur1.getNom(), hehe.joueur2.getNom());
		partie.actualiserPartie();
		this.lancerThread(partie, false);
	}

	void lancerThread(Partie partie) {
		this.lancerThread(partie, true);
	}

	private void lancerThread(Partie partie, boolean nouvelle) {
		this.setPartieArretee(false);
		Thread threadGraphique = new Thread(() -> {
			lancerUneNouvellePartieGraphique(partie, nouvelle);
			JoueurAwale gagnant = getGagnant();
			if (gagnant != null) {
				String message = "Le joueur : " + gagnant.getNom() + "a gagné avec un score de : " + gagnant.getScore();
				DrawingManager.showDialog(message, "Bravo!");
			}
		});
		threadGraphique.start();
	}

	void arreterThread() {
		this.setPartieArretee(true);
	}

	//Unused methods
	public void initJoueurs(String J1, int difficulte1, String J2, int difficulte2) {
		setJoueur1(J1, difficulte1);
		setJoueur2(J2, difficulte2);
	}

	//cette méthode ne concerne pas la partie graphique, elle utilise seulement loadGame et getSavedGame pour charger
	//une partie sauvegardé, en console
	public static GameManagerAwale chargerPartieConsole() {
		ArrayList<String> savedGames = getSavedGames();
		GameManagerAwale retour = new GameManagerAwale();
		if (savedGames.size() != 0) {
			for (int i = 0; i < savedGames.size(); i++) {
				System.out.println("Sauvegarde " + i + " : " + savedGames.get(i));
			}
			Scanner sc = new Scanner(System.in);
			int choix;
			do {
				System.out.print("charger quelle partie ? >>");
				choix = sc.nextInt();
			} while (choix < 0 || choix >= savedGames.size());

			retour = loadGame(choix);
		} else {
			System.out.println("Aucune sauvegarde disponible");
		}

		return retour;
	}

	public void afficheHistorique() {
		int[] current;
		for (int[] ints : historique) {
			current = ints;
			for (int j = 11; j > 5; j--) {
				System.out.print(current[j] + "|");
			}
			System.out.println();
			for (int j = 0; j < 6; j++) {
				System.out.print(current[j] + "|");
			}
			System.out.println();
			System.out.println();
		}
	}
}
