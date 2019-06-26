package org.telegram.messenger.audioinfo.mp3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.telegram.messenger.audioinfo.AudioInfo;

public class ID3v2Info extends AudioInfo {
   static final Logger LOGGER = Logger.getLogger(ID3v2Info.class.getName());
   private byte coverPictureType;
   private final Level debugLevel;

   public ID3v2Info(InputStream var1) throws IOException, ID3v2Exception {
      this(var1, Level.FINEST);
   }

   public ID3v2Info(InputStream var1, Level var2) throws IOException, ID3v2Exception {
      this.debugLevel = var2;
      if (isID3v2StartPosition(var1)) {
         ID3v2TagHeader var3 = new ID3v2TagHeader(var1);
         super.brand = "ID3";
         super.version = String.format("2.%d.%d", var3.getVersion(), var3.getRevision());
         ID3v2TagBody var4 = var3.tagBody(var1);

         label328: {
            ID3v2Exception var10000;
            while(true) {
               ID3v2FrameHeader var5;
               boolean var10001;
               try {
                  if (var4.getRemainingLength() <= 10L) {
                     break label328;
                  }

                  var5 = new ID3v2FrameHeader(var4);
                  if (var5.isPadding()) {
                     break label328;
                  }
               } catch (ID3v2Exception var42) {
                  var10000 = var42;
                  var10001 = false;
                  break;
               }

               try {
                  if ((long)var5.getBodySize() > var4.getRemainingLength()) {
                     if (LOGGER.isLoggable(var2)) {
                        LOGGER.log(var2, "ID3 frame claims to extend frames area");
                     }
                     break label328;
                  }
               } catch (ID3v2Exception var41) {
                  var10000 = var41;
                  var10001 = false;
                  break;
               }

               ID3v2FrameBody var6;
               label334: {
                  try {
                     if (var5.isValid() && !var5.isEncryption()) {
                        var6 = var4.frameBody(var5);
                        break label334;
                     }
                  } catch (ID3v2Exception var40) {
                     var10000 = var40;
                     var10001 = false;
                     break;
                  }

                  try {
                     var4.getData().skipFully((long)var5.getBodySize());
                     continue;
                  } catch (ID3v2Exception var34) {
                     var10000 = var34;
                     var10001 = false;
                     break;
                  }
               }

               long var7;
               ID3v2DataInput var44;
               label310: {
                  label335: {
                     Throwable var48;
                     label336: {
                        ID3v2Exception var9;
                        try {
                           try {
                              this.parseFrame(var6);
                              break label335;
                           } catch (ID3v2Exception var38) {
                              var9 = var38;
                           }
                        } catch (Throwable var39) {
                           var48 = var39;
                           var10001 = false;
                           break label336;
                        }

                        try {
                           if (LOGGER.isLoggable(var2)) {
                              LOGGER.log(var2, String.format("ID3 exception occured in frame %s: %s", var5.getFrameId(), var9.getMessage()));
                           }
                        } catch (Throwable var37) {
                           var48 = var37;
                           var10001 = false;
                           break label336;
                        }

                        try {
                           var44 = var6.getData();
                           var7 = var6.getRemainingLength();
                           break label310;
                        } catch (ID3v2Exception var35) {
                           var10000 = var35;
                           var10001 = false;
                           break;
                        }
                     }

                     Throwable var43 = var48;

                     try {
                        var6.getData().skipFully(var6.getRemainingLength());
                        throw var43;
                     } catch (ID3v2Exception var33) {
                        var10000 = var33;
                        var10001 = false;
                        break;
                     }
                  }

                  try {
                     var44 = var6.getData();
                     var7 = var6.getRemainingLength();
                  } catch (ID3v2Exception var36) {
                     var10000 = var36;
                     var10001 = false;
                     break;
                  }
               }

               try {
                  var44.skipFully(var7);
               } catch (ID3v2Exception var32) {
                  var10000 = var32;
                  var10001 = false;
                  break;
               }
            }

            ID3v2Exception var45 = var10000;
            if (LOGGER.isLoggable(var2)) {
               Logger var46 = LOGGER;
               StringBuilder var47 = new StringBuilder();
               var47.append("ID3 exception occured: ");
               var47.append(var45.getMessage());
               var46.log(var2, var47.toString());
            }
         }

         var4.getData().skipFully(var4.getRemainingLength());
         if (var3.getFooterSize() > 0) {
            var1.skip((long)var3.getFooterSize());
         }
      }

   }

