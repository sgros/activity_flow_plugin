package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ActionMode.Callback;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.math.BigInteger;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SRPHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.EditTextSettingsCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.ContextProgressView;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class TwoStepVerificationActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   private static final int done_button = 1;
   private int abortPasswordRow;
   private TextView bottomButton;
   private TextView bottomTextView;
   private int changePasswordRow;
   private int changeRecoveryEmailRow;
   private boolean closeAfterSet;
   private EditTextSettingsCell codeFieldCell;
   private TLRPC.TL_account_password currentPassword;
   private byte[] currentPasswordHash = new byte[0];
   private byte[] currentSecret;
   private long currentSecretId;
   private boolean destroyed;
   private ActionBarMenuItem doneItem;
   private AnimatorSet doneItemAnimation;
   private String email;
   private int emailCodeLength = 6;
   private boolean emailOnly;
   private EmptyTextProgressView emptyView;
   private String firstPassword;
   private String hint;
   private TwoStepVerificationActivity.ListAdapter listAdapter;
   private RecyclerListView listView;
   private boolean loading;
   private int passwordCodeFieldRow;
   private EditTextBoldCursor passwordEditText;
   private int passwordEnabledDetailRow;
   private boolean passwordEntered = true;
   private int passwordSetState;
   private int passwordSetupDetailRow;
   private boolean paused;
   private AlertDialog progressDialog;
   private ContextProgressView progressView;
   private int resendCodeRow;
   private int rowCount;
   private ScrollView scrollView;
   private int setPasswordDetailRow;
   private int setPasswordRow;
   private int setRecoveryEmailRow;
   private int shadowRow;
   private Runnable shortPollRunnable;
   private TextView titleTextView;
   private int turnPasswordOffRow;
   private int type;
   private boolean waitingForEmail;

   public TwoStepVerificationActivity(int var1) {
      this.type = var1;
      if (var1 == 0) {
         this.loadPasswordInfo(false);
      }

   }

   public TwoStepVerificationActivity(int var1, int var2) {
      super.currentAccount = var1;
      this.type = var2;
      if (var2 == 0) {
         this.loadPasswordInfo(false);
      }

   }

   public static boolean canHandleCurrentPassword(TLRPC.TL_account_password var0, boolean var1) {
      if (var1) {
         if (var0.current_algo instanceof TLRPC.TL_passwordKdfAlgoUnknown) {
            return false;
         }
      } else if (var0.new_algo instanceof TLRPC.TL_passwordKdfAlgoUnknown || var0.current_algo instanceof TLRPC.TL_passwordKdfAlgoUnknown || var0.new_secure_algo instanceof TLRPC.TL_securePasswordKdfAlgoUnknown) {
         return false;
      }

      return true;
   }

   private boolean checkSecretValues(byte[] var1, TLRPC.TL_account_passwordSettings var2) {
      TLRPC.TL_secureSecretSettings var3 = var2.secure_settings;
      if (var3 != null) {
         this.currentSecret = var3.secure_secret;
         TLRPC.SecurePasswordKdfAlgo var9 = var3.secure_algo;
         byte[] var10;
         if (var9 instanceof TLRPC.TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000) {
            var1 = Utilities.computePBKDF2(var1, ((TLRPC.TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000)var9).salt);
         } else {
            if (!(var9 instanceof TLRPC.TL_securePasswordKdfAlgoSHA512)) {
               return false;
            }

            var10 = ((TLRPC.TL_securePasswordKdfAlgoSHA512)var9).salt;
            var1 = Utilities.computeSHA512(var10, var1, var10);
         }

         this.currentSecretId = var2.secure_settings.secure_secret_id;
         byte[] var4 = new byte[32];
         System.arraycopy(var1, 0, var4, 0, 32);
         var10 = new byte[16];
         System.arraycopy(var1, 32, var10, 0, 16);
         var1 = this.currentSecret;
         Utilities.aesCbcEncryptionByteArraySafe(var1, var4, var10, 0, var1.length, 0, 0);
         TLRPC.TL_secureSecretSettings var6 = var2.secure_settings;
         if (!PassportActivity.checkSecret(var6.secure_secret, var6.secure_secret_id)) {
            TLRPC.TL_account_updatePasswordSettings var8 = new TLRPC.TL_account_updatePasswordSettings();
            var8.password = this.getNewSrpPassword();
            var8.new_settings = new TLRPC.TL_account_passwordInputSettings();
            var8.new_settings.new_secure_settings = new TLRPC.TL_secureSecretSettings();
            TLRPC.TL_secureSecretSettings var5 = var8.new_settings.new_secure_settings;
            var5.secure_secret = new byte[0];
            var5.secure_algo = new TLRPC.TL_securePasswordKdfAlgoUnknown();
            TLRPC.TL_account_passwordInputSettings var7 = var8.new_settings;
            var7.new_secure_settings.secure_secret_id = 0L;
            var7.flags |= 4;
            ConnectionsManager.getInstance(super.currentAccount).sendRequest(var8, _$$Lambda$TwoStepVerificationActivity$0aZmkrG3QYsDjfpNDgl1nGy4PJM.INSTANCE);
            this.currentSecret = null;
            this.currentSecretId = 0L;
         }
      } else {
         this.currentSecret = null;
         this.currentSecretId = 0L;
      }

      return true;
   }

   private static byte[] getBigIntegerBytes(BigInteger var0) {
      byte[] var1 = var0.toByteArray();
      if (var1.length > 256) {
         byte[] var2 = new byte[256];
         System.arraycopy(var1, 1, var2, 0, 256);
         return var2;
      } else {
         return var1;
      }
   }

   private TLRPC.TL_inputCheckPasswordSRP getNewSrpPassword() {
      TLRPC.TL_account_password var1 = this.currentPassword;
      TLRPC.PasswordKdfAlgo var2 = var1.current_algo;
      if (var2 instanceof TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
         TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow var3 = (TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow)var2;
         return SRPHelper.startCheck(this.currentPasswordHash, var1.srp_id, var1.srp_B, var3);
      } else {
         return null;
      }
   }

   public static void initPasswordNewAlgo(TLRPC.TL_account_password var0) {
      TLRPC.PasswordKdfAlgo var1 = var0.new_algo;
      if (var1 instanceof TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
         TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow var6 = (TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow)var1;
         byte[] var2 = new byte[var6.salt1.length + 32];
         Utilities.random.nextBytes(var2);
         byte[] var3 = var6.salt1;
         System.arraycopy(var3, 0, var2, 0, var3.length);
         var6.salt1 = var2;
      }

      TLRPC.SecurePasswordKdfAlgo var4 = var0.new_secure_algo;
      if (var4 instanceof TLRPC.TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000) {
         TLRPC.TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000 var8 = (TLRPC.TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000)var4;
         byte[] var7 = new byte[var8.salt.length + 32];
         Utilities.random.nextBytes(var7);
         byte[] var5 = var8.salt;
         System.arraycopy(var5, 0, var7, 0, var5.length);
         var8.salt = var7;
      }

   }

   private boolean isValidEmail(String var1) {
      boolean var2 = false;
      boolean var3 = var2;
      if (var1 != null) {
         if (var1.length() < 3) {
            var3 = var2;
         } else {
            int var4 = var1.lastIndexOf(46);
            int var5 = var1.lastIndexOf(64);
            var3 = var2;
            if (var5 >= 0) {
               var3 = var2;
               if (var4 >= var5) {
                  var3 = true;
               }
            }
         }
      }

      return var3;
   }

   // $FF: synthetic method
   static void lambda$checkSecretValues$26(TLObject var0, TLRPC.TL_error var1) {
   }

   // $FF: synthetic method
   static void lambda$null$8(TLObject var0, TLRPC.TL_error var1) {
   }

   private void loadPasswordInfo(boolean var1) {
      if (!var1) {
         this.loading = true;
         TwoStepVerificationActivity.ListAdapter var2 = this.listAdapter;
         if (var2 != null) {
            var2.notifyDataSetChanged();
         }
      }

      TLRPC.TL_account_getPassword var3 = new TLRPC.TL_account_getPassword();
      ConnectionsManager.getInstance(super.currentAccount).sendRequest(var3, new _$$Lambda$TwoStepVerificationActivity$oYcvtMp3mI7Cv13Fi4uxabOCA00(this, var1), 10);
   }

   private void needHideProgress() {
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

   private void needShowProgress() {
      if (this.getParentActivity() != null && !this.getParentActivity().isFinishing() && this.progressDialog == null) {
         this.progressDialog = new AlertDialog(this.getParentActivity(), 3);
         this.progressDialog.setCanCacnel(false);
         this.progressDialog.show();
      }

   }

   private void onFieldError(TextView var1, boolean var2) {
      if (this.getParentActivity() != null) {
         Vibrator var3 = (Vibrator)this.getParentActivity().getSystemService("vibrator");
         if (var3 != null) {
            var3.vibrate(200L);
         }

         if (var2) {
            var1.setText("");
         }

         AndroidUtilities.shakeView(var1, 2.0F, 0);
      }
   }

   private void processDone() {
      int var1 = this.type;
      String var2;
      if (var1 == 0) {
         if (!this.passwordEntered) {
            var2 = this.passwordEditText.getText().toString();
            if (var2.length() == 0) {
               this.onFieldError(this.passwordEditText, false);
               return;
            }

            byte[] var6 = AndroidUtilities.getStringBytes(var2);
            this.needShowProgress();
            Utilities.globalQueue.postRunnable(new _$$Lambda$TwoStepVerificationActivity$q99ypmQww_xNEmbw14QjiBotShg(this, var6));
         } else if (this.waitingForEmail && this.currentPassword != null) {
            if (this.codeFieldCell.length() == 0) {
               this.onFieldError(this.codeFieldCell.getTextView(), false);
               return;
            }

            this.sendEmailConfirm(this.codeFieldCell.getText());
            this.showDoneProgress(true);
         }
      } else if (var1 == 1) {
         var1 = this.passwordSetState;
         if (var1 == 0) {
            if (this.passwordEditText.getText().length() == 0) {
               this.onFieldError(this.passwordEditText, false);
               return;
            }

            this.titleTextView.setText(LocaleController.getString("ReEnterYourPasscode", 2131560536));
            this.firstPassword = this.passwordEditText.getText().toString();
            this.setPasswordSetState(1);
         } else if (var1 == 1) {
            if (!this.firstPassword.equals(this.passwordEditText.getText().toString())) {
               try {
                  Toast.makeText(this.getParentActivity(), LocaleController.getString("PasswordDoNotMatch", 2131560346), 0).show();
               } catch (Exception var4) {
                  FileLog.e((Throwable)var4);
               }

               this.onFieldError(this.passwordEditText, true);
               return;
            }

            this.setPasswordSetState(2);
         } else if (var1 == 2) {
            this.hint = this.passwordEditText.getText().toString();
            if (this.hint.toLowerCase().equals(this.firstPassword.toLowerCase())) {
               try {
                  Toast.makeText(this.getParentActivity(), LocaleController.getString("PasswordAsHintError", 2131560344), 0).show();
               } catch (Exception var5) {
                  FileLog.e((Throwable)var5);
               }

               this.onFieldError(this.passwordEditText, false);
               return;
            }

            if (!this.currentPassword.has_recovery) {
               this.setPasswordSetState(3);
            } else {
               this.email = "";
               this.setNewPassword(false);
            }
         } else if (var1 == 3) {
            this.email = this.passwordEditText.getText().toString();
            if (!this.isValidEmail(this.email)) {
               this.onFieldError(this.passwordEditText, false);
               return;
            }

            this.setNewPassword(false);
         } else if (var1 == 4) {
            var2 = this.passwordEditText.getText().toString();
            if (var2.length() == 0) {
               this.onFieldError(this.passwordEditText, false);
               return;
            }

            TLRPC.TL_auth_recoverPassword var3 = new TLRPC.TL_auth_recoverPassword();
            var3.code = var2;
            ConnectionsManager.getInstance(super.currentAccount).sendRequest(var3, new _$$Lambda$TwoStepVerificationActivity$f9cuV_saSqd5BKXFxtWSGi8SzTE(this), 10);
         }
      }

   }

   private void sendEmailConfirm(String var1) {
      TLRPC.TL_account_confirmPasswordEmail var2 = new TLRPC.TL_account_confirmPasswordEmail();
      var2.code = var1;
      ConnectionsManager.getInstance(super.currentAccount).sendRequest(var2, new _$$Lambda$TwoStepVerificationActivity$zGkZlp2DnS104dUP2z3jhg8EgmM(this), 10);
   }

   private void setNewPassword(boolean var1) {
      if (var1 && this.waitingForEmail && this.currentPassword.has_password) {
         this.needShowProgress();
         TLRPC.TL_account_cancelPasswordEmail var5 = new TLRPC.TL_account_cancelPasswordEmail();
         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var5, new _$$Lambda$TwoStepVerificationActivity$XEU2eF294cmakN0jjZyU1ShYsjA(this));
      } else {
         String var2 = this.firstPassword;
         TLRPC.TL_account_updatePasswordSettings var3 = new TLRPC.TL_account_updatePasswordSettings();
         byte[] var4 = this.currentPasswordHash;
         if (var4 == null || var4.length == 0) {
            var3.password = new TLRPC.TL_inputCheckPasswordEmpty();
         }

         var3.new_settings = new TLRPC.TL_account_passwordInputSettings();
         TLRPC.TL_account_passwordInputSettings var6;
         if (var1) {
            UserConfig.getInstance(super.currentAccount).resetSavedPassword();
            this.currentSecret = null;
            if (this.waitingForEmail) {
               var6 = var3.new_settings;
               var6.flags = 2;
               var6.email = "";
               var3.password = new TLRPC.TL_inputCheckPasswordEmpty();
            } else {
               var6 = var3.new_settings;
               var6.flags = 3;
               var6.hint = "";
               var6.new_password_hash = new byte[0];
               var6.new_algo = new TLRPC.TL_passwordKdfAlgoUnknown();
               var3.new_settings.email = "";
            }
         } else {
            if (this.hint == null) {
               TLRPC.TL_account_password var7 = this.currentPassword;
               if (var7 != null) {
                  this.hint = var7.hint;
               }
            }

            if (this.hint == null) {
               this.hint = "";
            }

            if (var2 != null) {
               var6 = var3.new_settings;
               var6.flags |= 1;
               var6.hint = this.hint;
               var6.new_algo = this.currentPassword.new_algo;
            }

            if (this.email.length() > 0) {
               var6 = var3.new_settings;
               var6.flags |= 2;
               var6.email = this.email.trim();
            }
         }

         this.needShowProgress();
         Utilities.globalQueue.postRunnable(new _$$Lambda$TwoStepVerificationActivity$BbXpzRMc1z5d7yEqTJj2C6ReLvg(this, var3, var1, var2));
      }
   }

   private void setPasswordSetState(int var1) {
      if (this.passwordEditText != null) {
         this.passwordSetState = var1;
         int var2 = this.passwordSetState;
         byte var5 = 4;
         if (var2 == 0) {
            super.actionBar.setTitle(LocaleController.getString("YourPassword", 2131561155));
            if (this.currentPassword.has_password) {
               this.titleTextView.setText(LocaleController.getString("PleaseEnterPassword", 2131560457));
            } else {
               this.titleTextView.setText(LocaleController.getString("PleaseEnterFirstPassword", 2131560456));
            }

            this.passwordEditText.setImeOptions(5);
            this.passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            this.bottomTextView.setVisibility(4);
            this.bottomButton.setVisibility(4);
         } else if (var2 == 1) {
            super.actionBar.setTitle(LocaleController.getString("YourPassword", 2131561155));
            this.titleTextView.setText(LocaleController.getString("PleaseReEnterPassword", 2131560459));
            this.passwordEditText.setImeOptions(5);
            this.passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            this.bottomTextView.setVisibility(4);
            this.bottomButton.setVisibility(4);
         } else if (var2 == 2) {
            super.actionBar.setTitle(LocaleController.getString("PasswordHint", 2131560348));
            this.titleTextView.setText(LocaleController.getString("PasswordHintText", 2131560349));
            this.passwordEditText.setImeOptions(5);
            this.passwordEditText.setTransformationMethod((TransformationMethod)null);
            this.bottomTextView.setVisibility(4);
            this.bottomButton.setVisibility(4);
         } else if (var2 == 3) {
            super.actionBar.setTitle(LocaleController.getString("RecoveryEmail", 2131560549));
            this.titleTextView.setText(LocaleController.getString("YourEmail", 2131561144));
            this.passwordEditText.setImeOptions(5);
            this.passwordEditText.setTransformationMethod((TransformationMethod)null);
            this.passwordEditText.setInputType(33);
            this.bottomTextView.setVisibility(0);
            TextView var3 = this.bottomButton;
            if (!this.emailOnly) {
               var5 = 0;
            }

            var3.setVisibility(var5);
         } else if (var2 == 4) {
            super.actionBar.setTitle(LocaleController.getString("PasswordRecovery", 2131560350));
            this.titleTextView.setText(LocaleController.getString("PasswordCode", 2131560345));
            this.bottomTextView.setText(LocaleController.getString("RestoreEmailSentInfo", 2131560608));
            TextView var4 = this.bottomButton;
            String var6 = this.currentPassword.email_unconfirmed_pattern;
            if (var6 == null) {
               var6 = "";
            }

            var4.setText(LocaleController.formatString("RestoreEmailTrouble", 2131560609, var6));
            this.passwordEditText.setImeOptions(6);
            this.passwordEditText.setTransformationMethod((TransformationMethod)null);
            this.passwordEditText.setInputType(3);
            this.bottomTextView.setVisibility(0);
            this.bottomButton.setVisibility(0);
         }

         this.passwordEditText.setText("");
      }
   }

   private void showAlertWithText(String var1, String var2) {
      AlertDialog.Builder var3 = new AlertDialog.Builder(this.getParentActivity());
      var3.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
      var3.setTitle(var1);
      var3.setMessage(var2);
      this.showDialog(var3.create());
   }

   private void showDoneProgress(final boolean var1) {
      AnimatorSet var2 = this.doneItemAnimation;
      if (var2 != null) {
         var2.cancel();
      }

      this.doneItemAnimation = new AnimatorSet();
      if (var1) {
         this.progressView.setVisibility(0);
         this.doneItem.setEnabled(false);
         this.doneItemAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.doneItem.getImageView(), "scaleX", new float[]{0.1F}), ObjectAnimator.ofFloat(this.doneItem.getImageView(), "scaleY", new float[]{0.1F}), ObjectAnimator.ofFloat(this.doneItem.getImageView(), "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(this.progressView, "scaleX", new float[]{1.0F}), ObjectAnimator.ofFloat(this.progressView, "scaleY", new float[]{1.0F}), ObjectAnimator.ofFloat(this.progressView, "alpha", new float[]{1.0F})});
      } else {
         this.doneItem.getImageView().setVisibility(0);
         this.doneItem.setEnabled(true);
         this.doneItemAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.progressView, "scaleX", new float[]{0.1F}), ObjectAnimator.ofFloat(this.progressView, "scaleY", new float[]{0.1F}), ObjectAnimator.ofFloat(this.progressView, "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(this.doneItem.getImageView(), "scaleX", new float[]{1.0F}), ObjectAnimator.ofFloat(this.doneItem.getImageView(), "scaleY", new float[]{1.0F}), ObjectAnimator.ofFloat(this.doneItem.getImageView(), "alpha", new float[]{1.0F})});
      }

      this.doneItemAnimation.addListener(new AnimatorListenerAdapter() {
         public void onAnimationCancel(Animator var1x) {
            if (TwoStepVerificationActivity.this.doneItemAnimation != null && TwoStepVerificationActivity.this.doneItemAnimation.equals(var1x)) {
               TwoStepVerificationActivity.this.doneItemAnimation = null;
            }

         }

         public void onAnimationEnd(Animator var1x) {
            if (TwoStepVerificationActivity.this.doneItemAnimation != null && TwoStepVerificationActivity.this.doneItemAnimation.equals(var1x)) {
               if (!var1) {
                  TwoStepVerificationActivity.this.progressView.setVisibility(4);
               } else {
                  TwoStepVerificationActivity.this.doneItem.getImageView().setVisibility(4);
               }
            }

         }
      });
      this.doneItemAnimation.setDuration(150L);
      this.doneItemAnimation.start();
   }

   private void startShortpoll() {
      Runnable var1 = this.shortPollRunnable;
      if (var1 != null) {
         AndroidUtilities.cancelRunOnUIThread(var1);
      }

      this.shortPollRunnable = new _$$Lambda$TwoStepVerificationActivity$wO_OLA1D7DR12e78ifRLekXVYec(this);
      AndroidUtilities.runOnUIThread(this.shortPollRunnable, 5000L);
   }

   private void updateRows() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.setPasswordRow);
      var1.append(this.setPasswordDetailRow);
      var1.append(this.changePasswordRow);
      var1.append(this.turnPasswordOffRow);
      var1.append(this.setRecoveryEmailRow);
      var1.append(this.changeRecoveryEmailRow);
      var1.append(this.resendCodeRow);
      var1.append(this.abortPasswordRow);
      var1.append(this.passwordSetupDetailRow);
      var1.append(this.passwordCodeFieldRow);
      var1.append(this.passwordEnabledDetailRow);
      var1.append(this.shadowRow);
      var1.append(this.rowCount);
      boolean var2;
      if (this.passwordCodeFieldRow != -1) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.rowCount = 0;
      this.setPasswordRow = -1;
      this.setPasswordDetailRow = -1;
      this.changePasswordRow = -1;
      this.turnPasswordOffRow = -1;
      this.setRecoveryEmailRow = -1;
      this.changeRecoveryEmailRow = -1;
      this.abortPasswordRow = -1;
      this.resendCodeRow = -1;
      this.passwordSetupDetailRow = -1;
      this.passwordCodeFieldRow = -1;
      this.passwordEnabledDetailRow = -1;
      this.shadowRow = -1;
      if (!this.loading) {
         TLRPC.TL_account_password var3 = this.currentPassword;
         if (var3 != null) {
            int var4;
            if (this.waitingForEmail) {
               var4 = this.rowCount++;
               this.passwordCodeFieldRow = var4;
               var4 = this.rowCount++;
               this.passwordSetupDetailRow = var4;
               var4 = this.rowCount++;
               this.resendCodeRow = var4;
               var4 = this.rowCount++;
               this.abortPasswordRow = var4;
               var4 = this.rowCount++;
               this.shadowRow = var4;
            } else if (var3.has_password) {
               var4 = this.rowCount++;
               this.changePasswordRow = var4;
               var4 = this.rowCount++;
               this.turnPasswordOffRow = var4;
               if (var3.has_recovery) {
                  var4 = this.rowCount++;
                  this.changeRecoveryEmailRow = var4;
               } else {
                  var4 = this.rowCount++;
                  this.setRecoveryEmailRow = var4;
               }

               var4 = this.rowCount++;
               this.passwordEnabledDetailRow = var4;
            } else {
               var4 = this.rowCount++;
               this.setPasswordRow = var4;
               var4 = this.rowCount++;
               this.setPasswordDetailRow = var4;
            }
         }
      }

      StringBuilder var6 = new StringBuilder();
      var6.append(this.setPasswordRow);
      var6.append(this.setPasswordDetailRow);
      var6.append(this.changePasswordRow);
      var6.append(this.turnPasswordOffRow);
      var6.append(this.setRecoveryEmailRow);
      var6.append(this.changeRecoveryEmailRow);
      var6.append(this.resendCodeRow);
      var6.append(this.abortPasswordRow);
      var6.append(this.passwordSetupDetailRow);
      var6.append(this.passwordCodeFieldRow);
      var6.append(this.passwordEnabledDetailRow);
      var6.append(this.shadowRow);
      var6.append(this.rowCount);
      if (this.listAdapter != null && !var1.toString().equals(var6.toString())) {
         this.listAdapter.notifyDataSetChanged();
         if (this.passwordCodeFieldRow == -1 && this.getParentActivity() != null && var2) {
            AndroidUtilities.hideKeyboard(this.getParentActivity().getCurrentFocus());
            this.codeFieldCell.setText("", false);
         }
      }

      if (super.fragmentView != null) {
         RecyclerListView var5;
         if (!this.loading && !this.passwordEntered) {
            var5 = this.listView;
            if (var5 != null) {
               var5.setEmptyView((View)null);
               this.listView.setVisibility(4);
               this.scrollView.setVisibility(0);
               this.emptyView.setVisibility(4);
            }

            if (this.passwordEditText != null) {
               this.doneItem.setVisibility(0);
               this.passwordEditText.setVisibility(0);
               super.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
               super.fragmentView.setTag("windowBackgroundWhite");
               this.titleTextView.setVisibility(0);
               this.bottomButton.setVisibility(0);
               this.bottomTextView.setVisibility(4);
               this.bottomButton.setText(LocaleController.getString("ForgotPassword", 2131559503));
               if (!TextUtils.isEmpty(this.currentPassword.hint)) {
                  this.passwordEditText.setHint(this.currentPassword.hint);
               } else {
                  this.passwordEditText.setHint("");
               }

               AndroidUtilities.runOnUIThread(new _$$Lambda$TwoStepVerificationActivity$9VaVKy9W3pbWvJiDnN2bOVx7AfA(this), 200L);
            }
         } else {
            var5 = this.listView;
            if (var5 != null) {
               var5.setVisibility(0);
               this.scrollView.setVisibility(4);
               this.listView.setEmptyView(this.emptyView);
            }

            if (this.waitingForEmail && this.currentPassword != null) {
               this.doneItem.setVisibility(0);
            } else if (this.passwordEditText != null) {
               this.doneItem.setVisibility(8);
               this.passwordEditText.setVisibility(4);
               this.titleTextView.setVisibility(4);
               this.bottomTextView.setVisibility(4);
               this.bottomButton.setVisibility(4);
            }

            super.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
            super.fragmentView.setTag("windowBackgroundGray");
         }
      }

   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(false);
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               TwoStepVerificationActivity.this.finishFragment();
            } else if (var1 == 1) {
               TwoStepVerificationActivity.this.processDone();
            }

         }
      });
      super.fragmentView = new FrameLayout(var1);
      FrameLayout var2 = (FrameLayout)super.fragmentView;
      var2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
      this.doneItem = super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0F));
      this.progressView = new ContextProgressView(var1, 1);
      this.progressView.setAlpha(0.0F);
      this.progressView.setScaleX(0.1F);
      this.progressView.setScaleY(0.1F);
      this.progressView.setVisibility(4);
      this.doneItem.addView(this.progressView, LayoutHelper.createFrame(-1, -1.0F));
      this.scrollView = new ScrollView(var1);
      this.scrollView.setFillViewport(true);
      var2.addView(this.scrollView, LayoutHelper.createFrame(-1, -1.0F));
      LinearLayout var3 = new LinearLayout(var1);
      var3.setOrientation(1);
      this.scrollView.addView(var3, LayoutHelper.createScroll(-1, -2, 51));
      this.titleTextView = new TextView(var1);
      this.titleTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
      this.titleTextView.setTextSize(1, 18.0F);
      this.titleTextView.setGravity(1);
      var3.addView(this.titleTextView, LayoutHelper.createLinear(-2, -2, 1, 0, 38, 0, 0));
      this.passwordEditText = new EditTextBoldCursor(var1);
      this.passwordEditText.setTextSize(1, 20.0F);
      this.passwordEditText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.passwordEditText.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
      this.passwordEditText.setBackgroundDrawable(Theme.createEditTextDrawable(var1, false));
      this.passwordEditText.setMaxLines(1);
      this.passwordEditText.setLines(1);
      this.passwordEditText.setGravity(1);
      this.passwordEditText.setSingleLine(true);
      this.passwordEditText.setInputType(129);
      this.passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
      this.passwordEditText.setTypeface(Typeface.DEFAULT);
      this.passwordEditText.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.passwordEditText.setCursorSize(AndroidUtilities.dp(20.0F));
      this.passwordEditText.setCursorWidth(1.5F);
      var3.addView(this.passwordEditText, LayoutHelper.createLinear(-1, 36, 51, 40, 32, 40, 0));
      this.passwordEditText.setOnEditorActionListener(new _$$Lambda$TwoStepVerificationActivity$YWYhM6syo7BDAVMlJEwwl5l8lR0(this));
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
      this.bottomTextView = new TextView(var1);
      this.bottomTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
      this.bottomTextView.setTextSize(1, 14.0F);
      TextView var4 = this.bottomTextView;
      boolean var5 = LocaleController.isRTL;
      byte var6 = 5;
      byte var7;
      if (var5) {
         var7 = 5;
      } else {
         var7 = 3;
      }

      var4.setGravity(var7 | 48);
      this.bottomTextView.setText(LocaleController.getString("YourEmailInfo", 2131561149));
      var4 = this.bottomTextView;
      if (LocaleController.isRTL) {
         var7 = 5;
      } else {
         var7 = 3;
      }

      var3.addView(var4, LayoutHelper.createLinear(-2, -2, var7 | 48, 40, 30, 40, 0));
      LinearLayout var12 = new LinearLayout(var1);
      var12.setGravity(80);
      var3.addView(var12, LayoutHelper.createLinear(-1, -1));
      this.bottomButton = new TextView(var1);
      this.bottomButton.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
      this.bottomButton.setTextSize(1, 14.0F);
      TextView var10 = this.bottomButton;
      if (LocaleController.isRTL) {
         var7 = 5;
      } else {
         var7 = 3;
      }

      var10.setGravity(var7 | 80);
      this.bottomButton.setText(LocaleController.getString("YourEmailSkip", 2131561150));
      this.bottomButton.setPadding(0, AndroidUtilities.dp(10.0F), 0, 0);
      var10 = this.bottomButton;
      if (LocaleController.isRTL) {
         var7 = var6;
      } else {
         var7 = 3;
      }

      var12.addView(var10, LayoutHelper.createLinear(-1, -2, var7 | 80, 40, 0, 40, 14));
      this.bottomButton.setOnClickListener(new _$$Lambda$TwoStepVerificationActivity$hA8CuA2mAlwBUP9M3CP0dvU1Oc0(this));
      int var13 = this.type;
      if (var13 == 0) {
         this.emptyView = new EmptyTextProgressView(var1);
         this.emptyView.showProgress();
         var2.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0F));
         this.listView = new RecyclerListView(var1);
         this.listView.setLayoutManager(new LinearLayoutManager(var1, 1, false));
         this.listView.setEmptyView(this.emptyView);
         this.listView.setVerticalScrollBarEnabled(false);
         var2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F));
         RecyclerListView var11 = this.listView;
         TwoStepVerificationActivity.ListAdapter var9 = new TwoStepVerificationActivity.ListAdapter(var1);
         this.listAdapter = var9;
         var11.setAdapter(var9);
         this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$TwoStepVerificationActivity$R271ECy978pWdDX5q4Leb46bpF0(this)));
         this.codeFieldCell = new EditTextSettingsCell(var1);
         this.codeFieldCell.setTextAndHint("", LocaleController.getString("PasswordCode", 2131560345), false);
         this.codeFieldCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         EditTextBoldCursor var8 = this.codeFieldCell.getTextView();
         var8.setInputType(3);
         var8.setImeOptions(6);
         var8.setOnEditorActionListener(new _$$Lambda$TwoStepVerificationActivity$Nl_K_LAtgIdu6AfA5V1bgHfleb8(this));
         var8.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable var1) {
               if (TwoStepVerificationActivity.this.emailCodeLength != 0 && var1.length() == TwoStepVerificationActivity.this.emailCodeLength) {
                  TwoStepVerificationActivity.this.processDone();
               }

            }

            public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
            }

            public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
            }
         });
         this.updateRows();
         super.actionBar.setTitle(LocaleController.getString("TwoStepVerificationTitle", 2131560920));
         this.titleTextView.setText(LocaleController.getString("PleaseEnterCurrentPassword", 2131560455));
      } else if (var13 == 1) {
         this.setPasswordSetState(this.passwordSetState);
      }

      if (this.passwordEntered && this.type != 1) {
         super.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
         super.fragmentView.setTag("windowBackgroundGray");
      } else {
         super.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         super.fragmentView.setTag("windowBackgroundWhite");
      }

      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.didSetTwoStepPassword) {
         if (var3 != null && var3.length > 0 && var3[0] != null) {
            this.currentPasswordHash = (byte[])var3[0];
            if (this.closeAfterSet && TextUtils.isEmpty((String)var3[4]) && this.closeAfterSet) {
               this.removeSelfFromStack();
            }
         }

         this.loadPasswordInfo(false);
         this.updateRows();
      }

   }

   public ThemeDescription[] getThemeDescriptions() {
      ThemeDescription var1 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, EditTextSettingsCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var2 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      View var3 = super.fragmentView;
      int var4 = ThemeDescription.FLAG_BACKGROUND;
      ThemeDescription var5 = new ThemeDescription(var3, ThemeDescription.FLAG_CHECKTAG | var4, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var7 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var8 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var9 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var10 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var13 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var11 = this.listView;
      Paint var12 = Theme.dividerPaint;
      return new ThemeDescription[]{var1, var2, var5, var6, var7, var8, var9, var10, var13, new ThemeDescription(var11, 0, new Class[]{View.class}, var12, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"), new ThemeDescription(this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "progressCircle"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteRedText3"), new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{EditTextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, ThemeDescription.FLAG_HINTTEXTCOLOR, new Class[]{EditTextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4"), new ThemeDescription(this.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"), new ThemeDescription(this.bottomTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"), new ThemeDescription(this.bottomButton, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText4"), new ThemeDescription(this.passwordEditText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.passwordEditText, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"), new ThemeDescription(this.passwordEditText, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputField"), new ThemeDescription(this.passwordEditText, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated")};
   }

   // $FF: synthetic method
   public boolean lambda$createView$0$TwoStepVerificationActivity(TextView var1, int var2, KeyEvent var3) {
      if (var2 != 5 && var2 != 6) {
         return false;
      } else {
         this.processDone();
         return true;
      }
   }

   // $FF: synthetic method
   public boolean lambda$createView$10$TwoStepVerificationActivity(TextView var1, int var2, KeyEvent var3) {
      if (var2 == 6) {
         this.processDone();
         return true;
      } else {
         return false;
      }
   }

   // $FF: synthetic method
   public void lambda$createView$6$TwoStepVerificationActivity(View var1) {
      AlertDialog.Builder var3;
      if (this.type == 0) {
         if (this.currentPassword.has_recovery) {
            this.needShowProgress();
            TLRPC.TL_auth_requestPasswordRecovery var2 = new TLRPC.TL_auth_requestPasswordRecovery();
            ConnectionsManager.getInstance(super.currentAccount).sendRequest(var2, new _$$Lambda$TwoStepVerificationActivity$rnLkpi4dhDuCJJJXKpOcjSAJ1bE(this), 10);
         } else {
            if (this.getParentActivity() == null) {
               return;
            }

            var3 = new AlertDialog.Builder(this.getParentActivity());
            var3.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
            var3.setNegativeButton(LocaleController.getString("RestorePasswordResetAccount", 2131560613), new _$$Lambda$TwoStepVerificationActivity$Lh9jzeSQrwQKDB5QSE89Hn8p2n8(this));
            var3.setTitle(LocaleController.getString("RestorePasswordNoEmailTitle", 2131560612));
            var3.setMessage(LocaleController.getString("RestorePasswordNoEmailText", 2131560611));
            this.showDialog(var3.create());
         }
      } else if (this.passwordSetState == 4) {
         this.showAlertWithText(LocaleController.getString("RestorePasswordNoEmailTitle", 2131560612), LocaleController.getString("RestoreEmailTroubleText", 2131560610));
      } else {
         var3 = new AlertDialog.Builder(this.getParentActivity());
         var3.setMessage(LocaleController.getString("YourEmailSkipWarningText", 2131561152));
         var3.setTitle(LocaleController.getString("YourEmailSkipWarning", 2131561151));
         var3.setPositiveButton(LocaleController.getString("YourEmailSkip", 2131561150), new _$$Lambda$TwoStepVerificationActivity$Qtb1CP1a_ctZOdCDJuV26Qenpkg(this));
         var3.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
         this.showDialog(var3.create());
      }

   }

   // $FF: synthetic method
   public void lambda$createView$9$TwoStepVerificationActivity(View var1, int var2) {
      TwoStepVerificationActivity var5;
      if (var2 != this.setPasswordRow && var2 != this.changePasswordRow) {
         if (var2 != this.setRecoveryEmailRow && var2 != this.changeRecoveryEmailRow) {
            if (var2 != this.turnPasswordOffRow && var2 != this.abortPasswordRow) {
               if (var2 == this.resendCodeRow) {
                  TLRPC.TL_account_resendPasswordEmail var9 = new TLRPC.TL_account_resendPasswordEmail();
                  ConnectionsManager.getInstance(super.currentAccount).sendRequest(var9, _$$Lambda$TwoStepVerificationActivity$4F6YJgV3MBVhnoAYE2KFt09BlNg.INSTANCE);
                  AlertDialog.Builder var10 = new AlertDialog.Builder(this.getParentActivity());
                  var10.setMessage(LocaleController.getString("ResendCodeInfo", 2131560582));
                  var10.setTitle(LocaleController.getString("AppName", 2131558635));
                  var10.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
                  this.showDialog(var10.create());
               }
            } else {
               AlertDialog.Builder var3 = new AlertDialog.Builder(this.getParentActivity());
               String var6;
               if (var2 == this.abortPasswordRow) {
                  TLRPC.TL_account_password var8 = this.currentPassword;
                  if (var8 != null && var8.has_password) {
                     var6 = LocaleController.getString("CancelEmailQuestion", 2131558894);
                  } else {
                     var6 = LocaleController.getString("CancelPasswordQuestion", 2131558897);
                  }
               } else {
                  String var4 = LocaleController.getString("TurnPasswordOffQuestion", 2131560918);
                  var6 = var4;
                  if (this.currentPassword.has_secure_values) {
                     StringBuilder var7 = new StringBuilder();
                     var7.append(var4);
                     var7.append("\n\n");
                     var7.append(LocaleController.getString("TurnPasswordOffPassport", 2131560917));
                     var6 = var7.toString();
                  }
               }

               var3.setMessage(var6);
               var3.setTitle(LocaleController.getString("AppName", 2131558635));
               var3.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$TwoStepVerificationActivity$10etTYa5XWjUNmwZWDDim1g1lNY(this));
               var3.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
               this.showDialog(var3.create());
            }
         } else {
            var5 = new TwoStepVerificationActivity(super.currentAccount, 1);
            var5.currentPasswordHash = this.currentPasswordHash;
            var5.currentPassword = this.currentPassword;
            var5.currentSecretId = this.currentSecretId;
            var5.currentSecret = this.currentSecret;
            var5.emailOnly = true;
            var5.passwordSetState = 3;
            this.presentFragment(var5);
         }
      } else {
         var5 = new TwoStepVerificationActivity(super.currentAccount, 1);
         var5.currentPasswordHash = this.currentPasswordHash;
         var5.currentPassword = this.currentPassword;
         var5.currentSecretId = this.currentSecretId;
         var5.currentSecret = this.currentSecret;
         this.presentFragment(var5);
      }

   }

   // $FF: synthetic method
   public void lambda$loadPasswordInfo$14$TwoStepVerificationActivity(boolean var1, TLObject var2, TLRPC.TL_error var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$TwoStepVerificationActivity$4P2nkSO_oWh_p7A_7V0YWykRkO0(this, var3, var2, var1));
   }

   // $FF: synthetic method
   public void lambda$null$1$TwoStepVerificationActivity(TLRPC.TL_auth_passwordRecovery var1, DialogInterface var2, int var3) {
      TwoStepVerificationActivity var4 = new TwoStepVerificationActivity(super.currentAccount, 1);
      var4.currentPassword = this.currentPassword;
      var4.currentPassword.email_unconfirmed_pattern = var1.email_pattern;
      var4.currentSecretId = this.currentSecretId;
      var4.currentSecret = this.currentSecret;
      var4.passwordSetState = 4;
      this.presentFragment(var4);
   }

   // $FF: synthetic method
   public void lambda$null$13$TwoStepVerificationActivity(TLRPC.TL_error var1, TLObject var2, boolean var3) {
      TLRPC.TL_account_password var8;
      if (var1 == null) {
         this.loading = false;
         this.currentPassword = (TLRPC.TL_account_password)var2;
         if (!canHandleCurrentPassword(this.currentPassword, false)) {
            AlertsCreator.showUpdateAppAlert(this.getParentActivity(), LocaleController.getString("UpdateAppAlert", 2131560951), true);
            return;
         }

         if (!var3) {
            byte[] var7 = this.currentPasswordHash;
            if ((var7 == null || var7.length <= 0) && this.currentPassword.has_password) {
               var3 = false;
            } else {
               var3 = true;
            }

            this.passwordEntered = var3;
         }

         this.waitingForEmail = TextUtils.isEmpty(this.currentPassword.email_unconfirmed_pattern) ^ true;
         initPasswordNewAlgo(this.currentPassword);
         if (!this.paused && this.closeAfterSet) {
            var8 = this.currentPassword;
            if (var8.has_password) {
               TLRPC.PasswordKdfAlgo var4 = var8.current_algo;
               TLRPC.SecurePasswordKdfAlgo var5 = var8.new_secure_algo;
               byte[] var6 = var8.secure_random;
               String var9;
               if (var8.has_recovery) {
                  var9 = "1";
               } else {
                  var9 = null;
               }

               String var10 = this.currentPassword.hint;
               if (var10 == null) {
                  var10 = "";
               }

               if (!this.waitingForEmail && var4 != null) {
                  NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.didSetTwoStepPassword);
                  NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.didSetTwoStepPassword, null, var4, var5, var6, var9, var10, null, null);
                  this.finishFragment();
               }
            }
         }
      }

      if (this.type == 0 && !this.destroyed && this.shortPollRunnable == null) {
         var8 = this.currentPassword;
         if (var8 != null && !TextUtils.isEmpty(var8.email_unconfirmed_pattern)) {
            this.startShortpoll();
         }
      }

      this.updateRows();
   }

   // $FF: synthetic method
   public void lambda$null$17$TwoStepVerificationActivity(TLRPC.TL_error var1) {
      this.needHideProgress();
      if (var1 == null) {
         this.loadPasswordInfo(false);
         NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.didRemoveTwoStepPassword);
         this.updateRows();
      }

   }

   // $FF: synthetic method
   public void lambda$null$19$TwoStepVerificationActivity(TLRPC.TL_error var1, TLObject var2, boolean var3) {
      if (var1 == null) {
         this.currentPassword = (TLRPC.TL_account_password)var2;
         initPasswordNewAlgo(this.currentPassword);
         this.setNewPassword(var3);
      }

   }

   // $FF: synthetic method
   public void lambda$null$2$TwoStepVerificationActivity(TLRPC.TL_error var1, TLObject var2) {
      this.needHideProgress();
      if (var1 == null) {
         TLRPC.TL_auth_passwordRecovery var4 = (TLRPC.TL_auth_passwordRecovery)var2;
         AlertDialog.Builder var7 = new AlertDialog.Builder(this.getParentActivity());
         var7.setMessage(LocaleController.formatString("RestoreEmailSent", 2131560607, var4.email_pattern));
         var7.setTitle(LocaleController.getString("AppName", 2131558635));
         var7.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$TwoStepVerificationActivity$o3LczIT3DtDztCPkC_aGamfNhzE(this, var4));
         Dialog var5 = this.showDialog(var7.create());
         if (var5 != null) {
            var5.setCanceledOnTouchOutside(false);
            var5.setCancelable(false);
         }
      } else if (var1.text.startsWith("FLOOD_WAIT")) {
         int var3 = Utilities.parseInt(var1.text);
         String var6;
         if (var3 < 60) {
            var6 = LocaleController.formatPluralString("Seconds", var3);
         } else {
            var6 = LocaleController.formatPluralString("Minutes", var3 / 60);
         }

         this.showAlertWithText(LocaleController.getString("AppName", 2131558635), LocaleController.formatString("FloodWaitTime", 2131559496, var6));
      } else {
         this.showAlertWithText(LocaleController.getString("AppName", 2131558635), var1.text);
      }

   }

   // $FF: synthetic method
   public void lambda$null$20$TwoStepVerificationActivity(boolean var1, TLObject var2, TLRPC.TL_error var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$TwoStepVerificationActivity$rcQJqDBv63lm3h19T_uWVt6J7mA(this, var3, var2, var1));
   }

   // $FF: synthetic method
   public void lambda$null$21$TwoStepVerificationActivity(byte[] var1, TLRPC.TL_account_updatePasswordSettings var2, DialogInterface var3, int var4) {
      NotificationCenter var7 = NotificationCenter.getInstance(super.currentAccount);
      var4 = NotificationCenter.didSetTwoStepPassword;
      TLRPC.PasswordKdfAlgo var6 = var2.new_settings.new_algo;
      TLRPC.TL_account_password var5 = this.currentPassword;
      var7.postNotificationName(var4, var1, var6, var5.new_secure_algo, var5.secure_random, this.email, this.hint, null, this.firstPassword);
      this.finishFragment();
   }

   // $FF: synthetic method
   public void lambda$null$22$TwoStepVerificationActivity(byte[] var1, TLRPC.TL_account_updatePasswordSettings var2, DialogInterface var3, int var4) {
      if (this.closeAfterSet) {
         TwoStepVerificationActivity var9 = new TwoStepVerificationActivity(super.currentAccount, 0);
         var9.setCloseAfterSet(true);
         ActionBarLayout var5 = super.parentLayout;
         var5.addFragmentToStack(var9, var5.fragmentsStack.size() - 1);
      }

      NotificationCenter var10 = NotificationCenter.getInstance(super.currentAccount);
      var4 = NotificationCenter.didSetTwoStepPassword;
      TLRPC.PasswordKdfAlgo var11 = var2.new_settings.new_algo;
      TLRPC.TL_account_password var6 = this.currentPassword;
      TLRPC.SecurePasswordKdfAlgo var8 = var6.new_secure_algo;
      byte[] var12 = var6.secure_random;
      String var7 = this.email;
      var10.postNotificationName(var4, var1, var11, var8, var12, var7, this.hint, var7, this.firstPassword);
      this.finishFragment();
   }

   // $FF: synthetic method
   public void lambda$null$23$TwoStepVerificationActivity(TLRPC.TL_error var1, boolean var2, TLObject var3, byte[] var4, TLRPC.TL_account_updatePasswordSettings var5, String var6) {
      if (var1 != null && "SRP_ID_INVALID".equals(var1.text)) {
         TLRPC.TL_account_getPassword var12 = new TLRPC.TL_account_getPassword();
         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var12, new _$$Lambda$TwoStepVerificationActivity$juf_ayzPNKWkx2ZD2C5VwnmYfxI(this, var2), 8);
      } else {
         this.needHideProgress();
         AlertDialog.Builder var8;
         Dialog var9;
         if (var1 == null && var3 instanceof TLRPC.TL_boolTrue) {
            if (var2) {
               this.currentPassword = null;
               this.currentPasswordHash = new byte[0];
               this.loadPasswordInfo(false);
               NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.didRemoveTwoStepPassword);
               this.updateRows();
            } else {
               if (this.getParentActivity() == null) {
                  return;
               }

               label48: {
                  var8 = new AlertDialog.Builder(this.getParentActivity());
                  var8.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$TwoStepVerificationActivity$6t0ApRpo2RwhN_WZ8Os0Xk1Kb5c(this, var4, var5));
                  if (var6 == null) {
                     TLRPC.TL_account_password var11 = this.currentPassword;
                     if (var11 != null && var11.has_password) {
                        var8.setMessage(LocaleController.getString("YourEmailSuccessText", 2131561153));
                        break label48;
                     }
                  }

                  var8.setMessage(LocaleController.getString("YourPasswordSuccessText", 2131561157));
               }

               var8.setTitle(LocaleController.getString("YourPasswordSuccess", 2131561156));
               var9 = this.showDialog(var8.create());
               if (var9 != null) {
                  var9.setCanceledOnTouchOutside(false);
                  var9.setCancelable(false);
               }
            }
         } else if (var1 != null) {
            if (!"EMAIL_UNCONFIRMED".equals(var1.text) && !var1.text.startsWith("EMAIL_UNCONFIRMED_")) {
               if ("EMAIL_INVALID".equals(var1.text)) {
                  this.showAlertWithText(LocaleController.getString("AppName", 2131558635), LocaleController.getString("PasswordEmailInvalid", 2131560347));
               } else if (var1.text.startsWith("FLOOD_WAIT")) {
                  int var7 = Utilities.parseInt(var1.text);
                  String var10;
                  if (var7 < 60) {
                     var10 = LocaleController.formatPluralString("Seconds", var7);
                  } else {
                     var10 = LocaleController.formatPluralString("Minutes", var7 / 60);
                  }

                  this.showAlertWithText(LocaleController.getString("AppName", 2131558635), LocaleController.formatString("FloodWaitTime", 2131559496, var10));
               } else {
                  this.showAlertWithText(LocaleController.getString("AppName", 2131558635), var1.text);
               }
            } else {
               NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.didSetTwoStepPassword);
               var8 = new AlertDialog.Builder(this.getParentActivity());
               var8.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$TwoStepVerificationActivity$dZl6L7A3pdg7u0zh2dz9t60mIJU(this, var4, var5));
               var8.setMessage(LocaleController.getString("YourEmailAlmostThereText", 2131561146));
               var8.setTitle(LocaleController.getString("YourEmailAlmostThere", 2131561145));
               var9 = this.showDialog(var8.create());
               if (var9 != null) {
                  var9.setCanceledOnTouchOutside(false);
                  var9.setCancelable(false);
               }
            }
         }

      }
   }

   // $FF: synthetic method
   public void lambda$null$24$TwoStepVerificationActivity(boolean var1, byte[] var2, TLRPC.TL_account_updatePasswordSettings var3, String var4, TLObject var5, TLRPC.TL_error var6) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$TwoStepVerificationActivity$tWVKNMhPWigeKF9TDwsCxxYPaU0(this, var6, var1, var5, var2, var3, var4));
   }

   // $FF: synthetic method
   public void lambda$null$27$TwoStepVerificationActivity(boolean var1, byte[] var2) {
      this.needHideProgress();
      if (var1) {
         this.currentPasswordHash = var2;
         this.passwordEntered = true;
         AndroidUtilities.hideKeyboard(this.passwordEditText);
         this.updateRows();
      } else {
         AlertsCreator.showUpdateAppAlert(this.getParentActivity(), LocaleController.getString("UpdateAppAlert", 2131560951), true);
      }

   }

   // $FF: synthetic method
   public void lambda$null$28$TwoStepVerificationActivity(byte[] var1, TLObject var2, byte[] var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$TwoStepVerificationActivity$xhbQi4PJ11MM_ccHkQglBt5UyIo(this, this.checkSecretValues(var1, (TLRPC.TL_account_passwordSettings)var2), var3));
   }

   // $FF: synthetic method
   public void lambda$null$29$TwoStepVerificationActivity(TLRPC.TL_error var1, TLObject var2) {
      if (var1 == null) {
         this.currentPassword = (TLRPC.TL_account_password)var2;
         initPasswordNewAlgo(this.currentPassword);
         this.processDone();
      }

   }

   // $FF: synthetic method
   public void lambda$null$3$TwoStepVerificationActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$TwoStepVerificationActivity$RrCadl1PvK1PcC0yhZngVlsUO_I(this, var2, var1));
   }

   // $FF: synthetic method
   public void lambda$null$30$TwoStepVerificationActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$TwoStepVerificationActivity$6TAE9CBLd9XTecWOIuc8_sMGfrI(this, var2, var1));
   }

   // $FF: synthetic method
   public void lambda$null$31$TwoStepVerificationActivity(TLRPC.TL_error var1) {
      if ("SRP_ID_INVALID".equals(var1.text)) {
         TLRPC.TL_account_getPassword var4 = new TLRPC.TL_account_getPassword();
         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var4, new _$$Lambda$TwoStepVerificationActivity$Rpy7sAZdpMPMoVwAFeMyYhG4S6U(this), 8);
      } else {
         this.needHideProgress();
         if ("PASSWORD_HASH_INVALID".equals(var1.text)) {
            this.onFieldError(this.passwordEditText, true);
         } else if (var1.text.startsWith("FLOOD_WAIT")) {
            int var2 = Utilities.parseInt(var1.text);
            String var3;
            if (var2 < 60) {
               var3 = LocaleController.formatPluralString("Seconds", var2);
            } else {
               var3 = LocaleController.formatPluralString("Minutes", var2 / 60);
            }

            this.showAlertWithText(LocaleController.getString("AppName", 2131558635), LocaleController.formatString("FloodWaitTime", 2131559496, var3));
         } else {
            this.showAlertWithText(LocaleController.getString("AppName", 2131558635), var1.text);
         }

      }
   }

   // $FF: synthetic method
   public void lambda$null$32$TwoStepVerificationActivity(byte[] var1, byte[] var2, TLObject var3, TLRPC.TL_error var4) {
      if (var4 == null) {
         Utilities.globalQueue.postRunnable(new _$$Lambda$TwoStepVerificationActivity$WUc7fmeEGuagN_JxrWFhQ9_Sfr0(this, var1, var3, var2));
      } else {
         AndroidUtilities.runOnUIThread(new _$$Lambda$TwoStepVerificationActivity$E4rg93feEBQhYJq6Ifx6EQm4LFA(this, var4));
      }

   }

   // $FF: synthetic method
   public void lambda$null$34$TwoStepVerificationActivity(DialogInterface var1, int var2) {
      NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.didSetTwoStepPassword);
      this.finishFragment();
   }

   // $FF: synthetic method
   public void lambda$null$35$TwoStepVerificationActivity(TLRPC.TL_error var1) {
      if (var1 == null) {
         AlertDialog.Builder var3 = new AlertDialog.Builder(this.getParentActivity());
         var3.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$TwoStepVerificationActivity$WcaCems7fna6T9hxKJ46cWEKKY4(this));
         var3.setMessage(LocaleController.getString("PasswordReset", 2131560351));
         var3.setTitle(LocaleController.getString("AppName", 2131558635));
         Dialog var4 = this.showDialog(var3.create());
         if (var4 != null) {
            var4.setCanceledOnTouchOutside(false);
            var4.setCancelable(false);
         }
      } else if (var1.text.startsWith("CODE_INVALID")) {
         this.onFieldError(this.passwordEditText, true);
      } else if (var1.text.startsWith("FLOOD_WAIT")) {
         int var2 = Utilities.parseInt(var1.text);
         String var5;
         if (var2 < 60) {
            var5 = LocaleController.formatPluralString("Seconds", var2);
         } else {
            var5 = LocaleController.formatPluralString("Minutes", var2 / 60);
         }

         this.showAlertWithText(LocaleController.getString("AppName", 2131558635), LocaleController.formatString("FloodWaitTime", 2131559496, var5));
      } else {
         this.showAlertWithText(LocaleController.getString("AppName", 2131558635), var1.text);
      }

   }

   // $FF: synthetic method
   public void lambda$null$37$TwoStepVerificationActivity(DialogInterface var1, int var2) {
      if (this.type == 0) {
         this.loadPasswordInfo(false);
         this.doneItem.setVisibility(8);
      } else {
         NotificationCenter var3 = NotificationCenter.getInstance(super.currentAccount);
         var2 = NotificationCenter.didSetTwoStepPassword;
         byte[] var4 = this.currentPasswordHash;
         TLRPC.TL_account_password var5 = this.currentPassword;
         var3.postNotificationName(var2, var4, var5.new_algo, var5.new_secure_algo, var5.secure_random, this.email, this.hint, null, this.firstPassword);
         this.finishFragment();
      }

   }

   // $FF: synthetic method
   public void lambda$null$38$TwoStepVerificationActivity(TLRPC.TL_error var1) {
      if (this.type == 0 && this.waitingForEmail) {
         this.showDoneProgress(false);
      }

      if (var1 == null) {
         if (this.getParentActivity() == null) {
            return;
         }

         Runnable var4 = this.shortPollRunnable;
         if (var4 != null) {
            AndroidUtilities.cancelRunOnUIThread(var4);
            this.shortPollRunnable = null;
         }

         AlertDialog.Builder var2 = new AlertDialog.Builder(this.getParentActivity());
         var2.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$TwoStepVerificationActivity$pOfSC0LoruYxxVqu7mmiCuIPGxU(this));
         TLRPC.TL_account_password var5 = this.currentPassword;
         if (var5 != null && var5.has_password) {
            var2.setMessage(LocaleController.getString("YourEmailSuccessText", 2131561153));
         } else {
            var2.setMessage(LocaleController.getString("YourPasswordSuccessText", 2131561157));
         }

         var2.setTitle(LocaleController.getString("YourPasswordSuccess", 2131561156));
         Dialog var6 = this.showDialog(var2.create());
         if (var6 != null) {
            var6.setCanceledOnTouchOutside(false);
            var6.setCancelable(false);
         }
      } else if (var1.text.startsWith("CODE_INVALID")) {
         EditTextBoldCursor var7;
         if (this.waitingForEmail) {
            var7 = this.codeFieldCell.getTextView();
         } else {
            var7 = this.passwordEditText;
         }

         this.onFieldError(var7, true);
      } else if (var1.text.startsWith("FLOOD_WAIT")) {
         int var3 = Utilities.parseInt(var1.text);
         String var8;
         if (var3 < 60) {
            var8 = LocaleController.formatPluralString("Seconds", var3);
         } else {
            var8 = LocaleController.formatPluralString("Minutes", var3 / 60);
         }

         this.showAlertWithText(LocaleController.getString("AppName", 2131558635), LocaleController.formatString("FloodWaitTime", 2131559496, var8));
      } else {
         this.showAlertWithText(LocaleController.getString("AppName", 2131558635), var1.text);
      }

   }

   // $FF: synthetic method
   public void lambda$null$4$TwoStepVerificationActivity(DialogInterface var1, int var2) {
      Activity var3 = this.getParentActivity();
      StringBuilder var4 = new StringBuilder();
      var4.append("https://telegram.org/deactivate?phone=");
      var4.append(UserConfig.getInstance(super.currentAccount).getClientPhone());
      Browser.openUrl(var3, (String)var4.toString());
   }

   // $FF: synthetic method
   public void lambda$null$5$TwoStepVerificationActivity(DialogInterface var1, int var2) {
      this.email = "";
      this.setNewPassword(false);
   }

   // $FF: synthetic method
   public void lambda$null$7$TwoStepVerificationActivity(DialogInterface var1, int var2) {
      this.setNewPassword(true);
   }

   // $FF: synthetic method
   public void lambda$onResume$11$TwoStepVerificationActivity() {
      EditTextBoldCursor var1 = this.passwordEditText;
      if (var1 != null) {
         var1.requestFocus();
         AndroidUtilities.showKeyboard(this.passwordEditText);
      }

   }

   // $FF: synthetic method
   public void lambda$onResume$12$TwoStepVerificationActivity() {
      EditTextSettingsCell var1 = this.codeFieldCell;
      if (var1 != null) {
         var1.getTextView().requestFocus();
         AndroidUtilities.showKeyboard(this.codeFieldCell.getTextView());
      }

   }

   // $FF: synthetic method
   public void lambda$processDone$33$TwoStepVerificationActivity(byte[] var1) {
      TLRPC.TL_account_getPasswordSettings var2 = new TLRPC.TL_account_getPasswordSettings();
      TLRPC.PasswordKdfAlgo var3 = this.currentPassword.current_algo;
      byte[] var7;
      if (var3 instanceof TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
         var7 = SRPHelper.getX(var1, (TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow)var3);
      } else {
         var7 = null;
      }

      _$$Lambda$TwoStepVerificationActivity$9nDa_eoqxtx9fYaZZmibmVZpEjY var6 = new _$$Lambda$TwoStepVerificationActivity$9nDa_eoqxtx9fYaZZmibmVZpEjY(this, var1, var7);
      TLRPC.TL_account_password var4 = this.currentPassword;
      TLRPC.PasswordKdfAlgo var5 = var4.current_algo;
      TLRPC.TL_error var8;
      if (var5 instanceof TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
         TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow var9 = (TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow)var5;
         var2.password = SRPHelper.startCheck(var7, var4.srp_id, var4.srp_B, var9);
         if (var2.password == null) {
            var8 = new TLRPC.TL_error();
            var8.text = "ALGO_INVALID";
            var6.run((TLObject)null, var8);
            return;
         }

         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var2, var6, 10);
      } else {
         var8 = new TLRPC.TL_error();
         var8.text = "PASSWORD_HASH_INVALID";
         var6.run((TLObject)null, var8);
      }

   }

   // $FF: synthetic method
   public void lambda$processDone$36$TwoStepVerificationActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$TwoStepVerificationActivity$KRiOgrWvOScAk9_kyHUo1nOtRGA(this, var2));
   }

   // $FF: synthetic method
   public void lambda$sendEmailConfirm$39$TwoStepVerificationActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$TwoStepVerificationActivity$WFxjAtXG7ouiOAVtFI0iHMnbBt4(this, var2));
   }

   // $FF: synthetic method
   public void lambda$setNewPassword$18$TwoStepVerificationActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$TwoStepVerificationActivity$ecl8yWbW6dpt4FVHunWRv6TTi0Y(this, var2));
   }

   // $FF: synthetic method
   public void lambda$setNewPassword$25$TwoStepVerificationActivity(TLRPC.TL_account_updatePasswordSettings var1, boolean var2, String var3) {
      if (var1.password == null) {
         var1.password = this.getNewSrpPassword();
      }

      byte[] var4;
      byte[] var5;
      if (!var2 && var3 != null) {
         var4 = AndroidUtilities.getStringBytes(var3);
         TLRPC.PasswordKdfAlgo var13 = this.currentPassword.new_algo;
         if (var13 instanceof TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
            var5 = SRPHelper.getX(var4, (TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow)var13);
         } else {
            var5 = null;
         }
      } else {
         var5 = null;
         var4 = var5;
      }

      _$$Lambda$TwoStepVerificationActivity$VJV5wRfpqB6jJ8dEtoqePT3HaYk var15 = new _$$Lambda$TwoStepVerificationActivity$VJV5wRfpqB6jJ8dEtoqePT3HaYk(this, var2, var5, var1, var3);
      if (!var2) {
         if (var3 != null) {
            byte[] var6 = this.currentSecret;
            if (var6 != null && var6.length == 32) {
               TLRPC.SecurePasswordKdfAlgo var14 = this.currentPassword.new_secure_algo;
               if (var14 instanceof TLRPC.TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000) {
                  TLRPC.TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000 var16 = (TLRPC.TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000)var14;
                  byte[] var7 = Utilities.computePBKDF2(var4, var16.salt);
                  byte[] var8 = new byte[32];
                  System.arraycopy(var7, 0, var8, 0, 32);
                  byte[] var9 = new byte[16];
                  System.arraycopy(var7, 32, var9, 0, 16);
                  var7 = new byte[32];
                  System.arraycopy(this.currentSecret, 0, var7, 0, 32);
                  Utilities.aesCbcEncryptionByteArraySafe(var7, var8, var9, 0, var7.length, 0, 1);
                  var1.new_settings.new_secure_settings = new TLRPC.TL_secureSecretSettings();
                  TLRPC.TL_account_passwordInputSettings var18 = var1.new_settings;
                  TLRPC.TL_secureSecretSettings var19 = var18.new_secure_settings;
                  var19.secure_algo = var16;
                  var19.secure_secret = var7;
                  var19.secure_secret_id = this.currentSecretId;
                  var18.flags |= 4;
               }
            }
         }

         TLRPC.PasswordKdfAlgo var17 = this.currentPassword.new_algo;
         if (var17 instanceof TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
            if (var3 != null) {
               TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow var11 = (TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow)var17;
               var1.new_settings.new_password_hash = SRPHelper.getVBytes(var4, var11);
               if (var1.new_settings.new_password_hash == null) {
                  TLRPC.TL_error var12 = new TLRPC.TL_error();
                  var12.text = "ALGO_INVALID";
                  var15.run((TLObject)null, var12);
               }
            }

            ConnectionsManager.getInstance(super.currentAccount).sendRequest(var1, var15, 10);
         } else {
            TLRPC.TL_error var10 = new TLRPC.TL_error();
            var10.text = "PASSWORD_HASH_INVALID";
            var15.run((TLObject)null, var10);
         }
      } else {
         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var1, var15, 10);
      }

   }

   // $FF: synthetic method
   public void lambda$startShortpoll$15$TwoStepVerificationActivity() {
      if (this.shortPollRunnable != null) {
         this.loadPasswordInfo(true);
         this.shortPollRunnable = null;
      }
   }

   // $FF: synthetic method
   public void lambda$updateRows$16$TwoStepVerificationActivity() {
      if (!this.isFinishing() && !this.destroyed) {
         EditTextBoldCursor var1 = this.passwordEditText;
         if (var1 != null) {
            var1.requestFocus();
            AndroidUtilities.showKeyboard(this.passwordEditText);
         }
      }

   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      this.updateRows();
      if (this.type == 0) {
         NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.didSetTwoStepPassword);
      }

      return true;
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      if (this.type == 0) {
         NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.didSetTwoStepPassword);
         Runnable var1 = this.shortPollRunnable;
         if (var1 != null) {
            AndroidUtilities.cancelRunOnUIThread(var1);
            this.shortPollRunnable = null;
         }

         this.destroyed = true;
      }

      AlertDialog var3 = this.progressDialog;
      if (var3 != null) {
         try {
            var3.dismiss();
         } catch (Exception var2) {
            FileLog.e((Throwable)var2);
         }

         this.progressDialog = null;
      }

      AndroidUtilities.removeAdjustResize(this.getParentActivity(), super.classGuid);
   }

   public void onPause() {
      super.onPause();
      this.paused = true;
   }

   public void onResume() {
      super.onResume();
      this.paused = false;
      int var1 = this.type;
      if (var1 == 1) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$TwoStepVerificationActivity$K5PXZQZsq_ZtNeeIBTIP6T_teFE(this), 200L);
      } else if (var1 == 0) {
         EditTextSettingsCell var2 = this.codeFieldCell;
         if (var2 != null && var2.getVisibility() == 0) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$TwoStepVerificationActivity$3nO3FhnPEKwuXE4FLUhVmyh2GII(this), 200L);
         }
      }

      AndroidUtilities.requestAdjustResize(this.getParentActivity(), super.classGuid);
   }

   public void onTransitionAnimationEnd(boolean var1, boolean var2) {
      if (var1) {
         int var3 = this.type;
         if (var3 == 1) {
            AndroidUtilities.showKeyboard(this.passwordEditText);
         } else if (var3 == 0) {
            EditTextSettingsCell var4 = this.codeFieldCell;
            if (var4 != null && var4.getVisibility() == 0) {
               AndroidUtilities.showKeyboard(this.codeFieldCell.getTextView());
            }
         }
      }

   }

   public void setCloseAfterSet(boolean var1) {
      this.closeAfterSet = var1;
   }

   public void setCurrentPasswordInfo(byte[] var1, TLRPC.TL_account_password var2) {
      this.currentPasswordHash = var1;
      this.currentPassword = var2;
   }

   protected void setRecoveryParams(TLRPC.TL_account_password var1) {
      this.currentPassword = var1;
      this.passwordSetState = 4;
   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;

      public ListAdapter(Context var2) {
         this.mContext = var2;
      }

      public int getItemCount() {
         int var1;
         if (!TwoStepVerificationActivity.this.loading && TwoStepVerificationActivity.this.currentPassword != null) {
            var1 = TwoStepVerificationActivity.this.rowCount;
         } else {
            var1 = 0;
         }

         return var1;
      }

      public int getItemViewType(int var1) {
         if (var1 != TwoStepVerificationActivity.this.setPasswordDetailRow && var1 != TwoStepVerificationActivity.this.shadowRow && var1 != TwoStepVerificationActivity.this.passwordSetupDetailRow && var1 != TwoStepVerificationActivity.this.passwordEnabledDetailRow) {
            return var1 == TwoStepVerificationActivity.this.passwordCodeFieldRow ? 2 : 0;
         } else {
            return 1;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         boolean var2;
         if (var1.getItemViewType() == 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         boolean var4 = false;
         if (var3 != 0) {
            if (var3 == 1) {
               TextInfoPrivacyCell var5 = (TextInfoPrivacyCell)var1.itemView;
               if (var2 == TwoStepVerificationActivity.this.setPasswordDetailRow) {
                  var5.setText(LocaleController.getString("SetAdditionalPasswordInfo", 2131560729));
                  var5.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
               } else {
                  var3 = TwoStepVerificationActivity.this.shadowRow;
                  String var6 = "";
                  if (var2 == var3) {
                     var5.setText("");
                     var5.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                  } else if (var2 == TwoStepVerificationActivity.this.passwordSetupDetailRow) {
                     if (TwoStepVerificationActivity.this.currentPassword != null && TwoStepVerificationActivity.this.currentPassword.has_password) {
                        if (TwoStepVerificationActivity.this.currentPassword.email_unconfirmed_pattern != null) {
                           var6 = TwoStepVerificationActivity.this.currentPassword.email_unconfirmed_pattern;
                        }

                        var5.setText(LocaleController.formatString("EmailPasswordConfirmText3", 2131559330, var6));
                     } else {
                        if (TwoStepVerificationActivity.this.currentPassword.email_unconfirmed_pattern != null) {
                           var6 = TwoStepVerificationActivity.this.currentPassword.email_unconfirmed_pattern;
                        }

                        var5.setText(LocaleController.formatString("EmailPasswordConfirmText2", 2131559329, var6));
                     }

                     var5.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165396, "windowBackgroundGrayShadow"));
                  } else if (var2 == TwoStepVerificationActivity.this.passwordEnabledDetailRow) {
                     var5.setText(LocaleController.getString("EnabledPasswordText", 2131559350));
                     var5.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                  }
               }
            }
         } else {
            TextSettingsCell var7 = (TextSettingsCell)var1.itemView;
            var7.setTag("windowBackgroundWhiteBlackText");
            var7.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            if (var2 == TwoStepVerificationActivity.this.changePasswordRow) {
               var7.setText(LocaleController.getString("ChangePassword", 2131558909), true);
            } else if (var2 == TwoStepVerificationActivity.this.setPasswordRow) {
               var7.setText(LocaleController.getString("SetAdditionalPassword", 2131560728), true);
            } else if (var2 == TwoStepVerificationActivity.this.turnPasswordOffRow) {
               var7.setText(LocaleController.getString("TurnPasswordOff", 2131560916), true);
            } else if (var2 == TwoStepVerificationActivity.this.changeRecoveryEmailRow) {
               String var8 = LocaleController.getString("ChangeRecoveryEmail", 2131558917);
               if (TwoStepVerificationActivity.this.abortPasswordRow != -1) {
                  var4 = true;
               }

               var7.setText(var8, var4);
            } else if (var2 == TwoStepVerificationActivity.this.resendCodeRow) {
               var7.setText(LocaleController.getString("ResendCode", 2131560581), true);
            } else if (var2 == TwoStepVerificationActivity.this.setRecoveryEmailRow) {
               var7.setText(LocaleController.getString("SetRecoveryEmail", 2131560736), false);
            } else if (var2 == TwoStepVerificationActivity.this.abortPasswordRow) {
               var7.setTag("windowBackgroundWhiteRedText3");
               var7.setTextColor(Theme.getColor("windowBackgroundWhiteRedText3"));
               if (TwoStepVerificationActivity.this.currentPassword != null && TwoStepVerificationActivity.this.currentPassword.has_password) {
                  var7.setText(LocaleController.getString("AbortEmail", 2131558401), false);
               } else {
                  var7.setText(LocaleController.getString("AbortPassword", 2131558402), false);
               }
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var4;
         if (var2 != 0) {
            if (var2 != 1) {
               EditTextSettingsCell var3 = TwoStepVerificationActivity.this.codeFieldCell;
               var4 = var3;
               if (var3.getParent() != null) {
                  ((ViewGroup)var3.getParent()).removeView(var3);
                  var4 = var3;
               }
            } else {
               var4 = new TextInfoPrivacyCell(this.mContext);
            }
         } else {
            var4 = new TextSettingsCell(this.mContext);
            ((View)var4).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         }

         return new RecyclerListView.Holder((View)var4);
      }
   }
}
