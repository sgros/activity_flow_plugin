// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2;

import java.util.Arrays;
import com.google.android.exoplayer2.util.MimeTypes;
import java.util.Collections;
import java.util.ArrayList;
import com.google.android.exoplayer2.util.Util;
import android.os.Parcel;
import com.google.android.exoplayer2.metadata.Metadata;
import java.util.List;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.video.ColorInfo;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public final class Format implements Parcelable
{
    public static final Parcelable$Creator<Format> CREATOR;
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
    public final List<byte[]> initializationData;
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
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<Format>() {
            public Format createFromParcel(final Parcel parcel) {
                return new Format(parcel);
            }
            
            public Format[] newArray(final int n) {
                return new Format[n];
            }
        };
    }
    
    Format(final Parcel parcel) {
        this.id = parcel.readString();
        this.label = parcel.readString();
        this.containerMimeType = parcel.readString();
        this.sampleMimeType = parcel.readString();
        this.codecs = parcel.readString();
        this.bitrate = parcel.readInt();
        this.maxInputSize = parcel.readInt();
        this.width = parcel.readInt();
        this.height = parcel.readInt();
        this.frameRate = parcel.readFloat();
        this.rotationDegrees = parcel.readInt();
        this.pixelWidthHeightRatio = parcel.readFloat();
        byte[] byteArray;
        if (Util.readBoolean(parcel)) {
            byteArray = parcel.createByteArray();
        }
        else {
            byteArray = null;
        }
        this.projectionData = byteArray;
        this.stereoMode = parcel.readInt();
        this.colorInfo = (ColorInfo)parcel.readParcelable(ColorInfo.class.getClassLoader());
        this.channelCount = parcel.readInt();
        this.sampleRate = parcel.readInt();
        this.pcmEncoding = parcel.readInt();
        this.encoderDelay = parcel.readInt();
        this.encoderPadding = parcel.readInt();
        this.selectionFlags = parcel.readInt();
        this.language = parcel.readString();
        this.accessibilityChannel = parcel.readInt();
        this.subsampleOffsetUs = parcel.readLong();
        final int int1 = parcel.readInt();
        this.initializationData = new ArrayList<byte[]>(int1);
        for (int i = 0; i < int1; ++i) {
            this.initializationData.add(parcel.createByteArray());
        }
        this.drmInitData = (DrmInitData)parcel.readParcelable(DrmInitData.class.getClassLoader());
        this.metadata = (Metadata)parcel.readParcelable(Metadata.class.getClassLoader());
    }
    
    Format(final String id, final String label, final String containerMimeType, final String sampleMimeType, final String codecs, int n, final int maxInputSize, final int width, final int height, final float frameRate, final int n2, final float n3, final byte[] projectionData, final int stereoMode, final ColorInfo colorInfo, final int channelCount, final int sampleRate, final int pcmEncoding, final int n4, final int n5, final int selectionFlags, final String language, final int accessibilityChannel, final long subsampleOffsetUs, List<byte[]> emptyList, final DrmInitData drmInitData, final Metadata metadata) {
        this.id = id;
        this.label = label;
        this.containerMimeType = containerMimeType;
        this.sampleMimeType = sampleMimeType;
        this.codecs = codecs;
        this.bitrate = n;
        this.maxInputSize = maxInputSize;
        this.width = width;
        this.height = height;
        this.frameRate = frameRate;
        n = n2;
        if (n2 == -1) {
            n = 0;
        }
        this.rotationDegrees = n;
        this.pixelWidthHeightRatio = 1.0f;
        this.projectionData = projectionData;
        this.stereoMode = stereoMode;
        this.colorInfo = colorInfo;
        this.channelCount = channelCount;
        this.sampleRate = sampleRate;
        this.pcmEncoding = pcmEncoding;
        if ((n = n4) == -1) {
            n = 0;
        }
        this.encoderDelay = n;
        if ((n = n5) == -1) {
            n = 0;
        }
        this.encoderPadding = n;
        this.selectionFlags = selectionFlags;
        this.language = language;
        this.accessibilityChannel = accessibilityChannel;
        this.subsampleOffsetUs = subsampleOffsetUs;
        if (emptyList == null) {
            emptyList = Collections.emptyList();
        }
        this.initializationData = emptyList;
        this.drmInitData = drmInitData;
        this.metadata = metadata;
    }
    
    public static Format createAudioContainerFormat(final String s, final String s2, final String s3, final String s4, final String s5, final int n, final int n2, final int n3, final List<byte[]> list, final int n4, final String s6) {
        return new Format(s, s2, s3, s4, s5, n, -1, -1, -1, -1.0f, -1, -1.0f, null, -1, null, n2, n3, -1, -1, -1, n4, s6, -1, Long.MAX_VALUE, list, null, null);
    }
    
    public static Format createAudioSampleFormat(final String s, final String s2, final String s3, final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final List<byte[]> list, final DrmInitData drmInitData, final int n8, final String s4, final Metadata metadata) {
        return new Format(s, null, null, s2, s3, n, n2, -1, -1, -1.0f, -1, -1.0f, null, -1, null, n3, n4, n5, n6, n7, n8, s4, -1, Long.MAX_VALUE, list, drmInitData, metadata);
    }
    
    public static Format createAudioSampleFormat(final String s, final String s2, final String s3, final int n, final int n2, final int n3, final int n4, final int n5, final List<byte[]> list, final DrmInitData drmInitData, final int n6, final String s4) {
        return createAudioSampleFormat(s, s2, s3, n, n2, n3, n4, n5, -1, -1, list, drmInitData, n6, s4, null);
    }
    
    public static Format createAudioSampleFormat(final String s, final String s2, final String s3, final int n, final int n2, final int n3, final int n4, final List<byte[]> list, final DrmInitData drmInitData, final int n5, final String s4) {
        return createAudioSampleFormat(s, s2, s3, n, n2, n3, n4, -1, list, drmInitData, n5, s4);
    }
    
    public static Format createContainerFormat(final String s, final String s2, final String s3, final String s4, final String s5, final int n, final int n2, final String s6) {
        return new Format(s, s2, s3, s4, s5, n, -1, -1, -1, -1.0f, -1, -1.0f, null, -1, null, -1, -1, -1, -1, -1, n2, s6, -1, Long.MAX_VALUE, null, null, null);
    }
    
    public static Format createImageSampleFormat(final String s, final String s2, final String s3, final int n, final int n2, final List<byte[]> list, final String s4, final DrmInitData drmInitData) {
        return new Format(s, null, null, s2, s3, n, -1, -1, -1, -1.0f, -1, -1.0f, null, -1, null, -1, -1, -1, -1, -1, n2, s4, -1, Long.MAX_VALUE, list, drmInitData, null);
    }
    
    public static Format createSampleFormat(final String s, final String s2, final long n) {
        return new Format(s, null, null, s2, null, -1, -1, -1, -1, -1.0f, -1, -1.0f, null, -1, null, -1, -1, -1, -1, -1, 0, null, -1, n, null, null, null);
    }
    
    public static Format createSampleFormat(final String s, final String s2, final String s3, final int n, final DrmInitData drmInitData) {
        return new Format(s, null, null, s2, s3, n, -1, -1, -1, -1.0f, -1, -1.0f, null, -1, null, -1, -1, -1, -1, -1, 0, null, -1, Long.MAX_VALUE, null, drmInitData, null);
    }
    
    public static Format createTextContainerFormat(final String s, final String s2, final String s3, final String s4, final String s5, final int n, final int n2, final String s6) {
        return createTextContainerFormat(s, s2, s3, s4, s5, n, n2, s6, -1);
    }
    
    public static Format createTextContainerFormat(final String s, final String s2, final String s3, final String s4, final String s5, final int n, final int n2, final String s6, final int n3) {
        return new Format(s, s2, s3, s4, s5, n, -1, -1, -1, -1.0f, -1, -1.0f, null, -1, null, -1, -1, -1, -1, -1, n2, s6, n3, Long.MAX_VALUE, null, null, null);
    }
    
    public static Format createTextSampleFormat(final String s, final String s2, final int n, final String s3) {
        return createTextSampleFormat(s, s2, n, s3, null);
    }
    
    public static Format createTextSampleFormat(final String s, final String s2, final int n, final String s3, final DrmInitData drmInitData) {
        return createTextSampleFormat(s, s2, null, -1, n, s3, -1, drmInitData, Long.MAX_VALUE, Collections.emptyList());
    }
    
    public static Format createTextSampleFormat(final String s, final String s2, final String s3, final int n, final int n2, final String s4, final int n3, final DrmInitData drmInitData, final long n4, final List<byte[]> list) {
        return new Format(s, null, null, s2, s3, n, -1, -1, -1, -1.0f, -1, -1.0f, null, -1, null, -1, -1, -1, -1, -1, n2, s4, n3, n4, list, drmInitData, null);
    }
    
    public static Format createTextSampleFormat(final String s, final String s2, final String s3, final int n, final int n2, final String s4, final DrmInitData drmInitData, final long n3) {
        return createTextSampleFormat(s, s2, s3, n, n2, s4, -1, drmInitData, n3, Collections.emptyList());
    }
    
    public static Format createVideoContainerFormat(final String s, final String s2, final String s3, final String s4, final String s5, final int n, final int n2, final int n3, final float n4, final List<byte[]> list, final int n5) {
        return new Format(s, s2, s3, s4, s5, n, -1, n2, n3, n4, -1, -1.0f, null, -1, null, -1, -1, -1, -1, -1, n5, null, -1, Long.MAX_VALUE, list, null, null);
    }
    
    public static Format createVideoSampleFormat(final String s, final String s2, final String s3, final int n, final int n2, final int n3, final int n4, final float n5, final List<byte[]> list, final int n6, final float n7, final DrmInitData drmInitData) {
        return createVideoSampleFormat(s, s2, s3, n, n2, n3, n4, n5, list, n6, n7, null, -1, null, drmInitData);
    }
    
    public static Format createVideoSampleFormat(final String s, final String s2, final String s3, final int n, final int n2, final int n3, final int n4, final float n5, final List<byte[]> list, final int n6, final float n7, final byte[] array, final int n8, final ColorInfo colorInfo, final DrmInitData drmInitData) {
        return new Format(s, null, null, s2, s3, n, n2, n3, n4, n5, n6, n7, array, n8, colorInfo, -1, -1, -1, -1, -1, 0, null, -1, Long.MAX_VALUE, list, drmInitData, null);
    }
    
    public static String toLogString(final Format format) {
        if (format == null) {
            return "null";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("id=");
        sb.append(format.id);
        sb.append(", mimeType=");
        sb.append(format.sampleMimeType);
        if (format.bitrate != -1) {
            sb.append(", bitrate=");
            sb.append(format.bitrate);
        }
        if (format.codecs != null) {
            sb.append(", codecs=");
            sb.append(format.codecs);
        }
        if (format.width != -1 && format.height != -1) {
            sb.append(", res=");
            sb.append(format.width);
            sb.append("x");
            sb.append(format.height);
        }
        if (format.frameRate != -1.0f) {
            sb.append(", fps=");
            sb.append(format.frameRate);
        }
        if (format.channelCount != -1) {
            sb.append(", channels=");
            sb.append(format.channelCount);
        }
        if (format.sampleRate != -1) {
            sb.append(", sample_rate=");
            sb.append(format.sampleRate);
        }
        if (format.language != null) {
            sb.append(", language=");
            sb.append(format.language);
        }
        if (format.label != null) {
            sb.append(", label=");
            sb.append(format.label);
        }
        return sb.toString();
    }
    
    public Format copyWithBitrate(final int n) {
        return new Format(this.id, this.label, this.containerMimeType, this.sampleMimeType, this.codecs, n, this.maxInputSize, this.width, this.height, this.frameRate, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, this.encoderDelay, this.encoderPadding, this.selectionFlags, this.language, this.accessibilityChannel, this.subsampleOffsetUs, this.initializationData, this.drmInitData, this.metadata);
    }
    
    public Format copyWithContainerInfo(final String s, final String s2, final String s3, final String s4, final int n, final int n2, final int n3, final int n4, final String s5) {
        return new Format(s, s2, this.containerMimeType, s3, s4, n, this.maxInputSize, n2, n3, this.frameRate, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, this.encoderDelay, this.encoderPadding, n4, s5, this.accessibilityChannel, this.subsampleOffsetUs, this.initializationData, this.drmInitData, this.metadata);
    }
    
    public Format copyWithDrmInitData(final DrmInitData drmInitData) {
        return new Format(this.id, this.label, this.containerMimeType, this.sampleMimeType, this.codecs, this.bitrate, this.maxInputSize, this.width, this.height, this.frameRate, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, this.encoderDelay, this.encoderPadding, this.selectionFlags, this.language, this.accessibilityChannel, this.subsampleOffsetUs, this.initializationData, drmInitData, this.metadata);
    }
    
    public Format copyWithFrameRate(final float n) {
        return new Format(this.id, this.label, this.containerMimeType, this.sampleMimeType, this.codecs, this.bitrate, this.maxInputSize, this.width, this.height, n, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, this.encoderDelay, this.encoderPadding, this.selectionFlags, this.language, this.accessibilityChannel, this.subsampleOffsetUs, this.initializationData, this.drmInitData, this.metadata);
    }
    
    public Format copyWithGaplessInfo(final int n, final int n2) {
        return new Format(this.id, this.label, this.containerMimeType, this.sampleMimeType, this.codecs, this.bitrate, this.maxInputSize, this.width, this.height, this.frameRate, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, n, n2, this.selectionFlags, this.language, this.accessibilityChannel, this.subsampleOffsetUs, this.initializationData, this.drmInitData, this.metadata);
    }
    
    public Format copyWithManifestFormatInfo(final Format format) {
        if (this == format) {
            return this;
        }
        final int trackType = MimeTypes.getTrackType(this.sampleMimeType);
        final String id = format.id;
        String s = format.label;
        if (s == null) {
            s = this.label;
        }
        String language = this.language;
        if (trackType == 3 || trackType == 1) {
            final String language2 = format.language;
            if (language2 != null) {
                language = language2;
            }
        }
        int n;
        if ((n = this.bitrate) == -1) {
            n = format.bitrate;
        }
        final String codecs = this.codecs;
        String codecsOfType = null;
        Label_0134: {
            if (codecs == null) {
                codecsOfType = Util.getCodecsOfType(format.codecs, trackType);
                if (Util.splitCodecs(codecsOfType).length == 1) {
                    break Label_0134;
                }
            }
            codecsOfType = codecs;
        }
        float n2 = this.frameRate;
        if (n2 == -1.0f && trackType == 2) {
            n2 = format.frameRate;
        }
        return new Format(id, s, this.containerMimeType, this.sampleMimeType, codecsOfType, n, this.maxInputSize, this.width, this.height, n2, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, this.encoderDelay, this.encoderPadding, this.selectionFlags | format.selectionFlags, language, this.accessibilityChannel, this.subsampleOffsetUs, this.initializationData, DrmInitData.createSessionCreationData(format.drmInitData, this.drmInitData), this.metadata);
    }
    
    public Format copyWithMaxInputSize(final int n) {
        return new Format(this.id, this.label, this.containerMimeType, this.sampleMimeType, this.codecs, this.bitrate, n, this.width, this.height, this.frameRate, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, this.encoderDelay, this.encoderPadding, this.selectionFlags, this.language, this.accessibilityChannel, this.subsampleOffsetUs, this.initializationData, this.drmInitData, this.metadata);
    }
    
    public Format copyWithMetadata(final Metadata metadata) {
        return new Format(this.id, this.label, this.containerMimeType, this.sampleMimeType, this.codecs, this.bitrate, this.maxInputSize, this.width, this.height, this.frameRate, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, this.encoderDelay, this.encoderPadding, this.selectionFlags, this.language, this.accessibilityChannel, this.subsampleOffsetUs, this.initializationData, this.drmInitData, metadata);
    }
    
    public Format copyWithSubsampleOffsetUs(final long n) {
        return new Format(this.id, this.label, this.containerMimeType, this.sampleMimeType, this.codecs, this.bitrate, this.maxInputSize, this.width, this.height, this.frameRate, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, this.encoderDelay, this.encoderPadding, this.selectionFlags, this.language, this.accessibilityChannel, n, this.initializationData, this.drmInitData, this.metadata);
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (o != null && Format.class == o.getClass()) {
            final Format format = (Format)o;
            final int hashCode = this.hashCode;
            if (hashCode != 0) {
                final int hashCode2 = format.hashCode;
                if (hashCode2 != 0 && hashCode != hashCode2) {
                    return false;
                }
            }
            if (this.bitrate != format.bitrate || this.maxInputSize != format.maxInputSize || this.width != format.width || this.height != format.height || Float.compare(this.frameRate, format.frameRate) != 0 || this.rotationDegrees != format.rotationDegrees || Float.compare(this.pixelWidthHeightRatio, format.pixelWidthHeightRatio) != 0 || this.stereoMode != format.stereoMode || this.channelCount != format.channelCount || this.sampleRate != format.sampleRate || this.pcmEncoding != format.pcmEncoding || this.encoderDelay != format.encoderDelay || this.encoderPadding != format.encoderPadding || this.subsampleOffsetUs != format.subsampleOffsetUs || this.selectionFlags != format.selectionFlags || !Util.areEqual(this.id, format.id) || !Util.areEqual(this.label, format.label) || !Util.areEqual(this.language, format.language) || this.accessibilityChannel != format.accessibilityChannel || !Util.areEqual(this.containerMimeType, format.containerMimeType) || !Util.areEqual(this.sampleMimeType, format.sampleMimeType) || !Util.areEqual(this.codecs, format.codecs) || !Util.areEqual(this.drmInitData, format.drmInitData) || !Util.areEqual(this.metadata, format.metadata) || !Util.areEqual(this.colorInfo, format.colorInfo) || !Arrays.equals(this.projectionData, format.projectionData) || !this.initializationDataEquals(format)) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    public int getPixelCount() {
        final int width = this.width;
        int n = -1;
        if (width != -1) {
            final int height = this.height;
            if (height == -1) {
                n = n;
            }
            else {
                n = width * height;
            }
        }
        return n;
    }
    
    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            final String id = this.id;
            int hashCode = 0;
            int hashCode2;
            if (id == null) {
                hashCode2 = 0;
            }
            else {
                hashCode2 = id.hashCode();
            }
            final String containerMimeType = this.containerMimeType;
            int hashCode3;
            if (containerMimeType == null) {
                hashCode3 = 0;
            }
            else {
                hashCode3 = containerMimeType.hashCode();
            }
            final String sampleMimeType = this.sampleMimeType;
            int hashCode4;
            if (sampleMimeType == null) {
                hashCode4 = 0;
            }
            else {
                hashCode4 = sampleMimeType.hashCode();
            }
            final String codecs = this.codecs;
            int hashCode5;
            if (codecs == null) {
                hashCode5 = 0;
            }
            else {
                hashCode5 = codecs.hashCode();
            }
            final int bitrate = this.bitrate;
            final int width = this.width;
            final int height = this.height;
            final int channelCount = this.channelCount;
            final int sampleRate = this.sampleRate;
            final String language = this.language;
            int hashCode6;
            if (language == null) {
                hashCode6 = 0;
            }
            else {
                hashCode6 = language.hashCode();
            }
            final int accessibilityChannel = this.accessibilityChannel;
            final DrmInitData drmInitData = this.drmInitData;
            int hashCode7;
            if (drmInitData == null) {
                hashCode7 = 0;
            }
            else {
                hashCode7 = drmInitData.hashCode();
            }
            final Metadata metadata = this.metadata;
            int hashCode8;
            if (metadata == null) {
                hashCode8 = 0;
            }
            else {
                hashCode8 = metadata.hashCode();
            }
            final String label = this.label;
            if (label != null) {
                hashCode = label.hashCode();
            }
            this.hashCode = (((((((((((((((((((((((527 + hashCode2) * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode5) * 31 + bitrate) * 31 + width) * 31 + height) * 31 + channelCount) * 31 + sampleRate) * 31 + hashCode6) * 31 + accessibilityChannel) * 31 + hashCode7) * 31 + hashCode8) * 31 + hashCode) * 31 + this.maxInputSize) * 31 + (int)this.subsampleOffsetUs) * 31 + Float.floatToIntBits(this.frameRate)) * 31 + Float.floatToIntBits(this.pixelWidthHeightRatio)) * 31 + this.rotationDegrees) * 31 + this.stereoMode) * 31 + this.pcmEncoding) * 31 + this.encoderDelay) * 31 + this.encoderPadding) * 31 + this.selectionFlags;
        }
        return this.hashCode;
    }
    
    public boolean initializationDataEquals(final Format format) {
        if (this.initializationData.size() != format.initializationData.size()) {
            return false;
        }
        for (int i = 0; i < this.initializationData.size(); ++i) {
            if (!Arrays.equals(this.initializationData.get(i), format.initializationData.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Format(");
        sb.append(this.id);
        sb.append(", ");
        sb.append(this.label);
        sb.append(", ");
        sb.append(this.containerMimeType);
        sb.append(", ");
        sb.append(this.sampleMimeType);
        sb.append(", ");
        sb.append(this.codecs);
        sb.append(", ");
        sb.append(this.bitrate);
        sb.append(", ");
        sb.append(this.language);
        sb.append(", [");
        sb.append(this.width);
        sb.append(", ");
        sb.append(this.height);
        sb.append(", ");
        sb.append(this.frameRate);
        sb.append("], [");
        sb.append(this.channelCount);
        sb.append(", ");
        sb.append(this.sampleRate);
        sb.append("])");
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.label);
        parcel.writeString(this.containerMimeType);
        parcel.writeString(this.sampleMimeType);
        parcel.writeString(this.codecs);
        parcel.writeInt(this.bitrate);
        parcel.writeInt(this.maxInputSize);
        parcel.writeInt(this.width);
        parcel.writeInt(this.height);
        parcel.writeFloat(this.frameRate);
        parcel.writeInt(this.rotationDegrees);
        parcel.writeFloat(this.pixelWidthHeightRatio);
        Util.writeBoolean(parcel, this.projectionData != null);
        final byte[] projectionData = this.projectionData;
        if (projectionData != null) {
            parcel.writeByteArray(projectionData);
        }
        parcel.writeInt(this.stereoMode);
        parcel.writeParcelable((Parcelable)this.colorInfo, i);
        parcel.writeInt(this.channelCount);
        parcel.writeInt(this.sampleRate);
        parcel.writeInt(this.pcmEncoding);
        parcel.writeInt(this.encoderDelay);
        parcel.writeInt(this.encoderPadding);
        parcel.writeInt(this.selectionFlags);
        parcel.writeString(this.language);
        parcel.writeInt(this.accessibilityChannel);
        parcel.writeLong(this.subsampleOffsetUs);
        final int size = this.initializationData.size();
        parcel.writeInt(size);
        for (i = 0; i < size; ++i) {
            parcel.writeByteArray((byte[])this.initializationData.get(i));
        }
        parcel.writeParcelable((Parcelable)this.drmInitData, 0);
        parcel.writeParcelable((Parcelable)this.metadata, 0);
    }
}
