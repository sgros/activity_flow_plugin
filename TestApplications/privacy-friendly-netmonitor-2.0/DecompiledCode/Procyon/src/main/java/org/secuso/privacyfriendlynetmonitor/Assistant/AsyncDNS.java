// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.Assistant;

import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Collector;
import android.os.AsyncTask;

public class AsyncDNS extends AsyncTask<String, Void, String>
{
    protected String doInBackground(final String... array) {
        Collector.resolveHosts();
        return "Executed!";
    }
}
