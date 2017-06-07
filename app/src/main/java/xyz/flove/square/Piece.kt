package xyz.flove.square

import xyz.flove.square.core.Color
import xyz.flove.square.core.PieceStatus
import xyz.flove.square.core.Position
import xyz.flove.square.five.FiveSquare


class Piece(position: Position, color: Color) {
    var position: Position? = null
    var color: Color? = null
    var squares: List<FiveSquare>? = null
    var status: PieceStatus? = null

    init {
        this.position = position
        this.color = color
        this.status = PieceStatus.NULL
    }

    fun addSquares(square: FiveSquare) {
        val oldSquares: MutableList<FiveSquare>? = null
        if (null != squares) {
            for (s in squares!!) {
                oldSquares!!.add(s)
            }
        }
        oldSquares!!.add(square)
        squares = oldSquares.toList()
    }

    fun deleteSquares(square: FiveSquare) {
        val oldSquares: MutableList<FiveSquare>? = null
        for (s in squares!!) {
            oldSquares!!.add(s)
        }
        oldSquares!!.remove(square)
        squares = oldSquares.toList()
    }

    fun getSteps(): Int {
        var steps = 0
        if (null == squares || squares!!.isEmpty()) {
            return steps
        }
        for (square in squares!!) {
            if (square == FiveSquare.SORTH_LAT_LINE
                    || square == FiveSquare.CENTER_LAT_LINE
                    || square == FiveSquare.NORTH_LAT_LINE
                    || square == FiveSquare.WEST_LON_LINE
                    || square == FiveSquare.CENTER_LON_LINE
                    || square == FiveSquare.EAST_LON_LINE
                    || square == FiveSquare.WNES_CENTER_OBLIQUE
                    || square == FiveSquare.ENWS_CENTER_OBLIQUE) {
                steps += 2
            } else if (square == FiveSquare.WEST_NORTH_SQUARE
                    || square == FiveSquare.WEST_SORTH_SQUARE
                    || square == FiveSquare.EAST_NORTH_SQUARE
                    || square == FiveSquare.EAST_SORTH_SQUARE
                    || square == FiveSquare.WNES_LEFT_THREE_OBLIQUE
                    || square == FiveSquare.WNES_LEFT_FOUR_OBLIQUE
                    || square == FiveSquare.WNES_RIGHT_FOUR_OBLIQUE
                    || square == FiveSquare.WNES_RIGHT_THREE_OBLIQUE
                    || square == FiveSquare.ENWS_LEFT_THREE_OBLIQUE
                    || square == FiveSquare.ENWS_LEFT_FOUR_OBLIQUE
                    || square == FiveSquare.ENWS_RIGHT_FOUR_OBLIQUE
                    || square == FiveSquare.ENWS_RIGHT_THREE_OBLIQUE) {
                steps += 1
            }
        }
        return steps
    }
}
