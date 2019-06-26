package org.telegram.messenger.audioinfo.mp3;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class ID3v2DataInput {
   private final InputStream input;

   public ID3v2DataInput(InputStream var1) {
      this.input = var1;
   }

   public byte readByte() throws IOException {
      int var1 = this.input.read();
      if (var1 >= 0) {
         return (byte)var1;
      } else {
         throw new EOFException();
      }
   }

   public final void readFully(byte[] var1, int var2, int var3) throws IOException {
      int var5;
      for(int var4 = 0; var4 < var3; var4 += var5) {
         var5 = this.input.read(var1, var2 + var4, var3 - var4);
         if (var5 <= 0) {
            throw new EOFException();
         }
      }

   }

   public byte[] readFully(int var1) throws IOException {
      byte[] var2 = new byte[var1];
      this.readFully(var2, 0, var1);
      return var2;
   }

   public int readInt() throws IOException {
      return (this.readByte() & 255) << 24 | (this.readByte() & 255) << 16 | (this.readByte() & 255) << 8 | this.readByte() & 255;
   }

   public int readSyncsafeInt() throws IOException {
      return (this.readByte() & 127) << 21 | (this.readByte() & 127) << 14 | (this.readByte() & 127) << 7 | this.readByte() & 127;
   }

   public void skipFully(long var1) throws IOException {
      long var5;
      for(long var3 = 0L; var3 < var1; var3 += var5) {
         var5 = this.input.skip(var1 - var3);
         if (var5 <= 0L) {
            throw new EOFException();
         }
      }

   }
}
