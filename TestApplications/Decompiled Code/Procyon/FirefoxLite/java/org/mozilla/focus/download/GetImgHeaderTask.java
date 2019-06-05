// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.download;

import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;
import android.net.TrafficStats;
import android.os.AsyncTask;

public class GetImgHeaderTask extends AsyncTask<String, Void, String>
{
    public Callback callback;
    
    protected String doInBackground(String... array) {
        TrafficStats.setThreadStatsTag(10001);
        int n = 0;
        String s = null;
        Label_0134: {
            int responseCode;
            try {
                final HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(array[0]).openConnection();
                try {
                    try {
                        httpURLConnection.setRequestMethod("HEAD");
                        httpURLConnection.getContentType();
                        try {
                            responseCode = httpURLConnection.getResponseCode();
                            try {
                                httpURLConnection.disconnect();
                                n = responseCode;
                                if (httpURLConnection != null) {
                                    httpURLConnection.disconnect();
                                    n = responseCode;
                                    break Label_0134;
                                }
                                break Label_0134;
                            }
                            catch (IOException ex) {}
                        }
                        catch (IOException ex2) {
                            responseCode = n;
                        }
                    }
                    finally {}
                }
                catch (IOException s2) {
                    responseCode = n;
                }
            }
            catch (IOException s2) {
                s = "";
                array = null;
                responseCode = n;
            }
            finally {
                array = null;
                break Label_0134;
            }
            try {
                final String s2;
                ((Throwable)s2).printStackTrace();
                if (array != null) {
                    ((HttpURLConnection)(Object)array).disconnect();
                }
                s2 = s;
                n = responseCode;
                if (n == 200) {
                    return s2;
                }
                return null;
            }
            finally {}
        }
        if (array != null) {
            ((HttpURLConnection)(Object)array).disconnect();
        }
        throw s;
    }
    
    protected void onPostExecute(final String mimeType) {
        super.onPostExecute((Object)mimeType);
        this.callback.setMIMEType(mimeType);
    }
    
    public void setCallback(final Callback callback) {
        this.callback = callback;
    }
    
    public interface Callback
    {
        void setMIMEType(final String p0);
    }
}
