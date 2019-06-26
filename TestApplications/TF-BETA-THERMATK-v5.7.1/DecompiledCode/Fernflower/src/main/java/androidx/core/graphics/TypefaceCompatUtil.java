package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Process;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

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
      boolean var9 = false;

      InputStream var3;
      try {
         var9 = true;
         var3 = var1.openRawResource(var2);
         var9 = false;
      } finally {
         if (var9) {
            var0 = null;
            closeQuietly(var0);
         }
      }

      boolean var4;
      try {
         var4 = copyToFile(var0, var3);
      } finally {
         ;
      }

      closeQuietly(var3);
      return var4;
   }

   public static boolean copyToFile(File param0, InputStream param1) {
      // $FF: Couldn't be decompiled
   }

   public static File getTempFile(Context var0) {
      File var6 = var0.getCacheDir();
      if (var6 == null) {
         return null;
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append(".font");
         var1.append(Process.myPid());
         var1.append("-");
         var1.append(Process.myTid());
         var1.append("-");
         String var7 = var1.toString();

         for(int var2 = 0; var2 < 100; ++var2) {
            StringBuilder var3 = new StringBuilder();
            var3.append(var7);
            var3.append(var2);
            File var8 = new File(var6, var3.toString());

            boolean var4;
            try {
               var4 = var8.createNewFile();
            } catch (IOException var5) {
               continue;
            }

            if (var4) {
               return var8;
            }
         }

         return null;
      }
   }

   public static ByteBuffer mmap(Context param0, CancellationSignal param1, Uri param2) {
      // $FF: Couldn't be decompiled
   }

   private static ByteBuffer mmap(File var0) {
      FileInputStream var1;
      boolean var10001;
      try {
         var1 = new FileInputStream(var0);
      } catch (IOException var28) {
         var10001 = false;
         return null;
      }

      MappedByteBuffer var30;
      label141: {
         Throwable var4;
         try {
            FileChannel var29 = var1.getChannel();
            long var2 = var29.size();
            var30 = var29.map(MapMode.READ_ONLY, 0L, var2);
            break label141;
         } catch (Throwable var26) {
            var4 = var26;
         } finally {
            ;
         }

         try {
            throw var4;
         } finally {
            if (var4 != null) {
               try {
                  var1.close();
               } catch (Throwable var21) {
               }
            } else {
               try {
                  var1.close();
               } catch (IOException var23) {
                  var10001 = false;
                  return null;
               }
            }

            try {
               throw var0;
            } catch (IOException var22) {
               var10001 = false;
               return null;
            }
         }
      }

      try {
         var1.close();
         return var30;
      } catch (IOException var25) {
         var10001 = false;
         return null;
      }
   }
}
