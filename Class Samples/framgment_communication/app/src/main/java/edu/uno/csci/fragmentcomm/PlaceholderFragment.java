package edu.uno.csci.fragmentcomm;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    private static final String TEXT_COLOR = "color";

    // ---------------------
    // Intefraces

    public interface CommChannel {
        public void causeChange();
    }

    // ----------------------

    private CommChannel channel;

    public static PlaceholderFragment newInstance() {
        return PlaceholderFragment.newInstance(0, null);
    }

    public static PlaceholderFragment newInstance(int colorId) {
        return PlaceholderFragment.newInstance(colorId, null);
    }

    public static PlaceholderFragment newInstance(CommChannel channel) {
        return PlaceholderFragment.newInstance(0, channel);
    }

    public static PlaceholderFragment newInstance(int colorId, CommChannel channel) {
        PlaceholderFragment fragment = new PlaceholderFragment();

        if (channel != null) {
            fragment.setChannel(channel);
        }

        if (colorId != 0) {
            Bundle args = new Bundle();
            args.putInt(TEXT_COLOR, colorId);

            fragment.setArguments(args);
        }

        return fragment;
    }

    public PlaceholderFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (this.channel == null) {
            this.channel = new CommChannel() {
                @Override
                public void causeChange() {
                    Intent intent = new Intent(getActivity(), MyActivity.class);
                    intent.putExtra(MyActivity.TEXT_COLOR, R.color.red);
                    getActivity().startActivity(intent);
                }
            };
        }
        else if (activity instanceof CommChannel) {
            this.channel = (CommChannel)activity;
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my, container, false);

        rootView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               PlaceholderFragment.this.channel.causeChange();
            }
        });



        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
           changeTxtColor(getArguments().getInt(TEXT_COLOR, android.R.color.black));
        }

    }

    public void changeTxtColor(int colorId) {
        TextView textView = (TextView) getView().findViewById(R.id.text);
        textView.setTextColor(getResources().getColor(colorId));
    }

    public void setChannel(CommChannel channel) {
        this.channel = channel;
    }
}
