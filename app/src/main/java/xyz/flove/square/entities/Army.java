package xyz.flove.square.entities;

import xyz.flove.square.enums.Color;
import xyz.flove.square.enums.Direction;

public class Army {

	public Color color;
	public Piece[][] panel;

	public Army(Board board, Color color) {
		this.panel = board.panel;
		this.color = color;
	}

	/**
	 * 下子
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public int downPiece(int x, int y) {
		return panel[x][y].getDownPieceStepCount(color);
	}

	/**
	 * 提子
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public void removePiece(int x, int y) {
		panel[x][y].handleRemovePiece();
	}

	/**
	 * 走子
	 * 
	 * @param x
	 * @param y
	 * @param direction
	 * @return
	 */
	public int movePiece(int x, int y, Direction direction) {
		return panel[x][y].getMovePieceStepCount(direction);
	}

	/**
	 * 吃子
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public void eatPiece(int x, int y) {
		panel[x][y].color = Color.NULL;
	}
}
