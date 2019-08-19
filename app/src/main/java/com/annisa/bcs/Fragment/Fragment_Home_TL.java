package com.annisa.bcs.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annisa.bcs.R;

public class Fragment_Home_TL extends Fragment {
    private static final String TAG = Fragment_Home_TL.class.getSimpleName();

    public Fragment_Home_TL() {
        // Required empty public constructor
    }

    public static Fragment_Home_TL newInstance(String param1, String param2) {
        Fragment_Home_TL fragment = new Fragment_Home_TL();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_tl, container, false);

        return view;
    }
}
