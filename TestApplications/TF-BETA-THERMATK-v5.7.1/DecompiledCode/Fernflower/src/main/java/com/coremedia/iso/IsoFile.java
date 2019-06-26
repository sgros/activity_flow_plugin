package com.coremedia.iso;

import com.googlecode.mp4parser.BasicContainer;
import com.googlecode.mp4parser.util.Logger;
import java.io.Closeable;
import java.io.IOException;

public class IsoFile extends BasicContainer implements Closeable {
   private static Logger LOG = Logger.getLogger(IsoFile.class);

   public static byte[] fourCCtoBytes(String var0) {
      byte[] var1 = new byte[4];
      if (var0 != null) {
         for(int var2 = 0; var2 < Math.min(4, var0.length()); ++var2) {
            var1[var2] = (byte)((byte)var0.charAt(var2));
         }
      }

      return var1;
   }

   public void close() throws IOException {
      super.dataSource.close();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("model(");
      var1.append(super.dataSource.toString());
      var1.append(")");
      return var1.toString();
   }
}
