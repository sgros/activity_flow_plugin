package com.google.android.exoplayer2.upstream;

import android.net.Uri;
import android.util.Base64;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.net.URLDecoder;

public final class DataSchemeDataSource extends BaseDataSource {
   private int bytesRead;
   private byte[] data;
   private DataSpec dataSpec;

   public DataSchemeDataSource() {
      super(false);
   }

   public void close() throws IOException {
      if (this.data != null) {
         this.data = null;
         this.transferEnded();
      }

      this.dataSpec = null;
   }

   public Uri getUri() {
      DataSpec var1 = this.dataSpec;
      Uri var2;
      if (var1 != null) {
         var2 = var1.uri;
      } else {
         var2 = null;
      }

      return var2;
   }

   public long open(DataSpec var1) throws IOException {
      this.transferInitializing(var1);
      this.dataSpec = var1;
      Uri var2 = var1.uri;
      String var3 = var2.getScheme();
      StringBuilder var5;
      if ("data".equals(var3)) {
         String[] var7 = Util.split(var2.getSchemeSpecificPart(), ",");
         if (var7.length == 2) {
            String var6 = var7[1];
            if (var7[0].contains(";base64")) {
               try {
                  this.data = Base64.decode(var6, 0);
               } catch (IllegalArgumentException var4) {
                  var5 = new StringBuilder();
                  var5.append("Error while parsing Base64 encoded string: ");
                  var5.append(var6);
                  throw new ParserException(var5.toString(), var4);
               }
            } else {
               this.data = Util.getUtf8Bytes(URLDecoder.decode(var6, "US-ASCII"));
            }

            this.transferStarted(var1);
            return (long)this.data.length;
         } else {
            var5 = new StringBuilder();
            var5.append("Unexpected URI format: ");
            var5.append(var2);
            throw new ParserException(var5.toString());
         }
      } else {
         var5 = new StringBuilder();
         var5.append("Unsupported scheme: ");
         var5.append(var3);
         throw new ParserException(var5.toString());
      }
   }

   public int read(byte[] var1, int var2, int var3) {
      if (var3 == 0) {
         return 0;
      } else {
         int var4 = this.data.length - this.bytesRead;
         if (var4 == 0) {
            return -1;
         } else {
            var3 = Math.min(var3, var4);
            System.arraycopy(this.data, this.bytesRead, var1, var2, var3);
            this.bytesRead += var3;
            this.bytesTransferred(var3);
            return var3;
         }
      }
   }
}
