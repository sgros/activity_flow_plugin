package com.google.android.exoplayer2.extractor.mkv;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import java.util.ArrayDeque;

final class DefaultEbmlReader implements EbmlReader {
   private long elementContentSize;
   private int elementId;
   private int elementState;
   private final ArrayDeque masterElementsStack = new ArrayDeque();
   private EbmlReaderOutput output;
   private final byte[] scratch = new byte[8];
   private final VarintReader varintReader = new VarintReader();

   public DefaultEbmlReader() {
   }

   private long maybeResyncToNextLevel1Element(ExtractorInput var1) throws IOException, InterruptedException {
      var1.resetPeekPosition();

      while(true) {
         var1.peekFully(this.scratch, 0, 4);
         int var2 = VarintReader.parseUnsignedVarintLength(this.scratch[0]);
         if (var2 != -1 && var2 <= 4) {
            int var3 = (int)VarintReader.assembleVarint(this.scratch, var2, false);
            if (this.output.isLevel1Element(var3)) {
               var1.skipFully(var2);
               return (long)var3;
            }
         }

         var1.skipFully(1);
      }
   }

   private double readFloat(ExtractorInput var1, int var2) throws IOException, InterruptedException {
      long var3 = this.readInteger(var1, var2);
      double var5;
      if (var2 == 4) {
         var5 = (double)Float.intBitsToFloat((int)var3);
      } else {
         var5 = Double.longBitsToDouble(var3);
      }

      return var5;
   }

   private long readInteger(ExtractorInput var1, int var2) throws IOException, InterruptedException {
      byte[] var3 = this.scratch;
      int var4 = 0;
      var1.readFully(var3, 0, var2);

      long var5;
      for(var5 = 0L; var4 < var2; ++var4) {
         var5 = var5 << 8 | (long)(this.scratch[var4] & 255);
      }

      return var5;
   }

   private String readString(ExtractorInput var1, int var2) throws IOException, InterruptedException {
      if (var2 == 0) {
         return "";
      } else {
         byte[] var3 = new byte[var2];
         var1.readFully(var3, 0, var2);

         while(var2 > 0 && var3[var2 - 1] == 0) {
            --var2;
         }

         return new String(var3, 0, var2);
      }
   }

   public void init(EbmlReaderOutput var1) {
      this.output = var1;
   }

   public boolean read(ExtractorInput var1) throws IOException, InterruptedException {
      boolean var2;
      if (this.output != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      Assertions.checkState(var2);

      while(this.masterElementsStack.isEmpty() || var1.getPosition() < ((DefaultEbmlReader.MasterElement)this.masterElementsStack.peek()).elementEndPosition) {
         long var3;
         long var5;
         if (this.elementState == 0) {
            var3 = this.varintReader.readUnsignedVarint(var1, true, false, 4);
            var5 = var3;
            if (var3 == -2L) {
               var5 = this.maybeResyncToNextLevel1Element(var1);
            }

            if (var5 == -1L) {
               return false;
            }

            this.elementId = (int)var5;
            this.elementState = 1;
         }

         if (this.elementState == 1) {
            this.elementContentSize = this.varintReader.readUnsignedVarint(var1, false, true, 8);
            this.elementState = 2;
         }

         int var7 = this.output.getElementType(this.elementId);
         if (var7 != 0) {
            if (var7 != 1) {
               StringBuilder var8;
               if (var7 != 2) {
                  if (var7 != 3) {
                     if (var7 != 4) {
                        if (var7 == 5) {
                           var5 = this.elementContentSize;
                           if (var5 != 4L && var5 != 8L) {
                              var8 = new StringBuilder();
                              var8.append("Invalid float size: ");
                              var8.append(this.elementContentSize);
                              throw new ParserException(var8.toString());
                           }

                           this.output.floatElement(this.elementId, this.readFloat(var1, (int)this.elementContentSize));
                           this.elementState = 0;
                           return true;
                        }

                        var8 = new StringBuilder();
                        var8.append("Invalid element type ");
                        var8.append(var7);
                        throw new ParserException(var8.toString());
                     }

                     this.output.binaryElement(this.elementId, (int)this.elementContentSize, var1);
                     this.elementState = 0;
                     return true;
                  }

                  var5 = this.elementContentSize;
                  if (var5 <= 2147483647L) {
                     this.output.stringElement(this.elementId, this.readString(var1, (int)var5));
                     this.elementState = 0;
                     return true;
                  }

                  var8 = new StringBuilder();
                  var8.append("String element size: ");
                  var8.append(this.elementContentSize);
                  throw new ParserException(var8.toString());
               }

               var5 = this.elementContentSize;
               if (var5 <= 8L) {
                  this.output.integerElement(this.elementId, this.readInteger(var1, (int)var5));
                  this.elementState = 0;
                  return true;
               }

               var8 = new StringBuilder();
               var8.append("Invalid integer size: ");
               var8.append(this.elementContentSize);
               throw new ParserException(var8.toString());
            }

            var3 = var1.getPosition();
            var5 = this.elementContentSize;
            this.masterElementsStack.push(new DefaultEbmlReader.MasterElement(this.elementId, var5 + var3));
            this.output.startMasterElement(this.elementId, var3, this.elementContentSize);
            this.elementState = 0;
            return true;
         }

         var1.skipFully((int)this.elementContentSize);
         this.elementState = 0;
      }

      this.output.endMasterElement(((DefaultEbmlReader.MasterElement)this.masterElementsStack.pop()).elementId);
      return true;
   }

   public void reset() {
      this.elementState = 0;
      this.masterElementsStack.clear();
      this.varintReader.reset();
   }

   private static final class MasterElement {
      private final long elementEndPosition;
      private final int elementId;

      private MasterElement(int var1, long var2) {
         this.elementId = var1;
         this.elementEndPosition = var2;
      }

      // $FF: synthetic method
      MasterElement(int var1, long var2, Object var4) {
         this(var1, var2);
      }
   }
}
