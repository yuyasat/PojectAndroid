package yuyasat.pojectandroid.entity;

import android.widget.LinearLayout;

/**
 * Created by yuyataki on 2017/08/12.
 */


public class CountAndGrid {
    public int count;
    public LinearLayout[][] grids;

    public CountAndGrid(int count, LinearLayout[][] grids) {
        this.count = count;
        this.grids = grids;
    }
}