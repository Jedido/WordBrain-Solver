package solver;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

public class Panel extends JPanel implements MouseInputListener, KeyListener {
	
	// dimensions
	public static final int HEIGHT = 600;
	public static final int WIDTH = 1020;
	
	// image
	private BufferedImage image;
	private Graphics2D g;
	public static final Color BLANK = new Color(25, 80, 80);
	public static final Color RED = new Color(255, 60, 60);
	
	// game
	private Board board;
	private JCButton[] buttons, menu;
	private WordFinder finder;
	
	// fonts
	private final static Font HEADER_FONT = new Font("Bookman Old Style", Font.BOLD, 60);
	public static Font NORMAL_FONT = new Font("Arial", Font.PLAIN, 14);
	public static Font ERROR_FONT = new Font("Arial", Font.BOLD, 14);

	// system
	public static final int SOLVE = 0;
	public static final int SETUP_LENGTHS = 1;
	public static final int SETUP_BOARD = 2;
	public static final int RESET = 3;
	public static final int STOP = 4;
	public static final String DICTIONARY_NAME = "wordbrain"; //Dictionary created from list of answers in wordbrain.org
	
	// game
	private boolean gameStart;
	private int x, y;
	private ArrayList<Integer> wordSizes;
	private Iterable<ArrayList<String>> answer;
	private boolean playError, charError, solveError, numberError, sumError;
	
