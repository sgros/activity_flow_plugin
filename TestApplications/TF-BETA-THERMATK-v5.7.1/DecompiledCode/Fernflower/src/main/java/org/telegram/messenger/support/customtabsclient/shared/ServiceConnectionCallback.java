package org.telegram.messenger.support.customtabsclient.shared;

import org.telegram.messenger.support.customtabs.CustomTabsClient;

public interface ServiceConnectionCallback {
   void onServiceConnected(CustomTabsClient var1);

   void onServiceDisconnected();
}
