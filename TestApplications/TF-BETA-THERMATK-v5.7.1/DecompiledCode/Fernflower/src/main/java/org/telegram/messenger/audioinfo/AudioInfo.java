package org.telegram.messenger.audioinfo;

import android.graphics.Bitmap;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import org.telegram.messenger.audioinfo.m4a.M4AInfo;
import org.telegram.messenger.audioinfo.mp3.MP3Info;

public abstract class AudioInfo {
   protected String album;
   protected String albumArtist;
   protected String artist;
   protected String brand;
   protected String comment;
   protected boolean compilation;
   protected String composer;
   protected String copyright;
   protected Bitmap cover;
   protected short disc;
   protected short discs;
   protected long duration;
   protected String genre;
   protected String grouping;
   protected String lyrics;
   protected Bitmap smallCover;
   protected String title;
   protected short track;
   protected short tracks;
   protected String version;
   protected short year;

   public static AudioInfo getAudioInfo(File var0) {
      byte[] var1;
      boolean var10001;
      BufferedInputStream var3;
      try {
         var1 = new byte[12];
         RandomAccessFile var2 = new RandomAccessFile(var0, "r");
         var2.readFully(var1, 0, 8);
         var2.close();
         FileInputStream var8 = new FileInputStream(var0);
         var3 = new BufferedInputStream(var8);
      } catch (Exception var6) {
         var10001 = false;
         return null;
      }

      if (var1[4] == 102 && var1[5] == 116 && var1[6] == 121 && var1[7] == 112) {
         try {
            return new M4AInfo(var3);
         } catch (Exception var4) {
            var10001 = false;
         }
      } else {
         try {
            if (var0.getAbsolutePath().endsWith("mp3")) {
               MP3Info var7 = new MP3Info(var3, var0.length());
               return var7;
            }
         } catch (Exception var5) {
            var10001 = false;
         }
      }

      return null;
   }

   public String getAlbum() {
      return this.album;
   }

   public String getAlbumArtist() {
      return this.albumArtist;
   }

   public String getArtist() {
      return this.artist;
   }

   public String getBrand() {
      return this.brand;
   }

   public String getComment() {
      return this.comment;
   }

   public String getComposer() {
      return this.composer;
   }

   public String getCopyright() {
      return this.copyright;
   }

   public Bitmap getCover() {
      return this.cover;
   }

   public short getDisc() {
      return this.disc;
   }

   public short getDiscs() {
      return this.discs;
   }

   public long getDuration() {
      return this.duration;
   }

   public String getGenre() {
      return this.genre;
   }

   public String getGrouping() {
      return this.grouping;
   }

   public String getLyrics() {
      return this.lyrics;
   }

   public Bitmap getSmallCover() {
      return this.smallCover;
   }

   public String getTitle() {
      return this.title;
   }

   public short getTrack() {
      return this.track;
   }

   public short getTracks() {
      return this.tracks;
   }

   public String getVersion() {
      return this.version;
   }

   public short getYear() {
      return this.year;
   }

   public boolean isCompilation() {
      return this.compilation;
   }
}
