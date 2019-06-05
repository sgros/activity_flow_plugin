package net.sqlcipher.database;

import android.content.res.AssetFileDescriptor;
import android.os.MemoryFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import net.sqlcipher.Cursor;

public class SQLiteContentHelper {
   public static AssetFileDescriptor getBlobColumnAsAssetFile(SQLiteDatabase param0, String param1, String[] param2) throws FileNotFoundException {
      // $FF: Couldn't be decompiled
   }

   private static MemoryFile simpleQueryForBlobMemoryFile(SQLiteDatabase var0, String var1, String[] var2) throws IOException {
      Cursor var16 = var0.rawQuery(var1, var2);
      if (var16 == null) {
         return null;
      } else {
         Throwable var10000;
         label148: {
            boolean var3;
            boolean var10001;
            try {
               var3 = var16.moveToFirst();
            } catch (Throwable var15) {
               var10000 = var15;
               var10001 = false;
               break label148;
            }

            if (!var3) {
               var16.close();
               return null;
            }

            byte[] var17;
            try {
               var17 = var16.getBlob(0);
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label148;
            }

            if (var17 == null) {
               var16.close();
               return null;
            }

            MemoryFile var19;
            try {
               var19 = new MemoryFile((String)null, var17.length);
               var19.writeBytes(var17, 0, 0, var17.length);
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label148;
            }

            var16.close();
            return var19;
         }

         Throwable var18 = var10000;
         var16.close();
         throw var18;
      }
   }
}
