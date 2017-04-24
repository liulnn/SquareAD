package xyz.flove.square.roles;


/**
 * Created by liulnn on 17/4/20.
 */

import xyz.flove.square.Army;
import xyz.flove.square.Board;


public class People extends Army {
    public People(Board board, String color) {
        super(board, color, false);
    }
}
