package org.telegram.messenger.audioinfo.mp3;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.telegram.messenger.audioinfo.AudioInfo;

public class MP3Info extends AudioInfo {
   static final Logger LOGGER = Logger.getLogger(MP3Info.class.getName());

   public MP3Info(InputStream var1, long var2) throws IOException, ID3v2Exception, MP3Exception {
      this(var1, var2, Level.FINEST);
   }

   public MP3Info(InputStream var1, final long var2, Level var4) throws IOException, ID3v2Exception, MP3Exception {
      super.brand = "MP3";
      super.version = "0";
      MP3Input var5 = new MP3Input(var1);
      if (ID3v2Info.isID3v2StartPosition(var5)) {
         ID3v2Info var6 = new ID3v2Info(var5, var4);
         super.album = var6.getAlbum();
         super.albumArtist = var6.getAlbumArtist();
         super.artist = var6.getArtist();
         super.comment = var6.getComment();
         super.cover = var6.getCover();
         super.smallCover = var6.getSmallCover();
         super.compilation = var6.isCompilation();
         super.composer = var6.getComposer();
         super.copyright = var6.getCopyright();
         super.disc = var6.getDisc();
         super.discs = var6.getDiscs();
         super.duration = var6.getDuration();
         super.genre = var6.getGenre();
         super.grouping = var6.getGrouping();
         super.lyrics = var6.getLyrics();
         super.title = var6.getTitle();
         super.track = var6.getTrack();
         super.tracks = var6.getTracks();
         super.year = var6.getYear();
      }

      long var7 = super.duration;
      if (var7 <= 0L || var7 >= 3600000L) {
         try {
            MP3Info.StopReadCondition var11 = new MP3Info.StopReadCondition() {
               final long stopPosition = var2 - 128L;

               public boolean stopRead(MP3Input var1) throws IOException {
                  boolean var2x;
                  if (var1.getPosition() == this.stopPosition && ID3v1Info.isID3v1StartPosition(var1)) {
                     var2x = true;
                  } else {
                     var2x = false;
                  }

                  return var2x;
               }
            };
            super.duration = this.calculateDuration(var5, var2, var11);
         } catch (MP3Exception var9) {
            if (LOGGER.isLoggable(var4)) {
               LOGGER.log(var4, "Could not determine MP3 duration", var9);
            }
         }
      }

      if (super.title == null || super.album == null || super.artist == null) {
         var7 = var5.getPosition();
         var2 -= 128L;
         if (var7 <= var2) {
            var5.skipFully(var2 - var5.getPosition());
            if (ID3v1Info.isID3v1StartPosition(var1)) {
               ID3v1Info var10 = new ID3v1Info(var1);
               if (super.album == null) {
                  super.album = var10.getAlbum();
               }

               if (super.artist == null) {
                  super.artist = var10.getArtist();
               }

               if (super.comment == null) {
                  super.comment = var10.getComment();
               }

               if (super.genre == null) {
                  super.genre = var10.getGenre();
               }

               if (super.title == null) {
                  super.title = var10.getTitle();
               }

               if (super.track == 0) {
                  super.track = var10.getTrack();
               }

               if (super.year == 0) {
                  super.year = var10.getYear();
               }
            }
         }
      }

   }

   long calculateDuration(MP3Input var1, long var2, MP3Info.StopReadCondition var4) throws IOException, MP3Exception {
      MP3Frame var5 = this.readFirstFrame(var1, var4);
      if (var5 == null) {
         throw new MP3Exception("No audio frame");
      } else {
         int var6 = var5.getNumberOfFrames();
         if (var6 > 0) {
            return var5.getHeader().getTotalDuration((long)(var6 * var5.getSize()));
         } else {
            long var7 = var1.getPosition();
            long var9 = (long)var5.getSize();
            long var11 = (long)var5.getSize();
            int var13 = var5.getHeader().getBitrate();
            long var14 = (long)var13;
            boolean var16 = false;
            int var17 = 10000 / var5.getHeader().getDuration();

            for(var6 = 1; var6 != var17 || var16 || var2 <= 0L; ++var6) {
               var5 = this.readNextFrame(var1, var4, var5);
               if (var5 == null) {
                  return var11 * 1000L * (long)var6 * 8L / var14;
               }

               int var18 = var5.getHeader().getBitrate();
               if (var18 != var13) {
                  var16 = true;
               }

               var14 += (long)var18;
               var11 += (long)var5.getSize();
            }

            return var5.getHeader().getTotalDuration(var2 - (var7 - var9));
         }
      }
   }

