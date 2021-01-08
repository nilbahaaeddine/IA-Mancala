package mancala;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SPanel extends JPanel {
	private Image backgroundImage;

	SPanel() {
		this(Theme.BACKGROUND_COLOR);
	}

	private SPanel(Color bgColor) {
		super();
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setBackground(bgColor);
		backgroundImage = null;
	}

	public SPanel(Image backgroundImage) {
		super();
		this.backgroundImage = backgroundImage;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (backgroundImage == null)
			return;
		g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
	}
}
