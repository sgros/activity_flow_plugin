package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;

public final class DefaultExtractorInput implements ExtractorInput {
   private final DataSource dataSource;
   private byte[] peekBuffer;
   private int peekBufferLength;
   private int peekBufferPosition;
   private long position;
   private final byte[] scratchSpace;
   private final long streamLength;

   public DefaultExtractorInput(DataSource var1, long var2, long var4) {
      this.dataSource = var1;
      this.position = var2;
      this.streamLength = var4;
      this.peekBuffer = new byte[65536];
      this.scratchSpace = new byte[4096];
   }

   private void commitBytesRead(int var1) {
      if (var1 != -1) {
         this.position += (long)var1;
      }

   }

   private void ensureSpaceForPeek(int var1) {
      var1 += this.peekBufferPosition;
      byte[] var2 = this.peekBuffer;
      if (var1 > var2.length) {
         var1 = Util.constrainValue(var2.length * 2, 65536 + var1, var1 + 524288);
         this.peekBuffer = Arrays.copyOf(this.peekBuffer, var1);
      }

   }

   private int readFromDataSource(byte[] var1, int var2, int var3, int var4, boolean var5) throws InterruptedException, IOException {
      if (!Thread.interrupted()) {
         var2 = this.dataSource.read(var1, var2 + var4, var3 - var4);
         if (var2 == -1) {
            if (var4 == 0 && var5) {
               return -1;
            } else {
               throw new EOFException();
            }
         } else {
            return var4 + var2;
         }
      } else {
         throw new InterruptedException();
      }
   }

   private int readFromPeekBuffer(byte[] var1, int var2, int var3) {
      int var4 = this.peekBufferLength;
      if (var4 == 0) {
         return 0;
      } else {
         var3 = Math.min(var4, var3);
         System.arraycopy(this.peekBuffer, 0, var1, var2, var3);
         this.updatePeekBuffer(var3);
         return var3;
      }
   }

   private int skipFromPeekBuffer(int var1) {
      var1 = Math.min(this.peekBufferLength, var1);
      this.updatePeekBuffer(var1);
      return var1;
   }

   private void updatePeekBuffer(int var1) {
      this.peekBufferLength -= var1;
      this.peekBufferPosition = 0;
      byte[] var2 = this.peekBuffer;
      int var3 = this.peekBufferLength;
      byte[] var4 = var2;
      if (var3 < var2.length - 524288) {
         var4 = new byte[var3 + 65536];
      }

      System.arraycopy(this.peekBuffer, var1, var4, 0, this.peekBufferLength);
      this.peekBuffer = var4;
   }

   public void advancePeekPosition(int var1) throws IOException, InterruptedException {
      this.advancePeekPosition(var1, false);
   }

   public boolean advancePeekPosition(int var1, boolean var2) throws IOException, InterruptedException {
      this.ensureSpaceForPeek(var1);

      for(int var3 = this.peekBufferLength - this.peekBufferPosition; var3 < var1; this.peekBufferLength = this.peekBufferPosition + var3) {
         var3 = this.readFromDataSource(this.peekBuffer, this.peekBufferPosition, var1, var3, var2);
         if (var3 == -1) {
            return false;
         }
      }

      this.peekBufferPosition += var1;
      return true;
   }

   public long getLength() {
      return this.streamLength;
   }

   public long getPeekPosition() {
      return this.position + (long)this.peekBufferPosition;
   }

   public long getPosition() {
      return this.position;
   }

   public void peekFully(byte[] var1, int var2, int var3) throws IOException, InterruptedException {
      this.peekFully(var1, var2, var3, false);
   }

   public boolean peekFully(byte[] var1, int var2, int var3, boolean var4) throws IOException, InterruptedException {
      if (!this.advancePeekPosition(var3, var4)) {
         return false;
      } else {
         System.arraycopy(this.peekBuffer, this.peekBufferPosition - var3, var1, var2, var3);
         return true;
      }
   }

   public int read(byte[] var1, int var2, int var3) throws IOException, InterruptedException {
      int var4 = this.readFromPeekBuffer(var1, var2, var3);
      int var5 = var4;
      if (var4 == 0) {
         var5 = this.readFromDataSource(var1, var2, var3, 0, true);
      }

      this.commitBytesRead(var5);
      return var5;
   }

   public void readFully(byte[] var1, int var2, int var3) throws IOException, InterruptedException {
      this.readFully(var1, var2, var3, false);
   }

   public boolean readFully(byte[] var1, int var2, int var3, boolean var4) throws IOException, InterruptedException {
      int var5;
      for(var5 = this.readFromPeekBuffer(var1, var2, var3); var5 < var3 && var5 != -1; var5 = this.readFromDataSource(var1, var2, var3, var5, var4)) {
      }

      this.commitBytesRead(var5);
      if (var5 != -1) {
         var4 = true;
      } else {
         var4 = false;
      }

      return var4;
   }

   public void resetPeekPosition() {
      this.peekBufferPosition = 0;
   }

   public void setRetryPosition(long var1, Throwable var3) throws Throwable {
      boolean var4;
      if (var1 >= 0L) {
         var4 = true;
      } else {
         var4 = false;
      }

      Assertions.checkArgument(var4);
      this.position = var1;
      throw var3;
   }

   public int skip(int var1) throws IOException, InterruptedException {
      int var2 = this.skipFromPeekBuffer(var1);
      int var3 = var2;
      if (var2 == 0) {
         byte[] var4 = this.scratchSpace;
         var3 = this.readFromDataSource(var4, 0, Math.min(var1, var4.length), 0, true);
      }

      this.commitBytesRead(var3);
      return var3;
   }

   public void skipFully(int var1) throws IOException, InterruptedException {
      this.skipFully(var1, false);
   }

   public boolean skipFully(int var1, boolean var2) throws IOException, InterruptedException {
      int var3;
      int var4;
      for(var3 = this.skipFromPeekBuffer(var1); var3 < var1 && var3 != -1; var3 = this.readFromDataSource(this.scratchSpace, -var3, var4, var3, var2)) {
         var4 = Math.min(var1, this.scratchSpace.length + var3);
      }

      this.commitBytesRead(var3);
      if (var3 != -1) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }
}
