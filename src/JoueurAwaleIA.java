package mancala;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

//les methodes de decision seront utilisees(MiniMax, Evalution...)
public class JoueurAwaleIA extends JoueurAwale implements Cloneable{
	//Pour calculer le nombre d'appels récursifs de minimax
	private int compteur = 0;
	
	//difficulte de l'IA
	private int difficulte=-1;
	
	//heuristiques actives
	private boolean[] heuristique= {false,false,false,false,true,false,false,false,false};
	//leur poids
	double[] poids= {0.5,0.19,0.5,0.5,0.5659,0.1986,0.3707,0.4188,1};
	
	//Pour calculer le temps d'execution de minimax
	private long time = 0;
	
	//Pour calculer le temps d'execution total au cours d'une partie de minimax
	private long totalTime = 0;
	
	//Pour calculer le nombre de coup joue par minimax au cours d'une partie
	private int nombreDeCoup = 0;
	
	//Pour calculer le nombre d'appel recursif
	private int nombreAppel = 0;
	
	//Pour calculer le nombre total de noeuds parcourus au cours d'une partie
	private int totalNode = 0;
	
	//profondeur maximum
	private int profondeurMax=-1;
	
	public int getProfondeurMax(){
		return this.profondeurMax;
	}
	
	public int getCompteur() 
	{
		return compteur;
	}

	public void setCompteur(int compteur) 
	{
		this.compteur = compteur;
	}
	
	public void incrementCompteur()
	{
		this.compteur += 1;
	}
	
	public long getTime() 
	{
		return time;
	}

	public void setTime(long time) 
	{
		this.time = time;
	}
	
	public long getTotalTime() 
	{
		return totalTime;
	}

	public void setTotalTime(long totalTime) 
	{
		this.totalTime = totalTime;
	}
	
	public int getNombreDeCoup() 
	{
		return nombreDeCoup;
	}

	public void setNombreDeCoup(int nombreDeCoup) 
	{
		this.nombreDeCoup = nombreDeCoup;
	}
	public void incrementNombreDeCoup()
	{
		this.nombreDeCoup += 1;
	}
	
	public int getNombreAppel()
	{
		return nombreAppel;
	}

	public void setNombreAppel(int nombreAppel) 
	{
		this.nombreAppel = nombreAppel;
	}
	
	public void incrementNombreAppel()
	{
		this.nombreAppel += 1;
	}
	
	public int getTotalNode() 
	{
		return totalNode;
	}

	public void setTotalNode(int totalNode) 
	{
		this.totalNode = totalNode;
	}
	
	public int getDifficulte() {
		return difficulte;
	}
	
	public void setDifficulte(int dif) {
		this.difficulte=(difficulteValide(dif)?dif:0);
	}
	
	public void reset() {
		super.reset();
		this.compteur=0;
		this.time=0;
		this.nombreDeCoup=0;
		this.nombreAppel=0;
		this.totalTime=0;
		this.totalNode=0;
	}
	
	public void setNomParDefaut() {
		switch(this.difficulte) {
		case 0:
			this.setNomJoueur("IA_RANDOM_J"+this.getNumeroJoueur());
			break;
			
		case 1:
			this.setNomJoueur("IA_MINMAX_J"+this.getNumeroJoueur());
			break;
			
		case 2:
			this.setNomJoueur("IA_ALPHABETA_J"+this.getNumeroJoueur());
			break;
			
		default:
			this.setNomJoueur("IA_J"+this.getNumeroJoueur());
			break;
		}
	}
	
	//constructeurs :
	JoueurAwaleIA(String nomJoueur, int score, int numeroJoueur, int min, int max, int nbrGraineJoueur) {
		super(nomJoueur, score, numeroJoueur, min, max, nbrGraineJoueur);
		choisirDifficulte();
	}
	JoueurAwaleIA(String nomJoueur, int score, int numeroJoueur, int min, int max, int nbrGraineJoueur, int difficulte) {
		super(nomJoueur, score, numeroJoueur, min, max, nbrGraineJoueur);
		setDifficulte(difficulte);
	}
	JoueurAwaleIA() {
		
	}
	
	public JoueurAwaleIA clone() {
		JoueurAwaleIA clone=new JoueurAwaleIA(this.getNom(),
													this.getScore(),
													this.getNumeroJoueur(),
													this.getMin(),
													this.getMax(),
													this.getNbrGraineJoueur(),
													this.difficulte);
		clone.compteur=this.compteur;
		clone.time=this.time;
		return clone;
	}
	
