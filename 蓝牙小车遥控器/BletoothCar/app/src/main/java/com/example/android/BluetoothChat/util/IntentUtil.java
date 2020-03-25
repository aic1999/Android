package com.example.android.BluetoothChat.util;

/**
 * Created by asus on 2017/10/16.
 */

import android.content.Intent;

public class IntentUtil {
    public IntentUtil() {
    }

    public static boolean isBundleEmpty(Intent intent) {
        return intent == null && intent.getExtras() == null;
    }
}