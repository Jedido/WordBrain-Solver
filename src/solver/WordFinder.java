package solver;

import java.util.ArrayList;

public class WordFinder {
	private JCTrie trie;

	public WordFinder(String[] dictionary) {
		trie = new JCTrie(dictionary);
	}
	
	public ArrayList<ArrayList<String>> getSolution(char[][] b, ArrayList<Integer> lengths) {
		ArrayList<ArrayList<String>> words = new ArrayList<ArrayList<String>>();
		ArrayList<String> next = new ArrayList<String>();
		for (int i = 0; i < b.length; i++) {
			for (int j = 0; j < b[0].length; j++) {
				wordSearch(b, new boolean[b.length][b[0].length],
						"", i, j, next, GameSystem.replicate(lengths));
				if(!next.isEmpty()) {
					words.add(next);
				}
				next = new ArrayList<String>();
			}
		}		
		return words;
	}
	
	private void wordSearch(char[][] board, boolean[][] marked,
			String current, int x, int y, ArrayList<String> words, ArrayList<Integer> lengths) {
//		boolean solution = false;
		if(board[x][y] != '\u0000') {
			current += board[x][y];
		} else {
			return;
		}
		if (trie.collect(current)) {
			marked[x][y] = true;

//			System.out.println(current);

			add(x - 1, y - 1, board, marked, current, words, lengths);
			add(x + 1, y - 1, board, marked, current, words, lengths);
			add(x - 1, y + 1, board, marked, current, words, lengths);
			add(x + 1, y + 1, board, marked, current, words, lengths);
			add(x, y - 1, board, marked, current, words, lengths);
			add(x, y + 1, board, marked, current, words, lengths);
			add(x - 1, y, board, marked, current, words, lengths);
			add(x + 1, y, board, marked, current, words, lengths);
			
			//Is this a potential word?
			if (!lengths.isEmpty() && current.length() > 1 && current.length() == lengths.get(0) && trie.contains(current)) {

				//Remove word from the board
				char[][] newBoard = new char[board.length][board[0].length];
				for(int i = 0; i < marked.length; i++) {
					for(int j = 0; j < marked[i].length; j++) {
						if(!marked[i][j]) {
							newBoard[i][j] = board[i][j];
						}
					}
				}
				
				//Found Solution (Is the board empty?)
				if(GameSystem.boardEmpty(newBoard)) {
					marked[x][y] = false;
					words.add(current);
					return; //Begin ending loop
				}
				//Search for next word
				GameSystem.down(newBoard);
				lengths.remove(0);
				char[][] dupe = GameSystem.replicate(newBoard);
				for (int i = 0; i < newBoard.length; i++) {
					for (int j = 0; j < newBoard[i].length; j++) {
						wordSearch(dupe, new boolean[marked.length][marked[0].length], "", i, j, words, lengths);
						dupe = GameSystem.replicate(newBoard);
					}
				}
				
				//Is this word part of the solution?
				if(!words.isEmpty()) {
					//Checking check
					if(!lengths.isEmpty()) {
						lengths.remove(0);
					}
					marked[x][y] = false;
					words.add(current);
					return;
				}
				
				lengths.add(0, current.length());
			}

			marked[x][y] = false;
		}
	}
	
	private void add(int x, int y, char[][] board, boolean[][] marked, String current, ArrayList<String> words, ArrayList<Integer> lengths) {
		if(x >= 0 && y >= 0 && x < board.length && y < board[0].length && !marked[x][y] && board[x][y] != '\u0000') {
			wordSearch(board, marked, current, x, y, words, lengths);
		} 
	}
}
