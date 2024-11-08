package com.eldroidfri730.extr.utils;

import android.content.Context;
import android.content.Intent;

public class IntentUtil {

    // Generic method to start an Activity
    public static void startActivity(Context context, Class<?> targetActivity) {
        Intent intent = new Intent(context, targetActivity);
        context.startActivity(intent);
    }
}
