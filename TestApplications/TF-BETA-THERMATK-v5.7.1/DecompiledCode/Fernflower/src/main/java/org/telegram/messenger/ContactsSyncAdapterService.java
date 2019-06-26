package org.telegram.messenger;

import android.accounts.Account;
import android.accounts.OperationCanceledException;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.IBinder;

public class ContactsSyncAdapterService extends Service {
   private static ContactsSyncAdapterService.SyncAdapterImpl sSyncAdapter;

   private ContactsSyncAdapterService.SyncAdapterImpl getSyncAdapter() {
      if (sSyncAdapter == null) {
         sSyncAdapter = new ContactsSyncAdapterService.SyncAdapterImpl(this);
      }

      return sSyncAdapter;
   }

   private static void performSync(Context var0, Account var1, Bundle var2, String var3, ContentProviderClient var4, SyncResult var5) throws OperationCanceledException {
      if (BuildVars.LOGS_ENABLED) {
         StringBuilder var6 = new StringBuilder();
         var6.append("performSync: ");
         var6.append(var1.toString());
         FileLog.d(var6.toString());
      }

   }

   public IBinder onBind(Intent var1) {
      return this.getSyncAdapter().getSyncAdapterBinder();
   }

   private static class SyncAdapterImpl extends AbstractThreadedSyncAdapter {
      private Context mContext;

      public SyncAdapterImpl(Context var1) {
         super(var1, true);
         this.mContext = var1;
      }

      public void onPerformSync(Account var1, Bundle var2, String var3, ContentProviderClient var4, SyncResult var5) {
         try {
            ContactsSyncAdapterService.performSync(this.mContext, var1, var2, var3, var4, var5);
         } catch (OperationCanceledException var6) {
            FileLog.e((Throwable)var6);
         }

      }
   }
}
