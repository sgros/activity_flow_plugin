// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.ViewGroup;
import android.app.Activity;
import org.telegram.messenger.browser.Browser;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.Components.AlertsCreator;
import android.content.DialogInterface;
import android.view.KeyEvent;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.text.Editable;
import android.text.TextWatcher;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View$OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ActionMode;
import android.view.ActionMode$Callback;
import android.widget.TextView$OnEditorActionListener;
import android.graphics.Typeface;
import android.widget.LinearLayout;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import android.widget.FrameLayout;
import org.telegram.ui.ActionBar.ActionBar;
import android.text.TextUtils;
import org.telegram.ui.ActionBar.Theme;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import android.text.method.TransformationMethod;
import android.text.method.PasswordTransformationMethod;
import org.telegram.messenger.UserConfig;
import android.widget.Toast;
import org.telegram.messenger.LocaleController;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import android.os.Vibrator;
import android.content.Context;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.SRPHelper;
import java.math.BigInteger;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.Utilities;
import android.widget.ScrollView;
import org.telegram.ui.Components.ContextProgressView;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.EmptyTextProgressView;
import android.animation.AnimatorSet;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Cells.EditTextSettingsCell;
import android.widget.TextView;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class TwoStepVerificationActivity extends BaseFragment implements NotificationCenterDelegate
{
    private static final int done_button = 1;
    private int abortPasswordRow;
    private TextView bottomButton;
    private TextView bottomTextView;
    private int changePasswordRow;
    private int changeRecoveryEmailRow;
    private boolean closeAfterSet;
    private EditTextSettingsCell codeFieldCell;
    private TLRPC.TL_account_password currentPassword;
    private byte[] currentPasswordHash;
    private byte[] currentSecret;
    private long currentSecretId;
    private boolean destroyed;
    private ActionBarMenuItem doneItem;
    private AnimatorSet doneItemAnimation;
    private String email;
    private int emailCodeLength;
    private boolean emailOnly;
    private EmptyTextProgressView emptyView;
    private String firstPassword;
    private String hint;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private boolean loading;
    private int passwordCodeFieldRow;
    private EditTextBoldCursor passwordEditText;
    private int passwordEnabledDetailRow;
    private boolean passwordEntered;
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
    
    public TwoStepVerificationActivity(final int type) {
        this.emailCodeLength = 6;
        this.passwordEntered = true;
        this.currentPasswordHash = new byte[0];
        this.type = type;
        if (type == 0) {
            this.loadPasswordInfo(false);
        }
    }
    
    public TwoStepVerificationActivity(final int currentAccount, final int type) {
        this.emailCodeLength = 6;
        this.passwordEntered = true;
        this.currentPasswordHash = new byte[0];
        super.currentAccount = currentAccount;
        this.type = type;
        if (type == 0) {
            this.loadPasswordInfo(false);
        }
    }
    
    public static boolean canHandleCurrentPassword(final TLRPC.TL_account_password tl_account_password, final boolean b) {
        if (b) {
            if (tl_account_password.current_algo instanceof TLRPC.TL_passwordKdfAlgoUnknown) {
                return false;
            }
        }
        else if (tl_account_password.new_algo instanceof TLRPC.TL_passwordKdfAlgoUnknown || tl_account_password.current_algo instanceof TLRPC.TL_passwordKdfAlgoUnknown || tl_account_password.new_secure_algo instanceof TLRPC.TL_securePasswordKdfAlgoUnknown) {
            return false;
        }
        return true;
    }
    
    private boolean checkSecretValues(byte[] array, final TLRPC.TL_account_passwordSettings tl_account_passwordSettings) {
        final TLRPC.TL_secureSecretSettings secure_settings = tl_account_passwordSettings.secure_settings;
        if (secure_settings != null) {
            this.currentSecret = secure_settings.secure_secret;
            final TLRPC.SecurePasswordKdfAlgo secure_algo = secure_settings.secure_algo;
            if (secure_algo instanceof TLRPC.TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000) {
                array = Utilities.computePBKDF2(array, ((TLRPC.TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000)secure_algo).salt);
            }
            else {
                if (!(secure_algo instanceof TLRPC.TL_securePasswordKdfAlgoSHA512)) {
                    return false;
                }
                final byte[] salt = ((TLRPC.TL_securePasswordKdfAlgoSHA512)secure_algo).salt;
                array = Utilities.computeSHA512(salt, array, salt);
            }
            this.currentSecretId = tl_account_passwordSettings.secure_settings.secure_secret_id;
            final byte[] array2 = new byte[32];
            System.arraycopy(array, 0, array2, 0, 32);
            final byte[] array3 = new byte[16];
            System.arraycopy(array, 32, array3, 0, 16);
            array = this.currentSecret;
            Utilities.aesCbcEncryptionByteArraySafe(array, array2, array3, 0, array.length, 0, 0);
            final TLRPC.TL_secureSecretSettings secure_settings2 = tl_account_passwordSettings.secure_settings;
            if (!PassportActivity.checkSecret(secure_settings2.secure_secret, secure_settings2.secure_secret_id)) {
                final TLRPC.TL_account_updatePasswordSettings tl_account_updatePasswordSettings = new TLRPC.TL_account_updatePasswordSettings();
                tl_account_updatePasswordSettings.password = this.getNewSrpPassword();
                tl_account_updatePasswordSettings.new_settings = new TLRPC.TL_account_passwordInputSettings();
                tl_account_updatePasswordSettings.new_settings.new_secure_settings = new TLRPC.TL_secureSecretSettings();
                final TLRPC.TL_secureSecretSettings new_secure_settings = tl_account_updatePasswordSettings.new_settings.new_secure_settings;
                new_secure_settings.secure_secret = new byte[0];
                new_secure_settings.secure_algo = new TLRPC.TL_securePasswordKdfAlgoUnknown();
                final TLRPC.TL_account_passwordInputSettings new_settings = tl_account_updatePasswordSettings.new_settings;
                new_settings.new_secure_settings.secure_secret_id = 0L;
                new_settings.flags |= 0x4;
                ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_account_updatePasswordSettings, (RequestDelegate)_$$Lambda$TwoStepVerificationActivity$0aZmkrG3QYsDjfpNDgl1nGy4PJM.INSTANCE);
                this.currentSecret = null;
                this.currentSecretId = 0L;
            }
        }
        else {
            this.currentSecret = null;
            this.currentSecretId = 0L;
        }
        return true;
    }
    
    private static byte[] getBigIntegerBytes(final BigInteger bigInteger) {
        final byte[] byteArray = bigInteger.toByteArray();
        if (byteArray.length > 256) {
            final byte[] array = new byte[256];
            System.arraycopy(byteArray, 1, array, 0, 256);
            return array;
        }
        return byteArray;
    }
    
    private TLRPC.TL_inputCheckPasswordSRP getNewSrpPassword() {
        final TLRPC.TL_account_password currentPassword = this.currentPassword;
        final TLRPC.PasswordKdfAlgo current_algo = currentPassword.current_algo;
        if (current_algo instanceof TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
            return SRPHelper.startCheck(this.currentPasswordHash, currentPassword.srp_id, currentPassword.srp_B, (TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow)current_algo);
        }
        return null;
    }
    
    public static void initPasswordNewAlgo(final TLRPC.TL_account_password tl_account_password) {
        final TLRPC.PasswordKdfAlgo new_algo = tl_account_password.new_algo;
        if (new_algo instanceof TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
            final TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow tl_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow = (TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow)new_algo;
            final byte[] array = new byte[tl_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow.salt1.length + 32];
            Utilities.random.nextBytes(array);
            final byte[] salt1 = tl_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow.salt1;
            System.arraycopy(salt1, 0, array, 0, salt1.length);
            tl_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow.salt1 = array;
        }
        final TLRPC.SecurePasswordKdfAlgo new_secure_algo = tl_account_password.new_secure_algo;
        if (new_secure_algo instanceof TLRPC.TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000) {
            final TLRPC.TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000 tl_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000 = (TLRPC.TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000)new_secure_algo;
            final byte[] array2 = new byte[tl_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000.salt.length + 32];
            Utilities.random.nextBytes(array2);
            final byte[] salt2 = tl_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000.salt;
            System.arraycopy(salt2, 0, array2, 0, salt2.length);
            tl_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000.salt = array2;
        }
    }
    
    private boolean isValidEmail(final String s) {
        boolean b2;
        final boolean b = b2 = false;
        if (s != null) {
            if (s.length() < 3) {
                b2 = b;
            }
            else {
                final int lastIndex = s.lastIndexOf(46);
                final int lastIndex2 = s.lastIndexOf(64);
                b2 = b;
                if (lastIndex2 >= 0) {
                    b2 = b;
                    if (lastIndex >= lastIndex2) {
                        b2 = true;
                    }
                }
            }
        }
        return b2;
    }
    
    private void loadPasswordInfo(final boolean b) {
        if (!b) {
            this.loading = true;
            final ListAdapter listAdapter = this.listAdapter;
            if (listAdapter != null) {
                ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
            }
        }
        ConnectionsManager.getInstance(super.currentAccount).sendRequest(new TLRPC.TL_account_getPassword(), new _$$Lambda$TwoStepVerificationActivity$oYcvtMp3mI7Cv13Fi4uxabOCA00(this, b), 10);
    }
    
    private void needHideProgress() {
        final AlertDialog progressDialog = this.progressDialog;
        if (progressDialog == null) {
            return;
        }
        try {
            progressDialog.dismiss();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        this.progressDialog = null;
    }
    
    private void needShowProgress() {
        if (this.getParentActivity() != null && !this.getParentActivity().isFinishing()) {
            if (this.progressDialog == null) {
                (this.progressDialog = new AlertDialog((Context)this.getParentActivity(), 3)).setCanCacnel(false);
                this.progressDialog.show();
            }
        }
    }
    
    private void onFieldError(final TextView textView, final boolean b) {
        if (this.getParentActivity() == null) {
            return;
        }
        final Vibrator vibrator = (Vibrator)this.getParentActivity().getSystemService("vibrator");
        if (vibrator != null) {
            vibrator.vibrate(200L);
        }
        if (b) {
            textView.setText((CharSequence)"");
        }
        AndroidUtilities.shakeView((View)textView, 2.0f, 0);
    }
    
    private void processDone() {
        final int type = this.type;
        if (type == 0) {
            if (!this.passwordEntered) {
                final String string = this.passwordEditText.getText().toString();
                if (string.length() == 0) {
                    this.onFieldError((TextView)this.passwordEditText, false);
                    return;
                }
                final byte[] stringBytes = AndroidUtilities.getStringBytes(string);
                this.needShowProgress();
                Utilities.globalQueue.postRunnable(new _$$Lambda$TwoStepVerificationActivity$q99ypmQww_xNEmbw14QjiBotShg(this, stringBytes));
            }
            else if (this.waitingForEmail && this.currentPassword != null) {
                if (this.codeFieldCell.length() == 0) {
                    this.onFieldError((TextView)this.codeFieldCell.getTextView(), false);
                    return;
                }
                this.sendEmailConfirm(this.codeFieldCell.getText());
                this.showDoneProgress(true);
            }
        }
        else if (type == 1) {
            final int passwordSetState = this.passwordSetState;
            if (passwordSetState == 0) {
                if (this.passwordEditText.getText().length() == 0) {
                    this.onFieldError((TextView)this.passwordEditText, false);
                    return;
                }
                this.titleTextView.setText((CharSequence)LocaleController.getString("ReEnterYourPasscode", 2131560536));
                this.firstPassword = this.passwordEditText.getText().toString();
                this.setPasswordSetState(1);
            }
            else if (passwordSetState == 1) {
                if (!this.firstPassword.equals(this.passwordEditText.getText().toString())) {
                    try {
                        Toast.makeText((Context)this.getParentActivity(), (CharSequence)LocaleController.getString("PasswordDoNotMatch", 2131560346), 0).show();
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                    this.onFieldError((TextView)this.passwordEditText, true);
                    return;
                }
                this.setPasswordSetState(2);
            }
            else if (passwordSetState == 2) {
                this.hint = this.passwordEditText.getText().toString();
                if (this.hint.toLowerCase().equals(this.firstPassword.toLowerCase())) {
                    try {
                        Toast.makeText((Context)this.getParentActivity(), (CharSequence)LocaleController.getString("PasswordAsHintError", 2131560344), 0).show();
                    }
                    catch (Exception ex2) {
                        FileLog.e(ex2);
                    }
                    this.onFieldError((TextView)this.passwordEditText, false);
                    return;
                }
                if (!this.currentPassword.has_recovery) {
                    this.setPasswordSetState(3);
                }
                else {
                    this.email = "";
                    this.setNewPassword(false);
                }
            }
            else if (passwordSetState == 3) {
                this.email = this.passwordEditText.getText().toString();
                if (!this.isValidEmail(this.email)) {
                    this.onFieldError((TextView)this.passwordEditText, false);
                    return;
                }
                this.setNewPassword(false);
            }
            else if (passwordSetState == 4) {
                final String string2 = this.passwordEditText.getText().toString();
                if (string2.length() == 0) {
                    this.onFieldError((TextView)this.passwordEditText, false);
                    return;
                }
                final TLRPC.TL_auth_recoverPassword tl_auth_recoverPassword = new TLRPC.TL_auth_recoverPassword();
                tl_auth_recoverPassword.code = string2;
                ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_auth_recoverPassword, new _$$Lambda$TwoStepVerificationActivity$f9cuV_saSqd5BKXFxtWSGi8SzTE(this), 10);
            }
        }
    }
    
    private void sendEmailConfirm(final String code) {
        final TLRPC.TL_account_confirmPasswordEmail tl_account_confirmPasswordEmail = new TLRPC.TL_account_confirmPasswordEmail();
        tl_account_confirmPasswordEmail.code = code;
        ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_account_confirmPasswordEmail, new _$$Lambda$TwoStepVerificationActivity$zGkZlp2DnS104dUP2z3jhg8EgmM(this), 10);
    }
    
    private void setNewPassword(final boolean b) {
        if (b && this.waitingForEmail && this.currentPassword.has_password) {
            this.needShowProgress();
            ConnectionsManager.getInstance(super.currentAccount).sendRequest(new TLRPC.TL_account_cancelPasswordEmail(), new _$$Lambda$TwoStepVerificationActivity$XEU2eF294cmakN0jjZyU1ShYsjA(this));
            return;
        }
        final String firstPassword = this.firstPassword;
        final TLRPC.TL_account_updatePasswordSettings tl_account_updatePasswordSettings = new TLRPC.TL_account_updatePasswordSettings();
        final byte[] currentPasswordHash = this.currentPasswordHash;
        if (currentPasswordHash == null || currentPasswordHash.length == 0) {
            tl_account_updatePasswordSettings.password = new TLRPC.TL_inputCheckPasswordEmpty();
        }
        tl_account_updatePasswordSettings.new_settings = new TLRPC.TL_account_passwordInputSettings();
        if (b) {
            UserConfig.getInstance(super.currentAccount).resetSavedPassword();
            this.currentSecret = null;
            if (this.waitingForEmail) {
                final TLRPC.TL_account_passwordInputSettings new_settings = tl_account_updatePasswordSettings.new_settings;
                new_settings.flags = 2;
                new_settings.email = "";
                tl_account_updatePasswordSettings.password = new TLRPC.TL_inputCheckPasswordEmpty();
            }
            else {
                final TLRPC.TL_account_passwordInputSettings new_settings2 = tl_account_updatePasswordSettings.new_settings;
                new_settings2.flags = 3;
                new_settings2.hint = "";
                new_settings2.new_password_hash = new byte[0];
                new_settings2.new_algo = new TLRPC.TL_passwordKdfAlgoUnknown();
                tl_account_updatePasswordSettings.new_settings.email = "";
            }
        }
        else {
            if (this.hint == null) {
                final TLRPC.TL_account_password currentPassword = this.currentPassword;
                if (currentPassword != null) {
                    this.hint = currentPassword.hint;
                }
            }
            if (this.hint == null) {
                this.hint = "";
            }
            if (firstPassword != null) {
                final TLRPC.TL_account_passwordInputSettings new_settings3 = tl_account_updatePasswordSettings.new_settings;
                new_settings3.flags |= 0x1;
                new_settings3.hint = this.hint;
                new_settings3.new_algo = this.currentPassword.new_algo;
            }
            if (this.email.length() > 0) {
                final TLRPC.TL_account_passwordInputSettings new_settings4 = tl_account_updatePasswordSettings.new_settings;
                new_settings4.flags |= 0x2;
                new_settings4.email = this.email.trim();
            }
        }
        this.needShowProgress();
        Utilities.globalQueue.postRunnable(new _$$Lambda$TwoStepVerificationActivity$BbXpzRMc1z5d7yEqTJj2C6ReLvg(this, tl_account_updatePasswordSettings, b, firstPassword));
    }
    
    private void setPasswordSetState(int n) {
        if (this.passwordEditText == null) {
            return;
        }
        this.passwordSetState = n;
        final int passwordSetState = this.passwordSetState;
        n = 4;
        if (passwordSetState == 0) {
            super.actionBar.setTitle(LocaleController.getString("YourPassword", 2131561155));
            if (this.currentPassword.has_password) {
                this.titleTextView.setText((CharSequence)LocaleController.getString("PleaseEnterPassword", 2131560457));
            }
            else {
                this.titleTextView.setText((CharSequence)LocaleController.getString("PleaseEnterFirstPassword", 2131560456));
            }
            this.passwordEditText.setImeOptions(5);
            this.passwordEditText.setTransformationMethod((TransformationMethod)PasswordTransformationMethod.getInstance());
            this.bottomTextView.setVisibility(4);
            this.bottomButton.setVisibility(4);
        }
        else if (passwordSetState == 1) {
            super.actionBar.setTitle(LocaleController.getString("YourPassword", 2131561155));
            this.titleTextView.setText((CharSequence)LocaleController.getString("PleaseReEnterPassword", 2131560459));
            this.passwordEditText.setImeOptions(5);
            this.passwordEditText.setTransformationMethod((TransformationMethod)PasswordTransformationMethod.getInstance());
            this.bottomTextView.setVisibility(4);
            this.bottomButton.setVisibility(4);
        }
        else if (passwordSetState == 2) {
            super.actionBar.setTitle(LocaleController.getString("PasswordHint", 2131560348));
            this.titleTextView.setText((CharSequence)LocaleController.getString("PasswordHintText", 2131560349));
            this.passwordEditText.setImeOptions(5);
            this.passwordEditText.setTransformationMethod((TransformationMethod)null);
            this.bottomTextView.setVisibility(4);
            this.bottomButton.setVisibility(4);
        }
        else if (passwordSetState == 3) {
            super.actionBar.setTitle(LocaleController.getString("RecoveryEmail", 2131560549));
            this.titleTextView.setText((CharSequence)LocaleController.getString("YourEmail", 2131561144));
            this.passwordEditText.setImeOptions(5);
            this.passwordEditText.setTransformationMethod((TransformationMethod)null);
            this.passwordEditText.setInputType(33);
            this.bottomTextView.setVisibility(0);
            final TextView bottomButton = this.bottomButton;
            if (!this.emailOnly) {
                n = 0;
            }
            bottomButton.setVisibility(n);
        }
        else if (passwordSetState == 4) {
            super.actionBar.setTitle(LocaleController.getString("PasswordRecovery", 2131560350));
            this.titleTextView.setText((CharSequence)LocaleController.getString("PasswordCode", 2131560345));
            this.bottomTextView.setText((CharSequence)LocaleController.getString("RestoreEmailSentInfo", 2131560608));
            final TextView bottomButton2 = this.bottomButton;
            String email_unconfirmed_pattern = this.currentPassword.email_unconfirmed_pattern;
            if (email_unconfirmed_pattern == null) {
                email_unconfirmed_pattern = "";
            }
            bottomButton2.setText((CharSequence)LocaleController.formatString("RestoreEmailTrouble", 2131560609, email_unconfirmed_pattern));
            this.passwordEditText.setImeOptions(6);
            this.passwordEditText.setTransformationMethod((TransformationMethod)null);
            this.passwordEditText.setInputType(3);
            this.bottomTextView.setVisibility(0);
            this.bottomButton.setVisibility(0);
        }
        this.passwordEditText.setText((CharSequence)"");
    }
    
    private void showAlertWithText(final String title, final String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
        builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
        builder.setTitle(title);
        builder.setMessage(message);
        this.showDialog(builder.create());
    }
    
    private void showDoneProgress(final boolean b) {
        final AnimatorSet doneItemAnimation = this.doneItemAnimation;
        if (doneItemAnimation != null) {
            doneItemAnimation.cancel();
        }
        this.doneItemAnimation = new AnimatorSet();
        if (b) {
            this.progressView.setVisibility(0);
            this.doneItem.setEnabled(false);
            this.doneItemAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), "scaleX", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), "scaleY", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), "alpha", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, "scaleX", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, "scaleY", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, "alpha", new float[] { 1.0f }) });
        }
        else {
            this.doneItem.getImageView().setVisibility(0);
            this.doneItem.setEnabled(true);
            this.doneItemAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.progressView, "scaleX", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, "scaleY", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, "alpha", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), "scaleX", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), "scaleY", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), "alpha", new float[] { 1.0f }) });
        }
        this.doneItemAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationCancel(final Animator obj) {
                if (TwoStepVerificationActivity.this.doneItemAnimation != null && TwoStepVerificationActivity.this.doneItemAnimation.equals(obj)) {
                    TwoStepVerificationActivity.this.doneItemAnimation = null;
                }
            }
            
            public void onAnimationEnd(final Animator obj) {
                if (TwoStepVerificationActivity.this.doneItemAnimation != null && TwoStepVerificationActivity.this.doneItemAnimation.equals(obj)) {
                    if (!b) {
                        TwoStepVerificationActivity.this.progressView.setVisibility(4);
                    }
                    else {
                        TwoStepVerificationActivity.this.doneItem.getImageView().setVisibility(4);
                    }
                }
            }
        });
        this.doneItemAnimation.setDuration(150L);
        this.doneItemAnimation.start();
    }
    
    private void startShortpoll() {
        final Runnable shortPollRunnable = this.shortPollRunnable;
        if (shortPollRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(shortPollRunnable);
        }
        AndroidUtilities.runOnUIThread(this.shortPollRunnable = new _$$Lambda$TwoStepVerificationActivity$wO_OLA1D7DR12e78ifRLekXVYec(this), 5000L);
    }
    
    private void updateRows() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.setPasswordRow);
        sb.append(this.setPasswordDetailRow);
        sb.append(this.changePasswordRow);
        sb.append(this.turnPasswordOffRow);
        sb.append(this.setRecoveryEmailRow);
        sb.append(this.changeRecoveryEmailRow);
        sb.append(this.resendCodeRow);
        sb.append(this.abortPasswordRow);
        sb.append(this.passwordSetupDetailRow);
        sb.append(this.passwordCodeFieldRow);
        sb.append(this.passwordEnabledDetailRow);
        sb.append(this.shadowRow);
        sb.append(this.rowCount);
        final boolean b = this.passwordCodeFieldRow != -1;
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
            final TLRPC.TL_account_password currentPassword = this.currentPassword;
            if (currentPassword != null) {
                if (this.waitingForEmail) {
                    this.passwordCodeFieldRow = this.rowCount++;
                    this.passwordSetupDetailRow = this.rowCount++;
                    this.resendCodeRow = this.rowCount++;
                    this.abortPasswordRow = this.rowCount++;
                    this.shadowRow = this.rowCount++;
                }
                else if (currentPassword.has_password) {
                    this.changePasswordRow = this.rowCount++;
                    this.turnPasswordOffRow = this.rowCount++;
                    if (currentPassword.has_recovery) {
                        this.changeRecoveryEmailRow = this.rowCount++;
                    }
                    else {
                        this.setRecoveryEmailRow = this.rowCount++;
                    }
                    this.passwordEnabledDetailRow = this.rowCount++;
                }
                else {
                    this.setPasswordRow = this.rowCount++;
                    this.setPasswordDetailRow = this.rowCount++;
                }
            }
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(this.setPasswordRow);
        sb2.append(this.setPasswordDetailRow);
        sb2.append(this.changePasswordRow);
        sb2.append(this.turnPasswordOffRow);
        sb2.append(this.setRecoveryEmailRow);
        sb2.append(this.changeRecoveryEmailRow);
        sb2.append(this.resendCodeRow);
        sb2.append(this.abortPasswordRow);
        sb2.append(this.passwordSetupDetailRow);
        sb2.append(this.passwordCodeFieldRow);
        sb2.append(this.passwordEnabledDetailRow);
        sb2.append(this.shadowRow);
        sb2.append(this.rowCount);
        if (this.listAdapter != null && !sb.toString().equals(sb2.toString())) {
            this.listAdapter.notifyDataSetChanged();
            if (this.passwordCodeFieldRow == -1 && this.getParentActivity() != null && b) {
                AndroidUtilities.hideKeyboard(this.getParentActivity().getCurrentFocus());
                this.codeFieldCell.setText("", false);
            }
        }
        if (super.fragmentView != null) {
            if (!this.loading && !this.passwordEntered) {
                final RecyclerListView listView = this.listView;
                if (listView != null) {
                    listView.setEmptyView(null);
                    this.listView.setVisibility(4);
                    this.scrollView.setVisibility(0);
                    this.emptyView.setVisibility(4);
                }
                if (this.passwordEditText != null) {
                    this.doneItem.setVisibility(0);
                    this.passwordEditText.setVisibility(0);
                    super.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    super.fragmentView.setTag((Object)"windowBackgroundWhite");
                    this.titleTextView.setVisibility(0);
                    this.bottomButton.setVisibility(0);
                    this.bottomTextView.setVisibility(4);
                    this.bottomButton.setText((CharSequence)LocaleController.getString("ForgotPassword", 2131559503));
                    if (!TextUtils.isEmpty((CharSequence)this.currentPassword.hint)) {
                        this.passwordEditText.setHint((CharSequence)this.currentPassword.hint);
                    }
                    else {
                        this.passwordEditText.setHint((CharSequence)"");
                    }
                    AndroidUtilities.runOnUIThread(new _$$Lambda$TwoStepVerificationActivity$9VaVKy9W3pbWvJiDnN2bOVx7AfA(this), 200L);
                }
            }
            else {
                final RecyclerListView listView2 = this.listView;
                if (listView2 != null) {
                    listView2.setVisibility(0);
                    this.scrollView.setVisibility(4);
                    this.listView.setEmptyView((View)this.emptyView);
                }
                if (this.waitingForEmail && this.currentPassword != null) {
                    this.doneItem.setVisibility(0);
                }
                else if (this.passwordEditText != null) {
                    this.doneItem.setVisibility(8);
                    this.passwordEditText.setVisibility(4);
                    this.titleTextView.setVisibility(4);
                    this.bottomTextView.setVisibility(4);
                    this.bottomButton.setVisibility(4);
                }
                super.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
                super.fragmentView.setTag((Object)"windowBackgroundGray");
            }
        }
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(false);
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    TwoStepVerificationActivity.this.finishFragment();
                }
                else if (n == 1) {
                    TwoStepVerificationActivity.this.processDone();
                }
            }
        });
        super.fragmentView = (View)new FrameLayout(context);
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        frameLayout.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        this.doneItem = super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0f));
        (this.progressView = new ContextProgressView(context, 1)).setAlpha(0.0f);
        this.progressView.setScaleX(0.1f);
        this.progressView.setScaleY(0.1f);
        this.progressView.setVisibility(4);
        this.doneItem.addView((View)this.progressView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        (this.scrollView = new ScrollView(context)).setFillViewport(true);
        frameLayout.addView((View)this.scrollView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        final LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        this.scrollView.addView((View)linearLayout, (ViewGroup$LayoutParams)LayoutHelper.createScroll(-1, -2, 51));
        (this.titleTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
        this.titleTextView.setTextSize(1, 18.0f);
        this.titleTextView.setGravity(1);
        linearLayout.addView((View)this.titleTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 1, 0, 38, 0, 0));
        (this.passwordEditText = new EditTextBoldCursor(context)).setTextSize(1, 20.0f);
        this.passwordEditText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.passwordEditText.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
        this.passwordEditText.setBackgroundDrawable(Theme.createEditTextDrawable(context, false));
        this.passwordEditText.setMaxLines(1);
        this.passwordEditText.setLines(1);
        this.passwordEditText.setGravity(1);
        this.passwordEditText.setSingleLine(true);
        this.passwordEditText.setInputType(129);
        this.passwordEditText.setTransformationMethod((TransformationMethod)PasswordTransformationMethod.getInstance());
        this.passwordEditText.setTypeface(Typeface.DEFAULT);
        this.passwordEditText.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.passwordEditText.setCursorSize(AndroidUtilities.dp(20.0f));
        this.passwordEditText.setCursorWidth(1.5f);
        linearLayout.addView((View)this.passwordEditText, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 36, 51, 40, 32, 40, 0));
        this.passwordEditText.setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$TwoStepVerificationActivity$YWYhM6syo7BDAVMlJEwwl5l8lR0(this));
        this.passwordEditText.setCustomSelectionActionModeCallback((ActionMode$Callback)new ActionMode$Callback() {
            public boolean onActionItemClicked(final ActionMode actionMode, final MenuItem menuItem) {
                return false;
            }
            
            public boolean onCreateActionMode(final ActionMode actionMode, final Menu menu) {
                return false;
            }
            
            public void onDestroyActionMode(final ActionMode actionMode) {
            }
            
            public boolean onPrepareActionMode(final ActionMode actionMode, final Menu menu) {
                return false;
            }
        });
        (this.bottomTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
        this.bottomTextView.setTextSize(1, 14.0f);
        final TextView bottomTextView = this.bottomTextView;
        final boolean isRTL = LocaleController.isRTL;
        final int n = 5;
        int n2;
        if (isRTL) {
            n2 = 5;
        }
        else {
            n2 = 3;
        }
        bottomTextView.setGravity(n2 | 0x30);
        this.bottomTextView.setText((CharSequence)LocaleController.getString("YourEmailInfo", 2131561149));
        final TextView bottomTextView2 = this.bottomTextView;
        int n3;
        if (LocaleController.isRTL) {
            n3 = 5;
        }
        else {
            n3 = 3;
        }
        linearLayout.addView((View)bottomTextView2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, n3 | 0x30, 40, 30, 40, 0));
        final LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setGravity(80);
        linearLayout.addView((View)linearLayout2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -1));
        (this.bottomButton = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
        this.bottomButton.setTextSize(1, 14.0f);
        final TextView bottomButton = this.bottomButton;
        int n4;
        if (LocaleController.isRTL) {
            n4 = 5;
        }
        else {
            n4 = 3;
        }
        bottomButton.setGravity(n4 | 0x50);
        this.bottomButton.setText((CharSequence)LocaleController.getString("YourEmailSkip", 2131561150));
        this.bottomButton.setPadding(0, AndroidUtilities.dp(10.0f), 0, 0);
        final TextView bottomButton2 = this.bottomButton;
        int n5;
        if (LocaleController.isRTL) {
            n5 = n;
        }
        else {
            n5 = 3;
        }
        linearLayout2.addView((View)bottomButton2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2, n5 | 0x50, 40, 0, 40, 14));
        this.bottomButton.setOnClickListener((View$OnClickListener)new _$$Lambda$TwoStepVerificationActivity$hA8CuA2mAlwBUP9M3CP0dvU1Oc0(this));
        final int type = this.type;
        if (type == 0) {
            (this.emptyView = new EmptyTextProgressView(context)).showProgress();
            frameLayout.addView((View)this.emptyView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
            (this.listView = new RecyclerListView(context)).setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(context, 1, false));
            this.listView.setEmptyView((View)this.emptyView);
            this.listView.setVerticalScrollBarEnabled(false);
            frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
            this.listView.setAdapter(this.listAdapter = new ListAdapter(context));
            this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$TwoStepVerificationActivity$R271ECy978pWdDX5q4Leb46bpF0(this));
            (this.codeFieldCell = new EditTextSettingsCell(context)).setTextAndHint("", LocaleController.getString("PasswordCode", 2131560345), false);
            this.codeFieldCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            final EditTextBoldCursor textView = this.codeFieldCell.getTextView();
            textView.setInputType(3);
            textView.setImeOptions(6);
            textView.setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$TwoStepVerificationActivity$Nl_K_LAtgIdu6AfA5V1bgHfleb8(this));
            textView.addTextChangedListener((TextWatcher)new TextWatcher() {
                public void afterTextChanged(final Editable editable) {
                    if (TwoStepVerificationActivity.this.emailCodeLength != 0 && editable.length() == TwoStepVerificationActivity.this.emailCodeLength) {
                        TwoStepVerificationActivity.this.processDone();
                    }
                }
                
                public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                }
                
                public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                }
            });
            this.updateRows();
            super.actionBar.setTitle(LocaleController.getString("TwoStepVerificationTitle", 2131560920));
            this.titleTextView.setText((CharSequence)LocaleController.getString("PleaseEnterCurrentPassword", 2131560455));
        }
        else if (type == 1) {
            this.setPasswordSetState(this.passwordSetState);
        }
        if (this.passwordEntered && this.type != 1) {
            super.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
            super.fragmentView.setTag((Object)"windowBackgroundGray");
        }
        else {
            super.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            super.fragmentView.setTag((Object)"windowBackgroundWhite");
        }
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.didSetTwoStepPassword) {
            if (array != null && array.length > 0 && array[0] != null) {
                this.currentPasswordHash = (byte[])array[0];
                if (this.closeAfterSet && TextUtils.isEmpty((CharSequence)array[4]) && this.closeAfterSet) {
                    this.removeSelfFromStack();
                }
            }
            this.loadPasswordInfo(false);
            this.updateRows();
        }
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { TextSettingsCell.class, EditTextSettingsCell.class }, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "progressCircle"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteRedText3"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { EditTextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_HINTTEXTCOLOR, new Class[] { EditTextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteHintText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4"), new ThemeDescription((View)this.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"), new ThemeDescription((View)this.bottomTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"), new ThemeDescription((View)this.bottomButton, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlueText4"), new ThemeDescription((View)this.passwordEditText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.passwordEditText, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"), new ThemeDescription((View)this.passwordEditText, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"), new ThemeDescription((View)this.passwordEditText, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated") };
    }
    
    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        this.updateRows();
        if (this.type == 0) {
            NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.didSetTwoStepPassword);
        }
        return true;
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        if (this.type == 0) {
            NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.didSetTwoStepPassword);
            final Runnable shortPollRunnable = this.shortPollRunnable;
            if (shortPollRunnable != null) {
                AndroidUtilities.cancelRunOnUIThread(shortPollRunnable);
                this.shortPollRunnable = null;
            }
            this.destroyed = true;
        }
        final AlertDialog progressDialog = this.progressDialog;
        if (progressDialog != null) {
            try {
                progressDialog.dismiss();
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            this.progressDialog = null;
        }
        AndroidUtilities.removeAdjustResize(this.getParentActivity(), super.classGuid);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        this.paused = true;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        this.paused = false;
        final int type = this.type;
        if (type == 1) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$TwoStepVerificationActivity$K5PXZQZsq_ZtNeeIBTIP6T_teFE(this), 200L);
        }
        else if (type == 0) {
            final EditTextSettingsCell codeFieldCell = this.codeFieldCell;
            if (codeFieldCell != null && codeFieldCell.getVisibility() == 0) {
                AndroidUtilities.runOnUIThread(new _$$Lambda$TwoStepVerificationActivity$3nO3FhnPEKwuXE4FLUhVmyh2GII(this), 200L);
            }
        }
        AndroidUtilities.requestAdjustResize(this.getParentActivity(), super.classGuid);
    }
    
    public void onTransitionAnimationEnd(final boolean b, final boolean b2) {
        if (b) {
            final int type = this.type;
            if (type == 1) {
                AndroidUtilities.showKeyboard((View)this.passwordEditText);
            }
            else if (type == 0) {
                final EditTextSettingsCell codeFieldCell = this.codeFieldCell;
                if (codeFieldCell != null && codeFieldCell.getVisibility() == 0) {
                    AndroidUtilities.showKeyboard((View)this.codeFieldCell.getTextView());
                }
            }
        }
    }
    
    public void setCloseAfterSet(final boolean closeAfterSet) {
        this.closeAfterSet = closeAfterSet;
    }
    
    public void setCurrentPasswordInfo(final byte[] currentPasswordHash, final TLRPC.TL_account_password currentPassword) {
        this.currentPasswordHash = currentPasswordHash;
        this.currentPassword = currentPassword;
    }
    
    protected void setRecoveryParams(final TLRPC.TL_account_password currentPassword) {
        this.currentPassword = currentPassword;
        this.passwordSetState = 4;
    }
    
    private class ListAdapter extends SelectionAdapter
    {
        private Context mContext;
        
        public ListAdapter(final Context mContext) {
            this.mContext = mContext;
        }
        
        @Override
        public int getItemCount() {
            int access$700;
            if (!TwoStepVerificationActivity.this.loading && TwoStepVerificationActivity.this.currentPassword != null) {
                access$700 = TwoStepVerificationActivity.this.rowCount;
            }
            else {
                access$700 = 0;
            }
            return access$700;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == TwoStepVerificationActivity.this.setPasswordDetailRow || n == TwoStepVerificationActivity.this.shadowRow || n == TwoStepVerificationActivity.this.passwordSetupDetailRow || n == TwoStepVerificationActivity.this.passwordEnabledDetailRow) {
                return 1;
            }
            if (n == TwoStepVerificationActivity.this.passwordCodeFieldRow) {
                return 2;
            }
            return 0;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 0;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
            final int itemViewType = viewHolder.getItemViewType();
            boolean b = false;
            if (itemViewType != 0) {
                if (itemViewType == 1) {
                    final TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell)viewHolder.itemView;
                    if (n == TwoStepVerificationActivity.this.setPasswordDetailRow) {
                        textInfoPrivacyCell.setText(LocaleController.getString("SetAdditionalPasswordInfo", 2131560729));
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                    }
                    else {
                        final int access$1700 = TwoStepVerificationActivity.this.shadowRow;
                        String s = "";
                        if (n == access$1700) {
                            textInfoPrivacyCell.setText("");
                            textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                        }
                        else if (n == TwoStepVerificationActivity.this.passwordSetupDetailRow) {
                            if (TwoStepVerificationActivity.this.currentPassword != null && TwoStepVerificationActivity.this.currentPassword.has_password) {
                                if (TwoStepVerificationActivity.this.currentPassword.email_unconfirmed_pattern != null) {
                                    s = TwoStepVerificationActivity.this.currentPassword.email_unconfirmed_pattern;
                                }
                                textInfoPrivacyCell.setText(LocaleController.formatString("EmailPasswordConfirmText3", 2131559330, s));
                            }
                            else {
                                if (TwoStepVerificationActivity.this.currentPassword.email_unconfirmed_pattern != null) {
                                    s = TwoStepVerificationActivity.this.currentPassword.email_unconfirmed_pattern;
                                }
                                textInfoPrivacyCell.setText(LocaleController.formatString("EmailPasswordConfirmText2", 2131559329, s));
                            }
                            textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165396, "windowBackgroundGrayShadow"));
                        }
                        else if (n == TwoStepVerificationActivity.this.passwordEnabledDetailRow) {
                            textInfoPrivacyCell.setText(LocaleController.getString("EnabledPasswordText", 2131559350));
                            textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                        }
                    }
                }
            }
            else {
                final TextSettingsCell textSettingsCell = (TextSettingsCell)viewHolder.itemView;
                textSettingsCell.setTag((Object)"windowBackgroundWhiteBlackText");
                textSettingsCell.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                if (n == TwoStepVerificationActivity.this.changePasswordRow) {
                    textSettingsCell.setText(LocaleController.getString("ChangePassword", 2131558909), true);
                }
                else if (n == TwoStepVerificationActivity.this.setPasswordRow) {
                    textSettingsCell.setText(LocaleController.getString("SetAdditionalPassword", 2131560728), true);
                }
                else if (n == TwoStepVerificationActivity.this.turnPasswordOffRow) {
                    textSettingsCell.setText(LocaleController.getString("TurnPasswordOff", 2131560916), true);
                }
                else if (n == TwoStepVerificationActivity.this.changeRecoveryEmailRow) {
                    final String string = LocaleController.getString("ChangeRecoveryEmail", 2131558917);
                    if (TwoStepVerificationActivity.this.abortPasswordRow != -1) {
                        b = true;
                    }
                    textSettingsCell.setText(string, b);
                }
                else if (n == TwoStepVerificationActivity.this.resendCodeRow) {
                    textSettingsCell.setText(LocaleController.getString("ResendCode", 2131560581), true);
                }
                else if (n == TwoStepVerificationActivity.this.setRecoveryEmailRow) {
                    textSettingsCell.setText(LocaleController.getString("SetRecoveryEmail", 2131560736), false);
                }
                else if (n == TwoStepVerificationActivity.this.abortPasswordRow) {
                    textSettingsCell.setTag((Object)"windowBackgroundWhiteRedText3");
                    textSettingsCell.setTextColor(Theme.getColor("windowBackgroundWhiteRedText3"));
                    if (TwoStepVerificationActivity.this.currentPassword != null && TwoStepVerificationActivity.this.currentPassword.has_password) {
                        textSettingsCell.setText(LocaleController.getString("AbortEmail", 2131558401), false);
                    }
                    else {
                        textSettingsCell.setText(LocaleController.getString("AbortPassword", 2131558402), false);
                    }
                }
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            Object access$800;
            if (n != 0) {
                if (n != 1) {
                    final EditTextSettingsCell editTextSettingsCell = (EditTextSettingsCell)(access$800 = TwoStepVerificationActivity.this.codeFieldCell);
                    if (((View)editTextSettingsCell).getParent() != null) {
                        ((ViewGroup)((View)editTextSettingsCell).getParent()).removeView((View)editTextSettingsCell);
                        access$800 = editTextSettingsCell;
                    }
                }
                else {
                    access$800 = new TextInfoPrivacyCell(this.mContext);
                }
            }
            else {
                access$800 = new TextSettingsCell(this.mContext);
                ((View)access$800).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
            return new RecyclerListView.Holder((View)access$800);
        }
    }
}
