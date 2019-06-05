package org.secuso.privacyfriendlynetmonitor.Assistant;

import android.app.Activity;
import android.content.Context;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.ServiceHandler;

public class RunStore {
    private static Context gAppContext;
    private static Activity gContext;
    private static ServiceHandler gService;

    public static void setContext(Activity activity) {
        gContext = activity;
    }

    public static Context getContext() {
        return gContext;
    }

    public static ServiceHandler getServiceHandler() {
        if (gService == null) {
            gService = new ServiceHandler();
        }
        return gService;
    }

    public static void setAppContext(Context context) {
        gAppContext = context;
    }

    public static Context getAppContext() {
        return gAppContext;
    }
}
