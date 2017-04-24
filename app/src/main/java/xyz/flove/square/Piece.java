package xyz.flove.square;


/**
 * Created by liulnn on 17/4/19.
 */

import java.util.ArrayList;

import xyz.flove.square.entities.Position;
import xyz.flove.square.entities.FiveSquare;

public class Piece {
    public Position position;
    public String color;
    public String[] squares;

    public Piece(Position position, String color) {
        this.position = position;
        this.color = color;
    }

    public void addSquares(String square) {
        ArrayList<String> oldSquares = new ArrayList<>();
        if (null != squares) {
            for (String s : squares) {
                oldSquares.add(s);
            }
        }
        oldSquares.add(square);
        squares = oldSquares.toArray(new String[oldSquares.size()]);
    }

    public void deleteSquares(String square) {
        ArrayList<String> oldSquares = new ArrayList<>();
        if (null != squares) {
            for (String s : squares) {
                oldSquares.add(s);
            }
        }
        oldSquares.remove(square);
        squares = oldSquares.toArray(new String[oldSquares.size()]);
    }

    public int getSteps() {
        int steps = 0;
        if (null == squares || squares.length == 0) {
            return steps;
        }
        for (String square : squares) {
            if (square.equals(FiveSquare.SORTH_LAT_LINE)
                    || square.equals(FiveSquare.CENTER_LAT_LINE)
                    || square.equals(FiveSquare.NORTH_LAT_LINE)
                    || square.equals(FiveSquare.WEST_LON_LINE)
                    || square.equals(FiveSquare.CENTER_LON_LINE)
                    || square.equals(FiveSquare.EAST_LON_LINE)
                    || square.equals(FiveSquare.WNES_CENTER_OBLIQUE)
                    || square.equals(FiveSquare.ENWS_CENTER_OBLIQUE)) {
                steps += 2;
            } else if (square.equals(FiveSquare.WEST_NORTH_SQUARE)
                    || square.equals(FiveSquare.WEST_SORTH_SQUARE)
                    || square.equals(FiveSquare.EAST_NORTH_SQUARE)
                    || square.equals(FiveSquare.EAST_SORTH_SQUARE)
                    || square.equals(FiveSquare.WNES_LEFT_THREE_OBLIQUE)
                    || square.equals(FiveSquare.WNES_LEFT_FOUR_OBLIQUE)
                    || square.equals(FiveSquare.WNES_RIGHT_FOUR_OBLIQUE)
                    || square.equals(FiveSquare.WNES_RIGHT_THREE_OBLIQUE)
                    || square.equals(FiveSquare.ENWS_LEFT_THREE_OBLIQUE)
                    || square.equals(FiveSquare.ENWS_LEFT_FOUR_OBLIQUE)
                    || square.equals(FiveSquare.ENWS_RIGHT_FOUR_OBLIQUE)
                    || square.equals(FiveSquare.ENWS_RIGHT_THREE_OBLIQUE)) {
                steps += 1;
            }
        }
        return steps;
    }
}
