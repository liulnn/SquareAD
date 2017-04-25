package xyz.flove.square.entities;

/**
 * Created by liulnn on 17/4/19.
 */

public class Position {

    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }


    @Override
    public boolean equals(Object o) {
        if (null == o) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (o instanceof Position) {
            Position p = (Position) o;
            if (p.x == x && p.y == y) {
                return true;
            }
        }
        return false;
    }
}
