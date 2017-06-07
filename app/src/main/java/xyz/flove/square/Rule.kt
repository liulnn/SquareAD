package xyz.flove.square

import xyz.flove.square.core.Position
import xyz.flove.square.core.FiveSquare


class Rule(private val army: Army, private val position: Position) {

    fun squares() {
        army.board.addPiece(position, army.color)
        computeFoursquareSuccess()
        computeLineSuccess()
        computeObliqueSuccess()
    }

    /**
     * 成方：放子或走子在棋盘的任意地方成正方格。
     * 单方：放子或走子棋盘的任意地方成单一正方格。
     * 双方：放子或走子棋盘的任意地方成二个正方格。
     * 三方：放子或走子棋盘的任意地方成三个正方格，仅出现在满盘前。
     * 四方：放子或走子棋盘的任意地方成四个正方格，仅出现在满盘前。
     */
    private fun computeFoursquareSuccess() {
        // 子的方位：中，西，西北，北，东北，东，东南，南，西南
        val piece = army.board.getPiece(position.x, position.y)
        var westPiece: Piece? = null
        if (piece!!.position.x - 1 >= 0) {
            westPiece = army.board.getPiece(position.x - 1, position.y)
        }
        var westNorthPiece: Piece? = null
        if (piece.position.x - 1 >= 0 && piece.position.y - 1 >= 0) {
            westNorthPiece = army.board.getPiece(position.x - 1, position.y - 1)
        }
        var northPiece: Piece? = null
        if (piece.position.y - 1 >= 0) {
            northPiece = army.board.getPiece(position.x, position.y - 1)
        }
        var eastNorthPiece: Piece? = null
        if (piece.position.x + 1 <= 4 && piece.position.y - 1 >= 0) {
            eastNorthPiece = army.board.getPiece(position.x + 1, position.y - 1)
        }
        var eastPiece: Piece? = null
        if (piece.position.x + 1 <= 4) {
            eastPiece = army.board.getPiece(position.x + 1, position.y)
        }
        var eastSorthPiece: Piece? = null
        if (piece.position.x + 1 <= 4 && piece.position.y + 1 <= 4) {
            eastSorthPiece = army.board.getPiece(position.x + 1, position.y + 1)
        }
        var sorthPiece: Piece? = null
        if (piece.position.y + 1 <= 4) {
            sorthPiece = army.board.getPiece(position.x, position.y + 1)
        }
        var westSorthPiece: Piece? = null
        if (piece.position.x - 1 >= 0 && piece.position.y + 1 <= 4) {
            westSorthPiece = army.board.getPiece(position.x - 1, position.y + 1)
        }

        // 西北方
        if (null != westPiece && null != westNorthPiece && null != northPiece
                && westPiece.color == piece.color
                && westNorthPiece.color == piece.color
                && northPiece.color == piece.color) {
            piece.addSquares(FiveSquare.WEST_NORTH_SQUARE)
            westPiece.addSquares(FiveSquare.EAST_NORTH_SQUARE)
            westNorthPiece.addSquares(FiveSquare.EAST_SORTH_SQUARE)
            northPiece.addSquares(FiveSquare.WEST_SORTH_SQUARE)
        }

        // 西南方
        if (null != westPiece && null != westSorthPiece && null != sorthPiece
                && westPiece.color == piece.color
                && westSorthPiece.color == piece.color
                && sorthPiece.color == piece.color) {
            piece.addSquares(FiveSquare.WEST_SORTH_SQUARE)
            sorthPiece.addSquares(FiveSquare.WEST_NORTH_SQUARE)
            westSorthPiece.addSquares(FiveSquare.EAST_NORTH_SQUARE)
            westPiece.addSquares(FiveSquare.EAST_SORTH_SQUARE)
        }
        // 东北方
        if (null != eastPiece && null != eastNorthPiece && null != northPiece
                && eastPiece.color == piece.color
                && eastNorthPiece.color == piece.color
                && northPiece.color == piece.color) {
            piece.addSquares(FiveSquare.EAST_NORTH_SQUARE)
            northPiece.addSquares(FiveSquare.EAST_SORTH_SQUARE)
            eastNorthPiece.addSquares(FiveSquare.WEST_SORTH_SQUARE)
            eastPiece.addSquares(FiveSquare.WEST_NORTH_SQUARE)
        }
        // 东南方
        if (null != eastPiece && null != eastSorthPiece && null != sorthPiece
                && eastPiece.color == piece.color
                && eastSorthPiece.color == piece.color
                && sorthPiece.color == piece.color) {
            piece.addSquares(FiveSquare.EAST_SORTH_SQUARE)
            eastPiece.addSquares(FiveSquare.WEST_SORTH_SQUARE)
            eastSorthPiece.addSquares(FiveSquare.WEST_NORTH_SQUARE)
            sorthPiece.addSquares(FiveSquare.EAST_NORTH_SQUARE)
        }
    }

