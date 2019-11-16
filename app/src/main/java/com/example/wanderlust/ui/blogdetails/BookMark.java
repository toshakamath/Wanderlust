package com.example.wanderlust.ui.blogdetails;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class BookMark {

    private static final String TAG = "BookMark";

    private  static final DecelerateInterpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();

    ImageView blackBookMark;
    ImageView whitleBookMark;

    public BookMark(ImageView blackBookMark, ImageView whitleBookMark) {
        this.blackBookMark = blackBookMark;
        this.whitleBookMark = whitleBookMark;
    }

    public void toggleBookMark() {
        Log.i(TAG, "toggleBookMark : called");

        AnimatorSet animatorSet = new AnimatorSet();

        if(blackBookMark.getVisibility() == View.VISIBLE){
            Log.i(TAG, "toggle red heart off");

            blackBookMark.setScaleX(0.1f);
            blackBookMark.setScaleY(0.1f);

            ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(blackBookMark, "scaleY", 1f, 0f);
            scaleDownY.setDuration(300);
            scaleDownY.setInterpolator(ACCELERATE_INTERPOLATOR);

            ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(blackBookMark, "scaleX", 1f, 0f);
            scaleDownX.setDuration(300);
            scaleDownX.setInterpolator(ACCELERATE_INTERPOLATOR);

            blackBookMark.setVisibility(View.GONE);
            whitleBookMark.setVisibility(View.VISIBLE);

            animatorSet.playTogether(scaleDownY, scaleDownX);
        }

        else if(blackBookMark.getVisibility() == View.GONE){
            Log.i(TAG, "toggle red heart on");

            blackBookMark.setScaleX(0.1f);
            blackBookMark.setScaleY(0.1f);

            ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(blackBookMark, "scaleY", 0.1f, 1f);
            scaleDownY.setDuration(300);
            scaleDownY.setInterpolator(DECELERATE_INTERPOLATOR);

            ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(blackBookMark, "scaleX", 0.1f, 1f);
            scaleDownX.setDuration(300);
            scaleDownX.setInterpolator(DECELERATE_INTERPOLATOR);

            blackBookMark.setVisibility(View.VISIBLE);
            whitleBookMark.setVisibility(View.GONE);

            animatorSet.playTogether(scaleDownY, scaleDownX);
        }

        animatorSet.start();
    }
}
