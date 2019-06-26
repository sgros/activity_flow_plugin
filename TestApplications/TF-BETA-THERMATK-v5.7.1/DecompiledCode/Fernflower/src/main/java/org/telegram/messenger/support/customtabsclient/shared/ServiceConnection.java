package org.telegram.messenger.support.customtabsclient.shared;

import android.content.ComponentName;
import java.lang.ref.WeakReference;
import org.telegram.messenger.support.customtabs.CustomTabsClient;
import org.telegram.messenger.support.customtabs.CustomTabsServiceConnection;

public class ServiceConnection extends CustomTabsServiceConnection {
   private WeakReference mConnectionCallback;

   public ServiceConnection(ServiceConnectionCallback var1) {
      this.mConnectionCallback = new WeakReference(var1);
   }

   public void onCustomTabsServiceConnected(ComponentName var1, CustomTabsClient var2) {
      ServiceConnectionCallback var3 = (ServiceConnectionCallback)this.mConnectionCallback.get();
      if (var3 != null) {
         var3.onServiceConnected(var2);
      }

   }

   public void onServiceDisconnected(ComponentName var1) {
      ServiceConnectionCallback var2 = (ServiceConnectionCallback)this.mConnectionCallback.get();
      if (var2 != null) {
         var2.onServiceDisconnected();
      }

   }
}