	public Panel() {
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();

		
		try {
			createDictionary();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		init();
		
		g = (Graphics2D) image.getGraphics();
		paintComponent(g);
		draw();
	}

	private void init() {
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		addKeyListener(this);
		addMouseListener(this);
		
		menu = new JCButton[8];
		for(int i = 0; i < menu.length; i++) {
			menu[i] = new JCButton(80 + i * 110, HEIGHT / 2 + 30, 90, 60, (i+2) + "x" + (i+2));
		}
		
		
	}
	
	private void createDictionary() throws FileNotFoundException {
		Scanner in = new Scanner(new File("Resources\\dictionary-" + DICTIONARY_NAME + ".txt"));
		ArrayList<String> temp = new ArrayList<String>();
		while(in.hasNext()) {
			temp.add(in.nextLine());
		}
		in.close();
		String[] a = new String[temp.size()];
		finder = new WordFinder(temp.toArray(a));
	}
	
	private void setup(int size) {
		board = new Board(size);
		
		buttons = new JCButton[5];
		buttons[SOLVE] = new JCButton(690, 170, "Solve");
		buttons[SETUP_LENGTHS] = new JCButton(690, 240, "Word Lengths");
		buttons[SETUP_BOARD] = new JCButton(690, 310, "Set Up Board");
		buttons[RESET] = new JCButton(690, 380, "Reset");
		buttons[STOP] = new JCButton(690, 450, "Stop");

		wordSizes = new ArrayList<Integer>();
		gameStart = true;
	}
	
	private void reset() {
		board.clear();
		buttons[SETUP_LENGTHS].setText("Word Lengths");
		buttons[SETUP_LENGTHS].toggle(false);
		buttons[SETUP_BOARD].toggle(false);
		answer = null;
		wordSizes = new ArrayList<Integer>();
		playError = false;
		charError = false;
		solveError = false;
		numberError = false;
		sumError = false;
	}

	private void findSolution() {
		GameSystem.down(board.getBoard());
		answer = finder.getSolution(board.getBoard(), wordSizes);
	}
	
	private void draw() {
		revalidate();
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		// clear image
		g.setColor(BLANK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		if(gameStart) {
			// draw game
			board.draw(g);
			if(!buttons[SOLVE].isToggled()) {
				g.setColor(Color.WHITE);
				GameSystem.drawCenteredString(g, "Menu", HEADER_FONT, 790, 90);
				g.drawLine(690, 120, 890, 120);
				if(buttons[SETUP_LENGTHS].getText().equals("?") && !buttons[SETUP_LENGTHS].isToggled()) {
					buttons[SETUP_LENGTHS].setText("Word Lengths");
				}
				for(JCButton b : buttons) {
					if(!buttons[STOP].equals(b)) {
						b.draw(g);
					}
				}
			} else {
				g.setColor(Color.WHITE);
				GameSystem.drawCenteredString(g, "Words:", HEADER_FONT, 790, 90);
				g.drawLine(690, 120, 890, 120);
				TreeSet<String> compiled = new TreeSet<String>();
				if(answer != null) {
					for(ArrayList<String> ss : answer) {
						for(String s : ss) {
							compiled.add(s);
						}
					}
				} else solveError = true;
				int dy = 0;
				
				//Version 2
				for(ArrayList<String> sol : answer) {
					for(int i = sol.size() - 1; i >=0; i--) {
						GameSystem.drawCenteredString(g, sol.get(i), NORMAL_FONT, 790, 140 + dy);
						dy += 22;
					}
					dy += 10;
				}
				buttons[STOP].draw(g);
			}
			
			//TODO Set errors
			g.setColor(RED);
			if(playError) {
				GameSystem.drawCenteredString(g, "Board is not set up properly", ERROR_FONT, 790, 450);
				GameSystem.drawCenteredString(g, "Letters on board: " + board.letters(), ERROR_FONT, 790, 472);
				GameSystem.drawCenteredString(g, "Sum of word lengths: " + GameSystem.sum(wordSizes), ERROR_FONT, 790, 494);
				playError = false;
			} else if(charError) {
				GameSystem.drawCenteredString(g, "Must be a letter of the alphabet (or backspace).", ERROR_FONT, 790, 450);
				charError = false;
			} else if(numberError) {
				GameSystem.drawCenteredString(g, "Must be a number from 2 to 9 (or backspace).", ERROR_FONT, 790, 450);
				numberError = false;
			} else if(sumError) {
				GameSystem.drawCenteredString(g, "Sum of word lengths cannot exceed board size.", ERROR_FONT, 790, 450);
				GameSystem.drawCenteredString(g, "Board size: " + board.size() * board.size(), ERROR_FONT, 790, 472);
				GameSystem.drawCenteredString(g, "Sum of word lengths: " + GameSystem.sum(wordSizes), ERROR_FONT, 790, 494);
				sumError = false;
			} else if(solveError) {
				GameSystem.drawCenteredString(g, "No Solution!", ERROR_FONT, 790, 450);
				GameSystem.drawCenteredString(g, "Make sure to input the board lengths in order.", ERROR_FONT, 790, 472);
				GameSystem.drawCenteredString(g, "Also, check the letters again.", ERROR_FONT, 790, 472);
				solveError = false;
			}
		} else {
			// draw menu
			g.setColor(Color.WHITE);
			GameSystem.drawCenteredString(g, "Board Size:", HEADER_FONT, WIDTH / 2, HEIGHT / 2 - 50);
			for(JCButton b : menu) {
				b.draw(g, true);
			}
		}
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		x = arg0.getX();
		y = arg0.getY();
		
		if(gameStart) {
			// mouse on board
			if(x > Board.BOARD_MARGIN && x < Board.BOARD_MARGIN + Board.BOARD_SIZE
					&& y > Board.BOARD_MARGIN && y < Board.BOARD_MARGIN + Board.BOARD_SIZE) {
				board.highlight(x, y);
				buttons[SETUP_LENGTHS].toggle(false);
				buttons[SETUP_BOARD].toggle(false);
			} 
			
			//Only Stop button is click-able when solve is running
			if(buttons[SOLVE].isToggled()) {
				if(buttons[STOP].pressed(x, y)) {
					buttons[SOLVE].toggle();
				}
			} else {
				for(int i = 0; i < buttons.length - 1; i++) {
					if(buttons[i].pressed(x, y)) {
						board.deselect();
						switch(i) {
						case(SOLVE) : if(board.letters() == GameSystem.sum(wordSizes) && board.letters() != 0) {
							findSolution(); toggleButton(SOLVE); 
							} else playError = true; break;
						case(SETUP_LENGTHS) : toggleButton(SETUP_LENGTHS); if(wordSizes.isEmpty()) buttons[SETUP_LENGTHS].setText("?"); break;
						case(SETUP_BOARD) : if(!buttons[SETUP_BOARD].isToggled()) {
							board.highlight(Board.BOARD_MARGIN + 1, Board.BOARD_MARGIN + 1); 
						} else {
							board.deselect();
						}
						toggleButton(SETUP_BOARD); 
						break;
						case(RESET) : reset(); break;
						}
						break;
					}
				}
			}
		} else {
			for(int i = 0; i < menu.length; i++) {
				if(menu[i].pressed(x, y)) {
					setup(i+2);
					break;
				}
			}
		}
		
//		findSolution();
		draw();
	}

	public void toggleButton(int index) {
		for(int i = 0; i < buttons.length; i++) {
			if(i != index) {
				buttons[i].toggle(false);
			} else {
				buttons[i].toggle();
			}
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) {
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(board.selected()) {
			char input = (e.getKeyChar() + "").toUpperCase().charAt(0);
			if(input - 'A' >= 0 && input - 'Z' <= 0) {
				board.setSelectedLetter(input);
			} else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE){
				board.removeSelectedLetter();
			} else {
				charError = true;
			}
			if(buttons[SETUP_BOARD].isToggled()) {
				if(!board.selected()) {
					buttons[SETUP_BOARD].toggle();
				}
			} else {
				board.deselect();
			}
		} else if (buttons[SETUP_LENGTHS].isToggled()) {
			char input = e.getKeyChar();
			if(input - '2' >= 0 && input - '9' <= 0) {
				if(board.size() * board.size() >= GameSystem.sum(wordSizes) + input - '0') {
					wordSizes.add(input - '0');
					if(board.size() * board.size() == GameSystem.sum(wordSizes)) {
						buttons[SETUP_LENGTHS].toggle();
					}
				} else {
					sumError = true;
				}
			} else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
				if(wordSizes.isEmpty()) {
					buttons[SETUP_LENGTHS].toggle();
				} else {
					wordSizes.remove(wordSizes.size() - 1);
					if(wordSizes.isEmpty()) {
						buttons[SETUP_LENGTHS].setText("?");
					}
				}
			} else if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				buttons[SETUP_LENGTHS].toggle();
			} else {
				numberError = true;
			}
			
			String text = "";
			if(!wordSizes.isEmpty()) {
				text = wordSizes.get(0) + "";
				for(int i = 1; i < wordSizes.size(); i++) {
					text += ", " + wordSizes.get(i);
				}
			}
			buttons[SETUP_LENGTHS].setText(text + ((buttons[SETUP_LENGTHS].isToggled() && !wordSizes.isEmpty()) ? " ?" : ""));
		} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameStart = false;
		}
		draw();
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
