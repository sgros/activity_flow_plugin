package org.telegram.messenger.audioinfo.m4a;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.telegram.messenger.audioinfo.AudioInfo;
import org.telegram.messenger.audioinfo.mp3.ID3v1Genre;

public class M4AInfo extends AudioInfo {
   private static final String ASCII = "ISO8859_1";
   static final Logger LOGGER = Logger.getLogger(M4AInfo.class.getName());
   private static final String UTF_8 = "UTF-8";
   private final Level debugLevel;
   private byte rating;
   private BigDecimal speed;
   private short tempo;
   private BigDecimal volume;

   public M4AInfo(InputStream var1) throws IOException {
      this(var1, Level.FINEST);
   }

   public M4AInfo(InputStream var1, Level var2) throws IOException {
      this.debugLevel = var2;
      MP4Input var3 = new MP4Input(var1);
      if (LOGGER.isLoggable(var2)) {
         LOGGER.log(var2, var3.toString());
      }

      this.ftyp(var3.nextChild("ftyp"));
      this.moov(var3.nextChildUpTo("moov"));
   }

   void data(MP4Atom var1) throws IOException {
      if (LOGGER.isLoggable(this.debugLevel)) {
         LOGGER.log(this.debugLevel, var1.toString());
      }

      String var2;
      byte var3;
      label195: {
         var1.skip(4);
         var1.skip(4);
         var2 = var1.getParent().getType();
         switch(var2.hashCode()) {
         case 2954818:
            if (var2.equals("aART")) {
               var3 = 1;
               break label195;
            }
            break;
         case 3059752:
            if (var2.equals("covr")) {
               var3 = 6;
               break label195;
            }
            break;
         case 3060304:
            if (var2.equals("cpil")) {
               var3 = 7;
               break label195;
            }
            break;
         case 3060591:
            if (var2.equals("cprt")) {
               var3 = 8;
               break label195;
            }
            break;
         case 3083677:
            if (var2.equals("disk")) {
               var3 = 11;
               break label195;
            }
            break;
         case 3177818:
            if (var2.equals("gnre")) {
               var3 = 12;
               break label195;
            }
            break;
         case 3511163:
            if (var2.equals("rtng")) {
               var3 = 17;
               break label195;
            }
            break;
         case 3564088:
            if (var2.equals("tmpo")) {
               var3 = 18;
               break label195;
            }
            break;
         case 3568737:
            if (var2.equals("trkn")) {
               var3 = 19;
               break label195;
            }
            break;
         case 5099770:
            if (var2.equals("©ART")) {
               var3 = 2;
               break label195;
            }
            break;
         case 5131342:
            if (var2.equals("©alb")) {
               var3 = 0;
               break label195;
            }
            break;
         case 5133313:
            if (var2.equals("©cmt")) {
               var3 = 3;
               break label195;
            }
            break;
         case 5133368:
            if (var2.equals("©com")) {
               var3 = 4;
               break label195;
            }
            break;
         case 5133411:
            if (var2.equals("©cpy")) {
               var3 = 9;
               break label195;
            }
            break;
         case 5133907:
            if (var2.equals("©day")) {
               var3 = 10;
               break label195;
            }
            break;
         case 5136903:
            if (var2.equals("©gen")) {
               var3 = 13;
               break label195;
            }
            break;
         case 5137308:
            if (var2.equals("©grp")) {
               var3 = 14;
               break label195;
            }
            break;
         case 5142332:
            if (var2.equals("©lyr")) {
               var3 = 15;
               break label195;
            }
            break;
         case 5143505:
            if (var2.equals("©nam")) {
               var3 = 16;
               break label195;
            }
            break;
         case 5152688:
            if (var2.equals("©wrt")) {
               var3 = 5;
               break label195;
            }
         }

         var3 = -1;
      }

      switch(var3) {
      case 0:
         super.album = var1.readString("UTF-8");
         break;
      case 1:
         super.albumArtist = var1.readString("UTF-8");
         break;
      case 2:
         super.artist = var1.readString("UTF-8");
         break;
      case 3:
         super.comment = var1.readString("UTF-8");
         break;
      case 4:
      case 5:
         var2 = super.composer;
         if (var2 == null || var2.trim().length() == 0) {
            super.composer = var1.readString("UTF-8");
         }
         break;
      case 6:
         Exception var10000;
         label199: {
            byte[] var15;
            Options var17;
            boolean var10001;
            label200: {
               try {
                  var15 = var1.readBytes();
                  var17 = new Options();
                  var17.inJustDecodeBounds = true;
                  var17.inSampleSize = 1;
                  BitmapFactory.decodeByteArray(var15, 0, var15.length, var17);
                  if (var17.outWidth <= 800 && var17.outHeight <= 800) {
                     break label200;
                  }
               } catch (Exception var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label199;
               }

               int var18;
               try {
                  var18 = Math.max(var17.outWidth, var17.outHeight);
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label199;
               }

               while(var18 > 800) {
                  try {
                     var17.inSampleSize *= 2;
                     var18 /= 2;
                  } catch (Exception var10) {
                     var10000 = var10;
                     var10001 = false;
                     break label199;
                  }
               }
            }

            float var4;
            try {
               var17.inJustDecodeBounds = false;
               super.cover = BitmapFactory.decodeByteArray(var15, 0, var15.length, var17);
               if (super.cover == null) {
                  break;
               }

               var4 = (float)Math.max(super.cover.getWidth(), super.cover.getHeight()) / 120.0F;
            } catch (Exception var9) {
               var10000 = var9;
               var10001 = false;
               break label199;
            }

            if (var4 > 0.0F) {
               try {
                  super.smallCover = Bitmap.createScaledBitmap(super.cover, (int)((float)super.cover.getWidth() / var4), (int)((float)super.cover.getHeight() / var4), true);
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label199;
               }
            } else {
               try {
                  super.smallCover = super.cover;
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label199;
               }
            }

            try {
               if (super.smallCover == null) {
                  super.smallCover = super.cover;
               }
               break;
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
            }
         }

         Exception var16 = var10000;
         var16.printStackTrace();
         break;
      case 7:
         super.compilation = var1.readBoolean();
         break;
      case 8:
      case 9:
         var2 = super.copyright;
         if (var2 == null || var2.trim().length() == 0) {
            super.copyright = var1.readString("UTF-8");
         }
         break;
      case 10:
         String var14 = var1.readString("UTF-8").trim();
         if (var14.length() >= 4) {
            try {
               super.year = Short.valueOf(var14.substring(0, 4));
            } catch (NumberFormatException var5) {
            }
         }
         break;
      case 11:
         var1.skip(2);
         super.disc = var1.readShort();
         super.discs = var1.readShort();
         break;
      case 12:
         var2 = super.genre;
         if (var2 == null || var2.trim().length() == 0) {
            if (var1.getRemaining() == 2L) {
               ID3v1Genre var13 = ID3v1Genre.getGenre(var1.readShort() - 1);
               if (var13 != null) {
                  super.genre = var13.getDescription();
               }
            } else {
               super.genre = var1.readString("UTF-8");
            }
         }
         break;
      case 13:
         var2 = super.genre;
         if (var2 == null || var2.trim().length() == 0) {
            super.genre = var1.readString("UTF-8");
         }
         break;
      case 14:
         super.grouping = var1.readString("UTF-8");
         break;
      case 15:
         super.lyrics = var1.readString("UTF-8");
         break;
      case 16:
         super.title = var1.readString("UTF-8");
         break;
      case 17:
         this.rating = var1.readByte();
         break;
      case 18:
         this.tempo = var1.readShort();
         break;
      case 19:
         var1.skip(2);
         super.track = var1.readShort();
         super.tracks = var1.readShort();
      }

   }