	private void choisirDifficulte() {
		int difficulte=0;
		Scanner sc=new Scanner(System.in);
		System.out.println("\n----Choisissez la difficulte de l'IA <"+this.getNom()+">----");
		System.out.println("0. IA naive (random)");
		System.out.println("1. IA minimax");
		System.out.println("2. IA alphaBeta");
		do {
			System.out.print("\nVotre choix >> ");
			difficulte=sc.nextInt();
		}while(difficulte<0 || difficulte>2);
		this.setDifficulte(difficulte);
	}
	
	
	/* Cette methode teste si une difficulte donnee
	 *  en parametre est valide ou non. On peut donc gerer
	 *  toutes les valeurs que l'on accepte, facilement
	 */
	public boolean difficulteValide(int difficulte) {
		return (difficulte==0 || difficulte==1 || difficulte==2);
	}
	
	
	//methods:
	public int[] simulerUnCoup(int caseJouee, GameManagerAwale arbitreAwale)
	{
		int plateauSimule[] = new int[12];
		
		for(int i = 0; i < 12; i++)
		{
			plateauSimule[i] = arbitreAwale.getPartie().getPlateau()[i];
		}
		miseAJourPlateauSimuler(plateauSimule, caseJouee);
		
		arbitreAwale.setTourActuel(arbitreAwale.getTourActuel() + 1);
		
		return plateauSimule;
	}
	
	//Permet de savoir qui gagne
	public int vainqueur(GameManagerAwale arbitreAwaleSimule)
	{
		arbitreAwaleSimule.ajoutGains();
		
		int scoreJoueur1 = arbitreAwaleSimule.getJoueur1().getScore(), 
			scoreJoueur2 = arbitreAwaleSimule.getJoueur2().getScore();		
		int joueurGagnant = -1;
		
		if(scoreJoueur1 > scoreJoueur2)
		{
			joueurGagnant = 1;
		}
		else if(scoreJoueur1 < scoreJoueur2)
		{
			joueurGagnant = 2;
		}
		else
		{
			joueurGagnant = 0;
		}
		
		return joueurGagnant;
	}
	
	public int differenceScore(int scoreJoueurA, int scoreJoueurB)
	{
		return scoreJoueurA - scoreJoueurB;
	}
	
	public int simulerFinPartie(ArrayList<int []> historique, GameManagerAwale arbitreAwaleSimule) 
	{
		int joueurGagnant = -1;
		
		GameManagerAwale arbitreSimule = arbitreAwaleSimule.clone();
		
		if(arbitreSimule.getPartie().getNbrGraines() <= 1) 
		{
			joueurGagnant = vainqueur(arbitreSimule);
		}
		
		else if(arbitreSimule.nbRedondanceHistorique(36) >= 3)
		{
			joueurGagnant = vainqueur(arbitreSimule);
		}
		
		else if(arbitreSimule.joueurActuel().getNbrGraineJoueur() == 0 ) 
		{
			joueurGagnant = vainqueur(arbitreSimule);
		}
		
		boolean affamerPartout = true;
		
		for(int i = arbitreSimule.joueurActuel().getMin(); i <= arbitreSimule.joueurActuel().getMax(); i++) 
		{
			if(arbitreSimule.interdictionAffamer(i)) 
			{
				affamerPartout = false;
			}
		}
		
		if(affamerPartout)
		{
			joueurGagnant = vainqueur(arbitreSimule);
		}
		
		return joueurGagnant;
	}
	
	/* Heuristique 1:
	 * L'objectif de cette heuristique est de minimiser
	 * le nombre de cases vulnerables
	 */
	private double H1(int numeroJoueur,int[] plateau) {
		int nbCasesVulnerables=0;
		
		int debut=(numeroJoueur==1?0:6);
		int fin=(numeroJoueur==2?6:12);
		
		for(int i=debut;i<fin;i++) {
			if(plateau[i]==1 || plateau[i]==2) {
				nbCasesVulnerables++;
			}
		}
		//System.out.print("(H1 : "+nbCasesVulnerables+" CV )");
		return 1-((double)(nbCasesVulnerables)*1/6);
		
	}
	
	/* Heuristique 2:
	 * L'objectif de cette heuristique est de maximiser
	 * notre nombre de graines
	 */
	private double H2(int numeroJoueur,int[] plateau) {
		double nombreGraine=0;
		double nombreGraineJoueur=0;
		
		int debut=(numeroJoueur==1?0:6);
		int fin=(numeroJoueur==2?6:12);
		
		for(int i=0;i<12;i++) {
			nombreGraine+=plateau[i];
			
			if(i>=debut && i<fin) {
				nombreGraineJoueur+=plateau[i];
			}
		}
		//System.out.print("(H2)");
		return (nombreGraine==0?0:(double)(nombreGraineJoueur)/(double)(nombreGraine));
	}
	
