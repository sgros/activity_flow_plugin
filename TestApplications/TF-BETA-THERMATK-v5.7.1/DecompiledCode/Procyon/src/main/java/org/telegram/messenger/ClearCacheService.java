// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.content.Intent;
import android.util.SparseArray;
import java.io.File;
import android.app.IntentService;

public class ClearCacheService extends IntentService
{
    public ClearCacheService() {
        super("ClearCacheService");
    }
    
    protected void onHandleIntent(final Intent intent) {
        ApplicationLoader.postInitApplication();
        final int int1 = MessagesController.getGlobalMainSettings().getInt("keep_media", 2);
        if (int1 == 2) {
            return;
        }
        Utilities.globalQueue.postRunnable(new _$$Lambda$ClearCacheService$eaqMxt0ELhhnRq_8qc8rSbdFYt0(int1));
    }
}