   void ftyp(MP4Atom var1) throws IOException {
      if (LOGGER.isLoggable(this.debugLevel)) {
         LOGGER.log(this.debugLevel, var1.toString());
      }

      super.brand = var1.readString(4, "ISO8859_1").trim();
      if (super.brand.matches("M4V|MP4|mp42|isom")) {
         Logger var2 = LOGGER;
         StringBuilder var3 = new StringBuilder();
         var3.append(var1.getPath());
         var3.append(": brand=");
         var3.append(super.brand);
         var3.append(" (experimental)");
         var2.warning(var3.toString());
      } else if (!super.brand.matches("M4A|M4P")) {
         Logger var5 = LOGGER;
         StringBuilder var4 = new StringBuilder();
         var4.append(var1.getPath());
         var4.append(": brand=");
         var4.append(super.brand);
         var4.append(" (expected M4A or M4P)");
         var5.warning(var4.toString());
      }

      super.version = String.valueOf(var1.readInt());
   }

   public byte getRating() {
      return this.rating;
   }

   public BigDecimal getSpeed() {
      return this.speed;
   }

   public short getTempo() {
      return this.tempo;
   }

   public BigDecimal getVolume() {
      return this.volume;
   }

   void ilst(MP4Atom var1) throws IOException {
      if (LOGGER.isLoggable(this.debugLevel)) {
         LOGGER.log(this.debugLevel, var1.toString());
      }

      while(var1.hasMoreChildren()) {
         MP4Atom var2 = var1.nextChild();
         if (LOGGER.isLoggable(this.debugLevel)) {
            LOGGER.log(this.debugLevel, var2.toString());
         }

         if (var2.getRemaining() == 0L) {
            if (LOGGER.isLoggable(this.debugLevel)) {
               Logger var3 = LOGGER;
               Level var4 = this.debugLevel;
               StringBuilder var5 = new StringBuilder();
               var5.append(var2.getPath());
               var5.append(": contains no value");
               var3.log(var4, var5.toString());
            }
         } else {
            this.data(var2.nextChildUpTo("data"));
         }
      }

   }

