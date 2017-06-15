package xyz.flove.square

import xyz.flove.square.core.*


class Board(var panel: Array<Array<Piece?>>) : Cloneable {
    var size: Int = panel.size
    var status: BoardStatus? = null
    private var removeCount: Int = 0

    init {
        status = BoardStatus.DOWN
        removeCount = 0
    }


    fun isSquare(piece: Piece): Boolean {
        if (piece.squares.isNotEmpty()) {
            return true
        }
        return false
    }

    fun isSquare(position: Position): Boolean {
        val piece = getPiece(position)
        if (piece != null) {
            return isSquare(piece)
        }
        return false
    }

    fun getMoveDirection(position: Position): List<Direction> {
        val directions: MutableList<Direction> = ArrayList()
        val piece = getPiece(position) ?: return directions
        if (piece.position.x - 1 >= 0) {
            val westPiece = getPiece(position.x - 1, position.y)
            if (westPiece == null) {
                directions.add(Direction.WEST)
            }
        }
        if (piece.position.y - 1 >= 0) {
            val northPiece = getPiece(position.x, position.y - 1)
            if (northPiece == null) {
                directions.add(Direction.NORTH)
            }
        }
        if (piece.position.x + 1 <= 4) {
            val eastPiece = getPiece(position.x + 1, position.y)
            if (eastPiece == null) {
                directions.add(Direction.EAST)
            }
        }
        if (piece.position.y + 1 <= 4) {
            val sorthPiece = getPiece(position.x, position.y + 1)
            if (sorthPiece == null) {
                directions.add(Direction.SORTH)
            }
        }
        return directions
    }


    val length: Int
        get() = this.size

    fun getPiece(position: Position): Piece? {
        return panel[position.x][position.y]
    }

    fun getPiece(x: Int, y: Int): Piece? {
        return panel[x][y]
    }

    fun addPiece(position: Position, color: Color): Boolean {
        var piece: Piece = Piece(position, color)
        return addPiece(piece)
    }

    fun addPiece(piece: Piece): Boolean {
        if (panel[piece.position.x][piece.position.y] != null) {
            return false
        }
        panel[piece.position.x][piece.position.y] = piece
        updateStatus()
        return true
    }

    fun removePiece(piece: Piece): Boolean {
        if (panel[piece.position.x][piece.position.y] == null
                || panel[piece.position.x][piece.position.y]!!.color != piece.color) {
            return false
        }
        panel[piece.position.x][piece.position.y] = null
        removeCount += 1
        if (removeCount >= 2 && status == BoardStatus.REMOVE) {
            status = BoardStatus.FIGHT
        }
        return true
    }

    private fun updateStatus() {
        if (status == BoardStatus.DOWN) {
            if (isFull()) {
                status = BoardStatus.REMOVE
            }
        }
    }


    fun isFull(): Boolean {
        for (i in 0..length - 1) {
            panel[i].asList().forEach {
                if (it == null || it.color == Color.NULL) return false
            }
        }
        return true
    }


    fun getCanEatPieces(color: Color): List<Position> {
        val positions: MutableList<Position> = ArrayList()
        for (i in 0..length - 1) {
            panel[i].forEach {
                if (it != null && it.color != color && !isSquare(it))
                    positions.add(it.position)
            }
        }
        return positions
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
                val piece = panel[i][j]
                val directions = getMoveDirection(Position(i, j))
                if (piece != null && piece.color != color) {
                    enemyPieceCount += 1
                    if (directions.isNotEmpty()) {
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
                if (panel[j][i] != null) {
                    color = panel[j][i]!!.color
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

    public override fun clone(): Board {
        var newBoard: Board = this
        val pieces: Array<Array<Piece?>> = Array(length) { Array<Piece?>(length, { null }) }
        try {
            newBoard = super.clone() as Board
            for (i in 0..length - 1) {
                for (j in 0..length - 1) {
                    if (panel[i][j] != null) {
                        pieces[i][j] = Piece(Position(i, j), panel[i][j]!!.color)
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