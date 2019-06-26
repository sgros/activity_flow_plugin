// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.support.customtabsclient.shared;

import org.telegram.messenger.support.customtabs.CustomTabsClient;

public interface ServiceConnectionCallback
{
    void onServiceConnected(final CustomTabsClient p0);
    
    void onServiceDisconnected();
}
