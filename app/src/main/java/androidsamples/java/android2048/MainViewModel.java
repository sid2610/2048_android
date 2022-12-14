package androidsamples.java.android2048;

import static java.lang.Math.max;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.util.Random;

public class MainViewModel extends ViewModel {
    private static final String TAG = "vm";

    private Random mRng;
    private int[][] grid;
    private int[][] prevGrid;
    private final int[][] tempGrid;
    private int moves, zeroes, prevZeroes, tempZeroes, maxNum, score, prevScore, tempScore;
    private boolean undoable;

    public MainViewModel() {
        Log.d(TAG, "2");
        grid = new int[4][4];
        prevGrid = new int[4][4];
        tempGrid = new int[4][4];

        for (int i=0; i<4; i++)
            for (int j=0; j<4; j++)
                grid[i][j] = 0;

        mRng = new Random();
        int x1 = mRng.nextInt(4);
        int y1 = mRng.nextInt(4);
        int x2 = mRng.nextInt(4);
        int y2 = mRng.nextInt(4);

        while (x1==x2 && y1==y2) {
            x2 = mRng.nextInt(4);
            y2 = mRng.nextInt(4);
        }

        maxNum = 2;

        if (mRng.nextInt(10)==0) {
            grid[y1][x1] = 4;
            maxNum = 4;
        }
        else
            grid[y1][x1] = 2;

        if (mRng.nextInt(10)==0) {
            grid[y2][x2] = 4;
            maxNum = 4;
        }
        else
            grid[y2][x2] = 2;

        score = 0;
        prevScore = 0;

        moves = 0;
        zeroes = 14;
        prevZeroes = 14;
        undoable = false;
    }

    public void up() {
        moves += 1;
        tempZeroes = prevZeroes;
        prevZeroes = zeroes;

        tempScore = prevScore;
        prevScore = score;

        undoable = true;

        int[] col = new int[4];
        int[] vcnt = new int[4];
        boolean same = true;
        int num, y_ind, c_ind, v;

        Log.d(TAG, "previous zeroes: " + prevZeroes);

        for (int i=0; i<4; i++)
            tempGrid[i] = prevGrid[i].clone();

        for (int i=0; i<4; i++)
            prevGrid[i] = grid[i].clone();

        for (int j=0; j<4; j++) {
            num = 0;
            for (int i=0; i<4; i++) {
                if (grid[i][j]!=0) {
                    col[num] = grid[i][j];
                    num++;
                    zeroes++;
                }

                grid[i][j] = 0;
            }

            y_ind = 0;
            c_ind = 0;

            while (c_ind<num) {
                if (c_ind<num-1 && col[c_ind]==col[c_ind+1]) {
                    grid[y_ind][j] = 2 * col[c_ind];
                    maxNum = max(maxNum, grid[y_ind][j]);
                    score += grid[y_ind][j];
                    c_ind += 2;
                }
                else {
                    grid[y_ind][j] = col[c_ind];
                    c_ind++;
                }
                y_ind++;
                zeroes--;
            }

            if (same) {
                for (int i=0; i<4; i++) {
                    if (prevGrid[i][j]!=grid[i][j]) {
                        same = false;
                        break;
                    }
                }

            }

            vcnt[j] = 4 - y_ind;
            Log.d(TAG, "column " + j + ": " + vcnt[j]);
        }
        Log.d(TAG, "zeroes: " + zeroes);

        if (zeroes>0 && !same) {
            v = mRng.nextInt(4);

            while (vcnt[v]==0)
                v = mRng.nextInt(4);

            if (mRng.nextInt(10)==0)
                grid[3-mRng.nextInt(vcnt[v])][v] = 4;
            else
                grid[3-mRng.nextInt(vcnt[v])][v] = 2;

            zeroes--;
        }
        else {
            if (moves>0) {
                moves--;
                prevZeroes = tempZeroes;
                prevScore = tempScore;
                for (int i = 0; i < 4; i++)
                    prevGrid[i] = tempGrid[i].clone();
            }
        }
        Log.d(TAG, "zeroes: " + zeroes);
    }

