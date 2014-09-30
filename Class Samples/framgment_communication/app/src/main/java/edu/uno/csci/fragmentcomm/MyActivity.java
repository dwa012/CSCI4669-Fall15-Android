package edu.uno.csci.fragmentcomm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MyActivity extends Activity {

    public static final String TEXT_COLOR = "color";

    PlaceholderFragment fragment1;
    PlaceholderFragment fragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        if (savedInstanceState == null) {

            int colorId = getIntent().getIntExtra(TEXT_COLOR, android.R.color.black);

            if (findViewById(R.id.container2) != null) {

                fragment1 = PlaceholderFragment.newInstance(colorId, new PlaceholderFragment.CommChannel() {
                    @Override
                    public void causeChange() {
                        fragment2.changeTxtColor(R.color.green);
                    }
                });

                fragment2 = PlaceholderFragment.newInstance(colorId, new PlaceholderFragment.CommChannel() {
                    @Override
                    public void causeChange() {
                        fragment1.changeTxtColor(R.color.red);
                    }
                });


                getFragmentManager().beginTransaction()
                        .add(R.id.container, fragment1)
                        .commit();

                getFragmentManager().beginTransaction()
                        .add(R.id.container2, fragment2)
                        .commit();
            } else {
                fragment1 = PlaceholderFragment.newInstance(colorId, new PlaceholderFragment.CommChannel() {
                    @Override
                    public void causeChange() {
                        Intent intent = new Intent(MyActivity.this, MyActivity.class);
                        intent.putExtra(MyActivity.TEXT_COLOR, R.color.green);
                        MyActivity.this.startActivity(intent);
                    }
                });

                getFragmentManager().beginTransaction()
                        .add(R.id.container, fragment1)
                        .commit();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
