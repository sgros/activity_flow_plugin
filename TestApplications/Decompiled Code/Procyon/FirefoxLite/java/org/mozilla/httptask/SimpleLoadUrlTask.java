// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.httptask;

import java.net.MalformedURLException;
import org.mozilla.httprequest.HttpRequest;
import java.net.URL;
import android.net.TrafficStats;
import android.os.AsyncTask;

public class SimpleLoadUrlTask extends AsyncTask<String, Void, String>
{
    protected String doInBackground(final String... array) {
        try {
            TrafficStats.setThreadStatsTag(Integer.parseInt(array[2]));
            try {
                return HttpRequest.get(new URL(array[0]), array[1]);
            }
            catch (MalformedURLException ex) {
                throw new IllegalArgumentException("MalformedURLException");
            }
        }
        catch (NumberFormatException ex2) {
            throw new IllegalArgumentException("Socket Tag should be a number");
        }
    }
}
