// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.gui.extension.dialog;

import android.os.Message;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public abstract class CustomDialogFragment extends DialogFragment
{
    public abstract Dialog createDialog(final Bundle p0);
    
    public boolean isDialogVisible() {
        return this.isAdded() && !this.isHidden();
    }
    
    @Override
    public void onActivityCreated(final Bundle bundle) {
        try {
            super.onActivityCreated(bundle);
        }
        catch (Exception ex) {
            this.dismissAllowingStateLoss();
        }
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setRetainInstance(this.shouldRetainInstance());
    }
    
    @Override
    public Dialog onCreateDialog(final Bundle bundle) {
        final Dialog dialog = this.createDialog(bundle);
        if (dialog != null) {
            dialog.setCancelable(this.isCancelable());
        }
        return dialog;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    
    @Override
    public void onDestroyView() {
        if (this.getDialog() != null && this.getRetainInstance()) {
            this.getDialog().setDismissMessage((Message)null);
        }
        super.onDestroyView();
    }
    
    @Override
    public void onDetach() {
        super.onDetach();
    }
    
    public boolean shouldRetainInstance() {
        return true;
    }
}
