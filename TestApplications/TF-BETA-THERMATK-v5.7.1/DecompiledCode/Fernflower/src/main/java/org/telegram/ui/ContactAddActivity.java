package org.telegram.ui;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;

public class ContactAddActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   private static final int done_button = 1;
   private boolean addContact;
   private AvatarDrawable avatarDrawable;
   private BackupImageView avatarImage;
   private View doneButton;
   private EditTextBoldCursor firstNameField;
   private EditTextBoldCursor lastNameField;
   private TextView nameTextView;
   private TextView onlineTextView;
   private String phone = null;
   private int user_id;

   public ContactAddActivity(Bundle var1) {
      super(var1);
   }

   // $FF: synthetic method
   static int access$200(ContactAddActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$400(ContactAddActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$500(ContactAddActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$600(ContactAddActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$700(ContactAddActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static boolean lambda$createView$0(View var0, MotionEvent var1) {
      return true;
   }

   private void updateAvatarLayout() {
      if (this.nameTextView != null) {
         TLRPC.User var1 = MessagesController.getInstance(super.currentAccount).getUser(this.user_id);
         if (var1 != null) {
            TextView var2 = this.nameTextView;
            PhoneFormat var3 = PhoneFormat.getInstance();
            StringBuilder var4 = new StringBuilder();
            var4.append("+");
            var4.append(var1.phone);
            var2.setText(var3.format(var4.toString()));
            this.onlineTextView.setText(LocaleController.formatUserStatus(super.currentAccount, var1));
            BackupImageView var7 = this.avatarImage;
            ImageLocation var5 = ImageLocation.getForUser(var1, false);
            AvatarDrawable var6 = new AvatarDrawable(var1);
            this.avatarDrawable = var6;
            var7.setImage((ImageLocation)var5, "50_50", (Drawable)var6, (Object)var1);
         }
      }
   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      if (this.addContact) {
         super.actionBar.setTitle(LocaleController.getString("AddContactTitle", 2131558569));
      } else {
         super.actionBar.setTitle(LocaleController.getString("EditName", 2131559326));
      }

      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               ContactAddActivity.this.finishFragment();
            } else if (var1 == 1 && ContactAddActivity.this.firstNameField.getText().length() != 0) {
               TLRPC.User var2 = MessagesController.getInstance(ContactAddActivity.access$200(ContactAddActivity.this)).getUser(ContactAddActivity.this.user_id);
               var2.first_name = ContactAddActivity.this.firstNameField.getText().toString();
               var2.last_name = ContactAddActivity.this.lastNameField.getText().toString();
               ContactsController.getInstance(ContactAddActivity.access$400(ContactAddActivity.this)).addContact(var2);
               ContactAddActivity.this.finishFragment();
               Editor var4 = MessagesController.getNotificationsSettings(ContactAddActivity.access$500(ContactAddActivity.this)).edit();
               StringBuilder var3 = new StringBuilder();
               var3.append("spam3_");
               var3.append(ContactAddActivity.this.user_id);
               var4.putInt(var3.toString(), 1).commit();
               NotificationCenter.getInstance(ContactAddActivity.access$600(ContactAddActivity.this)).postNotificationName(NotificationCenter.updateInterfaces, 1);
               NotificationCenter.getInstance(ContactAddActivity.access$700(ContactAddActivity.this)).postNotificationName(NotificationCenter.peerSettingsDidLoad, (long)ContactAddActivity.this.user_id);
            }

         }
      });
      this.doneButton = super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0F));
      super.fragmentView = new ScrollView(var1);
      LinearLayout var2 = new LinearLayout(var1);
      var2.setOrientation(1);
      ((ScrollView)super.fragmentView).addView(var2, LayoutHelper.createScroll(-1, -2, 51));
      var2.setOnTouchListener(_$$Lambda$ContactAddActivity$A7kSn3Cfc_ajr4rigI3HkJXjVCE.INSTANCE);
      FrameLayout var3 = new FrameLayout(var1);
      var2.addView(var3, LayoutHelper.createLinear(-1, -2, 24.0F, 24.0F, 24.0F, 0.0F));
      this.avatarImage = new BackupImageView(var1);
      this.avatarImage.setRoundRadius(AndroidUtilities.dp(30.0F));
      BackupImageView var4 = this.avatarImage;
      boolean var5 = LocaleController.isRTL;
      byte var6 = 3;
      byte var7;
      if (var5) {
         var7 = 5;
      } else {
         var7 = 3;
      }

      var3.addView(var4, LayoutHelper.createFrame(60, 60, var7 | 48));
      this.nameTextView = new TextView(var1);
      this.nameTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.nameTextView.setTextSize(1, 20.0F);
      this.nameTextView.setLines(1);
      this.nameTextView.setMaxLines(1);
      this.nameTextView.setSingleLine(true);
      this.nameTextView.setEllipsize(TruncateAt.END);
      TextView var15 = this.nameTextView;
      if (LocaleController.isRTL) {
         var7 = 5;
      } else {
         var7 = 3;
      }

      var15.setGravity(var7);
      this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      var15 = this.nameTextView;
      if (LocaleController.isRTL) {
         var7 = 5;
      } else {
         var7 = 3;
      }

      float var8;
      if (LocaleController.isRTL) {
         var8 = 0.0F;
      } else {
         var8 = 80.0F;
      }

      float var9;
      if (LocaleController.isRTL) {
         var9 = 80.0F;
      } else {
         var9 = 0.0F;
      }

      var3.addView(var15, LayoutHelper.createFrame(-2, -2.0F, var7 | 48, var8, 3.0F, var9, 0.0F));
      this.onlineTextView = new TextView(var1);
      this.onlineTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
      this.onlineTextView.setTextSize(1, 14.0F);
      this.onlineTextView.setLines(1);
      this.onlineTextView.setMaxLines(1);
      this.onlineTextView.setSingleLine(true);
      this.onlineTextView.setEllipsize(TruncateAt.END);
      var15 = this.onlineTextView;
      if (LocaleController.isRTL) {
         var7 = 5;
      } else {
         var7 = 3;
      }

      var15.setGravity(var7);
      var15 = this.onlineTextView;
      if (LocaleController.isRTL) {
         var7 = 5;
      } else {
         var7 = 3;
      }

      if (LocaleController.isRTL) {
         var8 = 0.0F;
      } else {
         var8 = 80.0F;
      }

      if (LocaleController.isRTL) {
         var9 = 80.0F;
      } else {
         var9 = 0.0F;
      }

      var3.addView(var15, LayoutHelper.createFrame(-2, -2.0F, var7 | 48, var8, 32.0F, var9, 0.0F));
      this.firstNameField = new EditTextBoldCursor(var1);
      this.firstNameField.setTextSize(1, 18.0F);
      this.firstNameField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
      this.firstNameField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.firstNameField.setBackgroundDrawable(Theme.createEditTextDrawable(var1, false));
      this.firstNameField.setMaxLines(1);
      this.firstNameField.setLines(1);
      this.firstNameField.setSingleLine(true);
      EditTextBoldCursor var14 = this.firstNameField;
      if (LocaleController.isRTL) {
         var7 = 5;
      } else {
         var7 = 3;
      }

      var14.setGravity(var7);
      this.firstNameField.setInputType(49152);
      this.firstNameField.setImeOptions(5);
      this.firstNameField.setHint(LocaleController.getString("FirstName", 2131559494));
      this.firstNameField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.firstNameField.setCursorSize(AndroidUtilities.dp(20.0F));
      this.firstNameField.setCursorWidth(1.5F);
      var2.addView(this.firstNameField, LayoutHelper.createLinear(-1, 36, 24.0F, 24.0F, 24.0F, 0.0F));
      this.firstNameField.setOnEditorActionListener(new _$$Lambda$ContactAddActivity$xQQmG_ikgUejGCdP82NOkH8zIao(this));
      this.lastNameField = new EditTextBoldCursor(var1);
      this.lastNameField.setTextSize(1, 18.0F);
      this.lastNameField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
      this.lastNameField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.lastNameField.setBackgroundDrawable(Theme.createEditTextDrawable(var1, false));
      this.lastNameField.setMaxLines(1);
      this.lastNameField.setLines(1);
      this.lastNameField.setSingleLine(true);
      EditTextBoldCursor var10 = this.lastNameField;
      var7 = var6;
      if (LocaleController.isRTL) {
         var7 = 5;
      }

      var10.setGravity(var7);
      this.lastNameField.setInputType(49152);
      this.lastNameField.setImeOptions(6);
      this.lastNameField.setHint(LocaleController.getString("LastName", 2131559728));
      this.lastNameField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.lastNameField.setCursorSize(AndroidUtilities.dp(20.0F));
      this.lastNameField.setCursorWidth(1.5F);
      var2.addView(this.lastNameField, LayoutHelper.createLinear(-1, 36, 24.0F, 16.0F, 24.0F, 0.0F));
      this.lastNameField.setOnEditorActionListener(new _$$Lambda$ContactAddActivity$LmoZEE36adLyzqPJDOoRrL7aQWs(this));
      TLRPC.User var11 = MessagesController.getInstance(super.currentAccount).getUser(this.user_id);
      if (var11 != null) {
         if (var11.phone == null) {
            String var12 = this.phone;
            if (var12 != null) {
               var11.phone = PhoneFormat.stripExceptNumbers(var12);
            }
         }

         this.firstNameField.setText(var11.first_name);
         EditTextBoldCursor var13 = this.firstNameField;
         var13.setSelection(var13.length());
         this.lastNameField.setText(var11.last_name);
      }

      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.updateInterfaces) {
         var1 = (Integer)var3[0];
         if ((var1 & 2) != 0 || (var1 & 4) != 0) {
            this.updateAvatarLayout();
         }
      }

   }

   public ThemeDescription[] getThemeDescriptions() {
      _$$Lambda$ContactAddActivity$SkTZ31Qqc_ITmfJvKZ4HlR_Vj08 var1 = new _$$Lambda$ContactAddActivity$SkTZ31Qqc_ITmfJvKZ4HlR_Vj08(this);
      return new ThemeDescription[]{new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector"), new ThemeDescription(this.nameTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.onlineTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText3"), new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"), new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputField"), new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"), new ThemeDescription(this.lastNameField, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.lastNameField, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"), new ThemeDescription(this.lastNameField, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputField"), new ThemeDescription(this.lastNameField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, new Drawable[]{Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable}, var1, "avatar_text"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundRed"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundOrange"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundViolet"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundGreen"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundCyan"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundBlue"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundPink")};
   }

   // $FF: synthetic method
   public boolean lambda$createView$1$ContactAddActivity(TextView var1, int var2, KeyEvent var3) {
      if (var2 == 5) {
         this.lastNameField.requestFocus();
         EditTextBoldCursor var4 = this.lastNameField;
         var4.setSelection(var4.length());
         return true;
      } else {
         return false;
      }
   }

   // $FF: synthetic method
   public boolean lambda$createView$2$ContactAddActivity(TextView var1, int var2, KeyEvent var3) {
      if (var2 == 6) {
         this.doneButton.performClick();
         return true;
      } else {
         return false;
      }
   }

   // $FF: synthetic method
   public void lambda$getThemeDescriptions$3$ContactAddActivity() {
      if (this.avatarImage != null) {
         TLRPC.User var1 = MessagesController.getInstance(super.currentAccount).getUser(this.user_id);
         if (var1 == null) {
            return;
         }

         this.avatarDrawable.setInfo(var1);
         this.avatarImage.invalidate();
      }

   }

   public boolean onFragmentCreate() {
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
      Bundle var1 = this.getArguments();
      boolean var2 = false;
      this.user_id = var1.getInt("user_id", 0);
      this.phone = this.getArguments().getString("phone");
      this.addContact = this.getArguments().getBoolean("addContact", false);
      boolean var3 = var2;
      if (MessagesController.getInstance(super.currentAccount).getUser(this.user_id) != null) {
         var3 = var2;
         if (super.onFragmentCreate()) {
            var3 = true;
         }
      }

      return var3;
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.updateInterfaces);
   }

   public void onResume() {
      super.onResume();
      this.updateAvatarLayout();
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
