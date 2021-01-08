package mancala;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

class ChoixDifficulte {
	private ArrayList<JCheckBox> boxListJ1;
	private ArrayList<JCheckBox> boxListJ2;

	private JCheckBox getBoxJ1(int i) {
		return boxListJ1.get(i);
	}

	private JCheckBox getBoxJ2(int i) {
		return boxListJ2.get(i);
	}

	ChoixDifficulte(String nomJ1, String nomJ2, int nbrIA, GameManagerAwale arbitre) {
		initialize(nomJ1, nomJ2, nbrIA, arbitre);
	}

	private void initialize(String nomJ1, String nomJ2, int nbrIA, GameManagerAwale arbitre) {
		//Tableaux de difficulté
		String[] difficulteIA = {"IA naive (random)", "IA minimax", "IA alphaBeta"};
		String[] difficulteHumain = {"Facile", "Moyen", "Difficile"};
		AtomicBoolean labelPaneJ1 = new AtomicBoolean(false);
		AtomicBoolean labelPaneJ2 = new AtomicBoolean(false);

		//Création des panel
		JPanel all = new JPanel(new BorderLayout(0, 0));
		SPanel joueur1Panel = new SPanel();
		SPanel joueur2Panel = new SPanel();
		SPanel boutonSuivantPanel = new SPanel();
		SPanel heuristiqueJoueur1Panel = new SPanel();
		SPanel heuristiqueJoueur2Panel = new SPanel();
		SPanel aidePanel = new SPanel();
		SPanel niveauDifficulte1Panel = new SPanel();
		SPanel niveauDifficulte2Panel = new SPanel();

		//Set layout des panel
		joueur1Panel.setLayout(new GridLayout(3, 1, 0, 0));
		joueur2Panel.setLayout(new GridLayout(3, 1, 0, 0));
		aidePanel.setLayout(new GridLayout(3, 1, 0, 0));
		niveauDifficulte2Panel.setLayout(new GridLayout(1, 2, 0, 0));

		//Ajout des panel
		all.add(boutonSuivantPanel, BorderLayout.SOUTH);

		//Spinner model
		SpinnerModel spinnerModelMinimaxJ1 = new SpinnerNumberModel(0, 0, 8, 1);
		SpinnerModel spinnerModelAlphaBetaJ1 = new SpinnerNumberModel(0, 0, 12, 1);
		SpinnerModel spinnerModelMinimaxJ2 = new SpinnerNumberModel(0, 0, 8, 1);
		SpinnerModel spinnerModelAlphaBetaJ2 = new SpinnerNumberModel(0, 0, 12, 1);

		//Création des élements de la fenêtre
		JComboBox<String> typeIAJoueur1 = new JComboBox<>(difficulteIA);
		JComboBox<String> typeIAJoueur2 = new JComboBox<>(difficulteIA);
		JComboBox<String> typeIA = new JComboBox<>(difficulteHumain);
		JButton suivantButton = new SButton("Suivant");
		JSpinner profondeurJoueur1 = new JSpinner(spinnerModelAlphaBetaJ1);
		profondeurJoueur1.setEditor(new JSpinner.DefaultEditor(profondeurJoueur1));
		JSpinner profondeurJoueur2 = new JSpinner(spinnerModelAlphaBetaJ2);
		profondeurJoueur2.setEditor(new JSpinner.DefaultEditor(profondeurJoueur2));
		JLabel typeIALabel = new JLabel("Type IA :");
		JLabel profondeurLabel = new JLabel("Profondeur :");
		JLabel heuristiqueLabel = new JLabel("Heuristique :");

		//Ajout des élements dans les panel
		boutonSuivantPanel.add(suivantButton);

		if (nbrIA == 1) {
			all.add(niveauDifficulte1Panel, BorderLayout.CENTER);
			niveauDifficulte1Panel.add(typeIA);
		}

		if (nbrIA == 2) {
			all.add(niveauDifficulte2Panel, BorderLayout.CENTER);
			all.add(aidePanel, BorderLayout.WEST);

			aidePanel.add(typeIALabel);
			aidePanel.add(profondeurLabel);
			aidePanel.add(heuristiqueLabel);

			niveauDifficulte2Panel.add(joueur1Panel);
			niveauDifficulte2Panel.add(joueur2Panel);

			//J1
			profondeurJoueur1.setValue(4);
			typeIAJoueur1.setSelectedIndex(-1);

			joueur1Panel.add(typeIAJoueur1);
			joueur1Panel.add(profondeurJoueur1);
			joueur1Panel.add(heuristiqueJoueur1Panel);

			boxListJ1 = new ArrayList<>();
			for (int i = 0; i < 9; i++) {
				JCheckBox box = new JCheckBox();
				box.setBackground(Theme.BACKGROUND_COLOR);
				boxListJ1.add(box);
			}

			for (int i = 0; i < 9; i++) {
				getBoxJ1(i).setText("H" + (i + 1));
				heuristiqueJoueur1Panel.add(getBoxJ1(i));
			}

			//J2
			profondeurJoueur2.setValue(4);
			typeIAJoueur2.setSelectedIndex(-1);

			joueur2Panel.add(typeIAJoueur2);
			joueur2Panel.add(profondeurJoueur2);
			joueur2Panel.add(heuristiqueJoueur2Panel);

			boxListJ2 = new ArrayList<>();
			for (int i = 0; i < 9; i++) {
				JCheckBox box = new JCheckBox();
				box.setBackground(Theme.BACKGROUND_COLOR);
				boxListJ2.add(box);
			}

			for (int i = 0; i < 9; i++) {
				getBoxJ2(i).setText("H" + (i + 1));
				heuristiqueJoueur2Panel.add(getBoxJ2(i));
			}
		}

		//ActionListener ComboBox
		//J1
		typeIAJoueur1.addActionListener(e -> {
			switch (typeIAJoueur1.getSelectedIndex()) {
				case 0:
					profondeurJoueur1.setVisible(false);
					heuristiqueJoueur1Panel.setVisible(false);
					labelPaneJ1.set(true);
					if (labelPaneJ2.get()) {
						profondeurLabel.setVisible(false);
						heuristiqueLabel.setVisible(false);
					}
					break;
				case 1:
					profondeurJoueur1.setVisible(true);
					profondeurJoueur1.setModel(spinnerModelMinimaxJ1);
					profondeurJoueur1.setValue(4);
					heuristiqueJoueur1Panel.setVisible(true);
					labelPaneJ1.set(false);
					profondeurLabel.setVisible(true);
					heuristiqueLabel.setVisible(true);
					break;
				case 2:
					profondeurJoueur1.setVisible(true);
					profondeurJoueur1.setModel(spinnerModelAlphaBetaJ1);
					profondeurJoueur1.setValue(4);
					heuristiqueJoueur1Panel.setVisible(true);
					labelPaneJ1.set(false);
					profondeurLabel.setVisible(true);
					heuristiqueLabel.setVisible(true);
					break;
			}
		});

		//J2
		typeIAJoueur2.addActionListener(e -> {
			switch (typeIAJoueur2.getSelectedIndex()) {
				case 0:
					profondeurJoueur2.setVisible(false);
					heuristiqueJoueur2Panel.setVisible(false);
					labelPaneJ2.set(true);
					if (labelPaneJ1.get()) {
						profondeurLabel.setVisible(false);
						heuristiqueLabel.setVisible(false);
					}
					break;
				case 1:
					profondeurJoueur2.setVisible(true);
					profondeurJoueur2.setModel(spinnerModelMinimaxJ2);
					profondeurJoueur2.setValue(4);
					heuristiqueJoueur2Panel.setVisible(true);
					labelPaneJ2.set(false);
					profondeurLabel.setVisible(true);
					heuristiqueLabel.setVisible(true);

					break;
				case 2:
					profondeurJoueur2.setVisible(true);
					profondeurJoueur2.setModel(spinnerModelAlphaBetaJ2);
					profondeurJoueur2.setValue(4);
					heuristiqueJoueur2Panel.setVisible(true);
					labelPaneJ2.set(false);
					profondeurLabel.setVisible(true);
					heuristiqueLabel.setVisible(true);
					break;
			}
		});

		//ActionListener Button
		suivantButton.addActionListener(arg0 -> {
			if (typeIAJoueur1.getSelectedIndex() == -1 || typeIAJoueur2.getSelectedIndex() == -1) {
				DrawingManager.showDialog("Sélectionner le type de l'IA", "Attention");
			} else {
				int diff1, diff2;
				int profondeur1 = 4, profondeur2 = 4;
				StringBuilder heuristique1 = new StringBuilder();
				StringBuilder heuristique2 = new StringBuilder();

				diff1 = typeIAJoueur1.getSelectedIndex();
				diff2 = typeIAJoueur2.getSelectedIndex();

				if (nbrIA == 2)//2 IA
				{
					//Récupération de la profondeur
					profondeur1 = (int) profondeurJoueur1.getValue();
					profondeur2 = (int) profondeurJoueur2.getValue();

					//Création de la chaine des heuristiques en binaire
					for (JCheckBox box : boxListJ1) {
						if (box.isSelected())
							heuristique1.append("1");
						else
							heuristique1.append("0");
					}
					for (JCheckBox box : boxListJ2) {
						if (box.isSelected())
							heuristique2.append("1");
						else
							heuristique2.append("0");
					}

					//Vérification si y a au moins une heuristique sélectionnée
					if ((heuristique1.toString().equals("000000000") && typeIAJoueur1.getSelectedIndex() != 0)
							|| (heuristique2.toString().equals("000000000") && typeIAJoueur2.getSelectedIndex() != 0)) {
						DrawingManager.showDialog("Aucune heuristique sélectionnée", "Attention");
					} else {
						arbitre.initJoueurs(nomJ1, diff1, nomJ2, diff2, profondeur1, profondeur2, heuristique1, heuristique2);
						Partie partie = new Partie(nomJ1, nomJ2);
						arbitre.lancerThread(partie);
					}
				} else//1 IA
				{
					arbitre.initJoueurs(nomJ1, diff1, nomJ2, diff2, profondeur1, profondeur2, heuristique1, heuristique2);
					Partie partie = new Partie(nomJ1, nomJ2);
					arbitre.lancerThread(partie);
				}
			}
		});

		//Ajout du panel principal et affichage de l'instance (singleton)
		DrawingManagerAwale.getInstance().setContentPane(all);
		DrawingManagerAwale.getInstance().setVisible(true);
	}
}