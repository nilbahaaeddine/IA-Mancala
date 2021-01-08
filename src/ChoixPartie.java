package mancala;

import javax.swing.*;
import java.awt.*;

class ChoixPartie {
	private GameManagerAwale arbitre;

	ChoixPartie(GameManagerAwale arbitre) {
		this.arbitre = arbitre;
		initialize();
	}

	private void initialize() {
		//Création des panel
		JPanel all = new JPanel(new BorderLayout(0, 0));
		SPanel btnPanel = new SPanel();
		SPanel comboPanel = new SPanel();

		//Set layout des panel
		btnPanel.setLayout(new GridLayout(1, 2, 0, 0));

		//Ajout des panel
		all.add(btnPanel, BorderLayout.CENTER);
		all.add(comboPanel, BorderLayout.SOUTH);

		//Création des élements de la fenêtre
		SButton loadBtn = new SButton("Charger une partie");
		SButton goBtn = new SButton("Lancer une nouvelle partie");
		JComboBox test = new JComboBox();
		SButton btn = new SButton("Suivant");

		//Ajout des élements dans les panel
		btnPanel.add(loadBtn);
		btnPanel.add(goBtn);
		comboPanel.add(test);
		comboPanel.add(btn);

		//Plus
		int nbSaves = arbitre.bla1(test);
		test.setSelectedIndex(-1);
		comboPanel.setVisible(false);

		//ActionListener Button
		goBtn.addActionListener(arg0 -> new ChoixJoueur(arbitre));

		loadBtn.addActionListener(arg0 -> {
			if (nbSaves != 0) {
				comboPanel.setVisible(true);
			} else {
				DrawingManager.showDialog("Aucune sauvegarde disponible", "Attention");
			}
		});

		btn.addActionListener(arg0 -> {
			GameManagerAwale.getInstance().chargerPartieGraphique(test.getSelectedIndex());
			GameManagerAwale.getInstance().ehem(GameManagerAwale.getInstance());
		});

		//Ajout du panel principal et affichage de l'instance (singleton)
		DrawingManagerAwale.getInstance().setContentPane(all);
		DrawingManagerAwale.getInstance().setVisible(true);
	}
}
