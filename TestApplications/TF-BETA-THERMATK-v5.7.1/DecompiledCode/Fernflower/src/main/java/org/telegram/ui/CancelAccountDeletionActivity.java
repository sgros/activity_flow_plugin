package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SmsReceiver;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.SlideView;

public class CancelAccountDeletionActivity extends BaseFragment {
   private static final int done_button = 1;
   private boolean checkPermissions = false;
   private int currentViewNum = 0;
   private View doneButton;
   private Dialog errorDialog;
   private String hash;
   private Dialog permissionsDialog;
   private ArrayList permissionsItems = new ArrayList();
   private String phone;
   private AlertDialog progressDialog;
   private int scrollHeight;
   private SlideView[] views = new SlideView[5];

   public CancelAccountDeletionActivity(Bundle var1) {
      super(var1);
      this.hash = var1.getString("hash");
      this.phone = var1.getString("phone");
   }

   // $FF: synthetic method
   static int access$3200(CancelAccountDeletionActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3400(CancelAccountDeletionActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3500(CancelAccountDeletionActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3600(CancelAccountDeletionActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$500(CancelAccountDeletionActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$800(CancelAccountDeletionActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$900(CancelAccountDeletionActivity var0) {
      return var0.currentAccount;
   }

   private void fillNextCodeParams(Bundle var1, TLRPC.TL_auth_sentCode var2) {
      var1.putString("phoneHash", var2.phone_code_hash);
      TLRPC.auth_CodeType var3 = var2.next_type;
      if (var3 instanceof TLRPC.TL_auth_codeTypeCall) {
         var1.putInt("nextType", 4);
      } else if (var3 instanceof TLRPC.TL_auth_codeTypeFlashCall) {
         var1.putInt("nextType", 3);
      } else if (var3 instanceof TLRPC.TL_auth_codeTypeSms) {
         var1.putInt("nextType", 2);
      }

      if (var2.type instanceof TLRPC.TL_auth_sentCodeTypeApp) {
         var1.putInt("type", 1);
         var1.putInt("length", var2.type.length);
         this.setPage(1, true, var1, false);
      } else {
         if (var2.timeout == 0) {
            var2.timeout = 60;
         }

         var1.putInt("timeout", var2.timeout * 1000);
         TLRPC.auth_SentCodeType var4 = var2.type;
         if (var4 instanceof TLRPC.TL_auth_sentCodeTypeCall) {
            var1.putInt("type", 4);
            var1.putInt("length", var2.type.length);
            this.setPage(4, true, var1, false);
         } else if (var4 instanceof TLRPC.TL_auth_sentCodeTypeFlashCall) {
            var1.putInt("type", 3);
            var1.putString("pattern", var2.type.pattern);
            this.setPage(3, true, var1, false);
         } else if (var4 instanceof TLRPC.TL_auth_sentCodeTypeSms) {
            var1.putInt("type", 2);
            var1.putInt("length", var2.type.length);
            this.setPage(2, true, var1, false);
         }
      }

   }

   public View createView(Context var1) {
      super.actionBar.setTitle(LocaleController.getString("AppName", 2131558635));
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == 1) {
               CancelAccountDeletionActivity.this.views[CancelAccountDeletionActivity.this.currentViewNum].onNextPressed();
            } else if (var1 == -1) {
               CancelAccountDeletionActivity.this.finishFragment();
            }

         }
      });
      this.doneButton = super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0F));
      this.doneButton.setVisibility(8);
      ScrollView var2 = new ScrollView(var1) {
         protected void onMeasure(int var1, int var2) {
            CancelAccountDeletionActivity.this.scrollHeight = MeasureSpec.getSize(var2) - AndroidUtilities.dp(30.0F);
            super.onMeasure(var1, var2);
         }

         public boolean requestChildRectangleOnScreen(View var1, Rect var2, boolean var3) {
            if (CancelAccountDeletionActivity.this.currentViewNum == 1 || CancelAccountDeletionActivity.this.currentViewNum == 2 || CancelAccountDeletionActivity.this.currentViewNum == 4) {
               var2.bottom += AndroidUtilities.dp(40.0F);
            }

            return super.requestChildRectangleOnScreen(var1, var2, var3);
         }
      };
      var2.setFillViewport(true);
      super.fragmentView = var2;
      FrameLayout var3 = new FrameLayout(var1);
      var2.addView(var3, LayoutHelper.createScroll(-1, -2, 51));
      this.views[0] = new CancelAccountDeletionActivity.PhoneView(var1);
      this.views[1] = new CancelAccountDeletionActivity.LoginActivitySmsView(var1, 1);
      this.views[2] = new CancelAccountDeletionActivity.LoginActivitySmsView(var1, 2);
      this.views[3] = new CancelAccountDeletionActivity.LoginActivitySmsView(var1, 3);
      this.views[4] = new CancelAccountDeletionActivity.LoginActivitySmsView(var1, 4);
      int var4 = 0;

      while(true) {
         SlideView[] var9 = this.views;
         if (var4 >= var9.length) {
            super.actionBar.setTitle(var9[0].getHeaderName());
            return super.fragmentView;
         }

         SlideView var10 = var9[var4];
         byte var5;
         if (var4 == 0) {
            var5 = 0;
         } else {
            var5 = 8;
         }

         var10.setVisibility(var5);
         var10 = this.views[var4];
         float var6;
         if (var4 == 0) {
            var6 = -2.0F;
         } else {
            var6 = -1.0F;
         }

         float var7;
         if (AndroidUtilities.isTablet()) {
            var7 = 26.0F;
         } else {
            var7 = 18.0F;
         }

         float var8;
         if (AndroidUtilities.isTablet()) {
            var8 = 26.0F;
         } else {
            var8 = 18.0F;
         }

         var3.addView(var10, LayoutHelper.createFrame(-1, var6, 51, var7, 30.0F, var8, 0.0F));
         ++var4;
      }
   }

