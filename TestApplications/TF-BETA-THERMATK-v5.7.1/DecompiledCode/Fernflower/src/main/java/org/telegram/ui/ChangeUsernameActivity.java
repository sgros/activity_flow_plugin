package org.telegram.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
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

public class ChangeUsernameActivity extends BaseFragment {
   private static final int done_button = 1;
   private int checkReqId;
   private Runnable checkRunnable;
   private TextView checkTextView;
   private View doneButton;
   private EditTextBoldCursor firstNameField;
   private TextView helpTextView;
   private boolean ignoreCheck;
   private CharSequence infoText;
   private String lastCheckName;
   private boolean lastNameAvailable;

   // $FF: synthetic method
   static int access$400(ChangeUsernameActivity var0) {
      return var0.currentAccount;
   }

   private boolean checkUserName(String var1, boolean var2) {
      if (var1 != null && var1.length() > 0) {
         this.checkTextView.setVisibility(0);
      } else {
         this.checkTextView.setVisibility(8);
      }

      if (var2 && var1.length() == 0) {
         return true;
      } else {
         Runnable var3 = this.checkRunnable;
         if (var3 != null) {
            AndroidUtilities.cancelRunOnUIThread(var3);
            this.checkRunnable = null;
            this.lastCheckName = null;
            if (this.checkReqId != 0) {
               ConnectionsManager.getInstance(super.currentAccount).cancelRequest(this.checkReqId, true);
            }
         }

         this.lastNameAvailable = false;
         if (var1 != null) {
            if (var1.startsWith("_") || var1.endsWith("_")) {
               this.checkTextView.setText(LocaleController.getString("UsernameInvalid", 2131561028));
               this.checkTextView.setTag("windowBackgroundWhiteRedText4");
               this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
               return false;
            }

            for(int var4 = 0; var4 < var1.length(); ++var4) {
               char var5 = var1.charAt(var4);
               if (var4 == 0 && var5 >= '0' && var5 <= '9') {
                  if (var2) {
                     AlertsCreator.showSimpleAlert(this, LocaleController.getString("UsernameInvalidStartNumber", 2131561031));
                  } else {
                     this.checkTextView.setText(LocaleController.getString("UsernameInvalidStartNumber", 2131561031));
                     this.checkTextView.setTag("windowBackgroundWhiteRedText4");
                     this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
                  }

                  return false;
               }

               if ((var5 < '0' || var5 > '9') && (var5 < 'a' || var5 > 'z') && (var5 < 'A' || var5 > 'Z') && var5 != '_') {
                  if (var2) {
                     AlertsCreator.showSimpleAlert(this, LocaleController.getString("UsernameInvalid", 2131561028));
                  } else {
                     this.checkTextView.setText(LocaleController.getString("UsernameInvalid", 2131561028));
                     this.checkTextView.setTag("windowBackgroundWhiteRedText4");
                     this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
                  }

                  return false;
               }
            }
         }

         if (var1 != null && var1.length() >= 5) {
            if (var1.length() > 32) {
               if (var2) {
                  AlertsCreator.showSimpleAlert(this, LocaleController.getString("UsernameInvalidLong", 2131561029));
               } else {
                  this.checkTextView.setText(LocaleController.getString("UsernameInvalidLong", 2131561029));
                  this.checkTextView.setTag("windowBackgroundWhiteRedText4");
                  this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
               }

               return false;
            } else {
               if (!var2) {
                  String var6 = UserConfig.getInstance(super.currentAccount).getCurrentUser().username;
                  String var7 = var6;
                  if (var6 == null) {
                     var7 = "";
                  }

                  if (var1.equals(var7)) {
                     this.checkTextView.setText(LocaleController.formatString("UsernameAvailable", 2131561022, var1));
                     this.checkTextView.setTag("windowBackgroundWhiteGreenText");
                     this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGreenText"));
                     return true;
                  }

                  this.checkTextView.setText(LocaleController.getString("UsernameChecking", 2131561023));
                  this.checkTextView.setTag("windowBackgroundWhiteGrayText8");
                  this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText8"));
                  this.lastCheckName = var1;
                  this.checkRunnable = new _$$Lambda$ChangeUsernameActivity$a8ZrhEmIlukHAljdlSoaVTMTqDc(this, var1);
                  AndroidUtilities.runOnUIThread(this.checkRunnable, 300L);
               }

               return true;
            }
         } else {
            if (var2) {
               AlertsCreator.showSimpleAlert(this, LocaleController.getString("UsernameInvalidShort", 2131561030));
            } else {
               this.checkTextView.setText(LocaleController.getString("UsernameInvalidShort", 2131561030));
               this.checkTextView.setTag("windowBackgroundWhiteRedText4");
               this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
            }

            return false;
         }
      }
   }

