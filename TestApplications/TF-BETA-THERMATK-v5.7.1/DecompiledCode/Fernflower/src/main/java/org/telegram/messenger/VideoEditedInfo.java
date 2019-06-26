package org.telegram.messenger;

import java.util.Locale;
import org.telegram.tgnet.TLRPC;

public class VideoEditedInfo {
   public int bitrate;
   public TLRPC.InputEncryptedFile encryptedFile;
   public long endTime;
   public long estimatedDuration;
   public long estimatedSize;
   public TLRPC.InputFile file;
   public int framerate = 24;
   public byte[] iv;
   public byte[] key;
   public boolean muted;
   public int originalHeight;
   public String originalPath;
   public int originalWidth;
   public int resultHeight;
   public int resultWidth;
   public int rotationValue;
   public boolean roundVideo;
   public long startTime;

   public String getString() {
      return String.format(Locale.US, "-1_%d_%d_%d_%d_%d_%d_%d_%d_%d_%s", this.startTime, this.endTime, this.rotationValue, this.originalWidth, this.originalHeight, this.bitrate, this.resultWidth, this.resultHeight, this.framerate, this.originalPath);
   }

   public boolean needConvert() {
      boolean var1;
      label29: {
         var1 = this.roundVideo;
         if (var1) {
            if (!var1) {
               break label29;
            }

            if (this.startTime <= 0L) {
               long var2 = this.endTime;
               if (var2 == -1L || var2 == this.estimatedDuration) {
                  break label29;
               }
            }
         }

         var1 = true;
         return var1;
      }

      var1 = false;
      return var1;
   }

   public boolean parseString(String var1) {
      if (var1.length() < 6) {
         return false;
      } else {
         Exception var10000;
         label81: {
            int var2;
            String[] var13;
            boolean var10001;
            try {
               var13 = var1.split("_");
               var2 = var13.length;
            } catch (Exception var12) {
               var10000 = var12;
               var10001 = false;
               break label81;
            }

            int var3 = 10;
            if (var2 < 10) {
               return true;
            }

            try {
               this.startTime = Long.parseLong(var13[1]);
               this.endTime = Long.parseLong(var13[2]);
               this.rotationValue = Integer.parseInt(var13[3]);
               this.originalWidth = Integer.parseInt(var13[4]);
               this.originalHeight = Integer.parseInt(var13[5]);
               this.bitrate = Integer.parseInt(var13[6]);
               this.resultWidth = Integer.parseInt(var13[7]);
               this.resultHeight = Integer.parseInt(var13[8]);
               var2 = var13.length;
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
               break label81;
            }

            if (var2 >= 11) {
               try {
                  this.framerate = Integer.parseInt(var13[9]);
               } catch (Exception var5) {
               }
            }

            label82: {
               label65: {
                  try {
                     if (this.framerate <= 0) {
                        break label65;
                     }
                  } catch (Exception var10) {
                     var10000 = var10;
                     var10001 = false;
                     break label81;
                  }

                  try {
                     if (this.framerate <= 400) {
                        break label82;
                     }
                  } catch (Exception var9) {
                     var10000 = var9;
                     var10001 = false;
                     break label81;
                  }
               }

               try {
                  this.framerate = 25;
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label81;
               }

               var3 = 9;
            }

            while(true) {
               label53: {
                  try {
                     if (var3 >= var13.length) {
                        return true;
                     }

                     if (this.originalPath == null) {
                        this.originalPath = var13[var3];
                        break label53;
                     }
                  } catch (Exception var7) {
                     var10000 = var7;
                     var10001 = false;
                     break;
                  }

                  try {
                     StringBuilder var4 = new StringBuilder();
                     var4.append(this.originalPath);
                     var4.append("_");
                     var4.append(var13[var3]);
                     this.originalPath = var4.toString();
                  } catch (Exception var6) {
                     var10000 = var6;
                     var10001 = false;
                     break;
                  }
               }

               ++var3;
            }
         }

         Exception var14 = var10000;
         FileLog.e((Throwable)var14);
         return false;
      }
   }
}
