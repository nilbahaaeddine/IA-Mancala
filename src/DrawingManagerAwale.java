package mancala;

import javax.swing.*;
import java.awt.*;

class DrawingManagerAwale extends JFrame {
	private static DrawingManagerAwale instance = null;

	static DrawingManagerAwale getInstance() {
		if (instance == null)
			instance = new DrawingManagerAwale();
		return instance;
	}

	private DrawingManagerAwale() {
		Dimension tailleEcran = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		ImageIcon image = new ImageIcon("icone.png");
		int hauteur = (int) tailleEcran.getHeight();
		int largeur = (int) tailleEcran.getWidth();
		this.setTitle("Awale");
		this.setSize(largeur / 2, hauteur / 2);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BorderLayout(0, 0));
		this.setIconImage(image.getImage());
	}
}