    /**
     * 成拉：放子或走子五棋子在一条线上，横竖匀可（除四条边）。
     */
    private fun computeLineSuccess() {
        val piece = army.board.getPiece(position.x, position.y)

        val latLinePieces = arrayOfNulls<Piece>(5)
        val lonLinePieces = arrayOfNulls<Piece>(5)

        var latLineCounter = 0
        var lonLineCounter = 0

        for (i in 0..4) {
            if (null != army.board.getPiece(i, position.y) && army.board.getPiece(i, position.y)!!.color == piece!!.color) {
                latLinePieces[i] = army.board.getPiece(i, position.y)
                latLineCounter++
            }
        }
        for (i in 0..4) {
            if (null != army.board.getPiece(position.x, i) && army.board.getPiece(position.x, i)!!.color == piece!!.color) {
                lonLinePieces[i] = army.board.getPiece(position.x, i)
                lonLineCounter++
            }
        }
        if (latLineCounter == 5) {
            when (position.y) {
                1 -> for (p in latLinePieces) {
                    p!!.addSquares(FiveSquare.NORTH_LAT_LINE)
                }
                2 -> for (p in latLinePieces) {
                    p!!.addSquares(FiveSquare.CENTER_LAT_LINE)
                }
                3 -> for (p in latLinePieces) {
                    p!!.addSquares(FiveSquare.SORTH_LAT_LINE)
                }
            }
        }
        if (lonLineCounter == 5) {
            when (position.x) {
                1 -> for (p in lonLinePieces) {
                    p!!.addSquares(FiveSquare.WEST_LON_LINE)
                }
                2 -> for (p in lonLinePieces) {
                    p!!.addSquares(FiveSquare.CENTER_LON_LINE)
                }
                3 -> for (p in lonLinePieces) {
                    p!!.addSquares(FiveSquare.EAST_LON_LINE)
                }
            }
        }
    }

