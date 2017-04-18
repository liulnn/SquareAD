package xyz.flove.square.entities;

import xyz.flove.square.ai.ScoreAi;
import xyz.flove.square.enums.Color;
import xyz.flove.square.enums.Direction;
import xyz.flove.square.enums.SuccessEnum;

import android.util.SparseArray;

/**
 * 成方：
 * 单方：放子或走子棋盘的任意地方成单一正方格。
 * 双方：放子或走子棋盘的任意地方成二个正方格。
 * 三方：放子或走子棋盘的任意地方成三个正方格，仅出现在满盘前。
 * 四方：放子或走子棋盘的任意地方成四个正方格，仅出现在满盘前。
 * 成拉：放子或走子五棋子在一条线上，横竖匀可（除四条边）。
 * 成斜：
 * 三斜：放子或走子成三棋子在一条斜线上，仅有四个，标有实心。
 * 四斜：放子或走子成四棋子在一条斜线上，仅有四个，标有空心。
 * 五斜：放子或走子成五棋子在一条斜线上，仅有二个。
 *
 * @author preture
 */
public class SuccessSquare {

    public Piece piece;
    public SparseArray<SparseArray<Piece>> success;
    public boolean isSuccess;

    public SuccessSquare(Piece piece) {
        this.piece = piece;
        //存储棋子的每个成方的信息
        success = new SparseArray<SparseArray<Piece>>(20);
        this.isSuccess = false;
    }

    public void clearSuccess() {
        for (int i = 0; i < 20; i++) {
            if (null != success.get(i) && success.get(i).size() > 0) {
                clearSuccess(i, success.get(i));
            }
        }
        isSuccess = false;
    }

    private void clearSuccess(int successEnumNumber, SparseArray<Piece> pieces) {
        switch (SuccessEnum.getSuccessEnum(successEnumNumber)) {
            case WEST_NORTH_SQUARE:
                Piece westPiece1 = pieces.get(0);
                Piece westNorthPiece1 = pieces.get(1);
                Piece northPiece1 = pieces.get(2);
                westPiece1.square.success.delete(SuccessEnum.EAST_NORTH_SQUARE
                        .ordinal());
                if (null == westPiece1.square.success
                        || westPiece1.square.success.size() == 0) {
                    westPiece1.square.isSuccess = false;
                }

                westNorthPiece1.square.success.delete(SuccessEnum.EAST_SORTH_SQUARE
                        .ordinal());
                if (null == westNorthPiece1.square.success
                        || westNorthPiece1.square.success.size() == 0) {
                    westNorthPiece1.square.isSuccess = false;
                }
                northPiece1.square.success.delete(SuccessEnum.WEST_SORTH_SQUARE
                        .ordinal());
                if (null == northPiece1.square.success
                        || northPiece1.square.success.size() == 0) {
                    northPiece1.square.isSuccess = false;
                }
                break;
            case WEST_SORTH_SQUARE:
                Piece sorthPiece2 = pieces.get(0);
                Piece westSorthPiece2 = pieces.get(1);
                Piece westPiece2 = pieces.get(2);

                sorthPiece2.square.success.delete(SuccessEnum.WEST_NORTH_SQUARE
                        .ordinal());
                if (null == sorthPiece2.square.success
                        || sorthPiece2.square.success.size() == 0) {
                    sorthPiece2.square.isSuccess = false;
                }
                westSorthPiece2.square.success.delete(SuccessEnum.EAST_NORTH_SQUARE
                        .ordinal());
                if (null == westSorthPiece2.square.success
                        || westSorthPiece2.square.success.size() == 0) {
                    westSorthPiece2.square.isSuccess = false;
                }

                westPiece2.square.success.delete(SuccessEnum.EAST_SORTH_SQUARE
                        .ordinal());
                if (null == westPiece2.square.success
                        || westPiece2.square.success.size() == 0) {
                    westPiece2.square.isSuccess = false;
                }
                break;
            case EAST_NORTH_SQUARE:
                Piece northPiece3 = pieces.get(0);
                Piece eastNorthPiece3 = pieces.get(1);
                Piece eastPiece3 = pieces.get(2);

                northPiece3.square.success.delete(SuccessEnum.EAST_SORTH_SQUARE
                        .ordinal());
                if (null == northPiece3.square.success
                        || northPiece3.square.success.size() == 0) {
                    northPiece3.square.isSuccess = false;
                }
                eastNorthPiece3.square.success.delete(SuccessEnum.WEST_SORTH_SQUARE
                        .ordinal());
                if (null == eastNorthPiece3.square.success
                        || eastNorthPiece3.square.success.size() == 0) {
                    eastNorthPiece3.square.isSuccess = false;
                }
                eastPiece3.square.success.delete(SuccessEnum.WEST_NORTH_SQUARE
                        .ordinal());
                if (null == eastPiece3.square.success
                        || eastPiece3.square.success.size() == 0) {
                    eastPiece3.square.isSuccess = false;
                }
                break;
            case EAST_SORTH_SQUARE:
                Piece eastPiece4 = pieces.get(0);
                Piece eastSorthPiece4 = pieces.get(1);
                Piece sorthPiece4 = pieces.get(2);

                eastPiece4.square.success.delete(SuccessEnum.WEST_SORTH_SQUARE
                        .ordinal());
                if (null == eastPiece4.square.success
                        || eastPiece4.square.success.size() == 0) {
                    eastPiece4.square.isSuccess = false;
                }
                eastSorthPiece4.square.success.delete(SuccessEnum.WEST_NORTH_SQUARE
                        .ordinal());
                if (null == eastSorthPiece4.square.success
                        || eastSorthPiece4.square.success.size() == 0) {
                    eastSorthPiece4.square.isSuccess = false;
                }
                sorthPiece4.square.success.delete(SuccessEnum.EAST_NORTH_SQUARE
                        .ordinal());
                if (null == sorthPiece4.square.success
                        || sorthPiece4.square.success.size() == 0) {
                    sorthPiece4.square.isSuccess = false;
                }
                break;
            default:
                for (int i = 0; i < pieces.size(); i++) {
                    Piece piece = pieces.get(i);
                    if (piece.equals(this.piece)) {
                        continue;
                    }
                    piece.square.success.delete(successEnumNumber);
                    if (null == piece.square.success
                            || piece.square.success.size() == 0) {
                        piece.square.isSuccess = false;
                    }

                }
        }
        success.delete(successEnumNumber);
    }