	/* Heuristique 3:
	 * L'objectif de cette heuristique est de minimiser
	 * le nombre de cases vides
	 */
	private double H3(int numeroJoueur,int[] plateau) {
		int nbCasesVides=0;
		
		int debut=(numeroJoueur==1?0:6);
		int fin=(numeroJoueur==2?6:12);
		
		for(int i=debut;i<fin;i++) {
			if(plateau[i]==0) {
				nbCasesVides++;
			}
		}
		//System.out.print("(H3)");
		return 1-((double)(nbCasesVides)*1/6);
		
	}
	
	/* Heuristique 4:
	 * L'objectif de cette heuristique est de valoriser
	 * les etats du jeu ou les graines sont a� droite
	 */
	private double H4(int numeroJoueur,int[] plateau) {
		int nombreGrainesJoueur=0;
		double valeurH4=0;
		
		double[] poids= {0,1/5,2/5,3/5,4/5,1};
		
		int debut=(numeroJoueur==1?0:6);
		int fin=(numeroJoueur==1?6:12);
		
		for(int i=debut;i<fin;i++) {
			nombreGrainesJoueur+=plateau[i];
		}
		
		if(nombreGrainesJoueur!=0) {
			int poidsActuel=0;
			for(int i=debut;i<fin;i++) {
				
				valeurH4+=poids[poidsActuel]*((double)(plateau[i])/(double)(nombreGrainesJoueur));
				poidsActuel++;
			}
		}
		//System.out.print("(H4)");
		return valeurH4;
		
	}
	
	
	/* Heuristique 5:
	 * L'objectif de cette heuristique est de minimiser
	 * le score de l'adversaire
	 */
	private double H5(int scoreJoueur,int scoreAdversaire) {
		//System.out.print("(H5)");
		return 1-((double)scoreAdversaire*((double)1/48));
	}
	
	/* Heuristique 6:
	 * On essaie d'avoir le plus de graines possible dans la case la plus à gauche 
	 */
	private double H6(int numeroJoueur,int[] plateau) {
		int nombreGrainesJoueur=0;
		int debut=(numeroJoueur==1?0:6);
		int fin=(numeroJoueur==1?6:12);
		
		for(int i=debut;i<fin;i++) {
			nombreGrainesJoueur+=plateau[i];
		}
		//System.out.print("(H6)");
		return (double)plateau[debut]/(double)nombreGrainesJoueur;
	}
	
	/* Heuristique 7:
	 * On essaie d'avoir le plus de cases jouables possibles 
	 */
	private double H7(int numeroJoueur,int[] plateau) {
		int nombreCasesJouables=0;
		int debut=(numeroJoueur==1?0:6);
		int fin=(numeroJoueur==1?6:12);
		
		for(int i=debut;i<fin;i++) {
			if(plateau[i]>0) {
				nombreCasesJouables++;
			}
		}
		//System.out.print("(H7)");
		return (1/6)*(double)nombreCasesJouables;
	}
	
	/* Heuristique 8:
	 * L'objectif de cette heuristique est de valoriser
	 * les etats du jeu où les cases de droites sont jouées
	 */
	private double H8(int numeroJoueur,int[] plateau) {
		int nombreGrainesJoueur=0;
		double valeurH8=0;
		
		double[] poids= {1,4/5,3/5,2/5,1/5,0};
		
		int debut=(numeroJoueur==1?0:6);
		int fin=(numeroJoueur==1?6:12);
		
		for(int i=debut;i<fin;i++) {
			nombreGrainesJoueur+=plateau[i];
		}
		
		if(nombreGrainesJoueur!=0) {
			int poidsActuel=0;
			for(int i=debut;i<fin;i++) {
				
				valeurH8+=poids[poidsActuel]*((double)(plateau[i])/(double)(nombreGrainesJoueur));
				poidsActuel++;
			}
		}
		//System.out.print("(H8)");
		return valeurH8;
		
	}
	
	/* Heuristique 9:
	 * L'objectif de cette heuristique est de maximiser
	 * le score du joueur
	 */
	private double H9(int scoreJoueur,int scoreAdversaire) {
		//System.out.print("(H9)");
		return ((double)scoreJoueur*((double)1/48));
	}
	
	public void setHeuristique(String string) {
		if(string.length()==this.heuristique.length) {
			for(int i=0;i<this.heuristique.length;i++) {
				if(string.charAt(i)=='1') {
					this.heuristique[i]=true;
				}else {
					this.heuristique[i]=false;
				}
			}
		}
	}
	
	public String getHeuristique() {
		String h="";
		for(int i=0;i<this.heuristique.length;i++) {
			if(this.heuristique[i]) {
				h+="1";
			}else {
				h+="0";
			}
		}
		return h;
	}
	
