package yuyasat.pojectandroid.entity;


import android.widget.LinearLayout;

public class TopState {
    public int firstColumn;
    public int secondColumn;
    public int firstRow;
    public int secondRow;
    public int firstColor;
    public int secondColor;
    public LinearLayout[][] topGrid;

    public TopState(int firstColumn, int secondColumn, int firstRow, int secondRow, int firstColor, int secondColor, LinearLayout[][] topGrid) {
        this.firstColumn = firstColumn;
        this.secondColumn = secondColumn;
        this.firstRow = firstRow;
        this.secondRow = secondRow;
        this.firstColor = firstColor;
        this.secondColor = secondColor;
        this.topGrid = topGrid;
    }
}