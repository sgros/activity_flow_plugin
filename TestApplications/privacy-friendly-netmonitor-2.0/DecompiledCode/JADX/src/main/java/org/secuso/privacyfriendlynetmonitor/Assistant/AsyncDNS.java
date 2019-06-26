package org.secuso.privacyfriendlynetmonitor.Assistant;

import android.os.AsyncTask;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Collector;

public class AsyncDNS extends AsyncTask<String, Void, String> {
    /* Access modifiers changed, original: protected|varargs */
    public String doInBackground(String... strArr) {
        Collector.resolveHosts();
        return "Executed!";
    }
}
