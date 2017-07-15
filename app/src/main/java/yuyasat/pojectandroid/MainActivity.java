package yuyasat.pojectandroid;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by yuyataki on 2017/05/28.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int COLUMN = 7;
    private static final int ROW = 11;

    private static final int TOP_ROW = 3;
    private static final int TOP_NEXT_COLUMN = 2;
    private static final int TOP_NEXT_ROW = 2;
    private static final int INITIAL_FIRST_ROW = 1;
    private static final int INITIAL_FIRST_COLUMN = 2;
    private static final int INITIAL_SECOND_ROW = 0;
    private static final int INITIAL_SECOND_COLUMN = 2;
    private static final int NONE = Color.WHITE;
    private static final int RED = Color.RED;
    private static final int BLUE = Color.BLUE;
    private static final int GREEN = Color.GREEN;
    private static final int YELLOW = Color.YELLOW;
    private static final int[] COLORS = {RED, BLUE, GREEN, YELLOW};

    private int gridWidth = 35;
    private int gridHeight = 25;

    private LinearLayout topGrid[][];
    private LinearLayout topNextGrid[][];
    private LinearLayout grid[][];

    private int firstRow = INITIAL_FIRST_ROW;
    private int firstColumn = INITIAL_FIRST_COLUMN;
    private int secondRow = INITIAL_SECOND_ROW;
    private int secondColumn = INITIAL_SECOND_COLUMN;

    private LinearLayout.LayoutParams rowLayoutParams =
            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout fieldLayout = (LinearLayout) findViewById(R.id.field);
        LinearLayout topFieldLayout = (LinearLayout) findViewById(R.id.top_field);
        LinearLayout topNextFieldLayout = (LinearLayout) findViewById(R.id.top_next_field);

        topGrid = initializeTopState(topGrid, COLUMN, TOP_ROW);
        renderField(topFieldLayout, topGrid);
        topNextGrid = initializeTopNextState(topNextGrid, TOP_NEXT_COLUMN, TOP_NEXT_ROW);
        renderField(topNextFieldLayout, topNextGrid);
        grid = initializeState(grid, COLUMN, ROW);
        renderField(fieldLayout, grid);

        Button ivt1Button = (Button) findViewById(R.id.button_left);
        ivt1Button.setOnClickListener(this);
        Button ivt2Button = (Button) findViewById(R.id.button_right);
        ivt2Button.setOnClickListener(this);
        Button ivt3Button = (Button) findViewById(R.id.button_X);
        ivt3Button.setOnClickListener(this);
        Button ivt4Button = (Button) findViewById(R.id.button_Z);
        ivt4Button.setOnClickListener(this);
        Button ivt5Button = (Button) findViewById(R.id.button_down);
        ivt5Button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ColorDrawable firstGridColor = (ColorDrawable) topGrid[firstRow][firstColumn].getBackground();
        ColorDrawable secondGridColor = (ColorDrawable) topGrid[secondRow][secondColumn].getBackground();

        int firstColor = firstGridColor.getColor();
        int secondColor = secondGridColor.getColor();

        int nextSecondColumn;
        int nextSecondRow;

        switch (v.getId()) {
            case R.id.button_left:
                if (isValidMove("left")) {
                    topGrid[firstRow][firstColumn].setBackgroundColor(NONE);
                    topGrid[secondRow][secondColumn].setBackgroundColor(NONE);
                    topGrid[firstRow][firstColumn - 1].setBackgroundColor(firstColor);
                    topGrid[secondRow][secondColumn - 1].setBackgroundColor(secondColor);

                    firstColumn -= 1;
                    secondColumn -= 1;
                }
                break;
            case R.id.button_right:
                if (isValidMove("right")) {
                    topGrid[firstRow][firstColumn].setBackgroundColor(NONE);
                    topGrid[secondRow][secondColumn].setBackgroundColor(NONE);
                    topGrid[firstRow][firstColumn + 1].setBackgroundColor(firstColor);
                    topGrid[secondRow][secondColumn + 1].setBackgroundColor(secondColor);

                    firstColumn += 1;
                    secondColumn += 1;
                }
                break;
            case R.id.button_X:
                nextSecondColumn = getRotatedSecondColumn("left");
                nextSecondRow = getRotatedSecondRow("left");

                topGrid[nextSecondRow][nextSecondColumn].setBackgroundColor(secondGridColor.getColor());
                if (isValidRotation("left")) {
                    topGrid[secondRow][secondColumn].setBackgroundColor(NONE);
                }
                secondColumn = nextSecondColumn;
                secondRow = nextSecondRow;
                break;
            case R.id.button_Z:
                nextSecondColumn = getRotatedSecondColumn("right");
                nextSecondRow = getRotatedSecondRow("right");

                topGrid[nextSecondRow][nextSecondColumn].setBackgroundColor(secondGridColor.getColor());
                if (isValidRotation("right")) {
                    topGrid[secondRow][secondColumn].setBackgroundColor(NONE);
                }

                secondColumn = nextSecondColumn;
                secondRow = nextSecondRow;
                break;
            case R.id.button_down:
                grid = getDropedGridStates();
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

                chain(grid, 0);
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

    private LinearLayout[][] initializeState(LinearLayout[][] targetGrid, int column, int row) {
        targetGrid = new LinearLayout[row][column];

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        LinearLayout.LayoutParams gridLayoutParams =
                new LinearLayout.LayoutParams(convertToDPI(gridWidth, metrics), convertToDPI(gridHeight, metrics));

        for (int i = 0; i < row; i++) {
            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setLayoutParams(rowLayoutParams);
            for (int j = 0; j < column; j++) {
                targetGrid[i][j] = new LinearLayout(this);
                targetGrid[i][j].setLayoutParams(gridLayoutParams);
                targetGrid[i][j].setBackgroundColor(Color.WHITE);
                gridLayoutParams.setMargins(convertToDPI(1, metrics), convertToDPI(1, metrics), convertToDPI(1, metrics), convertToDPI(1, metrics));
            }
        }
        return targetGrid;
    }

    private LinearLayout[][] initializeTopNextState(LinearLayout[][] targetGrid, int column, int row) {
        targetGrid = initializeState(targetGrid, column, row);
        Random rnd = new Random();
        targetGrid[0][0].setBackgroundColor(COLORS[rnd.nextInt(4)]);
        targetGrid[0][1].setBackgroundColor(COLORS[rnd.nextInt(4)]);
        targetGrid[1][0].setBackgroundColor(COLORS[rnd.nextInt(4)]);
        targetGrid[1][1].setBackgroundColor(COLORS[rnd.nextInt(4)]);

        return targetGrid;
    }

    private LinearLayout[][] initializeTopState(LinearLayout[][] targetGrid, int column, int row) {
        targetGrid = initializeState(targetGrid, column, row);
        Random rnd = new Random();
        targetGrid[1][2].setBackgroundColor(COLORS[rnd.nextInt(4)]);
        targetGrid[0][2].setBackgroundColor(COLORS[rnd.nextInt(4)]);

        return targetGrid;
    }

    private boolean isValidMove(String move) {
        switch (move) {
            case "left":
                if (firstColumn == 0) {
                    return false;
                }
                if (firstColumn == 1 && secondColumn == 0) {
                    return false;
                }
                return true;
            case "right":
                if (firstColumn == COLUMN - 1) {
                    return false;
                }
                if (firstColumn == COLUMN - 2 && secondColumn == COLUMN - 1) {
                    return false;
                }
                return true;
        }
        return false;
    }

    private boolean isValidRotation(String rotation) {
        if (rotation == "left") {
            if (firstColumn == 0 && secondRow == 0) {
                return false;
            }
            if (firstColumn == COLUMN - 1 && secondRow == 2) {
                return false;
            }
        }
        if (rotation == "right") {
            if (firstColumn == 0 && secondRow == 2) {
                return false;
            }
            if (firstColumn == COLUMN - 1 && secondRow == 0) {
                return false;
            }
        }
        return true;
    }

    private int getRotatedSecondColumn(String rotation) {
        if (!isValidRotation(rotation)) {
            return secondColumn;
        }

        if (secondColumn == firstColumn && secondRow == firstRow - 1) {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("right", firstColumn + 1);
            map.put("left", firstColumn - 1);
            return map.get(rotation);
        }
        if (secondColumn == firstColumn && secondRow == firstRow + 1) {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("right", firstColumn - 1);
            map.put("left", firstColumn + 1);
            return map.get(rotation);
        }
        return firstColumn;
    }

    private int getRotatedSecondRow(String rotation) {
        if (!isValidRotation(rotation)) {
            return secondRow;
        }

        if (secondRow == firstRow && secondColumn == firstColumn - 1) {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("right", firstRow - 1);
            map.put("left", firstRow + 1);
            return map.get(rotation);
        }
        if (secondRow == firstRow && secondColumn == firstColumn + 1) {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("right", firstRow + 1);
            map.put("left", firstRow - 1);
            return map.get(rotation);
        }
        return firstRow;
    }

    private LinearLayout[][] getDropedGridStates() {
        int r1 = ROW - 1;
        int dropedFirstRow = secondRow == firstRow + 1 ? r1 - 1 : r1;

        while (r1 >= 0 && ((ColorDrawable) grid[r1][firstColumn].getBackground()).getColor() != NONE) {
            dropedFirstRow = secondRow == firstRow + 1 ? r1 - 2 : r1 - 1;
            r1--;
        }

        int r2 = ROW - 1;
        int dropedSecondRow = secondRow == firstRow - 1 ? r2 - 1 : r2;
        while (r2 >= 0 && ((ColorDrawable) grid[r2][secondColumn].getBackground()).getColor() != NONE) {
            dropedSecondRow = secondRow == firstRow - 1 ? r2 - 2 : r2 - 1;
            r2--;
        }

        ColorDrawable firstGridColor = (ColorDrawable) topGrid[firstRow][firstColumn].getBackground();
        ColorDrawable secondGridColor = (ColorDrawable) topGrid[secondRow][secondColumn].getBackground();

        if (dropedFirstRow >= 0) {
            grid[dropedFirstRow][firstColumn].setBackgroundColor(firstGridColor.getColor());
        }
        if (dropedSecondRow >= 0) {
            grid[dropedSecondRow][secondColumn].setBackgroundColor(secondGridColor.getColor());
        }

        return grid;
    }

    private LinearLayout[][] chain(LinearLayout[][] chainedGridStates, int chainCount) {
        CountAndGrid countAndGrid = getDeletedGridStates(chainedGridStates, chainCount);
        int countedChainCount = countAndGrid.count;
        grid = countAndGrid.grids;
        dropGrids(grid, chainCount);
        return grid;

    }

    private CountAndGrid getDeletedGridStates(LinearLayout[][] grids, int chainCount) {
        int deletedColor = NONE;
        for (int j = 0; j < grids.length; j++) {
            for (int i = 0; i < grids[j].length; i++) {
                if (((ColorDrawable) grid[j][i].getBackground()).getColor() != NONE && countColor(j, i, grids) >= 4) {
                    if (deletedColor == NONE || deletedColor == ((ColorDrawable) grid[j][i].getBackground()).getColor()) {
                        deletedColor = ((ColorDrawable) grid[j][i].getBackground()).getColor();
                        chainCount++;
                    }
                    grids = deleteColor(j, i, grids);
                }
            }
        }
        CountAndGrid countAndGrid = new CountAndGrid(chainCount, grids);
        return countAndGrid;
    }

    private int countColor(int j, int i, LinearLayout[][] grids) {
        int color = ((ColorDrawable) grids[j][i].getBackground()).getColor();
        int n = 1;
        grids[j][i].setBackgroundColor(NONE);
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

    private LinearLayout[][] deleteColor(int j, int i, LinearLayout[][] grids) {
        int color = ((ColorDrawable) grids[j][i].getBackground()).getColor();
        grids[j][i].setBackgroundColor(NONE);

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

    private CountAndGrid allocateGrids(LinearLayout[][] grids) {
        int count = 0;
        for (int i = 0; i < grids[0].length; i++) {
            int spaces = 0;
            for (int j = grids.length - 1; j >= 0; j--) {
                if (((ColorDrawable) grid[j][i].getBackground()).getColor() == NONE) {
                    spaces++;
                } else if (spaces > 0) {
                    grids[j + spaces][i].setBackgroundColor(((ColorDrawable) grid[j][i].getBackground()).getColor());
                    grids[j][i].setBackgroundColor(NONE);
                    count++;
                }
            }
        }
        CountAndGrid countAndGrid = new CountAndGrid(count, grids);
        return countAndGrid;
    }

    private LinearLayout[][] dropGrids(LinearLayout[][] deletedGridStates, int chainCount) {
        CountAndGrid countAndGrid = allocateGrids(deletedGridStates);
        grid = countAndGrid.grids;

        if (countAndGrid.count > 0) {
            chain(grid, chainCount);
        } else {

        }
        return grid;

    }

    public static int convertToDPI(int size, DisplayMetrics metrics) {
        return (size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
    }

    class CountAndGrid {
        public int count;
        public LinearLayout[][] grids;

        CountAndGrid(int count, LinearLayout[][] grids) {
            this.count = count;
            this.grids = grids;
        }
    }
}