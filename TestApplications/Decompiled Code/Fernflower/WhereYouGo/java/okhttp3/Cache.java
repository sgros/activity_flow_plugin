package okhttp3;

import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import okhttp3.internal.Util;
import okhttp3.internal.cache.CacheRequest;
import okhttp3.internal.cache.CacheStrategy;
import okhttp3.internal.cache.DiskLruCache;
import okhttp3.internal.cache.InternalCache;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.HttpMethod;
import okhttp3.internal.http.StatusLine;
import okhttp3.internal.io.FileSystem;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.ForwardingSink;
import okio.ForwardingSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

public final class Cache implements Closeable, Flushable {
   private static final int ENTRY_BODY = 1;
   private static final int ENTRY_COUNT = 2;
   private static final int ENTRY_METADATA = 0;
   private static final int VERSION = 201105;
   final DiskLruCache cache;
   private int hitCount;
   final InternalCache internalCache;
   private int networkCount;
   private int requestCount;
   int writeAbortCount;
   int writeSuccessCount;

   public Cache(File var1, long var2) {
      this(var1, var2, FileSystem.SYSTEM);
   }

   Cache(File var1, long var2, FileSystem var4) {
      this.internalCache = new InternalCache() {
         public Response get(Request var1) throws IOException {
            return Cache.this.get(var1);
         }

         public CacheRequest put(Response var1) throws IOException {
            return Cache.this.put(var1);
         }

         public void remove(Request var1) throws IOException {
            Cache.this.remove(var1);
         }

         public void trackConditionalCacheHit() {
            Cache.this.trackConditionalCacheHit();
         }

         public void trackResponse(CacheStrategy var1) {
            Cache.this.trackResponse(var1);
         }

         public void update(Response var1, Response var2) {
            Cache.this.update(var1, var2);
         }
      };
      this.cache = DiskLruCache.create(var4, var1, 201105, 2, var2);
   }

   private void abortQuietly(DiskLruCache.Editor var1) {
      if (var1 != null) {
         try {
            var1.abort();
         } catch (IOException var2) {
         }
      }

   }

   public static String key(HttpUrl var0) {
      return ByteString.encodeUtf8(var0.toString()).md5().hex();
   }

   static int readInt(BufferedSource var0) throws IOException {
      NumberFormatException var10000;
      label31: {
         long var1;
         boolean var10001;
         String var3;
         try {
            var1 = var0.readDecimalLong();
            var3 = var0.readUtf8LineStrict();
         } catch (NumberFormatException var7) {
            var10000 = var7;
            var10001 = false;
            break label31;
         }

         if (var1 >= 0L && var1 <= 2147483647L) {
            try {
               if (var3.isEmpty()) {
                  return (int)var1;
               }
            } catch (NumberFormatException var6) {
               var10000 = var6;
               var10001 = false;
               break label31;
            }
         }

         try {
            StringBuilder var9 = new StringBuilder();
            IOException var4 = new IOException(var9.append("expected an int but was \"").append(var1).append(var3).append("\"").toString());
            throw var4;
         } catch (NumberFormatException var5) {
            var10000 = var5;
            var10001 = false;
         }
      }

      NumberFormatException var8 = var10000;
      throw new IOException(var8.getMessage());
   }

   public void close() throws IOException {
      this.cache.close();
   }

   public void delete() throws IOException {
      this.cache.delete();
   }

   public File directory() {
      return this.cache.getDirectory();
   }

   public void evictAll() throws IOException {
      this.cache.evictAll();
   }

   public void flush() throws IOException {
      this.cache.flush();
   }

