package com.stripe.android;

import com.stripe.android.model.Token;

public interface TokenCallback {
   void onError(Exception var1);

   void onSuccess(Token var1);
}
