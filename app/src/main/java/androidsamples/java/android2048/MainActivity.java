package androidsamples.java.android2048;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout main2048;
    SwipeListener swipeListener;
    TextView[][] btn;
    TextView txtScore;
    Button btnUndo, btnReset;
    int[][] grid;

    Context context;
    Toast toast;
    boolean win;

    String TAG = "2048";

    private MainViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        btn = new TextView[4][4];

        btn[0][0] = findViewById(R.id.btn_1_1);
        btn[0][1] = findViewById(R.id.btn_1_2);
        btn[0][2] = findViewById(R.id.btn_1_3);
        btn[0][3] = findViewById(R.id.btn_1_4);

        btn[1][0] = findViewById(R.id.btn_2_1);
        btn[1][1] = findViewById(R.id.btn_2_2);
        btn[1][2] = findViewById(R.id.btn_2_3);
        btn[1][3] = findViewById(R.id.btn_2_4);

        btn[2][0] = findViewById(R.id.btn_3_1);
        btn[2][1] = findViewById(R.id.btn_3_2);
        btn[2][2] = findViewById(R.id.btn_3_3);
        btn[2][3] = findViewById(R.id.btn_3_4);

        btn[3][0] = findViewById(R.id.btn_4_1);
        btn[3][1] = findViewById(R.id.btn_4_2);
        btn[3][2] = findViewById(R.id.btn_4_3);
        btn[3][3] = findViewById(R.id.btn_4_4);

        btnUndo = findViewById(R.id.btn_undo);
        btnReset = findViewById(R.id.btn_reset);

        txtScore = findViewById(R.id.txt_score);

        main2048 = findViewById(R.id.main_view);
        Log.d(TAG, "1");

        vm = new ViewModelProvider(this).get(MainViewModel.class);

        context = getApplicationContext();
        toast = Toast.makeText(context, getResources().getText(R.string.success), Toast.LENGTH_SHORT);
        win = false;

        Log.d(TAG, "2");
        updateUI();
        Log.d(TAG, "3");

        swipeListener = new SwipeListener(main2048);

        btnUndo.setOnClickListener(v -> {
            vm.undo();
            updateUI();
        });

        btnReset.setOnClickListener(v -> {
            vm.reset();
            updateUI();
        });
    }

    private class SwipeListener implements View.OnTouchListener {
        GestureDetector gestureDetector;

        SwipeListener(View view) {
            int threshold = 100;
            int velocity_threshold = 100;

            GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    float distX = e2.getX() - e1.getX();
                    float distY = e2.getY() - e1.getY();

                    try {
                        if (Math.abs(distX) > Math.abs(distY)) {
                            if (Math.abs(distX)>threshold && Math.abs(velocityX)>velocity_threshold) {
                                if (distX>0) {
                                    Log.d(TAG, "right");
                                    vm.right();
                                }
                                else {
                                    Log.d(TAG, "left");
                                    vm.left();
                                }
                                updateUI();
                                if (!win && vm.getMax()==2048) {
                                    win = true;
                                    toast.show();
                                }
                                return true;
                            }
                        }
                        else {
                            if (Math.abs(distY)>threshold && Math.abs(velocityY)>velocity_threshold) {
                                if (distY>0) {
                                    Log.d(TAG, "down");
                                    vm.down();
                                }
                                else {
                                    Log.d(TAG, "up");
                                    vm.up();
                                }
                                updateUI();
                                if (!win && vm.getMax()==2048) {
                                    win = true;
                                    toast.show();
                                }
                                return true;
                            }
                        }
                        return false;
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            };

            gestureDetector = new GestureDetector(listener);
            view.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }
    }

    public void updateUI() {
        grid = vm.getGrid();

        for (int i=0; i<4; i++)
            for (int j=0; j<4; j++) {
                switch (grid[i][j]) {
                    case 0: btn[i][j].setBackgroundColor(getResources().getColor(R.color.grey));
                        break;
                    case 2: btn[i][j].setBackgroundColor(getResources().getColor(R.color.dark_grey));
                        break;
                    case 4: btn[i][j].setBackgroundColor(getResources().getColor(R.color.rr));
                        break;
                    case 8: btn[i][j].setBackgroundColor(getResources().getColor(R.color.rg));
                        break;
                    case 16: btn[i][j].setBackgroundColor(getResources().getColor(R.color.gr));
                        break;
                    case 32: btn[i][j].setBackgroundColor(getResources().getColor(R.color.gg));
                        break;
                    case 64: btn[i][j].setBackgroundColor(getResources().getColor(R.color.gb));
                        break;
                    case 128: btn[i][j].setBackgroundColor(getResources().getColor(R.color.bg));
                        break;
                    case 256: btn[i][j].setBackgroundColor(getResources().getColor(R.color.bb));
                        break;
                    case 512: btn[i][j].setBackgroundColor(getResources().getColor(R.color.br));
                        break;
                    case 1024: btn[i][j].setBackgroundColor(getResources().getColor(R.color.rb));
                        break;
                    default: btn[i][j].setBackgroundColor(getResources().getColor(R.color.white));
                        break;
                }

                if (grid[i][j]>1024)
                    btn[i][j].setTextColor(getResources().getColor(R.color.black));
                else
                    btn[i][j].setTextColor(getResources().getColor(R.color.white));

                if (grid[i][j]==0)
                    btn[i][j].setText("");
                else
                    btn[i][j].setText(Integer.toString(grid[i][j]));

                txtScore.setText(Integer.toString(vm.getScore()));
            }
    }
}