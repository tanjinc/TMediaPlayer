package com.tanjinc.tmediaplayer.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;

/**
 * Created by tanjincheng on 16/8/28.
 */
public class AnimaUtils {
    private static final String TAG = "AnimaUtils";

    private static int sAnimaDuration = 350;
    private static Interpolator sInterpolator = PathInterpolatorCompat.create(0.33f, 0f, 0.33f, 1f);

    public static void setMask(final View view, final boolean show) {
        Log.d(TAG, "video setMask: " + show);
        int startColor = 0x000;
        int endColor = 0x4C000000;

        ObjectAnimator animator;

        if (show) {
            animator = ObjectAnimator.ofInt(view, "backgroundColor", startColor, endColor);
        } else {
            animator = ObjectAnimator.ofInt(view, "backgroundColor", endColor, startColor);
        }
        animator.setDuration(sAnimaDuration);
        animator.setInterpolator(sInterpolator);
        animator.setEvaluator(new ArgbEvaluator());
        animator.start();
    }

    public static void showControllerTranslate(final View view, boolean isTop) {
        final ObjectAnimator transAnimator;
        final ObjectAnimator alaphAnimator;
        final AnimatorSet animatorSet = new AnimatorSet();

        int distance = 30;

        transAnimator = ObjectAnimator.ofFloat(view, "translationY", isTop ? -distance:distance, 0);
        alaphAnimator = ObjectAnimator.ofFloat(view, "alpha", 0, 1);

        animatorSet.setDuration(sAnimaDuration);
        animatorSet.setInterpolator(sInterpolator);
        animatorSet.playTogether(transAnimator, alaphAnimator);
        animatorSet.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        view.setVisibility(View.VISIBLE);
    }

    public static void hideControllerTranslate(final View view, boolean isTop) {
        Log.d(TAG, "video hideControllerTranslate");
        final ObjectAnimator transAnimator;
        final ObjectAnimator alaphAnimator;
        final AnimatorSet animatorSet = new AnimatorSet();
        int distance = 30;
        transAnimator = ObjectAnimator.ofFloat(view, "translationY", 0, isTop ? -distance:distance);
        alaphAnimator = ObjectAnimator.ofFloat(view, "alpha", 1, 0);

        animatorSet.setDuration(sAnimaDuration);
        animatorSet.setInterpolator(sInterpolator);
        animatorSet.playTogether(transAnimator, alaphAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }
        });
        animatorSet.start();
    }
}