   public ThemeDescription[] getThemeDescriptions() {
      SlideView[] var1 = this.views;
      CancelAccountDeletionActivity.PhoneView var2 = (CancelAccountDeletionActivity.PhoneView)var1[0];
      CancelAccountDeletionActivity.LoginActivitySmsView var3 = (CancelAccountDeletionActivity.LoginActivitySmsView)var1[1];
      CancelAccountDeletionActivity.LoginActivitySmsView var4 = (CancelAccountDeletionActivity.LoginActivitySmsView)var1[2];
      CancelAccountDeletionActivity.LoginActivitySmsView var5 = (CancelAccountDeletionActivity.LoginActivitySmsView)var1[3];
      CancelAccountDeletionActivity.LoginActivitySmsView var6 = (CancelAccountDeletionActivity.LoginActivitySmsView)var1[4];
      ArrayList var8 = new ArrayList();
      var8.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
      var8.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"));
      var8.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"));
      var8.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon"));
      var8.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle"));
      var8.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector"));
      var8.add(new ThemeDescription(var2.progressBar, ThemeDescription.FLAG_PROGRESSBAR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "progressCircle"));
      var8.add(new ThemeDescription(var3.confirmTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
      var8.add(new ThemeDescription(var3.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
      int var7;
      if (var3.codeField != null) {
         for(var7 = 0; var7 < var3.codeField.length; ++var7) {
            var8.add(new ThemeDescription(var3.codeField[var7], ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
            var8.add(new ThemeDescription(var3.codeField[var7], ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"));
         }
      }

      var8.add(new ThemeDescription(var3.timeText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
      var8.add(new ThemeDescription(var3.problemText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText4"));
      var8.add(new ThemeDescription(var3.progressView, 0, new Class[]{CancelAccountDeletionActivity.ProgressView.class}, new String[]{"paint"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "login_progressInner"));
      var8.add(new ThemeDescription(var3.progressView, 0, new Class[]{CancelAccountDeletionActivity.ProgressView.class}, new String[]{"paint"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "login_progressOuter"));
      var8.add(new ThemeDescription(var3.blackImageView, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
      var8.add(new ThemeDescription(var3.blueImageView, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_actionBackground"));
      var8.add(new ThemeDescription(var4.confirmTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
      var8.add(new ThemeDescription(var4.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
      if (var4.codeField != null) {
         for(var7 = 0; var7 < var4.codeField.length; ++var7) {
            var8.add(new ThemeDescription(var4.codeField[var7], ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
            var8.add(new ThemeDescription(var4.codeField[var7], ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"));
         }
      }

      var8.add(new ThemeDescription(var4.timeText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
      var8.add(new ThemeDescription(var4.problemText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText4"));
      var8.add(new ThemeDescription(var4.progressView, 0, new Class[]{CancelAccountDeletionActivity.ProgressView.class}, new String[]{"paint"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "login_progressInner"));
      var8.add(new ThemeDescription(var4.progressView, 0, new Class[]{CancelAccountDeletionActivity.ProgressView.class}, new String[]{"paint"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "login_progressOuter"));
      var8.add(new ThemeDescription(var4.blackImageView, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
      var8.add(new ThemeDescription(var4.blueImageView, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_actionBackground"));
      var8.add(new ThemeDescription(var5.confirmTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
      var8.add(new ThemeDescription(var5.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
      if (var5.codeField != null) {
         for(var7 = 0; var7 < var5.codeField.length; ++var7) {
            var8.add(new ThemeDescription(var5.codeField[var7], ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
            var8.add(new ThemeDescription(var5.codeField[var7], ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"));
         }
      }

      var8.add(new ThemeDescription(var5.timeText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
      var8.add(new ThemeDescription(var5.problemText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText4"));
      var8.add(new ThemeDescription(var5.progressView, 0, new Class[]{CancelAccountDeletionActivity.ProgressView.class}, new String[]{"paint"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "login_progressInner"));
      var8.add(new ThemeDescription(var5.progressView, 0, new Class[]{CancelAccountDeletionActivity.ProgressView.class}, new String[]{"paint"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "login_progressOuter"));
      var8.add(new ThemeDescription(var5.blackImageView, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
      var8.add(new ThemeDescription(var5.blueImageView, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_actionBackground"));
      var8.add(new ThemeDescription(var6.confirmTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
      var8.add(new ThemeDescription(var6.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
      if (var6.codeField != null) {
         for(var7 = 0; var7 < var6.codeField.length; ++var7) {
            var8.add(new ThemeDescription(var6.codeField[var7], ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
            var8.add(new ThemeDescription(var6.codeField[var7], ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"));
         }
      }

      var8.add(new ThemeDescription(var6.timeText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
      var8.add(new ThemeDescription(var6.problemText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText4"));
      var8.add(new ThemeDescription(var6.progressView, 0, new Class[]{CancelAccountDeletionActivity.ProgressView.class}, new String[]{"paint"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "login_progressInner"));
      var8.add(new ThemeDescription(var6.progressView, 0, new Class[]{CancelAccountDeletionActivity.ProgressView.class}, new String[]{"paint"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "login_progressOuter"));
      var8.add(new ThemeDescription(var6.blackImageView, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
      var8.add(new ThemeDescription(var6.blueImageView, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_actionBackground"));
      return (ThemeDescription[])var8.toArray(new ThemeDescription[0]);
   }

   public void needHideProgress() {
      AlertDialog var1 = this.progressDialog;
      if (var1 != null) {
         try {
            var1.dismiss();
         } catch (Exception var2) {
            FileLog.e((Throwable)var2);
         }

         this.progressDialog = null;
      }
   }

   public void needShowProgress() {
      if (this.getParentActivity() != null && !this.getParentActivity().isFinishing() && this.progressDialog == null) {
         this.progressDialog = new AlertDialog(this.getParentActivity(), 3);
         this.progressDialog.setCanCacnel(false);
         this.progressDialog.show();
      }

   }

   public boolean onBackPressed() {
      int var1 = 0;

      while(true) {
         SlideView[] var2 = this.views;
         if (var1 >= var2.length) {
            return true;
         }

         if (var2[var1] != null) {
            var2[var1].onDestroyActivity();
         }

         ++var1;
      }
   }

   protected void onDialogDismiss(Dialog var1) {
      if (VERSION.SDK_INT >= 23 && var1 == this.permissionsDialog && !this.permissionsItems.isEmpty()) {
         this.getParentActivity().requestPermissions((String[])this.permissionsItems.toArray(new String[0]), 6);
      }

      if (var1 == this.errorDialog) {
         this.finishFragment();
      }

   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      int var1 = 0;

      while(true) {
         SlideView[] var2 = this.views;
         if (var1 >= var2.length) {
            AlertDialog var4 = this.progressDialog;
            if (var4 != null) {
               try {
                  var4.dismiss();
               } catch (Exception var3) {
                  FileLog.e((Throwable)var3);
               }

               this.progressDialog = null;
            }

            AndroidUtilities.removeAdjustResize(this.getParentActivity(), super.classGuid);
            return;
         }

         if (var2[var1] != null) {
            var2[var1].onDestroyActivity();
         }

         ++var1;
      }
   }

   public void onRequestPermissionsResultFragment(int var1, String[] var2, int[] var3) {
      if (var1 == 6) {
         this.checkPermissions = false;
         var1 = this.currentViewNum;
         if (var1 == 0) {
            this.views[var1].onNextPressed();
         }
      }

   }

   public void onResume() {
      super.onResume();
      AndroidUtilities.requestAdjustResize(this.getParentActivity(), super.classGuid);
   }

   public void onTransitionAnimationEnd(boolean var1, boolean var2) {
      if (var1) {
         this.views[this.currentViewNum].onShow();
      }

   }

   public void setPage(int var1, boolean var2, Bundle var3, boolean var4) {
      if (var1 != 3 && var1 != 0) {
         this.doneButton.setVisibility(0);
      } else {
         this.doneButton.setVisibility(8);
      }

      SlideView[] var5 = this.views;
      final SlideView var6 = var5[this.currentViewNum];
      final SlideView var8 = var5[var1];
      this.currentViewNum = var1;
      var8.setParams(var3, false);
      super.actionBar.setTitle(var8.getHeaderName());
      var8.onShow();
      if (var4) {
         var1 = -AndroidUtilities.displaySize.x;
      } else {
         var1 = AndroidUtilities.displaySize.x;
      }

      var8.setX((float)var1);
      AnimatorSet var7 = new AnimatorSet();
      var7.setInterpolator(new AccelerateDecelerateInterpolator());
      var7.setDuration(300L);
      if (var4) {
         var1 = AndroidUtilities.displaySize.x;
      } else {
         var1 = -AndroidUtilities.displaySize.x;
      }

      var7.playTogether(new Animator[]{ObjectAnimator.ofFloat(var6, "translationX", new float[]{(float)var1}), ObjectAnimator.ofFloat(var8, "translationX", new float[]{0.0F})});
      var7.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1) {
            var6.setVisibility(8);
            var6.setX(0.0F);
         }

         public void onAnimationStart(Animator var1) {
            var8.setVisibility(0);
         }
      });
      var7.start();
   }

   public class LoginActivitySmsView extends SlideView implements NotificationCenter.NotificationCenterDelegate {
      private ImageView blackImageView;
      private ImageView blueImageView;
      private EditTextBoldCursor[] codeField;
      private LinearLayout codeFieldContainer;
      private int codeTime = 15000;
      private Timer codeTimer;
      private TextView confirmTextView;
      private Bundle currentParams;
      private int currentType;
      private boolean ignoreOnTextChange;
      private double lastCodeTime;
      private double lastCurrentTime;
      private String lastError = "";
      private int length;
      private boolean nextPressed;
      private int nextType;
      private int openTime;
      private String pattern = "*";
      private String phone;
      private String phoneHash;
      private TextView problemText;
      private CancelAccountDeletionActivity.ProgressView progressView;
      private int time = 60000;
      private TextView timeText;
      private Timer timeTimer;
      private int timeout;
      private final Object timerSync = new Object();
      private TextView titleTextView;
      private boolean waitingForEvent;

      public LoginActivitySmsView(Context var2, int var3) {
         super(var2);
         this.currentType = var3;
         this.setOrientation(1);
         this.confirmTextView = new TextView(var2);
         this.confirmTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
         this.confirmTextView.setTextSize(1, 14.0F);
         this.confirmTextView.setLineSpacing((float)AndroidUtilities.dp(2.0F), 1.0F);
         this.titleTextView = new TextView(var2);
         this.titleTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.titleTextView.setTextSize(1, 18.0F);
         this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         TextView var4 = this.titleTextView;
         boolean var5 = LocaleController.isRTL;
         byte var6 = 3;
         byte var9;
         if (var5) {
            var9 = 5;
         } else {
            var9 = 3;
         }

         var4.setGravity(var9);
         this.titleTextView.setLineSpacing((float)AndroidUtilities.dp(2.0F), 1.0F);
         this.titleTextView.setGravity(49);
         FrameLayout var10;
         if (this.currentType == 3) {
            var4 = this.confirmTextView;
            if (LocaleController.isRTL) {
               var9 = 5;
            } else {
               var9 = 3;
            }

            var4.setGravity(var9 | 48);
            var10 = new FrameLayout(var2);
            if (LocaleController.isRTL) {
               var9 = 5;
            } else {
               var9 = 3;
            }

            this.addView(var10, LayoutHelper.createLinear(-2, -2, var9));
            ImageView var7 = new ImageView(var2);
            var7.setImageResource(2131165739);
            var5 = LocaleController.isRTL;
            if (var5) {
               var10.addView(var7, LayoutHelper.createFrame(64, 76.0F, 19, 2.0F, 2.0F, 0.0F, 0.0F));
               TextView var11 = this.confirmTextView;
               if (LocaleController.isRTL) {
                  var9 = 5;
               } else {
                  var9 = 3;
               }

               var10.addView(var11, LayoutHelper.createFrame(-1, -2.0F, var9, 82.0F, 0.0F, 0.0F, 0.0F));
            } else {
               TextView var8 = this.confirmTextView;
               if (var5) {
                  var9 = 5;
               } else {
                  var9 = 3;
               }

               var10.addView(var8, LayoutHelper.createFrame(-1, -2.0F, var9, 0.0F, 0.0F, 82.0F, 0.0F));
               var10.addView(var7, LayoutHelper.createFrame(64, 76.0F, 21, 0.0F, 2.0F, 0.0F, 2.0F));
            }
         } else {
            this.confirmTextView.setGravity(49);
            var10 = new FrameLayout(var2);
            this.addView(var10, LayoutHelper.createLinear(-2, -2, 49));
            if (this.currentType == 1) {
               this.blackImageView = new ImageView(var2);
               this.blackImageView.setImageResource(2131165856);
               this.blackImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteBlackText"), Mode.MULTIPLY));
               var10.addView(this.blackImageView, LayoutHelper.createFrame(-2, -2.0F, 51, 0.0F, 0.0F, 0.0F, 0.0F));
               this.blueImageView = new ImageView(var2);
               this.blueImageView.setImageResource(2131165854);
               this.blueImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chats_actionBackground"), Mode.MULTIPLY));
               var10.addView(this.blueImageView, LayoutHelper.createFrame(-2, -2.0F, 51, 0.0F, 0.0F, 0.0F, 0.0F));
               this.titleTextView.setText(LocaleController.getString("SentAppCodeTitle", 2131560718));
            } else {
               this.blueImageView = new ImageView(var2);
               this.blueImageView.setImageResource(2131165855);
               this.blueImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chats_actionBackground"), Mode.MULTIPLY));
               var10.addView(this.blueImageView, LayoutHelper.createFrame(-2, -2.0F, 51, 0.0F, 0.0F, 0.0F, 0.0F));
               this.titleTextView.setText(LocaleController.getString("SentSmsCodeTitle", 2131560722));
            }

            this.addView(this.titleTextView, LayoutHelper.createLinear(-2, -2, 49, 0, 18, 0, 0));
            this.addView(this.confirmTextView, LayoutHelper.createLinear(-2, -2, 49, 0, 17, 0, 0));
         }

         this.codeFieldContainer = new LinearLayout(var2);
         this.codeFieldContainer.setOrientation(0);
         this.addView(this.codeFieldContainer, LayoutHelper.createLinear(-2, 36, 1));
         if (this.currentType == 3) {
            this.codeFieldContainer.setVisibility(8);
         }

         this.timeText = new TextView(var2) {
            protected void onMeasure(int var1, int var2) {
               super.onMeasure(var1, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0F), Integer.MIN_VALUE));
            }
         };
         this.timeText.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
         this.timeText.setLineSpacing((float)AndroidUtilities.dp(2.0F), 1.0F);
         if (this.currentType == 3) {
            this.timeText.setTextSize(1, 14.0F);
            var4 = this.timeText;
            if (LocaleController.isRTL) {
               var9 = 5;
            } else {
               var9 = 3;
            }

            this.addView(var4, LayoutHelper.createLinear(-2, -2, var9));
            this.progressView = CancelAccountDeletionActivity.this.new ProgressView(var2);
            var4 = this.timeText;
            var9 = var6;
            if (LocaleController.isRTL) {
               var9 = 5;
            }

            var4.setGravity(var9);
            this.addView(this.progressView, LayoutHelper.createLinear(-1, 3, 0.0F, 12.0F, 0.0F, 0.0F));
         } else {
            this.timeText.setPadding(0, AndroidUtilities.dp(2.0F), 0, AndroidUtilities.dp(10.0F));
            this.timeText.setTextSize(1, 15.0F);
            this.timeText.setGravity(49);
            this.addView(this.timeText, LayoutHelper.createLinear(-2, -2, 49));
         }

         this.problemText = new TextView(var2) {
            protected void onMeasure(int var1, int var2) {
               super.onMeasure(var1, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0F), Integer.MIN_VALUE));
            }
         };
         this.problemText.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
         this.problemText.setLineSpacing((float)AndroidUtilities.dp(2.0F), 1.0F);
         this.problemText.setPadding(0, AndroidUtilities.dp(2.0F), 0, AndroidUtilities.dp(10.0F));
         this.problemText.setTextSize(1, 15.0F);
         this.problemText.setGravity(49);
         if (this.currentType == 1) {
            this.problemText.setText(LocaleController.getString("DidNotGetTheCodeSms", 2131559267));
         } else {
            this.problemText.setText(LocaleController.getString("DidNotGetTheCode", 2131559266));
         }

         this.addView(this.problemText, LayoutHelper.createLinear(-2, -2, 49));
         this.problemText.setOnClickListener(new _$$Lambda$CancelAccountDeletionActivity$LoginActivitySmsView$72NXgFF_13BImj3ROsBvF0l8AmQ(this));
      }

      private void createCodeTimer() {
         if (this.codeTimer == null) {
            this.codeTime = 15000;
            this.codeTimer = new Timer();
            this.lastCodeTime = (double)System.currentTimeMillis();
            this.codeTimer.schedule(new TimerTask() {
               // $FF: synthetic method
               public void lambda$run$0$CancelAccountDeletionActivity$LoginActivitySmsView$4() {
                  double var1 = (double)System.currentTimeMillis();
                  double var3 = LoginActivitySmsView.this.lastCodeTime;
                  Double.isNaN(var1);
                  LoginActivitySmsView.this.lastCodeTime = var1;
                  CancelAccountDeletionActivity.LoginActivitySmsView var5 = LoginActivitySmsView.this;
                  double var6 = (double)var5.codeTime;
                  Double.isNaN(var6);
                  var5.codeTime = (int)(var6 - (var1 - var3));
                  if (LoginActivitySmsView.this.codeTime <= 1000) {
                     LoginActivitySmsView.this.problemText.setVisibility(0);
                     LoginActivitySmsView.this.timeText.setVisibility(8);
                     LoginActivitySmsView.this.destroyCodeTimer();
                  }

               }

               public void run() {
                  AndroidUtilities.runOnUIThread(new _$$Lambda$CancelAccountDeletionActivity$LoginActivitySmsView$4$30Wvb2875vVECuq8Lqz3EDACKYg(this));
               }
            }, 0L, 1000L);
         }
      }

      private void createTimer() {
         if (this.timeTimer == null) {
            this.timeTimer = new Timer();
            this.timeTimer.schedule(new TimerTask() {
               public void run() {
                  if (LoginActivitySmsView.this.timeTimer != null) {
                     AndroidUtilities.runOnUIThread(new Runnable() {
                        // $FF: synthetic method
                        public void lambda$null$0$CancelAccountDeletionActivity$LoginActivitySmsView$5$1(TLRPC.TL_error var1) {
                           LoginActivitySmsView.this.lastError = var1.text;
                        }

                        // $FF: synthetic method
                        public void lambda$run$1$CancelAccountDeletionActivity$LoginActivitySmsView$5$1(TLObject var1, TLRPC.TL_error var2) {
                           if (var2 != null && var2.text != null) {
                              AndroidUtilities.runOnUIThread(new _$$Lambda$CancelAccountDeletionActivity$LoginActivitySmsView$5$1$z9uNBVal4_U1kKMzrr3JL_AbWI4(this, var2));
                           }

                        }

                        public void run() {
                           double var1 = (double)System.currentTimeMillis();
                           double var3 = LoginActivitySmsView.this.lastCurrentTime;
                           Double.isNaN(var1);
                           CancelAccountDeletionActivity.LoginActivitySmsView var5 = LoginActivitySmsView.this;
                           double var6 = (double)var5.time;
                           Double.isNaN(var6);
                           var5.time = (int)(var6 - (var1 - var3));
                           LoginActivitySmsView.this.lastCurrentTime = var1;
                           if (LoginActivitySmsView.this.time >= 1000) {
                              int var8 = LoginActivitySmsView.this.time / 1000 / 60;
                              int var9 = LoginActivitySmsView.this.time / 1000 - var8 * 60;
                              if (LoginActivitySmsView.this.nextType != 4 && LoginActivitySmsView.this.nextType != 3) {
                                 if (LoginActivitySmsView.this.nextType == 2) {
                                    LoginActivitySmsView.this.timeText.setText(LocaleController.formatString("SmsText", 2131560793, var8, var9));
                                 }
                              } else {
                                 LoginActivitySmsView.this.timeText.setText(LocaleController.formatString("CallText", 2131558885, var8, var9));
                              }

                              if (LoginActivitySmsView.this.progressView != null) {
                                 LoginActivitySmsView.this.progressView.setProgress(1.0F - (float)LoginActivitySmsView.this.time / (float)LoginActivitySmsView.this.timeout);
                              }
                           } else {
                              if (LoginActivitySmsView.this.progressView != null) {
                                 LoginActivitySmsView.this.progressView.setProgress(1.0F);
                              }

                              LoginActivitySmsView.this.destroyTimer();
                              if (LoginActivitySmsView.this.currentType == 3) {
                                 AndroidUtilities.setWaitingForCall(false);
                                 NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveCall);
                                 LoginActivitySmsView.this.waitingForEvent = false;
                                 LoginActivitySmsView.this.destroyCodeTimer();
                                 LoginActivitySmsView.this.resendCode();
                              } else if (LoginActivitySmsView.this.currentType == 2 || LoginActivitySmsView.this.currentType == 4) {
                                 if (LoginActivitySmsView.this.nextType != 4 && LoginActivitySmsView.this.nextType != 2) {
                                    if (LoginActivitySmsView.this.nextType == 3) {
                                       AndroidUtilities.setWaitingForSms(false);
                                       NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
                                       LoginActivitySmsView.this.waitingForEvent = false;
                                       LoginActivitySmsView.this.destroyCodeTimer();
                                       LoginActivitySmsView.this.resendCode();
                                    }
                                 } else {
                                    if (LoginActivitySmsView.this.nextType == 4) {
                                       LoginActivitySmsView.this.timeText.setText(LocaleController.getString("Calling", 2131558887));
                                    } else {
                                       LoginActivitySmsView.this.timeText.setText(LocaleController.getString("SendingSms", 2131560714));
                                    }

                                    LoginActivitySmsView.this.createCodeTimer();
                                    TLRPC.TL_auth_resendCode var10 = new TLRPC.TL_auth_resendCode();
                                    var10.phone_number = LoginActivitySmsView.this.phone;
                                    var10.phone_code_hash = LoginActivitySmsView.this.phoneHash;
                                    ConnectionsManager.getInstance(CancelAccountDeletionActivity.access$3200(CancelAccountDeletionActivity.this)).sendRequest(var10, new _$$Lambda$CancelAccountDeletionActivity$LoginActivitySmsView$5$1$TKosr_VcccEYaoxCJjoMXxLoY3g(this), 2);
                                 }
                              }
                           }

                        }
                     });
                  }
               }
            }, 0L, 1000L);
         }
      }

      private void destroyCodeTimer() {
         // $FF: Couldn't be decompiled
      }

      private void destroyTimer() {
         // $FF: Couldn't be decompiled
      }

      private String getCode() {
         if (this.codeField == null) {
            return "";
         } else {
            StringBuilder var1 = new StringBuilder();
            int var2 = 0;

            while(true) {
               EditTextBoldCursor[] var3 = this.codeField;
               if (var2 >= var3.length) {
                  return var1.toString();
               }

               var1.append(PhoneFormat.stripExceptNumbers(var3[var2].getText().toString()));
               ++var2;
            }
         }
      }

      private void resendCode() {
         Bundle var1 = new Bundle();
         var1.putString("phone", this.phone);
         this.nextPressed = true;
         CancelAccountDeletionActivity.this.needShowProgress();
         TLRPC.TL_auth_resendCode var2 = new TLRPC.TL_auth_resendCode();
         var2.phone_number = this.phone;
         var2.phone_code_hash = this.phoneHash;
         ConnectionsManager.getInstance(CancelAccountDeletionActivity.access$900(CancelAccountDeletionActivity.this)).sendRequest(var2, new _$$Lambda$CancelAccountDeletionActivity$LoginActivitySmsView$ymkWVQFIhlznIih0Xq953phiTOo(this, var1, var2), 2);
      }

      public void didReceivedNotification(int var1, int var2, Object... var3) {
         if (this.waitingForEvent) {
            EditTextBoldCursor[] var4 = this.codeField;
            if (var4 != null) {
               StringBuilder var7;
               if (var1 == NotificationCenter.didReceiveSmsCode) {
                  EditTextBoldCursor var5 = var4[0];
                  var7 = new StringBuilder();
                  var7.append("");
                  var7.append(var3[0]);
                  var5.setText(var7.toString());
                  this.onNextPressed();
               } else if (var1 == NotificationCenter.didReceiveCall) {
                  var7 = new StringBuilder();
                  var7.append("");
                  var7.append(var3[0]);
                  String var6 = var7.toString();
                  if (!AndroidUtilities.checkPhonePattern(this.pattern, var6)) {
                     return;
                  }

                  this.ignoreOnTextChange = true;
                  this.codeField[0].setText(var6);
                  this.ignoreOnTextChange = false;
                  this.onNextPressed();
               }
            }
         }

      }

      public String getHeaderName() {
         return this.currentType == 1 ? this.phone : LocaleController.getString("CancelAccountReset", 2131558892);
      }

      // $FF: synthetic method
      public void lambda$new$0$CancelAccountDeletionActivity$LoginActivitySmsView(View var1) {
         if (!this.nextPressed) {
            boolean var2;
            if ((this.nextType != 4 || this.currentType != 2) && this.nextType != 0) {
               var2 = false;
            } else {
               var2 = true;
            }

            if (!var2) {
               this.resendCode();
            } else {
               try {
                  PackageInfo var6 = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
                  String var7 = String.format(Locale.US, "%s (%d)", var6.versionName, var6.versionCode);
                  Intent var3 = new Intent("android.intent.action.SEND");
                  var3.setType("message/rfc822");
                  var3.putExtra("android.intent.extra.EMAIL", new String[]{"sms@stel.com"});
                  StringBuilder var4 = new StringBuilder();
                  var4.append("Android cancel account deletion issue ");
                  var4.append(var7);
                  var4.append(" ");
                  var4.append(this.phone);
                  var3.putExtra("android.intent.extra.SUBJECT", var4.toString());
                  var4 = new StringBuilder();
                  var4.append("Phone: ");
                  var4.append(this.phone);
                  var4.append("\nApp version: ");
                  var4.append(var7);
                  var4.append("\nOS version: SDK ");
                  var4.append(VERSION.SDK_INT);
                  var4.append("\nDevice Name: ");
                  var4.append(Build.MANUFACTURER);
                  var4.append(Build.MODEL);
                  var4.append("\nLocale: ");
                  var4.append(Locale.getDefault());
                  var4.append("\nError: ");
                  var4.append(this.lastError);
                  var3.putExtra("android.intent.extra.TEXT", var4.toString());
                  this.getContext().startActivity(Intent.createChooser(var3, "Send email..."));
               } catch (Exception var5) {
                  AlertsCreator.showSimpleAlert(CancelAccountDeletionActivity.this, LocaleController.getString("NoMailInstalled", 2131559927));
               }
            }

         }
      }

      // $FF: synthetic method
      public void lambda$null$1$CancelAccountDeletionActivity$LoginActivitySmsView(DialogInterface var1, int var2) {
         this.onBackPressed(true);
         CancelAccountDeletionActivity.this.finishFragment();
      }

      // $FF: synthetic method
      public void lambda$null$2$CancelAccountDeletionActivity$LoginActivitySmsView(TLRPC.TL_error var1, Bundle var2, TLObject var3, TLRPC.TL_auth_resendCode var4) {
         this.nextPressed = false;
         if (var1 == null) {
            CancelAccountDeletionActivity.this.fillNextCodeParams(var2, (TLRPC.TL_auth_sentCode)var3);
         } else if (var1.text != null) {
            AlertDialog var5 = (AlertDialog)AlertsCreator.processError(CancelAccountDeletionActivity.access$3600(CancelAccountDeletionActivity.this), var1, CancelAccountDeletionActivity.this, var4);
            if (var5 != null && var1.text.contains("PHONE_CODE_EXPIRED")) {
               var5.setPositiveButtonListener(new _$$Lambda$CancelAccountDeletionActivity$LoginActivitySmsView$4p7iOR1S_omGhM3JTRpI2lxpfeQ(this));
            }
         }

         CancelAccountDeletionActivity.this.needHideProgress();
      }

      // $FF: synthetic method
      public void lambda$null$6$CancelAccountDeletionActivity$LoginActivitySmsView(TLRPC.TL_error var1, TLRPC.TL_account_confirmPhone var2) {
         CancelAccountDeletionActivity.this.needHideProgress();
         this.nextPressed = false;
         if (var1 == null) {
            CancelAccountDeletionActivity var3 = CancelAccountDeletionActivity.this;
            PhoneFormat var7 = PhoneFormat.getInstance();
            StringBuilder var5 = new StringBuilder();
            var5.append("+");
            var5.append(this.phone);
            var3.errorDialog = AlertsCreator.showSimpleAlert(var3, LocaleController.formatString("CancelLinkSuccess", 2131558896, var7.format(var5.toString())));
         } else {
            int var4;
            label63: {
               label68: {
                  this.lastError = var1.text;
                  if (this.currentType == 3) {
                     var4 = this.nextType;
                     if (var4 == 4 || var4 == 2) {
                        break label68;
                     }
                  }

                  if (this.currentType == 2) {
                     var4 = this.nextType;
                     if (var4 == 4 || var4 == 3) {
                        break label68;
                     }
                  }

                  if (this.currentType != 4 || this.nextType != 2) {
                     break label63;
                  }
               }

               this.createTimer();
            }

            var4 = this.currentType;
            if (var4 == 2) {
               AndroidUtilities.setWaitingForSms(true);
               NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveSmsCode);
            } else if (var4 == 3) {
               AndroidUtilities.setWaitingForCall(true);
               NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveCall);
            }

            this.waitingForEvent = true;
            if (this.currentType != 3) {
               AlertsCreator.processError(CancelAccountDeletionActivity.access$3500(CancelAccountDeletionActivity.this), var1, CancelAccountDeletionActivity.this, var2);
            }

            if (!var1.text.contains("PHONE_CODE_EMPTY") && !var1.text.contains("PHONE_CODE_INVALID")) {
               if (var1.text.contains("PHONE_CODE_EXPIRED")) {
                  this.onBackPressed(true);
                  CancelAccountDeletionActivity.this.setPage(0, true, (Bundle)null, true);
               }
            } else {
               var4 = 0;

               while(true) {
                  EditTextBoldCursor[] var6 = this.codeField;
                  if (var4 >= var6.length) {
                     var6[0].requestFocus();
                     break;
                  }

                  var6[var4].setText("");
                  ++var4;
               }
            }
         }

      }

      // $FF: synthetic method
      public void lambda$onNextPressed$7$CancelAccountDeletionActivity$LoginActivitySmsView(TLRPC.TL_account_confirmPhone var1, TLObject var2, TLRPC.TL_error var3) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$CancelAccountDeletionActivity$LoginActivitySmsView$iU1Eg1dGYY6sRGDfzMe2rfeuHuA(this, var3, var1));
      }

      // $FF: synthetic method
      public void lambda$onShow$8$CancelAccountDeletionActivity$LoginActivitySmsView() {
         EditTextBoldCursor[] var1 = this.codeField;
         if (var1 != null) {
            for(int var2 = var1.length - 1; var2 >= 0; --var2) {
               if (var2 == 0 || this.codeField[var2].length() != 0) {
                  this.codeField[var2].requestFocus();
                  var1 = this.codeField;
                  var1[var2].setSelection(var1[var2].length());
                  AndroidUtilities.showKeyboard(this.codeField[var2]);
                  break;
               }
            }
         }

      }

      // $FF: synthetic method
      public void lambda$resendCode$3$CancelAccountDeletionActivity$LoginActivitySmsView(Bundle var1, TLRPC.TL_auth_resendCode var2, TLObject var3, TLRPC.TL_error var4) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$CancelAccountDeletionActivity$LoginActivitySmsView$6BL0ZFjSY7z5vTmWlXT9OO5buPM(this, var4, var1, var3, var2));
      }

      // $FF: synthetic method
      public boolean lambda$setParams$4$CancelAccountDeletionActivity$LoginActivitySmsView(int var1, View var2, int var3, KeyEvent var4) {
         if (var3 == 67 && this.codeField[var1].length() == 0 && var1 > 0) {
            EditTextBoldCursor[] var5 = this.codeField;
            --var1;
            var5[var1].setSelection(var5[var1].length());
            this.codeField[var1].requestFocus();
            this.codeField[var1].dispatchKeyEvent(var4);
            return true;
         } else {
            return false;
         }
      }

      // $FF: synthetic method
      public boolean lambda$setParams$5$CancelAccountDeletionActivity$LoginActivitySmsView(TextView var1, int var2, KeyEvent var3) {
         if (var2 == 5) {
            this.onNextPressed();
            return true;
         } else {
            return false;
         }
      }

      public boolean needBackButton() {
         return true;
      }

      public void onDestroyActivity() {
         super.onDestroyActivity();
         int var1 = this.currentType;
         if (var1 == 2) {
            AndroidUtilities.setWaitingForSms(false);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
         } else if (var1 == 3) {
            AndroidUtilities.setWaitingForCall(false);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveCall);
         }

         this.waitingForEvent = false;
         this.destroyTimer();
         this.destroyCodeTimer();
      }

      protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
         super.onLayout(var1, var2, var3, var4, var5);
         if (this.currentType != 3 && this.blueImageView != null) {
            var3 = this.confirmTextView.getBottom();
            var2 = this.getMeasuredHeight() - var3;
            TextView var6;
            if (this.problemText.getVisibility() == 0) {
               var4 = this.problemText.getMeasuredHeight();
               var2 = var2 + var3 - var4;
               var6 = this.problemText;
               var6.layout(var6.getLeft(), var2, this.problemText.getRight(), var4 + var2);
            } else if (this.timeText.getVisibility() == 0) {
               var4 = this.timeText.getMeasuredHeight();
               var2 = var2 + var3 - var4;
               var6 = this.timeText;
               var6.layout(var6.getLeft(), var2, this.timeText.getRight(), var4 + var2);
            } else {
               var2 += var3;
            }

            var4 = this.codeFieldContainer.getMeasuredHeight();
            var2 = (var2 - var3 - var4) / 2 + var3;
            LinearLayout var7 = this.codeFieldContainer;
            var7.layout(var7.getLeft(), var2, this.codeFieldContainer.getRight(), var4 + var2);
         }

      }

      protected void onMeasure(int var1, int var2) {
         super.onMeasure(var1, var2);
         if (this.currentType != 3) {
            ImageView var3 = this.blueImageView;
            if (var3 != null) {
               var2 = var3.getMeasuredHeight() + this.titleTextView.getMeasuredHeight() + this.confirmTextView.getMeasuredHeight() + AndroidUtilities.dp(35.0F);
               var1 = AndroidUtilities.dp(80.0F);
               int var4 = AndroidUtilities.dp(291.0F);
               if (CancelAccountDeletionActivity.this.scrollHeight - var2 < var1) {
                  this.setMeasuredDimension(this.getMeasuredWidth(), var2 + var1);
               } else if (CancelAccountDeletionActivity.this.scrollHeight > var4) {
                  this.setMeasuredDimension(this.getMeasuredWidth(), var4);
               } else {
                  this.setMeasuredDimension(this.getMeasuredWidth(), CancelAccountDeletionActivity.this.scrollHeight);
               }
            }
         }

      }

      public void onNextPressed() {
         if (!this.nextPressed) {
            String var1 = this.getCode();
            if (TextUtils.isEmpty(var1)) {
               AndroidUtilities.shakeView(this.codeFieldContainer, 2.0F, 0);
            } else {
               this.nextPressed = true;
               int var2 = this.currentType;
               if (var2 == 2) {
                  AndroidUtilities.setWaitingForSms(false);
                  NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
               } else if (var2 == 3) {
                  AndroidUtilities.setWaitingForCall(false);
                  NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveCall);
               }

               this.waitingForEvent = false;
               TLRPC.TL_account_confirmPhone var3 = new TLRPC.TL_account_confirmPhone();
               var3.phone_code = var1;
               var3.phone_code_hash = this.phoneHash;
               this.destroyTimer();
               CancelAccountDeletionActivity.this.needShowProgress();
               ConnectionsManager.getInstance(CancelAccountDeletionActivity.access$3400(CancelAccountDeletionActivity.this)).sendRequest(var3, new _$$Lambda$CancelAccountDeletionActivity$LoginActivitySmsView$1W4kZzPjzRDtXCpazovyaTPi9gk(this, var3), 2);
            }
         }
      }

      public void onShow() {
         super.onShow();
         if (this.currentType != 3) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$CancelAccountDeletionActivity$LoginActivitySmsView$dLIqV278x4RUePN_aKF0rcdGRXU(this), 100L);
         }
      }

      public void setParams(Bundle var1, boolean var2) {
         if (var1 != null) {
            this.waitingForEvent = true;
            int var3 = this.currentType;
            if (var3 == 2) {
               AndroidUtilities.setWaitingForSms(true);
               NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveSmsCode);
            } else if (var3 == 3) {
               AndroidUtilities.setWaitingForCall(true);
               NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveCall);
            }

            this.currentParams = var1;
            this.phone = var1.getString("phone");
            this.phoneHash = var1.getString("phoneHash");
            var3 = var1.getInt("timeout");
            this.time = var3;
            this.timeout = var3;
            this.openTime = (int)(System.currentTimeMillis() / 1000L);
            this.nextType = var1.getInt("nextType");
            this.pattern = var1.getString("pattern");
            this.length = var1.getInt("length");
            if (this.length == 0) {
               this.length = 5;
            }

            EditTextBoldCursor[] var8 = this.codeField;
            byte var14 = 8;
            final int var4;
            if (var8 != null && var8.length == this.length) {
               var4 = 0;

               while(true) {
                  var8 = this.codeField;
                  if (var4 >= var8.length) {
                     break;
                  }

                  var8[var4].setText("");
                  ++var4;
               }
            } else {
               this.codeField = new EditTextBoldCursor[this.length];

               for(var4 = 0; var4 < this.length; ++var4) {
                  this.codeField[var4] = new EditTextBoldCursor(this.getContext());
                  this.codeField[var4].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                  this.codeField[var4].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                  this.codeField[var4].setCursorSize(AndroidUtilities.dp(20.0F));
                  this.codeField[var4].setCursorWidth(1.5F);
                  Drawable var9 = this.getResources().getDrawable(2131165811).mutate();
                  var9.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteInputFieldActivated"), Mode.MULTIPLY));
                  this.codeField[var4].setBackgroundDrawable(var9);
                  this.codeField[var4].setImeOptions(268435461);
                  this.codeField[var4].setTextSize(1, 20.0F);
                  this.codeField[var4].setMaxLines(1);
                  this.codeField[var4].setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                  this.codeField[var4].setPadding(0, 0, 0, 0);
                  this.codeField[var4].setGravity(49);
                  if (this.currentType == 3) {
                     this.codeField[var4].setEnabled(false);
                     this.codeField[var4].setInputType(0);
                     this.codeField[var4].setVisibility(8);
                  } else {
                     this.codeField[var4].setInputType(3);
                  }

                  LinearLayout var10 = this.codeFieldContainer;
                  EditTextBoldCursor var5 = this.codeField[var4];
                  byte var6;
                  if (var4 != this.length - 1) {
                     var6 = 7;
                  } else {
                     var6 = 0;
                  }

                  var10.addView(var5, LayoutHelper.createLinear(34, 36, 1, 0, 0, var6, 0));
                  this.codeField[var4].addTextChangedListener(new TextWatcher() {
                     public void afterTextChanged(Editable var1) {
                        if (!LoginActivitySmsView.this.ignoreOnTextChange) {
                           int var2 = var1.length();
                           if (var2 >= 1) {
                              if (var2 > 1) {
                                 String var3 = var1.toString();
                                 LoginActivitySmsView.this.ignoreOnTextChange = true;

                                 for(int var4x = 0; var4x < Math.min(LoginActivitySmsView.this.length - var4, var2); ++var4x) {
                                    if (var4x == 0) {
                                       var1.replace(0, var2, var3.substring(var4x, var4x + 1));
                                    } else {
                                       LoginActivitySmsView.this.codeField[var4 + var4x].setText(var3.substring(var4x, var4x + 1));
                                    }
                                 }

                                 LoginActivitySmsView.this.ignoreOnTextChange = false;
                              }

                              if (var4 != LoginActivitySmsView.this.length - 1) {
                                 LoginActivitySmsView.this.codeField[var4 + 1].setSelection(LoginActivitySmsView.this.codeField[var4 + 1].length());
                                 LoginActivitySmsView.this.codeField[var4 + 1].requestFocus();
                              }

                              if ((var4 == LoginActivitySmsView.this.length - 1 || var4 == LoginActivitySmsView.this.length - 2 && var2 >= 2) && LoginActivitySmsView.this.getCode().length() == LoginActivitySmsView.this.length) {
                                 LoginActivitySmsView.this.onNextPressed();
                              }
                           }

                        }
                     }

                     public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4x) {
                     }

                     public void onTextChanged(CharSequence var1, int var2, int var3, int var4x) {
                     }
                  });
                  this.codeField[var4].setOnKeyListener(new _$$Lambda$CancelAccountDeletionActivity$LoginActivitySmsView$ZA7_mAFfTFMlQCzBbBXS9uV3KIM(this, var4));
                  this.codeField[var4].setOnEditorActionListener(new _$$Lambda$CancelAccountDeletionActivity$LoginActivitySmsView$jgzzj4_t91kdP6uXqtjWCi0O3d8(this));
               }
            }

            CancelAccountDeletionActivity.ProgressView var11 = this.progressView;
            byte var16;
            if (var11 != null) {
               if (this.nextType != 0) {
                  var16 = 0;
               } else {
                  var16 = 8;
               }

               var11.setVisibility(var16);
            }

            if (this.phone != null) {
               String var17 = PhoneFormat.getInstance().format(this.phone);
               PhoneFormat var7 = PhoneFormat.getInstance();
               StringBuilder var12 = new StringBuilder();
               var12.append("+");
               var12.append(var17);
               SpannableStringBuilder var13 = AndroidUtilities.replaceTags(LocaleController.formatString("CancelAccountResetInfo", 2131558893, var7.format(var12.toString())));
               this.confirmTextView.setText(var13);
               if (this.currentType != 3) {
                  AndroidUtilities.showKeyboard(this.codeField[0]);
                  this.codeField[0].requestFocus();
               } else {
                  AndroidUtilities.hideKeyboard(this.codeField[0]);
               }

               this.destroyTimer();
               this.destroyCodeTimer();
               this.lastCurrentTime = (double)System.currentTimeMillis();
               var4 = this.currentType;
               if (var4 == 1) {
                  this.problemText.setVisibility(0);
                  this.timeText.setVisibility(8);
               } else {
                  if (var4 == 3) {
                     var4 = this.nextType;
                     if (var4 == 4 || var4 == 2) {
                        this.problemText.setVisibility(8);
                        this.timeText.setVisibility(0);
                        var3 = this.nextType;
                        if (var3 == 4) {
                           this.timeText.setText(LocaleController.formatString("CallText", 2131558885, 1, 0));
                        } else if (var3 == 2) {
                           this.timeText.setText(LocaleController.formatString("SmsText", 2131560793, 1, 0));
                        }

                        this.createTimer();
                        return;
                     }
                  }

                  TextView var15;
                  if (this.currentType == 2) {
                     var4 = this.nextType;
                     if (var4 == 4 || var4 == 3) {
                        this.timeText.setText(LocaleController.formatString("CallText", 2131558885, 2, 0));
                        var15 = this.problemText;
                        if (this.time < 1000) {
                           var16 = 0;
                        } else {
                           var16 = 8;
                        }

                        var15.setVisibility(var16);
                        var15 = this.timeText;
                        if (this.time >= 1000) {
                           var14 = 0;
                        }

                        var15.setVisibility(var14);
                        this.createTimer();
                        return;
                     }
                  }

                  if (this.currentType == 4 && this.nextType == 2) {
                     this.timeText.setText(LocaleController.formatString("SmsText", 2131560793, 2, 0));
                     var15 = this.problemText;
                     if (this.time < 1000) {
                        var16 = 0;
                     } else {
                        var16 = 8;
                     }

                     var15.setVisibility(var16);
                     var15 = this.timeText;
                     if (this.time >= 1000) {
                        var14 = 0;
                     }

                     var15.setVisibility(var14);
                     this.createTimer();
                  } else {
                     this.timeText.setVisibility(8);
                     this.problemText.setVisibility(8);
                     this.createCodeTimer();
                  }
               }

            }
         }
      }
   }

   public class PhoneView extends SlideView {
      private boolean nextPressed = false;
      private RadialProgressView progressBar;

      public PhoneView(Context var2) {
         super(var2);
         this.setOrientation(1);
         FrameLayout var3 = new FrameLayout(var2);
         this.addView(var3, LayoutHelper.createLinear(-1, 200));
         this.progressBar = new RadialProgressView(var2);
         var3.addView(this.progressBar, LayoutHelper.createFrame(-2, -2, 17));
      }

      public String getHeaderName() {
         return LocaleController.getString("CancelAccountReset", 2131558892);
      }

      // $FF: synthetic method
      public void lambda$null$0$CancelAccountDeletionActivity$PhoneView(TLRPC.TL_error var1, Bundle var2, TLObject var3, TLRPC.TL_account_sendConfirmPhoneCode var4) {
         this.nextPressed = false;
         if (var1 == null) {
            CancelAccountDeletionActivity.this.fillNextCodeParams(var2, (TLRPC.TL_auth_sentCode)var3);
         } else {
            CancelAccountDeletionActivity var5 = CancelAccountDeletionActivity.this;
            var5.errorDialog = AlertsCreator.processError(CancelAccountDeletionActivity.access$800(var5), var1, CancelAccountDeletionActivity.this, var4);
         }

      }

      // $FF: synthetic method
      public void lambda$onNextPressed$1$CancelAccountDeletionActivity$PhoneView(Bundle var1, TLRPC.TL_account_sendConfirmPhoneCode var2, TLObject var3, TLRPC.TL_error var4) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$CancelAccountDeletionActivity$PhoneView$P6GwpG9TiT0cGhXBWpHiOCU7n94(this, var4, var1, var3, var2));
      }

      public void onNextPressed() {
         if (CancelAccountDeletionActivity.this.getParentActivity() != null && !this.nextPressed) {
            TelephonyManager var1 = (TelephonyManager)ApplicationLoader.applicationContext.getSystemService("phone");
            if (var1.getSimState() != 1) {
               var1.getPhoneType();
            }

            int var2 = VERSION.SDK_INT;
            TLRPC.TL_account_sendConfirmPhoneCode var3 = new TLRPC.TL_account_sendConfirmPhoneCode();
            var3.hash = CancelAccountDeletionActivity.this.hash;
            var3.settings = new TLRPC.TL_codeSettings();
            TLRPC.TL_codeSettings var4 = var3.settings;
            var4.allow_flashcall = false;
            if (VERSION.SDK_INT >= 26) {
               try {
                  SmsManager var5 = SmsManager.getDefault();
                  Context var6 = ApplicationLoader.applicationContext;
                  Intent var7 = new Intent(ApplicationLoader.applicationContext, SmsReceiver.class);
                  var4.app_hash = var5.createAppSpecificSmsToken(PendingIntent.getBroadcast(var6, 0, var7, 134217728));
               } catch (Throwable var9) {
                  FileLog.e(var9);
               }
            } else {
               var4.app_hash = BuildVars.SMS_HASH;
               var4.app_hash_persistent = true;
            }

            SharedPreferences var13 = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
            if (!TextUtils.isEmpty(var3.settings.app_hash)) {
               TLRPC.TL_codeSettings var12 = var3.settings;
               var12.flags |= 8;
               var13.edit().putString("sms_hash", var3.settings.app_hash).commit();
            } else {
               var13.edit().remove("sms_hash").commit();
            }

            if (var3.settings.allow_flashcall) {
               try {
                  String var10 = var1.getLine1Number();
                  if (!TextUtils.isEmpty(var10)) {
                     var3.settings.current_number = PhoneNumberUtils.compare(CancelAccountDeletionActivity.this.phone, var10);
                     if (!var3.settings.current_number) {
                        var3.settings.allow_flashcall = false;
                     }
                  } else {
                     var3.settings.current_number = false;
                  }
               } catch (Exception var8) {
                  var3.settings.allow_flashcall = false;
                  FileLog.e((Throwable)var8);
               }
            }

            Bundle var11 = new Bundle();
            var11.putString("phone", CancelAccountDeletionActivity.this.phone);
            this.nextPressed = true;
            ConnectionsManager.getInstance(CancelAccountDeletionActivity.access$500(CancelAccountDeletionActivity.this)).sendRequest(var3, new _$$Lambda$CancelAccountDeletionActivity$PhoneView$k7vfL3HDSHw4EqLEYwRz3mk5u4I(this, var11, var3), 2);
         }

      }

      public void onShow() {
         super.onShow();
         this.onNextPressed();
      }
   }

   private class ProgressView extends View {
      private Paint paint = new Paint();
      private Paint paint2 = new Paint();
      private float progress;

      public ProgressView(Context var2) {
         super(var2);
         this.paint.setColor(Theme.getColor("login_progressInner"));
         this.paint2.setColor(Theme.getColor("login_progressOuter"));
      }

      protected void onDraw(Canvas var1) {
         float var2 = (float)((int)((float)this.getMeasuredWidth() * this.progress));
         var1.drawRect(0.0F, 0.0F, var2, (float)this.getMeasuredHeight(), this.paint2);
         var1.drawRect(var2, 0.0F, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), this.paint);
      }

      public void setProgress(float var1) {
         this.progress = var1;
         this.invalidate();
      }
   }
}
