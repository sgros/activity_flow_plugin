package com.google.android.exoplayer2.text.tx3g;

import android.text.SpannableStringBuilder;
import android.text.Layout.Alignment;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;
import com.google.android.exoplayer2.text.Subtitle;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.nio.charset.Charset;
import java.util.List;

public final class Tx3gDecoder extends SimpleSubtitleDecoder {
   private static final int TYPE_STYL = Util.getIntegerCodeForString("styl");
   private static final int TYPE_TBOX = Util.getIntegerCodeForString("tbox");
   private int calculatedVideoTrackHeight;
   private boolean customVerticalPlacement;
   private int defaultColorRgba;
   private int defaultFontFace;
   private String defaultFontFamily;
   private float defaultVerticalPlacement;
   private final ParsableByteArray parsableByteArray = new ParsableByteArray();

   public Tx3gDecoder(List var1) {
      super("Tx3gDecoder");
      this.decodeInitializationData(var1);
   }

   private void applyStyleRecord(ParsableByteArray var1, SpannableStringBuilder var2) throws SubtitleDecoderException {
      boolean var3;
      if (var1.bytesLeft() >= 12) {
         var3 = true;
      } else {
         var3 = false;
      }

      assertTrue(var3);
      int var4 = var1.readUnsignedShort();
      int var5 = var1.readUnsignedShort();
      var1.skipBytes(2);
      int var6 = var1.readUnsignedByte();
      var1.skipBytes(1);
      int var7 = var1.readInt();
      attachFontFace(var2, var6, this.defaultFontFace, var4, var5, 0);
      attachColor(var2, var7, this.defaultColorRgba, var4, var5, 0);
   }

   private static void assertTrue(boolean var0) throws SubtitleDecoderException {
      if (!var0) {
         throw new SubtitleDecoderException("Unexpected subtitle format.");
      }
   }

   private static void attachColor(SpannableStringBuilder var0, int var1, int var2, int var3, int var4, int var5) {
      if (var1 != var2) {
         var0.setSpan(new ForegroundColorSpan(var1 >>> 8 | (var1 & 255) << 24), var3, var4, var5 | 33);
      }

   }

   private static void attachFontFace(SpannableStringBuilder var0, int var1, int var2, int var3, int var4, int var5) {
      if (var1 != var2) {
         int var6 = var5 | 33;
         boolean var7 = true;
         boolean var9;
         if ((var1 & 1) != 0) {
            var9 = true;
         } else {
            var9 = false;
         }

         boolean var10;
         if ((var1 & 2) != 0) {
            var10 = true;
         } else {
            var10 = false;
         }

         if (var9) {
            if (var10) {
               var0.setSpan(new StyleSpan(3), var3, var4, var6);
            } else {
               var0.setSpan(new StyleSpan(1), var3, var4, var6);
            }
         } else if (var10) {
            var0.setSpan(new StyleSpan(2), var3, var4, var6);
         }

         boolean var8;
         if ((var1 & 4) != 0) {
            var8 = var7;
         } else {
            var8 = false;
         }

         if (var8) {
            var0.setSpan(new UnderlineSpan(), var3, var4, var6);
         }

         if (!var8 && !var9 && !var10) {
            var0.setSpan(new StyleSpan(0), var3, var4, var6);
         }
      }

   }

   private static void attachFontFamily(SpannableStringBuilder var0, String var1, String var2, int var3, int var4, int var5) {
      if (var1 != var2) {
         var0.setSpan(new TypefaceSpan(var1), var3, var4, var5 | 33);
      }

   }

