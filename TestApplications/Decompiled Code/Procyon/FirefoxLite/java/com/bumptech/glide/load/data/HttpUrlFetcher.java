// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.data;

import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import java.util.Iterator;
import java.net.URISyntaxException;
import com.bumptech.glide.load.HttpException;
import java.util.Map;
import java.net.URL;
import java.io.IOException;
import android.util.Log;
import com.bumptech.glide.util.ContentLengthInputStream;
import android.text.TextUtils;
import java.net.HttpURLConnection;
import com.bumptech.glide.load.model.GlideUrl;
import java.io.InputStream;

public class HttpUrlFetcher implements DataFetcher<InputStream>
{
    static final HttpUrlConnectionFactory DEFAULT_CONNECTION_FACTORY;
    private final HttpUrlConnectionFactory connectionFactory;
    private final GlideUrl glideUrl;
    private volatile boolean isCancelled;
    private InputStream stream;
    private final int timeout;
    private HttpURLConnection urlConnection;
    
    static {
        DEFAULT_CONNECTION_FACTORY = (HttpUrlConnectionFactory)new DefaultHttpUrlConnectionFactory();
    }
    
    public HttpUrlFetcher(final GlideUrl glideUrl, final int n) {
        this(glideUrl, n, HttpUrlFetcher.DEFAULT_CONNECTION_FACTORY);
    }
    
    HttpUrlFetcher(final GlideUrl glideUrl, final int timeout, final HttpUrlConnectionFactory connectionFactory) {
        this.glideUrl = glideUrl;
        this.timeout = timeout;
        this.connectionFactory = connectionFactory;
    }
    
    private InputStream getStreamForSuccessfulRequest(final HttpURLConnection httpURLConnection) throws IOException {
        if (TextUtils.isEmpty((CharSequence)httpURLConnection.getContentEncoding())) {
            this.stream = ContentLengthInputStream.obtain(httpURLConnection.getInputStream(), httpURLConnection.getContentLength());
        }
        else {
            if (Log.isLoggable("HttpUrlFetcher", 3)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Got non empty content encoding: ");
                sb.append(httpURLConnection.getContentEncoding());
                Log.d("HttpUrlFetcher", sb.toString());
            }
            this.stream = httpURLConnection.getInputStream();
        }
        return this.stream;
    }
    
    private InputStream loadDataWithRedirects(final URL context, final int n, final URL url, final Map<String, String> map) throws IOException {
        Label_0311: {
            if (n >= 5) {
                break Label_0311;
            }
            while (true) {
                if (url == null) {
                    break Label_0038;
                }
                try {
                    if (context.toURI().equals(url.toURI())) {
                        throw new HttpException("In re-direct loop");
                    }
                    this.urlConnection = this.connectionFactory.build(context);
                    for (final Map.Entry<String, String> entry : map.entrySet()) {
                        this.urlConnection.addRequestProperty(entry.getKey(), entry.getValue());
                    }
                    this.urlConnection.setConnectTimeout(this.timeout);
                    this.urlConnection.setReadTimeout(this.timeout);
                    this.urlConnection.setUseCaches(false);
                    this.urlConnection.setDoInput(true);
                    this.urlConnection.setInstanceFollowRedirects(false);
                    this.urlConnection.connect();
                    this.stream = this.urlConnection.getInputStream();
                    if (this.isCancelled) {
                        return null;
                    }
                    final int responseCode = this.urlConnection.getResponseCode();
                    final int n2 = responseCode / 100;
                    if (n2 == 2) {
                        return this.getStreamForSuccessfulRequest(this.urlConnection);
                    }
                    if (n2 == 3) {
                        final String headerField = this.urlConnection.getHeaderField("Location");
                        if (!TextUtils.isEmpty((CharSequence)headerField)) {
                            final URL url2 = new URL(context, headerField);
                            this.cleanup();
                            return this.loadDataWithRedirects(url2, n + 1, context, map);
                        }
                        throw new HttpException("Received empty or null redirect url");
                    }
                    else {
                        if (responseCode == -1) {
                            throw new HttpException(responseCode);
                        }
                        throw new HttpException(this.urlConnection.getResponseMessage(), responseCode);
                    }
                    throw new HttpException("Too many (> 5) redirects!");
                }
                catch (URISyntaxException ex) {
                    continue;
                }
                break;
            }
        }
    }
    
    @Override
    public void cancel() {
        this.isCancelled = true;
    }
    
    @Override
    public void cleanup() {
        if (this.stream != null) {
            try {
                this.stream.close();
            }
            catch (IOException ex) {}
        }
        if (this.urlConnection != null) {
            this.urlConnection.disconnect();
        }
        this.urlConnection = null;
    }
    
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }
    
    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }
    
    @Override
    public void loadData(final Priority priority, final DataCallback<? super InputStream> dataCallback) {
        final long logTime = LogTime.getLogTime();
        try {
            final InputStream loadDataWithRedirects = this.loadDataWithRedirects(this.glideUrl.toURL(), 0, null, this.glideUrl.getHeaders());
            if (Log.isLoggable("HttpUrlFetcher", 2)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Finished http url fetcher fetch in ");
                sb.append(LogTime.getElapsedMillis(logTime));
                sb.append(" ms and loaded ");
                sb.append(loadDataWithRedirects);
                Log.v("HttpUrlFetcher", sb.toString());
            }
            dataCallback.onDataReady(loadDataWithRedirects);
        }
        catch (IOException ex) {
            if (Log.isLoggable("HttpUrlFetcher", 3)) {
                Log.d("HttpUrlFetcher", "Failed to load data for url", (Throwable)ex);
            }
            dataCallback.onLoadFailed(ex);
        }
    }
    
    private static class DefaultHttpUrlConnectionFactory implements HttpUrlConnectionFactory
    {
        DefaultHttpUrlConnectionFactory() {
        }
        
        @Override
        public HttpURLConnection build(final URL url) throws IOException {
            return (HttpURLConnection)url.openConnection();
        }
    }
    
    interface HttpUrlConnectionFactory
    {
        HttpURLConnection build(final URL p0) throws IOException;
    }
}