   Response get(Request var1) {
      String var2 = key(var1.url());

      Response var7;
      DiskLruCache.Snapshot var8;
      try {
         var8 = this.cache.get(var2);
      } catch (IOException var6) {
         var7 = null;
         return var7;
      }

      if (var8 == null) {
         var7 = null;
      } else {
         Cache.Entry var3;
         try {
            var3 = new Cache.Entry(var8.getSource(0));
         } catch (IOException var5) {
            Util.closeQuietly((Closeable)var8);
            var7 = null;
            return var7;
         }

         Response var4 = var3.response(var8);
         var7 = var4;
         if (!var3.matches(var1, var4)) {
            Util.closeQuietly((Closeable)var4.body());
            var7 = null;
         }
      }

      return var7;
   }

   public int hitCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.hitCount;
      } finally {
         ;
      }

      return var1;
   }

   public void initialize() throws IOException {
      this.cache.initialize();
   }

   public boolean isClosed() {
      return this.cache.isClosed();
   }

   public long maxSize() {
      return this.cache.getMaxSize();
   }

   public int networkCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.networkCount;
      } finally {
         ;
      }

      return var1;
   }

   CacheRequest put(Response var1) {
      Object var2 = null;
      String var3 = var1.request().method();
      Cache.CacheRequestImpl var4;
      if (HttpMethod.invalidatesCache(var1.request().method())) {
         try {
            this.remove(var1.request());
         } catch (IOException var9) {
            var4 = (Cache.CacheRequestImpl)var2;
            return var4;
         }

         var4 = (Cache.CacheRequestImpl)var2;
      } else {
         var4 = (Cache.CacheRequestImpl)var2;
         if (var3.equals("GET")) {
            var4 = (Cache.CacheRequestImpl)var2;
            if (!HttpHeaders.hasVaryAll(var1)) {
               Cache.Entry var11 = new Cache.Entry(var1);
               DiskLruCache.Editor var13 = null;

               Cache.CacheRequestImpl var12;
               label70: {
                  label61: {
                     boolean var10001;
                     DiskLruCache.Editor var10;
                     try {
                        var10 = this.cache.edit(key(var1.request().url()));
                     } catch (IOException var8) {
                        var10001 = false;
                        break label61;
                     }

                     var4 = (Cache.CacheRequestImpl)var2;
                     if (var10 == null) {
                        return var4;
                     }

                     var13 = var10;

                     try {
                        var11.writeTo(var10);
                     } catch (IOException var7) {
                        var10001 = false;
                        break label61;
                     }

                     var13 = var10;

                     try {
                        var12 = new Cache.CacheRequestImpl;
                     } catch (IOException var6) {
                        var10001 = false;
                        break label61;
                     }

                     var13 = var10;

                     try {
                        var12.<init>(var10);
                        break label70;
                     } catch (IOException var5) {
                        var10001 = false;
                     }
                  }

                  this.abortQuietly(var13);
                  var4 = (Cache.CacheRequestImpl)var2;
                  return var4;
               }

               var4 = var12;
            }
         }
      }

      return var4;
   }

   void remove(Request var1) throws IOException {
      this.cache.remove(key(var1.url()));
   }

   public int requestCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.requestCount;
      } finally {
         ;
      }

      return var1;
   }

   public long size() throws IOException {
      return this.cache.size();
   }

   void trackConditionalCacheHit() {
      synchronized(this){}

      try {
         ++this.hitCount;
      } finally {
         ;
      }

   }

   void trackResponse(CacheStrategy var1) {
      synchronized(this){}

      try {
         ++this.requestCount;
         if (var1.networkRequest != null) {
            ++this.networkCount;
         } else if (var1.cacheResponse != null) {
            ++this.hitCount;
         }
      } finally {
         ;
      }

   }

   void update(Response var1, Response var2) {
      Cache.Entry var3 = new Cache.Entry(var2);
      DiskLruCache.Snapshot var8 = ((Cache.CacheResponseBody)var1.body()).snapshot;
      DiskLruCache.Editor var7 = null;

      label37: {
         boolean var10001;
         DiskLruCache.Editor var9;
         try {
            var9 = var8.edit();
         } catch (IOException var6) {
            var10001 = false;
            break label37;
         }

         if (var9 == null) {
            return;
         }

         var7 = var9;

         try {
            var3.writeTo(var9);
         } catch (IOException var5) {
            var10001 = false;
            break label37;
         }

         var7 = var9;

         try {
            var9.commit();
            return;
         } catch (IOException var4) {
            var10001 = false;
         }
      }

      this.abortQuietly(var7);
   }

   public Iterator urls() throws IOException {
      return new Iterator() {
         boolean canRemove;
         final Iterator delegate;
         String nextUrl;

         {
            this.delegate = Cache.this.cache.snapshots();
         }

         public boolean hasNext() {
            boolean var1 = true;
            if (this.nextUrl == null) {
               this.canRemove = false;

               while(true) {
                  if (!this.delegate.hasNext()) {
                     var1 = false;
                     break;
                  }

                  DiskLruCache.Snapshot var2 = (DiskLruCache.Snapshot)this.delegate.next();

                  try {
                     this.nextUrl = Okio.buffer(var2.getSource(0)).readUtf8LineStrict();
                     break;
                  } catch (IOException var6) {
                  } finally {
                     var2.close();
                  }
               }
            }

            return var1;
         }

         public String next() {
            if (!this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               String var1 = this.nextUrl;
               this.nextUrl = null;
               this.canRemove = true;
               return var1;
            }
         }

         public void remove() {
            if (!this.canRemove) {
               throw new IllegalStateException("remove() before next()");
            } else {
               this.delegate.remove();
            }
         }
      };
   }

   public int writeAbortCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.writeAbortCount;
      } finally {
         ;
      }

      return var1;
   }

   public int writeSuccessCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.writeSuccessCount;
      } finally {
         ;
      }

      return var1;
   }

   private final class CacheRequestImpl implements CacheRequest {
      private Sink body;
      private Sink cacheOut;
      boolean done;
      private final DiskLruCache.Editor editor;

      public CacheRequestImpl(final DiskLruCache.Editor var2) {
         this.editor = var2;
         this.cacheOut = var2.newSink(1);
         this.body = new ForwardingSink(this.cacheOut) {
            public void close() throws IOException {
               Cache var1 = Cache.this;
               synchronized(var1){}

               Throwable var10000;
               boolean var10001;
               label158: {
                  try {
                     if (CacheRequestImpl.this.done) {
                        return;
                     }
                  } catch (Throwable var14) {
                     var10000 = var14;
                     var10001 = false;
                     break label158;
                  }

                  try {
                     CacheRequestImpl.this.done = true;
                     Cache var15 = Cache.this;
                     ++var15.writeSuccessCount;
                  } catch (Throwable var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label158;
                  }

                  super.close();
                  var2.commit();
                  return;
               }

               while(true) {
                  Throwable var2x = var10000;

                  try {
                     throw var2x;
                  } catch (Throwable var12) {
                     var10000 = var12;
                     var10001 = false;
                     continue;
                  }
               }
            }
         };
      }

      public void abort() {
         Cache var1 = Cache.this;
         synchronized(var1){}

         Throwable var10000;
         boolean var10001;
         label176: {
            try {
               if (this.done) {
                  return;
               }
            } catch (Throwable var18) {
               var10000 = var18;
               var10001 = false;
               break label176;
            }

            try {
               this.done = true;
               Cache var19 = Cache.this;
               ++var19.writeAbortCount;
            } catch (Throwable var17) {
               var10000 = var17;
               var10001 = false;
               break label176;
            }

            Util.closeQuietly((Closeable)this.cacheOut);

            try {
               this.editor.abort();
            } catch (IOException var15) {
            }

            return;
         }

         while(true) {
            Throwable var2 = var10000;

            try {
               throw var2;
            } catch (Throwable var16) {
               var10000 = var16;
               var10001 = false;
               continue;
            }
         }
      }

      public Sink body() {
         return this.body;
      }
   }

   private static class CacheResponseBody extends ResponseBody {
      private final BufferedSource bodySource;
      private final String contentLength;
      private final String contentType;
      final DiskLruCache.Snapshot snapshot;

      public CacheResponseBody(final DiskLruCache.Snapshot var1, String var2, String var3) {
         this.snapshot = var1;
         this.contentType = var2;
         this.contentLength = var3;
         this.bodySource = Okio.buffer((Source)(new ForwardingSource(var1.getSource(1)) {
            public void close() throws IOException {
               var1.close();
               super.close();
            }
         }));
      }

      public long contentLength() {
         long var1 = -1L;
         long var3 = var1;

         try {
            if (this.contentLength != null) {
               var3 = Long.parseLong(this.contentLength);
            }
         } catch (NumberFormatException var6) {
            var3 = var1;
         }

         return var3;
      }

      public MediaType contentType() {
         MediaType var1;
         if (this.contentType != null) {
            var1 = MediaType.parse(this.contentType);
         } else {
            var1 = null;
         }

         return var1;
      }

      public BufferedSource source() {
         return this.bodySource;
      }
   }

   private static final class Entry {
      private static final String RECEIVED_MILLIS = Platform.get().getPrefix() + "-Received-Millis";
      private static final String SENT_MILLIS = Platform.get().getPrefix() + "-Sent-Millis";
      private final int code;
      private final Handshake handshake;
      private final String message;
      private final Protocol protocol;
      private final long receivedResponseMillis;
      private final String requestMethod;
      private final Headers responseHeaders;
      private final long sentRequestMillis;
      private final String url;
      private final Headers varyHeaders;

      public Entry(Response var1) {
         this.url = var1.request().url().toString();
         this.varyHeaders = HttpHeaders.varyHeaders(var1);
         this.requestMethod = var1.request().method();
         this.protocol = var1.protocol();
         this.code = var1.code();
         this.message = var1.message();
         this.responseHeaders = var1.headers();
         this.handshake = var1.handshake();
         this.sentRequestMillis = var1.sentRequestAtMillis();
         this.receivedResponseMillis = var1.receivedResponseAtMillis();
      }

      public Entry(Source var1) throws IOException {
         label1451: {
            Throwable var10000;
            label1455: {
               BufferedSource var2;
               Headers.Builder var3;
               int var4;
               boolean var10001;
               try {
                  var2 = Okio.buffer(var1);
                  this.url = var2.readUtf8LineStrict();
                  this.requestMethod = var2.readUtf8LineStrict();
                  var3 = new Headers.Builder();
                  var4 = Cache.readInt(var2);
               } catch (Throwable var165) {
                  var10000 = var165;
                  var10001 = false;
                  break label1455;
               }

               int var5;
               for(var5 = 0; var5 < var4; ++var5) {
                  try {
                     var3.addLenient(var2.readUtf8LineStrict());
                  } catch (Throwable var164) {
                     var10000 = var164;
                     var10001 = false;
                     break label1455;
                  }
               }

               Headers.Builder var6;
               try {
                  this.varyHeaders = var3.build();
                  StatusLine var168 = StatusLine.parse(var2.readUtf8LineStrict());
                  this.protocol = var168.protocol;
                  this.code = var168.code;
                  this.message = var168.message;
                  var6 = new Headers.Builder();
                  var4 = Cache.readInt(var2);
               } catch (Throwable var163) {
                  var10000 = var163;
                  var10001 = false;
                  break label1455;
               }

               for(var5 = 0; var5 < var4; ++var5) {
                  try {
                     var6.addLenient(var2.readUtf8LineStrict());
                  } catch (Throwable var162) {
                     var10000 = var162;
                     var10001 = false;
                     break label1455;
                  }
               }

               String var7;
               String var169;
               try {
                  var169 = var6.get(SENT_MILLIS);
                  var7 = var6.get(RECEIVED_MILLIS);
                  var6.removeAll(SENT_MILLIS);
                  var6.removeAll(RECEIVED_MILLIS);
               } catch (Throwable var161) {
                  var10000 = var161;
                  var10001 = false;
                  break label1455;
               }

               long var8;
               if (var169 != null) {
                  try {
                     var8 = Long.parseLong(var169);
                  } catch (Throwable var160) {
                     var10000 = var160;
                     var10001 = false;
                     break label1455;
                  }
               } else {
                  var8 = 0L;
               }

               try {
                  this.sentRequestMillis = var8;
               } catch (Throwable var159) {
                  var10000 = var159;
                  var10001 = false;
                  break label1455;
               }

               if (var7 != null) {
                  try {
                     var8 = Long.parseLong(var7);
                  } catch (Throwable var158) {
                     var10000 = var158;
                     var10001 = false;
                     break label1455;
                  }
               } else {
                  var8 = 0L;
               }

               label1411: {
                  try {
                     this.receivedResponseMillis = var8;
                     this.responseHeaders = var6.build();
                     if (this.isHttps()) {
                        var169 = var2.readUtf8LineStrict();
                        if (var169.length() > 0) {
                           StringBuilder var174 = new StringBuilder();
                           IOException var170 = new IOException(var174.append("expected \"\" but was \"").append(var169).append("\"").toString());
                           throw var170;
                        }
                        break label1411;
                     }
                  } catch (Throwable var157) {
                     var10000 = var157;
                     var10001 = false;
                     break label1455;
                  }

                  try {
                     this.handshake = null;
                     break label1451;
                  } catch (Throwable var156) {
                     var10000 = var156;
                     var10001 = false;
                     break label1455;
                  }
               }

               TlsVersion var166;
               List var171;
               List var172;
               CipherSuite var173;
               label1401: {
                  try {
                     var173 = CipherSuite.forJavaName(var2.readUtf8LineStrict());
                     var172 = this.readCertificateList(var2);
                     var171 = this.readCertificateList(var2);
                     if (!var2.exhausted()) {
                        var166 = TlsVersion.forJavaName(var2.readUtf8LineStrict());
                        break label1401;
                     }
                  } catch (Throwable var155) {
                     var10000 = var155;
                     var10001 = false;
                     break label1455;
                  }

                  var166 = null;
               }

               label1394:
               try {
                  this.handshake = Handshake.get(var166, var173, var172, var171);
                  break label1451;
               } catch (Throwable var154) {
                  var10000 = var154;
                  var10001 = false;
                  break label1394;
               }
            }

            Throwable var167 = var10000;
            var1.close();
            throw var167;
         }

         var1.close();
      }

      private boolean isHttps() {
         return this.url.startsWith("https://");
      }

      private List readCertificateList(BufferedSource var1) throws IOException {
         int var2 = Cache.readInt(var1);
         Object var3;
         if (var2 == -1) {
            var3 = Collections.emptyList();
            return (List)var3;
         } else {
            CertificateException var10000;
            label35: {
               CertificateFactory var4;
               ArrayList var5;
               boolean var10001;
               try {
                  var4 = CertificateFactory.getInstance("X.509");
                  var5 = new ArrayList(var2);
               } catch (CertificateException var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label35;
               }

               int var6 = 0;

               while(true) {
                  var3 = var5;
                  if (var6 >= var2) {
                     return (List)var3;
                  }

                  try {
                     String var11 = var1.readUtf8LineStrict();
                     Buffer var7 = new Buffer();
                     var7.write(ByteString.decodeBase64(var11));
                     var5.add(var4.generateCertificate(var7.inputStream()));
                  } catch (CertificateException var8) {
                     var10000 = var8;
                     var10001 = false;
                     break;
                  }

                  ++var6;
               }
            }

            CertificateException var10 = var10000;
            throw new IOException(var10.getMessage());
         }
      }

      private void writeCertList(BufferedSink var1, List var2) throws IOException {
         CertificateEncodingException var10000;
         label35: {
            boolean var10001;
            try {
               var1.writeDecimalLong((long)var2.size()).writeByte(10);
            } catch (CertificateEncodingException var7) {
               var10000 = var7;
               var10001 = false;
               break label35;
            }

            int var3 = 0;

            int var4;
            try {
               var4 = var2.size();
            } catch (CertificateEncodingException var6) {
               var10000 = var6;
               var10001 = false;
               break label35;
            }

            while(true) {
               if (var3 >= var4) {
                  return;
               }

               try {
                  var1.writeUtf8(ByteString.of(((Certificate)var2.get(var3)).getEncoded()).base64()).writeByte(10);
               } catch (CertificateEncodingException var5) {
                  var10000 = var5;
                  var10001 = false;
                  break;
               }

               ++var3;
            }
         }

         CertificateEncodingException var8 = var10000;
         throw new IOException(var8.getMessage());
      }

      public boolean matches(Request var1, Response var2) {
         boolean var3;
         if (this.url.equals(var1.url().toString()) && this.requestMethod.equals(var1.method()) && HttpHeaders.varyMatches(var2, this.varyHeaders, var1)) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }

      public Response response(DiskLruCache.Snapshot var1) {
         String var2 = this.responseHeaders.get("Content-Type");
         String var3 = this.responseHeaders.get("Content-Length");
         Request var4 = (new Request.Builder()).url(this.url).method(this.requestMethod, (RequestBody)null).headers(this.varyHeaders).build();
         return (new Response.Builder()).request(var4).protocol(this.protocol).code(this.code).message(this.message).headers(this.responseHeaders).body(new Cache.CacheResponseBody(var1, var2, var3)).handshake(this.handshake).sentRequestAtMillis(this.sentRequestMillis).receivedResponseAtMillis(this.receivedResponseMillis).build();
      }

      public void writeTo(DiskLruCache.Editor var1) throws IOException {
         BufferedSink var4 = Okio.buffer(var1.newSink(0));
         var4.writeUtf8(this.url).writeByte(10);
         var4.writeUtf8(this.requestMethod).writeByte(10);
         var4.writeDecimalLong((long)this.varyHeaders.size()).writeByte(10);
         int var2 = 0;

         int var3;
         for(var3 = this.varyHeaders.size(); var2 < var3; ++var2) {
            var4.writeUtf8(this.varyHeaders.name(var2)).writeUtf8(": ").writeUtf8(this.varyHeaders.value(var2)).writeByte(10);
         }

         var4.writeUtf8((new StatusLine(this.protocol, this.code, this.message)).toString()).writeByte(10);
         var4.writeDecimalLong((long)(this.responseHeaders.size() + 2)).writeByte(10);
         var2 = 0;

         for(var3 = this.responseHeaders.size(); var2 < var3; ++var2) {
            var4.writeUtf8(this.responseHeaders.name(var2)).writeUtf8(": ").writeUtf8(this.responseHeaders.value(var2)).writeByte(10);
         }

         var4.writeUtf8(SENT_MILLIS).writeUtf8(": ").writeDecimalLong(this.sentRequestMillis).writeByte(10);
         var4.writeUtf8(RECEIVED_MILLIS).writeUtf8(": ").writeDecimalLong(this.receivedResponseMillis).writeByte(10);
         if (this.isHttps()) {
            var4.writeByte(10);
            var4.writeUtf8(this.handshake.cipherSuite().javaName()).writeByte(10);
            this.writeCertList(var4, this.handshake.peerCertificates());
            this.writeCertList(var4, this.handshake.localCertificates());
            if (this.handshake.tlsVersion() != null) {
               var4.writeUtf8(this.handshake.tlsVersion().javaName()).writeByte(10);
            }
         }

         var4.close();
      }
   }
}
