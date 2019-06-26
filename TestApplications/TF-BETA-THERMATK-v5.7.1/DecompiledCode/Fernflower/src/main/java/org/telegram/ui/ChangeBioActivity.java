package org.telegram.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.InputFilter.LengthFilter;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
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
import org.telegram.ui.Components.LayoutHelper;

public class ChangeBioActivity extends BaseFragment {
   private static final int done_button = 1;
   private TextView checkTextView;
   private View doneButton;
   private EditTextBoldCursor firstNameField;
   private TextView helpTextView;

   // $FF: synthetic method
   static boolean lambda$createView$0(View var0, MotionEvent var1) {
      return true;
   }

   private void saveName() {
      TLRPC.UserFull var1 = MessagesController.getInstance(super.currentAccount).getUserFull(UserConfig.getInstance(super.currentAccount).getClientUserId());
      if (this.getParentActivity() != null && var1 != null) {
         String var2 = var1.about;
         String var3 = var2;
         if (var2 == null) {
            var3 = "";
         }

         var2 = this.firstNameField.getText().toString().replace("\n", "");
         if (var3.equals(var2)) {
            this.finishFragment();
            return;
         }

         AlertDialog var6 = new AlertDialog(this.getParentActivity(), 3);
         TLRPC.TL_account_updateProfile var4 = new TLRPC.TL_account_updateProfile();
         var4.about = var2;
         var4.flags |= 4;
         int var5 = ConnectionsManager.getInstance(super.currentAccount).sendRequest(var4, new _$$Lambda$ChangeBioActivity$JZsWl0x8uGEwh0dAVWe1jdDZvHs(this, var6, var1, var2, var4), 2);
         ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(var5, super.classGuid);
         var6.setOnCancelListener(new _$$Lambda$ChangeBioActivity$Q4bba_1YALoLViuYxQRzjsdV_6c(this, var5));
         var6.show();
      }

   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setTitle(LocaleController.getString("UserBio", 2131560987));
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               ChangeBioActivity.this.finishFragment();
            } else if (var1 == 1) {
               ChangeBioActivity.this.saveName();
            }

         }
      });
      this.doneButton = super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0F));
      super.fragmentView = new LinearLayout(var1);
      LinearLayout var2 = (LinearLayout)super.fragmentView;
      var2.setOrientation(1);
      super.fragmentView.setOnTouchListener(_$$Lambda$ChangeBioActivity$QHxoa1XUfskp3fsOABXGjgD9Sl4.INSTANCE);
      FrameLayout var3 = new FrameLayout(var1);
      var2.addView(var3, LayoutHelper.createLinear(-1, -2, 24.0F, 24.0F, 20.0F, 0.0F));
      this.firstNameField = new EditTextBoldCursor(var1);
      this.firstNameField.setTextSize(1, 18.0F);
      this.firstNameField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
      this.firstNameField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.firstNameField.setBackgroundDrawable(Theme.createEditTextDrawable(var1, false));
      this.firstNameField.setMaxLines(4);
      EditTextBoldCursor var4 = this.firstNameField;
      boolean var5 = LocaleController.isRTL;
      float var6 = 24.0F;
      float var7;
      if (var5) {
         var7 = 24.0F;
      } else {
         var7 = 0.0F;
      }

      int var8 = AndroidUtilities.dp(var7);
      var7 = var6;
      if (LocaleController.isRTL) {
         var7 = 0.0F;
      }

      var4.setPadding(var8, 0, AndroidUtilities.dp(var7), AndroidUtilities.dp(6.0F));
      var4 = this.firstNameField;
      byte var15;
      if (LocaleController.isRTL) {
         var15 = 5;
      } else {
         var15 = 3;
      }

      var4.setGravity(var15);
      this.firstNameField.setImeOptions(268435456);
      this.firstNameField.setInputType(147457);
      this.firstNameField.setImeOptions(6);
      LengthFilter var13 = new LengthFilter(70) {
         public CharSequence filter(CharSequence var1, int var2, int var3, Spanned var4, int var5, int var6) {
            if (var1 != null && TextUtils.indexOf(var1, '\n') != -1) {
               ChangeBioActivity.this.doneButton.performClick();
               return "";
            } else {
               CharSequence var8 = super.filter(var1, var2, var3, var4, var5, var6);
               if (var8 != null && var1 != null && var8.length() != var1.length()) {
                  Vibrator var7 = (Vibrator)ChangeBioActivity.this.getParentActivity().getSystemService("vibrator");
                  if (var7 != null) {
                     var7.vibrate(200L);
                  }

                  AndroidUtilities.shakeView(ChangeBioActivity.this.checkTextView, 2.0F, 0);
               }

               return var8;
            }
         }
      };
      this.firstNameField.setFilters(new InputFilter[]{var13});
      this.firstNameField.setMinHeight(AndroidUtilities.dp(36.0F));
      this.firstNameField.setHint(LocaleController.getString("UserBio", 2131560987));
      this.firstNameField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.firstNameField.setCursorSize(AndroidUtilities.dp(20.0F));
      this.firstNameField.setCursorWidth(1.5F);
      this.firstNameField.setOnEditorActionListener(new _$$Lambda$ChangeBioActivity$4xLMdC_iGI1lEMvcHXGPYTNQNU8(this));
      this.firstNameField.addTextChangedListener(new TextWatcher() {
         public void afterTextChanged(Editable var1) {
            ChangeBioActivity.this.checkTextView.setText(String.format("%d", 70 - ChangeBioActivity.this.firstNameField.length()));
         }

         public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
         }

         public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
         }
      });
      var3.addView(this.firstNameField, LayoutHelper.createFrame(-1, -2.0F, 51, 0.0F, 0.0F, 4.0F, 0.0F));
      this.checkTextView = new TextView(var1);
      this.checkTextView.setTextSize(1, 15.0F);
      this.checkTextView.setText(String.format("%d", 70));
      this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText4"));
      TextView var14 = this.checkTextView;
      if (LocaleController.isRTL) {
         var15 = 3;
      } else {
         var15 = 5;
      }

      var3.addView(var14, LayoutHelper.createFrame(-2, -2.0F, var15, 0.0F, 4.0F, 4.0F, 0.0F));
      this.helpTextView = new TextView(var1);
      this.helpTextView.setTextSize(1, 15.0F);
      this.helpTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText8"));
      TextView var9 = this.helpTextView;
      if (LocaleController.isRTL) {
         var15 = 5;
      } else {
         var15 = 3;
      }

      var9.setGravity(var15);
      this.helpTextView.setText(AndroidUtilities.replaceTags(LocaleController.getString("UserBioInfo", 2131560990)));
      var9 = this.helpTextView;
      if (LocaleController.isRTL) {
         var15 = 5;
      } else {
         var15 = 3;
      }

      var2.addView(var9, LayoutHelper.createLinear(-2, -2, var15, 24, 10, 24, 0));
      TLRPC.UserFull var10 = MessagesController.getInstance(super.currentAccount).getUserFull(UserConfig.getInstance(super.currentAccount).getClientUserId());
      if (var10 != null) {
         String var11 = var10.about;
         if (var11 != null) {
            this.firstNameField.setText(var11);
            EditTextBoldCursor var12 = this.firstNameField;
            var12.setSelection(var12.length());
         }
      }

      return super.fragmentView;
   }

   public ThemeDescription[] getThemeDescriptions() {
      return new ThemeDescription[]{new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector"), new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"), new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputField"), new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"), new ThemeDescription(this.helpTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText8"), new ThemeDescription(this.checkTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4")};
   }

   // $FF: synthetic method
   public boolean lambda$createView$1$ChangeBioActivity(TextView var1, int var2, KeyEvent var3) {
      if (var2 == 6) {
         View var4 = this.doneButton;
         if (var4 != null) {
            var4.performClick();
            return true;
         }
      }

      return false;
   }

   // $FF: synthetic method
   public void lambda$null$2$ChangeBioActivity(AlertDialog var1, TLRPC.UserFull var2, String var3, TLRPC.User var4) {
      try {
         var1.dismiss();
      } catch (Exception var5) {
         FileLog.e((Throwable)var5);
      }

      var2.about = var3;
      NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.userInfoDidLoad, var4.id, var2, null);
      this.finishFragment();
   }

   // $FF: synthetic method
   public void lambda$null$3$ChangeBioActivity(AlertDialog var1, TLRPC.TL_error var2, TLRPC.TL_account_updateProfile var3) {
      try {
         var1.dismiss();
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

      AlertsCreator.processError(super.currentAccount, var2, this, var3);
   }

   // $FF: synthetic method
   public void lambda$saveName$4$ChangeBioActivity(AlertDialog var1, TLRPC.UserFull var2, String var3, TLRPC.TL_account_updateProfile var4, TLObject var5, TLRPC.TL_error var6) {
      if (var6 == null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$ChangeBioActivity$tNpkkFgAdE6xw7aDpwDOLm8jmpc(this, var1, var2, var3, (TLRPC.User)var5));
      } else {
         AndroidUtilities.runOnUIThread(new _$$Lambda$ChangeBioActivity$T2lBd_q_6K_Uw6G0hNAJdgSSX7I(this, var1, var6, var4));
      }

   }

   // $FF: synthetic method
   public void lambda$saveName$5$ChangeBioActivity(int var1, DialogInterface var2) {
      ConnectionsManager.getInstance(super.currentAccount).cancelRequest(var1, true);
   }

   public void onResume() {
      super.onResume();
      if (!MessagesController.getGlobalMainSettings().getBoolean("view_animations", true)) {
         this.firstNameField.requestFocus();
         AndroidUtilities.showKeyboard(this.firstNameField);
      }

   }

   public void onTransitionAnimationEnd(boolean var1, boolean var2) {
      if (var1) {
         this.firstNameField.requestFocus();
         AndroidUtilities.showKeyboard(this.firstNameField);
      }

   }
}
