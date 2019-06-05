package menion.android.whereyougo.gui.extension.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.p000v4.app.DialogFragment;

public abstract class CustomDialogFragment extends DialogFragment {
    public abstract Dialog createDialog(Bundle bundle);

    public boolean isDialogVisible() {
        return isAdded() && !isHidden();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        try {
            super.onActivityCreated(savedInstanceState);
        } catch (Exception e) {
            dismissAllowingStateLoss();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(shouldRetainInstance());
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = createDialog(savedInstanceState);
        if (dialog != null) {
            dialog.setCancelable(isCancelable());
        }
        return dialog;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }

    public void onDetach() {
        super.onDetach();
    }

    public boolean shouldRetainInstance() {
        return true;
    }
}