	public void setProfondeurMax(int p) {
		this.profondeurMax=p;
	}
	
	public void printHeuristique() {
		for(int i=0;i<this.heuristique.length;i++) {
			System.out.print((this.heuristique[i]?"1":"0"));
		}
	}
	
	public void printPoids() {
		for(int i=0;i<this.heuristique.length;i++) {
			if(this.heuristique[i]) {
				System.out.print(this.poids[i]+":");
			}
			else {
				System.out.print("--:");
			}
		}
	}
	
	public String getMasque() {
		String retour="/";
		for(int i=0;i<this.heuristique.length;i++) {
			retour+=(heuristique[i]?"1":"0");
		}
		return retour;
	}
	
	public String getPoids() {
		NumberFormat formatter = NumberFormat.getInstance(Locale.US);
		String retour="{";
		for(int i=0;i<this.poids.length;i++) {
			retour+=(heuristique[i]?String.format(Locale.US,"%s",this.poids[i]):"--")+";";
		}
		retour+="}";
		return retour;
	}
	
	public void demanderHeuristique() {
		if(this.difficulte!=0) {
			Scanner sc=new Scanner(System.in);
			String saisie;
			do {
				System.out.println("Heuristiques à activer pour <"+this.getNom()+"> (exemple: 111101100)\n >>");
				saisie=sc.nextLine();
			}while(saisie.length()!=this.heuristique.length);
			setHeuristique(saisie);
			System.out.print("Voici les nouvelles heuristiques : ");
			printHeuristique();
			System.out.println();
		}
	}
	
	public void demanderProfondeur() {
		if(this.difficulte==2 || this.difficulte==1) {
			Scanner sc=new Scanner(System.in);
			int saisie;
			do {
				System.out.print("Entrez une profondeur max <"+this.getNom()+">>");
				saisie=sc.nextInt();
			}while(saisie<1);
			setProfondeurMax(saisie);
		}
	}
	
	public void demanderPoids() {
		if(this.difficulte==2 || this.difficulte==1) {
			Scanner sc=new Scanner(System.in);
			double saisie;
			for(int i=0;i<this.poids.length;i++) {
				if(this.heuristique[i]) {
					do {
						System.out.print("Entrez un poid [0;1] pour h"+(i+1)+" <"+this.getNom()+">>");
						saisie=sc.nextDouble();
					}while(saisie>1 || saisie<0);
					this.poids[i]=saisie;
				}
			}
			System.out.print("Voici les nouveaux poids : ");
			printPoids();
		}
	}
	
	/* evaluation d'un etat du jeu en fonction
	 * de la ponderation de chaque heuristique
	 */
	public double evaluation(int numeroJoueur,int[] plateau,int scoreJoueur,int scoreAdversaire) {
		
		double[] heuristiques= {
				(this.heuristique[0]?H1(numeroJoueur,plateau):0),
				(this.heuristique[1]?H2(numeroJoueur,plateau):0),
				(this.heuristique[2]?H3(numeroJoueur,plateau):0),
				(this.heuristique[3]?H4(numeroJoueur,plateau):0),
				(this.heuristique[4]?H5(scoreJoueur,scoreAdversaire):0),
				(this.heuristique[5]?H6(numeroJoueur,plateau):0),
				(this.heuristique[6]?H7(numeroJoueur,plateau):0),
				(this.heuristique[7]?H8(numeroJoueur,plateau):0),
				(this.heuristique[8]?H9(scoreJoueur,scoreAdversaire):0)
		};
		
		double valeurEvaluation=0;
		for(int i=0;i<this.heuristique.length;i++) {
			valeurEvaluation+=heuristiques[i]*this.poids[i];
		}
		return valeurEvaluation;
	}
    
	public double noeudTerminal(int retourSimulerFinPartie, GameManagerAwale arbitreAwale)
	{
		double valeur;
		int score, scoreJoueur, scoreAdversaire;
		
		if(arbitreAwale.joueurActuel().getNumeroJoueur() == 1)
		{
			scoreJoueur =  arbitreAwale.getJoueur1().getScore();
			scoreAdversaire =  arbitreAwale.getJoueur2().getScore();
		}
		else
		{
			scoreJoueur =  arbitreAwale.getJoueur2().getScore();
			scoreAdversaire =  arbitreAwale.getJoueur1().getScore();
		}
		
		if(retourSimulerFinPartie == 0)
		{
			score = 0; //Neutre si ex aequo
		}
		else
		{
			score = differenceScore(scoreJoueur, scoreAdversaire); //Positif si le joueur actuel est gagnant, negatif sinon
		}
    	
		valeur = score * 1000;
    	
		return valeur;
	}
	
