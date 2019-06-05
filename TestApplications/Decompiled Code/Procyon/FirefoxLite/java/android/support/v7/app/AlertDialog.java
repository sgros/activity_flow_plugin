// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v7.app;

import android.content.DialogInterface$OnKeyListener;
import android.content.DialogInterface$OnDismissListener;
import android.content.DialogInterface$OnCancelListener;
import android.graphics.drawable.Drawable;
import android.content.DialogInterface$OnClickListener;
import android.widget.ListAdapter;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.KeyEvent;
import android.os.Bundle;
import android.support.v7.appcompat.R;
import android.util.TypedValue;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialog extends AppCompatDialog implements DialogInterface
{
    final AlertController mAlert;
    
    protected AlertDialog(final Context context, final int n) {
        super(context, resolveDialogTheme(context, n));
        this.mAlert = new AlertController(this.getContext(), this, this.getWindow());
    }
    
    static int resolveDialogTheme(final Context context, final int n) {
        if ((n >>> 24 & 0xFF) >= 1) {
            return n;
        }
        final TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.alertDialogTheme, typedValue, true);
        return typedValue.resourceId;
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.mAlert.installContent();
    }
    
    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        return this.mAlert.onKeyDown(n, keyEvent) || super.onKeyDown(n, keyEvent);
    }
    
    public boolean onKeyUp(final int n, final KeyEvent keyEvent) {
        return this.mAlert.onKeyUp(n, keyEvent) || super.onKeyUp(n, keyEvent);
    }
    
    @Override
    public void setTitle(final CharSequence charSequence) {
        super.setTitle(charSequence);
        this.mAlert.setTitle(charSequence);
    }
    
    public void setView(final View view) {
        this.mAlert.setView(view);
    }
    
    public static class Builder
    {
        private final AlertController.AlertParams P;
        private final int mTheme;
        
        public Builder(final Context context) {
            this(context, AlertDialog.resolveDialogTheme(context, 0));
        }
        
        public Builder(final Context context, final int mTheme) {
            this.P = new AlertController.AlertParams((Context)new ContextThemeWrapper(context, AlertDialog.resolveDialogTheme(context, mTheme)));
            this.mTheme = mTheme;
        }
        
        public AlertDialog create() {
            final AlertDialog alertDialog = new AlertDialog(this.P.mContext, this.mTheme);
            this.P.apply(alertDialog.mAlert);
            alertDialog.setCancelable(this.P.mCancelable);
            if (this.P.mCancelable) {
                alertDialog.setCanceledOnTouchOutside(true);
            }
            alertDialog.setOnCancelListener(this.P.mOnCancelListener);
            alertDialog.setOnDismissListener(this.P.mOnDismissListener);
            if (this.P.mOnKeyListener != null) {
                alertDialog.setOnKeyListener(this.P.mOnKeyListener);
            }
            return alertDialog;
        }
        
        public Context getContext() {
            return this.P.mContext;
        }
        
        public Builder setAdapter(final ListAdapter mAdapter, final DialogInterface$OnClickListener mOnClickListener) {
            this.P.mAdapter = mAdapter;
            this.P.mOnClickListener = mOnClickListener;
            return this;
        }
        
        public Builder setCancelable(final boolean mCancelable) {
            this.P.mCancelable = mCancelable;
            return this;
        }
        
        public Builder setCustomTitle(final View mCustomTitleView) {
            this.P.mCustomTitleView = mCustomTitleView;
            return this;
        }
        
        public Builder setIcon(final Drawable mIcon) {
            this.P.mIcon = mIcon;
            return this;
        }
        
        public Builder setMessage(final CharSequence mMessage) {
            this.P.mMessage = mMessage;
            return this;
        }
        
        public Builder setNegativeButton(final int n, final DialogInterface$OnClickListener mNegativeButtonListener) {
            this.P.mNegativeButtonText = this.P.mContext.getText(n);
            this.P.mNegativeButtonListener = mNegativeButtonListener;
            return this;
        }
        
        public Builder setNegativeButton(final CharSequence mNegativeButtonText, final DialogInterface$OnClickListener mNegativeButtonListener) {
            this.P.mNegativeButtonText = mNegativeButtonText;
            this.P.mNegativeButtonListener = mNegativeButtonListener;
            return this;
        }
        
        public Builder setOnCancelListener(final DialogInterface$OnCancelListener mOnCancelListener) {
            this.P.mOnCancelListener = mOnCancelListener;
            return this;
        }
        
        public Builder setOnDismissListener(final DialogInterface$OnDismissListener mOnDismissListener) {
            this.P.mOnDismissListener = mOnDismissListener;
            return this;
        }
        
        public Builder setOnKeyListener(final DialogInterface$OnKeyListener mOnKeyListener) {
            this.P.mOnKeyListener = mOnKeyListener;
            return this;
        }
        
        public Builder setPositiveButton(final int n, final DialogInterface$OnClickListener mPositiveButtonListener) {
            this.P.mPositiveButtonText = this.P.mContext.getText(n);
            this.P.mPositiveButtonListener = mPositiveButtonListener;
            return this;
        }
        
        public Builder setPositiveButton(final CharSequence mPositiveButtonText, final DialogInterface$OnClickListener mPositiveButtonListener) {
            this.P.mPositiveButtonText = mPositiveButtonText;
            this.P.mPositiveButtonListener = mPositiveButtonListener;
            return this;
        }
        
        public Builder setTitle(final int n) {
            this.P.mTitle = this.P.mContext.getText(n);
            return this;
        }
        
        public Builder setTitle(final CharSequence mTitle) {
            this.P.mTitle = mTitle;
            return this;
        }
        
        public Builder setView(final View mView) {
            this.P.mView = mView;
            this.P.mViewLayoutResId = 0;
            this.P.mViewSpacingSpecified = false;
            return this;
        }
    }
}
