// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.content.AbstractThreadedSyncAdapter;
import android.os.IBinder;
import android.content.Intent;
import android.accounts.OperationCanceledException;
import android.content.SyncResult;
import android.content.ContentProviderClient;
import android.os.Bundle;
import android.accounts.Account;
import android.content.Context;
import android.app.Service;

public class ContactsSyncAdapterService extends Service
{
    private static SyncAdapterImpl sSyncAdapter;
    
    private SyncAdapterImpl getSyncAdapter() {
        if (ContactsSyncAdapterService.sSyncAdapter == null) {
            ContactsSyncAdapterService.sSyncAdapter = new SyncAdapterImpl((Context)this);
        }
        return ContactsSyncAdapterService.sSyncAdapter;
    }
    
    private static void performSync(final Context context, final Account account, final Bundle bundle, final String s, final ContentProviderClient contentProviderClient, final SyncResult syncResult) throws OperationCanceledException {
        if (BuildVars.LOGS_ENABLED) {
            final StringBuilder sb = new StringBuilder();
            sb.append("performSync: ");
            sb.append(account.toString());
            FileLog.d(sb.toString());
        }
    }
    
    public IBinder onBind(final Intent intent) {
        return this.getSyncAdapter().getSyncAdapterBinder();
    }
    
    private static class SyncAdapterImpl extends AbstractThreadedSyncAdapter
    {
        private Context mContext;
        
        public SyncAdapterImpl(final Context mContext) {
            super(mContext, true);
            this.mContext = mContext;
        }
        
        public void onPerformSync(final Account account, final Bundle bundle, final String s, final ContentProviderClient contentProviderClient, final SyncResult syncResult) {
            try {
                performSync(this.mContext, account, bundle, s, contentProviderClient, syncResult);
            }
            catch (OperationCanceledException ex) {
                FileLog.e((Throwable)ex);
            }
        }
    }
}
