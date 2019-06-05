package android.support.v4.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Process;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class TypefaceCompatUtil {
   public static void closeQuietly(Closeable var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (IOException var1) {
         }
      }

   }

   public static ByteBuffer copyToDirectBuffer(Context var0, Resources var1, int var2) {
      File var10 = getTempFile(var0);
      if (var10 == null) {
         return null;
      } else {
         Throwable var10000;
         label84: {
            boolean var10001;
            boolean var3;
            try {
               var3 = copyToFile(var10, var1, var2);
            } catch (Throwable var9) {
               var10000 = var9;
               var10001 = false;
               break label84;
            }

            if (!var3) {
               var10.delete();
               return null;
            }

            ByteBuffer var12;
            try {
               var12 = mmap(var10);
            } catch (Throwable var8) {
               var10000 = var8;
               var10001 = false;
               break label84;
            }

            var10.delete();
            return var12;
         }

         Throwable var11 = var10000;
         var10.delete();
         throw var11;
      }
   }

   public static boolean copyToFile(File var0, Resources var1, int var2) {
      InputStream var10;
      try {
         var10 = var1.openRawResource(var2);
      } finally {
         ;
      }

      try {
         copyToFile(var0, var10);
      } finally {
         closeQuietly(var10);
         throw var0;
      }

   }

   public static boolean copyToFile(File param0, InputStream param1) {
      // $FF: Couldn't be decompiled
   }

   public static File getTempFile(Context var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append(".font");
      var1.append(Process.myPid());
      var1.append("-");
      var1.append(Process.myTid());
      var1.append("-");
      String var7 = var1.toString();

      for(int var2 = 0; var2 < 100; ++var2) {
         File var3 = var0.getCacheDir();
         StringBuilder var4 = new StringBuilder();
         var4.append(var7);
         var4.append(var2);
         File var8 = new File(var3, var4.toString());

         boolean var5;
         try {
            var5 = var8.createNewFile();
         } catch (IOException var6) {
            continue;
         }

         if (var5) {
            return var8;
         }
      }

      return null;
   }

   public static ByteBuffer mmap(Context param0, CancellationSignal param1, Uri param2) {
      // $FF: Couldn't be decompiled
   }

   private static ByteBuffer mmap(File param0) {
      // $FF: Couldn't be decompiled
   }
}
