package yuyasat.pojectandroid;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

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
    private LinearLayout topGrid[][];
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

        topGrid = initializeState(topGrid, column, topRow);
        Log.d("test",topGrid[0][1].toString());
        renderField(topFieldLayout, topGrid, column, topRow);
        topNextGrid = initializeState(topNextGrid, topNextColumn, topNextRow);
        renderField(topNextFieldLayout, topNextGrid, topNextColumn, topNextRow);
        grid = initializeState(grid, column, row);
        grid = renderField(fieldLayout, grid, column, row);

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
                this.topGrid[0][1].setBackgroundColor(Color.RED);
                break;
            case R.id.button_right:
                if (mark.x > 0) {
                    grid[mark.x][mark.y].setBackgroundColor(Color.LTGRAY);
                    grid[mark.x - 1][mark.y].setBackgroundColor(Color.RED);
                    mark.x -= 1;
                } else {
                }
                break;
            case R.id.button_down:
                break;
            case R.id.button_X:
                break;
            case R.id.button_Z:
                break;
            default:
                break;
        }
    }

    private LinearLayout[][] renderField(LinearLayout layout, LinearLayout[][] targetGrid, int column, int row) {
        for (int i = 0; i < targetGrid[0].length; i++) {
            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setLayoutParams(rowLayoutParams);
            for (int j = 0; j < targetGrid.length; j++) {
                rowLayout.addView(targetGrid[j][i]);
            }
            layout.addView(rowLayout);
        }
        return targetGrid;
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

}
