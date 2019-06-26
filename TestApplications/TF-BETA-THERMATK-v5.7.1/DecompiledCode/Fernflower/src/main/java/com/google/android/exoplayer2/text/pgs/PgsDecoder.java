package com.google.android.exoplayer2.text.pgs;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;
import com.google.android.exoplayer2.text.Subtitle;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.zip.Inflater;

public final class PgsDecoder extends SimpleSubtitleDecoder {
   private final ParsableByteArray buffer = new ParsableByteArray();
   private final PgsDecoder.CueBuilder cueBuilder = new PgsDecoder.CueBuilder();
   private final ParsableByteArray inflatedBuffer = new ParsableByteArray();
   private Inflater inflater;

   public PgsDecoder() {
      super("PgsDecoder");
   }

   private void maybeInflateData(ParsableByteArray var1) {
      if (var1.bytesLeft() > 0 && var1.peekUnsignedByte() == 120) {
         if (this.inflater == null) {
            this.inflater = new Inflater();
         }

         if (Util.inflate(var1, this.inflatedBuffer, this.inflater)) {
            ParsableByteArray var2 = this.inflatedBuffer;
            var1.reset(var2.data, var2.limit());
         }
      }

   }

   private static Cue readNextSection(ParsableByteArray var0, PgsDecoder.CueBuilder var1) {
      int var2 = var0.limit();
      int var3 = var0.readUnsignedByte();
      int var4 = var0.readUnsignedShort();
      int var5 = var0.getPosition() + var4;
      Cue var6 = null;
      if (var5 > var2) {
         var0.setPosition(var2);
         return null;
      } else {
         Cue var7;
         if (var3 != 128) {
            switch(var3) {
            case 20:
               var1.parsePaletteSection(var0, var4);
               var7 = var6;
               break;
            case 21:
               var1.parseBitmapSection(var0, var4);
               var7 = var6;
               break;
            case 22:
               var1.parseIdentifierSection(var0, var4);
               var7 = var6;
               break;
            default:
               var7 = var6;
            }
         } else {
            var6 = var1.build();
            var1.reset();
            var7 = var6;
         }

         var0.setPosition(var5);
         return var7;
      }
   }

   protected Subtitle decode(byte[] var1, int var2, boolean var3) throws SubtitleDecoderException {
      this.buffer.reset(var1, var2);
      this.maybeInflateData(this.buffer);
      this.cueBuilder.reset();
      ArrayList var5 = new ArrayList();

      while(this.buffer.bytesLeft() >= 3) {
         Cue var4 = readNextSection(this.buffer, this.cueBuilder);
         if (var4 != null) {
            var5.add(var4);
         }
      }

      return new PgsSubtitle(Collections.unmodifiableList(var5));
   }

   private static final class CueBuilder {
      private final ParsableByteArray bitmapData = new ParsableByteArray();
      private int bitmapHeight;
      private int bitmapWidth;
      private int bitmapX;
      private int bitmapY;
      private final int[] colors = new int[256];
      private boolean colorsSet;
      private int planeHeight;
      private int planeWidth;

      public CueBuilder() {
      }

      private void parseBitmapSection(ParsableByteArray var1, int var2) {
         if (var2 >= 4) {
            var1.skipBytes(3);
            boolean var3;
            if ((var1.readUnsignedByte() & 128) != 0) {
               var3 = true;
            } else {
               var3 = false;
            }

            int var4 = var2 - 4;
            var2 = var4;
            if (var3) {
               if (var4 < 7) {
                  return;
               }

               var2 = var1.readUnsignedInt24();
               if (var2 < 4) {
                  return;
               }

               this.bitmapWidth = var1.readUnsignedShort();
               this.bitmapHeight = var1.readUnsignedShort();
               this.bitmapData.reset(var2 - 4);
               var2 = var4 - 7;
            }

            int var5 = this.bitmapData.getPosition();
            var4 = this.bitmapData.limit();
            if (var5 < var4 && var2 > 0) {
               var2 = Math.min(var2, var4 - var5);
               var1.readBytes(this.bitmapData.data, var5, var2);
               this.bitmapData.setPosition(var5 + var2);
            }

         }
      }

