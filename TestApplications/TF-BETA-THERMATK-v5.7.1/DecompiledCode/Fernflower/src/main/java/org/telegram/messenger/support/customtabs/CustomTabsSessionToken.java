package org.telegram.messenger.support.customtabs;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import androidx.core.app.BundleCompat;

public class CustomTabsSessionToken {
   private static final String TAG = "CustomTabsSessionToken";
   private final CustomTabsCallback mCallback;
   private final ICustomTabsCallback mCallbackBinder;

   CustomTabsSessionToken(ICustomTabsCallback var1) {
      this.mCallbackBinder = var1;
      this.mCallback = new CustomTabsCallback() {
         public void extraCallback(String var1, Bundle var2) {
            try {
               CustomTabsSessionToken.this.mCallbackBinder.extraCallback(var1, var2);
            } catch (RemoteException var3) {
               Log.e("CustomTabsSessionToken", "RemoteException during ICustomTabsCallback transaction");
            }

         }

         public void onMessageChannelReady(Bundle var1) {
            try {
               CustomTabsSessionToken.this.mCallbackBinder.onMessageChannelReady(var1);
            } catch (RemoteException var2) {
               Log.e("CustomTabsSessionToken", "RemoteException during ICustomTabsCallback transaction");
            }

         }

         public void onNavigationEvent(int var1, Bundle var2) {
            try {
               CustomTabsSessionToken.this.mCallbackBinder.onNavigationEvent(var1, var2);
            } catch (RemoteException var3) {
               Log.e("CustomTabsSessionToken", "RemoteException during ICustomTabsCallback transaction");
            }

         }

         public void onPostMessage(String var1, Bundle var2) {
            try {
               CustomTabsSessionToken.this.mCallbackBinder.onPostMessage(var1, var2);
            } catch (RemoteException var3) {
               Log.e("CustomTabsSessionToken", "RemoteException during ICustomTabsCallback transaction");
            }

         }
      };
   }

   public static CustomTabsSessionToken createDummySessionTokenForTesting() {
      return new CustomTabsSessionToken(new CustomTabsSessionToken.DummyCallback());
   }

   public static CustomTabsSessionToken getSessionTokenFromIntent(Intent var0) {
      IBinder var1 = BundleCompat.getBinder(var0.getExtras(), "android.support.customtabs.extra.SESSION");
      return var1 == null ? null : new CustomTabsSessionToken(ICustomTabsCallback.Stub.asInterface(var1));
   }

   public boolean equals(Object var1) {
      return !(var1 instanceof CustomTabsSessionToken) ? false : ((CustomTabsSessionToken)var1).getCallbackBinder().equals(this.mCallbackBinder.asBinder());
   }

   public CustomTabsCallback getCallback() {
      return this.mCallback;
   }

   IBinder getCallbackBinder() {
      return this.mCallbackBinder.asBinder();
   }

   public int hashCode() {
      return this.getCallbackBinder().hashCode();
   }

   public boolean isAssociatedWith(CustomTabsSession var1) {
      return var1.getBinder().equals(this.mCallbackBinder);
   }

   static class DummyCallback extends ICustomTabsCallback.Stub {
      public IBinder asBinder() {
         return this;
      }

      public void extraCallback(String var1, Bundle var2) {
      }

      public void onMessageChannelReady(Bundle var1) {
      }

      public void onNavigationEvent(int var1, Bundle var2) {
      }

      public void onPostMessage(String var1, Bundle var2) {
      }
   }
}
