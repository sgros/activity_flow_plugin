package org.telegram.messenger.audioinfo.mp3;

public class MP3Frame {
   private final byte[] bytes;
   private final MP3Frame.Header header;

   MP3Frame(MP3Frame.Header var1, byte[] var2) {
      this.header = var1;
      this.bytes = var2;
   }

   public MP3Frame.Header getHeader() {
      return this.header;
   }

   public int getNumberOfFrames() {
      int var1;
      byte[] var2;
      int var3;
      byte var4;
      if (this.isXingFrame()) {
         var1 = this.header.getXingOffset();
         var2 = this.bytes;
         if ((var2[var1 + 7] & 1) != 0) {
            var3 = (var2[var1 + 8] & 255) << 24 | (var2[var1 + 9] & 255) << 16 | (var2[var1 + 10] & 255) << 8;
            var4 = var2[var1 + 11];
            return var4 & 255 | var3;
         }
      } else if (this.isVBRIFrame()) {
         var1 = this.header.getVBRIOffset();
         var2 = this.bytes;
         var3 = (var2[var1 + 14] & 255) << 24 | (var2[var1 + 15] & 255) << 16 | (var2[var1 + 16] & 255) << 8;
         var4 = var2[var1 + 17];
         return var4 & 255 | var3;
      }

      return -1;
   }

   public int getSize() {
      return this.bytes.length;
   }

   boolean isChecksumError() {
      int var1 = this.header.getProtection();
      boolean var2 = false;
      boolean var3 = var2;
      if (var1 == 0) {
         var3 = var2;
         if (this.header.getLayer() == 1) {
            MP3Frame.CRC16 var4 = new MP3Frame.CRC16();
            var4.update(this.bytes[2]);
            var4.update(this.bytes[3]);
            int var5 = this.header.getSideInfoSize();

            for(var1 = 0; var1 < var5; ++var1) {
               var4.update(this.bytes[var1 + 6]);
            }

            byte[] var6 = this.bytes;
            byte var7 = var6[4];
            var3 = var2;
            if ((var6[5] & 255 | (var7 & 255) << 8) != var4.getValue()) {
               var3 = true;
            }
         }
      }

      return var3;
   }

   boolean isVBRIFrame() {
      int var1 = this.header.getVBRIOffset();
      byte[] var2 = this.bytes;
      int var3 = var2.length;
      boolean var4 = false;
      if (var3 < var1 + 26) {
         return false;
      } else {
         boolean var5 = var4;
         if (var2[var1] == 86) {
            var5 = var4;
            if (var2[var1 + 1] == 66) {
               var5 = var4;
               if (var2[var1 + 2] == 82) {
                  var5 = var4;
                  if (var2[var1 + 3] == 73) {
                     var5 = true;
                  }
               }
            }
         }

         return var5;
      }
   }

   boolean isXingFrame() {
      int var1 = this.header.getXingOffset();
      byte[] var2 = this.bytes;
      if (var2.length < var1 + 12) {
         return false;
      } else {
         if (var1 >= 0 && var2.length >= var1 + 8) {
            if (var2[var1] == 88 && var2[var1 + 1] == 105 && var2[var1 + 2] == 110 && var2[var1 + 3] == 103) {
               return true;
            }

            var2 = this.bytes;
            if (var2[var1] == 73 && var2[var1 + 1] == 110 && var2[var1 + 2] == 102 && var2[var1 + 3] == 111) {
               return true;
            }
         }

         return false;
      }
   }

   static final class CRC16 {
      private short crc = (short)-1;

      public short getValue() {
         return this.crc;
      }

      public void reset() {
         this.crc = (short)-1;
      }

      public void update(byte var1) {
         this.update(var1, 8);
      }

      public void update(int var1, int var2) {
         var2 = 1 << var2 - 1;

         int var6;
         do {
            short var3 = this.crc;
            boolean var4 = false;
            boolean var5;
            if ((var3 & '耀') == 0) {
               var5 = true;
            } else {
               var5 = false;
            }

            if ((var1 & var2) == 0) {
               var4 = true;
            }

            if (var5 ^ var4) {
               this.crc = (short)((short)(this.crc << 1));
               this.crc = (short)((short)(this.crc ^ '者'));
            } else {
               this.crc = (short)((short)(this.crc << 1));
            }

            var6 = var2 >>> 1;
            var2 = var6;
         } while(var6 != 0);

      }
   }

   public static class Header {
      private static final int[][] BITRATES;
      private static final int[][] BITRATES_COLUMN;
      private static final int[][] FREQUENCIES = new int[][]{{11025, -1, 22050, 44100}, {12000, -1, 24000, 48000}, {8000, -1, 16000, 32000}, {-1, -1, -1, -1}};
      private static final int MPEG_BITRATE_FREE = 0;
      private static final int MPEG_BITRATE_RESERVED = 15;
      public static final int MPEG_CHANNEL_MODE_MONO = 3;
      private static final int MPEG_FRQUENCY_RESERVED = 3;
      public static final int MPEG_LAYER_1 = 3;
      public static final int MPEG_LAYER_2 = 2;
      public static final int MPEG_LAYER_3 = 1;
      private static final int MPEG_LAYER_RESERVED = 0;
      public static final int MPEG_PROTECTION_CRC = 0;
      public static final int MPEG_VERSION_1 = 3;
      public static final int MPEG_VERSION_2 = 2;
      public static final int MPEG_VERSION_2_5 = 0;
      private static final int MPEG_VERSION_RESERVED = 1;
      private static final int[][] SIDE_INFO_SIZES;
      private static final int[][] SIZE_COEFFICIENTS;
      private static final int[] SLOT_SIZES;
      private final int bitrate;
      private final int channelMode;
      private final int frequency;
      private final int layer;
      private final int padding;
      private final int protection;
      private final int version;