   private void decodeInitializationData(List var1) {
      String var2 = "sans-serif";
      boolean var3 = false;
      if (var1 == null || var1.size() != 1 || ((byte[])var1.get(0)).length != 48 && ((byte[])var1.get(0)).length != 53) {
         this.defaultFontFace = 0;
         this.defaultColorRgba = -1;
         this.defaultFontFamily = "sans-serif";
         this.customVerticalPlacement = false;
         this.defaultVerticalPlacement = 0.85F;
      } else {
         byte[] var4 = (byte[])var1.get(0);
         this.defaultFontFace = var4[24];
         this.defaultColorRgba = (var4[26] & 255) << 24 | (var4[27] & 255) << 16 | (var4[28] & 255) << 8 | var4[29] & 255;
         String var6 = var2;
         if ("Serif".equals(Util.fromUtf8Bytes(var4, 43, var4.length - 43))) {
            var6 = "serif";
         }

         this.defaultFontFamily = var6;
         this.calculatedVideoTrackHeight = var4[25] * 20;
         if ((var4[0] & 32) != 0) {
            var3 = true;
         }

         this.customVerticalPlacement = var3;
         if (this.customVerticalPlacement) {
            byte var5 = var4[10];
            this.defaultVerticalPlacement = (float)(var4[11] & 255 | (var5 & 255) << 8) / (float)this.calculatedVideoTrackHeight;
            this.defaultVerticalPlacement = Util.constrainValue(this.defaultVerticalPlacement, 0.0F, 0.95F);
         } else {
            this.defaultVerticalPlacement = 0.85F;
         }
      }

   }

   private static String readSubtitleText(ParsableByteArray var0) throws SubtitleDecoderException {
      boolean var1;
      if (var0.bytesLeft() >= 2) {
         var1 = true;
      } else {
         var1 = false;
      }

      assertTrue(var1);
      int var2 = var0.readUnsignedShort();
      if (var2 == 0) {
         return "";
      } else {
         if (var0.bytesLeft() >= 2) {
            char var3 = var0.peekChar();
            if (var3 == '\ufeff' || var3 == '\ufffe') {
               return var0.readString(var2, Charset.forName("UTF-16"));
            }
         }

         return var0.readString(var2, Charset.forName("UTF-8"));
      }
   }

   protected Subtitle decode(byte[] var1, int var2, boolean var3) throws SubtitleDecoderException {
      this.parsableByteArray.reset(var1, var2);
      String var11 = readSubtitleText(this.parsableByteArray);
      if (var11.isEmpty()) {
         return Tx3gSubtitle.EMPTY;
      } else {
         SpannableStringBuilder var12 = new SpannableStringBuilder(var11);
         attachFontFace(var12, this.defaultFontFace, 0, 0, var12.length(), 16711680);
         attachColor(var12, this.defaultColorRgba, -1, 0, var12.length(), 16711680);
         attachFontFamily(var12, this.defaultFontFamily, "sans-serif", 0, var12.length(), 16711680);

         float var4;
         float var10;
         for(var4 = this.defaultVerticalPlacement; this.parsableByteArray.bytesLeft() >= 8; var4 = var10) {
            int var5 = this.parsableByteArray.getPosition();
            int var6 = this.parsableByteArray.readInt();
            int var7 = this.parsableByteArray.readInt();
            int var8 = TYPE_STYL;
            boolean var9 = true;
            var3 = true;
            var2 = 0;
            if (var7 == var8) {
               if (this.parsableByteArray.bytesLeft() < 2) {
                  var3 = false;
               }

               assertTrue(var3);
               var7 = this.parsableByteArray.readUnsignedShort();

               while(true) {
                  var10 = var4;
                  if (var2 >= var7) {
                     break;
                  }

                  this.applyStyleRecord(this.parsableByteArray, var12);
                  ++var2;
               }
            } else {
               var10 = var4;
               if (var7 == TYPE_TBOX) {
                  var10 = var4;
                  if (this.customVerticalPlacement) {
                     if (this.parsableByteArray.bytesLeft() >= 2) {
                        var3 = var9;
                     } else {
                        var3 = false;
                     }

                     assertTrue(var3);
                     var10 = Util.constrainValue((float)this.parsableByteArray.readUnsignedShort() / (float)this.calculatedVideoTrackHeight, 0.0F, 0.95F);
                  }
               }
            }

            this.parsableByteArray.setPosition(var5 + var6);
         }

         return new Tx3gSubtitle(new Cue(var12, (Alignment)null, var4, 0, 0, Float.MIN_VALUE, Integer.MIN_VALUE, Float.MIN_VALUE));
      }
   }
}