   public static boolean isID3v2StartPosition(InputStream var0) throws IOException {
      var0.mark(3);
      boolean var5 = false;

      boolean var2;
      label54: {
         label53: {
            int var1;
            try {
               var5 = true;
               if (var0.read() != 73) {
                  var5 = false;
                  break label53;
               }

               if (var0.read() != 68) {
                  var5 = false;
                  break label53;
               }

               var1 = var0.read();
               var5 = false;
            } finally {
               if (var5) {
                  var0.reset();
               }
            }

            if (var1 == true) {
               var2 = true;
               break label54;
            }
         }

         var2 = false;
      }

      var0.reset();
      return var2;
   }

   ID3v2Info.AttachedPicture parseAttachedPictureFrame(ID3v2FrameBody var1) throws IOException, ID3v2Exception {
      ID3v2Encoding var2 = var1.readEncoding();
      String var3;
      if (var1.getTagHeader().getVersion() == 2) {
         var3 = var1.readFixedLengthString(3, ID3v2Encoding.ISO_8859_1).toUpperCase();
         byte var4 = -1;
         int var5 = var3.hashCode();
         if (var5 != 73665) {
            if (var5 == 79369 && var3.equals("PNG")) {
               var4 = 0;
            }
         } else if (var3.equals("JPG")) {
            var4 = 1;
         }

         if (var4 != 0) {
            if (var4 != 1) {
               var3 = "image/unknown";
            } else {
               var3 = "image/jpeg";
            }
         } else {
            var3 = "image/png";
         }
      } else {
         var3 = var1.readZeroTerminatedString(20, ID3v2Encoding.ISO_8859_1);
      }

      return new ID3v2Info.AttachedPicture(var1.getData().readByte(), var1.readZeroTerminatedString(200, var2), var3, var1.getData().readFully((int)var1.getRemainingLength()));
   }

   ID3v2Info.CommentOrUnsynchronizedLyrics parseCommentOrUnsynchronizedLyricsFrame(ID3v2FrameBody var1) throws IOException, ID3v2Exception {
      ID3v2Encoding var2 = var1.readEncoding();
      return new ID3v2Info.CommentOrUnsynchronizedLyrics(var1.readFixedLengthString(3, ID3v2Encoding.ISO_8859_1), var1.readZeroTerminatedString(200, var2), var1.readFixedLengthString((int)var1.getRemainingLength(), var2));
   }