      static {
         int[] var0 = new int[]{64000, 48000, 40000, 48000, 16000};
         int[] var1 = new int[]{96000, 56000, 48000, 56000, 24000};
         int[] var2 = new int[]{192000, 96000, 80000, 96000, 48000};
         int[] var3 = new int[]{256000, 128000, 112000, 128000, 64000};
         int[] var4 = new int[]{320000, 192000, 160000, 160000, 96000};
         int[] var5 = new int[]{448000, 384000, 320000, 256000, 160000};
         BITRATES = new int[][]{{0, 0, 0, 0, 0}, {32000, 32000, 32000, 32000, 8000}, var0, var1, {128000, 64000, 56000, 64000, 32000}, {160000, 80000, 64000, 80000, 40000}, var2, {224000, 112000, 96000, 112000, 56000}, var3, {288000, 160000, 128000, 144000, 80000}, var4, {352000, 224000, 192000, 176000, 112000}, {384000, 256000, 224000, 192000, 128000}, {416000, 320000, 256000, 224000, 144000}, var5, {-1, -1, -1, -1, -1}};
         BITRATES_COLUMN = new int[][]{{-1, 4, 4, 3}, {-1, -1, -1, -1}, {-1, 4, 4, 3}, {-1, 2, 1, 0}};
         var0 = new int[]{-1, -1, -1, -1};
         var1 = new int[]{-1, 72, 144, 12};
         SIZE_COEFFICIENTS = new int[][]{{-1, 72, 144, 12}, var0, var1, {-1, 144, 144, 12}};
         SLOT_SIZES = new int[]{-1, 1, 1, 4};
         SIDE_INFO_SIZES = new int[][]{{17, -1, 17, 32}, {17, -1, 17, 32}, {17, -1, 17, 32}, {9, -1, 9, 17}};
      }

      public Header(int var1, int var2, int var3) throws MP3Exception {
         this.version = var1 >> 3 & 3;
         if (this.version != 1) {
            this.layer = var1 >> 1 & 3;
            if (this.layer != 0) {
               this.bitrate = var2 >> 4 & 15;
               int var4 = this.bitrate;
               if (var4 != 15) {
                  if (var4 != 0) {
                     this.frequency = var2 >> 2 & 3;
                     if (this.frequency != 3) {
                        byte var7 = 6;
                        this.channelMode = var3 >> 6 & 3;
                        this.padding = var2 >> 1 & 1;
                        this.protection = var1 & 1;
                        byte var6;
                        if (this.protection == 0) {
                           var6 = var7;
                        } else {
                           var6 = 4;
                        }

                        var2 = var6;
                        if (this.layer == 1) {
                           var2 = var6 + this.getSideInfoSize();
                        }

                        if (this.getFrameSize() < var2) {
                           StringBuilder var5 = new StringBuilder();
                           var5.append("Frame size must be at least ");
                           var5.append(var2);
                           throw new MP3Exception(var5.toString());
                        }
                     } else {
                        throw new MP3Exception("Reserved frequency");
                     }
                  } else {
                     throw new MP3Exception("Free bitrate");
                  }
               } else {
                  throw new MP3Exception("Reserved bitrate");
               }
            } else {
               throw new MP3Exception("Reserved layer");
            }
         } else {
            throw new MP3Exception("Reserved version");
         }
      }

      public int getBitrate() {
         return BITRATES[this.bitrate][BITRATES_COLUMN[this.version][this.layer]];
      }

      public int getChannelMode() {
         return this.channelMode;
      }

      public int getDuration() {
         return (int)this.getTotalDuration((long)this.getFrameSize());
      }

      public int getFrameSize() {
         return (SIZE_COEFFICIENTS[this.version][this.layer] * this.getBitrate() / this.getFrequency() + this.padding) * SLOT_SIZES[this.layer];
      }

      public int getFrequency() {
         return FREQUENCIES[this.frequency][this.version];
      }

      public int getLayer() {
         return this.layer;
      }

      public int getProtection() {
         return this.protection;
      }

      public int getSampleCount() {
         return this.layer == 3 ? 384 : 1152;
      }

      public int getSideInfoSize() {
         return SIDE_INFO_SIZES[this.channelMode][this.version];
      }

      public long getTotalDuration(long var1) {
         long var3 = (long)this.getSampleCount() * var1 * 1000L / (long)(this.getFrameSize() * this.getFrequency());
         var1 = var3;
         if (this.getVersion() != 3) {
            var1 = var3;
            if (this.getChannelMode() == 3) {
               var1 = var3 / 2L;
            }
         }

         return var1;
      }

      public int getVBRIOffset() {
         return 36;
      }

      public int getVersion() {
         return this.version;
      }

      public int getXingOffset() {
         return this.getSideInfoSize() + 4;
      }

      public boolean isCompatible(MP3Frame.Header var1) {
         boolean var2;
         if (this.layer == var1.layer && this.version == var1.version && this.frequency == var1.frequency && this.channelMode == var1.channelMode) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }
   }
}
