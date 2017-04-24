package xyz.flove.square;


/**
 * Created by liulnn on 17/4/20.
 */

import xyz.flove.square.entities.Position;
import xyz.flove.square.entities.FiveSquare;


public class Rule {

    private Army army;
    private Position position;

    public Rule(Army army, Position position) {
        this.army = army;
        this.position = position;
        army.board.panel[position.x][position.y] = new Piece(position, this.army.color);
    }

    public void squares() {
        computeFoursquareSuccess();
        computeLineSuccess();
        computeObliqueSuccess();
    }

    /**
     * 成方：放子或走子在棋盘的任意地方成正方格。
     * 单方：放子或走子棋盘的任意地方成单一正方格。
     * 双方：放子或走子棋盘的任意地方成二个正方格。
     * 三方：放子或走子棋盘的任意地方成三个正方格，仅出现在满盘前。
     * 四方：放子或走子棋盘的任意地方成四个正方格，仅出现在满盘前。
     */
    private void computeFoursquareSuccess() {
        // 子的方位：中，西，西北，北，东北，东，东南，南，西南
        Piece piece = army.board.panel[position.x][position.y];
        Piece westPiece = null;
        if (piece.position.x - 1 >= 0) {
            westPiece = army.board.panel[position.x - 1][position.y];
        }
        Piece westNorthPiece = null;
        if (piece.position.x - 1 >= 0 && piece.position.y - 1 >= 0) {
            westNorthPiece = army.board.panel[position.x - 1][position.y - 1];
        }
        Piece northPiece = null;
        if (piece.position.y - 1 >= 0) {
            northPiece = army.board.panel[position.x][position.y - 1];
        }
        Piece eastNorthPiece = null;
        if (piece.position.x + 1 <= 4 && piece.position.y - 1 >= 0) {
            eastNorthPiece = army.board.panel[position.x + 1][position.y - 1];
        }
        Piece eastPiece = null;
        if (piece.position.x + 1 <= 4) {
            eastPiece = army.board.panel[position.x + 1][position.y];
        }
        Piece eastSorthPiece = null;
        if (piece.position.x + 1 <= 4 && piece.position.y + 1 <= 4) {
            eastSorthPiece = army.board.panel[position.x + 1][position.y + 1];
        }
        Piece sorthPiece = null;
        if (piece.position.y + 1 <= 4) {
            sorthPiece = army.board.panel[position.x][position.y + 1];
        }
        Piece westSorthPiece = null;
        if (piece.position.x - 1 >= 0 && piece.position.y + 1 <= 4) {
            westSorthPiece = army.board.panel[position.x - 1][position.y + 1];
        }

        // 西北方
        if (null != westPiece && null != westNorthPiece && null != northPiece
                && westPiece.color.equals(piece.color)
                && westNorthPiece.color.equals(piece.color)
                && northPiece.color.equals(piece.color)
                ) {
            piece.addSquares(FiveSquare.WEST_NORTH_SQUARE);
            westPiece.addSquares(FiveSquare.EAST_NORTH_SQUARE);
            westNorthPiece.addSquares(FiveSquare.EAST_SORTH_SQUARE);
            northPiece.addSquares(FiveSquare.WEST_SORTH_SQUARE);
        }

        // 西南方
        if (null != westPiece && null != westSorthPiece && null != sorthPiece
                && westPiece.color.equals(piece.color)
                && westSorthPiece.color.equals(piece.color)
                && sorthPiece.color.equals(piece.color)
                ) {
            piece.addSquares(FiveSquare.WEST_SORTH_SQUARE);
            sorthPiece.addSquares(FiveSquare.WEST_NORTH_SQUARE);
            westSorthPiece.addSquares(FiveSquare.EAST_NORTH_SQUARE);
            westPiece.addSquares(FiveSquare.EAST_SORTH_SQUARE);
        }
        // 东北方
        if (null != eastPiece && null != eastNorthPiece && null != northPiece
                && eastPiece.color.equals(piece.color)
                && eastNorthPiece.color.equals(piece.color)
                && northPiece.color.equals(piece.color)
                ) {
            piece.addSquares(FiveSquare.EAST_NORTH_SQUARE);
            northPiece.addSquares(FiveSquare.EAST_SORTH_SQUARE);
            eastNorthPiece.addSquares(FiveSquare.WEST_SORTH_SQUARE);
            eastPiece.addSquares(FiveSquare.WEST_NORTH_SQUARE);
        }
        // 东南方
        if (null != eastPiece && null != eastSorthPiece && null != sorthPiece
                && eastPiece.color.equals(piece.color)
                && eastSorthPiece.color.equals(piece.color)
                && sorthPiece.color.equals(piece.color)
                ) {
            piece.addSquares(FiveSquare.EAST_SORTH_SQUARE);
            eastPiece.addSquares(FiveSquare.WEST_SORTH_SQUARE);
            eastSorthPiece.addSquares(FiveSquare.WEST_NORTH_SQUARE);
            sorthPiece.addSquares(FiveSquare.EAST_NORTH_SQUARE);
        }
    }