	private double profondeurMaxAtteinte(GameManagerAwale arbitreSimule)
	{
		int scoreJoueur, scoreAdversaire, numeroJoueur;
		double valeur = -1;
		
		if(arbitreSimule.joueurActuel() == arbitreSimule.getJoueur1())
	    {
			numeroJoueur = 1;
			scoreJoueur = arbitreSimule.getJoueur1().getScore();
			scoreAdversaire = arbitreSimule.getJoueur2().getScore();
	    }
	    else
	    {
			numeroJoueur = 2;
			scoreJoueur = arbitreSimule.getJoueur2().getScore();
			scoreAdversaire = arbitreSimule.getJoueur1().getScore();
	    }
		
		valeur = evaluation(numeroJoueur, arbitreSimule.getPartie().getPlateau(), scoreJoueur, scoreAdversaire);
		
		return valeur;
	}
	
	public double minimax(int caseJouee, GameManagerAwale arbitreAwaleSimule, int profondeurMax, boolean joueurMax)
	{
		/*********************************************************************************************************
		******* Variables utilisees dans minimax
		*********************************************************************************************************/
		ArrayList coupPossible = new ArrayList<>();
		ArrayList <int[]> historique = new ArrayList<>();
		
		JoueurAwale joueurActuel;
		
		Awale partie;
		
		double valeur = -1;
		
		int retourSimulerFinPartie, score, scoreJoueur, scoreAdversaire, numeroJoueur;
		int[] plateauActuel;
		
		/*********************************************************************************************************
		******* Affectation des variables
		*********************************************************************************************************/		
			//On simule un nouveau GMA, c'est obligatoire, autrement c'est toujours l'arbitreAwaleSimule (le parametre que l'on donne) qui est modifie et donc la simulation n'est pas correcte
		GameManagerAwale arbitreSimuleMinimax = arbitreAwaleSimule.clone();
		
			//On stocke le joueur actuel dans une variable pour gagner en lisibilite par la suite
		joueurActuel = arbitreSimuleMinimax.joueurActuel();
		
			//Compte le nombre d'appels recursifs
		incrementCompteur();
			
			//On stocke la partie dans une variable pour la meme raison que joueurActuel
		partie = arbitreSimuleMinimax.getPartie();
		
			//On modifie le plateau
		partie.modifierPlateau(this.simulerUnCoup(caseJouee, arbitreAwaleSimule));
		
			//Meme chose que partie
		plateauActuel = partie.getPlateau();
		
			//On remplit l'historique
		arbitreSimuleMinimax.stockerEtatMouvement(plateauActuel);
		
			//On determine la liste des coups possible a partir de la caseJouee donnee en parametre
		coupPossible = arbitreSimuleMinimax.determinerCoupPossible(joueurActuel, plateauActuel);
		
		retourSimulerFinPartie = simulerFinPartie(historique, arbitreSimuleMinimax);
	    
		/*********************************************************************************************************
		******* Debut de la methode minimax
		*********************************************************************************************************/
		if(retourSimulerFinPartie != -1)
		{
			valeur = noeudTerminal(retourSimulerFinPartie, arbitreSimuleMinimax);
		}
        
			/*On verifie que la liste des coupPossible est vide
			Si oui, on met une valeur positive quelconque dans la variable valeur et on la renvoie
			Permet de jouer l'unique coup possible dans cette situation (sinon, la valeur renvoyee est -1 et du coup la variable coupOptimise de Jouer Minimax vaut -1 aussi => erreur)*/
		if(coupPossible.isEmpty())
		{
			valeur = 1000;
			return valeur;
		}
        
		if(profondeurMax == 0)
		{
		    valeur = profondeurMaxAtteinte(arbitreSimuleMinimax);

		    return valeur;
		}
         
		/*********************************************************************************************************
		******* Test pour savoir si on est dans le cas ou l'on maximise ou non le score 
		*********************************************************************************************************/
			//Si le joueur est celui qui a fait appel a minimax
		if(joueurMax)
		{
		    valeur = -10000;
		    for(int i = 0; i < coupPossible.size() ; i++)
		    {
				valeur = Math.max(valeur, minimax((int)coupPossible.get(i), arbitreSimuleMinimax, profondeurMax-1, false));
		    }
		}
		else
		{
		    valeur = 10000;
		    for(int i = 0; i < coupPossible.size(); i++)
		    {
				valeur = Math.min(valeur, minimax((int)coupPossible.get(i), arbitreSimuleMinimax, profondeurMax-1, true));
		    }
		}
		
		return valeur;
	}
  
