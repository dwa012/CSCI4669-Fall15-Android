package edu.uno.csci.drward3.remotedatalist;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Post>> {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<List<Post>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Post>>(getContext()) {
            @Override
            public List<Post> loadInBackground() {
                List<Post> data = new ArrayList<>();

                try {
                    data =  Api.getPosts();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return data;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Post>> loader, List<Post> data) {
        if (data.size() > 0) {
            Log.d("remote_data_list", data.get(0).title);
        } else {
            Log.d("remote_data_list", "No data");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Post>> loader) {

    }
}
