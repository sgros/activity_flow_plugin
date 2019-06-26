// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.Assistant;

import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.ServiceHandler;
import android.app.Activity;
import android.content.Context;

public class RunStore
{
    private static Context gAppContext;
    private static Activity gContext;
    private static ServiceHandler gService;
    
    public static Context getAppContext() {
        return RunStore.gAppContext;
    }
    
    public static Context getContext() {
        return (Context)RunStore.gContext;
    }
    
    public static ServiceHandler getServiceHandler() {
        if (RunStore.gService == null) {
            RunStore.gService = new ServiceHandler();
        }
        return RunStore.gService;
    }
    
    public static void setAppContext(final Context gAppContext) {
        RunStore.gAppContext = gAppContext;
    }
    
    public static void setContext(final Activity gContext) {
        RunStore.gContext = gContext;
    }
}