	public int jouerMinimax(GameManagerAwale arbitreAwale, int profondeurMax)
	{
		/*********************************************************************************************************
		******* Variables utilisees dans la methode jouerMinimax
		*********************************************************************************************************/
		long time = System.currentTimeMillis();
		
		GameManagerAwale arbitreAwaleSimule;
		
		JoueurAwale joueurActuel;
		
		Awale partie;
		
		double valeur_optimisee = -10000;
		double valeur;
		
		int coup_optimise = -1;
		int[] plateauActuel;
		
		ArrayList coupPossible = new ArrayList<>();
		
		/*********************************************************************************************************
		******* Affectation des variables
		*********************************************************************************************************/		
			//On simule un GMA pour ne pas modifier le GMA actuel du jeu
		arbitreAwaleSimule = arbitreAwale.clone();
		
		incrementNombreAppel();
		
			//On stocke le joueur actuel dans une variable pour gagner en lisibilite par la suite
		joueurActuel = arbitreAwale.joueurActuel();
				
			//Compte le nombre d'appels recursifs
		incrementCompteur();
					
			//On stocke la partie dans une variable pour la meme raison que joueurActuel
		partie = arbitreAwale.getPartie();
				
			//Meme chose que partie
		plateauActuel = partie.getPlateau();
		
			//On determine la liste des coups possible
		coupPossible = arbitreAwale.determinerCoupPossible(joueurActuel, plateauActuel);
		
		
		/*********************************************************************************************************
		******* Debut de la methode jouerMinimax
		*********************************************************************************************************/	
		for(int i = 0; i < coupPossible.size(); i++) //Pour chaque coup possible a partir de l'etat courant
		{
		    valeur = minimax((int)coupPossible.get(i), arbitreAwaleSimule, profondeurMax, true); 
		    if(valeur > valeur_optimisee)
		    {
				valeur_optimisee = valeur;
				coup_optimise = (int)coupPossible.get(i);
		    }
		}
	    
		/*********************************************************************************************************
		******* Partie concernant l'affichage de divers calculs (temps d'execution, etc)
		*********************************************************************************************************/
		time = System.currentTimeMillis() - time;
		if(arbitreAwale.getVocal()) 
		{
			System.out.println();
			
			//Une fois tous les appels recursifs pour le choix d'une case effectues, on affiche le nombre d'appels recursif de minimax
			System.out.println("Nombre d'appels recursif de minimax : " + getCompteur());
	
			System.out.println("Nombre d'appels recursifs de jouerMinimax : " + getNombreAppel());
			System.out.println("Nombre d'appels recursifs total (minimax + jouerMinimax) : " + (getNombreAppel() + getCompteur()));
	
			
			System.out.println("Temps d'execution de jouerMinimax : " + time + "ms.");
			
			System.out.println();
			
			
		}
		setTotalTime(getTotalTime() + time);
		setTotalNode(getTotalNode() + getNombreAppel() + getCompteur());
		setCompteur(0);
		setNombreAppel(0);
		
		incrementNombreDeCoup();
		
		return coup_optimise;
	}
	
