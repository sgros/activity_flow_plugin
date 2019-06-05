package com.bumptech.glide.load.data;

import android.text.TextUtils;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.HttpException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.util.ContentLengthInputStream;
import com.bumptech.glide.util.LogTime;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUrlFetcher implements DataFetcher {
   static final HttpUrlFetcher.HttpUrlConnectionFactory DEFAULT_CONNECTION_FACTORY = new HttpUrlFetcher.DefaultHttpUrlConnectionFactory();
   private final HttpUrlFetcher.HttpUrlConnectionFactory connectionFactory;
   private final GlideUrl glideUrl;
   private volatile boolean isCancelled;
   private InputStream stream;
   private final int timeout;
   private HttpURLConnection urlConnection;

   public HttpUrlFetcher(GlideUrl var1, int var2) {
      this(var1, var2, DEFAULT_CONNECTION_FACTORY);
   }

   HttpUrlFetcher(GlideUrl var1, int var2, HttpUrlFetcher.HttpUrlConnectionFactory var3) {
      this.glideUrl = var1;
      this.timeout = var2;
      this.connectionFactory = var3;
   }

   private InputStream getStreamForSuccessfulRequest(HttpURLConnection var1) throws IOException {
      if (TextUtils.isEmpty(var1.getContentEncoding())) {
         int var2 = var1.getContentLength();
         this.stream = ContentLengthInputStream.obtain(var1.getInputStream(), (long)var2);
      } else {
         if (Log.isLoggable("HttpUrlFetcher", 3)) {
            StringBuilder var3 = new StringBuilder();
            var3.append("Got non empty content encoding: ");
            var3.append(var1.getContentEncoding());
            Log.d("HttpUrlFetcher", var3.toString());
         }

         this.stream = var1.getInputStream();
      }

      return this.stream;
   }

   private InputStream loadDataWithRedirects(URL var1, int var2, URL var3, Map var4) throws IOException {
      if (var2 >= 5) {
         throw new HttpException("Too many (> 5) redirects!");
      } else {
         if (var3 != null) {
            try {
               if (var1.toURI().equals(var3.toURI())) {
                  HttpException var11 = new HttpException("In re-direct loop");
                  throw var11;
               }
            } catch (URISyntaxException var8) {
            }
         }

         this.urlConnection = this.connectionFactory.build(var1);
         Iterator var9 = var4.entrySet().iterator();

         while(var9.hasNext()) {
            Entry var5 = (Entry)var9.next();
            this.urlConnection.addRequestProperty((String)var5.getKey(), (String)var5.getValue());
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
         } else {
            int var6 = this.urlConnection.getResponseCode();
            int var7 = var6 / 100;
            if (var7 == 2) {
               return this.getStreamForSuccessfulRequest(this.urlConnection);
            } else if (var7 == 3) {
               String var10 = this.urlConnection.getHeaderField("Location");
               if (!TextUtils.isEmpty(var10)) {
                  var3 = new URL(var1, var10);
                  this.cleanup();
                  return this.loadDataWithRedirects(var3, var2 + 1, var1, var4);
               } else {
                  throw new HttpException("Received empty or null redirect url");
               }
            } else if (var6 == -1) {
               throw new HttpException(var6);
            } else {
               throw new HttpException(this.urlConnection.getResponseMessage(), var6);
            }
         }
      }
   }

   public void cancel() {
      this.isCancelled = true;
   }

   public void cleanup() {
      if (this.stream != null) {
         try {
            this.stream.close();
         } catch (IOException var2) {
         }
      }

      if (this.urlConnection != null) {
         this.urlConnection.disconnect();
      }

      this.urlConnection = null;
   }

   public Class getDataClass() {
      return InputStream.class;
   }

   public DataSource getDataSource() {
      return DataSource.REMOTE;
   }

   public void loadData(Priority var1, DataFetcher.DataCallback var2) {
      long var3 = LogTime.getLogTime();

      InputStream var5;
      try {
         var5 = this.loadDataWithRedirects(this.glideUrl.toURL(), 0, (URL)null, this.glideUrl.getHeaders());
      } catch (IOException var6) {
         if (Log.isLoggable("HttpUrlFetcher", 3)) {
            Log.d("HttpUrlFetcher", "Failed to load data for url", var6);
         }

         var2.onLoadFailed(var6);
         return;
      }

      if (Log.isLoggable("HttpUrlFetcher", 2)) {
         StringBuilder var7 = new StringBuilder();
         var7.append("Finished http url fetcher fetch in ");
         var7.append(LogTime.getElapsedMillis(var3));
         var7.append(" ms and loaded ");
         var7.append(var5);
         Log.v("HttpUrlFetcher", var7.toString());
      }

      var2.onDataReady(var5);
   }

   private static class DefaultHttpUrlConnectionFactory implements HttpUrlFetcher.HttpUrlConnectionFactory {
      DefaultHttpUrlConnectionFactory() {
      }

      public HttpURLConnection build(URL var1) throws IOException {
         return (HttpURLConnection)var1.openConnection();
      }
   }

   interface HttpUrlConnectionFactory {
      HttpURLConnection build(URL var1) throws IOException;
   }
}
