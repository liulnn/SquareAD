package xyz.flove.square.core.roles;


/**
 * Created by liulnn on 17/4/20.
 */

import xyz.flove.square.core.Army;
import xyz.flove.square.core.Board;


public class People extends Army {
    public People(Board board, String color) {
        super(board, color, false);
    }
}
