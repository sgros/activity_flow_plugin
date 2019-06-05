package org.mozilla.focus.web;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.p001v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import org.mozilla.focus.C0427R;
import org.mozilla.rocket.C0769R;

public class HttpAuthenticationDialogBuilder {
    private CancelListener cancelListener;
    private final Context context;
    private AlertDialog dialog;
    private final String host;
    private OkListener okListener;
    private TextView passwordTextView;
    private final String realm;
    private TextView usernameTextView;

    public static class Builder {
        private CancelListener cancelListener;
        private final Context context;
        private final String host;
        private OkListener okListener;
        private final String realm;

        public Builder(Context context, String str, String str2) {
            this.context = context;
            this.host = str;
            this.realm = str2;
        }

        public Builder setOkListener(OkListener okListener) {
            this.okListener = okListener;
            return this;
        }

        public Builder setCancelListener(CancelListener cancelListener) {
            this.cancelListener = cancelListener;
            return this;
        }

        public HttpAuthenticationDialogBuilder build() {
            return new HttpAuthenticationDialogBuilder(this);
        }
    }

    public interface CancelListener {
        void onCancel();
    }

    public interface OkListener {
        void onOk(String str, String str2, String str3, String str4);
    }

    HttpAuthenticationDialogBuilder(Builder builder) {
        this.context = builder.context;
        this.host = builder.host;
        this.realm = builder.realm;
        this.okListener = builder.okListener;
        this.cancelListener = builder.cancelListener;
    }

    private String getUsername() {
        return this.usernameTextView.getText().toString();
    }

    private String getPassword() {
        return this.passwordTextView.getText().toString();
    }

    public void show() {
        this.dialog.show();
        int color = ContextCompat.getColor(this.context, C0769R.color.paletteDarkBlueC100);
        this.dialog.getButton(-1).setTextColor(color);
        this.dialog.getButton(-2).setTextColor(color);
        this.usernameTextView.requestFocus();
    }

    public void createDialog() {
        View inflate = LayoutInflater.from(this.context).inflate(C0769R.layout.dialog_http_auth, null);
        this.usernameTextView = (TextView) inflate.findViewById(C0427R.C0426id.httpAuthUsername);
        this.passwordTextView = (TextView) inflate.findViewById(C0427R.C0426id.httpAuthPassword);
        this.passwordTextView.setOnEditorActionListener(new C0542xecd1e2cb(this));
        buildDialog(inflate);
    }

    public static /* synthetic */ boolean lambda$createDialog$0(HttpAuthenticationDialogBuilder httpAuthenticationDialogBuilder, TextView textView, int i, KeyEvent keyEvent) {
        if (i != 6) {
            return false;
        }
        httpAuthenticationDialogBuilder.dialog.getButton(-1).performClick();
        return true;
    }

    private void buildDialog(View view) {
        this.dialog = new android.app.AlertDialog.Builder(this.context).setIconAttribute(16843605).setView(view).setPositiveButton(C0769R.string.action_ok, new C0544x756d6cfd(this)).setNegativeButton(C0769R.string.action_cancel, new C0541x2f0dc5f9(this)).setOnCancelListener(new C0543xcd33fb86(this)).create();
        Window window = this.dialog.getWindow();
        if (window != null) {
            window.setSoftInputMode(4);
        }
    }

    public static /* synthetic */ void lambda$buildDialog$1(HttpAuthenticationDialogBuilder httpAuthenticationDialogBuilder, DialogInterface dialogInterface, int i) {
        if (httpAuthenticationDialogBuilder.okListener != null) {
            httpAuthenticationDialogBuilder.okListener.onOk(httpAuthenticationDialogBuilder.host, httpAuthenticationDialogBuilder.realm, httpAuthenticationDialogBuilder.getUsername(), httpAuthenticationDialogBuilder.getPassword());
        }
    }

    public static /* synthetic */ void lambda$buildDialog$2(HttpAuthenticationDialogBuilder httpAuthenticationDialogBuilder, DialogInterface dialogInterface, int i) {
        if (httpAuthenticationDialogBuilder.cancelListener != null) {
            httpAuthenticationDialogBuilder.cancelListener.onCancel();
        }
    }

    public static /* synthetic */ void lambda$buildDialog$3(HttpAuthenticationDialogBuilder httpAuthenticationDialogBuilder, DialogInterface dialogInterface) {
        if (httpAuthenticationDialogBuilder.cancelListener != null) {
            httpAuthenticationDialogBuilder.cancelListener.onCancel();
        }
    }
}
