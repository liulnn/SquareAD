package xyz.flove.square;

/**
 * Created by liulnn on 17/4/19.
 */

import xyz.flove.square.entities.Color;
import xyz.flove.square.entities.Position;
import xyz.flove.square.enums.Direction;


public abstract class Army {
    public boolean isAi;
    public String color;
    public Board board;
    public Position lastChecked;

    public Army(Board board, String color, boolean isAi) {
        this.board = board;
        this.color = color;
        this.isAi = isAi;
    }

    /**
     * @param p 下子的位置
     * @return -1:此处不能下子, 0:切换队手, n:返回继续下子的个数
     */
    public int downPiece(Position p) {
        Piece piece = board.getPiece(p);
        if (null != piece && !piece.color.equals(Color.NULL)) {
            return -1;
        }
        new Rule(this, p).squares();
        return board.getPiece(p).getSteps();
    }


    public boolean removePiece(Position p) {
        if(!board.getPiece(p).color.equals(color)){
            return false;
        }
        new Rule(this, p).clearSquares();
        return true;
    }

    public boolean checkedPiece(Position p) {
        Piece piece = board.getPiece(p);
        if (!piece.color.equals(color)) {
            return false;
        }
        if (lastChecked != null) {
            board.getPiece(lastChecked).status = Piece.Status.NULL;
        }
        piece.status = Piece.Status.CHECKED;
        lastChecked = p;
        return true;
    }

    public int movePiece(Position destPosition) {
        if (lastChecked == null) {
            return -1;
        }
        if (!board.getPiece(destPosition).color.equals(board.getPiece(lastChecked).color)) {
            return -1;
        }
        new Rule(this, lastChecked).clearSquares();
        new Rule(this, destPosition).squares();
        return board.getPiece(destPosition).getSteps();
    }

    public boolean eatPiece(Position position) {
        Piece piece = board.getPiece(position);
        if (piece == null || piece.color.equals(color) || board.isSquare(position)) {
            return false;
        }
        board.removePiece(piece);
        return true;
    }
}
