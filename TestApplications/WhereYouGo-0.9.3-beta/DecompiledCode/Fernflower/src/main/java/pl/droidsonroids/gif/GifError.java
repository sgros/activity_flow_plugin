package pl.droidsonroids.gif;

import android.support.annotation.NonNull;
import java.util.Locale;

public enum GifError {
   CLOSE_FAILED(110, "Failed to close given input"),
   DATA_TOO_BIG(108, "Number of pixels bigger than width * height"),
   EOF_TOO_SOON(113, "Image EOF detected before image complete"),
   IMAGE_DEFECT(112, "Image is defective, decoding aborted"),
   IMG_NOT_CONFINED(1003, "Image size exceeds screen size"),
   INVALID_BYTE_BUFFER(1005, "Invalid and/or indirect byte buffer specified"),
   @Deprecated
   INVALID_IMG_DIMS(1002, "Invalid image size, dimensions must be positive"),
   INVALID_SCR_DIMS(1001, "Invalid screen size, dimensions must be positive"),
   NOT_ENOUGH_MEM(109, "Failed to allocate required memory"),
   NOT_GIF_FILE(103, "Data is not in GIF format"),
   NOT_READABLE(111, "Given file was not opened for read"),
   NO_COLOR_MAP(106, "Neither global nor local color map found"),
   NO_ERROR(0, "No error"),
   NO_FRAMES(1000, "No frames found, at least one frame required"),
   NO_IMAG_DSCR(105, "No image descriptor detected"),
   NO_SCRN_DSCR(104, "No screen descriptor detected"),
   OPEN_FAILED(101, "Failed to open given input"),
   READ_FAILED(102, "Failed to read from given input"),
   REWIND_FAILED(1004, "Input source rewind failed, animation stopped"),
   UNKNOWN(-1, "Unknown error"),
   WRONG_RECORD(107, "Wrong record type detected");

   @NonNull
   public final String description;
   int errorCode;

   private GifError(int var3, @NonNull String var4) {
      this.errorCode = var3;
      this.description = var4;
   }

   static GifError fromCode(int var0) {
      GifError[] var1 = values();
      int var2 = var1.length;
      int var3 = 0;

      GifError var4;
      while(true) {
         if (var3 >= var2) {
            var4 = UNKNOWN;
            var4.errorCode = var0;
            break;
         }

         var4 = var1[var3];
         if (var4.errorCode == var0) {
            break;
         }

         ++var3;
      }

      return var4;
   }

   public int getErrorCode() {
      return this.errorCode;
   }

   String getFormattedDescription() {
      return String.format(Locale.ENGLISH, "GifError %d: %s", this.errorCode, this.description);
   }
}
