package xyz.flove.square.ai;

import xyz.flove.square.entities.Army;
import xyz.flove.square.entities.Board;
import xyz.flove.square.entities.Piece;
import xyz.flove.square.enums.Color;
import xyz.flove.square.entities.SuccessSquare;


public class ScoreAi extends Army {

    public ScoreAi(Board board, Color color) {
        super(board, color, true);
    }


    public Piece[][] deepCopyPanel() {
        //构造一个新的棋盘做计算
        Piece[][] pieces = new Piece[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                pieces[i][j] = new Piece(pieces, panel[i][j].color, panel[i][j].x, panel[i][j].y);
            }
        }
        return pieces;
    }

    public Piece getPiece() {
        int maxScore = 0;
        Piece piece = null;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (panel[i][j].color == Color.NULL) {
                    //判断己方得分
                    Piece[][] panel = deepCopyPanel();
                    Piece p = new Piece(panel, this.color, i, j);
                    panel[i][j].color = this.color;
                    int score = new SuccessSquare(p).computeSuccess().score;
                    if (maxScore <= score) {
                        maxScore = score;
                        piece = p;
                    }
                    //判断对方得分
                    Piece p1 = null;
                    Piece[][] panel1 = deepCopyPanel();
                    if (this.color == Color.BLACK) {
                        p1 = new Piece(panel1, Color.WHITE, i, j);
                        panel1[i][j].color = Color.WHITE;
                    } else {
                        p1 = new Piece(panel1, Color.BLACK, i, j);
                        panel1[i][j].color = Color.BLACK;
                    }
                    int score1 = new SuccessSquare(p1).computeSuccess().score;
                    if (maxScore <= score1) {
                        maxScore = score1;
                        piece = p1;
                    }
                }
            }
        }
        return piece;
    }
}
