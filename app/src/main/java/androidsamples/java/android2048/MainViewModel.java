package androidsamples.java.android2048;

import android.util.Log;

import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private static final String TAG = "vm";

    private int[][] grid;
    private int x, y;

    public MainViewModel() {
        Log.d(TAG, "2");
        grid = new int[4][4];

        for (int i=0; i<4; i++)
            for (int j=0; j<4; j++)
                grid[i][j] = 0;

        grid[1][1] = 1;
        x = 1;
        y = 1;
    }

    public void up() {
        grid[y][x] = 0;
        y = 0;
        grid[y][x] = 1;
    }

    public void down() {
        grid[y][x] = 0;
        y = 3;
        grid[y][x] = 1;
    }

    public void left() {
        grid[y][x] = 0;
        x = 0;
        grid[y][x] = 1;
    }

    public void right() {
        grid[y][x] = 0;
        x = 3;
        grid[y][x] = 1;
    }

    public int[][] getGrid() {
        return grid;
    }
}
