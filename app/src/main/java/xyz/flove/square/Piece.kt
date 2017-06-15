package xyz.flove.square

import xyz.flove.square.core.Color
import xyz.flove.square.core.PieceStatus
import xyz.flove.square.core.Position
import xyz.flove.square.core.FiveSquare


class Piece(var position: Position, var color: Color) {
    var squares: List<FiveSquare> = ArrayList()
    var status: PieceStatus = PieceStatus.NULL

    fun addSquares(square: FiveSquare) {
        val oldSquares: MutableList<FiveSquare> = ArrayList()
        squares.forEach {
            oldSquares.add(it)
        }
        oldSquares.add(square)
        squares = oldSquares
    }

    fun deleteSquares(square: FiveSquare) {
        val oldSquares: MutableList<FiveSquare> = ArrayList()
        squares.forEach {
            oldSquares.add(it)
        }
        oldSquares.remove(square)
        squares = oldSquares
    }

    fun getSteps(): Int {
        var steps = 0
        if (squares.isEmpty()) {
            return steps
        }
        for (square in squares) {
            when (square) {
                FiveSquare.SORTH_LAT_LINE,
                FiveSquare.CENTER_LAT_LINE,
                FiveSquare.NORTH_LAT_LINE,
                FiveSquare.WEST_LON_LINE,
                FiveSquare.CENTER_LON_LINE,
                FiveSquare.EAST_LON_LINE,
                FiveSquare.WNES_CENTER_OBLIQUE,
                FiveSquare.ENWS_CENTER_OBLIQUE
                ->
                    steps += 2
                FiveSquare.WEST_NORTH_SQUARE,
                FiveSquare.WEST_SORTH_SQUARE,
                FiveSquare.EAST_NORTH_SQUARE,
                FiveSquare.EAST_SORTH_SQUARE,
                FiveSquare.WNES_LEFT_THREE_OBLIQUE,
                FiveSquare.WNES_LEFT_FOUR_OBLIQUE,
                FiveSquare.WNES_RIGHT_FOUR_OBLIQUE,
                FiveSquare.WNES_RIGHT_THREE_OBLIQUE,
                FiveSquare.ENWS_LEFT_THREE_OBLIQUE,
                FiveSquare.ENWS_LEFT_FOUR_OBLIQUE,
                FiveSquare.ENWS_RIGHT_FOUR_OBLIQUE,
                FiveSquare.ENWS_RIGHT_THREE_OBLIQUE
                ->
                    steps += 1
            }
        }
        return steps
    }
}
