package yuyasat.pojectandroid.operation;

import android.graphics.drawable.ColorDrawable;
import android.view.View;

import yuyasat.pojectandroid.MainActivity;
import yuyasat.pojectandroid.entity.TopState;

/**
 * Created by yuyataki on 2017/08/26.
 */

public class ButtonXClickListener implements View.OnClickListener  {
    private MainActivity mainActivity = null;

    public ButtonXClickListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onClick(View v) {
        if (mainActivity.mTimer != null) { return; }
        ColorDrawable firstGridColor = (ColorDrawable) mainActivity.topGrid[mainActivity.firstRow][mainActivity.firstColumn].getBackground();
        ColorDrawable secondGridColor = (ColorDrawable) mainActivity.topGrid[mainActivity.secondRow][mainActivity.secondColumn].getBackground();

        int firstColor = firstGridColor.getColor();
        int secondColor = secondGridColor.getColor();

        int nextSecondColumn;
        int nextSecondRow;

        TopState topState = new TopState(mainActivity.firstColumn, mainActivity.secondColumn, mainActivity.firstRow, mainActivity.secondRow, firstColor, secondColor, mainActivity.topGrid);
        KeyOperation keyOperation = new KeyOperation(topState);

        nextSecondColumn = keyOperation.getRotatedSecondColumn("left");
        nextSecondRow = keyOperation.getRotatedSecondRow("left");

        mainActivity.topGrid[nextSecondRow][nextSecondColumn].setBackgroundColor(secondGridColor.getColor());
        if (keyOperation.isValidRotation("left")) {
            mainActivity.topGrid[mainActivity.secondRow][mainActivity.secondColumn].setBackgroundColor(mainActivity.NONE);
        }
        mainActivity.secondColumn = nextSecondColumn;
        mainActivity.secondRow = nextSecondRow;
    }
}
