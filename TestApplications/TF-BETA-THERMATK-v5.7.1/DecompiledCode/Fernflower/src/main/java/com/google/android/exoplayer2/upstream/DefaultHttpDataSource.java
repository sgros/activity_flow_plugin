package com.google.android.exoplayer2.upstream;

import android.net.Uri;
import android.text.TextUtils;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Predicate;
import com.google.android.exoplayer2.util.Util;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.NoRouteToHostException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultHttpDataSource extends BaseDataSource implements HttpDataSource {
   private static final Pattern CONTENT_RANGE_HEADER = Pattern.compile("^bytes (\\d+)-(\\d+)/(\\d+)$");
   private static final AtomicReference skipBufferReference = new AtomicReference();
   private final boolean allowCrossProtocolRedirects;
   private long bytesRead;
   private long bytesSkipped;
   private long bytesToRead;
   private long bytesToSkip;
   private final int connectTimeoutMillis;
   private HttpURLConnection connection;
   private final Predicate contentTypePredicate;
   private DataSpec dataSpec;
   private final HttpDataSource.RequestProperties defaultRequestProperties;
   private InputStream inputStream;
   private boolean opened;
   private final int readTimeoutMillis;
   private final HttpDataSource.RequestProperties requestProperties;
   private final String userAgent;

   public DefaultHttpDataSource(String var1, Predicate var2, int var3, int var4, boolean var5, HttpDataSource.RequestProperties var6) {
      super(true);
      Assertions.checkNotEmpty(var1);
      this.userAgent = var1;
      this.contentTypePredicate = var2;
      this.requestProperties = new HttpDataSource.RequestProperties();
      this.connectTimeoutMillis = var3;
      this.readTimeoutMillis = var4;
      this.allowCrossProtocolRedirects = var5;
      this.defaultRequestProperties = var6;
   }

   @Deprecated
   public DefaultHttpDataSource(String var1, Predicate var2, TransferListener var3, int var4, int var5, boolean var6, HttpDataSource.RequestProperties var7) {
      this(var1, var2, var4, var5, var6, var7);
      if (var3 != null) {
         this.addTransferListener(var3);
      }

   }

   private void closeConnectionQuietly() {
      HttpURLConnection var1 = this.connection;
      if (var1 != null) {
         try {
            var1.disconnect();
         } catch (Exception var2) {
            Log.e("DefaultHttpDataSource", "Unexpected error while disconnecting", var2);
         }

         this.connection = null;
      }

   }

   private static long getContentLength(HttpURLConnection var0) {
      String var1;
      long var2;
      StringBuilder var4;
      label43: {
         var1 = var0.getHeaderField("Content-Length");
         if (!TextUtils.isEmpty(var1)) {
            try {
               var2 = Long.parseLong(var1);
               break label43;
            } catch (NumberFormatException var11) {
               var4 = new StringBuilder();
               var4.append("Unexpected Content-Length [");
               var4.append(var1);
               var4.append("]");
               Log.e("DefaultHttpDataSource", var4.toString());
            }
         }

         var2 = -1L;
      }

      String var12 = var0.getHeaderField("Content-Range");
      long var5 = var2;
      if (!TextUtils.isEmpty(var12)) {
         Matcher var14 = CONTENT_RANGE_HEADER.matcher(var12);
         var5 = var2;
         if (var14.find()) {
            label46: {
               boolean var10001;
               long var7;
               try {
                  var7 = Long.parseLong(var14.group(2)) - Long.parseLong(var14.group(1)) + 1L;
               } catch (NumberFormatException var10) {
                  var10001 = false;
                  break label46;
               }

               if (var2 < 0L) {
                  var5 = var7;
                  return var5;
               }

               var5 = var2;
               if (var2 == var7) {
                  return var5;
               }

               try {
                  var4 = new StringBuilder();
                  var4.append("Inconsistent headers [");
                  var4.append(var1);
                  var4.append("] [");
                  var4.append(var12);
                  var4.append("]");
                  Log.w("DefaultHttpDataSource", var4.toString());
                  var5 = Math.max(var2, var7);
                  return var5;
               } catch (NumberFormatException var9) {
                  var10001 = false;
               }
            }

            StringBuilder var13 = new StringBuilder();
            var13.append("Unexpected Content-Range [");
            var13.append(var12);
            var13.append("]");
            Log.e("DefaultHttpDataSource", var13.toString());
            var5 = var2;
         }
      }

      return var5;
   }

   private static URL handleRedirect(URL var0, String var1) throws IOException {
      if (var1 != null) {
         URL var3 = new URL(var0, var1);
         String var2 = var3.getProtocol();
         if (!"https".equals(var2) && !"http".equals(var2)) {
            StringBuilder var4 = new StringBuilder();
            var4.append("Unsupported protocol redirect: ");
            var4.append(var2);
            throw new ProtocolException(var4.toString());
         } else {
            return var3;
         }
      } else {
         throw new ProtocolException("Null location redirect");
      }
   }

   private HttpURLConnection makeConnection(DataSpec var1) throws IOException {
      URL var2 = new URL(var1.uri.toString());
      int var3 = var1.httpMethod;
      byte[] var4 = var1.httpBody;
      long var5 = var1.position;
      long var7 = var1.length;
      boolean var9 = var1.isFlagSet(1);
      boolean var10 = var1.isFlagSet(2);
      if (!this.allowCrossProtocolRedirects) {
         return this.makeConnection(var2, var3, var4, var5, var7, var9, var10, true);
      } else {
         int var11 = 0;
         byte[] var14 = var4;

         while(true) {
            int var12 = var11 + 1;
            if (var11 > 20) {
               StringBuilder var16 = new StringBuilder();
               var16.append("Too many redirects: ");
               var16.append(var12);
               throw new NoRouteToHostException(var16.toString());
            }

            HttpURLConnection var13 = this.makeConnection(var2, var3, var14, var5, var7, var9, var10, false);
            var11 = var13.getResponseCode();
            String var17 = var13.getHeaderField("Location");
            URL var15;
            if ((var3 == 1 || var3 == 3) && (var11 == 300 || var11 == 301 || var11 == 302 || var11 == 303 || var11 == 307 || var11 == 308)) {
               var13.disconnect();
               var2 = handleRedirect(var2, var17);
               var4 = var14;
               var15 = var2;
            } else {
               if (var3 != 2 || var11 != 300 && var11 != 301 && var11 != 302 && var11 != 303) {
                  return var13;
               }

               var13.disconnect();
               var15 = handleRedirect(var2, var17);
               var4 = null;
               var3 = 1;
            }

            var11 = var12;
            var2 = var15;
            var14 = var4;
         }
      }
   }

   private HttpURLConnection makeConnection(URL var1, int var2, byte[] var3, long var4, long var6, boolean var8, boolean var9, boolean var10) throws IOException {
      HttpURLConnection var11 = (HttpURLConnection)var1.openConnection();
      var11.setConnectTimeout(this.connectTimeoutMillis);
      var11.setReadTimeout(this.readTimeoutMillis);
      HttpDataSource.RequestProperties var13 = this.defaultRequestProperties;
      Entry var12;
      Iterator var14;
      if (var13 != null) {
         var14 = var13.getSnapshot().entrySet().iterator();

         while(var14.hasNext()) {
            var12 = (Entry)var14.next();
            var11.setRequestProperty((String)var12.getKey(), (String)var12.getValue());
         }
      }

      var14 = this.requestProperties.getSnapshot().entrySet().iterator();

      while(var14.hasNext()) {
         var12 = (Entry)var14.next();
         var11.setRequestProperty((String)var12.getKey(), (String)var12.getValue());
      }

      if (var4 != 0L || var6 != -1L) {
         StringBuilder var15 = new StringBuilder();
         var15.append("bytes=");
         var15.append(var4);
         var15.append("-");
         String var18 = var15.toString();
         String var16 = var18;
         if (var6 != -1L) {
            var15 = new StringBuilder();
            var15.append(var18);
            var15.append(var4 + var6 - 1L);
            var16 = var15.toString();
         }

         var11.setRequestProperty("Range", var16);
      }

      var11.setRequestProperty("User-Agent", this.userAgent);
      if (!var8) {
         var11.setRequestProperty("Accept-Encoding", "identity");
      }

      if (var9) {
         var11.setRequestProperty("Icy-MetaData", "1");
      }

      var11.setInstanceFollowRedirects(var10);
      if (var3 != null) {
         var8 = true;
      } else {
         var8 = false;
      }

      var11.setDoOutput(var8);
      var11.setRequestMethod(DataSpec.getStringForHttpMethod(var2));
      if (var3 != null) {
         var11.setFixedLengthStreamingMode(var3.length);
         var11.connect();
         OutputStream var17 = var11.getOutputStream();
         var17.write(var3);
         var17.close();
      } else {
         var11.connect();
      }

      return var11;
   }

   private static void maybeTerminateInputStream(HttpURLConnection var0, long var1) {
      int var3 = Util.SDK_INT;
      if (var3 == 19 || var3 == 20) {
         boolean var10001;
         InputStream var9;
         try {
            var9 = var0.getInputStream();
         } catch (Exception var8) {
            var10001 = false;
            return;
         }

         if (var1 == -1L) {
            try {
               if (var9.read() == -1) {
                  return;
               }
            } catch (Exception var7) {
               var10001 = false;
               return;
            }
         } else if (var1 <= 2048L) {
            return;
         }

         try {
            String var4 = var9.getClass().getName();
            if (!"com.android.okhttp.internal.http.HttpTransport$ChunkedInputStream".equals(var4) && !"com.android.okhttp.internal.http.HttpTransport$FixedLengthInputStream".equals(var4)) {
               return;
            }
         } catch (Exception var6) {
            var10001 = false;
            return;
         }

         try {
            Method var10 = var9.getClass().getSuperclass().getDeclaredMethod("unexpectedEndOfInput");
            var10.setAccessible(true);
            var10.invoke(var9);
         } catch (Exception var5) {
            var10001 = false;
         }

      }
   }

   private int readInternal(byte[] var1, int var2, int var3) throws IOException {
      if (var3 == 0) {
         return 0;
      } else {
         long var4 = this.bytesToRead;
         int var6 = var3;
         if (var4 != -1L) {
            var4 -= this.bytesRead;
            if (var4 == 0L) {
               return -1;
            }

            var6 = (int)Math.min((long)var3, var4);
         }

         var2 = this.inputStream.read(var1, var2, var6);
         if (var2 == -1) {
            if (this.bytesToRead == -1L) {
               return -1;
            } else {
               throw new EOFException();
            }
         } else {
            this.bytesRead += (long)var2;
            this.bytesTransferred(var2);
            return var2;
         }
      }
   }

   private void skipInternal() throws IOException {
      if (this.bytesSkipped != this.bytesToSkip) {
         byte[] var1 = (byte[])skipBufferReference.getAndSet((Object)null);
         byte[] var2 = var1;
         if (var1 == null) {
            var2 = new byte[4096];
         }

         while(true) {
            long var3 = this.bytesSkipped;
            long var5 = this.bytesToSkip;
            if (var3 == var5) {
               skipBufferReference.set(var2);
               return;
            }

            int var7 = (int)Math.min(var5 - var3, (long)var2.length);
            var7 = this.inputStream.read(var2, 0, var7);
            if (Thread.currentThread().isInterrupted()) {
               throw new InterruptedIOException();
            }

            if (var7 == -1) {
               throw new EOFException();
            }

            this.bytesSkipped += (long)var7;
            this.bytesTransferred(var7);
         }
      }
   }

   protected final long bytesRemaining() {
      long var1 = this.bytesToRead;
      if (var1 != -1L) {
         var1 -= this.bytesRead;
      }

      return var1;
   }

   public void close() throws HttpDataSource.HttpDataSourceException {
      try {
         if (this.inputStream != null) {
            maybeTerminateInputStream(this.connection, this.bytesRemaining());

            try {
               this.inputStream.close();
            } catch (IOException var5) {
               HttpDataSource.HttpDataSourceException var2 = new HttpDataSource.HttpDataSourceException(var5, this.dataSpec, 3);
               throw var2;
            }
         }
      } finally {
         this.inputStream = null;
         this.closeConnectionQuietly();
         if (this.opened) {
            this.opened = false;
            this.transferEnded();
         }

      }

   }

   public Map getResponseHeaders() {
      HttpURLConnection var1 = this.connection;
      Map var2;
      if (var1 == null) {
         var2 = Collections.emptyMap();
      } else {
         var2 = var1.getHeaderFields();
      }

      return var2;
   }

   public Uri getUri() {
      HttpURLConnection var1 = this.connection;
      Uri var2;
      if (var1 == null) {
         var2 = null;
      } else {
         var2 = Uri.parse(var1.getURL().toString());
      }

      return var2;
   }

   public long open(DataSpec var1) throws HttpDataSource.HttpDataSourceException {
      this.dataSpec = var1;
      long var2 = 0L;
      this.bytesRead = 0L;
      this.bytesSkipped = 0L;
      this.transferInitializing(var1);

      try {
         this.connection = this.makeConnection(var1);
      } catch (IOException var13) {
         StringBuilder var6 = new StringBuilder();
         var6.append("Unable to connect to ");
         var6.append(var1.uri.toString());
         throw new HttpDataSource.HttpDataSourceException(var6.toString(), var13, var1, 1);
      }

      int var4;
      String var15;
      try {
         var4 = this.connection.getResponseCode();
         var15 = this.connection.getResponseMessage();
      } catch (IOException var12) {
         this.closeConnectionQuietly();
         StringBuilder var5 = new StringBuilder();
         var5.append("Unable to connect to ");
         var5.append(var1.uri.toString());
         throw new HttpDataSource.HttpDataSourceException(var5.toString(), var12, var1, 1);
      }

      if (var4 >= 200 && var4 <= 299) {
         var15 = this.connection.getContentType();
         Predicate var17 = this.contentTypePredicate;
         if (var17 != null && !var17.evaluate(var15)) {
            this.closeConnectionQuietly();
            throw new HttpDataSource.InvalidContentTypeException(var15, var1);
         } else {
            long var7 = var2;
            if (var4 == 200) {
               long var9 = var1.position;
               var7 = var2;
               if (var9 != 0L) {
                  var7 = var9;
               }
            }

            this.bytesToSkip = var7;
            if (!var1.isFlagSet(1)) {
               var2 = var1.length;
               var7 = -1L;
               if (var2 != -1L) {
                  this.bytesToRead = var2;
               } else {
                  var2 = getContentLength(this.connection);
                  if (var2 != -1L) {
                     var7 = var2 - this.bytesToSkip;
                  }

                  this.bytesToRead = var7;
               }
            } else {
               this.bytesToRead = var1.length;
            }

            try {
               this.inputStream = this.connection.getInputStream();
            } catch (IOException var11) {
               this.closeConnectionQuietly();
               throw new HttpDataSource.HttpDataSourceException(var11, var1, 1);
            }

            this.opened = true;
            this.transferStarted(var1);
            return this.bytesToRead;
         }
      } else {
         Map var16 = this.connection.getHeaderFields();
         this.closeConnectionQuietly();
         HttpDataSource.InvalidResponseCodeException var14 = new HttpDataSource.InvalidResponseCodeException(var4, var15, var16, var1);
         if (var4 == 416) {
            var14.initCause(new DataSourceException(0));
         }

         throw var14;
      }
   }

   public int read(byte[] var1, int var2, int var3) throws HttpDataSource.HttpDataSourceException {
      try {
         this.skipInternal();
         var2 = this.readInternal(var1, var2, var3);
         return var2;
      } catch (IOException var4) {
         throw new HttpDataSource.HttpDataSourceException(var4, this.dataSpec, 2);
      }
   }
}
