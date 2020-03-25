package com.example.android.BluetoothChat.util;

/**
 * Created by asus on 2017/10/16.
 */


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.File;

public class GetSimplePhotoHelper {
    public static final int FROM_ALBUM = 0;
    public static final int FROM_CAMERA = 1;
    private Activity mActivity;
    private String mPicFilePath;
    private int mFromWay;
    private static GetSimplePhotoHelper instance;
    private GetSimplePhotoHelper.OnSelectedPhotoListener mListener;

    private GetSimplePhotoHelper(Activity activity) {
        this.mActivity = activity;
    }

    public static GetSimplePhotoHelper getInstance(Activity activity) {
        if(instance == null) {
            instance = new GetSimplePhotoHelper(activity);
        }

        return instance;
    }

    public void choicePhoto(  int way,    String picFilePath, GetSimplePhotoHelper.OnSelectedPhotoListener listener) {
        this.mFromWay = way;
        this.mPicFilePath = picFilePath;
        if(way == 0) {
            this.choicePhotoFromAlbum();
        } else if(way == 1) {
            this.choicePhotoFromCamera(picFilePath);
        }

        this.mListener = listener;
    }

    private void choicePhotoFromAlbum() {
        Intent intent = new Intent(this.mActivity, GetSimplePhotoActivity.class);
        intent.putExtra("key_from_way", '푉');
        this.mActivity.startActivityForResult(intent, 0);
    }

    private void choicePhotoFromCamera(String picFilePath) {
        Intent intent = new Intent(this.mActivity, GetSimplePhotoActivity.class);
        intent.putExtra("key_from_way", '똨');
        intent.putExtra("key_photo_path", picFilePath);
        this.mActivity.startActivityForResult(intent, 0);
    }

    protected void getSelectedPhoto(Uri uri) {
        Bitmap bitmap = BitmapFactory.decodeFile(uri.toString());
        if(bitmap != null) {
            bitmap = BitmapUtil.rotateBitmap(bitmap, GetSimplePhotoUtil.getPhotoDegreeByUri(uri));
        }

        SimplePhoto photo = new SimplePhoto();
        photo.bitmap = bitmap;
        photo.uri = uri;
        photo.degree = GetSimplePhotoUtil.getPhotoDegreeByUri(uri);
        if(this.mFromWay == 1 && this.mPicFilePath == null) {
            File tempPicFile = new File(uri.toString());
            if(tempPicFile != null) {
                tempPicFile.delete();
            }
        }

        this.mListener.onSelectedPhoto(this.mFromWay, photo);
    }

    public interface OnSelectedPhotoListener {
        void onSelectedPhoto(int var1, SimplePhoto var2);
    }
}
