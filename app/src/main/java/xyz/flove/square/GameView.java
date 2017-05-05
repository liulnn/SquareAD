package xyz.flove.square;

import xyz.flove.square.core.Army;
import xyz.flove.square.core.Board;
import xyz.flove.square.core.Piece;
import xyz.flove.square.core.entities.Color;
import xyz.flove.square.core.entities.Position;
import xyz.flove.square.core.roles.Ai;
import xyz.flove.square.core.roles.People;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class GameView extends View {

    public MainActivity mainActivity;

    public int mBoardLineUnit = 0;
    public int mPieceDiameter = 0;
    public int mStartX;// 棋盘定位的左上角X
    public int mStartY;// 棋盘定位的左上角Y
    // 棋子画笔
    public Paint mPiecePaint = new Paint();
    // 棋盘画笔
    public Paint mBoardPaint = new Paint();

    public GameStatus status;
    public int panelLength;
    public Board board;
    public Army mPlayer;
    public Army rivalPlayer;
    public Army currentPlayer;
    public int stepCount = 0;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mainActivity = (MainActivity) context;
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.panelLength = 5;

        initGame(Color.BLACK, Color.WHITE);
//        initGame(Color.BLACK, Color.WHITE, true);
    }

    public void initGame(String self, String enemy) {
        status = GameStatus.START;
        board = new Board(this.panelLength);
        this.mPlayer = new People(board, self);
        this.rivalPlayer = new Ai(board, enemy);
//        this.rivalPlayer = new People(board, enemy);
        this.currentPlayer = this.mPlayer;
    }

    public void initGame(String self, String enemy, boolean remove) {
        initGame(self, enemy);
        if (remove) {// 直接进入提子阶段
            for (int i = 0; i < this.panelLength; i++) {
                for (int j = 0; j < this.panelLength; j++) {
                    int xx = (int) (Math.random() * 2);
                    if (xx == 1) {
                        board.addPiece(new Position(i, j), Color.BLACK);
                    } else {
                        board.addPiece(new Position(i, j), Color.WHITE);
                    }
                }
            }
            board.status = Board.Status.REMOVE;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mBoardLineUnit = (w - 100) / 4;
        mPieceDiameter = mBoardLineUnit / 3;
        mStartX = 50;
        mStartY = 60;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(android.graphics.Color.YELLOW);
        drawBoard(canvas);
        drawPiece(canvas);
    }

    /**
     * 绘制棋盘背景
     */
    public void drawBoard(Canvas canvas) {
        // 水平线
        for (int i = 0; i < this.panelLength; i++) {
            canvas.drawLine(mStartX, mStartY + i * mBoardLineUnit, mStartX + 4
                    * mBoardLineUnit, mStartY + i * mBoardLineUnit, mBoardPaint);
        }
        // 垂直线
        for (int i = 0; i < this.panelLength; i++) {
            canvas.drawLine(mStartX + i * mBoardLineUnit, mStartY, mStartX + i
                    * mBoardLineUnit, mStartY + 4 * mBoardLineUnit, mBoardPaint);
        }
    }

    public void drawPiece(Canvas canvas) {
        board.draw();
        for (int x = 0; x < board.getLength(); x++) {
            for (int y = 0; y < board.getLength(); y++) {
                if (board.getPiece(x, y) == null) {
                    continue;
                }
                if (board.getPiece(x, y).color.equals(Color.WHITE)) {
                    mPiecePaint.setColor(android.graphics.Color.WHITE);
                } else if (board.getPiece(x, y).color.equals(Color.BLACK)) {
                    mPiecePaint.setColor(android.graphics.Color.BLACK);
                } else {
                    continue;
                }

                float centerX = mStartX + x * mBoardLineUnit;
                float centerY = mStartY + y * mBoardLineUnit;
                canvas.drawCircle(centerX, centerY, mPieceDiameter / 2,
                        mPiecePaint);
                float[] aroundRect = new float[]{centerX - mPieceDiameter,
                        centerY - mPieceDiameter, centerX + mPieceDiameter,
                        centerY - mPieceDiameter, centerX + mPieceDiameter,
                        centerY - mPieceDiameter, centerX + mPieceDiameter,
                        centerY + mPieceDiameter, centerX + mPieceDiameter,
                        centerY + mPieceDiameter, centerX - mPieceDiameter,
                        centerY + mPieceDiameter, centerX - mPieceDiameter,
                        centerY + mPieceDiameter, centerX - mPieceDiameter,
                        centerY - mPieceDiameter};
                if (board.getPiece(x, y).status == Piece.Status.CHECKED) {
                    mPiecePaint.setColor(android.graphics.Color.BLUE);
                    canvas.drawLines(aroundRect, mPiecePaint);
                }
            }
        }
    }

    synchronized public void changePlayer() {
        if (currentPlayer.equals(mPlayer)) {
            currentPlayer = rivalPlayer;
        } else {
            currentPlayer = mPlayer;
        }
        if (currentPlayer.isAi) {
            initPanelAndPen();
            Ai ai = (Ai) currentPlayer;
            Position p = null;
            if (board.status == Board.Status.DOWN) {
                p = ai.getNextPosition();
            } else if (board.status == Board.Status.REMOVE) {
                p = ai.getRemovePosition();
            } else if (board.status == Board.Status.FIGHT) {
                if (ai.lastChecked == null) {
                    p = ai.getCheckedPosition();
                } else {
                    p = ai.getMovePosition();
                }
            } else if (board.status == Board.Status.EAT) {
                p = ai.getEatPosition();
            }
            handleClick(p.x, p.y);
        }
        initPanelAndPen();
    }

    public void initPanelAndPen() {
        Drawable drawable;
        if (currentPlayer.color.equals(Color.BLACK)) {
            drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.black, null);
        } else {
            drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.white, null);
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        mainActivity.mPlayer.setCompoundDrawables(null, null, drawable, null);
        stepCount = 0;
        mainActivity.mStepCount.setText("0");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (status == GameStatus.END) {
            return true;
        }
        float touchX = event.getX();
        float touchy = event.getY();
        int x = (int) (touchX / mBoardLineUnit);
        int y = (int) (touchy / mBoardLineUnit);
        if (x < 0 | x > 4 | y < 0 | y > 4) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                boolean flag = handleClick(x, y);
                switch (board.getResult(currentPlayer.color)) {
                    case WINNER:
                        status = GameStatus.END;
                        Toast.makeText(this.getContext(), currentPlayer.color + " is Win!", Toast.LENGTH_SHORT).show();
                        break;
                    case LOSER:
                        status = GameStatus.END;
                        if (currentPlayer.equals(mPlayer)) {
                            Toast.makeText(this.getContext(), mPlayer.color + " is Win!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this.getContext(), rivalPlayer.color + " is Win!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                return flag;
        }
        return true;
    }

    private boolean handleClick(int x, int y) {
        board.draw();
        if (board.status == Board.Status.DOWN) {
            int steps = currentPlayer.downPiece(new Position(x, y));
            if (steps == -1) {
                return true;
            }
            stepCount += steps;
            this.invalidate();
            mainActivity.mStepCount.setText(String.valueOf(stepCount));
            if (stepCount > 0) {
                stepCount--;
                return true;
            }
            changePlayer();
        } else if (board.status == Board.Status.REMOVE) {
            boolean removeSuccess = currentPlayer.removePiece(new Position(x, y));
            if (!removeSuccess) {
                return true;
            }
            this.invalidate();
            changePlayer();
        } else if (board.status == Board.Status.FIGHT) {
            if (currentPlayer.lastChecked == null) {
                currentPlayer.checkedPiece(new Position(x, y));
                this.invalidate();
                return true;
            } else {
                int steps = currentPlayer.movePiece(new Position(x, y));
                if (steps == -1) {
                    this.invalidate();
                    return true;
                }
                stepCount += steps;
                if (stepCount > 0 && board.getCanEatPieces(currentPlayer.color).length > 0) {
                    mainActivity.mStepCount.setText(String.valueOf(stepCount));
                    board.status = Board.Status.EAT;
                    this.invalidate();
                    return true;
                } else {
                    this.invalidate();
                    changePlayer();
                }
            }
        } else if (board.status == Board.Status.EAT) {
            if (stepCount > 0) {
                boolean eatSuccess = currentPlayer.eatPiece(new Position(x, y));
                if (!eatSuccess) {
                    return true;
                }
                stepCount--;
                mainActivity.mStepCount.setText(String.valueOf(stepCount));
                this.invalidate();
                if (stepCount > 0) {
                    return true;
                }
            }
            board.status = Board.Status.FIGHT;
            changePlayer();
        }
        return true;
    }

    public enum GameStatus {
        // 开始，暂停，结束
        START, PRESS, END;

    }
}
