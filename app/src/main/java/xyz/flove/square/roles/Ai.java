package xyz.flove.square.roles;


/**
 * Created by liulnn on 17/4/20.
 */

import xyz.flove.square.Army;
import xyz.flove.square.Board;
import xyz.flove.square.Piece;
import xyz.flove.square.Rule;
import xyz.flove.square.entities.Position;
import xyz.flove.square.entities.Color;


public class Ai extends Army {

    public Ai(Board board, String color) {
        super(board, color, true);
    }

    public Position getNextPosition() {

        int maxScore = 0;
        Position position = null;
        for (int i = 0; i < board.panel[0].length; i++) {
            for (int j = 0; j < board.panel[0].length; j++) {
                if (board.panel[i][j] == null || board.panel[i][j].color.equals(Color.NULL)) {
                    //判断己方得分
                    Board self_board = board.clone();
                    Army ai = new Ai(self_board, this.color);
                    Position self_position = new Position(i, j);
                    ai.board.panel[i][j] = new Piece(self_position, this.color);
                    new Rule(ai, self_position).squares();
                    int steps = ai.board.panel[i][j].getSteps();
                    if (maxScore <= steps) {
                        maxScore = steps;
                        position = ai.board.panel[i][j].position;
                    }
                    //判断对方得分
                    Board enemy_board = board.clone();
                    Army enemy;
                    if (this.color.equals(Color.BLACK)) {
                        enemy = new People(enemy_board, Color.WHITE);
                    } else {
                        enemy = new People(enemy_board, Color.BLACK);
                    }
                    Position enemy_position = new Position(i, j);
                    enemy.board.panel[i][j] = new Piece(enemy_position, enemy.color);
                    new Rule(enemy, enemy_position).squares();
                    int enemy_steps = enemy.board.panel[i][j].getSteps();
                    if (maxScore <= enemy_steps) {
                        maxScore = enemy_steps;
                        position = enemy.board.panel[i][j].position;
                    }
                }
            }
        }
        return position;
    }
}
