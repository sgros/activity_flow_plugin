package org.telegram.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.os.Build.VERSION;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.InputFilter.LengthFilter;
import android.text.TextUtils.TruncateAt;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.text.method.PasswordTransformationMethod;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ActionMode.Callback;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.support.fingerprint.FingerprintManagerCompat;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.NumberPicker;
import org.telegram.ui.Components.RecyclerListView;

public class PasscodeActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   private static final int done_button = 1;
   private static final int password_item = 3;
   private static final int pin_item = 2;
   private int autoLockDetailRow;
   private int autoLockRow;
   private int badPasscodeTries;
   private int captureDetailRow;
   private int captureRow;
   private int changePasscodeRow;
   private int currentPasswordType = 0;
   private TextView dropDown;
   private ActionBarMenuItem dropDownContainer;
   private Drawable dropDownDrawable;
   private int fingerprintRow;
   private String firstPassword;
   private long lastPasscodeTry;
   private PasscodeActivity.ListAdapter listAdapter;
   private RecyclerListView listView;
   private int passcodeDetailRow;
   private int passcodeRow;
   private int passcodeSetStep = 0;
   private EditTextBoldCursor passwordEditText;
   private int rowCount;
   private TextView titleTextView;
   private int type;

   public PasscodeActivity(int var1) {
      this.type = var1;
   }

   private void fixLayoutInternal() {
      if (this.dropDownContainer != null) {
         if (!AndroidUtilities.isTablet()) {
            LayoutParams var1 = (LayoutParams)this.dropDownContainer.getLayoutParams();
            int var2;
            if (VERSION.SDK_INT >= 21) {
               var2 = AndroidUtilities.statusBarHeight;
            } else {
               var2 = 0;
            }

            var1.topMargin = var2;
            this.dropDownContainer.setLayoutParams(var1);
         }

         if (!AndroidUtilities.isTablet() && ApplicationLoader.applicationContext.getResources().getConfiguration().orientation == 2) {
            this.dropDown.setTextSize(18.0F);
         } else {
            this.dropDown.setTextSize(20.0F);
         }
      }

   }

   // $FF: synthetic method
   static String lambda$null$2(int var0) {
      if (var0 == 0) {
         return LocaleController.getString("AutoLockDisabled", 2131558779);
      } else if (var0 == 1) {
         return LocaleController.formatString("AutoLockInTime", 2131558780, LocaleController.formatPluralString("Minutes", 1));
      } else if (var0 == 2) {
         return LocaleController.formatString("AutoLockInTime", 2131558780, LocaleController.formatPluralString("Minutes", 5));
      } else if (var0 == 3) {
         return LocaleController.formatString("AutoLockInTime", 2131558780, LocaleController.formatPluralString("Hours", 1));
      } else {
         return var0 == 4 ? LocaleController.formatString("AutoLockInTime", 2131558780, LocaleController.formatPluralString("Hours", 5)) : "";
      }
   }

   private void onPasscodeError() {
      if (this.getParentActivity() != null) {
         Vibrator var1 = (Vibrator)this.getParentActivity().getSystemService("vibrator");
         if (var1 != null) {
            var1.vibrate(200L);
         }

         AndroidUtilities.shakeView(this.titleTextView, 2.0F, 0);
      }
   }

   private void processDone() {
      if (this.passwordEditText.getText().length() == 0) {
         this.onPasscodeError();
      } else {
         int var1 = this.type;
         if (var1 == 1) {
            if (!this.firstPassword.equals(this.passwordEditText.getText().toString())) {
               try {
                  Toast.makeText(this.getParentActivity(), LocaleController.getString("PasscodeDoNotMatch", 2131560161), 0).show();
               } catch (Exception var8) {
                  FileLog.e((Throwable)var8);
               }

               AndroidUtilities.shakeView(this.titleTextView, 2.0F, 0);
               this.passwordEditText.setText("");
               return;
            }

            try {
               SharedConfig.passcodeSalt = new byte[16];
               Utilities.random.nextBytes(SharedConfig.passcodeSalt);
               byte[] var2 = this.firstPassword.getBytes("UTF-8");
               byte[] var3 = new byte[var2.length + 32];
               System.arraycopy(SharedConfig.passcodeSalt, 0, var3, 0, 16);
               System.arraycopy(var2, 0, var3, 16, var2.length);
               System.arraycopy(SharedConfig.passcodeSalt, 0, var3, var2.length + 16, 16);
               SharedConfig.passcodeHash = Utilities.bytesToHex(Utilities.computeSHA256(var3, 0, var3.length));
            } catch (Exception var9) {
               FileLog.e((Throwable)var9);
            }

            SharedConfig.allowScreenCapture = true;
            SharedConfig.passcodeType = this.currentPasswordType;
            SharedConfig.saveConfig();
            this.finishFragment();
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetPasscode);
            this.passwordEditText.clearFocus();
            AndroidUtilities.hideKeyboard(this.passwordEditText);
         } else if (var1 == 2) {
            long var4 = SharedConfig.passcodeRetryInMs;
            if (var4 > 0L) {
               double var6 = (double)var4;
               Double.isNaN(var6);
               var1 = Math.max(1, (int)Math.ceil(var6 / 1000.0D));
               Toast.makeText(this.getParentActivity(), LocaleController.formatString("TooManyTries", 2131560909, LocaleController.formatPluralString("Seconds", var1)), 0).show();
               this.passwordEditText.setText("");
               this.onPasscodeError();
               return;
            }

            if (!SharedConfig.checkPasscode(this.passwordEditText.getText().toString())) {
               SharedConfig.increaseBadPasscodeTries();
               this.passwordEditText.setText("");
               this.onPasscodeError();
               return;
            }

            SharedConfig.badPasscodeTries = 0;
            SharedConfig.saveConfig();
            this.passwordEditText.clearFocus();
            AndroidUtilities.hideKeyboard(this.passwordEditText);
            this.presentFragment(new PasscodeActivity(0), true);
         }

      }
   }

   private void processNext() {
      if (this.passwordEditText.getText().length() != 0 && (this.currentPasswordType != 0 || this.passwordEditText.getText().length() == 4)) {
         if (this.currentPasswordType == 0) {
            super.actionBar.setTitle(LocaleController.getString("PasscodePIN", 2131560162));
         } else {
            super.actionBar.setTitle(LocaleController.getString("PasscodePassword", 2131560163));
         }

         this.dropDownContainer.setVisibility(8);
         this.titleTextView.setText(LocaleController.getString("ReEnterYourPasscode", 2131560536));
         this.firstPassword = this.passwordEditText.getText().toString();
         this.passwordEditText.setText("");
         this.passcodeSetStep = 1;
      } else {
         this.onPasscodeError();
      }
   }

   private void updateDropDownTextView() {
      TextView var1 = this.dropDown;
      if (var1 != null) {
         int var2 = this.currentPasswordType;
         if (var2 == 0) {
            var1.setText(LocaleController.getString("PasscodePIN", 2131560162));
         } else if (var2 == 1) {
            var1.setText(LocaleController.getString("PasscodePassword", 2131560163));
         }
      }

      if ((this.type != 1 || this.currentPasswordType != 0) && (this.type != 2 || SharedConfig.passcodeType != 0)) {
         if (this.type == 1 && this.currentPasswordType == 1 || this.type == 2 && SharedConfig.passcodeType == 1) {
            this.passwordEditText.setFilters(new InputFilter[0]);
            this.passwordEditText.setKeyListener((KeyListener)null);
            this.passwordEditText.setInputType(129);
         }
      } else {
         LengthFilter var3 = new LengthFilter(4);
         this.passwordEditText.setFilters(new InputFilter[]{var3});
         this.passwordEditText.setInputType(3);
         this.passwordEditText.setKeyListener(DigitsKeyListener.getInstance("1234567890"));
      }

      this.passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
   }

   private void updateRows() {
      this.rowCount = 0;
      int var1 = this.rowCount++;
      this.passcodeRow = var1;
      var1 = this.rowCount++;
      this.changePasscodeRow = var1;
      var1 = this.rowCount++;
      this.passcodeDetailRow = var1;
      if (SharedConfig.passcodeHash.length() > 0) {
         try {
            if (VERSION.SDK_INT >= 23 && FingerprintManagerCompat.from(ApplicationLoader.applicationContext).isHardwareDetected()) {
               var1 = this.rowCount++;
               this.fingerprintRow = var1;
            }
         } catch (Throwable var3) {
            FileLog.e(var3);
         }

         var1 = this.rowCount++;
         this.autoLockRow = var1;
         var1 = this.rowCount++;
         this.autoLockDetailRow = var1;
         var1 = this.rowCount++;
         this.captureRow = var1;
         var1 = this.rowCount++;
         this.captureDetailRow = var1;
      } else {
         this.captureRow = -1;
         this.captureDetailRow = -1;
         this.fingerprintRow = -1;
         this.autoLockRow = -1;
         this.autoLockDetailRow = -1;
      }

   }

   public View createView(Context var1) {
      if (this.type != 3) {
         super.actionBar.setBackButtonImage(2131165409);
      }

      super.actionBar.setAllowOverlayTitle(false);
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               PasscodeActivity.this.finishFragment();
            } else if (var1 == 1) {
               if (PasscodeActivity.this.passcodeSetStep == 0) {
                  PasscodeActivity.this.processNext();
               } else if (PasscodeActivity.this.passcodeSetStep == 1) {
                  PasscodeActivity.this.processDone();
               }
            } else if (var1 == 2) {
               PasscodeActivity.this.currentPasswordType = 0;
               PasscodeActivity.this.updateDropDownTextView();
            } else if (var1 == 3) {
               PasscodeActivity.this.currentPasswordType = 1;
               PasscodeActivity.this.updateDropDownTextView();
            }

         }
      });
      super.fragmentView = new FrameLayout(var1);
      FrameLayout var2 = (FrameLayout)super.fragmentView;
      if (this.type != 0) {
         ActionBarMenu var3 = super.actionBar.createMenu();
         var3.addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0F));
         this.titleTextView = new TextView(var1);
         this.titleTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
         if (this.type == 1) {
            if (SharedConfig.passcodeHash.length() != 0) {
               this.titleTextView.setText(LocaleController.getString("EnterNewPasscode", 2131559372));
            } else {
               this.titleTextView.setText(LocaleController.getString("EnterNewFirstPasscode", 2131559371));
            }
         } else {
            this.titleTextView.setText(LocaleController.getString("EnterCurrentPasscode", 2131559368));
         }

         this.titleTextView.setTextSize(1, 18.0F);
         this.titleTextView.setGravity(1);
         var2.addView(this.titleTextView, LayoutHelper.createFrame(-2, -2.0F, 1, 0.0F, 38.0F, 0.0F, 0.0F));
         this.passwordEditText = new EditTextBoldCursor(var1);
         this.passwordEditText.setTextSize(1, 20.0F);
         this.passwordEditText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.passwordEditText.setBackgroundDrawable(Theme.createEditTextDrawable(var1, false));
         this.passwordEditText.setMaxLines(1);
         this.passwordEditText.setLines(1);
         this.passwordEditText.setGravity(1);
         this.passwordEditText.setSingleLine(true);
         if (this.type == 1) {
            this.passcodeSetStep = 0;
            this.passwordEditText.setImeOptions(5);
         } else {
            this.passcodeSetStep = 1;
            this.passwordEditText.setImeOptions(6);
         }

         this.passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
         this.passwordEditText.setTypeface(Typeface.DEFAULT);
         this.passwordEditText.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.passwordEditText.setCursorSize(AndroidUtilities.dp(20.0F));
         this.passwordEditText.setCursorWidth(1.5F);
         var2.addView(this.passwordEditText, LayoutHelper.createFrame(-1, 36.0F, 51, 40.0F, 90.0F, 40.0F, 0.0F));
         this.passwordEditText.setOnEditorActionListener(new _$$Lambda$PasscodeActivity$Dw5wi6axlaDg9f9aOPWpPJRaBU0(this));
         this.passwordEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable var1) {
               if (PasscodeActivity.this.passwordEditText.length() == 4) {
                  if (PasscodeActivity.this.type == 2 && SharedConfig.passcodeType == 0) {
                     PasscodeActivity.this.processDone();
                  } else if (PasscodeActivity.this.type == 1 && PasscodeActivity.this.currentPasswordType == 0) {
                     if (PasscodeActivity.this.passcodeSetStep == 0) {
                        PasscodeActivity.this.processNext();
                     } else if (PasscodeActivity.this.passcodeSetStep == 1) {
                        PasscodeActivity.this.processDone();
                     }
                  }
               }

            }

            public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
            }

            public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
            }
         });
         this.passwordEditText.setCustomSelectionActionModeCallback(new Callback() {
            public boolean onActionItemClicked(ActionMode var1, MenuItem var2) {
               return false;
            }

            public boolean onCreateActionMode(ActionMode var1, Menu var2) {
               return false;
            }

            public void onDestroyActionMode(ActionMode var1) {
            }

            public boolean onPrepareActionMode(ActionMode var1, Menu var2) {
               return false;
            }
         });
         if (this.type == 1) {
            var2.setTag("windowBackgroundWhite");
            this.dropDownContainer = new ActionBarMenuItem(var1, var3, 0, 0);
            this.dropDownContainer.setSubMenuOpenSide(1);
            this.dropDownContainer.addSubItem(2, LocaleController.getString("PasscodePIN", 2131560162));
            this.dropDownContainer.addSubItem(3, LocaleController.getString("PasscodePassword", 2131560163));
            ActionBar var6 = super.actionBar;
            ActionBarMenuItem var8 = this.dropDownContainer;
            float var4;
            if (AndroidUtilities.isTablet()) {
               var4 = 64.0F;
            } else {
               var4 = 56.0F;
            }

            var6.addView(var8, LayoutHelper.createFrame(-2, -1.0F, 51, var4, 0.0F, 40.0F, 0.0F));
            this.dropDownContainer.setOnClickListener(new _$$Lambda$PasscodeActivity$nthmgeFTBNbMbbgybaEUi4bvQ7I(this));
            this.dropDown = new TextView(var1);
            this.dropDown.setGravity(3);
            this.dropDown.setSingleLine(true);
            this.dropDown.setLines(1);
            this.dropDown.setMaxLines(1);
            this.dropDown.setEllipsize(TruncateAt.END);
            this.dropDown.setTextColor(Theme.getColor("actionBarDefaultTitle"));
            this.dropDown.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.dropDownDrawable = var1.getResources().getDrawable(2131165427).mutate();
            this.dropDownDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("actionBarDefaultTitle"), Mode.MULTIPLY));
            this.dropDown.setCompoundDrawablesWithIntrinsicBounds((Drawable)null, (Drawable)null, this.dropDownDrawable, (Drawable)null);
            this.dropDown.setCompoundDrawablePadding(AndroidUtilities.dp(4.0F));
            this.dropDown.setPadding(0, 0, AndroidUtilities.dp(10.0F), 0);
            this.dropDownContainer.addView(this.dropDown, LayoutHelper.createFrame(-2, -2.0F, 16, 16.0F, 0.0F, 0.0F, 1.0F));
         } else {
            super.actionBar.setTitle(LocaleController.getString("Passcode", 2131560160));
         }

         this.updateDropDownTextView();
      } else {
         super.actionBar.setTitle(LocaleController.getString("Passcode", 2131560160));
         var2.setTag("windowBackgroundGray");
         var2.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
         this.listView = new RecyclerListView(var1);
         this.listView.setLayoutManager(new LinearLayoutManager(var1, 1, false) {
            public boolean supportsPredictiveItemAnimations() {
               return false;
            }
         });
         this.listView.setVerticalScrollBarEnabled(false);
         this.listView.setItemAnimator((RecyclerView.ItemAnimator)null);
         this.listView.setLayoutAnimation((LayoutAnimationController)null);
         var2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F));
         RecyclerListView var7 = this.listView;
         PasscodeActivity.ListAdapter var5 = new PasscodeActivity.ListAdapter(var1);
         this.listAdapter = var5;
         var7.setAdapter(var5);
         this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$PasscodeActivity$mrP17AePE_jxJGc4Lp8zfeRkzb0(this)));
      }

      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.didSetPasscode && this.type == 0) {
         this.updateRows();
         PasscodeActivity.ListAdapter var4 = this.listAdapter;
         if (var4 != null) {
            var4.notifyDataSetChanged();
         }
      }

   }

   public ThemeDescription[] getThemeDescriptions() {
      ThemeDescription var1 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextCheckCell.class, TextSettingsCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var2 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      View var3 = super.fragmentView;
      int var4 = ThemeDescription.FLAG_BACKGROUND;
      ThemeDescription var5 = new ThemeDescription(var3, ThemeDescription.FLAG_CHECKTAG | var4, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var7 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var8 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var9 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var10 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var11 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSubmenuBackground");
      ThemeDescription var12 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSubmenuItem");
      ThemeDescription var16 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM | ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSubmenuItemIcon");
      ThemeDescription var13 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var14 = this.listView;
      Paint var15 = Theme.dividerPaint;
      return new ThemeDescription[]{var1, var2, var5, var6, var7, var8, var9, var10, var11, var12, var16, var13, new ThemeDescription(var14, 0, new Class[]{View.class}, var15, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"), new ThemeDescription(this.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"), new ThemeDescription(this.passwordEditText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.passwordEditText, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputField"), new ThemeDescription(this.passwordEditText, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"), new ThemeDescription(this.dropDown, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle"), new ThemeDescription(this.dropDown, 0, (Class[])null, (Paint)null, new Drawable[]{this.dropDownDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrack"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrackChecked"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText7"), new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteValueText"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4")};
   }

   // $FF: synthetic method
   public boolean lambda$createView$0$PasscodeActivity(TextView var1, int var2, KeyEvent var3) {
      var2 = this.passcodeSetStep;
      if (var2 == 0) {
         this.processNext();
         return true;
      } else if (var2 == 1) {
         this.processDone();
         return true;
      } else {
         return false;
      }
   }

   // $FF: synthetic method
   public void lambda$createView$1$PasscodeActivity(View var1) {
      this.dropDownContainer.toggleSubMenu();
   }

   // $FF: synthetic method
   public void lambda$createView$4$PasscodeActivity(View var1, int var2) {
      if (var1.isEnabled()) {
         int var3 = this.changePasscodeRow;
         boolean var4 = true;
         if (var2 == var3) {
            this.presentFragment(new PasscodeActivity(1));
         } else if (var2 == this.passcodeRow) {
            TextCheckCell var5 = (TextCheckCell)var1;
            if (SharedConfig.passcodeHash.length() != 0) {
               SharedConfig.passcodeHash = "";
               SharedConfig.appLocked = false;
               SharedConfig.saveConfig();
               var3 = this.listView.getChildCount();

               for(var2 = 0; var2 < var3; ++var2) {
                  var1 = this.listView.getChildAt(var2);
                  if (var1 instanceof TextSettingsCell) {
                     ((TextSettingsCell)var1).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText7"));
                     break;
                  }
               }

               if (SharedConfig.passcodeHash.length() == 0) {
                  var4 = false;
               }

               var5.setChecked(var4);
               NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetPasscode);
            } else {
               this.presentFragment(new PasscodeActivity(1));
            }
         } else if (var2 == this.autoLockRow) {
            if (this.getParentActivity() == null) {
               return;
            }

            AlertDialog.Builder var7 = new AlertDialog.Builder(this.getParentActivity());
            var7.setTitle(LocaleController.getString("AutoLock", 2131558778));
            NumberPicker var6 = new NumberPicker(this.getParentActivity());
            var6.setMinValue(0);
            var6.setMaxValue(4);
            var3 = SharedConfig.autoLockIn;
            if (var3 == 0) {
               var6.setValue(0);
            } else if (var3 == 60) {
               var6.setValue(1);
            } else if (var3 == 300) {
               var6.setValue(2);
            } else if (var3 == 3600) {
               var6.setValue(3);
            } else if (var3 == 18000) {
               var6.setValue(4);
            }

            var6.setFormatter(_$$Lambda$PasscodeActivity$e1eIIUXJcHN6dWhbpJbTrxba_60.INSTANCE);
            var7.setView(var6);
            var7.setNegativeButton(LocaleController.getString("Done", 2131559299), new _$$Lambda$PasscodeActivity$gpdG6ADQUdojRTN0OUtZLDO_4yI(this, var6, var2));
            this.showDialog(var7.create());
         } else if (var2 == this.fingerprintRow) {
            SharedConfig.useFingerprint ^= true;
            UserConfig.getInstance(super.currentAccount).saveConfig(false);
            ((TextCheckCell)var1).setChecked(SharedConfig.useFingerprint);
         } else if (var2 == this.captureRow) {
            SharedConfig.allowScreenCapture ^= true;
            UserConfig.getInstance(super.currentAccount).saveConfig(false);
            ((TextCheckCell)var1).setChecked(SharedConfig.allowScreenCapture);
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetPasscode);
            if (!SharedConfig.allowScreenCapture) {
               AlertsCreator.showSimpleAlert(this, LocaleController.getString("ScreenCaptureAlert", 2131560637));
            }
         }

      }
   }

   // $FF: synthetic method
   public void lambda$null$3$PasscodeActivity(NumberPicker var1, int var2, DialogInterface var3, int var4) {
      var4 = var1.getValue();
      if (var4 == 0) {
         SharedConfig.autoLockIn = 0;
      } else if (var4 == 1) {
         SharedConfig.autoLockIn = 60;
      } else if (var4 == 2) {
         SharedConfig.autoLockIn = 300;
      } else if (var4 == 3) {
         SharedConfig.autoLockIn = 3600;
      } else if (var4 == 4) {
         SharedConfig.autoLockIn = 18000;
      }

      this.listAdapter.notifyItemChanged(var2);
      UserConfig.getInstance(super.currentAccount).saveConfig(false);
   }

   // $FF: synthetic method
   public void lambda$onResume$5$PasscodeActivity() {
      EditTextBoldCursor var1 = this.passwordEditText;
      if (var1 != null) {
         var1.requestFocus();
         AndroidUtilities.showKeyboard(this.passwordEditText);
      }

   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      RecyclerListView var2 = this.listView;
      if (var2 != null) {
         var2.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            public boolean onPreDraw() {
               PasscodeActivity.this.listView.getViewTreeObserver().removeOnPreDrawListener(this);
               PasscodeActivity.this.fixLayoutInternal();
               return true;
            }
         });
      }

   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      this.updateRows();
      if (this.type == 0) {
         NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetPasscode);
      }

      return true;
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      if (this.type == 0) {
         NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetPasscode);
      }

   }

   public void onResume() {
      super.onResume();
      PasscodeActivity.ListAdapter var1 = this.listAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

      if (this.type != 0) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$PasscodeActivity$uOu47HjuX9AWXmApKYN2F1Z_SDQ(this), 200L);
      }

      this.fixLayoutInternal();
   }

   public void onTransitionAnimationEnd(boolean var1, boolean var2) {
      if (var1 && this.type != 0) {
         AndroidUtilities.showKeyboard(this.passwordEditText);
      }

   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;

      public ListAdapter(Context var2) {
         this.mContext = var2;
      }

      public int getItemCount() {
         return PasscodeActivity.this.rowCount;
      }

      public int getItemViewType(int var1) {
         if (var1 != PasscodeActivity.this.passcodeRow && var1 != PasscodeActivity.this.fingerprintRow && var1 != PasscodeActivity.this.captureRow) {
            if (var1 != PasscodeActivity.this.changePasscodeRow && var1 != PasscodeActivity.this.autoLockRow) {
               return var1 != PasscodeActivity.this.passcodeDetailRow && var1 != PasscodeActivity.this.autoLockDetailRow && var1 != PasscodeActivity.this.captureDetailRow ? 0 : 2;
            } else {
               return 1;
            }
         } else {
            return 0;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         int var2 = var1.getAdapterPosition();
         boolean var3;
         if (var2 == PasscodeActivity.this.passcodeRow || var2 == PasscodeActivity.this.fingerprintRow || var2 == PasscodeActivity.this.autoLockRow || var2 == PasscodeActivity.this.captureRow || SharedConfig.passcodeHash.length() != 0 && var2 == PasscodeActivity.this.changePasscodeRow) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         boolean var4 = false;
         if (var3 != 0) {
            if (var3 != 1) {
               if (var3 == 2) {
                  TextInfoPrivacyCell var6 = (TextInfoPrivacyCell)var1.itemView;
                  if (var2 == PasscodeActivity.this.passcodeDetailRow) {
                     var6.setText(LocaleController.getString("ChangePasscodeInfo", 2131558908));
                     if (PasscodeActivity.this.autoLockDetailRow != -1) {
                        var6.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                     } else {
                        var6.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                     }
                  } else if (var2 == PasscodeActivity.this.autoLockDetailRow) {
                     var6.setText(LocaleController.getString("AutoLockInfo", 2131558781));
                     var6.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                  } else if (var2 == PasscodeActivity.this.captureDetailRow) {
                     var6.setText(LocaleController.getString("ScreenCaptureInfo", 2131560638));
                     var6.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                  }
               }
            } else {
               TextSettingsCell var5 = (TextSettingsCell)var1.itemView;
               if (var2 == PasscodeActivity.this.changePasscodeRow) {
                  var5.setText(LocaleController.getString("ChangePasscode", 2131558907), false);
                  if (SharedConfig.passcodeHash.length() == 0) {
                     var5.setTag("windowBackgroundWhiteGrayText7");
                     var5.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText7"));
                  } else {
                     var5.setTag("windowBackgroundWhiteBlackText");
                     var5.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                  }
               } else if (var2 == PasscodeActivity.this.autoLockRow) {
                  var2 = SharedConfig.autoLockIn;
                  String var7;
                  if (var2 == 0) {
                     var7 = LocaleController.formatString("AutoLockDisabled", 2131558779);
                  } else if (var2 < 3600) {
                     var7 = LocaleController.formatString("AutoLockInTime", 2131558780, LocaleController.formatPluralString("Minutes", var2 / 60));
                  } else if (var2 < 86400) {
                     var7 = LocaleController.formatString("AutoLockInTime", 2131558780, LocaleController.formatPluralString("Hours", (int)Math.ceil((double)((float)var2 / 60.0F / 60.0F))));
                  } else {
                     var7 = LocaleController.formatString("AutoLockInTime", 2131558780, LocaleController.formatPluralString("Days", (int)Math.ceil((double)((float)var2 / 60.0F / 60.0F / 24.0F))));
                  }

                  var5.setTextAndValue(LocaleController.getString("AutoLock", 2131558778), var7, true);
                  var5.setTag("windowBackgroundWhiteBlackText");
                  var5.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
               }
            }
         } else {
            TextCheckCell var8 = (TextCheckCell)var1.itemView;
            if (var2 == PasscodeActivity.this.passcodeRow) {
               String var9 = LocaleController.getString("Passcode", 2131560160);
               if (SharedConfig.passcodeHash.length() > 0) {
                  var4 = true;
               }

               var8.setTextAndCheck(var9, var4, true);
            } else if (var2 == PasscodeActivity.this.fingerprintRow) {
               var8.setTextAndCheck(LocaleController.getString("UnlockFingerprint", 2131560938), SharedConfig.useFingerprint, true);
            } else if (var2 == PasscodeActivity.this.captureRow) {
               var8.setTextAndCheck(LocaleController.getString("ScreenCapture", 2131560636), SharedConfig.allowScreenCapture, false);
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            if (var2 != 1) {
               var3 = new TextInfoPrivacyCell(this.mContext);
            } else {
               var3 = new TextSettingsCell(this.mContext);
               ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
         } else {
            var3 = new TextCheckCell(this.mContext);
            ((TextCheckCell)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         }

         return new RecyclerListView.Holder((View)var3);
      }
   }
}