    private void handleSuccess(SuccessEnum successEnum,
                               SparseArray<Piece> pieces) {
        switch (successEnum) {
            case WEST_NORTH_SQUARE:
                Piece westPiece1 = pieces.get(0);
                Piece westNorthPiece1 = pieces.get(1);
                Piece northPiece1 = pieces.get(2);

                SparseArray<Piece> westPieces1 = new SparseArray<Piece>(3);
                westPieces1.put(0, westNorthPiece1);
                westPieces1.put(1, northPiece1);
                westPieces1.put(2, piece);
                westPiece1.square.success.put(
                        SuccessEnum.EAST_NORTH_SQUARE.ordinal(), westPieces1);
                westPiece1.square.isSuccess = true;

                SparseArray<Piece> westNorthPieces1 = new SparseArray<Piece>(3);
                westNorthPieces1.put(0, northPiece1);
                westNorthPieces1.put(1, piece);
                westNorthPieces1.put(2, westPiece1);
                westNorthPiece1.square.success.put(
                        SuccessEnum.EAST_SORTH_SQUARE.ordinal(), westNorthPieces1);
                westNorthPiece1.square.isSuccess = true;

                SparseArray<Piece> northPieces1 = new SparseArray<Piece>(3);
                northPieces1.put(0, piece);
                northPieces1.put(1, westPiece1);
                northPieces1.put(2, westNorthPiece1);
                northPiece1.square.success.put(
                        SuccessEnum.WEST_SORTH_SQUARE.ordinal(), northPieces1);
                northPiece1.square.isSuccess = true;
                break;
            case WEST_SORTH_SQUARE:
                Piece sorthPiece2 = pieces.get(0);
                Piece westSorthPiece2 = pieces.get(1);
                Piece westPiece2 = pieces.get(2);

                SparseArray<Piece> sorthPieces2 = new SparseArray<Piece>(3);
                sorthPieces2.put(0, westSorthPiece2);
                sorthPieces2.put(1, westPiece2);
                sorthPieces2.put(2, piece);
                sorthPiece2.square.success.put(
                        SuccessEnum.WEST_NORTH_SQUARE.ordinal(), sorthPieces2);
                sorthPiece2.square.isSuccess = true;

                SparseArray<Piece> westSorthPieces2 = new SparseArray<Piece>(3);
                westSorthPieces2.put(0, westPiece2);
                westSorthPieces2.put(1, piece);
                westSorthPieces2.put(2, sorthPiece2);
                westSorthPiece2.square.success.put(
                        SuccessEnum.EAST_NORTH_SQUARE.ordinal(), westSorthPieces2);
                westSorthPiece2.square.isSuccess = true;

                SparseArray<Piece> westPieces2 = new SparseArray<Piece>(3);
                westPieces2.put(0, piece);
                westPieces2.put(1, sorthPiece2);
                westPieces2.put(2, westSorthPiece2);
                westPiece2.square.success.put(
                        SuccessEnum.EAST_SORTH_SQUARE.ordinal(), westPieces2);
                westPiece2.square.isSuccess = true;
                break;
            case EAST_NORTH_SQUARE:
                Piece northPiece3 = pieces.get(0);
                Piece eastNorthPiece3 = pieces.get(1);
                Piece eastPiece3 = pieces.get(2);

                SparseArray<Piece> northPieces3 = new SparseArray<Piece>(3);
                northPieces3.put(0, eastNorthPiece3);
                northPieces3.put(1, eastPiece3);
                northPieces3.put(2, piece);
                northPiece3.square.success.put(
                        SuccessEnum.EAST_SORTH_SQUARE.ordinal(), northPieces3);
                northPiece3.square.isSuccess = true;

                SparseArray<Piece> eastNorthPieces3 = new SparseArray<Piece>(3);
                eastNorthPieces3.put(0, eastPiece3);
                eastNorthPieces3.put(1, piece);
                eastNorthPieces3.put(2, northPiece3);
                eastNorthPiece3.square.success.put(
                        SuccessEnum.WEST_SORTH_SQUARE.ordinal(), eastNorthPieces3);
                eastNorthPiece3.square.isSuccess = true;

                SparseArray<Piece> eastPieces3 = new SparseArray<Piece>(3);
                eastPieces3.put(0, piece);
                eastPieces3.put(1, northPiece3);
                eastPieces3.put(2, eastNorthPiece3);
                eastPiece3.square.success.put(
                        SuccessEnum.WEST_NORTH_SQUARE.ordinal(), eastPieces3);
                eastPiece3.square.isSuccess = true;
                break;
            case EAST_SORTH_SQUARE:
                Piece eastPiece4 = pieces.get(0);
                Piece eastSorthPiece4 = pieces.get(1);
                Piece sorthPiece4 = pieces.get(2);

                SparseArray<Piece> eastPieces4 = new SparseArray<Piece>(3);
                eastPieces4.put(0, eastSorthPiece4);
                eastPieces4.put(1, sorthPiece4);
                eastPieces4.put(2, piece);
                eastPiece4.square.success.put(
                        SuccessEnum.WEST_SORTH_SQUARE.ordinal(), eastPieces4);
                eastPiece4.square.isSuccess = true;

                SparseArray<Piece> eastSorthPieces4 = new SparseArray<Piece>(3);
                eastSorthPieces4.put(0, sorthPiece4);
                eastSorthPieces4.put(1, piece);
                eastSorthPieces4.put(2, eastPiece4);
                eastSorthPiece4.square.success.put(
                        SuccessEnum.WEST_NORTH_SQUARE.ordinal(), eastSorthPieces4);
                eastSorthPiece4.square.isSuccess = true;

                SparseArray<Piece> sorthPieces4 = new SparseArray<Piece>(3);
                sorthPieces4.put(0, piece);
                sorthPieces4.put(1, eastPiece4);
                sorthPieces4.put(2, eastSorthPiece4);
                sorthPiece4.square.success.put(
                        SuccessEnum.EAST_NORTH_SQUARE.ordinal(), sorthPieces4);
                sorthPiece4.square.isSuccess = true;
                break;
            default:
                for (int i = 0; i < pieces.size(); i++) {
                    Piece piece = pieces.get(i);
                    if (piece.equals(this.piece)) {
                        continue;
                    }
                    piece.square.success.put(successEnum.ordinal(), pieces);
                    piece.square.isSuccess = true;
                }
                break;
        }
        success.put(successEnum.ordinal(), pieces);
        isSuccess = true;
    }

