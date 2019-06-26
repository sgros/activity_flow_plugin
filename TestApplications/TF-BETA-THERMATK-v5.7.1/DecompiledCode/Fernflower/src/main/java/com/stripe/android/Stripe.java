package com.stripe.android;

import android.os.AsyncTask;
import android.os.Build.VERSION;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.exception.StripeException;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.net.RequestOptions;
import com.stripe.android.net.StripeApiHandler;
import com.stripe.android.util.StripeNetworkUtils;
import java.util.concurrent.Executor;

public class Stripe {
   private String defaultPublishableKey;
   Stripe.TokenCreator tokenCreator = new Stripe.TokenCreator() {
      public void create(final Card var1, final String var2, Executor var3, final TokenCallback var4) {
         AsyncTask var5 = new AsyncTask() {
            protected Stripe.ResponseWrapper doInBackground(Void... var1x) {
               try {
                  RequestOptions var3 = RequestOptions.builder(var2).build();
                  Token var4x = StripeApiHandler.createToken(StripeNetworkUtils.hashMapFromCard(var1), var3);
                  Stripe.ResponseWrapper var5 = Stripe.this.new ResponseWrapper(var4x, (Exception)null);
                  return var5;
               } catch (StripeException var2x) {
                  return Stripe.this.new ResponseWrapper((Token)null, var2x);
               }
            }

            protected void onPostExecute(Stripe.ResponseWrapper var1x) {
               Stripe.this.tokenTaskPostExecution(var1x, var4);
            }
         };
         Stripe.this.executeTokenTask(var3, var5);
      }
   };

   public Stripe(String var1) throws AuthenticationException {
      this.setDefaultPublishableKey(var1);
   }

   private void executeTokenTask(Executor var1, AsyncTask var2) {
      if (var1 != null && VERSION.SDK_INT > 11) {
         var2.executeOnExecutor(var1, new Void[0]);
      } else {
         var2.execute(new Void[0]);
      }

   }

   private void tokenTaskPostExecution(Stripe.ResponseWrapper var1, TokenCallback var2) {
      Token var3 = var1.token;
      if (var3 != null) {
         var2.onSuccess(var3);
      } else {
         Exception var4 = var1.error;
         if (var4 != null) {
            var2.onError(var4);
         } else {
            var2.onError(new RuntimeException("Somehow got neither a token response or an error response"));
         }
      }

   }

   private void validateKey(String var1) throws AuthenticationException {
      Integer var2 = 0;
      if (var1 != null && var1.length() != 0) {
         if (var1.startsWith("sk_")) {
            throw new AuthenticationException("Invalid Publishable Key: You are using a secret key to create a token, instead of the publishable one. For more info, see https://stripe.com/docs/stripe.js", (String)null, var2);
         }
      } else {
         throw new AuthenticationException("Invalid Publishable Key: You must use a valid publishable key to create a token.  For more info, see https://stripe.com/docs/stripe.js.", (String)null, var2);
      }
   }

   public void createToken(Card var1, TokenCallback var2) {
      this.createToken(var1, this.defaultPublishableKey, var2);
   }

   public void createToken(Card var1, String var2, TokenCallback var3) {
      this.createToken(var1, var2, (Executor)null, var3);
   }

   public void createToken(Card var1, String var2, Executor var3, TokenCallback var4) {
      AuthenticationException var10000;
      boolean var10001;
      RuntimeException var8;
      if (var1 != null) {
         if (var4 != null) {
            try {
               this.validateKey(var2);
               this.tokenCreator.create(var1, var2, var3, var4);
               return;
            } catch (AuthenticationException var7) {
               var10000 = var7;
               var10001 = false;
            }
         } else {
            try {
               var8 = new RuntimeException("Required Parameter: 'callback' is required to use the created token and handle errors");
               throw var8;
            } catch (AuthenticationException var5) {
               var10000 = var5;
               var10001 = false;
            }
         }
      } else {
         try {
            var8 = new RuntimeException("Required Parameter: 'card' is required to create a token");
            throw var8;
         } catch (AuthenticationException var6) {
            var10000 = var6;
            var10001 = false;
         }
      }

      AuthenticationException var9 = var10000;
      var4.onError(var9);
   }

   public void setDefaultPublishableKey(String var1) throws AuthenticationException {
      this.validateKey(var1);
      this.defaultPublishableKey = var1;
   }

   private class ResponseWrapper {
      final Exception error;
      final Token token;

      private ResponseWrapper(Token var2, Exception var3) {
         this.error = var3;
         this.token = var2;
      }

      // $FF: synthetic method
      ResponseWrapper(Token var2, Exception var3, Object var4) {
         this(var2, var3);
      }
   }

   interface TokenCreator {
      void create(Card var1, String var2, Executor var3, TokenCallback var4);
   }
}
