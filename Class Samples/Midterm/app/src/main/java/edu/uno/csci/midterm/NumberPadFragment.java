package edu.uno.csci.midterm;

import android.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * A placeholder fragment containing a simple view.
 */
public class NumberPadFragment extends Fragment {

    private MainActivity.MicrowaveStateListener listener;

    private long time = 0;
    private DecimalFormat decimalFormat = new DecimalFormat("##.##");
    CountDownTimer countDownTimer;

    public NumberPadFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_number_pad, container, false);
        return rootView;
    }

    public void startTimer() {

        countDownTimer = new CountDownTimer(time * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time = time - 1;
                updateDisplay();
            }

            @Override
            public void onFinish() {
                if (listener != null) {
                    listener.onDeactivate();
                }

                resetTime();
            }
        }.start();

        if (listener != null) {
            listener.onActivate();
        }
    }

    public void stopTimer() {

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public void resetTime() {

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        time = 0;
        updateDisplay();
    }

    public void setListener(MainActivity.MicrowaveStateListener listener) {
        this.listener = listener;
    }

    public void buttonClicked(View view) {
        switch (view.getId()) {
            case R.id.button0:
                addTime(0);
                break;

            case R.id.button1:
                addTime(1);
                break;

            case R.id.button2:
                addTime(2);
                break;

            case R.id.button3:
                addTime(3);
                break;

            case R.id.button4:
                addTime(4);
                break;

            case R.id.button5:
                addTime(5);
                break;

            case R.id.button6:
                addTime(6);
                break;

            case R.id.button7:
                addTime(7);
                break;

            case R.id.button8:
                addTime(8);
                break;

            case R.id.button9:
                addTime(9);
                break;
        }
    }

    private void addTime(int amount) {
        time = (time * 10) +  amount;
        updateDisplay();
    }

    private void updateDisplay(){
        ((TextView)getView().findViewById(R.id.time)).setText(decimalFormat.format(time));
    }

    public void setTime(long time) {
        resetTime();

        this.time = time/1000;
        updateDisplay();
    }

    public long getTime() {
        return time * 1000;
    }
}
