package com.iohertz.ashish.shield.View.Home;


import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iohertz.ashish.shield.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LockFragment extends Fragment {

    private ImageView image;
    private AnimationDrawable lockAnimation;
    private AnimationDrawable unlockAnimation;
    private boolean state = true;
    private TextView hometext;

    public LockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        image = (ImageView) rootView.findViewById(R.id.imageView);
        image.setBackgroundResource(R.drawable.lock_animation);
        lockAnimation = (AnimationDrawable) image.getBackground();
        hometext = (TextView) rootView.findViewById(R.id.home_textview);
        hometext.setText(R.string.door_locked);

        //hometext.setText(Html.fromHtml("Press the button below to <b>unlock the door</b>"));

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state) {
                    image.setBackgroundResource(R.drawable.lock_animation);
                    lockAnimation = (AnimationDrawable) image.getBackground();
                    lockAnimation.start();
                    hometext.setText(R.string.door_unlocked);
                    Toast.makeText(getContext(), "Door Unlocked", Toast.LENGTH_LONG).show();
                    state = false;

                } else {
                    image.setBackgroundResource(R.drawable.unlockanimation);
                    unlockAnimation = (AnimationDrawable) image.getBackground();
                    unlockAnimation.start();
                    hometext.setText(R.string.door_locked);
                    Toast.makeText(getContext(), "Door Locked", Toast.LENGTH_LONG).show();
                    state = true;
                }
            }
        });
        return rootView;
    }
}
