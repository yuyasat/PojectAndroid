package yuyasat.pojectandroid;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.os.Handler;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.RunnableFuture;

import yuyasat.pojectandroid.algorithm.Algorithm;
import yuyasat.pojectandroid.entity.CountAndGrid;
import yuyasat.pojectandroid.entity.TopState;
import yuyasat.pojectandroid.operation.ButtonLeftClickListener;
import yuyasat.pojectandroid.operation.ButtonRightClickListener;


import yuyasat.pojectandroid.operation.ButtonXClickListener;
import yuyasat.pojectandroid.operation.ButtonZClickListener;
import yuyasat.pojectandroid.operation.KeyOperation;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int COLUMN = 7;
    public static final int ROW = 11;

    private static final int TOP_ROW = 3;
    private static final int TOP_NEXT_COLUMN = 2;
    private static final int TOP_NEXT_ROW = 2;
    public static final int INITIAL_FIRST_ROW = 1;
    public static final int INITIAL_FIRST_COLUMN = 2;
    public static final int INITIAL_SECOND_ROW = 0;
    public static final int INITIAL_SECOND_COLUMN = 2;
    public static final int NONE = Color.TRANSPARENT;
    public static final int FIELD_NONE = Color.WHITE;
    private static final int RED = Color.RED;
    private static final int BLUE = Color.BLUE;
    private static final int GREEN = Color.GREEN;
    private static final int YELLOW = Color.YELLOW;
    public static final int[] COLORS = {RED, BLUE, GREEN, YELLOW};

    private int gridWidth = 35;
    private int gridHeight = 25;

    public LinearLayout topGrid[][];
    public LinearLayout topNextGrid[][];
    public LinearLayout grid[][];

    public int firstRow = INITIAL_FIRST_ROW;
    public int firstColumn = INITIAL_FIRST_COLUMN;
    public int secondRow = INITIAL_SECOND_ROW;
    public int secondColumn = INITIAL_SECOND_COLUMN;
    public ChainTimerTask chainTimerTask = null;
    private SetGridTimerTask setGridTimerTask = null;
    private DropGridTimerTask dropGridTimerTask = null;
    public Timer mTimer = null;
    public Handler mHandler = new Handler();

    private LinearLayout.LayoutParams rowLayoutParams =
            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout fieldLayout = (LinearLayout) findViewById(R.id.field);
        LinearLayout topFieldLayout = (LinearLayout) findViewById(R.id.top_field);
        LinearLayout topNextFieldLayout = (LinearLayout) findViewById(R.id.top_next_field);

        topGrid = initializeTopState(COLUMN, TOP_ROW);
        renderField(topFieldLayout, topGrid);
        topNextGrid = initializeTopNextState(TOP_NEXT_COLUMN, TOP_NEXT_ROW);
        renderField(topNextFieldLayout, topNextGrid);
        grid = initializeState(COLUMN, ROW);
        renderField(fieldLayout, grid);

        Button ivt1Button = (Button) findViewById(R.id.button_left);
        ivt1Button.setOnClickListener(new ButtonLeftClickListener(this));
        Button ivt2Button = (Button) findViewById(R.id.button_right);
        ivt2Button.setOnClickListener(new ButtonRightClickListener(this));
        Button ivt3Button = (Button) findViewById(R.id.button_X);
        ivt3Button.setOnClickListener(new ButtonXClickListener(this));
        Button ivt4Button = (Button) findViewById(R.id.button_Z);
        ivt4Button.setOnClickListener(new ButtonZClickListener(this));
        Button ivt5Button = (Button) findViewById(R.id.button_down);
        ivt5Button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mTimer != null) { return; }
        ColorDrawable firstGridColor = (ColorDrawable) topGrid[firstRow][firstColumn].getBackground();
        ColorDrawable secondGridColor = (ColorDrawable) topGrid[secondRow][secondColumn].getBackground();

        int firstColor = firstGridColor.getColor();
        int secondColor = secondGridColor.getColor();

        TopState topState = new TopState(firstColumn, secondColumn, firstRow, secondRow, firstColor, secondColor, topGrid);
        KeyOperation keyOperation = new KeyOperation(topState);

        switch (v.getId()) {
            case R.id.button_down:
                grid = keyOperation.getDropedGridStates(grid, topGrid);
                ColorDrawable firstNextColor = (ColorDrawable) topNextGrid[0][0].getBackground();
                ColorDrawable secondNextColor = (ColorDrawable) topNextGrid[1][0].getBackground();
                topGrid[firstRow][firstColumn].setBackgroundColor(NONE);
                topGrid[secondRow][secondColumn].setBackgroundColor(NONE);
                topGrid[0][2].setBackgroundColor(firstNextColor.getColor());
                topGrid[1][2].setBackgroundColor(secondNextColor.getColor());

                ColorDrawable firstNextNextColor = (ColorDrawable) topNextGrid[0][1].getBackground();
                ColorDrawable secondNextNextColor = (ColorDrawable) topNextGrid[1][1].getBackground();
                topNextGrid[0][0].setBackgroundColor(firstNextNextColor.getColor());
                topNextGrid[1][0].setBackgroundColor(secondNextNextColor.getColor());

                Random rnd = new Random();
                topNextGrid[0][1].setBackgroundColor(COLORS[rnd.nextInt(4)]);
                topNextGrid[1][1].setBackgroundColor(COLORS[rnd.nextInt(4)]);

                firstRow = INITIAL_FIRST_ROW;
                firstColumn = INITIAL_FIRST_COLUMN;
                secondRow = INITIAL_SECOND_ROW;
                secondColumn = INITIAL_SECOND_COLUMN;

                chainTimerTask = new ChainTimerTask();
                chainTimerTask.setChainCount(0);
                mTimer = new Timer();
                mTimer.schedule(chainTimerTask, 400);
                break;
            default:
                break;
        }
    }

    private void renderField(LinearLayout layout, LinearLayout[][] targetGrid) {
        for (int i = 0; i < targetGrid.length; i++) {
            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setLayoutParams(rowLayoutParams);
            for (int j = 0; j < targetGrid[i].length; j++) {
                rowLayout.addView(targetGrid[i][j]);
            }
            layout.addView(rowLayout);
        }
    }

    private LinearLayout[][] initializeState(int column, int row) {
        LinearLayout[][] targetGrid = new LinearLayout[row][column];

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        LinearLayout.LayoutParams gridLayoutParams =
                new LinearLayout.LayoutParams(convertToDPI(gridWidth, metrics), convertToDPI(gridHeight, metrics));

        int margin = convertToDPI(1, metrics);
        for (int i = 0; i < row; i++) {
            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setLayoutParams(rowLayoutParams);
            for (int j = 0; j < column; j++) {
                targetGrid[i][j] = new LinearLayout(this);
                targetGrid[i][j].setLayoutParams(gridLayoutParams);
                if (row == TOP_ROW) {
                    targetGrid[i][j].setBackgroundColor(NONE);
                } else {
                    targetGrid[i][j].setBackgroundColor(FIELD_NONE);
                }
                gridLayoutParams.setMargins(margin, margin, margin, margin);
            }
        }
        return targetGrid;
    }

    private LinearLayout[][] initializeTopNextState(int column, int row) {
        LinearLayout[][] targetGrid = initializeState(column, row);
        Random rnd = new Random();
        targetGrid[0][0].setBackgroundColor(COLORS[rnd.nextInt(4)]);
        targetGrid[0][1].setBackgroundColor(COLORS[rnd.nextInt(4)]);
        targetGrid[1][0].setBackgroundColor(COLORS[rnd.nextInt(4)]);
        targetGrid[1][1].setBackgroundColor(COLORS[rnd.nextInt(4)]);

        return targetGrid;
    }

    private LinearLayout[][] initializeTopState(int column, int row) {
        LinearLayout[][] targetGrid = initializeState(column, row);
        Random rnd = new Random();
        targetGrid[1][2].setBackgroundColor(COLORS[rnd.nextInt(4)]);
        targetGrid[0][2].setBackgroundColor(COLORS[rnd.nextInt(4)]);

        return targetGrid;
    }

    private LinearLayout[][] chain(LinearLayout[][] chainedGridStates, int chainCount) {
        CountAndGrid countAndGrid = getDeletedGridStates(chainedGridStates, chainCount);
        int countedChainCount = countAndGrid.count;
        setGridTimerTask = new SetGridTimerTask();

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        setGridTimerTask = new SetGridTimerTask();
        setGridTimerTask.setChainCount(chainCount);
        setGridTimerTask.setCountAndGrid(countAndGrid);
        mTimer = new Timer(true);
        mTimer.schedule(setGridTimerTask, 200);
        return grid;
    }

    private CountAndGrid getDeletedGridStates(LinearLayout[][] grids, int chainCount) {
        int deletedColor = FIELD_NONE;
        for (int j = 0; j < grids.length; j++) {
            for (int i = 0; i < grids[j].length; i++) {
                if (((ColorDrawable) grid[j][i].getBackground()).getColor() != FIELD_NONE && Algorithm.countColor(j, i, grids) >= 4) {
                    if (deletedColor == FIELD_NONE || deletedColor == ((ColorDrawable) grid[j][i].getBackground()).getColor()) {
                        deletedColor = ((ColorDrawable) grid[j][i].getBackground()).getColor();
                        chainCount++;
                    }
                    grids = Algorithm.deleteColor(j, i, grids);
                }
            }
        }
        return new CountAndGrid(chainCount, grids);
    }

    private LinearLayout[][] dropGrids(LinearLayout[][] deletedGridStates, int chainCount) {
        CountAndGrid countAndGrid = Algorithm.allocateGrids(deletedGridStates);
        grid = countAndGrid.grids;

        chainTimerTask = new ChainTimerTask();
        chainTimerTask.setChainCount(chainCount);
        mTimer = new Timer();
        if (countAndGrid.count > 0) {
            mTimer.scheduleAtFixedRate(chainTimerTask, 200, 200);
        } else {
            mTimer.cancel();
            mTimer = null;
        }
        return grid;

    }

    public static int convertToDPI(int size, DisplayMetrics metrics) {
        return (size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
    }

    private class ChainTimerTask extends TimerTask {
        private int chainCount;

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                public void run() {
                    chain(grid, chainCount);
                }
            });
        }
        private void setChainCount(int chainCount) {
            this.chainCount = chainCount;
        }
    }

    private class SetGridTimerTask extends TimerTask {
        private CountAndGrid countAndGrid;
        private int chainCount;

        @Override
        public void run() {
            grid = countAndGrid.grids;
            dropGridTimerTask = new DropGridTimerTask();
            dropGridTimerTask.setChainCount(chainCount);
            dropGridTimerTask.setCountAndGrid(countAndGrid);
            mTimer = new Timer(true);
            mTimer.schedule(dropGridTimerTask, 200);
        }
        private void setCountAndGrid(CountAndGrid countAndGrid) {
            this.countAndGrid = countAndGrid;
        }
        private void setChainCount(int chainCount) {
            this.chainCount = chainCount;
        }
    }

    private class DropGridTimerTask extends TimerTask {
        private CountAndGrid countAndGrid;
        private int chainCount;

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                public void run() {
                    dropGrids(grid, chainCount);
                }
            });
        }
        private void setCountAndGrid(CountAndGrid countAndGrid) {
            this.countAndGrid = countAndGrid;
        }
        private void setChainCount(int chainCount) {
            this.chainCount = chainCount;
        }
    }
}