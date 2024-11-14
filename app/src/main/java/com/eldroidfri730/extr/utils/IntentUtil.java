package com.eldroidfri730.extr.utils;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

public class IntentUtil {

    // Method to start a new activity
    public static void startActivity(Context context, Class<?> targetActivity) {
        Intent intent = new Intent(context, targetActivity);
        context.startActivity(intent);
    }

    // Method to replace the current fragment with a new one
    public static void replaceFragment(FragmentActivity activity, Fragment fragment, String tag) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(android.R.id.content, fragment, tag);  // Replace with container view ID if needed
        transaction.addToBackStack(tag);  // Add fragment to backstack so user can navigate back
        transaction.commit();
    }

    // Method to add a new fragment to the container
    public static void addFragment(FragmentActivity activity, Fragment fragment, String tag, int containerViewId) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.add(containerViewId, fragment, tag);
        transaction.addToBackStack(tag);  // Add to backstack to allow navigation back
        transaction.commit();
    }

    // Method to remove a fragment from the container
    public static void removeFragment(FragmentActivity activity, Fragment fragment) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
    }

    // Method to pop the top fragment from the back stack
    public static void popBackStack(FragmentActivity activity) {
        if (activity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
            activity.getSupportFragmentManager().popBackStack();
        }
    }
}
