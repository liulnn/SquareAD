package xyz.flove.square

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import android.util.Log
import xyz.flove.square.core.*

class GameView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    var mainActivity: MainActivity? = null
    var mBoardLineUnit = 0
    var mPieceDiameter = 0
    var mStartX: Int = 0// 棋盘定位的左上角X
    var mStartY: Int = 0// 棋盘定位的左上角Y
    // 棋子画笔
    var mPiecePaint = Paint()
    // 棋盘画笔
    var mBoardPaint = Paint()

    var status: GameStatus? = null
    var panelLength: Int = 0
    var board: Board? = null
    var mPlayer: Army? = null
    var rivalPlayer: Army? = null
    var currentPlayer: Army? = null
    var stepCount = 0

    init {
        this.mainActivity = context as MainActivity
        this.isFocusable = true
        this.isFocusableInTouchMode = true
        this.panelLength = 5
        Log.d("A", "init")

        initGame(Color.BLACK, Color.WHITE)
        Log.d("A", "initGame")
    }

    fun initGame(self: Color, enemy: Color) {
        status = GameStatus.START
        board = Board(this.panelLength)
        this.mPlayer = People(board!!, self) as Army
        this.rivalPlayer = Ai(board!!, enemy)
        //        this.rivalPlayer = new People(board, enemy);
        this.currentPlayer = this.mPlayer
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mBoardLineUnit = (w - 100) / 4
        mPieceDiameter = mBoardLineUnit / 3
        mStartX = 50
        mStartY = 60
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(android.graphics.Color.YELLOW)
        drawBoard(canvas)
        drawPiece(canvas)
    }

    /**
     * 绘制棋盘背景
     */
    fun drawBoard(canvas: Canvas) {
        // 水平线
        for (i in 0..this.panelLength - 1) {
            canvas.drawLine(mStartX.toFloat(), (mStartY + i * mBoardLineUnit).toFloat(), (mStartX + 4 * mBoardLineUnit).toFloat(), (mStartY + i * mBoardLineUnit).toFloat(), mBoardPaint)
        }
        // 垂直线
        for (i in 0..this.panelLength - 1) {
            canvas.drawLine((mStartX + i * mBoardLineUnit).toFloat(), mStartY.toFloat(), (mStartX + i * mBoardLineUnit).toFloat(), (mStartY + 4 * mBoardLineUnit).toFloat(), mBoardPaint)
        }
    }

    fun drawPiece(canvas: Canvas) {
        board!!.draw()
        for (x in 0..board!!.length - 1) {
            for (y in 0..board!!.length - 1) {
                if (board!!.getPiece(x, y) == null) {
                    continue
                }
                if (board!!.getPiece(x, y)!!.color == Color.WHITE) {
                    mPiecePaint.color = android.graphics.Color.WHITE
                } else if (board!!.getPiece(x, y)!!.color == Color.BLACK) {
                    mPiecePaint.color = android.graphics.Color.BLACK
                } else {
                    continue
                }

                val centerX = (mStartX + x * mBoardLineUnit).toFloat()
                val centerY = (mStartY + y * mBoardLineUnit).toFloat()
                canvas.drawCircle(centerX, centerY, (mPieceDiameter / 2).toFloat(),
                        mPiecePaint)
                val aroundRect = floatArrayOf(centerX - mPieceDiameter, centerY - mPieceDiameter, centerX + mPieceDiameter, centerY - mPieceDiameter, centerX + mPieceDiameter, centerY - mPieceDiameter, centerX + mPieceDiameter, centerY + mPieceDiameter, centerX + mPieceDiameter, centerY + mPieceDiameter, centerX - mPieceDiameter, centerY + mPieceDiameter, centerX - mPieceDiameter, centerY + mPieceDiameter, centerX - mPieceDiameter, centerY - mPieceDiameter)
                if (board!!.getPiece(x, y)!!.status === PieceStatus.CHECKED) {
                    mPiecePaint.color = android.graphics.Color.BLUE
                    canvas.drawLines(aroundRect, mPiecePaint)
                }
            }
        }
    }

    @Synchronized fun changePlayer() {
        if (currentPlayer == mPlayer) {
            currentPlayer = rivalPlayer
        } else {
            currentPlayer = mPlayer
        }
        if (currentPlayer!!.isAi) {
            initPanelAndPen()
            val ai = currentPlayer as Ai
            var p: Position? = null
            if (board!!.status === BoardStatus.DOWN) {
                p = ai.nextPosition
            } else if (board!!.status === BoardStatus.REMOVE) {
                p = ai.removePosition
            } else if (board!!.status === BoardStatus.FIGHT) {
                if (ai.lastChecked == null) {
                    p = ai.checkedPosition
                } else {
                    p = ai.movePosition
                }
            } else if (board!!.status === BoardStatus.EAT) {
                p = ai.eatPosition
            }
            handleClick(p!!)
        }
        initPanelAndPen()
    }

    fun initPanelAndPen() {
        val drawable: Drawable
        if (currentPlayer!!.color == Color.BLACK) {
            drawable = ResourcesCompat.getDrawable(resources, R.drawable.black, null)!!
        } else {
            drawable = ResourcesCompat.getDrawable(resources, R.drawable.white, null)!!
        }
        drawable.setBounds(0, 0, drawable.minimumWidth,
                drawable.minimumHeight)
        mainActivity!!.mPlayer!!.setCompoundDrawables(null, null, drawable, null)
        stepCount = 0
        mainActivity!!.mStepCount!!.text = "0"
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (status == GameStatus.END) {
            return true
        }
        val touchX = event.x
        val touchy = event.y
        val x = (touchX / mBoardLineUnit).toInt()
        val y = (touchy / mBoardLineUnit).toInt()
        if ((x < 0) or (x > 4) or (y < 0) or (y > 4)) {
            return true
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val flag = handleClick(Position(x, y))
                when (board!!.getResult(currentPlayer!!.color)) {
                    Result.WINNER -> {
                        status = GameStatus.END
                        Toast.makeText(this.context, currentPlayer!!.color.toString().plus(" is Win!"), Toast.LENGTH_SHORT).show()
                    }
                    Result.LOSER -> {
                        status = GameStatus.END
                        if (currentPlayer == mPlayer) {
                            Toast.makeText(this.context, mPlayer!!.color.toString().plus(" is Win!"), Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this.context, rivalPlayer!!.color.toString().plus(" is Win!"), Toast.LENGTH_SHORT).show()
                        }
                    }
                    Result.NULL -> {

                    }
                }
                return flag
            }
        }
        return true
    }

    private fun handleClick(position: Position): Boolean {
        board!!.draw()
        if (board!!.status === BoardStatus.DOWN) {
            val steps = currentPlayer!!.downPiece(position)
            if (steps == -1) {
                return true
            }
            stepCount += steps
            this.invalidate()
            mainActivity!!.mStepCount!!.text = stepCount.toString()
            if (stepCount > 0) {
                stepCount--
                return true
            }
            changePlayer()
        } else if (board!!.status === BoardStatus.REMOVE) {
            val removeSuccess = currentPlayer!!.removePiece(position)
            if (!removeSuccess) {
                return true
            }
            this.invalidate()
            changePlayer()
        } else if (board!!.status === BoardStatus.FIGHT) {
            if (currentPlayer!!.lastChecked == null) {
                currentPlayer!!.checkedPiece(position)
                this.invalidate()
                return true
            } else {
                val steps = currentPlayer!!.movePiece(position)
                if (steps == -1) {
                    this.invalidate()
                    return true
                }
                stepCount += steps
                if (stepCount > 0 && board!!.getCanEatPieces(currentPlayer!!.color).isNotEmpty()) {
                    mainActivity!!.mStepCount!!.text = stepCount.toString()
                    board!!.status = BoardStatus.EAT
                    this.invalidate()
                    return true
                } else {
                    this.invalidate()
                    changePlayer()
                }
            }
        } else if (board!!.status === BoardStatus.EAT) {
            if (stepCount > 0) {
                val eatSuccess = currentPlayer!!.eatPiece(position)
                if (!eatSuccess) {
                    return true
                }
                stepCount--
                mainActivity!!.mStepCount!!.text = stepCount.toString()
                this.invalidate()
                if (stepCount > 0) {
                    return true
                }
            }
            board!!.status = BoardStatus.FIGHT
            changePlayer()
        }
        return true
    }
}