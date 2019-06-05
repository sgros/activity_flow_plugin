package okhttp3;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSource;

public abstract class ResponseBody implements Closeable {
   private Reader reader;

   private Charset charset() {
      MediaType var1 = this.contentType();
      Charset var2;
      if (var1 != null) {
         var2 = var1.charset(Util.UTF_8);
      } else {
         var2 = Util.UTF_8;
      }

      return var2;
   }

   public static ResponseBody create(final MediaType var0, final long var1, final BufferedSource var3) {
      if (var3 == null) {
         throw new NullPointerException("source == null");
      } else {
         return new ResponseBody() {
            public long contentLength() {
               return var1;
            }

            public MediaType contentType() {
               return var0;
            }

            public BufferedSource source() {
               return var3;
            }
         };
      }
   }

   public static ResponseBody create(MediaType var0, String var1) {
      Charset var2 = Util.UTF_8;
      MediaType var3 = var0;
      if (var0 != null) {
         Charset var4 = var0.charset();
         var2 = var4;
         var3 = var0;
         if (var4 == null) {
            var2 = Util.UTF_8;
            var3 = MediaType.parse(var0 + "; charset=utf-8");
         }
      }

      Buffer var5 = (new Buffer()).writeString(var1, var2);
      return create(var3, var5.size(), var5);
   }

   public static ResponseBody create(MediaType var0, byte[] var1) {
      Buffer var2 = (new Buffer()).write(var1);
      return create(var0, (long)var1.length, var2);
   }

   public final InputStream byteStream() {
      return this.source().inputStream();
   }

   public final byte[] bytes() throws IOException {
      long var1 = this.contentLength();
      if (var1 > 2147483647L) {
         throw new IOException("Cannot buffer entire body for content length: " + var1);
      } else {
         BufferedSource var3 = this.source();

         byte[] var4;
         try {
            var4 = var3.readByteArray();
         } finally {
            Util.closeQuietly((Closeable)var3);
         }

         if (var1 != -1L && var1 != (long)var4.length) {
            throw new IOException("Content-Length (" + var1 + ") and stream length (" + var4.length + ") disagree");
         } else {
            return var4;
         }
      }
   }

   public final Reader charStream() {
      Object var1 = this.reader;
      if (var1 == null) {
         var1 = new ResponseBody.BomAwareReader(this.source(), this.charset());
         this.reader = (Reader)var1;
      }

      return (Reader)var1;
   }

   public void close() {
      Util.closeQuietly((Closeable)this.source());
   }

   public abstract long contentLength();

   public abstract MediaType contentType();

   public abstract BufferedSource source();

   public final String string() throws IOException {
      BufferedSource var1 = this.source();

      String var2;
      try {
         var2 = var1.readString(Util.bomAwareCharset(var1, this.charset()));
      } finally {
         Util.closeQuietly((Closeable)var1);
      }

      return var2;
   }

   static final class BomAwareReader extends Reader {
      private final Charset charset;
      private boolean closed;
      private Reader delegate;
      private final BufferedSource source;

      BomAwareReader(BufferedSource var1, Charset var2) {
         this.source = var1;
         this.charset = var2;
      }

      public void close() throws IOException {
         this.closed = true;
         if (this.delegate != null) {
            this.delegate.close();
         } else {
            this.source.close();
         }

      }

      public int read(char[] var1, int var2, int var3) throws IOException {
         if (this.closed) {
            throw new IOException("Stream closed");
         } else {
            Reader var4 = this.delegate;
            Object var5 = var4;
            if (var4 == null) {
               Charset var6 = Util.bomAwareCharset(this.source, this.charset);
               var5 = new InputStreamReader(this.source.inputStream(), var6);
               this.delegate = (Reader)var5;
            }

            return ((Reader)var5).read(var1, var2, var3);
         }
      }
   }
}