   void parseFrame(ID3v2FrameBody var1) throws IOException, ID3v2Exception {
      Logger var2;
      Level var3;
      StringBuilder var4;
      if (LOGGER.isLoggable(this.debugLevel)) {
         var2 = LOGGER;
         var3 = this.debugLevel;
         var4 = new StringBuilder();
         var4.append("Parsing frame: ");
         var4.append(var1.getFrameHeader().getFrameId());
         var2.log(var3, var4.toString());
      }

      String var36 = var1.getFrameHeader().getFrameId();
      byte var5 = -1;
      switch(var36.hashCode()) {
      case 66913:
         if (var36.equals("COM")) {
            var5 = 2;
         }
         break;
      case 79210:
         if (var36.equals("PIC")) {
            var5 = 0;
         }
         break;
      case 82815:
         if (var36.equals("TAL")) {
            var5 = 4;
         }
         break;
      case 82878:
         if (var36.equals("TCM")) {
            var5 = 8;
         }
         break;
      case 82880:
         if (var36.equals("TCO")) {
            var5 = 10;
         }
         break;
      case 82881:
         if (var36.equals("TCP")) {
            var5 = 6;
         }
         break;
      case 82883:
         if (var36.equals("TCR")) {
            var5 = 12;
         }
         break;
      case 83149:
         if (var36.equals("TLE")) {
            var5 = 15;
         }
         break;
      case 83253:
         if (var36.equals("TP1")) {
            var5 = 17;
         }
         break;
      case 83254:
         if (var36.equals("TP2")) {
            var5 = 19;
         }
         break;
      case 83269:
         if (var36.equals("TPA")) {
            var5 = 21;
         }
         break;
      case 83341:
         if (var36.equals("TRK")) {
            var5 = 23;
         }
         break;
      case 83377:
         if (var36.equals("TT1")) {
            var5 = 25;
         }
         break;
      case 83378:
         if (var36.equals("TT2")) {
            var5 = 27;
         }
         break;
      case 83552:
         if (var36.equals("TYE")) {
            var5 = 29;
         }
         break;
      case 84125:
         if (var36.equals("ULT")) {
            var5 = 31;
         }
         break;
      case 2015625:
         if (var36.equals("APIC")) {
            var5 = 1;
         }
         break;
      case 2074380:
         if (var36.equals("COMM")) {
            var5 = 3;
         }
         break;
      case 2567331:
         if (var36.equals("TALB")) {
            var5 = 5;
         }
         break;
      case 2569298:
         if (var36.equals("TCMP")) {
            var5 = 7;
         }
         break;
      case 2569357:
         if (var36.equals("TCOM")) {
            var5 = 9;
         }
         break;
      case 2569358:
         if (var36.equals("TCON")) {
            var5 = 11;
         }
         break;
      case 2569360:
         if (var36.equals("TCOP")) {
            var5 = 13;
         }
         break;
      case 2570401:
         if (var36.equals("TDRC")) {
            var5 = 14;
         }
         break;
      case 2575250:
         if (var36.equals("TIT1")) {
            var5 = 26;
         }
         break;
      case 2575251:
         if (var36.equals("TIT2")) {
            var5 = 28;
         }
         break;
      case 2577697:
         if (var36.equals("TLEN")) {
            var5 = 16;
         }
         break;
      case 2581512:
         if (var36.equals("TPE1")) {
            var5 = 18;
         }
         break;
      case 2581513:
         if (var36.equals("TPE2")) {
            var5 = 20;
         }
         break;
      case 2581856:
         if (var36.equals("TPOS")) {
            var5 = 22;
         }
         break;
      case 2583398:
         if (var36.equals("TRCK")) {
            var5 = 24;
         }
         break;
      case 2590194:
         if (var36.equals("TYER")) {
            var5 = 30;
         }
         break;
      case 2614438:
         if (var36.equals("USLT")) {
            var5 = 32;
         }
      }

      boolean var10001;
      String var30;
      StringBuilder var31;
      Level var33;
      Logger var34;
      StringBuilder var35;
      Level var37;
      Logger var41;
      int var46;
      switch(var5) {
      case 0:
      case 1:
         if (super.cover == null || this.coverPictureType != 3) {
            ID3v2Info.AttachedPicture var40 = this.parseAttachedPictureFrame(var1);
            if (super.cover != null) {
               var5 = var40.type;
               if (var5 != 3 && var5 != 0) {
                  return;
               }
            }

            label275: {
               Throwable var10000;
               label375: {
                  Options var42;
                  byte[] var44;
                  label376: {
                     try {
                        var44 = var40.imageData;
                        var42 = new Options();
                        var42.inJustDecodeBounds = true;
                        var42.inSampleSize = 1;
                        BitmapFactory.decodeByteArray(var44, 0, var44.length, var42);
                        if (var42.outWidth <= 800 && var42.outHeight <= 800) {
                           break label376;
                        }
                     } catch (Throwable var14) {
                        var10000 = var14;
                        var10001 = false;
                        break label375;
                     }

                     try {
                        var46 = Math.max(var42.outWidth, var42.outHeight);
                     } catch (Throwable var13) {
                        var10000 = var13;
                        var10001 = false;
                        break label375;
                     }

                     while(var46 > 800) {
                        try {
                           var42.inSampleSize *= 2;
                           var46 /= 2;
                        } catch (Throwable var12) {
                           var10000 = var12;
                           var10001 = false;
                           break label375;
                        }
                     }
                  }

                  float var7;
                  try {
                     var42.inJustDecodeBounds = false;
                     super.cover = BitmapFactory.decodeByteArray(var44, 0, var44.length, var42);
                     if (super.cover == null) {
                        break label275;
                     }

                     var7 = (float)Math.max(super.cover.getWidth(), super.cover.getHeight()) / 120.0F;
                  } catch (Throwable var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label375;
                  }

                  if (var7 > 0.0F) {
                     try {
                        super.smallCover = Bitmap.createScaledBitmap(super.cover, (int)((float)super.cover.getWidth() / var7), (int)((float)super.cover.getHeight() / var7), true);
                     } catch (Throwable var10) {
                        var10000 = var10;
                        var10001 = false;
                        break label375;
                     }
                  } else {
                     try {
                        super.smallCover = super.cover;
                     } catch (Throwable var9) {
                        var10000 = var9;
                        var10001 = false;
                        break label375;
                     }
                  }

                  try {
                     if (super.smallCover == null) {
                        super.smallCover = super.cover;
                     }
                     break label275;
                  } catch (Throwable var8) {
                     var10000 = var8;
                     var10001 = false;
                  }
               }

               Throwable var45 = var10000;
               var45.printStackTrace();
            }

            this.coverPictureType = (byte)var40.type;
         }
         break;
      case 2:
      case 3:
         ID3v2Info.CommentOrUnsynchronizedLyrics var38 = this.parseCommentOrUnsynchronizedLyricsFrame(var1);
         if (super.comment != null) {
            var36 = var38.description;
            if (var36 != null && !"".equals(var36)) {
               break;
            }
         }

         super.comment = var38.text;
         break;
      case 4:
      case 5:
         super.album = this.parseTextFrame(var1);
         break;
      case 6:
      case 7:
         super.compilation = "1".equals(this.parseTextFrame(var1));
         break;
      case 8:
      case 9:
         super.composer = this.parseTextFrame(var1);
         break;
      case 10:
      case 11:
         String var39 = this.parseTextFrame(var1);
         if (var39.length() > 0) {
            super.genre = var39;
            ID3v1Genre var32 = null;

            label314: {
               int var6;
               label313: {
                  try {
                     if (var39.charAt(0) == '(') {
                        var6 = var39.indexOf(41);
                        break label313;
                     }
                  } catch (NumberFormatException var20) {
                     var10001 = false;
                     return;
                  }

                  try {
                     var32 = ID3v1Genre.getGenre(Integer.parseInt(var39));
                     break label314;
                  } catch (NumberFormatException var16) {
                     var10001 = false;
                     return;
                  }
               }

               if (var6 > 1) {
                  ID3v1Genre var43;
                  try {
                     var43 = ID3v1Genre.getGenre(Integer.parseInt(var39.substring(1, var6)));
                  } catch (NumberFormatException var19) {
                     var10001 = false;
                     return;
                  }

                  var32 = var43;
                  if (var43 == null) {
                     try {
                        var46 = var39.length();
                     } catch (NumberFormatException var18) {
                        var10001 = false;
                        return;
                     }

                     ++var6;
                     var32 = var43;
                     if (var46 > var6) {
                        try {
                           super.genre = var39.substring(var6);
                        } catch (NumberFormatException var17) {
                           var10001 = false;
                           return;
                        }

                        var32 = var43;
                     }
                  }
               }
            }

            if (var32 != null) {
               try {
                  super.genre = var32.getDescription();
               } catch (NumberFormatException var15) {
                  var10001 = false;
               }
            }
         }
         break;
      case 12:
      case 13:
         super.copyright = this.parseTextFrame(var1);
         break;
      case 14:
         var30 = this.parseTextFrame(var1);
         if (var30.length() >= 4) {
            try {
               super.year = Short.valueOf(var30.substring(0, 4));
            } catch (NumberFormatException var21) {
               if (LOGGER.isLoggable(this.debugLevel)) {
                  var41 = LOGGER;
                  var33 = this.debugLevel;
                  var35 = new StringBuilder();
                  var35.append("Could not parse year from: ");
                  var35.append(var30);
                  var41.log(var33, var35.toString());
               }
            }
         }
         break;
      case 15:
      case 16:
         var30 = this.parseTextFrame(var1);

         try {
            super.duration = Long.valueOf(var30);
         } catch (NumberFormatException var22) {
            if (LOGGER.isLoggable(this.debugLevel)) {
               var34 = LOGGER;
               var37 = this.debugLevel;
               var31 = new StringBuilder();
               var31.append("Could not parse track duration: ");
               var31.append(var30);
               var34.log(var37, var31.toString());
            }
         }
         break;
      case 17:
      case 18:
         super.artist = this.parseTextFrame(var1);
         break;
      case 19:
      case 20:
         super.albumArtist = this.parseTextFrame(var1);
         break;
      case 21:
      case 22:
         var30 = this.parseTextFrame(var1);
         if (var30.length() > 0) {
            var46 = var30.indexOf(47);
            if (var46 < 0) {
               try {
                  super.disc = Short.valueOf(var30);
               } catch (NumberFormatException var25) {
                  if (LOGGER.isLoggable(this.debugLevel)) {
                     var2 = LOGGER;
                     var3 = this.debugLevel;
                     var4 = new StringBuilder();
                     var4.append("Could not parse disc number: ");
                     var4.append(var30);
                     var2.log(var3, var4.toString());
                  }
               }
            } else {
               try {
                  super.disc = Short.valueOf(var30.substring(0, var46));
               } catch (NumberFormatException var24) {
                  if (LOGGER.isLoggable(this.debugLevel)) {
                     var34 = LOGGER;
                     var33 = this.debugLevel;
                     var4 = new StringBuilder();
                     var4.append("Could not parse disc number: ");
                     var4.append(var30);
                     var34.log(var33, var4.toString());
                  }
               }

               try {
                  super.discs = Short.valueOf(var30.substring(var46 + 1));
               } catch (NumberFormatException var23) {
                  if (LOGGER.isLoggable(this.debugLevel)) {
                     var41 = LOGGER;
                     var33 = this.debugLevel;
                     var35 = new StringBuilder();
                     var35.append("Could not parse number of discs: ");
                     var35.append(var30);
                     var41.log(var33, var35.toString());
                  }
               }
            }
         }
         break;
      case 23:
      case 24:
         var30 = this.parseTextFrame(var1);
         if (var30.length() > 0) {
            var46 = var30.indexOf(47);
            if (var46 < 0) {
               try {
                  super.track = Short.valueOf(var30);
               } catch (NumberFormatException var28) {
                  if (LOGGER.isLoggable(this.debugLevel)) {
                     var34 = LOGGER;
                     var37 = this.debugLevel;
                     var31 = new StringBuilder();
                     var31.append("Could not parse track number: ");
                     var31.append(var30);
                     var34.log(var37, var31.toString());
                  }
               }
            } else {
               try {
                  super.track = Short.valueOf(var30.substring(0, var46));
               } catch (NumberFormatException var27) {
                  if (LOGGER.isLoggable(this.debugLevel)) {
                     var41 = LOGGER;
                     var33 = this.debugLevel;
                     var35 = new StringBuilder();
                     var35.append("Could not parse track number: ");
                     var35.append(var30);
                     var41.log(var33, var35.toString());
                  }
               }

               try {
                  super.tracks = Short.valueOf(var30.substring(var46 + 1));
               } catch (NumberFormatException var26) {
                  if (LOGGER.isLoggable(this.debugLevel)) {
                     var34 = LOGGER;
                     var37 = this.debugLevel;
                     var31 = new StringBuilder();
                     var31.append("Could not parse number of tracks: ");
                     var31.append(var30);
                     var34.log(var37, var31.toString());
                  }
               }
            }
         }
         break;
      case 25:
      case 26:
         super.grouping = this.parseTextFrame(var1);
         break;
      case 27:
      case 28:
         super.title = this.parseTextFrame(var1);
         break;
      case 29:
      case 30:
         var30 = this.parseTextFrame(var1);
         if (var30.length() > 0) {
            try {
               super.year = Short.valueOf(var30);
            } catch (NumberFormatException var29) {
               if (LOGGER.isLoggable(this.debugLevel)) {
                  var34 = LOGGER;
                  var37 = this.debugLevel;
                  var31 = new StringBuilder();
                  var31.append("Could not parse year: ");
                  var31.append(var30);
                  var34.log(var37, var31.toString());
               }
            }
         }
         break;
      case 31:
      case 32:
         if (super.lyrics == null) {
            super.lyrics = this.parseCommentOrUnsynchronizedLyricsFrame(var1).text;
         }
      }

   }

   String parseTextFrame(ID3v2FrameBody var1) throws IOException, ID3v2Exception {
      ID3v2Encoding var2 = var1.readEncoding();
      return var1.readFixedLengthString((int)var1.getRemainingLength(), var2);
   }

   static class AttachedPicture {
      static final byte TYPE_COVER_FRONT = 3;
      static final byte TYPE_OTHER = 0;
      final String description;
      final byte[] imageData;
      final String imageType;
      final byte type;

      public AttachedPicture(byte var1, String var2, String var3, byte[] var4) {
         this.type = (byte)var1;
         this.description = var2;
         this.imageType = var3;
         this.imageData = var4;
      }
   }

   static class CommentOrUnsynchronizedLyrics {
      final String description;
      final String language;
      final String text;

      public CommentOrUnsynchronizedLyrics(String var1, String var2, String var3) {
         this.language = var1;
         this.description = var2;
         this.text = var3;
      }
   }
}
