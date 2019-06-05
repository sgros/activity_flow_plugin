package org.mozilla.focus.web;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

public class HttpAuthenticationDialogBuilder {
   private HttpAuthenticationDialogBuilder.CancelListener cancelListener;
   private final Context context;
   private AlertDialog dialog;
   private final String host;
   private HttpAuthenticationDialogBuilder.OkListener okListener;
   private TextView passwordTextView;
   private final String realm;
   private TextView usernameTextView;

   HttpAuthenticationDialogBuilder(HttpAuthenticationDialogBuilder.Builder var1) {
      this.context = var1.context;
      this.host = var1.host;
      this.realm = var1.realm;
      this.okListener = var1.okListener;
      this.cancelListener = var1.cancelListener;
   }

   private void buildDialog(View var1) {
      this.dialog = (new android.app.AlertDialog.Builder(this.context)).setIconAttribute(16843605).setView(var1).setPositiveButton(2131755061, new _$$Lambda$HttpAuthenticationDialogBuilder$k_Dm8CgRytlnguX9ShhoWY4uc8Q(this)).setNegativeButton(2131755060, new _$$Lambda$HttpAuthenticationDialogBuilder$79_L6_eARRUsO3aVMA40B2wdxh4(this)).setOnCancelListener(new _$$Lambda$HttpAuthenticationDialogBuilder$OZ8WI06CBP8E2_wL0EbbCuZ14wQ(this)).create();
      Window var2 = this.dialog.getWindow();
      if (var2 != null) {
         var2.setSoftInputMode(4);
      }

   }

   private String getPassword() {
      return this.passwordTextView.getText().toString();
   }

   private String getUsername() {
      return this.usernameTextView.getText().toString();
   }

   // $FF: synthetic method
   public static void lambda$buildDialog$1(HttpAuthenticationDialogBuilder var0, DialogInterface var1, int var2) {
      if (var0.okListener != null) {
         var0.okListener.onOk(var0.host, var0.realm, var0.getUsername(), var0.getPassword());
      }

   }

   // $FF: synthetic method
   public static void lambda$buildDialog$2(HttpAuthenticationDialogBuilder var0, DialogInterface var1, int var2) {
      if (var0.cancelListener != null) {
         var0.cancelListener.onCancel();
      }

   }

   // $FF: synthetic method
   public static void lambda$buildDialog$3(HttpAuthenticationDialogBuilder var0, DialogInterface var1) {
      if (var0.cancelListener != null) {
         var0.cancelListener.onCancel();
      }

   }

   // $FF: synthetic method
   public static boolean lambda$createDialog$0(HttpAuthenticationDialogBuilder var0, TextView var1, int var2, KeyEvent var3) {
      if (var2 == 6) {
         var0.dialog.getButton(-1).performClick();
         return true;
      } else {
         return false;
      }
   }

   public void createDialog() {
      View var1 = LayoutInflater.from(this.context).inflate(2131492942, (ViewGroup)null);
      this.usernameTextView = (TextView)var1.findViewById(2131296472);
      this.passwordTextView = (TextView)var1.findViewById(2131296471);
      this.passwordTextView.setOnEditorActionListener(new _$$Lambda$HttpAuthenticationDialogBuilder$OWVDbtV3vCIyTwaiMgH1w9Yb7EU(this));
      this.buildDialog(var1);
   }

   public void show() {
      this.dialog.show();
      int var1 = ContextCompat.getColor(this.context, 2131099832);
      this.dialog.getButton(-1).setTextColor(var1);
      this.dialog.getButton(-2).setTextColor(var1);
      this.usernameTextView.requestFocus();
   }

   public static class Builder {
      private HttpAuthenticationDialogBuilder.CancelListener cancelListener;
      private final Context context;
      private final String host;
      private HttpAuthenticationDialogBuilder.OkListener okListener;
      private final String realm;

      public Builder(Context var1, String var2, String var3) {
         this.context = var1;
         this.host = var2;
         this.realm = var3;
      }

      public HttpAuthenticationDialogBuilder build() {
         return new HttpAuthenticationDialogBuilder(this);
      }

      public HttpAuthenticationDialogBuilder.Builder setCancelListener(HttpAuthenticationDialogBuilder.CancelListener var1) {
         this.cancelListener = var1;
         return this;
      }

      public HttpAuthenticationDialogBuilder.Builder setOkListener(HttpAuthenticationDialogBuilder.OkListener var1) {
         this.okListener = var1;
         return this;
      }
   }

   public interface CancelListener {
      void onCancel();
   }

   public interface OkListener {
      void onOk(String var1, String var2, String var3, String var4);
   }
}
