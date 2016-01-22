package solver;

import java.awt.*;

public class JCButton {
	public static Font MENU_FONT = new Font("Arial", Font.BOLD, 28);
	public static Font NORMAL_FONT = new Font("Arial", Font.BOLD, 20);
	public static Color BUTTON_COLOR = new Color(240, 230, 200);
	public static Color BUTTON_PRESSED = new Color(180, 170, 150);
	public static Color BUTTON_SHADOW = new Color(100, 95, 85);
	public static int BUTTON_HEIGHT = 50;
	public static int BUTTON_WIDTH = 200;
	
	private int x, y, width, height;
	private String text;
	private boolean toggle;
	
	public JCButton(int x, int y, int width, int height, String text) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		if(text != null) {
			this.text = text;
		} else {
			this.text = "";
		}
	}
	
	public JCButton(int x, int y, String text) {
		this.x = x;
		this.y = y;
		this.width = BUTTON_WIDTH;
		this.height = BUTTON_HEIGHT;
		if(text != null) {
			this.text = text;
		} else {
			this.text = "";
		}
	}
	
	public void draw(Graphics g) {
		g.setColor(toggle ? BUTTON_SHADOW : BUTTON_SHADOW);
		g.fillRoundRect(x + (toggle ? 0 : 3), y + (toggle ? 0 : 3), width, height, 50, 50);
		g.setColor(toggle ? BUTTON_PRESSED : BUTTON_COLOR);
		g.fillRoundRect(x + (toggle ? 3 : 0), y + (toggle ? 3 : 0), width, height, 50, 50);
		g.setColor(Board.FONT_COLOR);
		GameSystem.drawCenteredString(g, text, NORMAL_FONT, x + width / 2, y + height / 2);
	}
	
	public void draw(Graphics g, boolean menu) {
		g.setColor(toggle ? BUTTON_SHADOW : BUTTON_SHADOW);
		g.fillRoundRect(x + (toggle ? 0 : 3), y + (toggle ? 0 : 3), width, height, 15, 15);
		g.setColor(toggle ? BUTTON_PRESSED : BUTTON_COLOR);
		g.fillRoundRect(x + (toggle ? 3 : 0), y + (toggle ? 3 : 0), width, height, 15, 15);
		g.setColor(Board.FONT_COLOR);		g.setColor(Board.FONT_COLOR);
		GameSystem.drawCenteredString(g, text, MENU_FONT, x + width / 2, y + height / 2);
	}
	
	public boolean pressed(int ox, int oy) {
		return x < ox && y < oy && x + width > ox && y + height > oy;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String newText) {
		text = newText;
	}
	
	public void toggle() {
		toggle = !toggle;
	}
	
	public void toggle(boolean to) {
		toggle = to;
	}
	
	public boolean isToggled() {
		return toggle;
	}
}