	public double alphaBeta(int caseJouee, GameManagerAwale arbitreAwaleSimule, int profondeurMax, boolean joueurMax, double alpha, double beta)
	{
		/*********************************************************************************************************
		******* Variables utilisees dans alphaBeta
		*********************************************************************************************************/
		ArrayList coupPossible = new ArrayList<>();
		ArrayList <int[]> historique = new ArrayList<>();
		
		JoueurAwale joueurActuel;
		
		Awale partie;
		
		double alphaBeta = -1;
		
		int retourSimulerFinPartie, score;
		int[] plateauActuel;
		
		/*********************************************************************************************************
		******* Affectation des variables
		*********************************************************************************************************/
			//On simule un nouveau un GMA, c'est obligatoire, autrement c'est toujours l'arbitreAwaleSimule (le parametre que l'on donne) qui est modifie et donc la simulation n'est pas correcte
		GameManagerAwale arbitreSimuleMinimax = arbitreAwaleSimule.clone();
		
			//On stocke le joueur actuel dans une variable pour gagner en lisibilite par la suite
		joueurActuel = arbitreSimuleMinimax.joueurActuel();
				
			//Compte le nombre d'appels recursifs
		incrementCompteur();
					
			//On stocke la partie dans une variable pour la meme raison que joueurActuel
		partie = arbitreSimuleMinimax.getPartie();
				
			//On modifie le plateau
		partie.modifierPlateau(this.simulerUnCoup(caseJouee, arbitreAwaleSimule));
				
			//Meme chose que partie
		plateauActuel = partie.getPlateau();
		
			//On remplit l'historique
		arbitreSimuleMinimax.stockerEtatMouvement(plateauActuel);
		
			//On determine la liste des coups possible a partir de la caseJouee donnee en parametre
		coupPossible = arbitreSimuleMinimax.determinerCoupPossible(joueurActuel, plateauActuel);
		
		retourSimulerFinPartie = simulerFinPartie(historique, arbitreSimuleMinimax);
	    
		/*********************************************************************************************************
		******* Debut de la fonction alphaBeta
		*********************************************************************************************************/
		if(retourSimulerFinPartie != -1)
		{
			alphaBeta = noeudTerminal(retourSimulerFinPartie, arbitreSimuleMinimax);
		}
        
			/*On verifie que la liste des coupPossible est vide
			Si oui, on met une valeur positive quelconque dans la variable valeur et on la renvoie.
			Permet de jouer l'unique coup possible dans cette situation (sinon, la valeur renvoyee est -1 et du coup la variable coupOptimise de Jouer Minimax vaut -1 aussi => erreur)*/
		if(coupPossible.isEmpty())
		{
			alphaBeta = 1000;
			return alphaBeta;
		}
        
		if(profondeurMax == 0)
		{
			alphaBeta = profondeurMaxAtteinte(arbitreSimuleMinimax);

		    return alphaBeta;
		}
         
		/*********************************************************************************************************
		******* Test pour savoir si on est dans le cas ou l'on maximise ou non le score 
		*********************************************************************************************************/
			//Si le joueur est celui ayant fait appel a la methode alphaBeta
		if(joueurMax)
		{
		    for(int i = 0; i < coupPossible.size() ; i++)
		    {
				alpha = Math.max(alpha, alphaBeta((int)coupPossible.get(i), arbitreSimuleMinimax, profondeurMax-1, false, alpha, beta));
				if(alpha >= beta)
				{
					return beta;
				}
		    }
		}
		else
		{
		    for(int i = 0; i < coupPossible.size(); i++)
		    {
				beta = Math.min(beta, alphaBeta((int)coupPossible.get(i), arbitreSimuleMinimax, profondeurMax-1, true, alpha, beta));
				if(alpha >= beta)
				{
					return alpha;
				}
		    }
		}
		
		return beta;
	}
	
	public int jouerAlphaBeta(GameManagerAwale arbitreAwale, int profondeurMax)
	{
		long time = System.currentTimeMillis();
		double valeur_optimisee = -10000;
		double valeur;
		
		//On simule un GMA pour ne pas modifier le GMA actuel du jeu
		GameManagerAwale arbitreAwaleSimule;
		
		setNombreAppel(getNombreAppel() + 1);

		ArrayList coupPossible = new ArrayList<>();
		coupPossible = arbitreAwale.determinerCoupPossible(arbitreAwale.joueurActuel(),arbitreAwale.getPartie().getPlateau());
		
		arbitreAwaleSimule = arbitreAwale.clone();
		
		int coup_optimise = -1;
  
		for(int i = 0; i < coupPossible.size(); i++) //Pour chaque coup possible a partir de l'etat courant
		{
		    valeur = alphaBeta((int)coupPossible.get(i), arbitreAwaleSimule, profondeurMax, true, -10000, 10000); 
		    if(valeur > valeur_optimisee)
		    {
				valeur_optimisee = valeur;
				coup_optimise = (int)coupPossible.get(i);
		    }
		}
	    
		time = System.currentTimeMillis() - time;
		if(arbitreAwale.getVocal()) 
		{
			System.out.println();
			
			//Une fois tous les appels recursifs pour le choix d'une case effectues, on affiche le nombre d'appels recursif d'alphaBeta
			System.out.println("Nombre d'appels recursif d'alphaBeta : " + getCompteur());
	
			System.out.println("Nombre d'appels recursifs de jouerAlphaBeta : " + getNombreAppel());
			System.out.println("Nombre d'appels recursifs total (alphaBeta + jouerAlphaBeta) : " + (getNombreAppel() + getCompteur()));
	
			
			System.out.println("Temps d'execution de jouerAlphaBeta : " + time + "ms.");
			
			System.out.println();
			
			
		}
		setTotalTime(getTotalTime() + time);
		setTotalNode(getTotalNode() + getNombreAppel() + getCompteur());
		setNombreAppel(0);
		setCompteur(0);
		
		setNombreDeCoup(getNombreDeCoup() + 1);
		
		return coup_optimise;
	}
     
