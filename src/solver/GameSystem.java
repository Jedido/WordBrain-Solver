package solver;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JFrame;

//Collection of all static methods
public class GameSystem {
	
	public static void main(String[] args) {
		JFrame game = new JFrame("WordBrain Solver");
		game.setContentPane(new Panel());
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setResizable(false);
		game.pack();
		game.setVisible(true);
	}
	
	public static void drawCenteredString(Graphics g, String text, Font font, int x, int y) {
	    FontMetrics metrics = g.getFontMetrics(font);
	    x = x - metrics.stringWidth(text) / 2;
	    y = y + metrics.getAscent() + metrics.getLeading() * 2 - metrics.getHeight() / 2;
	    g.setFont(font);
		((Graphics2D)g).setRenderingHint(
		        RenderingHints.KEY_TEXT_ANTIALIASING,
		        RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
	    g.drawString(text, x, y);
	}
	

	public static void down(char[][] board) {
		for(int i = board.length - 1; i >= 0; i--) {
			for(int j = 0; j < board.length; j++) {
				if(board[j][i] == '\u0000') {
					int search = -1;
					for(int k = i - 1; k >= 0; k--) {
						if(board[j][k] != '\u0000') {
							search = k;
							break;
						}
					}
					if(search != -1) {
						board[j][i] = board[j][search]; 
						board[j][search] = '\u0000';
					}
				}
			}
		}
	}
	
	public static ArrayList<Integer> replicate(ArrayList<Integer> set) {
		ArrayList<Integer> to = new ArrayList<Integer>();
		for(int o : set) {
			to.add(o);
		}
		return to;
	}
	
	public static char[][] replicate(char[][] b) {
		char[][] ret = new char[b.length][b[0].length];
		for(int i = 0; i < b.length; i++) {
			for(int j = 0; j < b.length; j++) {
				ret[i][j] = b[i][j];
			}
		}
		return ret;
	}

	public static boolean boardEmpty(char[][] b) {
		for(int i = 0; i < b.length; i++) {
			for(int j = 0; j < b.length; j++) {
				if(b[i][j] != '\u0000') return false;
			}
		}
		return true;
	}
	
	public static int sum(ArrayList<Integer> numbers) {
		int x = 0;
		for(int n : numbers) {
			x += n;
		}
		return x;
	}
}
