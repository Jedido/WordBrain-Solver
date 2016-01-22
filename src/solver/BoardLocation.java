package solver;


public class BoardLocation {
	private int row, col;
	public BoardLocation(int x, int y, Board b) {
		col = (y - Board.BOARD_MARGIN) * b.size() / Board.BOARD_SIZE;
		row = (x - Board.BOARD_MARGIN) * b.size() / Board.BOARD_SIZE;
	}
	
	public BoardLocation(int x, int y, int N) {
		col = (y - Board.BOARD_MARGIN) * N / Board.BOARD_SIZE;
		row = (x - Board.BOARD_MARGIN) * N / Board.BOARD_SIZE;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
}