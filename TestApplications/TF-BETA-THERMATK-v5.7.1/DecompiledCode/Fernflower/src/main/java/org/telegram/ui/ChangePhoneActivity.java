package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
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
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.InputFilter.LengthFilter;
import android.text.TextUtils.TruncateAt;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SmsReceiver;
import org.telegram.messenger.UserConfig;
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
import org.telegram.ui.Components.HintEditText;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.SlideView;

public class ChangePhoneActivity extends BaseFragment {
   private static final int done_button = 1;
   private boolean checkPermissions = true;
   private int currentViewNum = 0;
   private View doneButton;
   private Dialog permissionsDialog;
   private ArrayList permissionsItems = new ArrayList();
   private AlertDialog progressDialog;
   private int scrollHeight;
   private SlideView[] views = new SlideView[5];

   // $FF: synthetic method
   static int access$1600(ChangePhoneActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1800(ChangePhoneActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1900(ChangePhoneActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$4200(ChangePhoneActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$4400(ChangePhoneActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$4500(ChangePhoneActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$4600(ChangePhoneActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$4700(ChangePhoneActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$4800(ChangePhoneActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$4900(ChangePhoneActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$5000(ChangePhoneActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$5100(ChangePhoneActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$5200(ChangePhoneActivity var0) {
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
               ChangePhoneActivity.this.views[ChangePhoneActivity.this.currentViewNum].onNextPressed();
            } else if (var1 == -1) {
               ChangePhoneActivity.this.finishFragment();
            }

         }
      });
      this.doneButton = super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0F));
      ScrollView var2 = new ScrollView(var1) {
         protected void onMeasure(int var1, int var2) {
            ChangePhoneActivity.this.scrollHeight = MeasureSpec.getSize(var2) - AndroidUtilities.dp(30.0F);
            super.onMeasure(var1, var2);
         }

         public boolean requestChildRectangleOnScreen(View var1, Rect var2, boolean var3) {
            if (ChangePhoneActivity.this.currentViewNum == 1 || ChangePhoneActivity.this.currentViewNum == 2 || ChangePhoneActivity.this.currentViewNum == 4) {
               var2.bottom += AndroidUtilities.dp(40.0F);
            }

            return super.requestChildRectangleOnScreen(var1, var2, var3);
         }
      };
      var2.setFillViewport(true);
      super.fragmentView = var2;
      FrameLayout var3 = new FrameLayout(var1);
      var2.addView(var3, LayoutHelper.createScroll(-1, -2, 51));
      this.views[0] = new ChangePhoneActivity.PhoneView(var1);
      this.views[1] = new ChangePhoneActivity.LoginActivitySmsView(var1, 1);
      this.views[2] = new ChangePhoneActivity.LoginActivitySmsView(var1, 2);
      this.views[3] = new ChangePhoneActivity.LoginActivitySmsView(var1, 3);
      this.views[4] = new ChangePhoneActivity.LoginActivitySmsView(var1, 4);
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
      ChangePhoneActivity.PhoneView var2 = (ChangePhoneActivity.PhoneView)var1[0];
      ChangePhoneActivity.LoginActivitySmsView var3 = (ChangePhoneActivity.LoginActivitySmsView)var1[1];
      ChangePhoneActivity.LoginActivitySmsView var4 = (ChangePhoneActivity.LoginActivitySmsView)var1[2];
      ChangePhoneActivity.LoginActivitySmsView var5 = (ChangePhoneActivity.LoginActivitySmsView)var1[3];
      ChangePhoneActivity.LoginActivitySmsView var8 = (ChangePhoneActivity.LoginActivitySmsView)var1[4];
      ArrayList var6 = new ArrayList();
      var6.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
      var6.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"));
      var6.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"));
      var6.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon"));
      var6.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle"));
      var6.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector"));
      var6.add(new ThemeDescription(var2.countryButton, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
      var6.add(new ThemeDescription(var2.view, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayLine"));
      var6.add(new ThemeDescription(var2.textView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
      var6.add(new ThemeDescription(var2.codeField, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
      var6.add(new ThemeDescription(var2.codeField, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputField"));
      var6.add(new ThemeDescription(var2.codeField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"));
      var6.add(new ThemeDescription(var2.phoneField, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
      var6.add(new ThemeDescription(var2.phoneField, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"));
      var6.add(new ThemeDescription(var2.phoneField, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputField"));
      var6.add(new ThemeDescription(var2.phoneField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"));
      var6.add(new ThemeDescription(var2.textView2, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
      var6.add(new ThemeDescription(var3.confirmTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
      var6.add(new ThemeDescription(var3.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
      int var7;
      if (var3.codeField != null) {
         for(var7 = 0; var7 < var3.codeField.length; ++var7) {
            var6.add(new ThemeDescription(var3.codeField[var7], ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
            var6.add(new ThemeDescription(var3.codeField[var7], ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"));
         }
      }

      var6.add(new ThemeDescription(var3.timeText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
      var6.add(new ThemeDescription(var3.problemText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText4"));
      var6.add(new ThemeDescription(var3.progressView, 0, new Class[]{ChangePhoneActivity.ProgressView.class}, new String[]{"paint"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "login_progressInner"));
      var6.add(new ThemeDescription(var3.progressView, 0, new Class[]{ChangePhoneActivity.ProgressView.class}, new String[]{"paint"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "login_progressOuter"));
      var6.add(new ThemeDescription(var3.blackImageView, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
      var6.add(new ThemeDescription(var3.blueImageView, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_actionBackground"));
      var6.add(new ThemeDescription(var4.confirmTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
      var6.add(new ThemeDescription(var4.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
      if (var4.codeField != null) {
         for(var7 = 0; var7 < var4.codeField.length; ++var7) {
            var6.add(new ThemeDescription(var4.codeField[var7], ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
            var6.add(new ThemeDescription(var4.codeField[var7], ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"));
         }
      }

      var6.add(new ThemeDescription(var4.timeText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
      var6.add(new ThemeDescription(var4.problemText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText4"));
      var6.add(new ThemeDescription(var4.progressView, 0, new Class[]{ChangePhoneActivity.ProgressView.class}, new String[]{"paint"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "login_progressInner"));
      var6.add(new ThemeDescription(var4.progressView, 0, new Class[]{ChangePhoneActivity.ProgressView.class}, new String[]{"paint"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "login_progressOuter"));
      var6.add(new ThemeDescription(var4.blackImageView, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
      var6.add(new ThemeDescription(var4.blueImageView, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_actionBackground"));
      var6.add(new ThemeDescription(var5.confirmTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
      var6.add(new ThemeDescription(var5.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
      if (var5.codeField != null) {
         for(var7 = 0; var7 < var5.codeField.length; ++var7) {
            var6.add(new ThemeDescription(var5.codeField[var7], ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
            var6.add(new ThemeDescription(var5.codeField[var7], ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"));
         }
      }

      var6.add(new ThemeDescription(var5.timeText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
      var6.add(new ThemeDescription(var5.problemText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText4"));
      var6.add(new ThemeDescription(var5.progressView, 0, new Class[]{ChangePhoneActivity.ProgressView.class}, new String[]{"paint"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "login_progressInner"));
      var6.add(new ThemeDescription(var5.progressView, 0, new Class[]{ChangePhoneActivity.ProgressView.class}, new String[]{"paint"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "login_progressOuter"));
      var6.add(new ThemeDescription(var5.blackImageView, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
      var6.add(new ThemeDescription(var5.blueImageView, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_actionBackground"));
      var6.add(new ThemeDescription(var8.confirmTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
      var6.add(new ThemeDescription(var8.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
      if (var8.codeField != null) {
         for(var7 = 0; var7 < var8.codeField.length; ++var7) {
            var6.add(new ThemeDescription(var8.codeField[var7], ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
            var6.add(new ThemeDescription(var8.codeField[var7], ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"));
         }
      }

      var6.add(new ThemeDescription(var8.timeText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
      var6.add(new ThemeDescription(var8.problemText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText4"));
      var6.add(new ThemeDescription(var8.progressView, 0, new Class[]{ChangePhoneActivity.ProgressView.class}, new String[]{"paint"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "login_progressInner"));
      var6.add(new ThemeDescription(var8.progressView, 0, new Class[]{ChangePhoneActivity.ProgressView.class}, new String[]{"paint"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "login_progressOuter"));
      var6.add(new ThemeDescription(var8.blackImageView, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
      var6.add(new ThemeDescription(var8.blueImageView, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_actionBackground"));
      return (ThemeDescription[])var6.toArray(new ThemeDescription[var6.size()]);
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
      int var1 = this.currentViewNum;
      int var2 = 0;
      if (var1 == 0) {
         while(true) {
            SlideView[] var3 = this.views;
            if (var2 >= var3.length) {
               return true;
            }

            if (var3[var2] != null) {
               var3[var2].onDestroyActivity();
            }

            ++var2;
         }
      } else {
         if (this.views[var1].onBackPressed(false)) {
            this.setPage(0, true, (Bundle)null, true);
         }

         return false;
      }
   }

   protected void onDialogDismiss(Dialog var1) {
      if (VERSION.SDK_INT >= 23 && var1 == this.permissionsDialog && !this.permissionsItems.isEmpty()) {
         Activity var3 = this.getParentActivity();
         ArrayList var2 = this.permissionsItems;
         var3.requestPermissions((String[])var2.toArray(new String[var2.size()]), 6);
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
      if (var1 == 3) {
         this.doneButton.setVisibility(8);
      } else {
         if (var1 == 0) {
            this.checkPermissions = true;
         }

         this.doneButton.setVisibility(0);
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
      private String emailPhone;
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
      private ChangePhoneActivity.ProgressView progressView;
      private String requestPhone;
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
            this.progressView = ChangePhoneActivity.this.new ProgressView(var2);
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
         this.problemText.setOnClickListener(new _$$Lambda$ChangePhoneActivity$LoginActivitySmsView$rfmfbNz40o5r3LlbaUeCqZ1rJcg(this));
      }

      private void createCodeTimer() {
         if (this.codeTimer == null) {
            this.codeTime = 15000;
            this.codeTimer = new Timer();
            this.lastCodeTime = (double)System.currentTimeMillis();
            this.codeTimer.schedule(new TimerTask() {
               // $FF: synthetic method
               public void lambda$run$0$ChangePhoneActivity$LoginActivitySmsView$4() {
                  double var1 = (double)System.currentTimeMillis();
                  double var3 = LoginActivitySmsView.this.lastCodeTime;
                  Double.isNaN(var1);
                  LoginActivitySmsView.this.lastCodeTime = var1;
                  ChangePhoneActivity.LoginActivitySmsView var5 = LoginActivitySmsView.this;
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
                  AndroidUtilities.runOnUIThread(new _$$Lambda$ChangePhoneActivity$LoginActivitySmsView$4$xo4JwLP5hAjbxHZTrGyLddRD9_8(this));
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
                        public void lambda$null$0$ChangePhoneActivity$LoginActivitySmsView$5$1(TLRPC.TL_error var1) {
                           LoginActivitySmsView.this.lastError = var1.text;
                        }

                        // $FF: synthetic method
                        public void lambda$run$1$ChangePhoneActivity$LoginActivitySmsView$5$1(TLObject var1, TLRPC.TL_error var2) {
                           if (var2 != null && var2.text != null) {
                              AndroidUtilities.runOnUIThread(new _$$Lambda$ChangePhoneActivity$LoginActivitySmsView$5$1$FMLmEU_ubtOj51_10AYQOG59ia0(this, var2));
                           }

                        }

                        public void run() {
                           double var1 = (double)System.currentTimeMillis();
                           double var3 = LoginActivitySmsView.this.lastCurrentTime;
                           Double.isNaN(var1);
                           ChangePhoneActivity.LoginActivitySmsView var5 = LoginActivitySmsView.this;
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
                                    var10.phone_number = LoginActivitySmsView.this.requestPhone;
                                    var10.phone_code_hash = LoginActivitySmsView.this.phoneHash;
                                    ConnectionsManager.getInstance(ChangePhoneActivity.access$4200(ChangePhoneActivity.this)).sendRequest(var10, new _$$Lambda$ChangePhoneActivity$LoginActivitySmsView$5$1$eJjB5myTNL1kqHYNqARreg4ju8A(this), 2);
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

      // $FF: synthetic method
      static void lambda$onBackPressed$9(TLObject var0, TLRPC.TL_error var1) {
      }

      private void resendCode() {
         Bundle var1 = new Bundle();
         var1.putString("phone", this.phone);
         var1.putString("ephone", this.emailPhone);
         var1.putString("phoneFormated", this.requestPhone);
         this.nextPressed = true;
         ChangePhoneActivity.this.needShowProgress();
         TLRPC.TL_auth_resendCode var2 = new TLRPC.TL_auth_resendCode();
         var2.phone_number = this.requestPhone;
         var2.phone_code_hash = this.phoneHash;
         ConnectionsManager.getInstance(ChangePhoneActivity.access$1900(ChangePhoneActivity.this)).sendRequest(var2, new _$$Lambda$ChangePhoneActivity$LoginActivitySmsView$_H4QL5uxdX_D5mADb1ZG_DA45HE(this, var1, var2), 2);
      }

      public void didReceivedNotification(int var1, int var2, Object... var3) {
         if (this.waitingForEvent) {
            EditTextBoldCursor[] var4 = this.codeField;
            if (var4 != null) {
               if (var1 == NotificationCenter.didReceiveSmsCode) {
                  EditTextBoldCursor var7 = var4[0];
                  StringBuilder var5 = new StringBuilder();
                  var5.append("");
                  var5.append(var3[0]);
                  var7.setText(var5.toString());
                  this.onNextPressed();
               } else if (var1 == NotificationCenter.didReceiveCall) {
                  StringBuilder var8 = new StringBuilder();
                  var8.append("");
                  var8.append(var3[0]);
                  String var6 = var8.toString();
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
         return this.currentType == 1 ? this.phone : LocaleController.getString("YourCode", 2131561142);
      }

      // $FF: synthetic method
      public void lambda$new$0$ChangePhoneActivity$LoginActivitySmsView(View var1) {
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
                  String var3 = String.format(Locale.US, "%s (%d)", var6.versionName, var6.versionCode);
                  Intent var7 = new Intent("android.intent.action.SEND");
                  var7.setType("message/rfc822");
                  var7.putExtra("android.intent.extra.EMAIL", new String[]{"sms@stel.com"});
                  StringBuilder var4 = new StringBuilder();
                  var4.append("Android registration/login issue ");
                  var4.append(var3);
                  var4.append(" ");
                  var4.append(this.emailPhone);
                  var7.putExtra("android.intent.extra.SUBJECT", var4.toString());
                  var4 = new StringBuilder();
                  var4.append("Phone: ");
                  var4.append(this.requestPhone);
                  var4.append("\nApp version: ");
                  var4.append(var3);
                  var4.append("\nOS version: SDK ");
                  var4.append(VERSION.SDK_INT);
                  var4.append("\nDevice Name: ");
                  var4.append(Build.MANUFACTURER);
                  var4.append(Build.MODEL);
                  var4.append("\nLocale: ");
                  var4.append(Locale.getDefault());
                  var4.append("\nError: ");
                  var4.append(this.lastError);
                  var7.putExtra("android.intent.extra.TEXT", var4.toString());
                  this.getContext().startActivity(Intent.createChooser(var7, "Send email..."));
               } catch (Exception var5) {
                  AlertsCreator.showSimpleAlert(ChangePhoneActivity.this, LocaleController.getString("NoMailInstalled", 2131559927));
               }
            }

         }
      }

      // $FF: synthetic method
      public void lambda$null$1$ChangePhoneActivity$LoginActivitySmsView(DialogInterface var1, int var2) {
         this.onBackPressed(true);
         ChangePhoneActivity.this.finishFragment();
      }

      // $FF: synthetic method
      public void lambda$null$2$ChangePhoneActivity$LoginActivitySmsView(TLRPC.TL_error var1, Bundle var2, TLObject var3, TLRPC.TL_auth_resendCode var4) {
         this.nextPressed = false;
         if (var1 == null) {
            ChangePhoneActivity.this.fillNextCodeParams(var2, (TLRPC.TL_auth_sentCode)var3);
         } else {
            AlertDialog var5 = (AlertDialog)AlertsCreator.processError(ChangePhoneActivity.access$5200(ChangePhoneActivity.this), var1, ChangePhoneActivity.this, var4);
            if (var5 != null && var1.text.contains("PHONE_CODE_EXPIRED")) {
               var5.setPositiveButtonListener(new _$$Lambda$ChangePhoneActivity$LoginActivitySmsView$AyKSTIEnTcnTDJMrf46_Qgw3yQk(this));
            }
         }

         ChangePhoneActivity.this.needHideProgress();
      }

      // $FF: synthetic method
      public void lambda$null$6$ChangePhoneActivity$LoginActivitySmsView(TLRPC.TL_error var1, TLObject var2, TLRPC.TL_account_changePhone var3) {
         ChangePhoneActivity.this.needHideProgress();
         this.nextPressed = false;
         if (var1 == null) {
            TLRPC.User var5 = (TLRPC.User)var2;
            this.destroyTimer();
            this.destroyCodeTimer();
            UserConfig.getInstance(ChangePhoneActivity.access$4600(ChangePhoneActivity.this)).setCurrentUser(var5);
            UserConfig.getInstance(ChangePhoneActivity.access$4700(ChangePhoneActivity.this)).saveConfig(true);
            ArrayList var7 = new ArrayList();
            var7.add(var5);
            MessagesStorage.getInstance(ChangePhoneActivity.access$4800(ChangePhoneActivity.this)).putUsersAndChats(var7, (ArrayList)null, true, true);
            MessagesController.getInstance(ChangePhoneActivity.access$4900(ChangePhoneActivity.this)).putUser(var5, false);
            ChangePhoneActivity.this.finishFragment();
            NotificationCenter.getInstance(ChangePhoneActivity.access$5000(ChangePhoneActivity.this)).postNotificationName(NotificationCenter.mainUserInfoChanged);
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
               AlertsCreator.processError(ChangePhoneActivity.access$5100(ChangePhoneActivity.this), var1, ChangePhoneActivity.this, var3);
            }

            if (!var1.text.contains("PHONE_CODE_EMPTY") && !var1.text.contains("PHONE_CODE_INVALID")) {
               if (var1.text.contains("PHONE_CODE_EXPIRED")) {
                  this.onBackPressed(true);
                  ChangePhoneActivity.this.setPage(0, true, (Bundle)null, true);
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
      public void lambda$onBackPressed$8$ChangePhoneActivity$LoginActivitySmsView(DialogInterface var1, int var2) {
         this.onBackPressed(true);
         ChangePhoneActivity.this.setPage(0, true, (Bundle)null, true);
      }

      // $FF: synthetic method
      public void lambda$onNextPressed$7$ChangePhoneActivity$LoginActivitySmsView(TLRPC.TL_account_changePhone var1, TLObject var2, TLRPC.TL_error var3) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$ChangePhoneActivity$LoginActivitySmsView$nnb2Q4_79T9z1PWAXzjTLfJ8r2Q(this, var3, var2, var1));
      }

      // $FF: synthetic method
      public void lambda$onShow$10$ChangePhoneActivity$LoginActivitySmsView() {
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
      public void lambda$resendCode$3$ChangePhoneActivity$LoginActivitySmsView(Bundle var1, TLRPC.TL_auth_resendCode var2, TLObject var3, TLRPC.TL_error var4) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$ChangePhoneActivity$LoginActivitySmsView$AkUnsjyTk3ocbOPZTE23QCsKSVg(this, var4, var1, var3, var2));
      }

      // $FF: synthetic method
      public boolean lambda$setParams$4$ChangePhoneActivity$LoginActivitySmsView(int var1, View var2, int var3, KeyEvent var4) {
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
      public boolean lambda$setParams$5$ChangePhoneActivity$LoginActivitySmsView(TextView var1, int var2, KeyEvent var3) {
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

      public boolean onBackPressed(boolean var1) {
         if (!var1) {
            AlertDialog.Builder var4 = new AlertDialog.Builder(ChangePhoneActivity.this.getParentActivity());
            var4.setTitle(LocaleController.getString("AppName", 2131558635));
            var4.setMessage(LocaleController.getString("StopVerification", 2131560831));
            var4.setPositiveButton(LocaleController.getString("Continue", 2131559153), (OnClickListener)null);
            var4.setNegativeButton(LocaleController.getString("Stop", 2131560820), new _$$Lambda$ChangePhoneActivity$LoginActivitySmsView$fJOIHWxgi1VrcISwAFf7EZ5s1yI(this));
            ChangePhoneActivity.this.showDialog(var4.create());
            return false;
         } else {
            TLRPC.TL_auth_cancelCode var2 = new TLRPC.TL_auth_cancelCode();
            var2.phone_number = this.requestPhone;
            var2.phone_code_hash = this.phoneHash;
            ConnectionsManager.getInstance(ChangePhoneActivity.access$4500(ChangePhoneActivity.this)).sendRequest(var2, _$$Lambda$ChangePhoneActivity$LoginActivitySmsView$kdOc_KDCO9lesbolp8AlwqF1nh4.INSTANCE, 10);
            this.destroyTimer();
            this.destroyCodeTimer();
            this.currentParams = null;
            int var3 = this.currentType;
            if (var3 == 2) {
               AndroidUtilities.setWaitingForSms(false);
               NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
            } else if (var3 == 3) {
               AndroidUtilities.setWaitingForCall(false);
               NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveCall);
            }

            this.waitingForEvent = false;
            return true;
         }
      }

      public void onCancelPressed() {
         this.nextPressed = false;
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
               int var4 = AndroidUtilities.dp(80.0F);
               var1 = AndroidUtilities.dp(291.0F);
               if (ChangePhoneActivity.this.scrollHeight - var2 < var4) {
                  this.setMeasuredDimension(this.getMeasuredWidth(), var2 + var4);
               } else if (ChangePhoneActivity.this.scrollHeight > var1) {
                  this.setMeasuredDimension(this.getMeasuredWidth(), var1);
               } else {
                  this.setMeasuredDimension(this.getMeasuredWidth(), ChangePhoneActivity.this.scrollHeight);
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
               TLRPC.TL_account_changePhone var3 = new TLRPC.TL_account_changePhone();
               var3.phone_number = this.requestPhone;
               var3.phone_code = var1;
               var3.phone_code_hash = this.phoneHash;
               this.destroyTimer();
               ChangePhoneActivity.this.needShowProgress();
               ConnectionsManager.getInstance(ChangePhoneActivity.access$4400(ChangePhoneActivity.this)).sendRequest(var3, new _$$Lambda$ChangePhoneActivity$LoginActivitySmsView$_a_ihjluIJ2vz2DCIrqa5SJZHuo(this, var3), 2);
            }
         }
      }

      public void onShow() {
         super.onShow();
         if (this.currentType != 3) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$ChangePhoneActivity$LoginActivitySmsView$ApE0zrzEPG0IwyfHRvw92p7wln0(this), 100L);
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
            this.emailPhone = var1.getString("ephone");
            this.requestPhone = var1.getString("phoneFormated");
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

            EditTextBoldCursor[] var4 = this.codeField;
            Object var8 = "";
            byte var10 = 8;
            final int var5;
            if (var4 != null && var4.length == this.length) {
               var5 = 0;

               while(true) {
                  var4 = this.codeField;
                  if (var5 >= var4.length) {
                     break;
                  }

                  var4[var5].setText("");
                  ++var5;
               }
            } else {
               this.codeField = new EditTextBoldCursor[this.length];

               for(var5 = 0; var5 < this.length; ++var5) {
                  this.codeField[var5] = new EditTextBoldCursor(this.getContext());
                  this.codeField[var5].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                  this.codeField[var5].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                  this.codeField[var5].setCursorSize(AndroidUtilities.dp(20.0F));
                  this.codeField[var5].setCursorWidth(1.5F);
                  Drawable var11 = this.getResources().getDrawable(2131165811).mutate();
                  var11.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteInputFieldActivated"), Mode.MULTIPLY));
                  this.codeField[var5].setBackgroundDrawable(var11);
                  this.codeField[var5].setImeOptions(268435461);
                  this.codeField[var5].setTextSize(1, 20.0F);
                  this.codeField[var5].setMaxLines(1);
                  this.codeField[var5].setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                  this.codeField[var5].setPadding(0, 0, 0, 0);
                  this.codeField[var5].setGravity(49);
                  if (this.currentType == 3) {
                     this.codeField[var5].setEnabled(false);
                     this.codeField[var5].setInputType(0);
                     this.codeField[var5].setVisibility(8);
                  } else {
                     this.codeField[var5].setInputType(3);
                  }

                  LinearLayout var12 = this.codeFieldContainer;
                  EditTextBoldCursor var6 = this.codeField[var5];
                  byte var7;
                  if (var5 != this.length - 1) {
                     var7 = 7;
                  } else {
                     var7 = 0;
                  }

                  var12.addView(var6, LayoutHelper.createLinear(34, 36, 1, 0, 0, var7, 0));
                  this.codeField[var5].addTextChangedListener(new TextWatcher() {
                     public void afterTextChanged(Editable var1) {
                        if (!LoginActivitySmsView.this.ignoreOnTextChange) {
                           int var2 = var1.length();
                           if (var2 >= 1) {
                              if (var2 > 1) {
                                 String var3 = var1.toString();
                                 LoginActivitySmsView.this.ignoreOnTextChange = true;

                                 for(int var4 = 0; var4 < Math.min(LoginActivitySmsView.this.length - var5, var2); ++var4) {
                                    if (var4 == 0) {
                                       var1.replace(0, var2, var3.substring(var4, var4 + 1));
                                    } else {
                                       LoginActivitySmsView.this.codeField[var5 + var4].setText(var3.substring(var4, var4 + 1));
                                    }
                                 }

                                 LoginActivitySmsView.this.ignoreOnTextChange = false;
                              }

                              if (var5 != LoginActivitySmsView.this.length - 1) {
                                 LoginActivitySmsView.this.codeField[var5 + 1].setSelection(LoginActivitySmsView.this.codeField[var5 + 1].length());
                                 LoginActivitySmsView.this.codeField[var5 + 1].requestFocus();
                              }

                              if ((var5 == LoginActivitySmsView.this.length - 1 || var5 == LoginActivitySmsView.this.length - 2 && var2 >= 2) && LoginActivitySmsView.this.getCode().length() == LoginActivitySmsView.this.length) {
                                 LoginActivitySmsView.this.onNextPressed();
                              }
                           }

                        }
                     }

                     public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
                     }

                     public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
                     }
                  });
                  this.codeField[var5].setOnKeyListener(new _$$Lambda$ChangePhoneActivity$LoginActivitySmsView$UkKWW40jg7mpE6ErNm6_eukinew(this, var5));
                  this.codeField[var5].setOnEditorActionListener(new _$$Lambda$ChangePhoneActivity$LoginActivitySmsView$tUraGeWZvqlYJBrGcWDDzAfODJQ(this));
               }
            }

            ChangePhoneActivity.ProgressView var13 = this.progressView;
            byte var15;
            if (var13 != null) {
               if (this.nextType != 0) {
                  var15 = 0;
               } else {
                  var15 = 8;
               }

               var13.setVisibility(var15);
            }

            if (this.phone != null) {
               String var14 = PhoneFormat.getInstance().format(this.phone);
               var5 = this.currentType;
               if (var5 == 1) {
                  var8 = AndroidUtilities.replaceTags(LocaleController.getString("SentAppCode", 2131560717));
               } else if (var5 == 2) {
                  var8 = AndroidUtilities.replaceTags(LocaleController.formatString("SentSmsCode", 2131560721, LocaleController.addNbsp(var14)));
               } else if (var5 == 3) {
                  var8 = AndroidUtilities.replaceTags(LocaleController.formatString("SentCallCode", 2131560719, LocaleController.addNbsp(var14)));
               } else if (var5 == 4) {
                  var8 = AndroidUtilities.replaceTags(LocaleController.formatString("SentCallOnly", 2131560720, LocaleController.addNbsp(var14)));
               }

               this.confirmTextView.setText((CharSequence)var8);
               if (this.currentType != 3) {
                  AndroidUtilities.showKeyboard(this.codeField[0]);
                  this.codeField[0].requestFocus();
               } else {
                  AndroidUtilities.hideKeyboard(this.codeField[0]);
               }

               this.destroyTimer();
               this.destroyCodeTimer();
               this.lastCurrentTime = (double)System.currentTimeMillis();
               var5 = this.currentType;
               if (var5 == 1) {
                  this.problemText.setVisibility(0);
                  this.timeText.setVisibility(8);
               } else {
                  if (var5 == 3) {
                     var5 = this.nextType;
                     if (var5 == 4 || var5 == 2) {
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

                  TextView var9;
                  if (this.currentType == 2) {
                     var5 = this.nextType;
                     if (var5 == 4 || var5 == 3) {
                        this.timeText.setText(LocaleController.formatString("CallText", 2131558885, 2, 0));
                        var9 = this.problemText;
                        if (this.time < 1000) {
                           var15 = 0;
                        } else {
                           var15 = 8;
                        }

                        var9.setVisibility(var15);
                        var9 = this.timeText;
                        if (this.time >= 1000) {
                           var10 = 0;
                        }

                        var9.setVisibility(var10);
                        this.createTimer();
                        return;
                     }
                  }

                  if (this.currentType == 4 && this.nextType == 2) {
                     this.timeText.setText(LocaleController.formatString("SmsText", 2131560793, 2, 0));
                     var9 = this.problemText;
                     if (this.time < 1000) {
                        var15 = 0;
                     } else {
                        var15 = 8;
                     }

                     var9.setVisibility(var15);
                     var9 = this.timeText;
                     if (this.time >= 1000) {
                        var10 = 0;
                     }

                     var9.setVisibility(var10);
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

   public class PhoneView extends SlideView implements OnItemSelectedListener {
      private EditTextBoldCursor codeField;
      private HashMap codesMap = new HashMap();
      private ArrayList countriesArray = new ArrayList();
      private HashMap countriesMap = new HashMap();
      private TextView countryButton;
      private int countryState = 0;
      private boolean ignoreOnPhoneChange = false;
      private boolean ignoreOnTextChange = false;
      private boolean ignoreSelection = false;
      private boolean nextPressed = false;
      private HintEditText phoneField;
      private HashMap phoneFormatMap = new HashMap();
      private TextView textView;
      private TextView textView2;
      private View view;

      public PhoneView(Context var2) {
         super(var2);
         this.setOrientation(1);
         this.countryButton = new TextView(var2);
         this.countryButton.setTextSize(1, 18.0F);
         this.countryButton.setPadding(AndroidUtilities.dp(12.0F), AndroidUtilities.dp(10.0F), AndroidUtilities.dp(12.0F), 0);
         this.countryButton.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.countryButton.setMaxLines(1);
         this.countryButton.setSingleLine(true);
         this.countryButton.setEllipsize(TruncateAt.END);
         TextView var3 = this.countryButton;
         byte var4;
         if (LocaleController.isRTL) {
            var4 = 5;
         } else {
            var4 = 3;
         }

         var3.setGravity(var4 | 1);
         this.countryButton.setBackgroundResource(2131165857);
         this.addView(this.countryButton, LayoutHelper.createLinear(-1, 36, 0.0F, 0.0F, 0.0F, 14.0F));
         this.countryButton.setOnClickListener(new _$$Lambda$ChangePhoneActivity$PhoneView$hHa0N5BwdjvTPGkcx_aGuYpU4o4(this));
         this.view = new View(var2);
         this.view.setPadding(AndroidUtilities.dp(12.0F), 0, AndroidUtilities.dp(12.0F), 0);
         this.view.setBackgroundColor(Theme.getColor("windowBackgroundWhiteGrayLine"));
         this.addView(this.view, LayoutHelper.createLinear(-1, 1, 4.0F, -17.5F, 4.0F, 0.0F));
         LinearLayout var5 = new LinearLayout(var2);
         var5.setOrientation(0);
         this.addView(var5, LayoutHelper.createLinear(-1, -2, 0.0F, 20.0F, 0.0F, 0.0F));
         this.textView = new TextView(var2);
         this.textView.setText("+");
         this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.textView.setTextSize(1, 18.0F);
         var5.addView(this.textView, LayoutHelper.createLinear(-2, -2));
         this.codeField = new EditTextBoldCursor(var2);
         this.codeField.setInputType(3);
         this.codeField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.codeField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.codeField.setCursorSize(AndroidUtilities.dp(20.0F));
         this.codeField.setCursorWidth(1.5F);
         this.codeField.setBackgroundDrawable(Theme.createEditTextDrawable(var2, false));
         this.codeField.setPadding(AndroidUtilities.dp(10.0F), 0, 0, 0);
         this.codeField.setTextSize(1, 18.0F);
         this.codeField.setMaxLines(1);
         this.codeField.setGravity(19);
         this.codeField.setImeOptions(268435461);
         LengthFilter var20 = new LengthFilter(5);
         this.codeField.setFilters(new InputFilter[]{var20});
         var5.addView(this.codeField, LayoutHelper.createLinear(55, 36, -9.0F, 0.0F, 16.0F, 0.0F));
         this.codeField.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable var1) {
               if (!PhoneView.this.ignoreOnTextChange) {
                  PhoneView.this.ignoreOnTextChange = true;
                  String var2 = PhoneFormat.stripExceptNumbers(PhoneView.this.codeField.getText().toString());
                  PhoneView.this.codeField.setText(var2);
                  int var3 = var2.length();
                  Object var4 = null;
                  if (var3 == 0) {
                     PhoneView.this.countryButton.setText(LocaleController.getString("ChooseCountry", 2131559086));
                     PhoneView.this.phoneField.setHintText((String)null);
                     PhoneView.this.countryState = 1;
                  } else {
                     int var5 = var2.length();
                     var3 = 4;
                     String var6;
                     String var9;
                     boolean var11;
                     if (var5 <= 4) {
                        var9 = null;
                        var11 = false;
                        var6 = var2;
                     } else {
                        PhoneView.this.ignoreOnTextChange = true;

                        StringBuilder var8;
                        boolean var10;
                        while(true) {
                           if (var3 < 1) {
                              var9 = null;
                              var10 = false;
                              break;
                           }

                           var6 = var2.substring(0, var3);
                           if ((String)PhoneView.this.codesMap.get(var6) != null) {
                              var8 = new StringBuilder();
                              var8.append(var2.substring(var3, var2.length()));
                              var8.append(PhoneView.this.phoneField.getText().toString());
                              var9 = var8.toString();
                              PhoneView.this.codeField.setText(var6);
                              var10 = true;
                              var2 = var6;
                              break;
                           }

                           --var3;
                        }

                        var6 = var2;
                        var11 = var10;
                        if (!var10) {
                           PhoneView.this.ignoreOnTextChange = true;
                           var8 = new StringBuilder();
                           var8.append(var2.substring(1, var2.length()));
                           var8.append(PhoneView.this.phoneField.getText().toString());
                           var9 = var8.toString();
                           EditTextBoldCursor var7 = PhoneView.this.codeField;
                           var6 = var2.substring(0, 1);
                           var7.setText(var6);
                           var11 = var10;
                        }
                     }

                     var2 = (String)PhoneView.this.codesMap.get(var6);
                     if (var2 != null) {
                        var3 = PhoneView.this.countriesArray.indexOf(var2);
                        if (var3 != -1) {
                           PhoneView.this.ignoreSelection = true;
                           PhoneView.this.countryButton.setText((CharSequence)PhoneView.this.countriesArray.get(var3));
                           String var12 = (String)PhoneView.this.phoneFormatMap.get(var6);
                           HintEditText var13 = PhoneView.this.phoneField;
                           var2 = (String)var4;
                           if (var12 != null) {
                              var2 = var12.replace('X', '–');
                           }

                           var13.setHintText(var2);
                           PhoneView.this.countryState = 0;
                        } else {
                           PhoneView.this.countryButton.setText(LocaleController.getString("WrongCountry", 2131561125));
                           PhoneView.this.phoneField.setHintText((String)null);
                           PhoneView.this.countryState = 2;
                        }
                     } else {
                        PhoneView.this.countryButton.setText(LocaleController.getString("WrongCountry", 2131561125));
                        PhoneView.this.phoneField.setHintText((String)null);
                        PhoneView.this.countryState = 2;
                     }

                     if (!var11) {
                        PhoneView.this.codeField.setSelection(PhoneView.this.codeField.getText().length());
                     }

                     if (var9 != null) {
                        PhoneView.this.phoneField.requestFocus();
                        PhoneView.this.phoneField.setText(var9);
                        PhoneView.this.phoneField.setSelection(PhoneView.this.phoneField.length());
                     }
                  }

                  PhoneView.this.ignoreOnTextChange = false;
               }
            }

            public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
            }

            public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
            }
         });
         this.codeField.setOnEditorActionListener(new _$$Lambda$ChangePhoneActivity$PhoneView$ygpxiaZ49URLLE6EQa6mU8BoI_E(this));
         this.phoneField = new HintEditText(var2);
         this.phoneField.setInputType(3);
         this.phoneField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.phoneField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
         this.phoneField.setBackgroundDrawable(Theme.createEditTextDrawable(var2, false));
         this.phoneField.setPadding(0, 0, 0, 0);
         this.phoneField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.phoneField.setCursorSize(AndroidUtilities.dp(20.0F));
         this.phoneField.setCursorWidth(1.5F);
         this.phoneField.setTextSize(1, 18.0F);
         this.phoneField.setMaxLines(1);
         this.phoneField.setGravity(19);
         this.phoneField.setImeOptions(268435461);
         var5.addView(this.phoneField, LayoutHelper.createFrame(-1, 36.0F));
         this.phoneField.addTextChangedListener(new TextWatcher() {
            private int actionPosition;
            private int characterAction = -1;

            public void afterTextChanged(Editable var1) {
               if (!PhoneView.this.ignoreOnPhoneChange) {
                  int var2 = PhoneView.this.phoneField.getSelectionStart();
                  String var3 = PhoneView.this.phoneField.getText().toString();
                  String var9 = var3;
                  int var4 = var2;
                  if (this.characterAction == 3) {
                     StringBuilder var10 = new StringBuilder();
                     var10.append(var3.substring(0, this.actionPosition));
                     var10.append(var3.substring(this.actionPosition + 1, var3.length()));
                     var9 = var10.toString();
                     var4 = var2 - 1;
                  }

                  StringBuilder var12 = new StringBuilder(var9.length());

                  int var5;
                  for(var2 = 0; var2 < var9.length(); var2 = var5) {
                     var5 = var2 + 1;
                     String var6 = var9.substring(var2, var5);
                     if ("0123456789".contains(var6)) {
                        var12.append(var6);
                     }
                  }

                  PhoneView.this.ignoreOnPhoneChange = true;
                  var9 = PhoneView.this.phoneField.getHintText();
                  var2 = var4;
                  if (var9 != null) {
                     label71: {
                        int var7;
                        for(var2 = 0; var2 < var12.length(); var4 = var7) {
                           if (var2 >= var9.length()) {
                              var12.insert(var2, ' ');
                              if (var4 == var2 + 1) {
                                 var2 = this.characterAction;
                                 if (var2 != 2 && var2 != 3) {
                                    var2 = var4 + 1;
                                    break label71;
                                 }
                              }
                              break;
                           }

                           var7 = var4;
                           var5 = var2;
                           if (var9.charAt(var2) == ' ') {
                              var12.insert(var2, ' ');
                              ++var2;
                              var7 = var4;
                              var5 = var2;
                              if (var4 == var2) {
                                 int var8 = this.characterAction;
                                 var7 = var4;
                                 var5 = var2;
                                 if (var8 != 2) {
                                    var7 = var4;
                                    var5 = var2;
                                    if (var8 != 3) {
                                       var7 = var4 + 1;
                                       var5 = var2;
                                    }
                                 }
                              }
                           }

                           var2 = var5 + 1;
                        }

                        var2 = var4;
                     }
                  }

                  PhoneView.this.phoneField.setText(var12);
                  if (var2 >= 0) {
                     HintEditText var11 = PhoneView.this.phoneField;
                     if (var2 > PhoneView.this.phoneField.length()) {
                        var2 = PhoneView.this.phoneField.length();
                     }

                     var11.setSelection(var2);
                  }

                  PhoneView.this.phoneField.onTextChange();
                  PhoneView.this.ignoreOnPhoneChange = false;
               }
            }

            public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
               if (var3 == 0 && var4 == 1) {
                  this.characterAction = 1;
               } else if (var3 == 1 && var4 == 0) {
                  if (var1.charAt(var2) == ' ' && var2 > 0) {
                     this.characterAction = 3;
                     this.actionPosition = var2 - 1;
                  } else {
                     this.characterAction = 2;
                  }
               } else {
                  this.characterAction = -1;
               }

            }

            public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
            }
         });
         this.phoneField.setOnEditorActionListener(new _$$Lambda$ChangePhoneActivity$PhoneView$mrGs3y2Wdb_mBuTVpvmbjtLfvnM(this));
         this.phoneField.setOnKeyListener(new _$$Lambda$ChangePhoneActivity$PhoneView$Giefqm7S2Vkx_CoymYR4pbzjY_Y(this));
         this.textView2 = new TextView(var2);
         this.textView2.setText(LocaleController.getString("ChangePhoneHelp", 2131558911));
         this.textView2.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
         this.textView2.setTextSize(1, 14.0F);
         TextView var13 = this.textView2;
         if (LocaleController.isRTL) {
            var4 = 5;
         } else {
            var4 = 3;
         }

         var13.setGravity(var4);
         this.textView2.setLineSpacing((float)AndroidUtilities.dp(2.0F), 1.0F);
         var13 = this.textView2;
         if (LocaleController.isRTL) {
            var4 = 5;
         } else {
            var4 = 3;
         }

         this.addView(var13, LayoutHelper.createLinear(-2, -2, var4, 0, 28, 0, 10));
         HashMap var21 = new HashMap();

         Exception var10000;
         Exception var16;
         boolean var10001;
         label99: {
            label98: {
               BufferedReader var14;
               try {
                  InputStreamReader var15 = new InputStreamReader(this.getResources().getAssets().open("countries.txt"));
                  var14 = new BufferedReader(var15);
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label98;
               }

               while(true) {
                  String var17;
                  try {
                     var17 = var14.readLine();
                  } catch (Exception var9) {
                     var10000 = var9;
                     var10001 = false;
                     break;
                  }

                  if (var17 == null) {
                     try {
                        var14.close();
                        break label99;
                     } catch (Exception var8) {
                        var10000 = var8;
                        var10001 = false;
                        break;
                     }
                  }

                  String[] var19;
                  try {
                     var19 = var17.split(";");
                     this.countriesArray.add(0, var19[2]);
                     this.countriesMap.put(var19[2], var19[0]);
                     this.codesMap.put(var19[0], var19[2]);
                     if (var19.length > 3) {
                        this.phoneFormatMap.put(var19[0], var19[3]);
                     }
                  } catch (Exception var12) {
                     var10000 = var12;
                     var10001 = false;
                     break;
                  }

                  try {
                     var21.put(var19[1], var19[2]);
                  } catch (Exception var10) {
                     var10000 = var10;
                     var10001 = false;
                     break;
                  }
               }
            }

            var16 = var10000;
            FileLog.e((Throwable)var16);
         }

         Collections.sort(this.countriesArray, _$$Lambda$TEfSBt3hRUlBSSARfPEHsJesTtE.INSTANCE);
         var2 = null;

         String var18;
         label77: {
            label104: {
               TelephonyManager var23;
               try {
                  var23 = (TelephonyManager)ApplicationLoader.applicationContext.getSystemService("phone");
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label104;
               }

               var18 = var2;
               if (var23 == null) {
                  break label77;
               }

               try {
                  var18 = var23.getSimCountryIso().toUpperCase();
                  break label77;
               } catch (Exception var6) {
                  var10000 = var6;
                  var10001 = false;
               }
            }

            var16 = var10000;
            FileLog.e((Throwable)var16);
            var18 = var2;
         }

         if (var18 != null) {
            var18 = (String)var21.get(var18);
            if (var18 != null && this.countriesArray.indexOf(var18) != -1) {
               this.codeField.setText((CharSequence)this.countriesMap.get(var18));
               this.countryState = 0;
            }
         }

         if (this.codeField.length() == 0) {
            this.countryButton.setText(LocaleController.getString("ChooseCountry", 2131559086));
            this.phoneField.setHintText((String)null);
            this.countryState = 1;
         }

         if (this.codeField.length() != 0) {
            AndroidUtilities.showKeyboard(this.phoneField);
            this.phoneField.requestFocus();
            HintEditText var22 = this.phoneField;
            var22.setSelection(var22.length());
         } else {
            AndroidUtilities.showKeyboard(this.codeField);
            this.codeField.requestFocus();
         }

      }

      public String getHeaderName() {
         return LocaleController.getString("ChangePhoneNewNumber", 2131558912);
      }

      // $FF: synthetic method
      public void lambda$new$2$ChangePhoneActivity$PhoneView(View var1) {
         CountrySelectActivity var2 = new CountrySelectActivity(true);
         var2.setCountrySelectActivityDelegate(new _$$Lambda$ChangePhoneActivity$PhoneView$A8p4yJz3eobVqDqSKiIVtCzw3Sk(this));
         ChangePhoneActivity.this.presentFragment(var2);
      }

      // $FF: synthetic method
      public boolean lambda$new$3$ChangePhoneActivity$PhoneView(TextView var1, int var2, KeyEvent var3) {
         if (var2 == 5) {
            this.phoneField.requestFocus();
            HintEditText var4 = this.phoneField;
            var4.setSelection(var4.length());
            return true;
         } else {
            return false;
         }
      }

      // $FF: synthetic method
      public boolean lambda$new$4$ChangePhoneActivity$PhoneView(TextView var1, int var2, KeyEvent var3) {
         if (var2 == 5) {
            this.onNextPressed();
            return true;
         } else {
            return false;
         }
      }

      // $FF: synthetic method
      public boolean lambda$new$5$ChangePhoneActivity$PhoneView(View var1, int var2, KeyEvent var3) {
         if (var2 == 67 && this.phoneField.length() == 0) {
            this.codeField.requestFocus();
            EditTextBoldCursor var4 = this.codeField;
            var4.setSelection(var4.length());
            this.codeField.dispatchKeyEvent(var3);
            return true;
         } else {
            return false;
         }
      }

      // $FF: synthetic method
      public void lambda$null$0$ChangePhoneActivity$PhoneView() {
         AndroidUtilities.showKeyboard(this.phoneField);
      }

      // $FF: synthetic method
      public void lambda$null$1$ChangePhoneActivity$PhoneView(String var1, String var2) {
         this.selectCountry(var1);
         AndroidUtilities.runOnUIThread(new _$$Lambda$ChangePhoneActivity$PhoneView$wvhGenZJjp0tqD9lb_2k5ayod7A(this), 300L);
         this.phoneField.requestFocus();
         HintEditText var3 = this.phoneField;
         var3.setSelection(var3.length());
      }

      // $FF: synthetic method
      public void lambda$null$6$ChangePhoneActivity$PhoneView(TLRPC.TL_error var1, Bundle var2, TLObject var3, TLRPC.TL_account_sendChangePhoneCode var4) {
         this.nextPressed = false;
         if (var1 == null) {
            ChangePhoneActivity.this.fillNextCodeParams(var2, (TLRPC.TL_auth_sentCode)var3);
         } else {
            AlertsCreator.processError(ChangePhoneActivity.access$1800(ChangePhoneActivity.this), var1, ChangePhoneActivity.this, var4, var2.getString("phone"));
         }

         ChangePhoneActivity.this.needHideProgress();
      }

      // $FF: synthetic method
      public void lambda$onNextPressed$7$ChangePhoneActivity$PhoneView(Bundle var1, TLRPC.TL_account_sendChangePhoneCode var2, TLObject var3, TLRPC.TL_error var4) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$ChangePhoneActivity$PhoneView$_8ZmDMwVcKhBgNHbawOvyWif9Ck(this, var4, var1, var3, var2));
      }

      public void onCancelPressed() {
         this.nextPressed = false;
      }

      public void onItemSelected(AdapterView var1, View var2, int var3, long var4) {
         if (this.ignoreSelection) {
            this.ignoreSelection = false;
         } else {
            this.ignoreOnTextChange = true;
            String var6 = (String)this.countriesArray.get(var3);
            this.codeField.setText((CharSequence)this.countriesMap.get(var6));
            this.ignoreOnTextChange = false;
         }
      }

      public void onNextPressed() {
         if (ChangePhoneActivity.this.getParentActivity() != null && !this.nextPressed) {
            TelephonyManager var1 = (TelephonyManager)ApplicationLoader.applicationContext.getSystemService("phone");
            boolean var2;
            if (var1.getSimState() != 1 && var1.getPhoneType() != 0) {
               var2 = true;
            } else {
               var2 = false;
            }

            boolean var4;
            if (VERSION.SDK_INT >= 23 && var2) {
               boolean var3;
               if (ChangePhoneActivity.this.getParentActivity().checkSelfPermission("android.permission.READ_PHONE_STATE") == 0) {
                  var3 = true;
               } else {
                  var3 = false;
               }

               var4 = var3;
               if (ChangePhoneActivity.this.checkPermissions) {
                  ChangePhoneActivity.this.permissionsItems.clear();
                  if (!var3) {
                     ChangePhoneActivity.this.permissionsItems.add("android.permission.READ_PHONE_STATE");
                  }

                  var4 = var3;
                  if (!ChangePhoneActivity.this.permissionsItems.isEmpty()) {
                     SharedPreferences var17 = MessagesController.getGlobalMainSettings();
                     if (!var17.getBoolean("firstlogin", true) && !ChangePhoneActivity.this.getParentActivity().shouldShowRequestPermissionRationale("android.permission.READ_PHONE_STATE")) {
                        ChangePhoneActivity.this.getParentActivity().requestPermissions((String[])ChangePhoneActivity.this.permissionsItems.toArray(new String[ChangePhoneActivity.this.permissionsItems.size()]), 6);
                     } else {
                        var17.edit().putBoolean("firstlogin", false).commit();
                        AlertDialog.Builder var20 = new AlertDialog.Builder(ChangePhoneActivity.this.getParentActivity());
                        var20.setTitle(LocaleController.getString("AppName", 2131558635));
                        var20.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
                        var20.setMessage(LocaleController.getString("AllowReadCall", 2131558607));
                        ChangePhoneActivity var18 = ChangePhoneActivity.this;
                        var18.permissionsDialog = var18.showDialog(var20.create());
                     }

                     return;
                  }
               }
            } else {
               var4 = true;
            }

            if (this.countryState == 1) {
               AlertsCreator.showSimpleAlert(ChangePhoneActivity.this, LocaleController.getString("ChooseCountry", 2131559086));
               return;
            }

            if (this.codeField.length() == 0) {
               AlertsCreator.showSimpleAlert(ChangePhoneActivity.this, LocaleController.getString("InvalidPhoneNumber", 2131559674));
               return;
            }

            TLRPC.TL_account_sendChangePhoneCode var5 = new TLRPC.TL_account_sendChangePhoneCode();
            StringBuilder var6 = new StringBuilder();
            var6.append("");
            var6.append(this.codeField.getText());
            var6.append(this.phoneField.getText());
            String var19 = PhoneFormat.stripExceptNumbers(var6.toString());
            var5.phone_number = var19;
            var5.settings = new TLRPC.TL_codeSettings();
            TLRPC.TL_codeSettings var7 = var5.settings;
            boolean var8;
            if (var2 && var4) {
               var8 = true;
            } else {
               var8 = false;
            }

            var7.allow_flashcall = var8;
            if (VERSION.SDK_INT >= 26) {
               try {
                  var7 = var5.settings;
                  SmsManager var9 = SmsManager.getDefault();
                  Context var10 = ApplicationLoader.applicationContext;
                  Intent var11 = new Intent(ApplicationLoader.applicationContext, SmsReceiver.class);
                  var7.app_hash = var9.createAppSpecificSmsToken(PendingIntent.getBroadcast(var10, 0, var11, 134217728));
               } catch (Throwable var14) {
                  FileLog.e(var14);
               }
            } else {
               var7 = var5.settings;
               var7.app_hash = BuildVars.SMS_HASH;
               var7.app_hash_persistent = true;
            }

            SharedPreferences var21 = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
            if (!TextUtils.isEmpty(var5.settings.app_hash)) {
               TLRPC.TL_codeSettings var23 = var5.settings;
               var23.flags |= 8;
               var21.edit().putString("sms_hash", var5.settings.app_hash).commit();
            } else {
               var21.edit().remove("sms_hash").commit();
            }

            if (var5.settings.allow_flashcall) {
               try {
                  String var15 = var1.getLine1Number();
                  if (!TextUtils.isEmpty(var15)) {
                     var5.settings.current_number = PhoneNumberUtils.compare(var19, var15);
                     if (!var5.settings.current_number) {
                        var5.settings.allow_flashcall = false;
                     }
                  } else {
                     var5.settings.current_number = false;
                  }
               } catch (Exception var13) {
                  var5.settings.allow_flashcall = false;
                  FileLog.e((Throwable)var13);
               }
            }

            Bundle var16 = new Bundle();
            StringBuilder var22 = new StringBuilder();
            var22.append("+");
            var22.append(this.codeField.getText());
            var22.append(" ");
            var22.append(this.phoneField.getText());
            var16.putString("phone", var22.toString());

            try {
               var22 = new StringBuilder();
               var22.append("+");
               var22.append(PhoneFormat.stripExceptNumbers(this.codeField.getText().toString()));
               var22.append(" ");
               var22.append(PhoneFormat.stripExceptNumbers(this.phoneField.getText().toString()));
               var16.putString("ephone", var22.toString());
            } catch (Exception var12) {
               FileLog.e((Throwable)var12);
               var22 = new StringBuilder();
               var22.append("+");
               var22.append(var19);
               var16.putString("ephone", var22.toString());
            }

            var16.putString("phoneFormated", var19);
            this.nextPressed = true;
            ChangePhoneActivity.this.needShowProgress();
            ConnectionsManager.getInstance(ChangePhoneActivity.access$1600(ChangePhoneActivity.this)).sendRequest(var5, new _$$Lambda$ChangePhoneActivity$PhoneView$osRwjZiwNNc4O0LSbhXJvCtt8fY(this, var16, var5), 2);
         }

      }

      public void onNothingSelected(AdapterView var1) {
      }

      public void onShow() {
         super.onShow();
         if (this.phoneField != null) {
            if (this.codeField.length() != 0) {
               AndroidUtilities.showKeyboard(this.phoneField);
               this.phoneField.requestFocus();
               HintEditText var1 = this.phoneField;
               var1.setSelection(var1.length());
            } else {
               AndroidUtilities.showKeyboard(this.codeField);
               this.codeField.requestFocus();
            }
         }

      }

      public void selectCountry(String var1) {
         if (this.countriesArray.indexOf(var1) != -1) {
            this.ignoreOnTextChange = true;
            String var2 = (String)this.countriesMap.get(var1);
            this.codeField.setText(var2);
            this.countryButton.setText(var1);
            var1 = (String)this.phoneFormatMap.get(var2);
            HintEditText var3 = this.phoneField;
            if (var1 != null) {
               var1 = var1.replace('X', '–');
            } else {
               var1 = null;
            }

            var3.setHintText(var1);
            this.countryState = 0;
            this.ignoreOnTextChange = false;
         }

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