   MP3Frame readFirstFrame(MP3Input var1, MP3Info.StopReadCondition var2) throws IOException {
      int var3;
      if (var2.stopRead(var1)) {
         var3 = -1;
      } else {
         var3 = var1.read();
      }

      int var4 = 0;

      while(true) {
         int var5;
         label112: {
            if (var3 != -1) {
               if (var4 != 255 || (var3 & 224) != 224) {
                  break label112;
               }

               var1.mark(2);
               if (var2.stopRead(var1)) {
                  var5 = -1;
               } else {
                  var5 = var1.read();
               }

               if (var5 != -1) {
                  if (var2.stopRead(var1)) {
                     var4 = -1;
                  } else {
                     var4 = var1.read();
                  }

                  if (var4 != -1) {
                     MP3Frame.Header var6;
                     try {
                        var6 = new MP3Frame.Header(var3, var5, var4);
                     } catch (MP3Exception var11) {
                        var6 = null;
                     }

                     if (var6 != null) {
                        var1.reset();
                        var1.mark(var6.getFrameSize() + 2);
                        byte[] var7 = new byte[var6.getFrameSize()];
                        var7[0] = (byte)-1;
                        var7[1] = (byte)((byte)var3);

                        try {
                           var1.readFully(var7, 2, var7.length - 2);
                        } catch (EOFException var12) {
                           return null;
                        }

                        MP3Frame var8 = new MP3Frame(var6, var7);
                        if (!var8.isChecksumError()) {
                           label123: {
                              if (var2.stopRead(var1)) {
                                 var4 = -1;
                              } else {
                                 var4 = var1.read();
                              }

                              if (var2.stopRead(var1)) {
                                 var5 = -1;
                              } else {
                                 var5 = var1.read();
                              }

                              if (var4 != -1 && var5 != -1) {
                                 if (var4 != 255 || (var5 & 254) != (var3 & 254)) {
                                    break label123;
                                 }

                                 if (var2.stopRead(var1)) {
                                    var4 = -1;
                                 } else {
                                    var4 = var1.read();
                                 }

                                 int var9;
                                 if (var2.stopRead(var1)) {
                                    var9 = -1;
                                 } else {
                                    var9 = var1.read();
                                 }

                                 if (var4 != -1 && var9 != -1) {
                                    try {
                                       MP3Frame.Header var10 = new MP3Frame.Header(var5, var4, var9);
                                       if (!var10.isCompatible(var6)) {
                                          break label123;
                                       }

                                       var1.reset();
                                       var1.skipFully((long)(var7.length - 2));
                                    } catch (MP3Exception var13) {
                                       break label123;
                                    }
                                 }
                              }

                              return var8;
                           }
                        }
                     }

                     var1.reset();
                     break label112;
                  }
               }
            }

            return null;
         }

         if (var2.stopRead(var1)) {
            var5 = -1;
         } else {
            var5 = var1.read();
         }

         var4 = var3;
         var3 = var5;
      }
   }

   MP3Frame readNextFrame(MP3Input var1, MP3Info.StopReadCondition var2, MP3Frame var3) throws IOException {
      MP3Frame.Header var11 = var3.getHeader();
      var1.mark(4);
      int var4;
      if (var2.stopRead(var1)) {
         var4 = -1;
      } else {
         var4 = var1.read();
      }

      int var5;
      if (var2.stopRead(var1)) {
         var5 = -1;
      } else {
         var5 = var1.read();
      }

      if (var4 != -1 && var5 != -1) {
         if (var4 == 255 && (var5 & 224) == 224) {
            int var6;
            if (var2.stopRead(var1)) {
               var6 = -1;
            } else {
               var6 = var1.read();
            }

            int var7;
            if (var2.stopRead(var1)) {
               var7 = -1;
            } else {
               var7 = var1.read();
            }

            if (var6 == -1 || var7 == -1) {
               return null;
            }

            MP3Frame.Header var10;
            try {
               var10 = new MP3Frame.Header(var5, var6, var7);
            } catch (MP3Exception var8) {
               var10 = null;
            }

            if (var10 != null && var10.isCompatible(var11)) {
               byte[] var12 = new byte[var10.getFrameSize()];
               var12[0] = (byte)((byte)var4);
               var12[1] = (byte)((byte)var5);
               var12[2] = (byte)((byte)var6);
               var12[3] = (byte)((byte)var7);

               try {
                  var1.readFully(var12, 4, var12.length - 4);
               } catch (EOFException var9) {
                  return null;
               }

               return new MP3Frame(var10, var12);
            }
         }

         var1.reset();
      }

      return null;
   }

   interface StopReadCondition {
      boolean stopRead(MP3Input var1) throws IOException;
   }
}
