package menion.android.whereyougo.openwig;

import cz.matejcik.openwig.platform.SeekableFile;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import menion.android.whereyougo.utils.Logger;

public class WSeekableFile implements SeekableFile {
   private static final String TAG = "WSeekableFile";
   private RandomAccessFile raf;

   public WSeekableFile(File var1) {
      try {
         RandomAccessFile var2 = new RandomAccessFile(var1, "rw");
         this.raf = var2;
      } catch (Exception var3) {
         Logger.e("WSeekableFile", "WSeekableFile(" + var1.getAbsolutePath() + ")", var3);
      }

   }

   private static double readDouble(byte[] var0, int var1, int var2) {
      long var3 = 0L;

      for(int var5 = 0; var5 < var2; ++var5) {
         var3 |= (long)(var0[var1 + var5] & 255) << var5 * 8;
      }

      return Double.longBitsToDouble(var3);
   }

   private static int readInt(byte[] var0, int var1, int var2) {
      int var3 = 0;

      for(int var4 = 0; var4 < var2; ++var4) {
         var3 += (var0[var1 + var4] & 255) << var4 * 8;
      }

      return var3;
   }

   private static long readLong(byte[] var0, int var1, int var2) {
      long var3 = 0L;

      for(int var5 = 0; var5 < var2; ++var5) {
         var3 |= (long)(var0[var1 + var5] & 255) << var5 * 8;
      }

      return var3;
   }

   public long position() throws IOException {
      return this.raf.getFilePointer();
   }

   public int read() throws IOException {
      return this.raf.read();
   }

   public double readDouble() throws IOException {
      double var2;
      try {
         byte[] var1 = new byte[8];
         this.raf.read(var1);
         var2 = readDouble(var1, 0, 8);
      } catch (Exception var4) {
         var2 = 0.0D;
      }

      return var2;
   }

   public void readFully(byte[] var1) throws IOException {
      this.raf.read(var1);
   }

   public int readInt() throws IOException {
      byte var1 = 0;

      int var3;
      try {
         byte[] var2 = new byte[4];
         this.raf.read(var2);
         var3 = readInt(var2, 0, 4);
      } catch (Exception var4) {
         var3 = var1;
      }

      return var3;
   }

   public long readLong() throws IOException {
      byte[] var1 = new byte[8];
      this.raf.read(var1);
      return readLong(var1, 0, 8);
   }

   public short readShort() throws IOException {
      byte[] var1 = new byte[2];
      this.raf.read(var1);
      return (short)(var1[1] << 8 | var1[0] & 255);
   }

   public String readString() throws IOException {
      StringBuilder var1 = new StringBuilder();

      for(int var2 = this.raf.read(); var2 > 0; var2 = this.raf.read()) {
         var1.append((char)var2);
      }

      return var1.toString();
   }

   public void seek(long var1) throws IOException {
      this.raf.seek(var1);
   }

   public long skip(long var1) throws IOException {
      return (long)this.raf.skipBytes((int)var1);
   }
}
