package xyz.flove.square;

import xyz.flove.square.entities.Color;
import xyz.flove.square.entities.Position;

/**
 * Created by liulnn on 17/4/19.
 */

public class Board implements Cloneable {
    public Status status;
    private Piece[][] panel;

    public Board(int size) {
        panel = new Piece[size][size];
        status = Status.DOWN;
    }

    enum Status {
        // 下子阶段，提子阶段，对战阶段（选中，移动，吃子）
        DOWN, REMOVE, FIGHT, CHECKED, MOVE, EAT;
    }

    public boolean isSquare(Position position) {
        Piece piece = getPiece(position);
        if (piece != null && piece.squares != null && piece.squares.length > 0) {
            return true;
        }
        return false;
    }

    public int getLength() {
        return panel[0].length;
    }

    public Piece getPiece(Position position) {
        return panel[position.x][position.y];
    }

    public Piece getPiece(int x, int y) {
        return panel[x][y];
    }

    public void addPiece(Position position, String color) {
        addPiece(new Piece(position, color));
    }

    public void addPiece(Piece piece) {
        panel[piece.position.x][piece.position.y] = piece;
        updateStatus();
    }

    private void updateStatus() {
        if (status == Status.DOWN) {
            boolean isFull = true;
            for (int i = 0; i < getLength(); i++) {
                for (int j = 0; j < getLength(); j++) {
                    if (panel[i][j] == null || panel[i][j].color.equals(Color.NULL)) {
                        isFull = false;
                    }
                }
            }
            if (isFull) {
                status = Status.REMOVE;
            }
        }
    }

    public void draw() {
        System.out.println("--------------------------------");
        for (int i = 0; i < getLength(); i++) {
            for (int j = 0; j < getLength(); j++) {

                String color = Color.NULL;
                if (panel[j][i] != null) {
                    color = panel[j][i].color;
                }
                if (color.equals(Color.BLACK)) {
                    System.out.print("[*]");
                } else if (color.equals(Color.WHITE)) {
                    System.out.print("[#]");
                } else {
                    System.out.print("[ ]");
                }
            }
            System.out.println("");
        }
        System.out.println("--------------------------------");
        for (int i = 0; i < getLength(); i++) {
            for (int j = 0; j < getLength(); j++) {
                if (this.isSquare(new Position(j, i))) {
                    System.out.print("[+]");
                } else {
                    System.out.print("[ ]");
                }
            }
            System.out.println("");
        }
    }

    @Override
    public Board clone() {
        Board newBoard = null;
        try {
            newBoard = (Board) super.clone();
            Piece[][] pieces = new Piece[getLength()][getLength()];
            for (int i = 0; i < getLength(); i++) {
                for (int j = 0; j < getLength(); j++) {
                    if (panel[i][j] != null) {
                        pieces[i][j] = new Piece(new Position(i, j), panel[i][j].color);
                    }
                }
            }
            newBoard.panel = pieces;
            return newBoard;
        } catch (CloneNotSupportedException e) {
        } finally {
            return newBoard;
        }
    }
}
