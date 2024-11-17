package com.eldroidfri730.extr.ui.home;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class LogoutDialogueFragment extends DialogFragment {

    public interface LogoutDialogListener {
        void onLogoutConfirmed();
    }

    private LogoutDialogListener listener;

    public void setLogoutDialogListener(LogoutDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (listener != null) {
                        listener.onLogoutConfirmed();
                    }
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}