package mancala;

import javax.swing.*;

class DrawingManager {
	private GameManagerAwale arbitre;

	DrawingManager(GameManagerAwale arbitre) {
		this.arbitre = arbitre;
	}

	void go() {
		new ChoixPartie(arbitre);
	}

	static void showDialog(String message, String titre) {
		JOptionPane.showMessageDialog(null, message, titre, JOptionPane.WARNING_MESSAGE);
	}
}
