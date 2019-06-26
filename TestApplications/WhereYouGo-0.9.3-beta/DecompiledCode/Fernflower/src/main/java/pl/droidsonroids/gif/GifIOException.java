package pl.droidsonroids.gif;

import android.support.annotation.NonNull;
import java.io.IOException;

public class GifIOException extends IOException {
   private static final long serialVersionUID = 13038402904505L;
   private final String mErrnoMessage;
   @NonNull
   public final GifError reason;

   private GifIOException(int var1, String var2) {
      this.reason = GifError.fromCode(var1);
      this.mErrnoMessage = var2;
   }

   static GifIOException fromCode(int var0) {
      GifIOException var1 = null;
      if (var0 != GifError.NO_ERROR.errorCode) {
         var1 = new GifIOException(var0, (String)null);
      }

      return var1;
   }

   public String getMessage() {
      String var1;
      if (this.mErrnoMessage == null) {
         var1 = this.reason.getFormattedDescription();
      } else {
         var1 = this.reason.getFormattedDescription() + ": " + this.mErrnoMessage;
      }

      return var1;
   }
}