    /**
     * 成拉：放子或走子五棋子在一条线上，横竖匀可（除四条边）。
     */
    private void computeLineSuccess() {
        if (position.x == 0 || position.x == 4 || position.y == 0 || position.y == 4) {
            return;
        }
        Piece piece = army.board.panel[position.x][position.y];

        Piece[] latLinePieces = new Piece[5];
        Piece[] lonLinePieces = new Piece[5];

        int latLineCounter = 0;
        int lonLineCounter = 0;

        for (int i = 0; i < 5; i++) {
            if (null != army.board.panel[i][position.y]
                    && army.board.panel[i][position.y].color.equals(piece.color)) {
                latLinePieces[i] = army.board.panel[i][position.y];
                latLineCounter++;
            }

        }
        for (int i = 0; i < 5; i++) {
            if (null != army.board.panel[position.x][i]
                    && army.board.panel[position.x][i].color.equals(piece.color)) {
                lonLinePieces[i] = army.board.panel[position.x][i];
                lonLineCounter++;
            }
        }
        if (latLineCounter == 5) {
            switch (position.y) {
                case 1:
                    for (Piece p : latLinePieces) {
                        p.addSquares(FiveSquare.NORTH_LAT_LINE);
                    }
                    break;
                case 2:
                    for (Piece p : latLinePieces) {
                        p.addSquares(FiveSquare.CENTER_LAT_LINE);
                    }
                    break;
                case 3:
                    for (Piece p : latLinePieces) {
                        p.addSquares(FiveSquare.SORTH_LAT_LINE);
                    }
                    break;
            }
        }
        if (lonLineCounter == 5) {
            switch (position.y) {
                case 1:
                    for (Piece p : lonLinePieces) {
                        p.addSquares(FiveSquare.WEST_LON_LINE);
                    }
                    break;
                case 2:
                    for (Piece p : lonLinePieces) {
                        p.addSquares(FiveSquare.CENTER_LON_LINE);
                    }
                    break;
                case 3:
                    for (Piece p : lonLinePieces) {
                        p.addSquares(FiveSquare.EAST_LON_LINE);
                    }
                    break;
            }
        }
    }

