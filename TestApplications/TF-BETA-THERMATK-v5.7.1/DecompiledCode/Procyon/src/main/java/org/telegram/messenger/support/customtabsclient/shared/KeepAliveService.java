// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.support.customtabsclient.shared;

import android.os.IBinder;
import android.content.Intent;
import android.os.Binder;
import android.app.Service;

public class KeepAliveService extends Service
{
    private static final Binder sBinder;
    
    static {
        sBinder = new Binder();
    }
    
    public IBinder onBind(final Intent intent) {
        return (IBinder)KeepAliveService.sBinder;
    }
}
