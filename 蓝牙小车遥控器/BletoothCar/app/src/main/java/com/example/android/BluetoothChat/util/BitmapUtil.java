package com.example.android.BluetoothChat.util;

/**
 * Created by asus on 2017/10/16.
 */
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import java.io.ByteArrayOutputStream;

public class BitmapUtil {
    private static int mDesiredWidth;
    private static int mDesiredHeight;

    public BitmapUtil() {
    }

    public static Bitmap bytes2Bitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    public static byte[] bitmap2Bytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public static Drawable bitmap2Drawable(Bitmap bitmap) {
        return new BitmapDrawable(bitmap);
    }

    public static Bitmap drawable2Bitmap(Drawable drawable) {
        BitmapDrawable bd = (BitmapDrawable)drawable;
        return bd.getBitmap();
    }

    public static Bitmap rotateBitmap(Bitmap bmp, int degrees) {
        if(degrees != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate((float)degrees);
            return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        } else {
            return bmp;
        }
    }

    public static int getBitmapSize(Bitmap bitmap) {
        return VERSION.SDK_INT >= 19?bitmap.getAllocationByteCount():(VERSION.SDK_INT >= 12?bitmap.getByteCount():bitmap.getRowBytes() * bitmap.getHeight());
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options = getBestOptions(options, reqWidth, reqHeight);
        Bitmap src = BitmapFactory.decodeResource(res, resId, options);
        return createScaleBitmap(src, mDesiredWidth, mDesiredHeight);
    }

    public static Bitmap decodeSampledBitmapFromFile(String pathName, int reqWidth, int reqHeight) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options = getBestOptions(options, reqWidth, reqHeight);
        Bitmap src = BitmapFactory.decodeFile(pathName, options);
        return createScaleBitmap(src, mDesiredWidth, mDesiredHeight);
    }

    private static Options getBestOptions(Options options, int reqWidth, int reqHeight) {
        int actualWidth = options.outWidth;
        int actualHeight = options.outHeight;
        mDesiredWidth = getResizedDimension(reqWidth, reqHeight, actualWidth, actualHeight);
        mDesiredHeight = getResizedDimension(reqHeight, reqWidth, actualHeight, actualWidth);
        options.inSampleSize = calculateBestInSampleSize(actualWidth, actualHeight, mDesiredWidth, mDesiredHeight);
        options.inJustDecodeBounds = false;
        return options;
    }

    private static int getResizedDimension(int maxPrimary, int maxSecondary, int actualPrimary, int actualSecondary) {
        double ratio = (double)actualSecondary / (double)actualPrimary;
        int resized = maxPrimary;
        if((double)maxPrimary * ratio > (double)maxSecondary) {
            resized = (int)((double)maxSecondary / ratio);
        }

        return resized;
    }

    private static int calculateBestInSampleSize(int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
        double wr = (double)actualWidth / (double)desiredWidth;
        double hr = (double)actualHeight / (double)desiredHeight;
        double ratio = Math.min(wr, hr);

        float inSampleSize;
        for(inSampleSize = 1.0F; (double)(inSampleSize * 2.0F) <= ratio; inSampleSize *= 2.0F) {
            ;
        }

        return (int)inSampleSize;
    }

    private static Bitmap createScaleBitmap(Bitmap tempBitmap, int desiredWidth, int desiredHeight) {
        if(tempBitmap == null || tempBitmap.getWidth() <= desiredWidth && tempBitmap.getHeight() <= desiredHeight) {
            return tempBitmap;
        } else {
            Bitmap bitmap = Bitmap.createScaledBitmap(tempBitmap, desiredWidth, desiredHeight, true);
            tempBitmap.recycle();
            return bitmap;
        }
    }
}