    public SuccessResult computeSuccess() {
        computeFoursquareSuccess();
        computeLineSuccess();
        computeObliqueSuccess();
        return this.computeAllSuccess();
    }

    /**
     * 成方：放子或走子在棋盘的任意地方成正方格。
     * 单方：放子或走子棋盘的任意地方成单一正方格。
     * 双方：放子或走子棋盘的任意地方成二个正方格。
     * 三方：放子或走子棋盘的任意地方成三个正方格，仅出现在满盘前。
     * 四方：放子或走子棋盘的任意地方成四个正方格，仅出现在满盘前。
     */
    private void computeFoursquareSuccess() {
        SparseArray<Piece> around = new SparseArray<Piece>(8);
        // 子的方位：中，西，西北，北，东北，东，东南，南，西南
        // 获取该点八个方向的点
        if (piece.x - 1 >= 0
                && null != piece.panel[piece.x - 1][piece.y]
                && piece.panel[piece.x - 1][piece.y].color == piece.panel[piece.x][piece.y].color) {
            around.put(Direction.WEST.ordinal(),
                    piece.panel[piece.x - 1][piece.y]);
        }
        if (piece.y - 1 >= 0
                && null != piece.panel[piece.x][piece.y - 1]
                && piece.panel[piece.x][piece.y - 1].color == piece.panel[piece.x][piece.y].color) {
            around.put(Direction.NORTH.ordinal(),
                    piece.panel[piece.x][piece.y - 1]);
        }
        if (piece.x + 1 <= 4
                && null != piece.panel[piece.x + 1][piece.y]
                && piece.panel[piece.x + 1][piece.y].color == piece.panel[piece.x][piece.y].color) {
            around.put(Direction.EAST.ordinal(),
                    piece.panel[piece.x + 1][piece.y]);
        }
        if (piece.y + 1 <= 4
                && null != piece.panel[piece.x][piece.y + 1]
                && piece.panel[piece.x][piece.y + 1].color == piece.panel[piece.x][piece.y].color) {
            around.put(Direction.SORTH.ordinal(),
                    piece.panel[piece.x][piece.y + 1]);
        }
        if (piece.x - 1 >= 0
                && piece.y - 1 >= 0
                && null != piece.panel[piece.x - 1][piece.y - 1]
                && piece.panel[piece.x - 1][piece.y - 1].color == piece.panel[piece.x][piece.y].color) {
            around.put(Direction.WEST_NORTH.ordinal(),
                    piece.panel[piece.x - 1][piece.y - 1]);
        }
        if (piece.x - 1 >= 0
                && piece.y + 1 <= 4
                && null != piece.panel[piece.x - 1][piece.y + 1]
                && piece.panel[piece.x - 1][piece.y + 1].color == piece.panel[piece.x][piece.y].color) {
            around.put(Direction.WEST_SORTH.ordinal(),
                    piece.panel[piece.x - 1][piece.y + 1]);
        }
        if (piece.x + 1 <= 4
                && piece.y - 1 >= 0
                && null != piece.panel[piece.x + 1][piece.y - 1]
                && piece.panel[piece.x + 1][piece.y - 1].color == piece.panel[piece.x][piece.y].color) {
            around.put(Direction.EAST_NORTH.ordinal(),
                    piece.panel[piece.x + 1][piece.y - 1]);
        }
        if (piece.x + 1 <= 4
                && piece.y + 1 <= 4
                && null != piece.panel[piece.x + 1][piece.y + 1]
                && piece.panel[piece.x + 1][piece.y + 1].color == piece.panel[piece.x][piece.y].color) {
            around.put(Direction.EAST_SORTH.ordinal(),
                    piece.panel[piece.x + 1][piece.y + 1]);
        }
        // 西北方
        if (null != around.get(Direction.WEST.ordinal())
                && null != around.get(Direction.WEST_NORTH.ordinal())
                && null != around.get(Direction.NORTH.ordinal())) {
            SparseArray<Piece> pieces = new SparseArray<Piece>(3);
            pieces.put(0, around.get(Direction.WEST.ordinal()));
            pieces.put(1, around.get(Direction.WEST_NORTH.ordinal()));
            pieces.put(2, around.get(Direction.NORTH.ordinal()));
            handleSuccess(SuccessEnum.WEST_NORTH_SQUARE, pieces);
        }

        // 西南方
        if (null != around.get(Direction.WEST.ordinal())
                && null != around.get(Direction.WEST_SORTH.ordinal())
                && null != around.get(Direction.SORTH.ordinal())) {
            SparseArray<Piece> pieces = new SparseArray<Piece>(3);
            pieces.put(0, around.get(Direction.SORTH.ordinal()));
            pieces.put(1, around.get(Direction.WEST_SORTH.ordinal()));
            pieces.put(2, around.get(Direction.WEST.ordinal()));
            handleSuccess(SuccessEnum.WEST_SORTH_SQUARE, pieces);
        }
        // 东北方
        if (null != around.get(Direction.EAST.ordinal())
                && null != around.get(Direction.EAST_NORTH.ordinal())
                && null != around.get(Direction.NORTH.ordinal())) {
            SparseArray<Piece> pieces = new SparseArray<Piece>(3);
            pieces.put(0, around.get(Direction.NORTH.ordinal()));
            pieces.put(1, around.get(Direction.EAST_NORTH.ordinal()));
            pieces.put(2, around.get(Direction.EAST.ordinal()));
            handleSuccess(SuccessEnum.EAST_NORTH_SQUARE, pieces);
        }
        // 东南方
        if (null != around.get(Direction.EAST.ordinal())
                && null != around.get(Direction.EAST_SORTH.ordinal())
                && null != around.get(Direction.SORTH.ordinal())) {
            SparseArray<Piece> pieces = new SparseArray<Piece>(3);
            pieces.put(0, around.get(Direction.EAST.ordinal()));
            pieces.put(1, around.get(Direction.EAST_SORTH.ordinal()));
            pieces.put(2, around.get(Direction.SORTH.ordinal()));
            handleSuccess(SuccessEnum.EAST_SORTH_SQUARE, pieces);
        }
    }