   void mdhd(MP4Atom var1) throws IOException {
      if (LOGGER.isLoggable(this.debugLevel)) {
         LOGGER.log(this.debugLevel, var1.toString());
      }

      byte var2 = var1.readByte();
      var1.skip(3);
      byte var3;
      if (var2 == 1) {
         var3 = 16;
      } else {
         var3 = 8;
      }

      var1.skip(var3);
      int var11 = var1.readInt();
      long var4;
      if (var2 == 1) {
         var4 = var1.readLong();
      } else {
         var4 = (long)var1.readInt();
      }

      if (super.duration == 0L) {
         super.duration = var4 * 1000L / (long)var11;
      } else if (LOGGER.isLoggable(this.debugLevel)) {
         long var6 = super.duration;
         var4 = var4 * 1000L / (long)var11;
         if (Math.abs(var6 - var4) > 2L) {
            Logger var8 = LOGGER;
            Level var10 = this.debugLevel;
            StringBuilder var9 = new StringBuilder();
            var9.append("mdhd: duration ");
            var9.append(super.duration);
            var9.append(" -> ");
            var9.append(var4);
            var8.log(var10, var9.toString());
         }
      }

   }

   void mdia(MP4Atom var1) throws IOException {
      if (LOGGER.isLoggable(this.debugLevel)) {
         LOGGER.log(this.debugLevel, var1.toString());
      }

      this.mdhd(var1.nextChild("mdhd"));
   }

   void meta(MP4Atom var1) throws IOException {
      if (LOGGER.isLoggable(this.debugLevel)) {
         LOGGER.log(this.debugLevel, var1.toString());
      }

      var1.skip(4);

      while(var1.hasMoreChildren()) {
         MP4Atom var2 = var1.nextChild();
         if ("ilst".equals(var2.getType())) {
            this.ilst(var2);
            break;
         }
      }

   }

   void moov(MP4Atom var1) throws IOException {
      if (LOGGER.isLoggable(this.debugLevel)) {
         LOGGER.log(this.debugLevel, var1.toString());
      }

      while(var1.hasMoreChildren()) {
         MP4Atom var2 = var1.nextChild();
         String var3 = var2.getType();
         byte var4 = -1;
         int var5 = var3.hashCode();
         if (var5 != 3363941) {
            if (var5 != 3568424) {
               if (var5 == 3585340 && var3.equals("udta")) {
                  var4 = 2;
               }
            } else if (var3.equals("trak")) {
               var4 = 1;
            }
         } else if (var3.equals("mvhd")) {
            var4 = 0;
         }

         if (var4 != 0) {
            if (var4 != 1) {
               if (var4 == 2) {
                  this.udta(var2);
               }
            } else {
               this.trak(var2);
            }
         } else {
            this.mvhd(var2);
         }
      }

   }

   void mvhd(MP4Atom var1) throws IOException {
      if (LOGGER.isLoggable(this.debugLevel)) {
         LOGGER.log(this.debugLevel, var1.toString());
      }

      byte var2 = var1.readByte();
      var1.skip(3);
      byte var3;
      if (var2 == 1) {
         var3 = 16;
      } else {
         var3 = 8;
      }

      var1.skip(var3);
      int var11 = var1.readInt();
      long var4;
      if (var2 == 1) {
         var4 = var1.readLong();
      } else {
         var4 = (long)var1.readInt();
      }

      if (super.duration == 0L) {
         super.duration = var4 * 1000L / (long)var11;
      } else if (LOGGER.isLoggable(this.debugLevel)) {
         long var6 = super.duration;
         var4 = var4 * 1000L / (long)var11;
         if (Math.abs(var6 - var4) > 2L) {
            Logger var8 = LOGGER;
            Level var9 = this.debugLevel;
            StringBuilder var10 = new StringBuilder();
            var10.append("mvhd: duration ");
            var10.append(super.duration);
            var10.append(" -> ");
            var10.append(var4);
            var8.log(var9, var10.toString());
         }
      }

      this.speed = var1.readIntegerFixedPoint();
      this.volume = var1.readShortFixedPoint();
   }

   void trak(MP4Atom var1) throws IOException {
      if (LOGGER.isLoggable(this.debugLevel)) {
         LOGGER.log(this.debugLevel, var1.toString());
      }

      this.mdia(var1.nextChildUpTo("mdia"));
   }

   void udta(MP4Atom var1) throws IOException {
      if (LOGGER.isLoggable(this.debugLevel)) {
         LOGGER.log(this.debugLevel, var1.toString());
      }

      while(var1.hasMoreChildren()) {
         MP4Atom var2 = var1.nextChild();
         if ("meta".equals(var2.getType())) {
            this.meta(var2);
            break;
         }
      }

   }
}
