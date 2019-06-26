package org.telegram.messenger.audioinfo.mp3;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import org.telegram.messenger.audioinfo.AudioInfo;

public class ID3v1Info extends AudioInfo {
   public ID3v1Info(InputStream var1) throws IOException {
      if (isID3v1StartPosition(var1)) {
         super.brand = "ID3";
         super.version = "1.0";
         byte[] var4 = this.readBytes(var1, 128);
         super.title = this.extractString(var4, 3, 30);
         super.artist = this.extractString(var4, 33, 30);
         super.album = this.extractString(var4, 63, 30);

         try {
            super.year = Short.parseShort(this.extractString(var4, 93, 4));
         } catch (NumberFormatException var3) {
            super.year = (short)0;
         }

         super.comment = this.extractString(var4, 97, 30);
         ID3v1Genre var2 = ID3v1Genre.getGenre(var4[127]);
         if (var2 != null) {
            super.genre = var2.getDescription();
         }

         if (var4[125] == 0 && var4[126] != 0) {
            super.version = "1.1";
            super.track = (short)((short)(var4[126] & 255));
         }
      }

   }

   public static boolean isID3v1StartPosition(InputStream var0) throws IOException {
      var0.mark(3);
      boolean var5 = false;

      boolean var2;
      label54: {
         label53: {
            int var1;
            try {
               var5 = true;
               if (var0.read() != 84) {
                  var5 = false;
                  break label53;
               }

               if (var0.read() != 65) {
                  var5 = false;
                  break label53;
               }

               var1 = var0.read();
               var5 = false;
            } finally {
               if (var5) {
                  var0.reset();
               }
            }

            if (var1 == true) {
               var2 = true;
               break label54;
            }
         }

         var2 = false;
      }

      var0.reset();
      return var2;
   }

   String extractString(byte[] param1, int param2, int param3) {
      // $FF: Couldn't be decompiled
   }

   byte[] readBytes(InputStream var1, int var2) throws IOException {
      byte[] var3 = new byte[var2];

      int var5;
      for(int var4 = 0; var4 < var2; var4 += var5) {
         var5 = var1.read(var3, var4, var2 - var4);
         if (var5 <= 0) {
            throw new EOFException();
         }
      }

      return var3;
   }
}