    public void down() {
        moves += 1;
        tempZeroes = prevZeroes;
        prevZeroes = zeroes;

        tempScore = prevScore;
        prevScore = score;

        undoable = true;

        int[] col = new int[4];
        int[] vcnt = new int[4];
        boolean same = true;
        int num, y_ind, c_ind, v;

        Log.d(TAG, "previous zeroes: " + prevZeroes);

        for (int i=0; i<4; i++)
            tempGrid[i] = prevGrid[i].clone();

        for (int i=0; i<4; i++)
            prevGrid[i] = grid[i].clone();

        for (int j=0; j<4; j++) {
            num = 0;
            for (int i=3; i>=0; i--) {
                if (grid[i][j]!=0) {
                    col[num] = grid[i][j];
                    num++;
                    zeroes++;
                }

                grid[i][j] = 0;
            }

            y_ind = 3;
            c_ind = 0;

            while (c_ind<num) {
                if (c_ind<num-1 && col[c_ind]==col[c_ind+1]) {
                    grid[y_ind][j] = 2 * col[c_ind];
                    maxNum = max(maxNum, grid[y_ind][j]);
                    score += grid[y_ind][j];
                    c_ind += 2;
                }
                else {
                    grid[y_ind][j] = col[c_ind];
                    c_ind++;
                }
                y_ind--;
                zeroes--;
            }

            if (same) {
                for (int i=0; i<4; i++) {
                    if (prevGrid[i][j]!=grid[i][j]) {
                        same = false;
                        break;
                    }
                }

            }

            vcnt[j] = y_ind + 1;
            Log.d(TAG, "column " + j + ": " + vcnt[j]);
        }
        Log.d(TAG, "zeroes: " + zeroes);

        if (zeroes>0  && !same) {
            v = mRng.nextInt(4);

            while (vcnt[v]==0)
                v = mRng.nextInt(4);

            if (mRng.nextInt(10)==0)
                grid[mRng.nextInt(vcnt[v])][v] = 4;
            else
                grid[mRng.nextInt(vcnt[v])][v] = 2;

            zeroes--;
        }
        else {
            if (moves>0) {
                moves--;
                prevZeroes = tempZeroes;
                prevScore = tempScore;
                for (int i = 0; i < 4; i++)
                    prevGrid[i] = tempGrid[i].clone();
            }
        }
        Log.d(TAG, "zeroes: " + zeroes);
    }

    public void left() {
        moves += 1;
        tempZeroes = prevZeroes;
        prevZeroes = zeroes;

        tempScore = prevScore;
        prevScore = score;

        undoable = true;

        int[] row = new int[4];
        int[] vcnt = new int[4];
        boolean same = true;
        int num, x_ind, r_ind, v;

        Log.d(TAG, "previous zeroes: " + prevZeroes);

        for (int i=0; i<4; i++)
            tempGrid[i] = prevGrid[i].clone();

        for (int i=0; i<4; i++)
            prevGrid[i] = grid[i].clone();

        for (int i=0; i<4; i++) {
            num = 0;
            for (int j=0; j<4; j++) {
                if (grid[i][j]!=0) {
                    row[num] = grid[i][j];
                    num++;
                    zeroes++;
                }

                grid[i][j] = 0;
            }

            x_ind = 0;
            r_ind = 0;

            while (r_ind<num) {
                if (r_ind<num-1 && row[r_ind]==row[r_ind+1]) {
                    grid[i][x_ind] = 2 * row[r_ind];
                    maxNum = max(maxNum, grid[i][x_ind]);
                    score += grid[i][x_ind];
                    r_ind += 2;
                }
                else {
                    grid[i][x_ind] = row[r_ind];
                    r_ind++;
                }
                x_ind++;
                zeroes--;
            }

            if (same) {
                for (int j=0; j<4; j++) {
                    if (prevGrid[i][j]!=grid[i][j]) {
                        same = false;
                        break;
                    }
                }

            }

            vcnt[i] = 4 - x_ind;
            Log.d(TAG, "row " + i + ": " + vcnt[i]);
        }
        Log.d(TAG, "zeroes: " + zeroes);

        if (zeroes>0  && !same) {
            v = mRng.nextInt(4);

            while (vcnt[v]==0)
                v = mRng.nextInt(4);

            if (mRng.nextInt(10)==0)
                grid[v][3-mRng.nextInt(vcnt[v])] = 4;
            else
                grid[v][3-mRng.nextInt(vcnt[v])] = 2;

            zeroes--;
        }
        else {
            if (moves>0) {
                moves--;
                prevZeroes = tempZeroes;
                prevScore = tempScore;
                for (int i = 0; i < 4; i++)
                    prevGrid[i] = tempGrid[i].clone();
            }
        }
        Log.d(TAG, "zeroes: " + zeroes);
    }

