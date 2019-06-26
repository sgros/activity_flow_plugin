// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.support.customtabsclient.shared;

import org.telegram.messenger.support.customtabs.CustomTabsClient;
import android.content.ComponentName;
import java.lang.ref.WeakReference;
import org.telegram.messenger.support.customtabs.CustomTabsServiceConnection;

public class ServiceConnection extends CustomTabsServiceConnection
{
    private WeakReference<ServiceConnectionCallback> mConnectionCallback;
    
    public ServiceConnection(final ServiceConnectionCallback referent) {
        this.mConnectionCallback = new WeakReference<ServiceConnectionCallback>(referent);
    }
    
    @Override
    public void onCustomTabsServiceConnected(final ComponentName componentName, final CustomTabsClient customTabsClient) {
        final ServiceConnectionCallback serviceConnectionCallback = this.mConnectionCallback.get();
        if (serviceConnectionCallback != null) {
            serviceConnectionCallback.onServiceConnected(customTabsClient);
        }
    }
    
    public void onServiceDisconnected(final ComponentName componentName) {
        final ServiceConnectionCallback serviceConnectionCallback = this.mConnectionCallback.get();
        if (serviceConnectionCallback != null) {
            serviceConnectionCallback.onServiceDisconnected();
        }
    }
}
