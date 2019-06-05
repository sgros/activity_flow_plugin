// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.web;

import android.support.v4.content.ContextCompat;
import android.widget.TextView$OnEditorActionListener;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.KeyEvent;
import android.content.DialogInterface;
import android.view.Window;
import android.content.DialogInterface$OnCancelListener;
import android.content.DialogInterface$OnClickListener;
import android.app.AlertDialog$Builder;
import android.view.View;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.Context;

public class HttpAuthenticationDialogBuilder
{
    private CancelListener cancelListener;
    private final Context context;
    private AlertDialog dialog;
    private final String host;
    private OkListener okListener;
    private TextView passwordTextView;
    private final String realm;
    private TextView usernameTextView;
    
    HttpAuthenticationDialogBuilder(final Builder builder) {
        this.context = builder.context;
        this.host = builder.host;
        this.realm = builder.realm;
        this.okListener = builder.okListener;
        this.cancelListener = builder.cancelListener;
    }
    
    private void buildDialog(final View view) {
        this.dialog = new AlertDialog$Builder(this.context).setIconAttribute(16843605).setView(view).setPositiveButton(2131755061, (DialogInterface$OnClickListener)new _$$Lambda$HttpAuthenticationDialogBuilder$k_Dm8CgRytlnguX9ShhoWY4uc8Q(this)).setNegativeButton(2131755060, (DialogInterface$OnClickListener)new _$$Lambda$HttpAuthenticationDialogBuilder$79_L6_eARRUsO3aVMA40B2wdxh4(this)).setOnCancelListener((DialogInterface$OnCancelListener)new _$$Lambda$HttpAuthenticationDialogBuilder$OZ8WI06CBP8E2_wL0EbbCuZ14wQ(this)).create();
        final Window window = this.dialog.getWindow();
        if (window != null) {
            window.setSoftInputMode(4);
        }
    }
    
    private String getPassword() {
        return this.passwordTextView.getText().toString();
    }
    
    private String getUsername() {
        return this.usernameTextView.getText().toString();
    }
    
    public void createDialog() {
        final View inflate = LayoutInflater.from(this.context).inflate(2131492942, (ViewGroup)null);
        this.usernameTextView = (TextView)inflate.findViewById(2131296472);
        (this.passwordTextView = (TextView)inflate.findViewById(2131296471)).setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$HttpAuthenticationDialogBuilder$OWVDbtV3vCIyTwaiMgH1w9Yb7EU(this));
        this.buildDialog(inflate);
    }
    
    public void show() {
        this.dialog.show();
        final int color = ContextCompat.getColor(this.context, 2131099832);
        this.dialog.getButton(-1).setTextColor(color);
        this.dialog.getButton(-2).setTextColor(color);
        this.usernameTextView.requestFocus();
    }
    
    public static class Builder
    {
        private CancelListener cancelListener;
        private final Context context;
        private final String host;
        private OkListener okListener;
        private final String realm;
        
        public Builder(final Context context, final String host, final String realm) {
            this.context = context;
            this.host = host;
            this.realm = realm;
        }
        
        public HttpAuthenticationDialogBuilder build() {
            return new HttpAuthenticationDialogBuilder(this);
        }
        
        public Builder setCancelListener(final CancelListener cancelListener) {
            this.cancelListener = cancelListener;
            return this;
        }
        
        public Builder setOkListener(final OkListener okListener) {
            this.okListener = okListener;
            return this;
        }
    }
    
    public interface CancelListener
    {
        void onCancel();
    }
    
    public interface OkListener
    {
        void onOk(final String p0, final String p1, final String p2, final String p3);
    }
}
