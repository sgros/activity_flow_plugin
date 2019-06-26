package org.telegram.messenger.support.customtabs;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

public abstract class PostMessageServiceConnection implements ServiceConnection {
   private final Object mLock = new Object();
   private IPostMessageService mService;
   private final ICustomTabsCallback mSessionBinder;

   public PostMessageServiceConnection(CustomTabsSessionToken var1) {
      this.mSessionBinder = ICustomTabsCallback.Stub.asInterface(var1.getCallbackBinder());
   }

   public boolean bindSessionToPostMessageService(Context var1, String var2) {
      Intent var3 = new Intent();
      var3.setClassName(var2, PostMessageService.class.getName());
      return var1.bindService(var3, this, 1);
   }

   public final boolean notifyMessageChannelReady(Bundle var1) {
      if (this.mService == null) {
         return false;
      } else {
         Object var2 = this.mLock;
         synchronized(var2){}

         Throwable var10000;
         boolean var10001;
         label198: {
            label197: {
               try {
                  try {
                     this.mService.onMessageChannelReady(this.mSessionBinder, var1);
                     break label197;
                  } catch (RemoteException var26) {
                  }
               } catch (Throwable var27) {
                  var10000 = var27;
                  var10001 = false;
                  break label198;
               }

               try {
                  return false;
               } catch (Throwable var24) {
                  var10000 = var24;
                  var10001 = false;
                  break label198;
               }
            }

            label191:
            try {
               return true;
            } catch (Throwable var25) {
               var10000 = var25;
               var10001 = false;
               break label191;
            }
         }

         while(true) {
            Throwable var28 = var10000;

            try {
               throw var28;
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public void onPostMessageServiceConnected() {
   }

   public void onPostMessageServiceDisconnected() {
   }

   public final void onServiceConnected(ComponentName var1, IBinder var2) {
      this.mService = IPostMessageService.Stub.asInterface(var2);
      this.onPostMessageServiceConnected();
   }

   public final void onServiceDisconnected(ComponentName var1) {
      this.mService = null;
      this.onPostMessageServiceDisconnected();
   }

   public final boolean postMessage(String var1, Bundle var2) {
      if (this.mService == null) {
         return false;
      } else {
         Object var3 = this.mLock;
         synchronized(var3){}

         Throwable var10000;
         boolean var10001;
         label198: {
            label197: {
               try {
                  try {
                     this.mService.onPostMessage(this.mSessionBinder, var1, var2);
                     break label197;
                  } catch (RemoteException var27) {
                  }
               } catch (Throwable var28) {
                  var10000 = var28;
                  var10001 = false;
                  break label198;
               }

               try {
                  return false;
               } catch (Throwable var25) {
                  var10000 = var25;
                  var10001 = false;
                  break label198;
               }
            }

            label191:
            try {
               return true;
            } catch (Throwable var26) {
               var10000 = var26;
               var10001 = false;
               break label191;
            }
         }

         while(true) {
            Throwable var29 = var10000;

            try {
               throw var29;
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public void unbindFromContext(Context var1) {
      var1.unbindService(this);
   }
}
