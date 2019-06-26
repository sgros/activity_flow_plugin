package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;

public class UpdateAppAlertDialog extends AlertDialog implements NotificationCenter.NotificationCenterDelegate {
   private int accountNum;
   private TLRPC.TL_help_appUpdate appUpdate;
   private String fileName;
   private Activity parentActivity;
   private AnimatorSet progressAnimation;
   private RadialProgress radialProgress;
   private FrameLayout radialProgressView;

   public UpdateAppAlertDialog(Activity var1, TLRPC.TL_help_appUpdate var2, int var3) {
      super(var1, 0);
      this.appUpdate = var2;
      this.accountNum = var3;
      TLRPC.Document var5 = var2.document;
      if (var5 instanceof TLRPC.TL_document) {
         this.fileName = FileLoader.getAttachFileName(var5);
      }

      this.parentActivity = var1;
      this.setTopImage(2131165892, Theme.getColor("dialogTopBackground"));
      this.setTopHeight(175);
      this.setMessage(this.appUpdate.text);
      TLRPC.Document var4 = this.appUpdate.document;
      if (var4 instanceof TLRPC.TL_document) {
         this.setSecondTitle(AndroidUtilities.formatFileSize((long)var4.size));
      }

      this.setDismissDialogByButtons(false);
      this.setTitle(LocaleController.getString("UpdateTelegram", 2131560955));
      this.setPositiveButton(LocaleController.getString("UpdateNow", 2131560954), new _$$Lambda$UpdateAppAlertDialog$voSGHXhYuACsnb2_x2STYrPYmkw(this));
      this.setNeutralButton(LocaleController.getString("Later", 2131559743), new _$$Lambda$UpdateAppAlertDialog$egnS6_js0sES87wcIflPDbL7olc(this));
      this.radialProgressView = new FrameLayout(this.parentActivity) {
         protected void onDraw(Canvas var1) {
            UpdateAppAlertDialog.this.radialProgress.draw(var1);
         }

         protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
            super.onLayout(var1, var2, var3, var4, var5);
            int var6 = AndroidUtilities.dp(24.0F);
            var2 = (var4 - var2 - var6) / 2;
            var3 = (var5 - var3 - var6) / 2 + AndroidUtilities.dp(2.0F);
            UpdateAppAlertDialog.this.radialProgress.setProgressRect(var2, var3, var2 + var6, var6 + var3);
         }
      };
      this.radialProgressView.setWillNotDraw(false);
      this.radialProgressView.setAlpha(0.0F);
      this.radialProgressView.setScaleX(0.1F);
      this.radialProgressView.setScaleY(0.1F);
      this.radialProgressView.setVisibility(4);
      this.radialProgress = new RadialProgress(this.radialProgressView);
      this.radialProgress.setStrokeWidth(AndroidUtilities.dp(2.0F));
      this.radialProgress.setBackground((Drawable)null, true, false);
      this.radialProgress.setProgressColor(Theme.getColor("dialogButton"));
   }

   private void showProgress(final boolean var1) {
      AnimatorSet var2 = this.progressAnimation;
      if (var2 != null) {
         var2.cancel();
      }

      this.progressAnimation = new AnimatorSet();
      final View var3 = super.buttonsLayout.findViewWithTag(-1);
      if (var1) {
         this.radialProgressView.setVisibility(0);
         var3.setEnabled(false);
         this.progressAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(var3, "scaleX", new float[]{0.1F}), ObjectAnimator.ofFloat(var3, "scaleY", new float[]{0.1F}), ObjectAnimator.ofFloat(var3, "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(this.radialProgressView, "scaleX", new float[]{1.0F}), ObjectAnimator.ofFloat(this.radialProgressView, "scaleY", new float[]{1.0F}), ObjectAnimator.ofFloat(this.radialProgressView, "alpha", new float[]{1.0F})});
      } else {
         var3.setVisibility(0);
         var3.setEnabled(true);
         this.progressAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.radialProgressView, "scaleX", new float[]{0.1F}), ObjectAnimator.ofFloat(this.radialProgressView, "scaleY", new float[]{0.1F}), ObjectAnimator.ofFloat(this.radialProgressView, "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(var3, "scaleX", new float[]{1.0F}), ObjectAnimator.ofFloat(var3, "scaleY", new float[]{1.0F}), ObjectAnimator.ofFloat(var3, "alpha", new float[]{1.0F})});
      }

      this.progressAnimation.addListener(new AnimatorListenerAdapter() {
         public void onAnimationCancel(Animator var1x) {
            if (UpdateAppAlertDialog.this.progressAnimation != null && UpdateAppAlertDialog.this.progressAnimation.equals(var1x)) {
               UpdateAppAlertDialog.this.progressAnimation = null;
            }

         }

         public void onAnimationEnd(Animator var1x) {
            if (UpdateAppAlertDialog.this.progressAnimation != null && UpdateAppAlertDialog.this.progressAnimation.equals(var1x)) {
               if (!var1) {
                  UpdateAppAlertDialog.this.radialProgressView.setVisibility(4);
               } else {
                  var3.setVisibility(4);
               }
            }

         }
      });
      this.progressAnimation.setDuration(150L);
      this.progressAnimation.start();
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      String var4;
      String var6;
      if (var1 == NotificationCenter.fileDidLoad) {
         var4 = (String)var3[0];
         var6 = this.fileName;
         if (var6 != null && var6.equals(var4)) {
            this.showProgress(false);
            BlockingUpdateView.openApkInstall(this.parentActivity, this.appUpdate.document);
         }
      } else if (var1 == NotificationCenter.fileDidFailedLoad) {
         var6 = (String)var3[0];
         var4 = this.fileName;
         if (var4 != null && var4.equals(var6)) {
            this.showProgress(false);
         }
      } else if (var1 == NotificationCenter.FileLoadProgressChanged) {
         var4 = (String)var3[0];
         String var5 = this.fileName;
         if (var5 != null && var5.equals(var4)) {
            Float var7 = (Float)var3[1];
            this.radialProgress.setProgress(var7, true);
         }
      }

   }

   public void dismiss() {
      super.dismiss();
      NotificationCenter.getInstance(this.accountNum).removeObserver(this, NotificationCenter.fileDidLoad);
      NotificationCenter.getInstance(this.accountNum).removeObserver(this, NotificationCenter.fileDidFailedLoad);
      NotificationCenter.getInstance(this.accountNum).removeObserver(this, NotificationCenter.FileLoadProgressChanged);
   }

   // $FF: synthetic method
   public void lambda$new$0$UpdateAppAlertDialog(DialogInterface var1, int var2) {
      if (BlockingUpdateView.checkApkInstallPermissions(this.getContext())) {
         TLRPC.TL_help_appUpdate var3 = this.appUpdate;
         TLRPC.Document var4 = var3.document;
         if (var4 instanceof TLRPC.TL_document) {
            if (!BlockingUpdateView.openApkInstall(this.parentActivity, var4)) {
               FileLoader.getInstance(this.accountNum).loadFile(this.appUpdate.document, "update", 1, 1);
               this.showProgress(true);
            }
         } else if (var3.url != null) {
            Browser.openUrl(this.getContext(), this.appUpdate.url);
            var1.dismiss();
         }

      }
   }

   // $FF: synthetic method
   public void lambda$new$1$UpdateAppAlertDialog(DialogInterface var1, int var2) {
      if (this.appUpdate.document instanceof TLRPC.TL_document) {
         FileLoader.getInstance(this.accountNum).cancelLoadFile(this.appUpdate.document);
      }

      var1.dismiss();
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      NotificationCenter.getInstance(this.accountNum).addObserver(this, NotificationCenter.fileDidLoad);
      NotificationCenter.getInstance(this.accountNum).addObserver(this, NotificationCenter.fileDidFailedLoad);
      NotificationCenter.getInstance(this.accountNum).addObserver(this, NotificationCenter.FileLoadProgressChanged);
      super.buttonsLayout.addView(this.radialProgressView, LayoutHelper.createFrame(36, 36.0F));
   }
}
