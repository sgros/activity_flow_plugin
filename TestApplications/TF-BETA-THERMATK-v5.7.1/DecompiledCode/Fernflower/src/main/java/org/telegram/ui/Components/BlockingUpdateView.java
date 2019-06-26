package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import androidx.core.content.FileProvider;
import java.io.File;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;

public class BlockingUpdateView extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
   private FrameLayout acceptButton;
   private TextView acceptTextView;
   private int accountNum;
   private TLRPC.TL_help_appUpdate appUpdate;
   private String fileName;
   private int pressCount;
   private AnimatorSet progressAnimation;
   private RadialProgress radialProgress;
   private FrameLayout radialProgressView;
   private TextView textView;

   public BlockingUpdateView(Context var1) {
      super(var1);
      this.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
      int var2;
      if (VERSION.SDK_INT >= 21) {
         var2 = (int)((float)AndroidUtilities.statusBarHeight / AndroidUtilities.density);
      } else {
         var2 = 0;
      }

      FrameLayout var3 = new FrameLayout(var1);
      var3.setBackgroundColor(-11556378);
      int var4 = AndroidUtilities.dp(176.0F);
      int var5;
      if (VERSION.SDK_INT >= 21) {
         var5 = AndroidUtilities.statusBarHeight;
      } else {
         var5 = 0;
      }

      this.addView(var3, new LayoutParams(-1, var4 + var5));
      ImageView var6 = new ImageView(var1);
      var6.setImageResource(2131165518);
      var6.setScaleType(ScaleType.CENTER);
      var6.setPadding(0, 0, 0, AndroidUtilities.dp(14.0F));
      var3.addView(var6, LayoutHelper.createFrame(-2, -2.0F, 17, 0.0F, (float)var2, 0.0F, 0.0F));
      var6.setOnClickListener(new _$$Lambda$BlockingUpdateView$ninaHPZEm5ZV8HP69_4gylboxn4(this));
      ScrollView var7 = new ScrollView(var1);
      AndroidUtilities.setScrollViewEdgeEffectColor(var7, Theme.getColor("actionBarDefault"));
      this.addView(var7, LayoutHelper.createFrame(-1, -1.0F, 51, 27.0F, (float)(var2 + 206), 27.0F, 130.0F));
      FrameLayout var10 = new FrameLayout(var1);
      var7.addView(var10, LayoutHelper.createScroll(-1, -2, 17));
      TextView var8 = new TextView(var1);
      var8.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      var8.setTextSize(1, 20.0F);
      var8.setGravity(49);
      var8.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      var8.setText(LocaleController.getString("UpdateTelegram", 2131560955));
      var10.addView(var8, LayoutHelper.createFrame(-2, -2, 49));
      this.textView = new TextView(var1);
      this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.textView.setLinkTextColor(Theme.getColor("windowBackgroundWhiteLinkText"));
      this.textView.setTextSize(1, 15.0F);
      this.textView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
      this.textView.setGravity(49);
      this.textView.setLineSpacing((float)AndroidUtilities.dp(2.0F), 1.0F);
      var10.addView(this.textView, LayoutHelper.createFrame(-2, -2.0F, 51, 0.0F, 44.0F, 0.0F, 0.0F));
      this.acceptButton = new FrameLayout(var1);
      this.acceptButton.setBackgroundResource(2131165800);
      if (VERSION.SDK_INT >= 21) {
         StateListAnimator var11 = new StateListAnimator();
         ObjectAnimator var9 = ObjectAnimator.ofFloat(this.acceptButton, "translationZ", new float[]{(float)AndroidUtilities.dp(2.0F), (float)AndroidUtilities.dp(4.0F)}).setDuration(200L);
         var11.addState(new int[]{16842919}, var9);
         var9 = ObjectAnimator.ofFloat(this.acceptButton, "translationZ", new float[]{(float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(2.0F)}).setDuration(200L);
         var11.addState(new int[0], var9);
         this.acceptButton.setStateListAnimator(var11);
      }

      this.acceptButton.setPadding(AndroidUtilities.dp(20.0F), 0, AndroidUtilities.dp(20.0F), 0);
      this.addView(this.acceptButton, LayoutHelper.createFrame(-2, 56.0F, 81, 0.0F, 0.0F, 0.0F, 45.0F));
      this.acceptButton.setOnClickListener(new _$$Lambda$BlockingUpdateView$58XjGl4nF8__CcazfB65zVJEC_c(this));
      this.acceptTextView = new TextView(var1);
      this.acceptTextView.setGravity(17);
      this.acceptTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.acceptTextView.setTextColor(-1);
      this.acceptTextView.setTextSize(1, 16.0F);
      this.acceptButton.addView(this.acceptTextView, LayoutHelper.createFrame(-2, -2, 17));
      this.radialProgressView = new FrameLayout(var1) {
         protected void onDraw(Canvas var1) {
            BlockingUpdateView.this.radialProgress.draw(var1);
         }

         protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
            super.onLayout(var1, var2, var3, var4, var5);
            int var6 = AndroidUtilities.dp(36.0F);
            var2 = (var4 - var2 - var6) / 2;
            var3 = (var5 - var3 - var6) / 2;
            BlockingUpdateView.this.radialProgress.setProgressRect(var2, var3, var2 + var6, var6 + var3);
         }
      };
      this.radialProgressView.setWillNotDraw(false);
      this.radialProgressView.setAlpha(0.0F);
      this.radialProgressView.setScaleX(0.1F);
      this.radialProgressView.setScaleY(0.1F);
      this.radialProgressView.setVisibility(4);
      this.radialProgress = new RadialProgress(this.radialProgressView);
      this.radialProgress.setBackground((Drawable)null, true, false);
      this.radialProgress.setProgressColor(-1);
      this.acceptButton.addView(this.radialProgressView, LayoutHelper.createFrame(36, 36, 17));
   }

   public static boolean checkApkInstallPermissions(Context var0) {
      if (VERSION.SDK_INT >= 26 && !ApplicationLoader.applicationContext.getPackageManager().canRequestPackageInstalls()) {
         AlertDialog.Builder var1 = new AlertDialog.Builder(var0);
         var1.setTitle(LocaleController.getString("AppName", 2131558635));
         var1.setMessage(LocaleController.getString("ApkRestricted", 2131558634));
         var1.setPositiveButton(LocaleController.getString("PermissionOpenSettings", 2131560419), new _$$Lambda$BlockingUpdateView$ftz1vauUG0mc962Y32crTPmAh74(var0));
         var1.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
         var1.show();
         return false;
      } else {
         return true;
      }
   }

   // $FF: synthetic method
   static void lambda$checkApkInstallPermissions$2(Context var0, DialogInterface var1, int var2) {
      try {
         StringBuilder var3 = new StringBuilder();
         var3.append("package:");
         var3.append(ApplicationLoader.applicationContext.getPackageName());
         Intent var5 = new Intent("android.settings.MANAGE_UNKNOWN_APP_SOURCES", Uri.parse(var3.toString()));
         var0.startActivity(var5);
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

   }

   public static boolean openApkInstall(Activity var0, TLRPC.Document var1) {
      boolean var2 = false;
      boolean var3 = var2;

      label114: {
         Exception var10000;
         Exception var17;
         label103: {
            boolean var10001;
            try {
               FileLoader.getAttachFileName(var1);
            } catch (Exception var16) {
               var10000 = var16;
               var10001 = false;
               break label103;
            }

            var3 = var2;

            File var4;
            try {
               var4 = FileLoader.getPathToAttach(var1, true);
            } catch (Exception var15) {
               var10000 = var15;
               var10001 = false;
               break label103;
            }

            var3 = var2;

            try {
               var2 = var4.exists();
            } catch (Exception var14) {
               var10000 = var14;
               var10001 = false;
               break label103;
            }

            var3 = var2;
            if (!var2) {
               return var3;
            }

            var3 = var2;

            Intent var18;
            try {
               var18 = new Intent;
            } catch (Exception var13) {
               var10000 = var13;
               var10001 = false;
               break label103;
            }

            var3 = var2;

            try {
               var18.<init>("android.intent.action.VIEW");
            } catch (Exception var12) {
               var10000 = var12;
               var10001 = false;
               break label103;
            }

            var3 = var2;

            try {
               var18.setFlags(1);
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
               break label103;
            }

            var3 = var2;

            int var5;
            try {
               var5 = VERSION.SDK_INT;
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label103;
            }

            if (var5 >= 24) {
               var3 = var2;

               try {
                  var18.setDataAndType(FileProvider.getUriForFile(var0, "org.telegram.messenger.provider", var4), "application/vnd.android.package-archive");
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label103;
               }
            } else {
               var3 = var2;

               try {
                  var18.setDataAndType(Uri.fromFile(var4), "application/vnd.android.package-archive");
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label103;
               }
            }

            try {
               var0.startActivityForResult(var18, 500);
               break label114;
            } catch (Exception var7) {
               label115: {
                  var17 = var7;
                  var3 = var2;

                  try {
                     FileLog.e((Throwable)var17);
                  } catch (Exception var6) {
                     var10000 = var6;
                     var10001 = false;
                     break label115;
                  }

                  var3 = var2;
                  return var3;
               }
            }
         }

         var17 = var10000;
         FileLog.e((Throwable)var17);
         return var3;
      }

      var3 = var2;
      return var3;
   }

   private void showProgress(final boolean var1) {
      AnimatorSet var2 = this.progressAnimation;
      if (var2 != null) {
         var2.cancel();
      }

      this.progressAnimation = new AnimatorSet();
      if (var1) {
         this.radialProgressView.setVisibility(0);
         this.acceptButton.setEnabled(false);
         this.progressAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.acceptTextView, "scaleX", new float[]{0.1F}), ObjectAnimator.ofFloat(this.acceptTextView, "scaleY", new float[]{0.1F}), ObjectAnimator.ofFloat(this.acceptTextView, "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(this.radialProgressView, "scaleX", new float[]{1.0F}), ObjectAnimator.ofFloat(this.radialProgressView, "scaleY", new float[]{1.0F}), ObjectAnimator.ofFloat(this.radialProgressView, "alpha", new float[]{1.0F})});
      } else {
         this.acceptTextView.setVisibility(0);
         this.acceptButton.setEnabled(true);
         this.progressAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.radialProgressView, "scaleX", new float[]{0.1F}), ObjectAnimator.ofFloat(this.radialProgressView, "scaleY", new float[]{0.1F}), ObjectAnimator.ofFloat(this.radialProgressView, "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(this.acceptTextView, "scaleX", new float[]{1.0F}), ObjectAnimator.ofFloat(this.acceptTextView, "scaleY", new float[]{1.0F}), ObjectAnimator.ofFloat(this.acceptTextView, "alpha", new float[]{1.0F})});
      }

      this.progressAnimation.addListener(new AnimatorListenerAdapter() {
         public void onAnimationCancel(Animator var1x) {
            if (BlockingUpdateView.this.progressAnimation != null && BlockingUpdateView.this.progressAnimation.equals(var1x)) {
               BlockingUpdateView.this.progressAnimation = null;
            }

         }

         public void onAnimationEnd(Animator var1x) {
            if (BlockingUpdateView.this.progressAnimation != null && BlockingUpdateView.this.progressAnimation.equals(var1x)) {
               if (!var1) {
                  BlockingUpdateView.this.radialProgressView.setVisibility(4);
               } else {
                  BlockingUpdateView.this.acceptTextView.setVisibility(4);
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
         var6 = (String)var3[0];
         var4 = this.fileName;
         if (var4 != null && var4.equals(var6)) {
            this.showProgress(false);
            openApkInstall((Activity)this.getContext(), this.appUpdate.document);
         }
      } else if (var1 == NotificationCenter.fileDidFailedLoad) {
         var6 = (String)var3[0];
         var4 = this.fileName;
         if (var4 != null && var4.equals(var6)) {
            this.showProgress(false);
         }
      } else if (var1 == NotificationCenter.FileLoadProgressChanged) {
         String var5 = (String)var3[0];
         var4 = this.fileName;
         if (var4 != null && var4.equals(var5)) {
            Float var7 = (Float)var3[1];
            this.radialProgress.setProgress(var7, true);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$new$0$BlockingUpdateView(View var1) {
      ++this.pressCount;
      if (this.pressCount >= 10) {
         this.setVisibility(8);
         UserConfig.getInstance(0).pendingAppUpdate = null;
         UserConfig.getInstance(0).saveConfig(false);
      }

   }

   // $FF: synthetic method
   public void lambda$new$1$BlockingUpdateView(View var1) {
      if (checkApkInstallPermissions(this.getContext())) {
         TLRPC.TL_help_appUpdate var2 = this.appUpdate;
         if (var2.document instanceof TLRPC.TL_document) {
            if (!openApkInstall((Activity)this.getContext(), this.appUpdate.document)) {
               FileLoader.getInstance(this.accountNum).loadFile(this.appUpdate.document, "update", 2, 1);
               this.showProgress(true);
            }
         } else if (var2.url != null) {
            Browser.openUrl(this.getContext(), this.appUpdate.url);
         }

      }
   }

   public void setVisibility(int var1) {
      super.setVisibility(var1);
      if (var1 == 8) {
         NotificationCenter.getInstance(this.accountNum).removeObserver(this, NotificationCenter.fileDidLoad);
         NotificationCenter.getInstance(this.accountNum).removeObserver(this, NotificationCenter.fileDidFailedLoad);
         NotificationCenter.getInstance(this.accountNum).removeObserver(this, NotificationCenter.FileLoadProgressChanged);
      }

   }

   public void show(int var1, TLRPC.TL_help_appUpdate var2) {
      this.pressCount = 0;
      this.appUpdate = var2;
      this.accountNum = var1;
      TLRPC.Document var3 = var2.document;
      if (var3 instanceof TLRPC.TL_document) {
         this.fileName = FileLoader.getAttachFileName(var3);
      }

      if (this.getVisibility() != 0) {
         this.setVisibility(0);
      }

      SpannableStringBuilder var5 = new SpannableStringBuilder(var2.text);
      MessageObject.addEntitiesToText(var5, var2.entities, false, 0, false, false, false);
      this.textView.setText(var5);
      if (var2.document instanceof TLRPC.TL_document) {
         TextView var4 = this.acceptTextView;
         StringBuilder var6 = new StringBuilder();
         var6.append(LocaleController.getString("Update", 2131560949).toUpperCase());
         var6.append(String.format(Locale.US, " (%1$s)", AndroidUtilities.formatFileSize((long)var2.document.size)));
         var4.setText(var6.toString());
      } else {
         this.acceptTextView.setText(LocaleController.getString("Update", 2131560949).toUpperCase());
      }

      NotificationCenter.getInstance(this.accountNum).addObserver(this, NotificationCenter.fileDidLoad);
      NotificationCenter.getInstance(this.accountNum).addObserver(this, NotificationCenter.fileDidFailedLoad);
      NotificationCenter.getInstance(this.accountNum).addObserver(this, NotificationCenter.FileLoadProgressChanged);
   }
}
