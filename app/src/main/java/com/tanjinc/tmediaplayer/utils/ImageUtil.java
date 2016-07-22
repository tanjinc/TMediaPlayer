package com.tanjinc.tmediaplayer.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by tanjincheng on 16/5/16.
 */
public class ImageUtil {
    public static void loadLoalImage(Context context, String file, int width, int height, ImageView imageView, int radius) {
        Glide.with(context)
                .load(file)
                .override(width, height)
                .centerCrop()
                .transform(new GlideRoundTransform(context, file, width, height, radius))
                .into(imageView);
    }

    static class GlideRoundTransform extends BitmapTransformation {

        int radius;
        int width;
        int height;

        public GlideRoundTransform(Context context, String file, int w, int h, int r) {
            super(context);
            width = w;
            height = h;
            radius = r;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

        @Override
        public String getId() {
            return getClass().getName() + Math.round(radius);
        }

        Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;
            Paint paint = new Paint();

            //产生一个同样大小的画布
            Bitmap result = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);

//            paint.setAntiAlias(true);
//            paint.setFilterBitmap(true);
//            paint.setXfermode(null);
//            //绘制圆角矩形
//            RectF rectF = new RectF(0f, 0f, width, height);
//            canvas.drawRoundRect(rectF, radius, radius, paint);
//
//            //两个绘制叠加后去交集
//            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//            canvas.drawBitmap(result,  0, 0, paint);



            int bmWidth = source.getWidth();
            int bmHeight = source.getHeight();

            int left = 0;
            int top = 0;
            int right = bmWidth;
            int bottom = bmHeight;

            final Rect rect = new Rect(left, top, right, bottom);
            final RectF rectF = new RectF(0, 0, width, height);

            if (radius > 0) {
                paint.setColor(0xff424242);
                canvas.drawARGB(0, 0, 0, 0);
                paint.setXfermode(null);
                canvas.drawRoundRect(rectF, radius, radius, paint);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            }

            canvas.drawBitmap(source, rect, rectF, paint);
            return result;
        }
    }
}
