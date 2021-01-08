package mancala;

import java.awt.*;

public class Theme {
	// Font
	private static final Font FONT_DEFAULT = new Font("Cambria", Font.PLAIN, 18);
	private static final Font FONT_DEFAULT_BOLD = new Font("Cambria", Font.BOLD, 16);
	private static final Font FONT_DEFAULT_MEDIUM = new Font("Cambria", Font.PLAIN, 18);
	private static final Font FONT_DEFAULT_LARGE = new Font("Cambria", Font.PLAIN, 32);
	private static final Font FONT_DEFAULT_BIG = new Font("Cambria", Font.PLAIN, 48);

	// Custom Colors
	static final Color BACKGROUND_COLOR = new Color(187, 245, 255);
	static final Color BLUE_EMERALD = new Color(52, 93, 174);
	static final Color RED_EMERALD = new Color(198, 34, 39);
	static final Color GREEN_EMERALD = new Color(55, 112, 37);

	private static final int THEME = 1; // 0 plain, 1 Dracula

	// Font Colors
	private static final Color FONT_DEFAULT_COLOR = new Color(245, 255, 248);
	private static final Color FONT_INPUT_COLOR = (THEME == 1) ? Color.WHITE : Color.BLACK;
	private static final Color FONT_WARNING_COLOR = (THEME == 1) ? Color.RED : Color.RED;
	private static final Color FONT_SUCCESS_COLOR = (THEME == 1) ? Color.GREEN : Color.GREEN;

	// Text Area
	private static final Color INPUT_BACKGROUND_COLOR = (THEME == 1) ? new Color(43, 43, 43) : Color.WHITE;
	private static final Color FONT_KEYWORD_COLOR = (THEME == 1) ? new Color(204, 120, 50) : Color.RED;
	private static final Color FONT_TYPE_COLOR = (THEME == 1) ? FONT_KEYWORD_COLOR : Color.GREEN;
	private static final Color FONT_SYMBOL_COLOR = (THEME == 1) ? Color.RED : Color.RED;

	// Custom Button properties
	static final Font BTN_DEFAULT_FONT = FONT_DEFAULT;
	static final Color BTN_DEFAULT_COLOR = BLUE_EMERALD;
	static final Color BTN_DEFAULT_TEXT_COLOR = FONT_DEFAULT_COLOR;
	static final int BTN_DEFAULT_HEIGHT = 28;
	static final int BTN_DEFAULT_WIDTH = 200;

	static final int BORDER_PLUS = 4;// border color
	static final int HOVER_PLUS = 8; // color change when hovering

	public static final int ICON_HEIGHT = 64;
	public static final int ICON_WIDTH = 64;

	// TextInputs
	static final Font INPUT_TEXT_FONT = FONT_DEFAULT_MEDIUM;
	static final int LABELED_MARGIN = 2;
}
