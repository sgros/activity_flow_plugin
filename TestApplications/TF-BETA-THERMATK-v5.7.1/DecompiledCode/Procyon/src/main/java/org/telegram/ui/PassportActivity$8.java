// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.content.Context;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.messenger.LocaleController;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.SRPHelper;
import org.telegram.ui.ActionBar.BaseFragment;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.RequestDelegate;

class PassportActivity$8 implements RequestDelegate
{
    final /* synthetic */ PassportActivity this$0;
    final /* synthetic */ TLRPC.TL_account_getPasswordSettings val$req;
    final /* synthetic */ boolean val$saved;
    final /* synthetic */ String val$textPassword;
    final /* synthetic */ byte[] val$x_bytes;
    
    PassportActivity$8(final PassportActivity this$0, final boolean val$saved, final byte[] val$x_bytes, final TLRPC.TL_account_getPasswordSettings val$req, final String val$textPassword) {
        this.this$0 = this$0;
        this.val$saved = val$saved;
        this.val$x_bytes = val$x_bytes;
        this.val$req = val$req;
        this.val$textPassword = val$textPassword;
    }
    
    private void generateNewSecret() {
        Utilities.globalQueue.postRunnable(new _$$Lambda$PassportActivity$8$HOfaQC0wD4xlIJZiYrbf0Jd58uE(this, this.val$x_bytes, this.val$textPassword));
    }
    
    private void openRequestInterface() {
        if (this.this$0.inputFields == null) {
            return;
        }
        if (!this.val$saved) {
            UserConfig.getInstance(this.this$0.currentAccount).savePassword(this.val$x_bytes, this.this$0.saltedPassword);
        }
        AndroidUtilities.hideKeyboard((View)this.this$0.inputFields[0]);
        this.this$0.ignoreOnFailure = true;
        int n;
        if (this.this$0.currentBotId == 0) {
            n = 8;
        }
        else {
            n = 0;
        }
        final PassportActivity passportActivity = new PassportActivity(n, this.this$0.currentBotId, this.this$0.currentScope, this.this$0.currentPublicKey, this.this$0.currentPayload, this.this$0.currentNonce, this.this$0.currentCallbackUrl, this.this$0.currentForm, this.this$0.currentPassword);
        passportActivity.currentEmail = this.this$0.currentEmail;
        passportActivity.currentAccount = this.this$0.currentAccount;
        passportActivity.saltedPassword = this.this$0.saltedPassword;
        passportActivity.secureSecret = this.this$0.secureSecret;
        passportActivity.secureSecretId = this.this$0.secureSecretId;
        passportActivity.needActivityResult = this.this$0.needActivityResult;
        if (this.this$0.parentLayout != null && this.this$0.parentLayout.checkTransitionAnimation()) {
            this.this$0.presentAfterAnimation = passportActivity;
        }
        else {
            this.this$0.presentFragment(passportActivity, true);
        }
    }
    
    private void resetSecret() {
        final TLRPC.TL_account_updatePasswordSettings tl_account_updatePasswordSettings = new TLRPC.TL_account_updatePasswordSettings();
        if (this.this$0.currentPassword.current_algo instanceof TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
            tl_account_updatePasswordSettings.password = SRPHelper.startCheck(this.val$x_bytes, this.this$0.currentPassword.srp_id, this.this$0.currentPassword.srp_B, (TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow)this.this$0.currentPassword.current_algo);
        }
        tl_account_updatePasswordSettings.new_settings = new TLRPC.TL_account_passwordInputSettings();
        tl_account_updatePasswordSettings.new_settings.new_secure_settings = new TLRPC.TL_secureSecretSettings();
        final TLRPC.TL_secureSecretSettings new_secure_settings = tl_account_updatePasswordSettings.new_settings.new_secure_settings;
        new_secure_settings.secure_secret = new byte[0];
        new_secure_settings.secure_algo = new TLRPC.TL_securePasswordKdfAlgoUnknown();
        final TLRPC.TL_account_passwordInputSettings new_settings = tl_account_updatePasswordSettings.new_settings;
        new_settings.new_secure_settings.secure_secret_id = 0L;
        new_settings.flags |= 0x4;
        ConnectionsManager.getInstance(this.this$0.currentAccount).sendRequest(this.val$req, new _$$Lambda$PassportActivity$8$9UB99IqI_ykix5TDiaN6Iu5MpNI(this));
    }
    
    @Override
    public void run(final TLObject tlObject, final TLRPC.TL_error tl_error) {
        if (tl_error != null && "SRP_ID_INVALID".equals(tl_error.text)) {
            ConnectionsManager.getInstance(this.this$0.currentAccount).sendRequest(new TLRPC.TL_account_getPassword(), new _$$Lambda$PassportActivity$8$mGdIMbtzY2Oz5TSSjAmA4uEPy4U(this, this.val$saved), 8);
            return;
        }
        if (tl_error == null) {
            Utilities.globalQueue.postRunnable(new _$$Lambda$PassportActivity$8$uJcZ25nIuoS6ZH6mTt6S6kuhaAw(this, tlObject, this.val$textPassword, this.val$saved));
        }
        else {
            AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$8$f57rMytRL_jx0gvnoAw93D9zzYQ(this, this.val$saved, tl_error));
        }
    }
}
