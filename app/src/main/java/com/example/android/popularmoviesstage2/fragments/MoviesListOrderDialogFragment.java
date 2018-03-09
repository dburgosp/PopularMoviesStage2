package com.example.android.popularmoviesstage2.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.android.popularmoviesstage2.R;

public class MoviesListOrderDialogFragment extends DialogFragment {
    final String[] items = {"Español", "Inglés", "Francés"};
    OnSimpleDialogListener listener;

    /**
     * Override to build your own custom Dialog container.  This is typically
     * used to show an AlertDialog instead of a generic Dialog; when doing so,
     * onCreateView(LayoutInflater, ViewGroup, Bundle) does not need
     * to be implemented since the AlertDialog takes care of its own content.
     * <p>
     * <p>This method will be called after #onCreate(Bundle) and
     * before #onCreateView(LayoutInflater, ViewGroup, Bundle).  The
     * default implementation simply instantiates and returns a Dialog
     * class.
     * <p>
     * <p><em>Note: DialogFragment own the Dialog#setOnCancelListener
     * Dialog.setOnCancelListener and Dialog#setOnDismissListener
     * Dialog.setOnDismissListener callbacks.  You must not set them yourself.</em>
     * To find out about these events, override #onCancel(DialogInterface)
     * and #onDismiss(DialogInterface).</p>
     *
     * @param savedInstanceState The last saved instance state of the Fragment,
     *                           or null if this is a freshly created Fragment.
     * @return Return a new Dialog instance to be displayed by the Fragment.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set the dialog title
        builder.setTitle(R.string.list_order)
                .setSingleChoiceItems(items, -1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                Log.i("Dialogos", "Opción elegida: " + items[item]);
                            }
                        })
                // Set the action buttons
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        listener.onPossitiveButtonClick(MoviesListOrderDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onNegativeButtonClick(MoviesListOrderDialogFragment.this);
                    }
                });

        return builder.create();
    }

    /**
     * The activity that creates an instance of this dialog fragment must implement this interface
     * in order to receive event callbacks. Each method passes the DialogFragment in case the host
     * needs to query it.
     */
    public interface OnSimpleDialogListener {
        public void onPossitiveButtonClick(DialogFragment dialog);
        public void onNegativeButtonClick(DialogFragment dialog);
    }
}
