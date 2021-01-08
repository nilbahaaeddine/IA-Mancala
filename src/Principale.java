package mancala;

import java.util.Scanner;

public class Principale {

	public static void main(String[] args) {
		int choixPrincipal, choixTournois;
		Scanner sc = new Scanner(System.in);
		System.out.println("----MENU PRINCIPAL----");
		System.out.println("0. Jouer");
		System.out.println("1. Mode Tournois (IA vs IA)");
		do {
			System.out.print("\nVotre choix >> ");
			choixPrincipal = sc.nextInt();
		} while (choixPrincipal < 0 || choixPrincipal > 1);

		if (choixPrincipal == 0) {
			System.out.println("\n----Choisissez le mode d'affichage----");
			System.out.println("0. Console");
			System.out.println("1. Graphique");

			do {
				System.out.print("\nVotre choix >> ");
				choixPrincipal = sc.nextInt();
			} while (choixPrincipal < 0 || choixPrincipal > 1);

			GameManagerAwale arbitreAwale = new GameManagerAwale(choixPrincipal);

			switch (choixPrincipal) {
				//Partie sur console
				case 0:
					arbitreAwale.lancerUneNouvellePartie(true);
					arbitreAwale.getGagnant();
					break;
				//Partie graphique
				case 1:
					DrawingManager game = new DrawingManager(arbitreAwale);
					game.go();
					break;
			}
		} else {
			System.out.println("\n----Choisissez le mode de tournois----");
			System.out.println("0. Tester un algorithme précis (choix des heuristiques, poids, etc...)");
			System.out.println("1. Tester les heuristiques");
			System.out.println("2. Tester la profondeur");
			System.out.println("3. Tester le temps d'exécution par rapport à la profondeur");
			System.out.println("4. Tester le nombre de noeuds parcourus par rapport à la profondeur");
			do {
				System.out.print("\nVotre choix >> ");
				choixTournois = sc.nextInt();
			} while (choixTournois < 0 || choixTournois > 4);

			JoueurAwaleIA j1;
			JoueurAwaleIA j2;
			j2 = new JoueurAwaleIA("IA_TOURNOIS_2", 0, 2, 6, 11, 24);
			if (choixTournois < 3) {
				j1 = new JoueurAwaleIA("IA_TOURNOIS_1", 0, 1, 0, 5, 24);
			} else {
				j1 = new JoueurAwaleIA("IA_TOURNOIS_1", 0, 1, 0, 5, 24, 0);
			}

			Tournois tournois = new Tournois(j1, j2);

			if (choixTournois == 0) {
				tournois.lancer();
				tournois.saveCSV(null);
			}

			if (choixTournois == 1 || choixTournois == 2) {
				System.out.println("Tester combien d'heuristique ?");
				do {
					System.out.print("\nVotre choix >> ");
					choixPrincipal = sc.nextInt();
				} while (choixPrincipal < 0 || choixPrincipal > 512);
			}

			if (choixTournois == 1) {
				System.out.println("Gagnant : " + tournois.testerHeuristiques(choixPrincipal));
			} else if (choixTournois == 2) {
				System.out.println("profondeur gagnante : " + tournois.testerProfondeur(choixPrincipal));
			}

			if (choixTournois == 3 || choixTournois == 4) {
				System.out.println("Tester jusqu'à quelle profondeur ?");
				do {
					System.out.print("\nVotre choix >> ");
					choixPrincipal = sc.nextInt();
				} while (choixPrincipal < 0 || choixPrincipal > 10);

				if (choixTournois == 3) {
					tournois.testerProfondeurXTemps(choixPrincipal);
				}

				if (choixTournois == 4) {
					tournois.testerProfondeurXNombreDeNoeuds(choixPrincipal);
				}
			}
		}
		sc.close();
	}
}
