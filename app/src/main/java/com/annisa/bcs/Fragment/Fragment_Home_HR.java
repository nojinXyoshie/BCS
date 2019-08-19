package com.annisa.bcs.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annisa.bcs.R;

public class Fragment_Home_HR extends Fragment {
    private static final String TAG = Fragment_Home_HR.class.getSimpleName();

    public Fragment_Home_HR() {
        // Required empty public constructor
    }

    public static Fragment_Home_HR newInstance(String param1, String param2) {
        Fragment_Home_HR fragment = new Fragment_Home_HR();
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
        View view = inflater.inflate(R.layout.fragment_home_hr, container, false);

        return view;
    }
}
