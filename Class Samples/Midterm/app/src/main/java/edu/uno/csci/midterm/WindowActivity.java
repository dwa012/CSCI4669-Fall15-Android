package edu.uno.csci.midterm;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;


public class WindowActivity extends Activity {

    public static final String TIME_ARG = "time";

    private long timeRemaining;

    WindowFragment windowFragment;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window);

        windowFragment = (WindowFragment) getFragmentManager().findFragmentById(R.id.window_fragment);
    }

    @Override
    protected void onStart() {
        super.onStart();

        windowFragment.activateWindow();

        timeRemaining = getIntent().getLongExtra(TIME_ARG, 0);

        countDownTimer = new CountDownTimer(timeRemaining, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = timeRemaining - 1000;
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent();
                intent.putExtra(TIME_ARG, 0);
                WindowActivity.this.setResult(0, intent);
                WindowActivity.this.finish();
            }
        }.start();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(TIME_ARG, timeRemaining);
        this.setResult(0, intent);
        this.finish();
    }

}
