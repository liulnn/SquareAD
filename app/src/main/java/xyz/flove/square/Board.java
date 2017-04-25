package xyz.flove.square;

import java.util.ArrayList;

import xyz.flove.square.entities.Color;
import xyz.flove.square.entities.Position;
import xyz.flove.square.enums.Direction;

/**
 * Created by liulnn on 17/4/19.
 */

public class Board implements Cloneable {
    public Status status;
    private Piece[][] panel;
    private int removeCount;

    public Board(int size) {
        panel = new Piece[size][size];
        status = Status.DOWN;
        removeCount = 0;
    }

    enum Status {
        // 下子阶段，提子阶段，对战阶段（吃子），结束
        DOWN, REMOVE, FIGHT, EAT
    }

    public boolean isSquare(Position position) {
        Piece piece = getPiece(position);
        if (piece != null && piece.squares != null && piece.squares.length > 0) {
            return true;
        }
        return false;
    }

    public Direction[] getMoveDirection(Position position) {
        Piece piece = getPiece(position);
        if (piece == null) {
            return null;
        }
        ArrayList<Direction> directions = new ArrayList<>();
        if (piece.position.x - 1 >= 0) {
            Piece westPiece = getPiece(position.x - 1, position.y);
            if (westPiece == null) {
                directions.add(Direction.WEST);
            }
        }
        if (piece.position.y - 1 >= 0) {
            Piece northPiece = getPiece(position.x, position.y - 1);
            if (northPiece == null) {
                directions.add(Direction.NORTH);
            }
        }
        if (piece.position.x + 1 <= 4) {
            Piece eastPiece = getPiece(position.x + 1, position.y);
            if (eastPiece == null) {
                directions.add(Direction.EAST);
            }
        }
        if (piece.position.y + 1 <= 4) {
            Piece sorthPiece = getPiece(position.x, position.y + 1);
            if (sorthPiece == null) {
                directions.add(Direction.SORTH);
            }
        }
        return directions.toArray(new Direction[directions.size()]);
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

    public boolean addPiece(Piece piece) {
        if (panel[piece.position.x][piece.position.y] != null) {
            return false;
        }
        panel[piece.position.x][piece.position.y] = piece;
        updateStatus();
        return true;
    }


    public void removePiece(Position position, String color) {
        removePiece(new Piece(position, color));
    }

    public boolean removePiece(Piece piece) {
        if (panel[piece.position.x][piece.position.y] == null
                || !panel[piece.position.x][piece.position.y].color.equals(piece.color)) {
            return false;
        }
        panel[piece.position.x][piece.position.y] = null;
        removeCount += 1;
        if (removeCount >= 2 && status == Status.REMOVE) {
            status = Status.FIGHT;
        }
        return true;
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

    public Position[] getCanEatPieces(String color) {
        ArrayList<Position> positions = new ArrayList<>();
        for (int i = 0; i < getLength(); i++) {
            for (int j = 0; j < getLength(); j++) {
                if (panel[i][j] != null && !panel[i][j].color.equals(color) && !isSquare(new Position(i, j))) {
                    positions.add(panel[i][j].position);
                }
            }
        }
        return positions.toArray(new Position[positions.size()]);
    }

    public boolean isWin(String color) {
        for (int i = 0; i < getLength(); i++) {
            for (int j = 0; j < getLength(); j++) {
                if (panel[i][j] != null && !panel[i][j].color.equals(color)) {
                    return false;
                }
            }
        }
        return true;
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
            newBoard.status = Status.valueOf(status.name());
            return newBoard;
        } catch (CloneNotSupportedException e) {
        } finally {
            return newBoard;
        }
    }
}
