package solver;

import java.awt.*;

public class Board {
	
	public static final int BOARD_MARGIN = 30;
	public static final int BOARD_SIZE = 540;
	public static final Color BOARD_COLOR = new Color(240, 230, 200);
	public static final Color FONT_COLOR = new Color(30, 30, 30);
	
	private char[][] board; //col # x row #
	private int N, squareSize, padding; // squareSize = BoardSize / N; padding = squareSize / 20;
	private Font letterFont;
	private int x, y;
	
	public Board(int size) {
		N = size;
		squareSize = BOARD_SIZE / N;
		padding = squareSize / 10;
		letterFont = new Font("Arial", Font.PLAIN, 90 - N * 8);
		board = new char[size][size];
		x = -1;
		y = -1;
	}
	
	//takes in x and y pixel location value
	public void setSelectedLetter(char letter) {
		if(letter == '\u0000') {
			removeSelectedLetter();
			return;
		}
		board[x][y] = letter;
		next();
	}
	
	public void removeSelectedLetter() {
		board[x][y] = '\u0000';
		back();
	}
	
	public void highlight(int ix, int iy) {
		BoardLocation loc = new BoardLocation(ix, iy, N);
		x = loc.getRow();
		y = loc.getCol();
	}
	
	public void deselect() { 
		x = -1;
		y = -1;
	}
	
	private void next() {
		if(x < board.length - 1) {
			x++;
		} else {
			x = 0;
			y++;
			if(y == board.length) {
				x = -1;
				y = -1;
			}
		}
	}
	
	private void back() {
		if(x > 0) {
			x--;
		} else {
			x = board.length - 1;
			y--;
			if(y == -1) {
				x = -1;
			}
		}
	}
	
	public boolean selected() {
		return x >= 0;
	}
	
	
	public void draw(Graphics g) {
		g.setFont(letterFont);
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				g.setColor(BOARD_COLOR);
				if(i == x && j == y) {
					g.fillRoundRect(i * squareSize + BOARD_MARGIN + padding / 2, j * squareSize + BOARD_MARGIN + padding, squareSize - padding, squareSize - padding, padding, padding);
					g.setColor(FONT_COLOR);
					GameSystem.drawCenteredString(g, "?", letterFont, i * squareSize + BOARD_MARGIN + squareSize / 2, j * squareSize + BOARD_MARGIN + squareSize / 2);
				} else if(board[i][j] != '\u0000') {
					g.fillRoundRect(i * squareSize + BOARD_MARGIN + padding / 2, j * squareSize + BOARD_MARGIN + padding, squareSize - padding, squareSize - padding, padding, padding);
					g.setColor(FONT_COLOR);
					GameSystem.drawCenteredString(g, board[i][j] + "", letterFont, i * squareSize + BOARD_MARGIN + squareSize / 2, j * squareSize + BOARD_MARGIN + squareSize / 2);
				} else {
					g.drawRoundRect(i * squareSize + BOARD_MARGIN + padding / 2, j * squareSize + BOARD_MARGIN + padding, squareSize - padding, squareSize - padding, padding, padding);
				}
			}
		}
	}
	
	public void clear() {
		board = new char[N][N];
	}
	
	public int letters() {
		int count = 0;
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(board[i][j] != '\u0000') {
					count++;
				}
			}
		}
		return count;
	}
	
	public int size() {
		return N;
	}
	
	public char[][] getBoard() {
		return board;
	}
}
