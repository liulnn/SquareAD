package xyz.flove.square.entities;

import java.util.ArrayList;
import java.util.List;

import xyz.flove.square.enums.BoardStatus;
import xyz.flove.square.enums.Color;

public class Board {
    public BoardStatus status;
    public Piece[][] panel;

    public Board() {
        panel = new Piece[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                panel[i][j] = new Piece(panel, Color.NULL, i, j);
            }
        }
        status = BoardStatus.DOWN;
    }

    public List<Piece> getNullPieces() {
        List<Piece> pieces = new ArrayList<Piece>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (panel[i][j].color == Color.NULL) {
                    pieces.add(panel[i][j]);
                }
            }
        }
        return pieces;
    }

    public void draw() {
        System.out.println("--------------------------------");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Color color = panel[j][i].color;
                if (color == Color.BLACK) {
                    System.out.print("[*]");
                } else if (color == Color.WHITE) {
                    System.out.print("[#]");
                } else {
                    System.out.print("[ ]");
                }
            }
            System.out.println("");
        }
        System.out.println("--------------------------------");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                boolean isSuccess = panel[j][i].square.isSuccess;
                if (isSuccess) {
                    System.out.print("[+]");
                } else {
                    System.out.print("[ ]");
                }
            }
            System.out.println("");
        }
    }
}