    /**
     * 成斜：放子或走子成一条斜线。
     * 三斜：放子或走子成三棋子在一条斜线上，仅有四个，标有实心。
     * 四斜：放子或走子成四棋子在一条斜线上，仅有四个，标有空心。
     * 五斜：放子或走子成五棋子在一条斜线上，仅有二个。
     */
    private void computeObliqueSuccess() {
        Piece piece = army.board.panel[position.x][position.y];
        //成斜：西北东南
        switch (position.y - position.x) {
            case -2:
                Piece[] wnesRightThreeObliquePieces = new Piece[3];
                int wnesRightThreeObliqueCounter = 0;
                for (int i = 0; i + 2 <= 4; i++) {
                    if (null != army.board.panel[i + 2][i]
                            && army.board.panel[i + 2][i].color.equals(piece.color)) {
                        wnesRightThreeObliquePieces[i] = army.board.panel[i + 2][i];
                        wnesRightThreeObliqueCounter += 1;
                    }
                }
                if (wnesRightThreeObliqueCounter == 3) {
                    for (Piece p : wnesRightThreeObliquePieces) {
                        p.addSquares(FiveSquare.WNES_RIGHT_THREE_OBLIQUE);
                    }
                }
                break;
            case -1:
                Piece[] wnesRightFourObliquePieces = new Piece[4];
                int wnesRightFourObliqueCounter = 0;
                for (int i = 0; i + 1 <= 4; i++) {
                    if (null != army.board.panel[i + 1][i]
                            && army.board.panel[i + 1][i].color.equals(piece.color)) {
                        wnesRightFourObliquePieces[i] = army.board.panel[i + 1][i];
                        wnesRightFourObliqueCounter++;
                    }
                }
                if (wnesRightFourObliqueCounter == 4) {
                    for (Piece p : wnesRightFourObliquePieces) {
                        p.addSquares(FiveSquare.WNES_RIGHT_FOUR_OBLIQUE);
                    }
                }
                break;
            case 0:
                Piece[] wnesCenterObliquePieces = new Piece[5];
                int wnesCenterObliqueCounter = 0;
                for (int i = 0; i <= 4; i++) {
                    if (null != army.board.panel[i][i]
                            && army.board.panel[i][i].color.equals(piece.color)) {
                        wnesCenterObliquePieces[i] = army.board.panel[i][i];
                        wnesCenterObliqueCounter++;
                    }
                }
                if (wnesCenterObliqueCounter == 5) {
                    for (Piece p : wnesCenterObliquePieces) {
                        p.addSquares(FiveSquare.WNES_CENTER_OBLIQUE);
                    }
                }
                break;
            case 1:
                Piece[] wnesLeftFourObliquePieces = new Piece[4];
                int wnesLeftFourObliqueCounter = 0;
                for (int i = 0; i + 1 <= 4; i++) {
                    if (null != army.board.panel[i][i + 1]
                            && army.board.panel[i][i + 1].color.equals(piece.color)) {
                        wnesLeftFourObliquePieces[i] = army.board.panel[i][i + 1];
                        wnesLeftFourObliqueCounter++;
                    }
                }
                if (wnesLeftFourObliqueCounter == 4) {
                    for (Piece p : wnesLeftFourObliquePieces) {
                        p.addSquares(FiveSquare.WNES_LEFT_FOUR_OBLIQUE);
                    }
                }
                break;
            case 2:
                Piece[] wnesLeftThreeObliquePieces = new Piece[3];
                int wnesLeftThreeObliqueCounter = 0;
                for (int i = 0; i + 2 <= 4; i++) {
                    if (null != army.board.panel[i][i + 2]
                            && army.board.panel[i][i + 2].color.equals(piece.color)) {
                        wnesLeftThreeObliquePieces[i] = army.board.panel[i][i + 2];
                        wnesLeftThreeObliqueCounter++;
                    }
                }
                if (wnesLeftThreeObliqueCounter == 3) {
                    for (Piece p : wnesLeftThreeObliquePieces) {
                        p.addSquares(FiveSquare.WNES_LEFT_THREE_OBLIQUE);
                    }
                }
                break;
            default:
                break;
        }

        //成斜：东北西南
        switch (position.y + position.x) {
            case 2:
                Piece[] enwsLeftThreeObliquePieces = new Piece[3];
                int enwsLeftThreeObliqueCounter = 0;
                for (int i = 0; 2 - i >= 0; i++) {
                    if (null != army.board.panel[i][2 - i]
                            && army.board.panel[i][2 - i].color.equals(piece.color)) {
                        enwsLeftThreeObliquePieces[i] = army.board.panel[i][2 - i];
                        enwsLeftThreeObliqueCounter++;
                    }
                }
                if (enwsLeftThreeObliqueCounter == 3) {
                    for (Piece p : enwsLeftThreeObliquePieces) {
                        p.addSquares(FiveSquare.ENWS_LEFT_THREE_OBLIQUE);
                    }
                }
                break;
            case 3:
                Piece[] enwsLeftFourObliquePieces = new Piece[4];
                int enwsLeftFourObliqueCounter = 0;
                for (int i = 0; 3 - i >= 0; i++) {
                    if (null != army.board.panel[i][3 - i]
                            && army.board.panel[i][3 - i].color.equals(piece.color)) {
                        enwsLeftFourObliquePieces[i] = army.board.panel[i][3 - i];
                        enwsLeftFourObliqueCounter++;
                    }
                }
                if (enwsLeftFourObliqueCounter == 4) {
                    for (Piece p : enwsLeftFourObliquePieces) {
                        p.addSquares(FiveSquare.ENWS_LEFT_FOUR_OBLIQUE);
                    }
                }
                break;
            case 4:
                Piece[] enwsCenterObliquePieces = new Piece[5];
                int enwsCenterObliqueCounter = 0;
                for (int i = 0; 4 - i >= 0; i++) {
                    if (null != army.board.panel[i][4 - i]
                            && army.board.panel[i][4 - i].color.equals(piece.color)) {
                        enwsCenterObliquePieces[i] = army.board.panel[i][4 - i];
                        enwsCenterObliqueCounter++;
                    }
                }
                if (enwsCenterObliqueCounter == 5) {
                    for (Piece p : enwsCenterObliquePieces) {
                        p.addSquares(FiveSquare.ENWS_CENTER_OBLIQUE);
                    }
                }
                break;
            case 5:
                Piece[] enwsRightFourObliquePieces = new Piece[4];
                int enwsRightFourObliqueCounter = 0;
                for (int i = 1; 5 - i >= 1; i++) {
                    if (null != army.board.panel[i][5 - i]
                            && army.board.panel[i][5 - i].color.equals(piece.color)) {
                        enwsRightFourObliquePieces[i - 1] = army.board.panel[i][5 - i];
                        enwsRightFourObliqueCounter++;
                    }
                }
                if (enwsRightFourObliqueCounter == 4) {
                    for (Piece p : enwsRightFourObliquePieces) {
                        p.addSquares(FiveSquare.ENWS_RIGHT_FOUR_OBLIQUE);
                    }
                }
                break;
            case 6:
                Piece[] enwsRightThreeObliquePieces = new Piece[3];
                int enwsRightThreeObliqueCounter = 0;
                for (int i = 2; 6 - i >= 2; i++) {
                    if (null != army.board.panel[i][6 - i]
                            && army.board.panel[i][6 - i].color.equals(piece.color)) {
                        enwsRightThreeObliquePieces[i - 2] = army.board.panel[i][6 - i];
                        enwsRightThreeObliqueCounter++;
                    }
                }
                if (enwsRightThreeObliqueCounter == 3) {
                    for (Piece p : enwsRightThreeObliquePieces) {
                        p.addSquares(FiveSquare.ENWS_RIGHT_THREE_OBLIQUE);
                    }
                }
                break;
            default:
                break;
        }
    }
}