    /**
     * 成斜：放子或走子成一条斜线。
     * 三斜：放子或走子成三棋子在一条斜线上，仅有四个，标有实心。
     * 四斜：放子或走子成四棋子在一条斜线上，仅有四个，标有空心。
     * 五斜：放子或走子成五棋子在一条斜线上，仅有二个。
     */
    private fun computeObliqueSuccess() {
        val piece = army.board.getPiece(position.x, position.y)
        //成斜：西北东南
        when (position.y - position.x) {
            -2 -> {
                val wnesRightThreeObliquePieces = arrayOfNulls<Piece>(3)
                var wnesRightThreeObliqueCounter = 0
                run {
                    var i = 0
                    while (i + 2 <= 4) {
                        if (null != army.board.getPiece(i + 2, i) && army.board.getPiece(i + 2, i)!!.color == piece!!.color) {
                            wnesRightThreeObliquePieces[i] = army.board.getPiece(i + 2, i)
                            wnesRightThreeObliqueCounter += 1
                        }
                        i++
                    }
                }
                if (wnesRightThreeObliqueCounter == 3) {
                    for (p in wnesRightThreeObliquePieces) {
                        p!!.addSquares(FiveSquare.WNES_RIGHT_THREE_OBLIQUE)
                    }
                }
            }
            -1 -> {
                val wnesRightFourObliquePieces = arrayOfNulls<Piece>(4)
                var wnesRightFourObliqueCounter = 0
                run {
                    var i = 0
                    while (i + 1 <= 4) {
                        if (null != army.board.getPiece(i + 1, i) && army.board.getPiece(i + 1, i)!!.color == piece!!.color) {
                            wnesRightFourObliquePieces[i] = army.board.getPiece(i + 1, i)
                            wnesRightFourObliqueCounter++
                        }
                        i++
                    }
                }
                if (wnesRightFourObliqueCounter == 4) {
                    for (p in wnesRightFourObliquePieces) {
                        p!!.addSquares(FiveSquare.WNES_RIGHT_FOUR_OBLIQUE)
                    }
                }
            }
            0 -> {
                val wnesCenterObliquePieces = arrayOfNulls<Piece>(5)
                var wnesCenterObliqueCounter = 0
                for (i in 0..4) {
                    if (null != army.board.getPiece(i, i) && army.board.getPiece(i, i)!!.color == piece!!.color) {
                        wnesCenterObliquePieces[i] = army.board.getPiece(i, i)
                        wnesCenterObliqueCounter++
                    }
                }
                if (wnesCenterObliqueCounter == 5) {
                    for (p in wnesCenterObliquePieces) {
                        p!!.addSquares(FiveSquare.WNES_CENTER_OBLIQUE)
                    }
                }
            }
            1 -> {
                val wnesLeftFourObliquePieces = arrayOfNulls<Piece>(4)
                var wnesLeftFourObliqueCounter = 0
                run {
                    var i = 0
                    while (i + 1 <= 4) {
                        if (null != army.board.getPiece(i, i + 1) && army.board.getPiece(i, i + 1)!!.color == piece!!.color) {
                            wnesLeftFourObliquePieces[i] = army.board.getPiece(i, i + 1)
                            wnesLeftFourObliqueCounter++
                        }
                        i++
                    }
                }
                if (wnesLeftFourObliqueCounter == 4) {
                    for (p in wnesLeftFourObliquePieces) {
                        p!!.addSquares(FiveSquare.WNES_LEFT_FOUR_OBLIQUE)
                    }
                }
            }
            2 -> {
                val wnesLeftThreeObliquePieces = arrayOfNulls<Piece>(3)
                var wnesLeftThreeObliqueCounter = 0
                var i = 0
                while (i + 2 <= 4) {
                    if (null != army.board.getPiece(i, i + 2) && army.board.getPiece(i, i + 2)!!.color == piece!!.color) {
                        wnesLeftThreeObliquePieces[i] = army.board.getPiece(i, i + 2)
                        wnesLeftThreeObliqueCounter++
                    }
                    i++
                }
                if (wnesLeftThreeObliqueCounter == 3) {
                    for (p in wnesLeftThreeObliquePieces) {
                        p!!.addSquares(FiveSquare.WNES_LEFT_THREE_OBLIQUE)
                    }
                }
            }
            else -> {
            }
        }

        //成斜：东北西南
        when (position.y + position.x) {
            2 -> {
                val enwsLeftThreeObliquePieces = arrayOfNulls<Piece>(3)
                var enwsLeftThreeObliqueCounter = 0
                run {
                    var i = 0
                    while (2 - i >= 0) {
                        if (null != army.board.getPiece(i, 2 - i) && army.board.getPiece(i, 2 - i)!!.color == piece!!.color) {
                            enwsLeftThreeObliquePieces[i] = army.board.getPiece(i, 2 - i)
                            enwsLeftThreeObliqueCounter++
                        }
                        i++
                    }
                }
                if (enwsLeftThreeObliqueCounter == 3) {
                    for (p in enwsLeftThreeObliquePieces) {
                        p!!.addSquares(FiveSquare.ENWS_LEFT_THREE_OBLIQUE)
                    }
                }
            }
            3 -> {
                val enwsLeftFourObliquePieces = arrayOfNulls<Piece>(4)
                var enwsLeftFourObliqueCounter = 0
                run {
                    var i = 0
                    while (3 - i >= 0) {
                        if (null != army.board.getPiece(i, 3 - i) && army.board.getPiece(i, 3 - i)!!.color == piece!!.color) {
                            enwsLeftFourObliquePieces[i] = army.board.getPiece(i, 3 - i)
                            enwsLeftFourObliqueCounter++
                        }
                        i++
                    }
                }
                if (enwsLeftFourObliqueCounter == 4) {
                    for (p in enwsLeftFourObliquePieces) {
                        p!!.addSquares(FiveSquare.ENWS_LEFT_FOUR_OBLIQUE)
                    }
                }
            }
            4 -> {
                val enwsCenterObliquePieces = arrayOfNulls<Piece>(5)
                var enwsCenterObliqueCounter = 0
                run {
                    var i = 0
                    while (4 - i >= 0) {
                        if (null != army.board.getPiece(i, 4 - i) && army.board.getPiece(i, 4 - i)!!.color == piece!!.color) {
                            enwsCenterObliquePieces[i] = army.board.getPiece(i, 4 - i)
                            enwsCenterObliqueCounter++
                        }
                        i++
                    }
                }
                if (enwsCenterObliqueCounter == 5) {
                    for (p in enwsCenterObliquePieces) {
                        p!!.addSquares(FiveSquare.ENWS_CENTER_OBLIQUE)
                    }
                }
            }
            5 -> {
                val enwsRightFourObliquePieces = arrayOfNulls<Piece>(4)
                var enwsRightFourObliqueCounter = 0
                run {
                    var i = 1
                    while (5 - i >= 1) {
                        if (null != army.board.getPiece(i, 5 - i) && army.board.getPiece(i, 5 - i)!!.color == piece!!.color) {
                            enwsRightFourObliquePieces[i - 1] = army.board.getPiece(i, 5 - i)
                            enwsRightFourObliqueCounter++
                        }
                        i++
                    }
                }
                if (enwsRightFourObliqueCounter == 4) {
                    for (p in enwsRightFourObliquePieces) {
                        p!!.addSquares(FiveSquare.ENWS_RIGHT_FOUR_OBLIQUE)
                    }
                }
            }
            6 -> {
                val enwsRightThreeObliquePieces = arrayOfNulls<Piece>(3)
                var enwsRightThreeObliqueCounter = 0
                var i = 2
                while (6 - i >= 2) {
                    if (null != army.board.getPiece(i, 6 - i) && army.board.getPiece(i, 6 - i)!!.color == piece!!.color) {
                        enwsRightThreeObliquePieces[i - 2] = army.board.getPiece(i, 6 - i)
                        enwsRightThreeObliqueCounter++
                    }
                    i++
                }
                if (enwsRightThreeObliqueCounter == 3) {
                    for (p in enwsRightThreeObliquePieces) {
                        p!!.addSquares(FiveSquare.ENWS_RIGHT_THREE_OBLIQUE)
                    }
                }
            }
            else -> {
            }
        }
    }

