package org.telegram.messenger.voip;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.os.ParcelFileDescriptor.AutoCloseOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.telegram.messenger.ApplicationLoader;

public class CallNotificationSoundProvider extends ContentProvider {
   public int delete(Uri var1, String var2, String[] var3) {
      return 0;
   }

   public String getType(Uri var1) {
      return null;
   }

   public Uri insert(Uri var1, ContentValues var2) {
      return null;
   }

   public boolean onCreate() {
      return true;
   }

   public ParcelFileDescriptor openFile(Uri var1, String var2) throws FileNotFoundException {
      if ("r".equals(var2)) {
         if (ApplicationLoader.applicationContext != null) {
            VoIPBaseService var5 = VoIPBaseService.getSharedInstance();
            if (var5 != null) {
               var5.startRingtoneAndVibration();
            }

            ParcelFileDescriptor[] var7;
            try {
               var7 = ParcelFileDescriptor.createPipe();
               AutoCloseOutputStream var6 = new AutoCloseOutputStream(var7[1]);
               var6.write(new byte[]{82, 73, 70, 70, 41, 0, 0, 0, 87, 65, 86, 69, 102, 109, 116, 32, 16, 0, 0, 0, 1, 0, 1, 0, 68, -84, 0, 0, 16, -79, 2, 0, 2, 0, 16, 0, 100, 97, 116, 97, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
               var6.close();
            } catch (IOException var3) {
               throw new FileNotFoundException(var3.getMessage());
            }

            ParcelFileDescriptor var8 = var7[0];
            return var8;
         } else {
            throw new FileNotFoundException("Unexpected application state");
         }
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("Unexpected file mode ");
         var4.append(var2);
         throw new SecurityException(var4.toString());
      }
   }

   public Cursor query(Uri var1, String[] var2, String var3, String[] var4, String var5) {
      return null;
   }

   public int update(Uri var1, ContentValues var2, String var3, String[] var4) {
      return 0;
   }
}
