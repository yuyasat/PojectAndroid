package yuyasat.pojectandroid.algorithm;

import android.graphics.drawable.ColorDrawable;
import android.widget.LinearLayout;

import yuyasat.pojectandroid.entity.CountAndGrid;

import static yuyasat.pojectandroid.MainActivity.COLUMN;
import static yuyasat.pojectandroid.MainActivity.ROW;
import static yuyasat.pojectandroid.MainActivity.FIELD_NONE;

/**
 * Created by yuyataki on 2017/08/12.
 */

public class Algorithm {
    public static int countColor(int j, int i, LinearLayout[][] grids) {
        int color = ((ColorDrawable) grids[j][i].getBackground()).getColor();
        int n = 1;
        grids[j][i].setBackgroundColor(FIELD_NONE);
        if (j - 1 >= 0 && ((ColorDrawable) grids[j - 1][i].getBackground()).getColor() == color) {
            n += countColor(j - 1, i, grids);
        }
        if (j + 1 < ROW && ((ColorDrawable) grids[j + 1][i].getBackground()).getColor() == color) {
            n += countColor(j + 1, i, grids);
        }
        if (i - 1 >= 0 && ((ColorDrawable) grids[j][i - 1].getBackground()).getColor() == color) {
            n += countColor(j, i - 1, grids);
        }
        if (i + 1 < COLUMN && ((ColorDrawable) grids[j][i + 1].getBackground()).getColor() == color) {
            n += countColor(j, i + 1, grids);
        }
        grids[j][i].setBackgroundColor(color);
        return n;
    }

    public static LinearLayout[][] deleteColor(int j, int i, LinearLayout[][] grids) {
        int color = ((ColorDrawable) grids[j][i].getBackground()).getColor();
        grids[j][i].setBackgroundColor(FIELD_NONE);

        if (j - 1 >= 0 && ((ColorDrawable) grids[j - 1][i].getBackground()).getColor() == color) {
            deleteColor(j - 1, i, grids);
        }
        if (j + 1 < ROW && ((ColorDrawable) grids[j + 1][i].getBackground()).getColor() == color) {
            deleteColor(j + 1, i, grids);
        }
        if (i - 1 >= 0 && ((ColorDrawable) grids[j][i - 1].getBackground()).getColor() == color) {
            deleteColor(j, i - 1, grids);
        }
        if (i + 1 < COLUMN && ((ColorDrawable) grids[j][i + 1].getBackground()).getColor() == color) {
            deleteColor(j, i + 1, grids);
        }
        return grids;
    }

    public static CountAndGrid allocateGrids(LinearLayout[][] grids) {
        int count = 0;
        for (int i = 0; i < grids[0].length; i++) {
            int spaces = 0;
            for (int j = grids.length - 1; j >= 0; j--) {
                if (((ColorDrawable) grids[j][i].getBackground()).getColor() == FIELD_NONE) {
                    spaces++;
                } else if (spaces > 0) {
                    grids[j + spaces][i].setBackgroundColor(((ColorDrawable) grids[j][i].getBackground()).getColor());
                    grids[j][i].setBackgroundColor(FIELD_NONE);
                    count++;
                }
            }
        }
        CountAndGrid countAndGrid = new CountAndGrid(count, grids);
        return countAndGrid;
    }


}
