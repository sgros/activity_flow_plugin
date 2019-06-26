package org.telegram.ui;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;

public class ChangeNameActivity extends BaseFragment {
   private static final int done_button = 1;
   private View doneButton;
   private EditTextBoldCursor firstNameField;
   private View headerLabelView;
   private EditTextBoldCursor lastNameField;

   private void saveName() {
      TLRPC.User var1 = UserConfig.getInstance(super.currentAccount).getCurrentUser();
      if (var1 != null && this.lastNameField.getText() != null && this.firstNameField.getText() != null) {
         String var2 = this.firstNameField.getText().toString();
         String var3 = this.lastNameField.getText().toString();
         String var4 = var1.first_name;
         if (var4 != null && var4.equals(var2)) {
            var4 = var1.last_name;
            if (var4 != null && var4.equals(var3)) {
               return;
            }
         }

         TLRPC.TL_account_updateProfile var6 = new TLRPC.TL_account_updateProfile();
         var6.flags = 3;
         var6.first_name = var2;
         var1.first_name = var2;
         var6.last_name = var3;
         var1.last_name = var3;
         TLRPC.User var5 = MessagesController.getInstance(super.currentAccount).getUser(UserConfig.getInstance(super.currentAccount).getClientUserId());
         if (var5 != null) {
            var5.first_name = var6.first_name;
            var5.last_name = var6.last_name;
         }

         UserConfig.getInstance(super.currentAccount).saveConfig(true);
         NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.mainUserInfoChanged);
         NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 1);
         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var6, new RequestDelegate() {
            public void run(TLObject var1, TLRPC.TL_error var2) {
            }
         });
      }

   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setTitle(LocaleController.getString("EditName", 2131559326));
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               ChangeNameActivity.this.finishFragment();
            } else if (var1 == 1 && ChangeNameActivity.this.firstNameField.getText().length() != 0) {
               ChangeNameActivity.this.saveName();
               ChangeNameActivity.this.finishFragment();
            }

         }
      });
      this.doneButton = super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0F));
      TLRPC.User var2 = MessagesController.getInstance(super.currentAccount).getUser(UserConfig.getInstance(super.currentAccount).getClientUserId());
      TLRPC.User var3 = var2;
      if (var2 == null) {
         var3 = UserConfig.getInstance(super.currentAccount).getCurrentUser();
      }

      LinearLayout var9 = new LinearLayout(var1);
      super.fragmentView = var9;
      super.fragmentView.setLayoutParams(new LayoutParams(-1, -1));
      ((LinearLayout)super.fragmentView).setOrientation(1);
      super.fragmentView.setOnTouchListener(new OnTouchListener() {
         public boolean onTouch(View var1, MotionEvent var2) {
            return true;
         }
      });
      this.firstNameField = new EditTextBoldCursor(var1);
      this.firstNameField.setTextSize(1, 18.0F);
      this.firstNameField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
      this.firstNameField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.firstNameField.setBackgroundDrawable(Theme.createEditTextDrawable(var1, false));
      this.firstNameField.setMaxLines(1);
      this.firstNameField.setLines(1);
      this.firstNameField.setSingleLine(true);
      EditTextBoldCursor var4 = this.firstNameField;
      boolean var5 = LocaleController.isRTL;
      byte var6 = 3;
      byte var7;
      if (var5) {
         var7 = 5;
      } else {
         var7 = 3;
      }

      var4.setGravity(var7);
      this.firstNameField.setInputType(49152);
      this.firstNameField.setImeOptions(5);
      this.firstNameField.setHint(LocaleController.getString("FirstName", 2131559494));
      this.firstNameField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.firstNameField.setCursorSize(AndroidUtilities.dp(20.0F));
      this.firstNameField.setCursorWidth(1.5F);
      var9.addView(this.firstNameField, LayoutHelper.createLinear(-1, 36, 24.0F, 24.0F, 24.0F, 0.0F));
      this.firstNameField.setOnEditorActionListener(new OnEditorActionListener() {
         public boolean onEditorAction(TextView var1, int var2, KeyEvent var3) {
            if (var2 == 5) {
               ChangeNameActivity.this.lastNameField.requestFocus();
               ChangeNameActivity.this.lastNameField.setSelection(ChangeNameActivity.this.lastNameField.length());
               return true;
            } else {
               return false;
            }
         }
      });
      this.lastNameField = new EditTextBoldCursor(var1);
      this.lastNameField.setTextSize(1, 18.0F);
      this.lastNameField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
      this.lastNameField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.lastNameField.setBackgroundDrawable(Theme.createEditTextDrawable(var1, false));
      this.lastNameField.setMaxLines(1);
      this.lastNameField.setLines(1);
      this.lastNameField.setSingleLine(true);
      EditTextBoldCursor var8 = this.lastNameField;
      var7 = var6;
      if (LocaleController.isRTL) {
         var7 = 5;
      }

      var8.setGravity(var7);
      this.lastNameField.setInputType(49152);
      this.lastNameField.setImeOptions(6);
      this.lastNameField.setHint(LocaleController.getString("LastName", 2131559728));
      this.lastNameField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.lastNameField.setCursorSize(AndroidUtilities.dp(20.0F));
      this.lastNameField.setCursorWidth(1.5F);
      var9.addView(this.lastNameField, LayoutHelper.createLinear(-1, 36, 24.0F, 16.0F, 24.0F, 0.0F));
      this.lastNameField.setOnEditorActionListener(new OnEditorActionListener() {
         public boolean onEditorAction(TextView var1, int var2, KeyEvent var3) {
            if (var2 == 6) {
               ChangeNameActivity.this.doneButton.performClick();
               return true;
            } else {
               return false;
            }
         }
      });
      if (var3 != null) {
         this.firstNameField.setText(var3.first_name);
         var8 = this.firstNameField;
         var8.setSelection(var8.length());
         this.lastNameField.setText(var3.last_name);
      }

      return super.fragmentView;
   }

   public ThemeDescription[] getThemeDescriptions() {
      return new ThemeDescription[]{new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector"), new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"), new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputField"), new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"), new ThemeDescription(this.lastNameField, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.lastNameField, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"), new ThemeDescription(this.lastNameField, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputField"), new ThemeDescription(this.lastNameField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated")};
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
         AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
               if (ChangeNameActivity.this.firstNameField != null) {
                  ChangeNameActivity.this.firstNameField.requestFocus();
                  AndroidUtilities.showKeyboard(ChangeNameActivity.this.firstNameField);
               }

            }
         }, 100L);
      }

   }
}
