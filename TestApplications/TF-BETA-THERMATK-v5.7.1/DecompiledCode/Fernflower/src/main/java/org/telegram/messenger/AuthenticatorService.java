package org.telegram.messenger;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class AuthenticatorService extends Service {
   private static AuthenticatorService.Authenticator authenticator;

   protected AuthenticatorService.Authenticator getAuthenticator() {
      if (authenticator == null) {
         authenticator = new AuthenticatorService.Authenticator(this);
      }

      return authenticator;
   }

   public IBinder onBind(Intent var1) {
      return var1.getAction().equals("android.accounts.AccountAuthenticator") ? this.getAuthenticator().getIBinder() : null;
   }

   private static class Authenticator extends AbstractAccountAuthenticator {
      private final Context context;

      public Authenticator(Context var1) {
         super(var1);
         this.context = var1;
      }

      public Bundle addAccount(AccountAuthenticatorResponse var1, String var2, String var3, String[] var4, Bundle var5) throws NetworkErrorException {
         return null;
      }

      public Bundle confirmCredentials(AccountAuthenticatorResponse var1, Account var2, Bundle var3) throws NetworkErrorException {
         return null;
      }

      public Bundle editProperties(AccountAuthenticatorResponse var1, String var2) {
         return null;
      }

      public Bundle getAccountRemovalAllowed(AccountAuthenticatorResponse var1, Account var2) throws NetworkErrorException {
         return super.getAccountRemovalAllowed(var1, var2);
      }

      public Bundle getAuthToken(AccountAuthenticatorResponse var1, Account var2, String var3, Bundle var4) throws NetworkErrorException {
         return null;
      }

      public String getAuthTokenLabel(String var1) {
         return null;
      }

      public Bundle hasFeatures(AccountAuthenticatorResponse var1, Account var2, String[] var3) throws NetworkErrorException {
         return null;
      }

      public Bundle updateCredentials(AccountAuthenticatorResponse var1, Account var2, String var3, Bundle var4) throws NetworkErrorException {
         return null;
      }
   }
}
