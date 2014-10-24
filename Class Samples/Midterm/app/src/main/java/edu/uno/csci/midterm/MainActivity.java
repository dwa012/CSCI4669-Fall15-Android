package edu.uno.csci.midterm;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity {

    NumberPadFragment numberPadFragment;
    WindowFragment windowFragment;

    boolean isPortrait;

    public interface MicrowaveStateListener {
        public void onActivate ();
        public void onDeactivate ();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberPadFragment = (NumberPadFragment) getFragmentManager().findFragmentById(R.id.number_pad_fragment);
        windowFragment = (WindowFragment) getFragmentManager().findFragmentById(R.id.window_fragment);

        isPortrait = (windowFragment == null);

        if (!isPortrait) {
            numberPadFragment.setListener(new MicrowaveStateListener() {
                @Override
                public void onActivate() {
                    windowFragment.activateWindow();
                }

                @Override
                public void onDeactivate() {
                    windowFragment.deaciveWindow();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        long timeRemaining = data.getLongExtra(WindowActivity.TIME_ARG, 0);
        numberPadFragment.setTime(timeRemaining);
    }

    public void buttonClicked(View view) {
       if (view.getId() == R.id.button_start) {
           if (isPortrait) {
               Intent intent = new Intent(this, WindowActivity.class);
               intent.putExtra(WindowActivity.TIME_ARG, numberPadFragment.getTime());
               startActivityForResult(intent, 0);
           } else {
               numberPadFragment.startTimer();
           }
       }

       else if (view.getId() == R.id.button_stop) {
           if (isPortrait) {
               numberPadFragment.stopTimer();
           }
       }

       else if (view.getId() == R.id.button_reset) {
            numberPadFragment.resetTime();
       }

       else {
            numberPadFragment.buttonClicked(view);
       }
    }

}
