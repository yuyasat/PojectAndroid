package yuyasat.pojectandroid.operation;

import android.graphics.drawable.ColorDrawable;
import android.widget.LinearLayout;

import java.util.HashMap;

import static android.R.transition.move;
import static yuyasat.pojectandroid.MainActivity.NONE;
import static yuyasat.pojectandroid.MainActivity.ROW;
import static yuyasat.pojectandroid.MainActivity.FIELD_NONE;

import yuyasat.pojectandroid.MainActivity;
import yuyasat.pojectandroid.entity.TopState;
/**
 * Created by yuyataki on 2017/07/15.
 */

public class KeyOperation {
    public TopState topState;

    public KeyOperation(TopState topState) {
        this.topState = topState;
    }

    public boolean isValidMove(String move) {
        switch (move) {
            case "left":
                if (topState.firstColumn == 0) return false;
                if (topState.firstColumn == 1 && topState.secondColumn == 0) return false;
                return true;
            case "right":
                int column = MainActivity.COLUMN;
                if (topState.firstColumn == column - 1) return false;
                if (topState.firstColumn == column - 2 && topState.secondColumn == column - 1) return false;
                return true;
        }
        return false;
    }

    public int getRotatedSecondColumn(String rotation) {
        if (!isValidRotation(rotation)) {
            return topState.secondColumn;
        }

        if (topState.secondColumn == topState.firstColumn && topState.secondRow == topState.firstRow - 1) {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("right", topState.firstColumn + 1);
            map.put("left", topState.firstColumn - 1);
            return map.get(rotation);
        }
        if (topState.secondColumn == topState.firstColumn && topState.secondRow == topState.firstRow + 1) {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("right", topState.firstColumn - 1);
            map.put("left", topState.firstColumn + 1);
            return map.get(rotation);
        }
        return topState.firstColumn;
    }

    public int getRotatedSecondRow(String rotation) {
        if (!isValidRotation(rotation)) {
            return topState.secondRow;
        }

        if (topState.secondRow == topState.firstRow && topState.secondColumn == topState.firstColumn - 1) {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("right", topState.firstRow - 1);
            map.put("left", topState.firstRow + 1);
            return map.get(rotation);
        }
        if (topState.secondRow == topState.firstRow && topState.secondColumn == topState.firstColumn + 1) {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("right", topState.firstRow + 1);
            map.put("left", topState.firstRow - 1);
            return map.get(rotation);
        }
        return topState.firstRow;
    }


    public boolean isValidRotation(String rotation) {
        if (rotation == "left") {
            if (topState.firstColumn == 0 && topState.secondRow == 0) {
                return false;
            }
            if (topState.firstColumn == MainActivity.COLUMN - 1 && topState.secondRow == 2) {
                return false;
            }
        }
        if (rotation == "right") {
            if (topState.firstColumn == 0 && topState.secondRow == 2) {
                return false;
            }
            if (topState.firstColumn == MainActivity.COLUMN - 1 && topState.secondRow == 0) {
                return false;
            }
        }
        return true;
    }

    public LinearLayout[][] getDropedGridStates(LinearLayout[][] grid, LinearLayout[][] topGrid) {
        int r1 = ROW - 1;
        int dropedFirstRow = topState.secondRow == topState.firstRow + 1 ? r1 - 1 : r1;

        while (r1 >= 0 && ((ColorDrawable) grid[r1][topState.firstColumn].getBackground()).getColor() != FIELD_NONE) {
            dropedFirstRow = topState.secondRow == topState.firstRow + 1 ? r1 - 2 : r1 - 1;
            r1--;
        }

        int r2 = ROW - 1;
        int dropedSecondRow = topState.secondRow == topState.firstRow - 1 ? r2 - 1 : r2;
        while (r2 >= 0 && ((ColorDrawable) grid[r2][topState.secondColumn].getBackground()).getColor() != FIELD_NONE) {
            dropedSecondRow = topState.secondRow == topState.firstRow - 1 ? r2 - 2 : r2 - 1;
            r2--;
        }

        ColorDrawable firstGridColor = (ColorDrawable) topGrid[topState.firstRow][topState.firstColumn].getBackground();
        ColorDrawable secondGridColor = (ColorDrawable) topGrid[topState.secondRow][topState.secondColumn].getBackground();

        if (dropedFirstRow >= 0) {
            grid[dropedFirstRow][topState.firstColumn].setBackgroundColor(firstGridColor.getColor());
        }
        if (dropedSecondRow >= 0) {
            grid[dropedSecondRow][topState.secondColumn].setBackgroundColor(secondGridColor.getColor());
        }

        return grid;
    }
}
