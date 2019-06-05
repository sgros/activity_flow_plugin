package android.support.v4.graphics;

import android.content.Context;
import android.graphics.Typeface;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.support.v4.provider.FontsContractCompat;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import java.io.File;

class TypefaceCompatApi21Impl extends TypefaceCompatBaseImpl {
   private File getFile(ParcelFileDescriptor var1) {
      try {
         StringBuilder var2 = new StringBuilder();
         var2.append("/proc/self/fd/");
         var2.append(var1.getFd());
         String var4 = Os.readlink(var2.toString());
         if (OsConstants.S_ISREG(Os.stat(var4).st_mode)) {
            File var5 = new File(var4);
            return var5;
         } else {
            return null;
         }
      } catch (ErrnoException var3) {
         return null;
      }
   }

   public Typeface createFromFontInfo(Context param1, CancellationSignal param2, FontsContractCompat.FontInfo[] param3, int param4) {
      // $FF: Couldn't be decompiled
   }
}
