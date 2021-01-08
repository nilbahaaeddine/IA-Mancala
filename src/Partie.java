package mancala;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class Partie {
	private int coupActu = -1;
	private ArrayList<SButton> buttonListGame;
	private ArrayList<SButton> buttonListMenu;
	private ArrayList<JLabel> nameList;
	private ArrayList<JLabel> scoreList;

	int getCoupActu() {
		return coupActu;
	}

	void setCoupActu(int coupActu) {
		this.coupActu = coupActu;
	}

	ArrayList<SButton> getButtonListGame() {
		return buttonListGame;
	}

	ArrayList<JLabel> getNameList() {
		return nameList;
	}

	ArrayList<JLabel> getScoreList() {
		return scoreList;
	}

	private SButton getButtonGame(int i) {
		return buttonListGame.get(i);
	}

	private SButton getButtonMenu(int i) {
		return buttonListMenu.get(i);
	}

	Partie(String nomJ1, String nomJ2) {
		initialize(nomJ1, nomJ2);
	}

	void actualiserPartie() {
		String[] val = new String[12];
		int[] valInt = GameManagerAwale.getInstance().getPartie().etatActuel();
		for (int i = 0; i < 12; i++) {
			val[i] = Integer.toString(valInt[i]);
			getButtonGame(i).setText(val[i]);
		}
	}

	private void initialize(String nomJ1, String nomJ2) {
		//Partie panel
		JPanel all = new JPanel(new BorderLayout(0, 0));

		//Menu panel
		JPanel menu = new SPanel();
		menu.setLayout(new GridLayout(1, 3, 55, 0));
		all.add(menu, BorderLayout.NORTH);

		buttonListMenu = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			SButton btn = new SButton("0");
			buttonListMenu.add(btn);
		}

		getButtonMenu(0).setText("Menu");
		getButtonMenu(1).setText("Sauvegarder partie");
		getButtonMenu(2).setText("Quitter");

		for (int i = 0; i < 3; i++) {
			menu.add(getButtonMenu(i));
		}

		//Partie panel
		SPanel game = new SPanel();
		all.add(game);
		game.setLayout(new GridLayout(2, 1, 0, 5));

		JPanel j2 = new SPanel();
		game.add(j2);
		j2.setLayout(new GridLayout(0, 1, 0, 5));

		JPanel j2_info = new SPanel();
		j2.add(j2_info);
		j2_info.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JPanel j2_score = new SPanel();
		j2.add(j2_score);
		j2_score.setLayout(new GridLayout(1, 6, 10, 0));

		JPanel j2_cases = new SPanel();
		j2.add(j2_cases);
		j2_cases.setLayout(new GridLayout(1, 6, 10, 0));

		buttonListGame = new ArrayList<>();
		for (int i = 0; i < 12; i++) {
			SButton btn = new SButton("4");
			final int fi = i;
			btn.addActionListener(arg0 -> setCoupActu(fi));
			buttonListGame.add(btn);
		}

		nameList = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			JLabel nomJoueur = new JLabel("Joueur " + (i + 1));
			nameList.add(nomJoueur);
		}
		nameList.get(0).setText(nomJ1);
		nameList.get(1).setText(nomJ2);

		scoreList = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			JLabel scoreJoueur = new JLabel("0");
			scoreList.add(scoreJoueur);
		}

		JPanel j1 = new SPanel();
		game.add(j1);
		j1.setLayout(new GridLayout(0, 1, 0, 5));

		JPanel j1_cases = new SPanel();
		j1.add(j1_cases);
		j1_cases.setLayout(new GridLayout(1, 6, 10, 0));

		JPanel j1_score = new SPanel();
		j1.add(j1_score);
		j1_score.setLayout(new GridLayout(1, 6, 10, 0));

		JPanel j1_info = new SPanel();
		j1.add(j1_info);
		j1_info.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		// Ajouter cases J2
		for (int i = 11; i > 5; i--) {
			j2_cases.add(getButtonGame(i));
		}

		// Ajouter cases J1
		for (int i = 0; i < 6; i++) {
			j1_cases.add(getButtonGame(i));
		}

		SPanel nomJ1Panel = new SPanel();
		SPanel scoreJ1Panel = new SPanel();
		SPanel nomJ2Panel = new SPanel();
		SPanel scoreJ2Panel = new SPanel();

		JLabel nomJoueur1 = new JLabel("Nom : ");
		JLabel scoreJoueur1 = new JLabel("Score : ");
		JLabel nomJoueur2 = new JLabel("Nom : ");
		JLabel scoreJoueur2 = new JLabel("Score : ");

		nomJ1Panel.add(nomJoueur1);
		nomJ1Panel.add(nameList.get(0));
		scoreJ1Panel.add(scoreJoueur1);
		scoreJ1Panel.add(scoreList.get(0));

		nomJ2Panel.add(nomJoueur2);
		nomJ2Panel.add(nameList.get(1));
		scoreJ2Panel.add(scoreJoueur2);
		scoreJ2Panel.add(scoreList.get(1));

		j1_info.add(nomJ1Panel);
		j1_info.add(scoreJ1Panel);

		j2_info.add(nomJ2Panel);
		j2_info.add(scoreJ2Panel);

		SButton btnPause = new SButton("Pause");
		SButton btnPlay = new SButton("Play");
		SPanel footer = new SPanel();
		footer.add(btnPause);
		footer.add(btnPlay);

		if (GameManagerAwale.getInstance().getNbrJoueursHumain() == 0) {
			all.add(footer, BorderLayout.SOUTH);
		}

		//ActionListener Button
		getButtonMenu(0).addActionListener(arg0 -> {
			if (GameManagerAwale.getInstance().isAnimated()) {
				DrawingManager.showDialog("Attendez la fin du tour", "Attention");
			} else {
				int n = JOptionPane.showConfirmDialog(null, "Voulez vous sauvegarder avant d'ouvrir le menu ?", "Menu", JOptionPane.YES_NO_OPTION);
				if (n == 0) {
					String nom = JOptionPane.showInputDialog(null, "Entrer le nom de votre sauvegarde", "Menu", JOptionPane.QUESTION_MESSAGE);
					if (nom != null) {
						GameManagerAwale.getInstance().sauvegarder(nom);
					}
				}
				GameManagerAwale.getInstance().arreterThread();
				GameManagerAwale.getInstance().resetPartie();
				GameManagerAwale.getInstance().setPartiePaused(false);
				new ChoixPartie(GameManagerAwale.getInstance());
			}
		});

		getButtonMenu(1).addActionListener(arg0 -> {
			if (GameManagerAwale.getInstance().isAnimated()) {
				DrawingManager.showDialog("Attendez la fin du tour", "Attention");
			} else {
				String nom = JOptionPane.showInputDialog(null, "Entrez le nom de sauvegarde :", "Sauvegarder partie", JOptionPane.QUESTION_MESSAGE);
				if (nom != null) {
					GameManagerAwale.getInstance().sauvegarder(nom);
				}
			}
		});

		getButtonMenu(2).addActionListener(arg0 -> {
			int n = JOptionPane.showConfirmDialog(null, "Voulez vous sauvegarder avant de quitter la partie ?", "Quitter partie", JOptionPane.YES_NO_OPTION);
			if (n == 0) {
				String nom = JOptionPane.showInputDialog(null, "Voulez vous sauvegarder la partie ?", "Quitter partie", JOptionPane.QUESTION_MESSAGE);
				if (nom != null) {
					GameManagerAwale.getInstance().sauvegarder(nom);
				}
			}
			System.exit(0);
		});

		btnPause.addActionListener(arg0 -> GameManagerAwale.getInstance().setPartiePaused(true));

		btnPlay.addActionListener(arg0 -> GameManagerAwale.getInstance().setPartiePaused(false));

		//Ajout du panel principal et affichage de l'instance (singleton)
		DrawingManagerAwale.getInstance().setContentPane(all);
		DrawingManagerAwale.getInstance().setVisible(true);
	}
}
