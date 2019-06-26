package org.telegram.messenger.support.customtabs;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public abstract class CustomTabsServiceConnection implements ServiceConnection {
   public abstract void onCustomTabsServiceConnected(ComponentName var1, CustomTabsClient var2);

   public final void onServiceConnected(ComponentName var1, IBinder var2) {
      this.onCustomTabsServiceConnected(var1, new CustomTabsClient(ICustomTabsService.Stub.asInterface(var2), var1) {
      });
   }
}