    fun clearSquares() {
        val piece = army.board.getPiece(position)
        for (square in piece!!.squares!!) {
            when (square) {
                FiveSquare.WEST_NORTH_SQUARE -> {
                    val westPiece1 = army.board.getPiece(position.x - 1, position.y)
                    val westNorthPiece1 = army.board.getPiece(position.x - 1, position.y - 1)
                    val northPiece1 = army.board.getPiece(position.x, position.y - 1)

                    piece.deleteSquares(FiveSquare.WEST_NORTH_SQUARE)
                    westPiece1!!.deleteSquares(FiveSquare.EAST_NORTH_SQUARE)
                    westNorthPiece1!!.deleteSquares(FiveSquare.EAST_SORTH_SQUARE)
                    northPiece1!!.deleteSquares(FiveSquare.WEST_SORTH_SQUARE)
                }
                FiveSquare.WEST_SORTH_SQUARE -> {
                    val westPiece2 = army.board.getPiece(position.x - 1, position.y)
                    val sorthPiece2 = army.board.getPiece(position.x, position.y + 1)
                    val westSorthPiece2 = army.board.getPiece(position.x - 1, position.y + 1)

                    piece.deleteSquares(FiveSquare.WEST_SORTH_SQUARE)
                    sorthPiece2!!.deleteSquares(FiveSquare.WEST_NORTH_SQUARE)
                    westSorthPiece2!!.deleteSquares(FiveSquare.EAST_NORTH_SQUARE)
                    westPiece2!!.deleteSquares(FiveSquare.EAST_SORTH_SQUARE)
                }
                FiveSquare.EAST_NORTH_SQUARE -> {
                    val northPiece3 = army.board.getPiece(position.x, position.y - 1)
                    val eastNorthPiece3 = army.board.getPiece(position.x + 1, position.y - 1)
                    val eastPiece3 = army.board.getPiece(position.x + 1, position.y)

                    piece.deleteSquares(FiveSquare.EAST_NORTH_SQUARE)
                    northPiece3!!.deleteSquares(FiveSquare.EAST_SORTH_SQUARE)
                    eastNorthPiece3!!.deleteSquares(FiveSquare.WEST_SORTH_SQUARE)
                    eastPiece3!!.deleteSquares(FiveSquare.WEST_NORTH_SQUARE)
                }
                FiveSquare.EAST_SORTH_SQUARE -> {
                    val eastPiece4 = army.board.getPiece(position.x + 1, position.y)
                    val eastSorthPiece4 = army.board.getPiece(position.x + 1, position.y + 1)
                    val sorthPiece4 = army.board.getPiece(position.x, position.y + 1)

                    piece.deleteSquares(FiveSquare.EAST_SORTH_SQUARE)
                    eastPiece4!!.deleteSquares(FiveSquare.WEST_SORTH_SQUARE)
                    eastSorthPiece4!!.deleteSquares(FiveSquare.WEST_NORTH_SQUARE)
                    sorthPiece4!!.deleteSquares(FiveSquare.EAST_NORTH_SQUARE)
                }
                FiveSquare.SORTH_LAT_LINE, FiveSquare.CENTER_LAT_LINE, FiveSquare.NORTH_LAT_LINE -> for (i in 0..4) {
                    army.board.getPiece(i, position.y)!!.deleteSquares(square)
                }
                FiveSquare.EAST_LON_LINE, FiveSquare.CENTER_LON_LINE, FiveSquare.WEST_LON_LINE -> for (j in 0..4) {
                    army.board.getPiece(position.x, j)!!.deleteSquares(square)
                }
                FiveSquare.WNES_CENTER_OBLIQUE, FiveSquare.WNES_LEFT_FOUR_OBLIQUE, FiveSquare.WNES_LEFT_THREE_OBLIQUE, FiveSquare.WNES_RIGHT_FOUR_OBLIQUE, FiveSquare.WNES_RIGHT_THREE_OBLIQUE -> when (position.y - position.x) {
                    -2 -> run {
                        var i = 0
                        while (i + 2 <= 4) {
                            army.board.getPiece(i + 2, i)!!.deleteSquares(FiveSquare.WNES_RIGHT_THREE_OBLIQUE)
                            i++
                        }
                    }
                    -1 -> run {
                        var i = 0
                        while (i + 1 <= 4) {
                            army.board.getPiece(i + 1, i)!!.deleteSquares(FiveSquare.WNES_RIGHT_FOUR_OBLIQUE)
                            i++
                        }
                    }
                    0 -> for (i in 0..4) {
                        army.board.getPiece(i, i)!!.deleteSquares(FiveSquare.WNES_CENTER_OBLIQUE)
                    }
                    1 -> run {
                        var i = 0
                        while (i + 1 <= 4) {
                            army.board.getPiece(i, i + 1)!!.deleteSquares(FiveSquare.WNES_LEFT_FOUR_OBLIQUE)
                            i++
                        }
                    }
                    2 -> run {
                        var i = 0
                        while (i + 2 <= 4) {
                            army.board.getPiece(i, i + 2)!!.deleteSquares(FiveSquare.WNES_LEFT_THREE_OBLIQUE)
                            i++
                        }
                    }
                }
                FiveSquare.ENWS_CENTER_OBLIQUE, FiveSquare.ENWS_LEFT_FOUR_OBLIQUE, FiveSquare.ENWS_LEFT_THREE_OBLIQUE, FiveSquare.ENWS_RIGHT_FOUR_OBLIQUE, FiveSquare.ENWS_RIGHT_THREE_OBLIQUE -> when (position.y + position.x) {
                    2 -> run {
                        var i = 0
                        while (2 - i >= 0) {
                            army.board.getPiece(i, 2 - i)!!.deleteSquares(FiveSquare.ENWS_LEFT_THREE_OBLIQUE)
                            i++
                        }
                    }
                    3 -> run {
                        var i = 0
                        while (3 - i >= 0) {
                            army.board.getPiece(i, 3 - i)!!.deleteSquares(FiveSquare.ENWS_LEFT_FOUR_OBLIQUE)
                            i++
                        }
                    }
                    4 -> run {
                        var i = 0
                        while (4 - i >= 0) {
                            army.board.getPiece(i, 4 - i)!!.deleteSquares(FiveSquare.ENWS_CENTER_OBLIQUE)
                            i++
                        }
                    }
                    5 -> run {
                        var i = 1
                        while (5 - i >= 1) {
                            army.board.getPiece(i, 5 - i)!!.deleteSquares(FiveSquare.ENWS_RIGHT_FOUR_OBLIQUE)
                            i++
                        }
                    }
                    6 -> run {
                        var i = 2
                        while (6 - i >= 2) {
                            army.board.getPiece(i, 6 - i)!!.deleteSquares(FiveSquare.ENWS_RIGHT_THREE_OBLIQUE)
                            i++
                        }
                    }
                }
            }
        }
        army.board.removePiece(piece)
    }

}