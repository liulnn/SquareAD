package xyz.flove.square

import xyz.flove.square.core.Position
import xyz.flove.square.core.Color
import xyz.flove.square.core.Direction
import xyz.flove.square.core.People


class Ai(board: Board, color: Color) : Army(board, color, true) {

    val nextPosition: Position?
        get() {

            var maxScore = 0
            var position: Position? = null
            for (i in 0..board.length - 1) {
                for (j in 0..board.length - 1) {
                    if (board.getPiece(i, j) == null || board.getPiece(i, j)!!.color == Color.NULL) {
                        //判断己方得分
                        val self_board = board.clone()
                        val ai = xyz.flove.square.Ai(self_board, this.color)
                        val self_position = Position(i, j)
                        ai.board.addPiece(self_position, this.color)
                        Rule(ai, self_position).squares()
                        val steps = ai.board.getPiece(i, j)!!.getSteps()
                        if (maxScore <= steps) {
                            maxScore = steps
                            position = ai.board.getPiece(i, j)!!.position
                        }
                        //判断对方得分
                        val enemy_board = board.clone()
                        val enemy: Army
                        if (this.color == Color.BLACK) {
                            enemy = People(enemy_board, Color.WHITE) as Army
                        } else {
                            enemy = People(enemy_board, Color.BLACK) as Army
                        }
                        val enemy_position = Position(i, j)
                        enemy.board.addPiece(enemy_position, enemy.color)
                        Rule(enemy, enemy_position).squares()
                        val enemy_steps = enemy.board.getPiece(i, j)!!.getSteps()
                        if (maxScore <= enemy_steps) {
                            maxScore = enemy_steps
                            position = enemy.board.getPiece(i, j)!!.position
                        }
                    }
                }
            }
            return position
        }

    val removePosition: Position?
        get() {
            var maxScore = 0
            var position: Position? = null
            for (i in 0..board.length - 1) {
                for (j in 0..board.length - 1) {
                    if (board.getPiece(i, j) != null && board.getPiece(i, j)!!.color == color) {
                        val steps = board.getPiece(i, j)!!.getSteps()
                        if (maxScore <= steps) {
                            maxScore = steps
                            position = board.getPiece(i, j)!!.position
                        }
                    }
                }
            }
            return position
        }

    val checkedPosition: Position?
        get() {
            var position: Position? = null
            for (i in 0..board.length - 1) {
                for (j in 0..board.length - 1) {
                    val directions = board.getMoveDirection(Position(i, j))
                    if (directions.isNotEmpty()) {
                        when (directions[0]) {
                            Direction.WEST -> position = Position(i - 1, j)
                            Direction.EAST -> position = Position(i + 1, j)
                            Direction.NORTH -> position = Position(i, j - 1)
                            Direction.SORTH -> position = Position(i, j + 1)
                        }
                    }
                }
            }
            return position
        }

    val movePosition: Position?
        get() {
            var position: Position? = null
            val directions = board.getMoveDirection(lastChecked!!)
            if (directions.isNotEmpty()) {
                when (directions[0]) {
                    Direction.WEST -> position = Position(lastChecked!!.x - 1, lastChecked!!.y)
                    Direction.EAST -> position = Position(lastChecked!!.x + 1, lastChecked!!.y)
                    Direction.NORTH -> position = Position(lastChecked!!.x, lastChecked!!.y - 1)
                    Direction.SORTH -> position = Position(lastChecked!!.x, lastChecked!!.y + 1)
                }
            }
            return position
        }

    val eatPosition: Position?
        get() {
            val positions = board.getCanEatPieces(color)
            if (positions.isNotEmpty()) {
                return positions[0]
            }
            return null
        }
}