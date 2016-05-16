package com.tanjinc.tmediaplayer.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by tanjincheng on 16/5/16.
 */
public class ImageUtil {
    public static void loadLoalImage(Context context, String file, int width, int height, ImageView imageView, int radius) {
        Glide.with(context)
                .load(file)
                .override(width, height)
                .centerCrop()
                .into(imageView);
    }
}
