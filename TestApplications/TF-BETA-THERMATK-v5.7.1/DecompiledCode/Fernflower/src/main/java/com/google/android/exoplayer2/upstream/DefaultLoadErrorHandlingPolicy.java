package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.ParserException;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DefaultLoadErrorHandlingPolicy implements LoadErrorHandlingPolicy {
   private final int minimumLoadableRetryCount;

   public DefaultLoadErrorHandlingPolicy() {
      this(-1);
   }

   public DefaultLoadErrorHandlingPolicy(int var1) {
      this.minimumLoadableRetryCount = var1;
   }

   public long getBlacklistDurationMsFor(int var1, long var2, IOException var4, int var5) {
      boolean var6 = var4 instanceof HttpDataSource.InvalidResponseCodeException;
      long var7 = -9223372036854775807L;
      var2 = var7;
      if (var6) {
         var1 = ((HttpDataSource.InvalidResponseCodeException)var4).responseCode;
         if (var1 != 404) {
            var2 = var7;
            if (var1 != 410) {
               return var2;
            }
         }

         var2 = 60000L;
      }

      return var2;
   }

   public int getMinimumLoadableRetryCount(int var1) {
      int var2 = this.minimumLoadableRetryCount;
      if (var2 == -1) {
         byte var3;
         if (var1 == 7) {
            var3 = 6;
         } else {
            var3 = 3;
         }

         return var3;
      } else {
         return var2;
      }
   }

   public long getRetryDelayMsFor(int var1, long var2, IOException var4, int var5) {
      if (!(var4 instanceof ParserException) && !(var4 instanceof FileNotFoundException)) {
         var2 = (long)Math.min((var5 - 1) * 1000, 5000);
      } else {
         var2 = -9223372036854775807L;
      }

      return var2;
   }
}
