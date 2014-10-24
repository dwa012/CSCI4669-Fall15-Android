package edu.uno.csci.midterm;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class WindowFragment extends Fragment {

    public WindowFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_window, container, false);
        return rootView;
    }

    public void activateWindow() {
        getView().findViewById(R.id.window).setBackgroundResource(R.color.yellow);
    }

    public void deaciveWindow() {
        getView().findViewById(R.id.window).setBackgroundResource(R.color.black);
    }
}