      private void parseIdentifierSection(ParsableByteArray var1, int var2) {
         if (var2 >= 19) {
            this.planeWidth = var1.readUnsignedShort();
            this.planeHeight = var1.readUnsignedShort();
            var1.skipBytes(11);
            this.bitmapX = var1.readUnsignedShort();
            this.bitmapY = var1.readUnsignedShort();
         }
      }

      private void parsePaletteSection(ParsableByteArray var1, int var2) {
         if (var2 % 5 == 2) {
            var1.skipBytes(2);
            Arrays.fill(this.colors, 0);
            int var3 = var2 / 5;

            for(var2 = 0; var2 < var3; ++var2) {
               int var4 = var1.readUnsignedByte();
               int var5 = var1.readUnsignedByte();
               int var6 = var1.readUnsignedByte();
               int var7 = var1.readUnsignedByte();
               int var8 = var1.readUnsignedByte();
               double var9 = (double)var5;
               double var11 = (double)(var6 - 128);
               Double.isNaN(var11);
               Double.isNaN(var9);
               var6 = (int)(1.402D * var11 + var9);
               double var13 = (double)(var7 - 128);
               Double.isNaN(var13);
               Double.isNaN(var9);
               Double.isNaN(var11);
               var5 = (int)(var9 - 0.34414D * var13 - var11 * 0.71414D);
               Double.isNaN(var13);
               Double.isNaN(var9);
               var7 = (int)(var9 + var13 * 1.772D);
               int[] var15 = this.colors;
               var6 = Util.constrainValue(var6, 0, 255);
               var5 = Util.constrainValue(var5, 0, 255);
               var15[var4] = Util.constrainValue(var7, 0, 255) | var5 << 8 | var8 << 24 | var6 << 16;
            }

            this.colorsSet = true;
         }
      }

      public Cue build() {
         if (this.planeWidth != 0 && this.planeHeight != 0 && this.bitmapWidth != 0 && this.bitmapHeight != 0 && this.bitmapData.limit() != 0 && this.bitmapData.getPosition() == this.bitmapData.limit() && this.colorsSet) {
            this.bitmapData.setPosition(0);
            int[] var1 = new int[this.bitmapWidth * this.bitmapHeight];
            int var2 = 0;

            int var4;
            while(var2 < var1.length) {
               int var3 = this.bitmapData.readUnsignedByte();
               if (var3 != 0) {
                  var4 = var2 + 1;
                  var1[var2] = this.colors[var3];
                  var2 = var4;
               } else {
                  var3 = this.bitmapData.readUnsignedByte();
                  if (var3 != 0) {
                     if ((var3 & 64) == 0) {
                        var4 = var3 & 63;
                     } else {
                        var4 = (var3 & 63) << 8 | this.bitmapData.readUnsignedByte();
                     }

                     if ((var3 & 128) == 0) {
                        var3 = 0;
                     } else {
                        var3 = this.colors[this.bitmapData.readUnsignedByte()];
                     }

                     var4 += var2;
                     Arrays.fill(var1, var2, var4, var3);
                     var2 = var4;
                  }
               }
            }

            Bitmap var7 = Bitmap.createBitmap(var1, this.bitmapWidth, this.bitmapHeight, Config.ARGB_8888);
            float var5 = (float)this.bitmapX;
            var2 = this.planeWidth;
            float var6 = var5 / (float)var2;
            var5 = (float)this.bitmapY;
            var4 = this.planeHeight;
            return new Cue(var7, var6, 0, var5 / (float)var4, 0, (float)this.bitmapWidth / (float)var2, (float)this.bitmapHeight / (float)var4);
         } else {
            return null;
         }
      }

      public void reset() {
         this.planeWidth = 0;
         this.planeHeight = 0;
         this.bitmapX = 0;
         this.bitmapY = 0;
         this.bitmapWidth = 0;
         this.bitmapHeight = 0;
         this.bitmapData.reset(0);
         this.colorsSet = false;
      }
   }
}
