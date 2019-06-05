// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v7.app;

import android.support.v7.view.ActionMode;
import android.view.Window$Callback;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.support.v7.appcompat.R;
import android.util.TypedValue;
import android.os.Bundle;
import android.view.KeyEvent;
import android.content.Context;
import android.support.v4.view.KeyEventDispatcher;
import android.app.Dialog;

public class AppCompatDialog extends Dialog implements AppCompatCallback
{
    private AppCompatDelegate mDelegate;
    private final KeyEventDispatcher.Component mKeyDispatcher;
    
    public AppCompatDialog(final Context context, final int n) {
        super(context, getThemeResId(context, n));
        this.mKeyDispatcher = new KeyEventDispatcher.Component() {
            @Override
            public boolean superDispatchKeyEvent(final KeyEvent keyEvent) {
                return AppCompatDialog.this.superDispatchKeyEvent(keyEvent);
            }
        };
        this.getDelegate().onCreate(null);
        this.getDelegate().applyDayNight();
    }
    
    private static int getThemeResId(final Context context, final int n) {
        int resourceId = n;
        if (n == 0) {
            final TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.dialogTheme, typedValue, true);
            resourceId = typedValue.resourceId;
        }
        return resourceId;
    }
    
    public void addContentView(final View view, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        this.getDelegate().addContentView(view, viewGroup$LayoutParams);
    }
    
    public boolean dispatchKeyEvent(final KeyEvent keyEvent) {
        return KeyEventDispatcher.dispatchKeyEvent(this.mKeyDispatcher, this.getWindow().getDecorView(), (Window$Callback)this, keyEvent);
    }
    
    public <T extends View> T findViewById(final int n) {
        return this.getDelegate().findViewById(n);
    }
    
    public AppCompatDelegate getDelegate() {
        if (this.mDelegate == null) {
            this.mDelegate = AppCompatDelegate.create(this, this);
        }
        return this.mDelegate;
    }
    
    public void invalidateOptionsMenu() {
        this.getDelegate().invalidateOptionsMenu();
    }
    
    protected void onCreate(final Bundle bundle) {
        this.getDelegate().installViewFactory();
        super.onCreate(bundle);
        this.getDelegate().onCreate(bundle);
    }
    
    protected void onStop() {
        super.onStop();
        this.getDelegate().onStop();
    }
    
    public void onSupportActionModeFinished(final ActionMode actionMode) {
    }
    
    public void onSupportActionModeStarted(final ActionMode actionMode) {
    }
    
    public ActionMode onWindowStartingSupportActionMode(final ActionMode.Callback callback) {
        return null;
    }
    
    public void setContentView(final int contentView) {
        this.getDelegate().setContentView(contentView);
    }
    
    public void setContentView(final View contentView) {
        this.getDelegate().setContentView(contentView);
    }
    
    public void setContentView(final View view, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        this.getDelegate().setContentView(view, viewGroup$LayoutParams);
    }
    
    public void setTitle(final int title) {
        super.setTitle(title);
        this.getDelegate().setTitle(this.getContext().getString(title));
    }
    
    public void setTitle(final CharSequence charSequence) {
        super.setTitle(charSequence);
        this.getDelegate().setTitle(charSequence);
    }
    
    boolean superDispatchKeyEvent(final KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent);
    }
    
    public boolean supportRequestWindowFeature(final int n) {
        return this.getDelegate().requestWindowFeature(n);
    }
}
