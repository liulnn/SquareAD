package xyz.flove.square;

/**
 * Created by liulnn on 17/4/19.
 */

import xyz.flove.square.entities.Color;
import xyz.flove.square.entities.Position;


public abstract class Army {
    public boolean isAi;
    public String color;
    public Board board;

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
}
