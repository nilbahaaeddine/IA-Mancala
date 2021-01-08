package mancala;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

class ChoixJoueur {
	private JTextField nomJoueur1;
	private JTextField nomJoueur2;
	private GameManagerAwale arbitre;

	ChoixJoueur(GameManagerAwale arbitre) {
		this.arbitre = arbitre;
		initialize();
	}

	private void initialize() {
		//Création des panel
		JPanel all = new JPanel(new BorderLayout(0, 0));
		SPanel menu1 = new SPanel();
		SPanel j1 = new SPanel();
		SPanel j2 = new SPanel();
		SPanel okPanel = new SPanel();

		//Set layout des panel
		menu1.setLayout(new GridLayout(0, 2, 0, 0));
		j1.setLayout(new GridLayout(4, 0, 0, 0));
		j2.setLayout(new GridLayout(4, 0, 0, 0));

		//Ajout des panel
		all.add(menu1, BorderLayout.CENTER);
		menu1.add(j1);
		menu1.add(j2);
		all.add(okPanel, BorderLayout.SOUTH);

		//Création des élements de la fenêtre
		JLabel joueur1 = new JLabel("J1");
		JRadioButton j1Humain = new JRadioButton("Humain", true);
		JRadioButton j1IA = new JRadioButton("IA");
		ButtonGroup j1Radio = new ButtonGroup();
		JLabel joueur2 = new JLabel("J2");
		JRadioButton j2Humain = new JRadioButton("Humain", true);
		JRadioButton j2IA = new JRadioButton("IA");
		ButtonGroup j2Radio = new ButtonGroup();
		SButton okSelectionJoueurs = new SButton("Suivant");

		//Ajout des élements dans les panel
		j1.add(joueur1);
		j1.add(j1Humain);
		j1.add(j1IA);
		j1Radio.add(j1Humain);
		j1Radio.add(j1IA);
		j2.add(joueur2);
		j2.add(j2Humain);
		j2.add(j2IA);
		j2Radio.add(j2Humain);
		j2Radio.add(j2IA);
		okPanel.add(okSelectionJoueurs);

		//Plus
		joueur1.setHorizontalAlignment(SwingConstants.CENTER);
		j1Humain.setBackground(Theme.BACKGROUND_COLOR);
		j1Humain.setHorizontalAlignment(SwingConstants.CENTER);
		j1IA.setBackground(Theme.BACKGROUND_COLOR);
		j1IA.setHorizontalAlignment(SwingConstants.CENTER);
		joueur2.setHorizontalAlignment(SwingConstants.CENTER);
		j2Humain.setBackground(Theme.BACKGROUND_COLOR);
		j2Humain.setHorizontalAlignment(SwingConstants.CENTER);
		j2IA.setBackground(Theme.BACKGROUND_COLOR);
		j2IA.setHorizontalAlignment(SwingConstants.CENTER);

		//Nom joueurs et placeholder
		nomJoueur1 = new JTextField();
		nomJoueur1.setHorizontalAlignment(SwingConstants.CENTER);
		nomJoueur1.setText("Nom joueur 1");
		nomJoueur1.setForeground(Color.GRAY);
		nomJoueur1.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (nomJoueur1.getText().equals("Nom joueur 1")) {
					nomJoueur1.setText("");
					nomJoueur1.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (nomJoueur1.getText().isEmpty()) {
					nomJoueur1.setForeground(Color.GRAY);
					nomJoueur1.setText("Nom joueur 1");
				}
			}
		});
		j1.add(nomJoueur1);
		nomJoueur1.setColumns(10);

		nomJoueur2 = new JTextField();
		nomJoueur2.setHorizontalAlignment(SwingConstants.CENTER);
		nomJoueur2.setText("Nom joueur 2");
		nomJoueur2.setForeground(Color.GRAY);
		nomJoueur2.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (nomJoueur2.getText().equals("Nom joueur 2")) {
					nomJoueur2.setText("");
					nomJoueur2.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (nomJoueur2.getText().isEmpty()) {
					nomJoueur2.setForeground(Color.GRAY);
					nomJoueur2.setText("Nom joueur 2");
				}
			}
		});
		j2.add(nomJoueur2);
		nomJoueur2.setColumns(10);

		//ActionListener Button
		okSelectionJoueurs.addActionListener(arg0 -> {
			String nomJ1 = nomJoueur1.getText(), nomJ2 = nomJoueur2.getText();
			boolean isJ1IA = j1IA.isSelected(), isJ2IA = j2IA.isSelected();
			if (isJ1IA && isJ2IA) {//2 IA
				arbitre.setNbrJoueursHumain(0);
				new ChoixDifficulte(nomJ1, nomJ2, 2, arbitre);
			} else if (isJ1IA || isJ2IA) {//1 IA
				arbitre.setNbrJoueursHumain(1);
				new ChoixDifficulte(nomJ1, nomJ2, 1, arbitre);
			} else {//0 IA
				arbitre.setNbrJoueursHumain(2);
				arbitre.initJoueurs(nomJ1, nomJ2);
				Partie partie = new Partie(nomJ1, nomJ2);
				arbitre.lancerThread(partie);
			}
		});

		//Ajout du panel principal et affichage de l'instance (singleton)
		DrawingManagerAwale.getInstance().setContentPane(all);
		DrawingManagerAwale.getInstance().setVisible(true);
	}
}
