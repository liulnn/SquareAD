package xyz.flove.square.entities;

import xyz.flove.square.enums.Color;
import xyz.flove.square.enums.Direction;

public class Piece {

    public Piece[][] panel;
    public int x;
    public int y;
    public Color color;

    public SuccessSquare square;

    public int status;

    public Piece(Piece[][] panel, Color color, int x, int y) {
        this.panel = panel;
        this.x = x;
        this.y = y;
        this.color = color;
        this.square = new SuccessSquare(this);
    }

    public int getDownPieceStepCount(Color color) {
        this.color = color;
        return square.computeSuccess().step;
    }

    public void handleRemovePiece() {
        square.clearSuccess();
        color = Color.NULL;
    }

    public int getMovePieceStepCount(Direction direction) {
        int step;
        Color color = panel[x][y].color;
        panel[x][y].handleRemovePiece();
        if (direction.equals(Direction.WEST)) {
            step = panel[x - 1][y].getDownPieceStepCount(color);
        } else if (direction.equals(Direction.NORTH)) {
            step = panel[x][y - 1].getDownPieceStepCount(color);
        } else if (direction.equals(Direction.EAST)) {
            step = panel[x + 1][y].getDownPieceStepCount(color);
        } else if (direction.equals(Direction.SORTH)) {
            step = panel[x][y + 1].getDownPieceStepCount(color);
        } else {
            throw new RuntimeException();
        }
        return step;
    }

    public boolean isSuccess() {
        return square.isSuccess;
    }

    @Override
    public boolean equals(Object o) {
        if (null == o) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (o instanceof Piece) {
            Piece piece = (Piece) o;
            if (piece.x == x && piece.y == y) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("Piece(%s, %s)", this.x, this.y);
    }
}
