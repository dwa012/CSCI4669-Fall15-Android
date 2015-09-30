package edu.uno.csci.drward3.fragmentstuff;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String COLOR_PARAM = "param1";

    private int color;
    private FrameLayout containerLayout;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param color The Color to make the fragment background.
     * @return A new instance of fragment DetailFragment.
     */
    public static DetailFragment newInstance(int color) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(COLOR_PARAM, color);
        fragment.setArguments(args);
        return fragment;
    }

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            color = getArguments().getInt(COLOR_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        containerLayout = (FrameLayout) view.findViewById(R.id.container);

        if (color != 0) {
            containerLayout.setBackgroundColor(color);
        }

        return view;
    }

    public void updateBackgroundColor(int color) {
        containerLayout.setBackgroundColor(color);
    }


}
