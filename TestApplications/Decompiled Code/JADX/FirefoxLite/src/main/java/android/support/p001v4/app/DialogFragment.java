package android.support.p001v4.app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

/* renamed from: android.support.v4.app.DialogFragment */
public class DialogFragment extends Fragment implements OnCancelListener, OnDismissListener {
    int mBackStackId = -1;
    boolean mCancelable = true;
    Dialog mDialog;
    boolean mDismissed;
    boolean mShownByMe;
    boolean mShowsDialog = true;
    int mStyle = 0;
    int mTheme = 0;
    boolean mViewDestroyed;

    public void onCancel(DialogInterface dialogInterface) {
    }

    public void setStyle(int i, int i2) {
        this.mStyle = i;
        if (this.mStyle == 2 || this.mStyle == 3) {
            this.mTheme = 16973913;
        }
        if (i2 != 0) {
            this.mTheme = i2;
        }
    }

    public void show(FragmentManager fragmentManager, String str) {
        this.mDismissed = false;
        this.mShownByMe = true;
        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
        beginTransaction.add(this, str);
        beginTransaction.commit();
    }

    public void dismiss() {
        dismissInternal(false);
    }

    public void dismissAllowingStateLoss() {
        dismissInternal(true);
    }

    /* Access modifiers changed, original: 0000 */
    public void dismissInternal(boolean z) {
        if (!this.mDismissed) {
            this.mDismissed = true;
            this.mShownByMe = false;
            if (this.mDialog != null) {
                this.mDialog.dismiss();
            }
            this.mViewDestroyed = true;
            if (this.mBackStackId >= 0) {
                getFragmentManager().popBackStack(this.mBackStackId, 1);
                this.mBackStackId = -1;
            } else {
                FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
                beginTransaction.remove(this);
                if (z) {
                    beginTransaction.commitAllowingStateLoss();
                } else {
                    beginTransaction.commit();
                }
            }
        }
    }

    public Dialog getDialog() {
        return this.mDialog;
    }

    public int getTheme() {
        return this.mTheme;
    }

    public void setCancelable(boolean z) {
        this.mCancelable = z;
        if (this.mDialog != null) {
            this.mDialog.setCancelable(z);
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (!this.mShownByMe) {
            this.mDismissed = false;
        }
    }

    public void onDetach() {
        super.onDetach();
        if (!this.mShownByMe && !this.mDismissed) {
            this.mDismissed = true;
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mShowsDialog = this.mContainerId == 0;
        if (bundle != null) {
            this.mStyle = bundle.getInt("android:style", 0);
            this.mTheme = bundle.getInt("android:theme", 0);
            this.mCancelable = bundle.getBoolean("android:cancelable", true);
            this.mShowsDialog = bundle.getBoolean("android:showsDialog", this.mShowsDialog);
            this.mBackStackId = bundle.getInt("android:backStackId", -1);
        }
    }

    public LayoutInflater onGetLayoutInflater(Bundle bundle) {
        if (!this.mShowsDialog) {
            return super.onGetLayoutInflater(bundle);
        }
        this.mDialog = onCreateDialog(bundle);
        if (this.mDialog == null) {
            return (LayoutInflater) this.mHost.getContext().getSystemService("layout_inflater");
        }
        setupDialog(this.mDialog, this.mStyle);
        return (LayoutInflater) this.mDialog.getContext().getSystemService("layout_inflater");
    }

    public void setupDialog(Dialog dialog, int i) {
        switch (i) {
            case 1:
            case 2:
                break;
            case 3:
                dialog.getWindow().addFlags(24);
                break;
            default:
                return;
        }
        dialog.requestWindowFeature(1);
    }

    public Dialog onCreateDialog(Bundle bundle) {
        return new Dialog(getActivity(), getTheme());
    }

    public void onDismiss(DialogInterface dialogInterface) {
        if (!this.mViewDestroyed) {
            dismissInternal(true);
        }
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (this.mShowsDialog) {
            View view = getView();
            if (view != null) {
                if (view.getParent() == null) {
                    this.mDialog.setContentView(view);
                } else {
                    throw new IllegalStateException("DialogFragment can not be attached to a container view");
                }
            }
            FragmentActivity activity = getActivity();
            if (activity != null) {
                this.mDialog.setOwnerActivity(activity);
            }
            this.mDialog.setCancelable(this.mCancelable);
            this.mDialog.setOnCancelListener(this);
            this.mDialog.setOnDismissListener(this);
            if (bundle != null) {
                bundle = bundle.getBundle("android:savedDialogState");
                if (bundle != null) {
                    this.mDialog.onRestoreInstanceState(bundle);
                }
            }
        }
    }

    public void onStart() {
        super.onStart();
        if (this.mDialog != null) {
            this.mViewDestroyed = false;
            this.mDialog.show();
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (this.mDialog != null) {
            Bundle onSaveInstanceState = this.mDialog.onSaveInstanceState();
            if (onSaveInstanceState != null) {
                bundle.putBundle("android:savedDialogState", onSaveInstanceState);
            }
        }
        if (this.mStyle != 0) {
            bundle.putInt("android:style", this.mStyle);
        }
        if (this.mTheme != 0) {
            bundle.putInt("android:theme", this.mTheme);
        }
        if (!this.mCancelable) {
            bundle.putBoolean("android:cancelable", this.mCancelable);
        }
        if (!this.mShowsDialog) {
            bundle.putBoolean("android:showsDialog", this.mShowsDialog);
        }
        if (this.mBackStackId != -1) {
            bundle.putInt("android:backStackId", this.mBackStackId);
        }
    }

    public void onStop() {
        super.onStop();
        if (this.mDialog != null) {
            this.mDialog.hide();
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (this.mDialog != null) {
            this.mViewDestroyed = true;
            this.mDialog.dismiss();
            this.mDialog = null;
        }
    }
}
