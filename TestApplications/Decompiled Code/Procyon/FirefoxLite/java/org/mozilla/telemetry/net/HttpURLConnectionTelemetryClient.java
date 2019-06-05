// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.net;

import java.net.URLConnection;
import java.net.MalformedURLException;
import org.mozilla.telemetry.config.TelemetryConfiguration;
import java.io.OutputStream;
import java.io.Closeable;
import org.mozilla.telemetry.util.IOUtils;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Calendar;
import mozilla.components.support.base.log.logger.Logger;

public class HttpURLConnectionTelemetryClient implements TelemetryClient
{
    private Logger logger;
    
    public HttpURLConnectionTelemetryClient() {
        this.logger = new Logger("telemetry/client");
    }
    
    String createDateHeaderValue() {
        final Calendar instance = Calendar.getInstance();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return simpleDateFormat.format(instance.getTime());
    }
    
    HttpURLConnection openConnectionConnection(final String str, final String str2) throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(str2);
        return (HttpURLConnection)new URL(sb.toString()).openConnection();
    }
    
    int upload(final HttpURLConnection httpURLConnection, final String ex) throws IOException {
        Closeable closeable2;
        final Closeable closeable = closeable2 = null;
        try {
            try {
                closeable2 = closeable;
                closeable2 = closeable;
                final OutputStream outputStream = httpURLConnection.getOutputStream();
                try {
                    final BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                    bufferedWriter.write((String)ex);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    final int responseCode = httpURLConnection.getResponseCode();
                    IOUtils.safeClose(outputStream);
                    return responseCode;
                }
                catch (ArrayIndexOutOfBoundsException ex) {}
                finally {
                    closeable2 = outputStream;
                }
            }
            finally {}
        }
        catch (ArrayIndexOutOfBoundsException ex) {}
        throw new IOException(ex);
        IOUtils.safeClose(closeable2);
    }
    
    @Override
    public boolean uploadPing(final TelemetryConfiguration telemetryConfiguration, String openConnectionConnection, String ex) {
        final HttpURLConnection httpURLConnection = null;
        final HttpURLConnection httpURLConnection2 = null;
        final HttpURLConnection httpURLConnection3 = null;
        HttpURLConnection httpURLConnection5;
        try {
            try {
                openConnectionConnection = (String)this.openConnectionConnection(telemetryConfiguration.getServerEndpoint(), openConnectionConnection);
                try {
                    ((URLConnection)openConnectionConnection).setConnectTimeout(telemetryConfiguration.getConnectTimeout());
                    ((URLConnection)openConnectionConnection).setReadTimeout(telemetryConfiguration.getReadTimeout());
                    ((URLConnection)openConnectionConnection).setRequestProperty("Content-Type", "application/json; charset=utf-8");
                    ((URLConnection)openConnectionConnection).setRequestProperty("User-Agent", telemetryConfiguration.getUserAgent());
                    ((URLConnection)openConnectionConnection).setRequestProperty("Date", this.createDateHeaderValue());
                    ((HttpURLConnection)openConnectionConnection).setRequestMethod("POST");
                    ((URLConnection)openConnectionConnection).setDoOutput(true);
                    final int upload = this.upload((HttpURLConnection)openConnectionConnection, (String)ex);
                    final Logger logger = this.logger;
                    ex = (IOException)new StringBuilder();
                    ((StringBuilder)ex).append("Ping upload: ");
                    ((StringBuilder)ex).append(upload);
                    logger.debug(((StringBuilder)ex).toString(), null);
                    if (upload >= 200 && upload <= 299) {
                        if (openConnectionConnection != null) {
                            ((HttpURLConnection)openConnectionConnection).disconnect();
                        }
                        return true;
                    }
                    if (upload >= 400 && upload <= 499) {
                        final Logger logger2 = this.logger;
                        ex = (IOException)new StringBuilder();
                        ((StringBuilder)ex).append("Server returned client error code: ");
                        ((StringBuilder)ex).append(upload);
                        logger2.error(((StringBuilder)ex).toString(), null);
                        if (openConnectionConnection != null) {
                            ((HttpURLConnection)openConnectionConnection).disconnect();
                        }
                        return true;
                    }
                    final Logger logger3 = this.logger;
                    ex = (IOException)new StringBuilder();
                    ((StringBuilder)ex).append("Server returned response code: ");
                    ((StringBuilder)ex).append(upload);
                    logger3.warn(((StringBuilder)ex).toString(), null);
                    if (openConnectionConnection != null) {
                        ((HttpURLConnection)openConnectionConnection).disconnect();
                    }
                    return false;
                }
                catch (IOException ex) {}
                catch (MalformedURLException ex) {}
            }
            finally {
                final HttpURLConnection httpURLConnection4 = httpURLConnection3;
            }
        }
        catch (IOException httpURLConnection4) {
            httpURLConnection5 = httpURLConnection;
        }
        catch (MalformedURLException ex) {
            httpURLConnection5 = httpURLConnection2;
        }
        this.logger.error("Could not upload telemetry due to malformed URL", ex);
        if (httpURLConnection5 != null) {
            httpURLConnection5.disconnect();
        }
        return true;
        HttpURLConnection httpURLConnection4 = null;
        if (httpURLConnection4 != null) {
            httpURLConnection4.disconnect();
        }
    }
}