    /**
     * 成拉：放子或走子五棋子在一条线上，横竖匀可（除四条边）。
     */
    private void computeLineSuccess() {
        if (piece.x == 0 || piece.x == 4 || piece.y == 0 || piece.y == 4) {
            return;
        }

        SparseArray<Piece> latLinePieces = new SparseArray<Piece>(5);
        SparseArray<Piece> lonLinePieces = new SparseArray<Piece>(5);

        for (int i = 0; i < 5; i++) {
            if (null != piece.panel[i][piece.y]
                    && piece.panel[i][piece.y].color == piece.panel[piece.x][piece.y].color) {
                latLinePieces.put(i, piece.panel[i][piece.y]);
            }

        }
        for (int i = 0; i < 5; i++) {
            if (null != piece.panel[piece.x][i]
                    && piece.panel[piece.x][i].color == piece.panel[piece.x][piece.y].color) {
                lonLinePieces.put(i, piece.panel[piece.x][i]);
            }
        }
        if (latLinePieces.size() == 5) {
            switch (piece.y) {
                case 1:
                    handleSuccess(SuccessEnum.NORTH_LAT_LINE, latLinePieces);
                    break;
                case 2:
                    handleSuccess(SuccessEnum.CENTER_LAT_LINE, latLinePieces);
                    break;
                case 3:
                    handleSuccess(SuccessEnum.SORTH_LAT_LINE, latLinePieces);
                    break;
            }
        }
        if (lonLinePieces.size() == 5) {
            switch (piece.x) {
                case 1:
                    handleSuccess(SuccessEnum.WEST_LON_LINE, lonLinePieces);
                    break;
                case 2:
                    handleSuccess(SuccessEnum.CENTER_LON_LINE, lonLinePieces);
                    break;
                case 3:
                    handleSuccess(SuccessEnum.EAST_LON_LINE, lonLinePieces);
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
        computeWNESObliqueSuccess();
        computeENWSObliqueSuccess();
    }

    /**
     * 成斜：西北东南
     */
    private void computeWNESObliqueSuccess() {
        switch (piece.y - piece.x) {
            case -2:
                SparseArray<Piece> wnesRightThreeObliquePieces = new SparseArray<Piece>(
                        3);
                for (int i = 0; i + 2 <= 4; i++) {
                    if (null != piece.panel[i + 2][i]
                            && piece.panel[i + 2][i].color == piece.panel[piece.x][piece.y].color) {
                        wnesRightThreeObliquePieces.put(i, piece.panel[i + 2][i]);
                    }
                }
                if (wnesRightThreeObliquePieces.size() == 3) {
                    handleSuccess(SuccessEnum.WNES_RIGHT_THREE_OBLIQUE,
                            wnesRightThreeObliquePieces);
                }
                break;
            case -1:
                SparseArray<Piece> wnesRightFourObliquePieces = new SparseArray<Piece>(
                        4);
                for (int i = 0; i + 1 <= 4; i++) {
                    if (null != piece.panel[i + 1][i]
                            && piece.panel[i + 1][i].color == piece.panel[piece.x][piece.y].color) {
                        wnesRightFourObliquePieces.put(i, piece.panel[i + 1][i]);
                    }
                }
                if (wnesRightFourObliquePieces.size() == 4) {
                    handleSuccess(SuccessEnum.WNES_RIGHT_FOUR_OBLIQUE,
                            wnesRightFourObliquePieces);
                }
                break;
            case 0:
                SparseArray<Piece> wnesCenterObliquePieces = new SparseArray<Piece>(
                        5);
                for (int i = 0; i <= 4; i++) {
                    if (null != piece.panel[i][i]
                            && piece.panel[i][i].color == piece.panel[piece.x][piece.y].color) {
                        wnesCenterObliquePieces.put(i, piece.panel[i][i]);
                    }
                }
                if (wnesCenterObliquePieces.size() == 5) {
                    handleSuccess(SuccessEnum.WNES_CENTER_OBLIQUE,
                            wnesCenterObliquePieces);
                }
                break;
            case 1:
                SparseArray<Piece> wnesLeftFourObliquePieces = new SparseArray<Piece>(
                        4);
                for (int i = 0; i + 1 <= 4; i++) {
                    if (null != piece.panel[i][i + 1]
                            && piece.panel[i][i + 1].color == piece.panel[piece.x][piece.y].color) {
                        wnesLeftFourObliquePieces.put(i, piece.panel[i][i + 1]);
                    }
                }
                if (wnesLeftFourObliquePieces.size() == 4) {
                    handleSuccess(SuccessEnum.WNES_LEFT_FOUR_OBLIQUE,
                            wnesLeftFourObliquePieces);
                }
                break;
            case 2:
                SparseArray<Piece> wnesLeftThreeObliquePieces = new SparseArray<Piece>(
                        3);
                for (int i = 0; i + 2 <= 4; i++) {
                    if (null != piece.panel[i][i + 2]
                            && piece.panel[i][i + 2].color == piece.panel[piece.x][piece.y].color) {
                        wnesLeftThreeObliquePieces.put(i, piece.panel[i][i + 2]);
                    }
                }
                if (wnesLeftThreeObliquePieces.size() == 3) {
                    handleSuccess(SuccessEnum.WNES_LEFT_THREE_OBLIQUE,
                            wnesLeftThreeObliquePieces);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 成斜：东北西南
     */
    private void computeENWSObliqueSuccess() {
        switch (piece.y + piece.x) {
            case 2:
                SparseArray<Piece> enwsLeftThreeObliquePieces = new SparseArray<Piece>(
                        3);
                for (int i = 0; 2 - i >= 0; i++) {
                    if (null != piece.panel[i][2 - i]
                            && piece.panel[i][2 - i].color == piece.panel[piece.x][piece.y].color) {
                        enwsLeftThreeObliquePieces.put(i, piece.panel[i][2 - i]);
                    }
                }
                if (enwsLeftThreeObliquePieces.size() == 3) {
                    handleSuccess(SuccessEnum.ENWS_LEFT_THREE_OBLIQUE,
                            enwsLeftThreeObliquePieces);
                }
                break;
            case 3:
                SparseArray<Piece> enwsLeftFourObliquePieces = new SparseArray<Piece>(
                        4);
                for (int i = 0; 3 - i >= 0; i++) {
                    if (null != piece.panel[i][3 - i]
                            && piece.panel[i][3 - i].color == piece.panel[piece.x][piece.y].color) {
                        enwsLeftFourObliquePieces.put(i, piece.panel[i][3 - i]);
                    }
                }
                if (enwsLeftFourObliquePieces.size() == 4) {
                    handleSuccess(SuccessEnum.ENWS_LEFT_FOUR_OBLIQUE,
                            enwsLeftFourObliquePieces);
                }
                break;
            case 4:
                SparseArray<Piece> enwsCenterObliquePieces = new SparseArray<Piece>(
                        5);
                for (int i = 0; 4 - i >= 0; i++) {
                    if (null != piece.panel[i][4 - i]
                            && piece.panel[i][4 - i].color == piece.panel[piece.x][piece.y].color) {
                        enwsCenterObliquePieces.put(i, piece.panel[i][4 - i]);
                    }
                }
                if (enwsCenterObliquePieces.size() == 5) {
                    handleSuccess(SuccessEnum.ENWS_CENTER_OBLIQUE,
                            enwsCenterObliquePieces);
                }
                break;
            case 5:
                SparseArray<Piece> enwsRightFourObliquePieces = new SparseArray<Piece>(
                        4);
                for (int i = 1; 5 - i >= 1; i++) {
                    if (null != piece.panel[i][5 - i]
                            && piece.panel[i][5 - i].color == piece.panel[piece.x][piece.y].color) {
                        enwsRightFourObliquePieces
                                .put(i - 1, piece.panel[i][5 - i]);
                    }
                }
                if (enwsRightFourObliquePieces.size() == 4) {
                    handleSuccess(SuccessEnum.ENWS_RIGHT_FOUR_OBLIQUE,
                            enwsRightFourObliquePieces);
                }
                break;
            case 6:
                SparseArray<Piece> enwsRightThreeObliquePieces = new SparseArray<Piece>(
                        3);
                for (int i = 2; 6 - i >= 2; i++) {
                    if (null != piece.panel[i][6 - i]
                            && piece.panel[i][6 - i].color == piece.panel[piece.x][piece.y].color) {
                        enwsRightThreeObliquePieces.put(i - 2,
                                piece.panel[i][6 - i]);
                    }
                }
                if (enwsRightThreeObliquePieces.size() == 3) {
                    handleSuccess(SuccessEnum.ENWS_RIGHT_THREE_OBLIQUE,
                            enwsRightThreeObliquePieces);
                }
                break;
            default:
                break;
        }
    }

    private SuccessResult computeAllSuccess() {
        if (null == success) {
            return new SuccessResult();
        }
        int step = 0;
        int score = 0;
        for (int i = 0; i < 20; i++) {
            SparseArray<Piece> successPieces = success.get(i);
            if (null == successPieces || successPieces.size() == 0) {
                continue;
            }
            if (i == SuccessEnum.WEST_NORTH_SQUARE.ordinal()
                    || i == SuccessEnum.WEST_SORTH_SQUARE.ordinal()
                    || i == SuccessEnum.EAST_NORTH_SQUARE.ordinal()
                    || i == SuccessEnum.EAST_SORTH_SQUARE.ordinal()
                    || i == SuccessEnum.WNES_LEFT_THREE_OBLIQUE.ordinal()
                    || i == SuccessEnum.WNES_LEFT_FOUR_OBLIQUE.ordinal()
                    || i == SuccessEnum.WNES_RIGHT_FOUR_OBLIQUE.ordinal()
                    || i == SuccessEnum.WNES_RIGHT_THREE_OBLIQUE.ordinal()
                    || i == SuccessEnum.ENWS_LEFT_THREE_OBLIQUE.ordinal()
                    || i == SuccessEnum.ENWS_LEFT_FOUR_OBLIQUE.ordinal()
                    || i == SuccessEnum.ENWS_RIGHT_FOUR_OBLIQUE.ordinal()
                    || i == SuccessEnum.ENWS_RIGHT_THREE_OBLIQUE.ordinal()) {
                step += 1;
                score += 1;
            } else if (i == SuccessEnum.SORTH_LAT_LINE.ordinal()
                    || i == SuccessEnum.CENTER_LAT_LINE.ordinal()
                    || i == SuccessEnum.NORTH_LAT_LINE.ordinal()
                    || i == SuccessEnum.WEST_LON_LINE.ordinal()
                    || i == SuccessEnum.CENTER_LON_LINE.ordinal()
                    || i == SuccessEnum.EAST_LON_LINE.ordinal()
                    || i == SuccessEnum.WNES_CENTER_OBLIQUE.ordinal()
                    || i == SuccessEnum.ENWS_CENTER_OBLIQUE.ordinal()) {
                step += 2;
                score += 2;
            }
        }
        return new SuccessResult(step, score);
    }
}