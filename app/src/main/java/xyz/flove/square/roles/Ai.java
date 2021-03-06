package xyz.flove.square.roles;


/**
 * Created by liulnn on 17/4/20.
 */

import xyz.flove.square.Army;
import xyz.flove.square.Board;
import xyz.flove.square.Rule;
import xyz.flove.square.entities.Position;
import xyz.flove.square.entities.Color;
import xyz.flove.square.enums.Direction;


public class Ai extends Army {

    public Ai(Board board, String color) {
        super(board, color, true);
    }

    public Position getNextPosition() {

        int maxScore = 0;
        Position position = null;
        for (int i = 0; i < board.getLength(); i++) {
            for (int j = 0; j < board.getLength(); j++) {
                if (board.getPiece(i, j) == null || board.getPiece(i, j).color.equals(Color.NULL)) {
                    //判断己方得分
                    Board self_board = board.clone();
                    Army ai = new Ai(self_board, this.color);
                    Position self_position = new Position(i, j);
                    ai.board.addPiece(self_position, this.color);
                    new Rule(ai, self_position).squares();
                    int steps = ai.board.getPiece(i, j).getSteps();
                    if (maxScore <= steps) {
                        maxScore = steps;
                        position = ai.board.getPiece(i, j).position;
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
                    enemy.board.addPiece(enemy_position, enemy.color);
                    new Rule(enemy, enemy_position).squares();
                    int enemy_steps = enemy.board.getPiece(i, j).getSteps();
                    if (maxScore <= enemy_steps) {
                        maxScore = enemy_steps;
                        position = enemy.board.getPiece(i, j).position;
                    }
                }
            }
        }
        return position;
    }

    public Position getRemovePosition() {
        int maxScore = 0;
        Position position = null;
        for (int i = 0; i < board.getLength(); i++) {
            for (int j = 0; j < board.getLength(); j++) {
                if (board.getPiece(i, j) != null && board.getPiece(i, j).color.equals(color)) {
                    int steps = board.getPiece(i, j).getSteps();
                    if (maxScore <= steps) {
                        maxScore = steps;
                        position = board.getPiece(i, j).position;
                    }
                }
            }
        }
        return position;
    }

    public Position getCheckedPosition() {
        Position position = null;
        for (int i = 0; i < board.getLength(); i++) {
            for (int j = 0; j < board.getLength(); j++) {
                Direction[] directions = board.getMoveDirection(new Position(i, j));
                if (directions != null && directions.length > 0) {
                    switch (directions[0]) {
                        case WEST:
                            position = new Position(i - 1, j);
                            break;
                        case EAST:
                            position = new Position(i + 1, j);
                            break;
                        case NORTH:
                            position = new Position(i, j - 1);
                            break;
                        case SORTH:
                            position = new Position(i, j + 1);
                            break;
                    }
                }
            }
        }
        return position;
    }

    public Position getMovePosition() {
        Position position = null;
        Direction[] directions = board.getMoveDirection(lastChecked);
        switch (directions[0]) {
            case WEST:
                position = new Position(lastChecked.x - 1, lastChecked.y);
                break;
            case EAST:
                position = new Position(lastChecked.x + 1, lastChecked.y);
                break;
            case NORTH:
                position = new Position(lastChecked.x, lastChecked.y - 1);
                break;
            case SORTH:
                position = new Position(lastChecked.x, lastChecked.y + 1);
                break;
        }
        return position;
    }

    public Position getEatPosition() {
        Position[] positions = board.getCanEatPieces(color);
        return positions[0];
    }
}
