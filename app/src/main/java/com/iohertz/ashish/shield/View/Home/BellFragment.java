package com.iohertz.ashish.shield.View.Home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iohertz.ashish.shield.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BellFragment extends Fragment {


    public BellFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bell, container, false);
    }

}
