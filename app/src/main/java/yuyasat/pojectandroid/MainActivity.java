package yuyasat.pojectandroid;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.Random;

/**
 * Created by yuyataki on 2017/05/28.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final int x_length = 800;
    private final int y_length = 800;
    private final int column = 7;
    private final int row = 11;
    private final int topRow = 3;
    private final int topNextColumn = 2;
    private final int topNextRow = 2;
    private final int none = Color.WHITE;
    private final int red = Color.RED;
    private final int blue = Color.BLUE;
    private final int green = Color.GREEN;
    private final int yellow = Color.YELLOW;
    private final int[] colors = {red, blue, green, yellow};


    private LinearLayout topGrid[][];
    private int firstRow = 0;
    private int firstColumn = 2;
    private int secondRow = 1;
    private int secondColumn = 2;

    private LinearLayout topNextGrid[][];
    private LinearLayout grid[][];
    private int gridWidth = 70;
    private int gridHeight = 50;
    private LinearLayout.LayoutParams rowLayoutParams =
            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
    private LinearLayout.LayoutParams gridLayoutParams = new LinearLayout.LayoutParams(gridWidth, gridHeight);
    private Point mark;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout fieldLayout = (LinearLayout) findViewById(R.id.field);
        LinearLayout topFieldLayout = (LinearLayout) findViewById(R.id.top_field);
        LinearLayout topNextFieldLayout = (LinearLayout) findViewById(R.id.top_next_field);

        LinearLayout.LayoutParams fieldLayoutParams = new LinearLayout.LayoutParams(x_length, y_length);
        // fieldLayout.setOrientation(LinearLayout.VERTICAL);
        // fieldLayout.setLayoutParams(fieldLayoutParams);

        topGrid = initializeTopState(topGrid, column, topRow);
        renderField(topFieldLayout, topGrid);
        topNextGrid = initializeTopNextState(topNextGrid, topNextColumn, topNextRow);
        renderField(topNextFieldLayout, topNextGrid);
        grid = initializeState(grid, column, row);
        renderField(fieldLayout, grid);

        Button ivt1Button = (Button) findViewById(R.id.button_left);
        ivt1Button.setOnClickListener(this);
        Button ivt2Button = (Button) findViewById(R.id.button_right);
        ivt2Button.setOnClickListener(this);
        mark = new Point();
        mark.set(6, 3);
        grid[mark.x][mark.y].setBackgroundColor(Color.RED);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_left:
//                Log.d("testq111111",String.valueOf(isValidMove("left")));
                if ( isValidMove("left")) {
                    ColorDrawable firstGridColor = (ColorDrawable) topGrid[firstColumn][firstRow].getBackground();
                    topGrid[firstColumn - 1][firstRow].setBackgroundColor(firstGridColor.getColor());
                    topGrid[firstColumn][firstRow].setBackgroundColor(none);

                    ColorDrawable secondGridColor = (ColorDrawable) topGrid[secondColumn][secondRow].getBackground();
                    topGrid[secondColumn - 1][secondRow].setBackgroundColor(secondGridColor.getColor());
                    topGrid[secondColumn][secondRow].setBackgroundColor(none);

                    firstColumn -= 1;
                    secondColumn -= 1;
                }
                break;
            case R.id.button_right:
                if ( isValidMove("right")) {
                    ColorDrawable firstGridColor = (ColorDrawable) topGrid[firstColumn][firstRow].getBackground();
                    topGrid[firstColumn + 1][firstRow].setBackgroundColor(firstGridColor.getColor());
                    topGrid[firstColumn][firstRow].setBackgroundColor(none);

                    ColorDrawable secondGridColor = (ColorDrawable) topGrid[secondColumn][secondRow].getBackground();
                    topGrid[secondColumn + 1][secondRow].setBackgroundColor(secondGridColor.getColor());
                    topGrid[secondColumn][secondRow].setBackgroundColor(none);

                    firstColumn += 1;
                    secondColumn += 1;
                }
                break;
            case R.id.button_X:
                break;
            case R.id.button_Z:
                break;
            case R.id.button_down:
                break;
            default:
                break;
        }
    }

    private void renderField(LinearLayout layout, LinearLayout[][] targetGrid) {
        for (int i = 0; i < targetGrid[0].length; i++) {
            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setLayoutParams(rowLayoutParams);
            for (int j = 0; j < targetGrid.length; j++) {
                rowLayout.addView(targetGrid[j][i]);
            }
            layout.addView(rowLayout);
        }
    }

    private LinearLayout[][] initializeState(LinearLayout[][] targetGrid, int column, int row) {
        targetGrid = new LinearLayout[column][row];
        for (int i = 0; i < row; i++) {
            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setLayoutParams(rowLayoutParams);
            for (int j = 0; j < column; j++) {
                targetGrid[j][i] = new LinearLayout(this);
                targetGrid[j][i].setLayoutParams(gridLayoutParams);
                targetGrid[j][i].setBackgroundColor(Color.WHITE);
                gridLayoutParams.setMargins(1, 1, 1, 1);
            }
        }
        return targetGrid;
    }

    private LinearLayout[][] initializeTopNextState(LinearLayout[][] targetGrid, int column, int row) {
        targetGrid = initializeState(targetGrid, column, row);
        Random rnd = new Random();
        targetGrid[0][0].setBackgroundColor(colors[rnd.nextInt(4)]);
        targetGrid[0][1].setBackgroundColor(colors[rnd.nextInt(4)]);
        targetGrid[1][0].setBackgroundColor(colors[rnd.nextInt(4)]);
        targetGrid[1][1].setBackgroundColor(colors[rnd.nextInt(4)]);

        return targetGrid;
    }

    private LinearLayout[][] initializeTopState(LinearLayout[][] targetGrid, int column, int row) {
        targetGrid = initializeState(targetGrid, column, row);
        Random rnd = new Random();
        targetGrid[2][0].setBackgroundColor(colors[rnd.nextInt(4)]);
        targetGrid[2][1].setBackgroundColor(colors[rnd.nextInt(4)]);

        return targetGrid;
    }

    private boolean isValidMove (String move) {
        Log.d("test",move);
        switch (move) {
            case "left":
                if (firstColumn == 0) { return false; }
                if (firstColumn == 1 && secondColumn == 0) { return false; }
                return true;
            case "right":
                if (firstColumn == column - 1) { return false; }
                if (firstColumn == column - 2 && secondColumn == column - 1) {
                    return false;
                }
                return true;
        }
        return false;
    }

    private boolean isValidRotation (String rotation) {
        Log.d("test", rotation);
        if (rotation == "left") {
            if (firstColumn == 0 && secondRow == 0) { return false; }
            if (firstColumn == column - 1 && secondRow == 2) { return false; }
        }
        if (rotation == "right") {
            if (firstColumn == 0 && secondRow == 2) { return false; }
            if (firstColumn == column - 1 && secondRow == 0) { return false; }
        }
        return true;
    }
}