    public void right() {
        moves += 1;
        tempZeroes = prevZeroes;
        prevZeroes = zeroes;

        tempScore = prevScore;
        prevScore = score;

        undoable = true;

        int[] row = new int[4];
        int[] vcnt = new int[4];
        boolean same = true;
        int num, x_ind, r_ind, v;

        Log.d(TAG, "previous zeroes: " + prevZeroes);

        for (int i=0; i<4; i++)
            tempGrid[i] = prevGrid[i].clone();

        for (int i=0; i<4; i++)
            prevGrid[i] = grid[i].clone();

        for (int i=0; i<4; i++) {
            num = 0;
            for (int j=3; j>=0; j--) {
                if (grid[i][j]!=0) {
                    row[num] = grid[i][j];
                    num++;
                    zeroes++;
                }

                grid[i][j] = 0;
            }

            x_ind = 3;
            r_ind = 0;

            while (r_ind<num) {
                if (r_ind<num-1 && row[r_ind]==row[r_ind+1]) {
                    grid[i][x_ind] = 2 * row[r_ind];
                    maxNum = max(maxNum, grid[i][x_ind]);
                    score += grid[i][x_ind];
                    r_ind += 2;
                }
                else {
                    grid[i][x_ind] = row[r_ind];
                    r_ind++;
                }
                x_ind--;
                zeroes--;
            }

            if (same) {
                for (int j=0; j<4; j++) {
                    if (prevGrid[i][j]!=grid[i][j]) {
                        same = false;
                        break;
                    }
                }

            }

            vcnt[i] = x_ind + 1;
            Log.d(TAG, "row " + i + ": " + vcnt[i]);
        }
        Log.d(TAG, "zeroes: " + zeroes);

        if (zeroes>0  && !same) {
            v = mRng.nextInt(4);

            while (vcnt[v]==0)
                v = mRng.nextInt(4);

            if (mRng.nextInt(10)==0)
                grid[v][mRng.nextInt(vcnt[v])] = 4;
            else
                grid[v][mRng.nextInt(vcnt[v])] = 2;

            zeroes--;
        }
        else {
            if (moves>0) {
                moves--;
                prevZeroes = tempZeroes;
                prevScore = tempScore;
                for (int i = 0; i < 4; i++)
                    prevGrid[i] = tempGrid[i].clone();
            }
        }
        Log.d(TAG, "zeroes: " + zeroes);
    }

    public void undo() {
        if (moves>0 && undoable) {
            moves--;
            zeroes = prevZeroes;
            score = prevScore;
            undoable = false;

            for (int i=0; i<4; i++)
                for (int j=0; j<4; j++) {
                    grid[i][j] = prevGrid[i][j];
                    Log.d(TAG, "val " + i + ", " + j + " = " + grid[i][j]);
                }
        }
    }

    public void reset() {
        grid = new int[4][4];
        prevGrid = new int[4][4];

        for (int i=0; i<4; i++)
            for (int j=0; j<4; j++)
                grid[i][j] = 0;

        mRng = new Random();
        int x1 = mRng.nextInt(4);
        int y1 = mRng.nextInt(4);
        int x2 = mRng.nextInt(4);
        int y2 = mRng.nextInt(4);

        while (x1==x2 && y1==y2) {
            x2 = mRng.nextInt(4);
            y2 = mRng.nextInt(4);
        }

        if (mRng.nextInt(10)==0)
            grid[y1][x1] = 4;
        else
            grid[y1][x1] = 2;

        if (mRng.nextInt(10)==0)
            grid[y2][x2] = 4;
        else
            grid[y2][x2] = 2;

        score = 0;
        prevScore = 0;

        moves = 0;
        zeroes = 14;
        prevZeroes = 14;
        undoable = false;
    }

    public int[][] getGrid() {
        return grid;
    }

    public int getMax() {
        return maxNum;
    }

    public int getScore() {
        return score;
    }
}
