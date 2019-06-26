// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import org.telegram.messenger.LocaleController;
import org.telegram.tgnet.TLRPC;

class PaymentFormActivity$12 implements PaymentFormActivityDelegate
{
    final /* synthetic */ PaymentFormActivity this$0;
    
    PaymentFormActivity$12(final PaymentFormActivity this$0) {
        this.this$0 = this$0;
    }
    
    @Override
    public void currentPasswordUpdated(final TLRPC.TL_account_password tl_account_password) {
    }
    
    @Override
    public boolean didSelectNewCard(final String s, final String s2, final boolean b, final TLRPC.TL_inputPaymentCredentialsAndroidPay tl_inputPaymentCredentialsAndroidPay) {
        this.this$0.paymentForm.saved_credentials = null;
        this.this$0.paymentJson = s;
        this.this$0.saveCardInfo = b;
        this.this$0.cardName = s2;
        this.this$0.androidPayCredentials = tl_inputPaymentCredentialsAndroidPay;
        this.this$0.detailSettingsCell[0].setTextAndValue(this.this$0.cardName, LocaleController.getString("PaymentCheckoutMethod", 2131560364), true);
        return false;
    }
    
    @Override
    public void onFragmentDestroyed() {
    }
}
