package android.support.p004v7.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.p004v7.app.AlertController.AlertParams;
import android.support.p004v7.appcompat.C0187R;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListAdapter;

/* renamed from: android.support.v7.app.AlertDialog */
public class AlertDialog extends AppCompatDialog implements DialogInterface {
    final AlertController mAlert = new AlertController(getContext(), this, getWindow());

    /* renamed from: android.support.v7.app.AlertDialog$Builder */
    public static class Builder {
        /* renamed from: P */
        private final AlertParams f20P;
        private final int mTheme;

        public Builder(Context context) {
            this(context, AlertDialog.resolveDialogTheme(context, 0));
        }

        public Builder(Context context, int i) {
            this.f20P = new AlertParams(new ContextThemeWrapper(context, AlertDialog.resolveDialogTheme(context, i)));
            this.mTheme = i;
        }

        public Context getContext() {
            return this.f20P.mContext;
        }

        public Builder setTitle(int i) {
            this.f20P.mTitle = this.f20P.mContext.getText(i);
            return this;
        }

        public Builder setTitle(CharSequence charSequence) {
            this.f20P.mTitle = charSequence;
            return this;
        }

        public Builder setCustomTitle(View view) {
            this.f20P.mCustomTitleView = view;
            return this;
        }

        public Builder setMessage(CharSequence charSequence) {
            this.f20P.mMessage = charSequence;
            return this;
        }

        public Builder setIcon(Drawable drawable) {
            this.f20P.mIcon = drawable;
            return this;
        }

        public Builder setPositiveButton(int i, OnClickListener onClickListener) {
            this.f20P.mPositiveButtonText = this.f20P.mContext.getText(i);
            this.f20P.mPositiveButtonListener = onClickListener;
            return this;
        }

        public Builder setPositiveButton(CharSequence charSequence, OnClickListener onClickListener) {
            this.f20P.mPositiveButtonText = charSequence;
            this.f20P.mPositiveButtonListener = onClickListener;
            return this;
        }

        public Builder setNegativeButton(int i, OnClickListener onClickListener) {
            this.f20P.mNegativeButtonText = this.f20P.mContext.getText(i);
            this.f20P.mNegativeButtonListener = onClickListener;
            return this;
        }

        public Builder setNegativeButton(CharSequence charSequence, OnClickListener onClickListener) {
            this.f20P.mNegativeButtonText = charSequence;
            this.f20P.mNegativeButtonListener = onClickListener;
            return this;
        }

        public Builder setCancelable(boolean z) {
            this.f20P.mCancelable = z;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            this.f20P.mOnCancelListener = onCancelListener;
            return this;
        }

        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            this.f20P.mOnDismissListener = onDismissListener;
            return this;
        }

        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            this.f20P.mOnKeyListener = onKeyListener;
            return this;
        }

        public Builder setAdapter(ListAdapter listAdapter, OnClickListener onClickListener) {
            this.f20P.mAdapter = listAdapter;
            this.f20P.mOnClickListener = onClickListener;
            return this;
        }

        public Builder setView(View view) {
            this.f20P.mView = view;
            this.f20P.mViewLayoutResId = 0;
            this.f20P.mViewSpacingSpecified = false;
            return this;
        }

        public AlertDialog create() {
            AlertDialog alertDialog = new AlertDialog(this.f20P.mContext, this.mTheme);
            this.f20P.apply(alertDialog.mAlert);
            alertDialog.setCancelable(this.f20P.mCancelable);
            if (this.f20P.mCancelable) {
                alertDialog.setCanceledOnTouchOutside(true);
            }
            alertDialog.setOnCancelListener(this.f20P.mOnCancelListener);
            alertDialog.setOnDismissListener(this.f20P.mOnDismissListener);
            if (this.f20P.mOnKeyListener != null) {
                alertDialog.setOnKeyListener(this.f20P.mOnKeyListener);
            }
            return alertDialog;
        }
    }

    protected AlertDialog(Context context, int i) {
        super(context, AlertDialog.resolveDialogTheme(context, i));
    }

    static int resolveDialogTheme(Context context, int i) {
        if (((i >>> 24) & 255) >= 1) {
            return i;
        }
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(C0187R.attr.alertDialogTheme, typedValue, true);
        return typedValue.resourceId;
    }

    public void setTitle(CharSequence charSequence) {
        super.setTitle(charSequence);
        this.mAlert.setTitle(charSequence);
    }

    public void setView(View view) {
        this.mAlert.setView(view);
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mAlert.installContent();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (this.mAlert.onKeyDown(i, keyEvent)) {
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (this.mAlert.onKeyUp(i, keyEvent)) {
            return true;
        }
        return super.onKeyUp(i, keyEvent);
    }
}