   // $FF: synthetic method
   static boolean lambda$createView$0(View var0, MotionEvent var1) {
      return true;
   }

   private void saveName() {
      if (this.checkUserName(this.firstNameField.getText().toString(), true)) {
         TLRPC.User var1 = UserConfig.getInstance(super.currentAccount).getCurrentUser();
         if (this.getParentActivity() != null && var1 != null) {
            String var2 = var1.username;
            String var5 = var2;
            if (var2 == null) {
               var5 = "";
            }

            var2 = this.firstNameField.getText().toString();
            if (var5.equals(var2)) {
               this.finishFragment();
               return;
            }

            AlertDialog var3 = new AlertDialog(this.getParentActivity(), 3);
            TLRPC.TL_account_updateUsername var6 = new TLRPC.TL_account_updateUsername();
            var6.username = var2;
            NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 1);
            int var4 = ConnectionsManager.getInstance(super.currentAccount).sendRequest(var6, new _$$Lambda$ChangeUsernameActivity$v8ZSRwHtLRMnSL9d8TsxyGq5Q58(this, var3, var6), 2);
            ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(var4, super.classGuid);
            var3.setOnCancelListener(new _$$Lambda$ChangeUsernameActivity$gFWNPcfawPg_VyQnhopqwvS2g8Q(this, var4));
            var3.show();
         }

      }
   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setTitle(LocaleController.getString("Username", 2131561021));
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               ChangeUsernameActivity.this.finishFragment();
            } else if (var1 == 1) {
               ChangeUsernameActivity.this.saveName();
            }

         }
      });
      this.doneButton = super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0F));
      TLRPC.User var2 = MessagesController.getInstance(super.currentAccount).getUser(UserConfig.getInstance(super.currentAccount).getClientUserId());
      TLRPC.User var3 = var2;
      if (var2 == null) {
         var3 = UserConfig.getInstance(super.currentAccount).getCurrentUser();
      }

      super.fragmentView = new LinearLayout(var1);
      LinearLayout var7 = (LinearLayout)super.fragmentView;
      var7.setOrientation(1);
      super.fragmentView.setOnTouchListener(_$$Lambda$ChangeUsernameActivity$OwHEXkVzOQRLF0pIctj_xlc9Dcc.INSTANCE);
      this.firstNameField = new EditTextBoldCursor(var1);
      this.firstNameField.setTextSize(1, 18.0F);
      this.firstNameField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
      this.firstNameField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.firstNameField.setBackgroundDrawable(Theme.createEditTextDrawable(var1, false));
      this.firstNameField.setMaxLines(1);
      this.firstNameField.setLines(1);
      this.firstNameField.setPadding(0, 0, 0, 0);
      this.firstNameField.setSingleLine(true);
      EditTextBoldCursor var4 = this.firstNameField;
      byte var5;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var4.setGravity(var5);
      this.firstNameField.setInputType(180224);
      this.firstNameField.setImeOptions(6);
      this.firstNameField.setHint(LocaleController.getString("UsernamePlaceholder", 2131561032));
      this.firstNameField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.firstNameField.setCursorSize(AndroidUtilities.dp(20.0F));
      this.firstNameField.setCursorWidth(1.5F);
      this.firstNameField.setOnEditorActionListener(new _$$Lambda$ChangeUsernameActivity$I2mPiyaJW258mybxgiHBR_VofGA(this));
      this.firstNameField.addTextChangedListener(new TextWatcher() {
         public void afterTextChanged(Editable var1) {
            if (ChangeUsernameActivity.this.firstNameField.length() > 0) {
               StringBuilder var4 = new StringBuilder();
               var4.append("https://");
               var4.append(MessagesController.getInstance(ChangeUsernameActivity.access$400(ChangeUsernameActivity.this)).linkPrefix);
               var4.append("/");
               var4.append(ChangeUsernameActivity.this.firstNameField.getText());
               String var5 = var4.toString();
               String var2 = LocaleController.formatString("UsernameHelpLink", 2131561026, var5);
               int var3 = var2.indexOf(var5);
               SpannableStringBuilder var6 = new SpannableStringBuilder(var2);
               if (var3 >= 0) {
                  var6.setSpan(ChangeUsernameActivity.this.new LinkSpan(var5), var3, var5.length() + var3, 33);
               }

               ChangeUsernameActivity.this.helpTextView.setText(TextUtils.concat(new CharSequence[]{ChangeUsernameActivity.this.infoText, "\n\n", var6}));
            } else {
               ChangeUsernameActivity.this.helpTextView.setText(ChangeUsernameActivity.this.infoText);
            }

         }

         public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
         }

         public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
            if (!ChangeUsernameActivity.this.ignoreCheck) {
               ChangeUsernameActivity var5 = ChangeUsernameActivity.this;
               var5.checkUserName(var5.firstNameField.getText().toString(), false);
            }
         }
      });
      var7.addView(this.firstNameField, LayoutHelper.createLinear(-1, 36, 24.0F, 24.0F, 24.0F, 0.0F));
      this.checkTextView = new TextView(var1);
      this.checkTextView.setTextSize(1, 15.0F);
      TextView var10 = this.checkTextView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var10.setGravity(var5);
      var10 = this.checkTextView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var7.addView(var10, LayoutHelper.createLinear(-2, -2, var5, 24, 12, 24, 0));
      this.helpTextView = new TextView(var1);
      this.helpTextView.setTextSize(1, 15.0F);
      this.helpTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText8"));
      TextView var6 = this.helpTextView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var6.setGravity(var5);
      var6 = this.helpTextView;
      SpannableStringBuilder var11 = AndroidUtilities.replaceTags(LocaleController.getString("UsernameHelp", 2131561025));
      this.infoText = var11;
      var6.setText(var11);
      this.helpTextView.setLinkTextColor(Theme.getColor("windowBackgroundWhiteLinkText"));
      this.helpTextView.setHighlightColor(Theme.getColor("windowBackgroundWhiteLinkSelection"));
      this.helpTextView.setMovementMethod(new ChangeUsernameActivity.LinkMovementMethodMy());
      var6 = this.helpTextView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var7.addView(var6, LayoutHelper.createLinear(-2, -2, var5, 24, 10, 24, 0));
      this.checkTextView.setVisibility(8);
      if (var3 != null) {
         String var8 = var3.username;
         if (var8 != null && var8.length() > 0) {
            this.ignoreCheck = true;
            this.firstNameField.setText(var3.username);
            EditTextBoldCursor var9 = this.firstNameField;
            var9.setSelection(var9.length());
            this.ignoreCheck = false;
         }
      }

      return super.fragmentView;
   }

   public ThemeDescription[] getThemeDescriptions() {
      return new ThemeDescription[]{new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector"), new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"), new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputField"), new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"), new ThemeDescription(this.helpTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText8"), new ThemeDescription(this.checkTextView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteRedText4"), new ThemeDescription(this.checkTextView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGreenText"), new ThemeDescription(this.checkTextView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText8")};
   }

   // $FF: synthetic method
   public void lambda$checkUserName$4$ChangeUsernameActivity(String var1) {
      TLRPC.TL_account_checkUsername var2 = new TLRPC.TL_account_checkUsername();
      var2.username = var1;
      this.checkReqId = ConnectionsManager.getInstance(super.currentAccount).sendRequest(var2, new _$$Lambda$ChangeUsernameActivity$E2MlLRNMSBRM9AKBv43t22lAhfU(this, var1), 2);
   }

   // $FF: synthetic method
   public boolean lambda$createView$1$ChangeUsernameActivity(TextView var1, int var2, KeyEvent var3) {
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
   public void lambda$null$2$ChangeUsernameActivity(String var1, TLRPC.TL_error var2, TLObject var3) {
      this.checkReqId = 0;
      String var4 = this.lastCheckName;
      if (var4 != null && var4.equals(var1)) {
         if (var2 == null && var3 instanceof TLRPC.TL_boolTrue) {
            this.checkTextView.setText(LocaleController.formatString("UsernameAvailable", 2131561022, var1));
            this.checkTextView.setTag("windowBackgroundWhiteGreenText");
            this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGreenText"));
            this.lastNameAvailable = true;
         } else {
            this.checkTextView.setText(LocaleController.getString("UsernameInUse", 2131561027));
            this.checkTextView.setTag("windowBackgroundWhiteRedText4");
            this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
            this.lastNameAvailable = false;
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$3$ChangeUsernameActivity(String var1, TLObject var2, TLRPC.TL_error var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ChangeUsernameActivity$Lqd5P0eUXkJ_zO6dWug8_uUp7XY(this, var1, var3, var2));
   }

   // $FF: synthetic method
   public void lambda$null$5$ChangeUsernameActivity(AlertDialog var1, TLRPC.User var2) {
      try {
         var1.dismiss();
      } catch (Exception var3) {
         FileLog.e((Throwable)var3);
      }

      ArrayList var4 = new ArrayList();
      var4.add(var2);
      MessagesController.getInstance(super.currentAccount).putUsers(var4, false);
      MessagesStorage.getInstance(super.currentAccount).putUsersAndChats(var4, (ArrayList)null, false, true);
      UserConfig.getInstance(super.currentAccount).saveConfig(true);
      this.finishFragment();
   }

   // $FF: synthetic method
   public void lambda$null$6$ChangeUsernameActivity(AlertDialog var1, TLRPC.TL_error var2, TLRPC.TL_account_updateUsername var3) {
      try {
         var1.dismiss();
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

      AlertsCreator.processError(super.currentAccount, var2, this, var3);
   }

   // $FF: synthetic method
   public void lambda$saveName$7$ChangeUsernameActivity(AlertDialog var1, TLRPC.TL_account_updateUsername var2, TLObject var3, TLRPC.TL_error var4) {
      if (var4 == null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$ChangeUsernameActivity$iXI1HI9G_hbgkQiH6y4X4YZM9HI(this, var1, (TLRPC.User)var3));
      } else {
         AndroidUtilities.runOnUIThread(new _$$Lambda$ChangeUsernameActivity$dxm9wNh1IKlvP_EniBN7iVOIWGY(this, var1, var4, var2));
      }

   }

   // $FF: synthetic method
   public void lambda$saveName$8$ChangeUsernameActivity(int var1, DialogInterface var2) {
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

   private static class LinkMovementMethodMy extends LinkMovementMethod {
      private LinkMovementMethodMy() {
      }

      // $FF: synthetic method
      LinkMovementMethodMy(Object var1) {
         this();
      }

      public boolean onTouchEvent(TextView var1, Spannable var2, MotionEvent var3) {
         try {
            boolean var4 = super.onTouchEvent(var1, var2, var3);
            if (var3.getAction() == 1 || var3.getAction() == 3) {
               Selection.removeSelection(var2);
            }

            return var4;
         } catch (Exception var5) {
            FileLog.e((Throwable)var5);
            return false;
         }
      }
   }

   public class LinkSpan extends ClickableSpan {
      private String url;

      public LinkSpan(String var2) {
         this.url = var2;
      }

      public void onClick(View var1) {
         try {
            ((ClipboardManager)ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", this.url));
            Toast.makeText(ChangeUsernameActivity.this.getParentActivity(), LocaleController.getString("LinkCopied", 2131559751), 0).show();
         } catch (Exception var2) {
            FileLog.e((Throwable)var2);
         }

      }

      public void updateDrawState(TextPaint var1) {
         super.updateDrawState(var1);
         var1.setUnderlineText(false);
      }
   }
}
