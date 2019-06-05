// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.httprequest;

import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;

public class HttpRequest
{
    private static BufferedReader createReader(final InputStream in) throws IOException {
        return new BufferedReader(new InputStreamReader(new BufferedInputStream(in), "utf-8"));
    }
    
    public static String get(final URL url, final int connectTimeout, String lines) {
        HttpURLConnection httpURLConnection2 = null;
        try {
            final HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            try {
                httpURLConnection.setRequestProperty("User-Agent", (String)lines);
                if (connectTimeout > 0) {
                    httpURLConnection.setConnectTimeout(connectTimeout);
                }
                final String s = (String)(lines = readLines(httpURLConnection));
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                    lines = s;
                    return (String)lines;
                }
                return (String)lines;
            }
            catch (IOException ex) {}
        }
        catch (IOException ex2) {
            httpURLConnection2 = null;
        }
        finally {
            httpURLConnection2 = null;
        }
        if (httpURLConnection2 != null) {
            httpURLConnection2.disconnect();
        }
        lines = "";
        return (String)lines;
    }
    
    public static String get(final URL url, final String s) {
        return get(url, 2000, s);
    }
    
    private static String readLines(URLConnection urlConnection) throws IOException {
        try {
            final InputStream inputStream = urlConnection.getInputStream();
            final StringBuilder sb = new StringBuilder();
            final BufferedReader reader = createReader(inputStream);
            final URLConnection urlConnection2 = null;
            while (true) {
                urlConnection = urlConnection2;
                try {
                    try {
                        final String line = reader.readLine();
                        if (line != null) {
                            urlConnection = urlConnection2;
                            sb.append(line);
                            urlConnection = urlConnection2;
                            sb.append('\n');
                            continue;
                        }
                        if (reader != null) {
                            reader.close();
                        }
                        return sb.toString();
                    }
                    finally {
                        if (reader != null) {
                            if (urlConnection != null) {
                                final BufferedReader bufferedReader = reader;
                                bufferedReader.close();
                            }
                            else {
                                reader.close();
                            }
                        }
                    }
                }
                catch (Throwable t) {}
                try {
                    final BufferedReader bufferedReader = reader;
                    bufferedReader.close();
                }
                catch (Throwable t2) {}
            }
        }
        catch (IndexOutOfBoundsException ex) {
            return "";
        }
    }
}
