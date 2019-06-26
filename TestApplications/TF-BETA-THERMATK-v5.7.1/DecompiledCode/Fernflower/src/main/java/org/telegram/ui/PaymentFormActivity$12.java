package org.telegram.ui;

import org.telegram.messenger.LocaleController;
import org.telegram.tgnet.TLRPC;

class PaymentFormActivity$12 implements PaymentFormActivity.PaymentFormActivityDelegate {
   // $FF: synthetic field
   final PaymentFormActivity this$0;

   PaymentFormActivity$12(PaymentFormActivity var1) {
      this.this$0 = var1;
   }

   public void currentPasswordUpdated(TLRPC.TL_account_password var1) {
   }

   public boolean didSelectNewCard(String var1, String var2, boolean var3, TLRPC.TL_inputPaymentCredentialsAndroidPay var4) {
      PaymentFormActivity.access$3900(this.this$0).saved_credentials = null;
      PaymentFormActivity.access$002(this.this$0, var1);
      PaymentFormActivity.access$4002(this.this$0, var3);
      PaymentFormActivity.access$102(this.this$0, var2);
      PaymentFormActivity.access$4102(this.this$0, var4);
      PaymentFormActivity.access$4200(this.this$0)[0].setTextAndValue(PaymentFormActivity.access$100(this.this$0), LocaleController.getString("PaymentCheckoutMethod", 2131560364), true);
      return false;
   }

   public void onFragmentDestroyed() {
   }
}
