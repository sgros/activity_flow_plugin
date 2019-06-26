package org.telegram.messenger.audioinfo.m4a;

import java.io.EOFException;
import java.io.IOException;
import java.math.BigDecimal;
import org.telegram.messenger.audioinfo.util.RangeInputStream;

public class MP4Atom extends MP4Box {
   public MP4Atom(RangeInputStream var1, MP4Box var2, String var3) {
      super(var1, var2, var3);
   }

   private StringBuffer appendPath(StringBuffer var1, MP4Box var2) {
      if (var2.getParent() != null) {
         this.appendPath(var1, var2.getParent());
         var1.append("/");
      }

      var1.append(var2.getType());
      return var1;
   }

   public long getLength() {
      return ((RangeInputStream)this.getInput()).getPosition() + ((RangeInputStream)this.getInput()).getRemainingLength();
   }

   public long getOffset() {
      return this.getParent().getPosition() - this.getPosition();
   }

   public String getPath() {
      StringBuffer var1 = new StringBuffer();
      this.appendPath(var1, this);
      return var1.toString();
   }

   public long getRemaining() {
      return ((RangeInputStream)this.getInput()).getRemainingLength();
   }

   public boolean hasMoreChildren() {
      long var1;
      if (this.getChild() != null) {
         var1 = this.getChild().getRemaining();
      } else {
         var1 = 0L;
      }

      boolean var3;
      if (var1 < this.getRemaining()) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public MP4Atom nextChildUpTo(String var1) throws IOException {
      while(true) {
         if (this.getRemaining() > 0L) {
            MP4Atom var3 = this.nextChild();
            if (!var3.getType().matches(var1)) {
               continue;
            }

            return var3;
         }

         StringBuilder var2 = new StringBuilder();
         var2.append("atom type mismatch, not found: ");
         var2.append(var1);
         throw new IOException(var2.toString());
      }
   }

   public boolean readBoolean() throws IOException {
      return super.data.readBoolean();
   }

   public byte readByte() throws IOException {
      return super.data.readByte();
   }

   public byte[] readBytes() throws IOException {
      return this.readBytes((int)this.getRemaining());
   }

   public byte[] readBytes(int var1) throws IOException {
      byte[] var2 = new byte[var1];
      super.data.readFully(var2);
      return var2;
   }

   public int readInt() throws IOException {
      return super.data.readInt();
   }

   public BigDecimal readIntegerFixedPoint() throws IOException {
      short var1 = super.data.readShort();
      int var2 = super.data.readUnsignedShort();
      StringBuilder var3 = new StringBuilder();
      var3.append(String.valueOf(var1));
      var3.append("");
      var3.append(String.valueOf(var2));
      return new BigDecimal(var3.toString());
   }

   public long readLong() throws IOException {
      return super.data.readLong();
   }

   public short readShort() throws IOException {
      return super.data.readShort();
   }

   public BigDecimal readShortFixedPoint() throws IOException {
      byte var1 = super.data.readByte();
      int var2 = super.data.readUnsignedByte();
      StringBuilder var3 = new StringBuilder();
      var3.append(String.valueOf(var1));
      var3.append("");
      var3.append(String.valueOf(var2));
      return new BigDecimal(var3.toString());
   }

   public String readString(int var1, String var2) throws IOException {
      var2 = new String(this.readBytes(var1), var2);
      var1 = var2.indexOf(0);
      if (var1 >= 0) {
         var2 = var2.substring(0, var1);
      }

      return var2;
   }

   public String readString(String var1) throws IOException {
      return this.readString((int)this.getRemaining(), var1);
   }

   public void skip() throws IOException {
      while(true) {
         if (this.getRemaining() > 0L) {
            if (((RangeInputStream)this.getInput()).skip(this.getRemaining()) != 0L) {
               continue;
            }

            throw new EOFException("Cannot skip atom");
         }

         return;
      }
   }

   public void skip(int var1) throws IOException {
      int var3;
      for(int var2 = 0; var2 < var1; var2 += var3) {
         var3 = super.data.skipBytes(var1 - var2);
         if (var3 <= 0) {
            throw new EOFException();
         }
      }

   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      this.appendPath(var1, this);
      var1.append("[off=");
      var1.append(this.getOffset());
      var1.append(",pos=");
      var1.append(this.getPosition());
      var1.append(",len=");
      var1.append(this.getLength());
      var1.append("]");
      return var1.toString();
   }
}
