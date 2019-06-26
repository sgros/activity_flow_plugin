package org.telegram.messenger.audioinfo.mp3;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import org.telegram.messenger.audioinfo.util.PositionInputStream;

public class MP3Input extends PositionInputStream {
   public MP3Input(InputStream var1) throws IOException {
      super(var1);
   }

   public MP3Input(InputStream var1, long var2) {
      super(var1, var2);
   }

   public final void readFully(byte[] var1, int var2, int var3) throws IOException {
      int var5;
      for(int var4 = 0; var4 < var3; var4 += var5) {
         var5 = this.read(var1, var2 + var4, var3 - var4);
         if (var5 <= 0) {
            throw new EOFException();
         }
      }

   }

   public void skipFully(long var1) throws IOException {
      long var5;
      for(long var3 = 0L; var3 < var1; var3 += var5) {
         var5 = this.skip(var1 - var3);
         if (var5 <= 0L) {
            throw new EOFException();
         }
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("mp3[pos=");
      var1.append(this.getPosition());
      var1.append("]");
      return var1.toString();
   }
}
