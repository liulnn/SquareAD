package xyz.flove.square


import xyz.flove.square.core.Color
import xyz.flove.square.core.PieceStatus
import xyz.flove.square.core.Position


abstract class Army(var board: Board, var color: Color, var isAi: Boolean) {
    var lastChecked: Position? = null

    /**
     * @param p 下子的位置
     * *
     * @return -1:此处不能下子, 0:切换队手, n:返回继续下子的个数
     */
    fun downPiece(p: Position): Int {
        val piece = board.getPiece(p)
        if (null != piece && piece.color != Color.NULL) {
            return -1
        }
        Rule(this, p).squares()
        return board.getPiece(p)!!.getSteps()
    }


    fun removePiece(p: Position): Boolean {
        if (board.getPiece(p) == null || board.getPiece(p)!!.color != color) {
            return false
        }
        Rule(this, p).clearSquares()
        return true
    }

    fun checkedPiece(p: Position): Boolean {
        val piece = board.getPiece(p)
        if (piece == null || piece.color != color) {
            return false
        }
        if (lastChecked != null) {
            board.getPiece(lastChecked!!)!!.status = PieceStatus.NULL
        }
        piece.status = PieceStatus.CHECKED
        lastChecked = p
        return true
    }

    fun movePiece(destPosition: Position): Int {
        if (lastChecked == null) {
            return -1
        }
        if (board.getPiece(destPosition) != null) {
            board.getPiece(lastChecked!!)!!.status = PieceStatus.NULL
            lastChecked = null
            return -1
        }
        if (Math.abs(destPosition.x - lastChecked!!.x) + Math.abs(destPosition.y - lastChecked!!.y) > 1) {
            board.getPiece(lastChecked!!)!!.status = PieceStatus.NULL
            lastChecked = null
            return -1
        }
        Rule(this, lastChecked!!).clearSquares()
        Rule(this, destPosition).squares()
        lastChecked = null
        return board.getPiece(destPosition)!!.getSteps()
    }

    fun eatPiece(position: Position): Boolean {
        val piece = board.getPiece(position)
        if (piece == null || piece.color == color || board.isSquare(position)) {
            return false
        }
        board.removePiece(piece)
        return true
    }
}