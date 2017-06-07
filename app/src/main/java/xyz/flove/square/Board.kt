package xyz.flove.square

import xyz.flove.square.core.*


class Board(val size: Int) : Cloneable {
    var status: BoardStatus? = null
    private var panel: Array<Array<Piece?>?>? = null
    private var removeCount: Int = 0

    init {
        panel = Array<Array<Piece?>?>(size, { null })
        status = BoardStatus.DOWN
        removeCount = 0
    }


    fun isSquare(position: Position): Boolean {
        val piece = getPiece(position)
        if (piece != null && piece.squares != null && piece.squares!!.isNotEmpty()) {
            return true
        }
        return false
    }

    fun getMoveDirection(position: Position): List<Direction>? {
        val piece = getPiece(position) ?: return null
        val directions: MutableList<Direction>? = null
        if (piece.position!!.x - 1 >= 0) {
            val westPiece = getPiece(position.x - 1, position.y)
            if (westPiece == null) {
                directions!!.add(Direction.WEST)
            }
        }
        if (piece.position!!.y - 1 >= 0) {
            val northPiece = getPiece(position.x, position.y - 1)
            if (northPiece == null) {
                directions!!.add(Direction.NORTH)
            }
        }
        if (piece.position!!.x + 1 <= 4) {
            val eastPiece = getPiece(position.x + 1, position.y)
            if (eastPiece == null) {
                directions!!.add(Direction.EAST)
            }
        }
        if (piece.position!!.y + 1 <= 4) {
            val sorthPiece = getPiece(position.x, position.y + 1)
            if (sorthPiece == null) {
                directions!!.add(Direction.SORTH)
            }
        }
        return directions!!.toList()
    }


    val length: Int
        get() = this.size

    fun getPiece(position: Position): Piece? {
        if (panel != null && panel!![position.x] != null) {
            return panel!![position.x]!![position.y]
        }
        return null
    }

    fun getPiece(x: Int, y: Int): Piece? {
        if (panel != null && panel!![x] != null) {
            return panel!![x]!![y]
        }
        return null
    }

    fun addPiece(position: Position, color: Color) {
        addPiece(Piece(position, color))
    }

    fun addPiece(piece: Piece): Boolean {
        if (panel!![piece.position!!.x]!![piece.position!!.y] != null) {
            return false
        }
        panel!![piece.position!!.x]!![piece.position!!.y] = piece
        updateStatus()
        return true
    }

    fun removePiece(piece: Piece): Boolean {
        if (panel!![piece.position!!.x]!![piece.position!!.y] == null
                || panel!![piece.position!!.x]!![piece.position!!.y]!!.color != piece.color) {
            return false
        }
        panel!![piece.position!!.x]!![piece.position!!.y] = null
        removeCount += 1
        if (removeCount >= 2 && status == BoardStatus.REMOVE) {
            status = BoardStatus.FIGHT
        }
        return true
    }

    private fun updateStatus() {
        if (status == BoardStatus.DOWN) {
            var isFull = true
            for (i in 0..length - 1) {
                for (j in 0..length - 1) {
                    if (panel!![i]!![j] == null || panel!![i]!![j]!!.color == Color.NULL) {
                        isFull = false
                    }
                }
            }
            if (isFull) {
                status = BoardStatus.REMOVE
            }
        }
    }

    fun getCanEatPieces(color: Color): List<Position> {
        val positions: MutableList<Position>? = null
        for (i in 0..length - 1) {
            for (j in 0..length - 1) {
                if (panel!![i]!![j] != null && panel!![i]!![j]!!.color != color && !isSquare(Position(i, j))) {
                    positions!!.add(panel!![i]!![j]!!.position!!)
                }
            }
        }
        return positions!!.toList()
    }

    fun isLazy(color: Color): Boolean {
        return false
    }

    fun getResult(color: Color): Result {
        if (status == BoardStatus.DOWN || status == BoardStatus.REMOVE) {
            return Result.NULL
        }
        var enemyPieceCount = 0
        var enemyCanMovePieceCount = 0
        for (i in 0..length - 1) {
            for (j in 0..length - 1) {
                val piece = panel!![i]!![j]
                val directions = getMoveDirection(Position(i, j))
                if (piece != null && piece.color != color) {
                    enemyPieceCount += 1
                    if (directions != null && directions.isNotEmpty()) {
                        enemyCanMovePieceCount += 1
                    }
                }
            }
        }
        //对方棋子少于3个
        if (enemyPieceCount < 3) {
            return Result.WINNER
        }
        //对方没有可以移动的棋子
        if (enemyCanMovePieceCount == 0) {
            return Result.WINNER
        }

        //己方是否是懒棋
        if (isLazy(color)) {
            return Result.LOSER
        }
        return Result.NULL
    }

    fun draw() {
        println("--------------------------------")
        for (i in 0..length - 1) {
            for (j in 0..length - 1) {
                var color = Color.NULL
                if (panel != null && panel!![j] != null && panel!![j]!![i] != null) {
                    color = panel!![j]!![i]!!.color!!
                }
                if (color == Color.BLACK) {
                    print("[*]")
                } else if (color == Color.WHITE) {
                    print("[#]")
                } else {
                    print("[ ]")
                }
            }
            println("")
        }
        println("--------------------------------")
        for (i in 0..length - 1) {
            for (j in 0..length - 1) {
                if (this.isSquare(Position(j, i))) {
                    print("[+]")
                } else {
                    print("[ ]")
                }
            }
            println("")
        }
    }

    public override fun clone(): xyz.flove.square.Board {
        var newBoard: xyz.flove.square.Board = this
        try {
            newBoard = super.clone() as xyz.flove.square.Board
            val pieces = Array<Array<Piece?>?>(length, { null })
            for (i in 0..length - 1) {
                for (j in 0..length - 1) {
                    if (panel!![i]!![j] != null) {
                        pieces[i]!![j] = Piece(Position(i, j), panel!![i]!![j]!!.color!!)
                    }
                }
            }
            newBoard.panel = pieces
            newBoard.status = BoardStatus.valueOf(status!!.name)
        } catch (e: CloneNotSupportedException) {
        } finally {
            return newBoard
        }
    }
}