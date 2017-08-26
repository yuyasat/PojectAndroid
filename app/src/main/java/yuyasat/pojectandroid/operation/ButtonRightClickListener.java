package yuyasat.pojectandroid.operation;

import android.graphics.drawable.ColorDrawable;
import android.view.View;

import yuyasat.pojectandroid.MainActivity;
import yuyasat.pojectandroid.entity.TopState;

/**
 * Created by yuyataki on 2017/08/26.
 */

public class ButtonRightClickListener implements View.OnClickListener  {
    private MainActivity mainActivity = null;

    public ButtonRightClickListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onClick(View v) {
        if (mainActivity.mTimer != null) { return; }
        ColorDrawable firstGridColor = (ColorDrawable) mainActivity.topGrid[mainActivity.firstRow][mainActivity.firstColumn].getBackground();
        ColorDrawable secondGridColor = (ColorDrawable) mainActivity.topGrid[mainActivity.secondRow][mainActivity.secondColumn].getBackground();

        int firstColor = firstGridColor.getColor();
        int secondColor = secondGridColor.getColor();

        TopState topState = new TopState(mainActivity.firstColumn, mainActivity.secondColumn, mainActivity.firstRow, mainActivity.secondRow, firstColor, secondColor, mainActivity.topGrid);
        KeyOperation keyOperation = new KeyOperation(topState);
        if (keyOperation.isValidMove("right")) {
            mainActivity.topGrid[mainActivity.firstRow][mainActivity.firstColumn].setBackgroundColor(mainActivity.NONE);
            mainActivity.topGrid[mainActivity.secondRow][mainActivity.secondColumn].setBackgroundColor(mainActivity.NONE);
            mainActivity.topGrid[mainActivity.firstRow][mainActivity.firstColumn + 1].setBackgroundColor(firstColor);
            mainActivity.topGrid[mainActivity.secondRow][mainActivity.secondColumn + 1].setBackgroundColor(secondColor);

            mainActivity.firstColumn += 1;
            mainActivity.secondColumn += 1;
        }
    }
}