	public int choisirUnCoup(GameManagerAwale arbitreAwale)
	{
		int caseJouee = -1;
		
		if(difficulte==2) 
		{
			do {
				caseJouee = jouerAlphaBeta(arbitreAwale,this.profondeurMax==-1?4:this.profondeurMax);
			}while( !arbitreAwale.verifierCoupValide(arbitreAwale.joueurActuel(), caseJouee, arbitreAwale.getPartie().getPlateau()) );
			if(arbitreAwale.getVocal()) {
				System.out.println("case jouee : " + caseJouee);
			}
		}
		else if(difficulte==1) 
		{
//			double[] resultat=testMinMax(arbitreAwale,this.profondeurMax<0?4:profondeurMax,true);
//			caseJouee=(int)resultat[1];
//

			do {
				caseJouee = jouerMinimax(arbitreAwale,this.profondeurMax==-1?4:this.profondeurMax);
			}while( !arbitreAwale.verifierCoupValide(arbitreAwale.joueurActuel(), caseJouee, arbitreAwale.getPartie().getPlateau()) );
			if(arbitreAwale.getVocal()) {
				System.out.println("case jouee : " + caseJouee);
			}
		}
		else if(difficulte==0)
		{
			Random rand = new Random();
			do {
				caseJouee = rand.nextInt(6)+this.getMin();
		    }while( !arbitreAwale.verifierCoupValide(this,caseJouee,arbitreAwale.getPartie().getPlateau()) );
		}
		return caseJouee;
	}
	
	/* algo MinMax basé sur le pseudo code trouvable sur Wikipédia
	 * lien : https://en.wikipedia.org/wiki/Minimax#Pseudocode
	 * retour : couple (valeur,coup) où valeur est la valeur de minmax, et coup est le meilleur coup pour la profondeur p
	 */
	public double[] testMinMax(GameManagerAwale arbitre,int profondeur,boolean max){
		double[] retour= {0,0};int valeur=0,coup=1;int infini=1000;
		JoueurAwale joueur,adversaire;
		if(max) {
			joueur=arbitre.getJoueur1()==this?arbitre.getJoueur1():arbitre.getJoueur2();
			adversaire=arbitre.getJoueur1()==this?arbitre.getJoueur2():arbitre.getJoueur1();
		}else {
			joueur=arbitre.getJoueur1()==this?arbitre.getJoueur2():arbitre.getJoueur1();
			adversaire=arbitre.getJoueur1()==this?arbitre.getJoueur1():arbitre.getJoueur2();
		}
		//System.out.println("Je suis "+this.getNom()+", valeur Max :"+max+", nom de mon adversaire : "+adversaire.getNom());
		//si on est sur une fin de partie (ou profondeur nulle) on renvoie la valeur du plateau
		if(profondeur==0 || arbitre.finPartie()) {
			
			retour[valeur]=this.evaluation(joueur.numeroJoueur,arbitre.getPlateau(),joueur.score,adversaire.score);
			retour[coup]=-1;
//			System.out.print("l'évaluation du plateau : ");
//			arbitre.getPartie().printPlateau();
//			System.out.println(" ---> "+retour[valeur]);
		}
		
		//on n'est pas sur une fin de partie, on va devoir simuler des coups possibles
		else {
			ArrayList<Integer> coupsPossibles=arbitre.determinerCoupPossible(joueur,arbitre.getPlateau()); //on récupère les coups possibles
			//System.out.println("Mes coups possibles : "+coupsPossibles);
			GameManagerAwale clone;
			double[] retourMM;
			
			if(max) {//le joueur actuel est celui à l'origine de l'appel à minmax, on doit donc maximiser minmax
				retour[valeur]=-infini;
				for(int i=0;i<coupsPossibles.size();i++) {//pour chaque coup possible, on appelle recursivement minmax dessus
					clone=arbitre.clone();
					this.simulerUnCoup(coupsPossibles.get(i),clone);
					retourMM=testMinMax(clone,profondeur-1,false);
					
					//si le coup simulé nous donne une meilleure valeur, on la récupère et on enregistre le coup à l'origine de la valeur
					if(retour[valeur]<retourMM[valeur]){
						retour[valeur]=retourMM[valeur];
						retour[coup]=coupsPossibles.get(i);
					}
				}
			}
			//le joueur actuel est l'adversaire, on doit minimiser minmax
			//(même fonctionnement que juste au dessus)
			else {
				retour[valeur]=infini;
				for(int i=0;i<coupsPossibles.size();i++) {
					clone=arbitre.clone();
					this.simulerUnCoup(coupsPossibles.get(i),clone);
					retourMM=testMinMax(clone,profondeur-1,true);
					
					if(retour[valeur]>retourMM[valeur]){
						retour[valeur]=retourMM[valeur];
						retour[coup]=coupsPossibles.get(i);
					}
				}
			}
		}
		return retour;
	}
}
