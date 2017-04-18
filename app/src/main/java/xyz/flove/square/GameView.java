package xyz.flove.square;

import java.util.ArrayList;
import java.util.List;

import xyz.flove.square.entities.Army;
import xyz.flove.square.entities.Board;
import xyz.flove.square.entities.Piece;
import xyz.flove.square.enums.BoardStatus;
import xyz.flove.square.enums.Color;
import xyz.flove.square.enums.Direction;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

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
	public Board board;
	public Army player;
	public Piece beforePiece;
	public int removeCount = 0;
	public int stepCount = 0;

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mainActivity = (MainActivity) context;
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);

		initGame(Color.BLACK);
		// initGame(Color.BLACK, true);
	}

	public void initGame(Color player) {
		status = GameStatus.START;
		board = new Board();
		this.player = new Army(board, player);
	}

	public void initGame(Color player, boolean remove) {
		initGame(player);
		if (remove) {// 直接进入提子阶段
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					int xx = (int) (Math.random() * 2);
					if (xx == 1) {
						board.panel[i][j].color = Color.BLACK;
					} else {
						board.panel[i][j].color = Color.WHITE;
					}

				}
			}
			board.status = BoardStatus.REMOVE;
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
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				board.panel[i][j].status = 0;
			}
		}
	}

	/**
	 * 绘制棋盘背景
	 */
	public void drawBoard(Canvas canvas) {
		// 水平线
		for (int i = 0; i < 5; i++) {
			canvas.drawLine(mStartX, mStartY + i * mBoardLineUnit, mStartX + 4
					* mBoardLineUnit, mStartY + i * mBoardLineUnit, mBoardPaint);
		}
		// 垂直线
		for (int i = 0; i < 5; i++) {
			canvas.drawLine(mStartX + i * mBoardLineUnit, mStartY, mStartX + i
					* mBoardLineUnit, mStartY + 4 * mBoardLineUnit, mBoardPaint);
		}
	}

	public void drawPiece(Canvas canvas) {
		board.draw();
		for (int x = 0; x < board.panel[0].length; x++) {
			for (int y = 0; y < board.panel[0].length; y++) {
				if (board.panel[x][y].color == Color.WHITE) {
					mPiecePaint.setColor(android.graphics.Color.WHITE);
				} else if (board.panel[x][y].color == Color.BLACK) {
					mPiecePaint.setColor(android.graphics.Color.BLACK);
				} else {
					continue;
				}

				float centerX = mStartX + x * mBoardLineUnit;
				float centerY = mStartY + y * mBoardLineUnit;
				canvas.drawCircle(centerX, centerY, mPieceDiameter / 2,
						mPiecePaint);
				float[] aroundRect = new float[] { centerX - mPieceDiameter,
						centerY - mPieceDiameter, centerX + mPieceDiameter,
						centerY - mPieceDiameter, centerX + mPieceDiameter,
						centerY - mPieceDiameter, centerX + mPieceDiameter,
						centerY + mPieceDiameter, centerX + mPieceDiameter,
						centerY + mPieceDiameter, centerX - mPieceDiameter,
						centerY + mPieceDiameter, centerX - mPieceDiameter,
						centerY + mPieceDiameter, centerX - mPieceDiameter,
						centerY - mPieceDiameter };
				if (board.panel[x][y].status == 1) {
					mPiecePaint.setColor(android.graphics.Color.BLUE);
					canvas.drawLines(aroundRect, mPiecePaint);
				} else if (board.panel[x][y].status == 2) {
					mPiecePaint.setColor(android.graphics.Color.RED);
					canvas.drawLines(aroundRect, mPiecePaint);
				}
			}
		}
	}

	public void removePiece(int x, int y) {
		player.removePiece(x, y);
		this.invalidate();
		removeCount += 1;
		if (removeCount >= 2) {
			board.status = BoardStatus.FIGHT;
		}
	}

	public int movePiece(int x, int y, Direction direction) {
		int success = player.movePiece(x, y, direction);
		this.invalidate();
		return success;
	}

	public void eatPiece(int x, int y) {
		player.eatPiece(x, y);
		this.invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float touchX = event.getX();
		float touchy = event.getY();
		int x = (int) (touchX / mBoardLineUnit);
		int y = (int) (touchy / mBoardLineUnit);
		if (x < 0 | x > 4 | y < 0 | y > 4) {
			return true;
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (board.status == BoardStatus.DOWN) {
				if (board.panel[x][y].color == Color.NULL) {
					stepCount += player.downPiece(x, y);
					board.panel[x][y].status = 1;
					this.invalidate();
					if (board.getNullPieces().size() <= 0) {
						board.status = BoardStatus.REMOVE;
					}
					mainActivity.mStepCount.setText(stepCount + "");
					if (stepCount > 0) {
						stepCount--;
						return true;
					}
					changePlayer();
				}
			} else if (board.status == BoardStatus.REMOVE) {
				if (board.panel[x][y].color == player.color) {
					removePiece(x, y);
				}
				changePlayer();
			} else if (board.status == BoardStatus.FIGHT) {
				return fightProgress(event, x, y);
			} else if (board.status == BoardStatus.EAT) {
				if (stepCount > 0) {
					if (board.panel[x][y].color != player.color
							&& board.panel[x][y].color != null) {
						if (!board.panel[x][y].isSuccess()) {
							eatPiece(x, y);
							stepCount -= 1;
							mainActivity.mStepCount.setText(stepCount + "");
							if (stepCount > 0) {
								return true;
							}
						}else {
							return true;
						}
					} else {
						return true;
					}
				}
				board.status = BoardStatus.FIGHT;
				changePlayer();
			}
		}

		return true;
	}

	public List<Piece> getCanEatPieces() {
		List<Piece> pieces = new ArrayList<Piece>();
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (board.panel[i][j].color != player.color
						&& board.panel[i][j].color != null) {
					if (board.panel[i][j].isSuccess()) {
						pieces.add(board.panel[i][j]);
					}
				}

			}
		}

		return pieces;
	}

	public boolean fightProgress(MotionEvent event, int x, int y) {
		if (null == beforePiece) {
			if (board.panel[x][y].color == player.color) {
				beforePiece = board.panel[x][y];
				board.panel[x][y].status = 1;
				this.invalidate();
				return true;
			}
		} else {
			double length = Math.sqrt((beforePiece.x - x) * (beforePiece.x - x)
					+ (beforePiece.y - y) * (beforePiece.y - y));
			if (board.panel[x][y].color == Color.NULL && length == 1.0) {
				board.panel[x][y].status = 2;
				board.panel[beforePiece.x][beforePiece.y].status = 0;
				if (beforePiece.x - x == 0 && beforePiece.y - y == 1) {
					stepCount += movePiece(beforePiece.x, beforePiece.y,
							Direction.NORTH);
				} else if (beforePiece.x - x == 0 && beforePiece.y - y == -1) {
					stepCount += movePiece(beforePiece.x, beforePiece.y,
							Direction.SORTH);
				} else if (beforePiece.x - x == 1 && beforePiece.y - y == 0) {
					stepCount += movePiece(beforePiece.x, beforePiece.y,
							Direction.WEST);
				} else if (beforePiece.x - x == -1 && beforePiece.y - y == 0) {
					stepCount += movePiece(beforePiece.x, beforePiece.y,
							Direction.EAST);
				}
				if (stepCount > 0 && getCanEatPieces().size() > 0) {
					mainActivity.mStepCount.setText(stepCount + "");
					board.status = BoardStatus.EAT;
					this.invalidate();
					return true;
				} else {
					if (null != beforePiece) {
						board.panel[beforePiece.x][beforePiece.y].status = 0;
						beforePiece = null;
					}
					changePlayer();
				}
			} else {
				beforePiece = null;
				board.panel[x][y].status = 0;
				this.invalidate();
				return true;
			}
		}
		return true;
	}

	public void changePlayer() {
		Drawable drawable;
		if (player.color == Color.BLACK) {
			player.color = Color.WHITE;
			drawable = getResources().getDrawable(R.drawable.white);
		} else {
			player.color = Color.BLACK;
			drawable = getResources().getDrawable(R.drawable.black);
		}

		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		mainActivity.mPlayer.setCompoundDrawables(null, null, drawable, null);
		stepCount = 0;
		mainActivity.mStepCount.setText("0");
	}

	public enum GameStatus {
		// 开始，暂停，结束
		START, PRESS, END;

	}
}
