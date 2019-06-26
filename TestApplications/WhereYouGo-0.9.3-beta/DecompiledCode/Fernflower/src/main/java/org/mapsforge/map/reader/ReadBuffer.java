package org.mapsforge.map.reader;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

public class ReadBuffer {
   private static final String CHARSET_UTF8 = "UTF-8";
   private static final Logger LOGGER = Logger.getLogger(ReadBuffer.class.getName());
   static final int MAXIMUM_BUFFER_SIZE = 2500000;
   private byte[] bufferData;
   private int bufferPosition;
   private final RandomAccessFile inputFile;

   ReadBuffer(RandomAccessFile var1) {
      this.inputFile = var1;
   }

   int getBufferPosition() {
      return this.bufferPosition;
   }

   int getBufferSize() {
      return this.bufferData.length;
   }

   public byte readByte() {
      byte[] var1 = this.bufferData;
      int var2 = this.bufferPosition++;
      return var1[var2];
   }

   public boolean readFromFile(int var1) throws IOException {
      boolean var2 = false;
      if (this.bufferData == null || this.bufferData.length < var1) {
         if (var1 > 2500000) {
            LOGGER.warning("invalid read length: " + var1);
            return var2;
         }

         this.bufferData = new byte[var1];
      }

      this.bufferPosition = 0;
      if (this.inputFile.read(this.bufferData, 0, var1) == var1) {
         var2 = true;
      }

      return var2;
   }

   public int readInt() {
      this.bufferPosition += 4;
      return Deserializer.getInt(this.bufferData, this.bufferPosition - 4);
   }

   public long readLong() {
      this.bufferPosition += 8;
      return Deserializer.getLong(this.bufferData, this.bufferPosition - 8);
   }

   public int readShort() {
      this.bufferPosition += 2;
      return Deserializer.getShort(this.bufferData, this.bufferPosition - 2);
   }

   public int readSignedInt() {
      int var1 = 0;

      byte var2;
      byte[] var3;
      int var4;
      for(var2 = 0; (this.bufferData[this.bufferPosition] & 128) != 0; var2 = (byte)(var2 + 7)) {
         var3 = this.bufferData;
         var4 = this.bufferPosition++;
         var1 |= (var3[var4] & 127) << var2;
      }

      int var5;
      if ((this.bufferData[this.bufferPosition] & 64) != 0) {
         var3 = this.bufferData;
         var4 = this.bufferPosition++;
         var5 = -((var3[var4] & 63) << var2 | var1);
      } else {
         var3 = this.bufferData;
         var4 = this.bufferPosition++;
         var5 = (var3[var4] & 63) << var2 | var1;
      }

      return var5;
   }

   public String readUTF8EncodedString() {
      return this.readUTF8EncodedString(this.readUnsignedInt());
   }

   public String readUTF8EncodedString(int var1) {
      String var2;
      if (var1 > 0 && this.bufferPosition + var1 <= this.bufferData.length) {
         this.bufferPosition += var1;

         try {
            var2 = new String(this.bufferData, this.bufferPosition - var1, var1, "UTF-8");
         } catch (UnsupportedEncodingException var3) {
            throw new IllegalStateException(var3);
         }
      } else {
         LOGGER.warning("invalid string length: " + var1);
         var2 = null;
      }

      return var2;
   }

   public int readUnsignedInt() {
      int var1 = 0;

      byte var2;
      byte[] var3;
      int var4;
      for(var2 = 0; (this.bufferData[this.bufferPosition] & 128) != 0; var2 = (byte)(var2 + 7)) {
         var3 = this.bufferData;
         var4 = this.bufferPosition++;
         var1 |= (var3[var4] & 127) << var2;
      }

      var3 = this.bufferData;
      var4 = this.bufferPosition++;
      return var3[var4] << var2 | var1;
   }

   void setBufferPosition(int var1) {
      this.bufferPosition = var1;
   }

   void skipBytes(int var1) {
      this.bufferPosition += var1;
   }
}
