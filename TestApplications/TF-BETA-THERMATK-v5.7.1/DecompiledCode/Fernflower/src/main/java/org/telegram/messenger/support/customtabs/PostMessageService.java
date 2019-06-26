package org.telegram.messenger.support.customtabs;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

public class PostMessageService extends Service {
   private IPostMessageService.Stub mBinder = new IPostMessageService.Stub() {
      public void onMessageChannelReady(ICustomTabsCallback var1, Bundle var2) throws RemoteException {
         var1.onMessageChannelReady(var2);
      }

      public void onPostMessage(ICustomTabsCallback var1, String var2, Bundle var3) throws RemoteException {
         var1.onPostMessage(var2, var3);
      }
   };

   public IBinder onBind(Intent var1) {
      return this.mBinder;
   }
}
