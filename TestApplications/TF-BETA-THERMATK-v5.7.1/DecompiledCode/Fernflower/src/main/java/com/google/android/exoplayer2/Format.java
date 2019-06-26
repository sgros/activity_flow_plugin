package com.google.android.exoplayer2;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.ColorInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Format implements Parcelable {
   public static final Creator CREATOR = new Creator() {
      public Format createFromParcel(Parcel var1) {
         return new Format(var1);
      }

      public Format[] newArray(int var1) {
         return new Format[var1];
      }
   };
   public final int accessibilityChannel;
   public final int bitrate;
   public final int channelCount;
   public final String codecs;
   public final ColorInfo colorInfo;
   public final String containerMimeType;
   public final DrmInitData drmInitData;
   public final int encoderDelay;
   public final int encoderPadding;
   public final float frameRate;
   private int hashCode;
   public final int height;
   public final String id;
   public final List initializationData;
   public final String label;
   public final String language;
   public final int maxInputSize;
   public final Metadata metadata;
   public final int pcmEncoding;
   public final float pixelWidthHeightRatio;
   public final byte[] projectionData;
   public final int rotationDegrees;
   public final String sampleMimeType;
   public final int sampleRate;
   public final int selectionFlags;
   public final int stereoMode;
   public final long subsampleOffsetUs;
   public final int width;

   Format(Parcel var1) {
      this.id = var1.readString();
      this.label = var1.readString();
      this.containerMimeType = var1.readString();
      this.sampleMimeType = var1.readString();
      this.codecs = var1.readString();
      this.bitrate = var1.readInt();
      this.maxInputSize = var1.readInt();
      this.width = var1.readInt();
      this.height = var1.readInt();
      this.frameRate = var1.readFloat();
      this.rotationDegrees = var1.readInt();
      this.pixelWidthHeightRatio = var1.readFloat();
      byte[] var2;
      if (Util.readBoolean(var1)) {
         var2 = var1.createByteArray();
      } else {
         var2 = null;
      }

      this.projectionData = var2;
      this.stereoMode = var1.readInt();
      this.colorInfo = (ColorInfo)var1.readParcelable(ColorInfo.class.getClassLoader());
      this.channelCount = var1.readInt();
      this.sampleRate = var1.readInt();
      this.pcmEncoding = var1.readInt();
      this.encoderDelay = var1.readInt();
      this.encoderPadding = var1.readInt();
      this.selectionFlags = var1.readInt();
      this.language = var1.readString();
      this.accessibilityChannel = var1.readInt();
      this.subsampleOffsetUs = var1.readLong();
      int var3 = var1.readInt();
      this.initializationData = new ArrayList(var3);

      for(int var4 = 0; var4 < var3; ++var4) {
         this.initializationData.add(var1.createByteArray());
      }

      this.drmInitData = (DrmInitData)var1.readParcelable(DrmInitData.class.getClassLoader());
      this.metadata = (Metadata)var1.readParcelable(Metadata.class.getClassLoader());
   }

   Format(String var1, String var2, String var3, String var4, String var5, int var6, int var7, int var8, int var9, float var10, int var11, float var12, byte[] var13, int var14, ColorInfo var15, int var16, int var17, int var18, int var19, int var20, int var21, String var22, int var23, long var24, List var26, DrmInitData var27, Metadata var28) {
      this.id = var1;
      this.label = var2;
      this.containerMimeType = var3;
      this.sampleMimeType = var4;
      this.codecs = var5;
      this.bitrate = var6;
      this.maxInputSize = var7;
      this.width = var8;
      this.height = var9;
      this.frameRate = var10;
      var6 = var11;
      if (var11 == -1) {
         var6 = 0;
      }

      this.rotationDegrees = var6;
      this.pixelWidthHeightRatio = 1.0F;
      this.projectionData = var13;
      this.stereoMode = var14;
      this.colorInfo = var15;
      this.channelCount = var16;
      this.sampleRate = var17;
      this.pcmEncoding = var18;
      var6 = var19;
      if (var19 == -1) {
         var6 = 0;
      }

      this.encoderDelay = var6;
      var6 = var20;
      if (var20 == -1) {
         var6 = 0;
      }

      this.encoderPadding = var6;
      this.selectionFlags = var21;
      this.language = var22;
      this.accessibilityChannel = var23;
      this.subsampleOffsetUs = var24;
      if (var26 == null) {
         var26 = Collections.emptyList();
      }

      this.initializationData = var26;
      this.drmInitData = var27;
      this.metadata = var28;
   }

   public static Format createAudioContainerFormat(String var0, String var1, String var2, String var3, String var4, int var5, int var6, int var7, List var8, int var9, String var10) {
      return new Format(var0, var1, var2, var3, var4, var5, -1, -1, -1, -1.0F, -1, -1.0F, (byte[])null, -1, (ColorInfo)null, var6, var7, -1, -1, -1, var9, var10, -1, Long.MAX_VALUE, var8, (DrmInitData)null, (Metadata)null);
   }

   public static Format createAudioSampleFormat(String var0, String var1, String var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, List var10, DrmInitData var11, int var12, String var13, Metadata var14) {
      return new Format(var0, (String)null, (String)null, var1, var2, var3, var4, -1, -1, -1.0F, -1, -1.0F, (byte[])null, -1, (ColorInfo)null, var5, var6, var7, var8, var9, var12, var13, -1, Long.MAX_VALUE, var10, var11, var14);
   }

   public static Format createAudioSampleFormat(String var0, String var1, String var2, int var3, int var4, int var5, int var6, int var7, List var8, DrmInitData var9, int var10, String var11) {
      return createAudioSampleFormat(var0, var1, var2, var3, var4, var5, var6, var7, -1, -1, var8, var9, var10, var11, (Metadata)null);
   }

   public static Format createAudioSampleFormat(String var0, String var1, String var2, int var3, int var4, int var5, int var6, List var7, DrmInitData var8, int var9, String var10) {
      return createAudioSampleFormat(var0, var1, var2, var3, var4, var5, var6, -1, var7, var8, var9, var10);
   }

   public static Format createContainerFormat(String var0, String var1, String var2, String var3, String var4, int var5, int var6, String var7) {
      return new Format(var0, var1, var2, var3, var4, var5, -1, -1, -1, -1.0F, -1, -1.0F, (byte[])null, -1, (ColorInfo)null, -1, -1, -1, -1, -1, var6, var7, -1, Long.MAX_VALUE, (List)null, (DrmInitData)null, (Metadata)null);
   }

   public static Format createImageSampleFormat(String var0, String var1, String var2, int var3, int var4, List var5, String var6, DrmInitData var7) {
      return new Format(var0, (String)null, (String)null, var1, var2, var3, -1, -1, -1, -1.0F, -1, -1.0F, (byte[])null, -1, (ColorInfo)null, -1, -1, -1, -1, -1, var4, var6, -1, Long.MAX_VALUE, var5, var7, (Metadata)null);
   }

   public static Format createSampleFormat(String var0, String var1, long var2) {
      return new Format(var0, (String)null, (String)null, var1, (String)null, -1, -1, -1, -1, -1.0F, -1, -1.0F, (byte[])null, -1, (ColorInfo)null, -1, -1, -1, -1, -1, 0, (String)null, -1, var2, (List)null, (DrmInitData)null, (Metadata)null);
   }

   public static Format createSampleFormat(String var0, String var1, String var2, int var3, DrmInitData var4) {
      return new Format(var0, (String)null, (String)null, var1, var2, var3, -1, -1, -1, -1.0F, -1, -1.0F, (byte[])null, -1, (ColorInfo)null, -1, -1, -1, -1, -1, 0, (String)null, -1, Long.MAX_VALUE, (List)null, var4, (Metadata)null);
   }

   public static Format createTextContainerFormat(String var0, String var1, String var2, String var3, String var4, int var5, int var6, String var7) {
      return createTextContainerFormat(var0, var1, var2, var3, var4, var5, var6, var7, -1);
   }

   public static Format createTextContainerFormat(String var0, String var1, String var2, String var3, String var4, int var5, int var6, String var7, int var8) {
      return new Format(var0, var1, var2, var3, var4, var5, -1, -1, -1, -1.0F, -1, -1.0F, (byte[])null, -1, (ColorInfo)null, -1, -1, -1, -1, -1, var6, var7, var8, Long.MAX_VALUE, (List)null, (DrmInitData)null, (Metadata)null);
   }

   public static Format createTextSampleFormat(String var0, String var1, int var2, String var3) {
      return createTextSampleFormat(var0, var1, var2, var3, (DrmInitData)null);
   }

   public static Format createTextSampleFormat(String var0, String var1, int var2, String var3, DrmInitData var4) {
      return createTextSampleFormat(var0, var1, (String)null, -1, var2, var3, -1, var4, Long.MAX_VALUE, Collections.emptyList());
   }

   public static Format createTextSampleFormat(String var0, String var1, String var2, int var3, int var4, String var5, int var6, DrmInitData var7, long var8, List var10) {
      return new Format(var0, (String)null, (String)null, var1, var2, var3, -1, -1, -1, -1.0F, -1, -1.0F, (byte[])null, -1, (ColorInfo)null, -1, -1, -1, -1, -1, var4, var5, var6, var8, var10, var7, (Metadata)null);
   }

   public static Format createTextSampleFormat(String var0, String var1, String var2, int var3, int var4, String var5, DrmInitData var6, long var7) {
      return createTextSampleFormat(var0, var1, var2, var3, var4, var5, -1, var6, var7, Collections.emptyList());
   }

   public static Format createVideoContainerFormat(String var0, String var1, String var2, String var3, String var4, int var5, int var6, int var7, float var8, List var9, int var10) {
      return new Format(var0, var1, var2, var3, var4, var5, -1, var6, var7, var8, -1, -1.0F, (byte[])null, -1, (ColorInfo)null, -1, -1, -1, -1, -1, var10, (String)null, -1, Long.MAX_VALUE, var9, (DrmInitData)null, (Metadata)null);
   }

   public static Format createVideoSampleFormat(String var0, String var1, String var2, int var3, int var4, int var5, int var6, float var7, List var8, int var9, float var10, DrmInitData var11) {
      return createVideoSampleFormat(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, (byte[])null, -1, (ColorInfo)null, var11);
   }

   public static Format createVideoSampleFormat(String var0, String var1, String var2, int var3, int var4, int var5, int var6, float var7, List var8, int var9, float var10, byte[] var11, int var12, ColorInfo var13, DrmInitData var14) {
      return new Format(var0, (String)null, (String)null, var1, var2, var3, var4, var5, var6, var7, var9, var10, var11, var12, var13, -1, -1, -1, -1, -1, 0, (String)null, -1, Long.MAX_VALUE, var8, var14, (Metadata)null);
   }

   public static String toLogString(Format var0) {
      if (var0 == null) {
         return "null";
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("id=");
         var1.append(var0.id);
         var1.append(", mimeType=");
         var1.append(var0.sampleMimeType);
         if (var0.bitrate != -1) {
            var1.append(", bitrate=");
            var1.append(var0.bitrate);
         }

         if (var0.codecs != null) {
            var1.append(", codecs=");
            var1.append(var0.codecs);
         }

         if (var0.width != -1 && var0.height != -1) {
            var1.append(", res=");
            var1.append(var0.width);
            var1.append("x");
            var1.append(var0.height);
         }

         if (var0.frameRate != -1.0F) {
            var1.append(", fps=");
            var1.append(var0.frameRate);
         }

         if (var0.channelCount != -1) {
            var1.append(", channels=");
            var1.append(var0.channelCount);
         }

         if (var0.sampleRate != -1) {
            var1.append(", sample_rate=");
            var1.append(var0.sampleRate);
         }

         if (var0.language != null) {
            var1.append(", language=");
            var1.append(var0.language);
         }

         if (var0.label != null) {
            var1.append(", label=");
            var1.append(var0.label);
         }

         return var1.toString();
      }
   }

   public Format copyWithBitrate(int var1) {
      return new Format(this.id, this.label, this.containerMimeType, this.sampleMimeType, this.codecs, var1, this.maxInputSize, this.width, this.height, this.frameRate, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, this.encoderDelay, this.encoderPadding, this.selectionFlags, this.language, this.accessibilityChannel, this.subsampleOffsetUs, this.initializationData, this.drmInitData, this.metadata);
   }

   public Format copyWithContainerInfo(String var1, String var2, String var3, String var4, int var5, int var6, int var7, int var8, String var9) {
      return new Format(var1, var2, this.containerMimeType, var3, var4, var5, this.maxInputSize, var6, var7, this.frameRate, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, this.encoderDelay, this.encoderPadding, var8, var9, this.accessibilityChannel, this.subsampleOffsetUs, this.initializationData, this.drmInitData, this.metadata);
   }

   public Format copyWithDrmInitData(DrmInitData var1) {
      return new Format(this.id, this.label, this.containerMimeType, this.sampleMimeType, this.codecs, this.bitrate, this.maxInputSize, this.width, this.height, this.frameRate, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, this.encoderDelay, this.encoderPadding, this.selectionFlags, this.language, this.accessibilityChannel, this.subsampleOffsetUs, this.initializationData, var1, this.metadata);
   }

   public Format copyWithFrameRate(float var1) {
      return new Format(this.id, this.label, this.containerMimeType, this.sampleMimeType, this.codecs, this.bitrate, this.maxInputSize, this.width, this.height, var1, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, this.encoderDelay, this.encoderPadding, this.selectionFlags, this.language, this.accessibilityChannel, this.subsampleOffsetUs, this.initializationData, this.drmInitData, this.metadata);
   }

   public Format copyWithGaplessInfo(int var1, int var2) {
      return new Format(this.id, this.label, this.containerMimeType, this.sampleMimeType, this.codecs, this.bitrate, this.maxInputSize, this.width, this.height, this.frameRate, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, var1, var2, this.selectionFlags, this.language, this.accessibilityChannel, this.subsampleOffsetUs, this.initializationData, this.drmInitData, this.metadata);
   }

   public Format copyWithManifestFormatInfo(Format var1) {
      if (this == var1) {
         return this;
      } else {
         int var2 = MimeTypes.getTrackType(this.sampleMimeType);
         String var3 = var1.id;
         String var4 = var1.label;
         if (var4 == null) {
            var4 = this.label;
         }

         String var5 = this.language;
         String var6;
         if (var2 == 3 || var2 == 1) {
            var6 = var1.language;
            if (var6 != null) {
               var5 = var6;
            }
         }

         int var7 = this.bitrate;
         int var8 = var7;
         if (var7 == -1) {
            var8 = var1.bitrate;
         }

         label32: {
            String var9 = this.codecs;
            if (var9 == null) {
               var6 = Util.getCodecsOfType(var1.codecs, var2);
               if (Util.splitCodecs(var6).length == 1) {
                  break label32;
               }
            }

            var6 = var9;
         }

         float var10 = this.frameRate;
         if (var10 == -1.0F && var2 == 2) {
            var10 = var1.frameRate;
         }

         var2 = this.selectionFlags;
         var7 = var1.selectionFlags;
         DrmInitData var11 = DrmInitData.createSessionCreationData(var1.drmInitData, this.drmInitData);
         return new Format(var3, var4, this.containerMimeType, this.sampleMimeType, var6, var8, this.maxInputSize, this.width, this.height, var10, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, this.encoderDelay, this.encoderPadding, var2 | var7, var5, this.accessibilityChannel, this.subsampleOffsetUs, this.initializationData, var11, this.metadata);
      }
   }

   public Format copyWithMaxInputSize(int var1) {
      return new Format(this.id, this.label, this.containerMimeType, this.sampleMimeType, this.codecs, this.bitrate, var1, this.width, this.height, this.frameRate, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, this.encoderDelay, this.encoderPadding, this.selectionFlags, this.language, this.accessibilityChannel, this.subsampleOffsetUs, this.initializationData, this.drmInitData, this.metadata);
   }

   public Format copyWithMetadata(Metadata var1) {
      return new Format(this.id, this.label, this.containerMimeType, this.sampleMimeType, this.codecs, this.bitrate, this.maxInputSize, this.width, this.height, this.frameRate, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, this.encoderDelay, this.encoderPadding, this.selectionFlags, this.language, this.accessibilityChannel, this.subsampleOffsetUs, this.initializationData, this.drmInitData, var1);
   }

   public Format copyWithSubsampleOffsetUs(long var1) {
      return new Format(this.id, this.label, this.containerMimeType, this.sampleMimeType, this.codecs, this.bitrate, this.maxInputSize, this.width, this.height, this.frameRate, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, this.encoderDelay, this.encoderPadding, this.selectionFlags, this.language, this.accessibilityChannel, var1, this.initializationData, this.drmInitData, this.metadata);
   }

   public int describeContents() {
      return 0;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && Format.class == var1.getClass()) {
         Format var5 = (Format)var1;
         int var3 = this.hashCode;
         if (var3 != 0) {
            int var4 = var5.hashCode;
            if (var4 != 0 && var3 != var4) {
               return false;
            }
         }

         if (this.bitrate != var5.bitrate || this.maxInputSize != var5.maxInputSize || this.width != var5.width || this.height != var5.height || Float.compare(this.frameRate, var5.frameRate) != 0 || this.rotationDegrees != var5.rotationDegrees || Float.compare(this.pixelWidthHeightRatio, var5.pixelWidthHeightRatio) != 0 || this.stereoMode != var5.stereoMode || this.channelCount != var5.channelCount || this.sampleRate != var5.sampleRate || this.pcmEncoding != var5.pcmEncoding || this.encoderDelay != var5.encoderDelay || this.encoderPadding != var5.encoderPadding || this.subsampleOffsetUs != var5.subsampleOffsetUs || this.selectionFlags != var5.selectionFlags || !Util.areEqual(this.id, var5.id) || !Util.areEqual(this.label, var5.label) || !Util.areEqual(this.language, var5.language) || this.accessibilityChannel != var5.accessibilityChannel || !Util.areEqual(this.containerMimeType, var5.containerMimeType) || !Util.areEqual(this.sampleMimeType, var5.sampleMimeType) || !Util.areEqual(this.codecs, var5.codecs) || !Util.areEqual(this.drmInitData, var5.drmInitData) || !Util.areEqual(this.metadata, var5.metadata) || !Util.areEqual(this.colorInfo, var5.colorInfo) || !Arrays.equals(this.projectionData, var5.projectionData) || !this.initializationDataEquals(var5)) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public int getPixelCount() {
      int var1 = this.width;
      byte var2 = -1;
      int var3 = var2;
      if (var1 != -1) {
         var3 = this.height;
         if (var3 == -1) {
            var3 = var2;
         } else {
            var3 = var1 * var3;
         }
      }

      return var3;
   }

   public int hashCode() {
      if (this.hashCode == 0) {
         String var1 = this.id;
         int var2 = 0;
         int var3;
         if (var1 == null) {
            var3 = 0;
         } else {
            var3 = var1.hashCode();
         }

         var1 = this.containerMimeType;
         int var4;
         if (var1 == null) {
            var4 = 0;
         } else {
            var4 = var1.hashCode();
         }

         var1 = this.sampleMimeType;
         int var5;
         if (var1 == null) {
            var5 = 0;
         } else {
            var5 = var1.hashCode();
         }

         var1 = this.codecs;
         int var6;
         if (var1 == null) {
            var6 = 0;
         } else {
            var6 = var1.hashCode();
         }

         int var7 = this.bitrate;
         int var8 = this.width;
         int var9 = this.height;
         int var10 = this.channelCount;
         int var11 = this.sampleRate;
         var1 = this.language;
         int var12;
         if (var1 == null) {
            var12 = 0;
         } else {
            var12 = var1.hashCode();
         }

         int var13 = this.accessibilityChannel;
         DrmInitData var16 = this.drmInitData;
         int var14;
         if (var16 == null) {
            var14 = 0;
         } else {
            var14 = var16.hashCode();
         }

         Metadata var17 = this.metadata;
         int var15;
         if (var17 == null) {
            var15 = 0;
         } else {
            var15 = var17.hashCode();
         }

         var1 = this.label;
         if (var1 != null) {
            var2 = var1.hashCode();
         }

         this.hashCode = (((((((((((((((((((((((527 + var3) * 31 + var4) * 31 + var5) * 31 + var6) * 31 + var7) * 31 + var8) * 31 + var9) * 31 + var10) * 31 + var11) * 31 + var12) * 31 + var13) * 31 + var14) * 31 + var15) * 31 + var2) * 31 + this.maxInputSize) * 31 + (int)this.subsampleOffsetUs) * 31 + Float.floatToIntBits(this.frameRate)) * 31 + Float.floatToIntBits(this.pixelWidthHeightRatio)) * 31 + this.rotationDegrees) * 31 + this.stereoMode) * 31 + this.pcmEncoding) * 31 + this.encoderDelay) * 31 + this.encoderPadding) * 31 + this.selectionFlags;
      }

      return this.hashCode;
   }

   public boolean initializationDataEquals(Format var1) {
      if (this.initializationData.size() != var1.initializationData.size()) {
         return false;
      } else {
         for(int var2 = 0; var2 < this.initializationData.size(); ++var2) {
            if (!Arrays.equals((byte[])this.initializationData.get(var2), (byte[])var1.initializationData.get(var2))) {
               return false;
            }
         }

         return true;
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Format(");
      var1.append(this.id);
      var1.append(", ");
      var1.append(this.label);
      var1.append(", ");
      var1.append(this.containerMimeType);
      var1.append(", ");
      var1.append(this.sampleMimeType);
      var1.append(", ");
      var1.append(this.codecs);
      var1.append(", ");
      var1.append(this.bitrate);
      var1.append(", ");
      var1.append(this.language);
      var1.append(", [");
      var1.append(this.width);
      var1.append(", ");
      var1.append(this.height);
      var1.append(", ");
      var1.append(this.frameRate);
      var1.append("], [");
      var1.append(this.channelCount);
      var1.append(", ");
      var1.append(this.sampleRate);
      var1.append("])");
      return var1.toString();
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeString(this.id);
      var1.writeString(this.label);
      var1.writeString(this.containerMimeType);
      var1.writeString(this.sampleMimeType);
      var1.writeString(this.codecs);
      var1.writeInt(this.bitrate);
      var1.writeInt(this.maxInputSize);
      var1.writeInt(this.width);
      var1.writeInt(this.height);
      var1.writeFloat(this.frameRate);
      var1.writeInt(this.rotationDegrees);
      var1.writeFloat(this.pixelWidthHeightRatio);
      boolean var3;
      if (this.projectionData != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      Util.writeBoolean(var1, var3);
      byte[] var4 = this.projectionData;
      if (var4 != null) {
         var1.writeByteArray(var4);
      }

      var1.writeInt(this.stereoMode);
      var1.writeParcelable(this.colorInfo, var2);
      var1.writeInt(this.channelCount);
      var1.writeInt(this.sampleRate);
      var1.writeInt(this.pcmEncoding);
      var1.writeInt(this.encoderDelay);
      var1.writeInt(this.encoderPadding);
      var1.writeInt(this.selectionFlags);
      var1.writeString(this.language);
      var1.writeInt(this.accessibilityChannel);
      var1.writeLong(this.subsampleOffsetUs);
      int var5 = this.initializationData.size();
      var1.writeInt(var5);

      for(var2 = 0; var2 < var5; ++var2) {
         var1.writeByteArray((byte[])this.initializationData.get(var2));
      }

      var1.writeParcelable(this.drmInitData, 0);
      var1.writeParcelable(this.metadata, 0);
   }
}
