package org.mozilla.httptask;

import android.net.TrafficStats;
import android.os.AsyncTask;
import java.net.MalformedURLException;
import java.net.URL;
import org.mozilla.httprequest.HttpRequest;

public class SimpleLoadUrlTask extends AsyncTask<String, Void, String> {
    /* Access modifiers changed, original: protected|varargs */
    public String doInBackground(String... strArr) {
        try {
            TrafficStats.setThreadStatsTag(Integer.parseInt(strArr[2]));
            try {
                return HttpRequest.get(new URL(strArr[0]), strArr[1]);
            } catch (MalformedURLException unused) {
                throw new IllegalArgumentException("MalformedURLException");
            }
        } catch (NumberFormatException unused2) {
            throw new IllegalArgumentException("Socket Tag should be a number");
        }
    }
}
