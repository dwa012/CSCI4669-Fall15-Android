package edu.uno.csci.drward3.fragmentstuff;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailActivity extends AppCompatActivity {

    static final String COLOR_ARG = "color_arg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // should use the getExtras to fetch the data put in via
        // Intent.putExtra()
        int color = getIntent().getExtras().getInt(COLOR_ARG, 0);
        Fragment fragment;

        if (color != 0) {
            fragment = DetailFragment.newInstance(color);
        } else {
            fragment = new DetailFragment();
        }

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_holder, fragment)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}
