package com.google.android.exoplayer2;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.ColorInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Format implements Parcelable {
    public static final Creator<Format> CREATOR = new C01341();
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
    /* renamed from: id */
    public final String f14id;
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

    /* renamed from: com.google.android.exoplayer2.Format$1 */
    static class C01341 implements Creator<Format> {
        C01341() {
        }

        public Format createFromParcel(Parcel parcel) {
            return new Format(parcel);
        }

        public Format[] newArray(int i) {
            return new Format[i];
        }
    }

    public int describeContents() {
        return 0;
    }

    public static Format createVideoContainerFormat(String str, String str2, String str3, String str4, String str5, int i, int i2, int i3, float f, List<byte[]> list, int i4) {
        return new Format(str, str2, str3, str4, str5, i, -1, i2, i3, f, -1, -1.0f, null, -1, null, -1, -1, -1, -1, -1, i4, null, -1, TimestampAdjuster.DO_NOT_OFFSET, list, null, null);
    }

    public static Format createVideoSampleFormat(String str, String str2, String str3, int i, int i2, int i3, int i4, float f, List<byte[]> list, int i5, float f2, DrmInitData drmInitData) {
        return createVideoSampleFormat(str, str2, str3, i, i2, i3, i4, f, list, i5, f2, null, -1, null, drmInitData);
    }

    public static Format createVideoSampleFormat(String str, String str2, String str3, int i, int i2, int i3, int i4, float f, List<byte[]> list, int i5, float f2, byte[] bArr, int i6, ColorInfo colorInfo, DrmInitData drmInitData) {
        return new Format(str, null, null, str2, str3, i, i2, i3, i4, f, i5, f2, bArr, i6, colorInfo, -1, -1, -1, -1, -1, 0, null, -1, TimestampAdjuster.DO_NOT_OFFSET, list, drmInitData, null);
    }

    public static Format createAudioContainerFormat(String str, String str2, String str3, String str4, String str5, int i, int i2, int i3, List<byte[]> list, int i4, String str6) {
        return new Format(str, str2, str3, str4, str5, i, -1, -1, -1, -1.0f, -1, -1.0f, null, -1, null, i2, i3, -1, -1, -1, i4, str6, -1, TimestampAdjuster.DO_NOT_OFFSET, list, null, null);
    }

    public static Format createAudioSampleFormat(String str, String str2, String str3, int i, int i2, int i3, int i4, List<byte[]> list, DrmInitData drmInitData, int i5, String str4) {
        return createAudioSampleFormat(str, str2, str3, i, i2, i3, i4, -1, list, drmInitData, i5, str4);
    }

    public static Format createAudioSampleFormat(String str, String str2, String str3, int i, int i2, int i3, int i4, int i5, List<byte[]> list, DrmInitData drmInitData, int i6, String str4) {
        return createAudioSampleFormat(str, str2, str3, i, i2, i3, i4, i5, -1, -1, list, drmInitData, i6, str4, null);
    }

    public static Format createAudioSampleFormat(String str, String str2, String str3, int i, int i2, int i3, int i4, int i5, int i6, int i7, List<byte[]> list, DrmInitData drmInitData, int i8, String str4, Metadata metadata) {
        return new Format(str, null, null, str2, str3, i, i2, -1, -1, -1.0f, -1, -1.0f, null, -1, null, i3, i4, i5, i6, i7, i8, str4, -1, TimestampAdjuster.DO_NOT_OFFSET, list, drmInitData, metadata);
    }

    public static Format createTextContainerFormat(String str, String str2, String str3, String str4, String str5, int i, int i2, String str6) {
        return createTextContainerFormat(str, str2, str3, str4, str5, i, i2, str6, -1);
    }

    public static Format createTextContainerFormat(String str, String str2, String str3, String str4, String str5, int i, int i2, String str6, int i3) {
        return new Format(str, str2, str3, str4, str5, i, -1, -1, -1, -1.0f, -1, -1.0f, null, -1, null, -1, -1, -1, -1, -1, i2, str6, i3, TimestampAdjuster.DO_NOT_OFFSET, null, null, null);
    }

    public static Format createTextSampleFormat(String str, String str2, int i, String str3) {
        return createTextSampleFormat(str, str2, i, str3, null);
    }

    public static Format createTextSampleFormat(String str, String str2, int i, String str3, DrmInitData drmInitData) {
        return createTextSampleFormat(str, str2, null, -1, i, str3, -1, drmInitData, TimestampAdjuster.DO_NOT_OFFSET, Collections.emptyList());
    }

    public static Format createTextSampleFormat(String str, String str2, String str3, int i, int i2, String str4, DrmInitData drmInitData, long j) {
        return createTextSampleFormat(str, str2, str3, i, i2, str4, -1, drmInitData, j, Collections.emptyList());
    }

    public static Format createTextSampleFormat(String str, String str2, String str3, int i, int i2, String str4, int i3, DrmInitData drmInitData, long j, List<byte[]> list) {
        return new Format(str, null, null, str2, str3, i, -1, -1, -1, -1.0f, -1, -1.0f, null, -1, null, -1, -1, -1, -1, -1, i2, str4, i3, j, list, drmInitData, null);
    }

    public static Format createImageSampleFormat(String str, String str2, String str3, int i, int i2, List<byte[]> list, String str4, DrmInitData drmInitData) {
        return new Format(str, null, null, str2, str3, i, -1, -1, -1, -1.0f, -1, -1.0f, null, -1, null, -1, -1, -1, -1, -1, i2, str4, -1, TimestampAdjuster.DO_NOT_OFFSET, list, drmInitData, null);
    }

    public static Format createContainerFormat(String str, String str2, String str3, String str4, String str5, int i, int i2, String str6) {
        return new Format(str, str2, str3, str4, str5, i, -1, -1, -1, -1.0f, -1, -1.0f, null, -1, null, -1, -1, -1, -1, -1, i2, str6, -1, TimestampAdjuster.DO_NOT_OFFSET, null, null, null);
    }

    public static Format createSampleFormat(String str, String str2, long j) {
        return new Format(str, null, null, str2, null, -1, -1, -1, -1, -1.0f, -1, -1.0f, null, -1, null, -1, -1, -1, -1, -1, 0, null, -1, j, null, null, null);
    }

    public static Format createSampleFormat(String str, String str2, String str3, int i, DrmInitData drmInitData) {
        return new Format(str, null, null, str2, str3, i, -1, -1, -1, -1.0f, -1, -1.0f, null, -1, null, -1, -1, -1, -1, -1, 0, null, -1, TimestampAdjuster.DO_NOT_OFFSET, null, drmInitData, null);
    }

    Format(String str, String str2, String str3, String str4, String str5, int i, int i2, int i3, int i4, float f, int i5, float f2, byte[] bArr, int i6, ColorInfo colorInfo, int i7, int i8, int i9, int i10, int i11, int i12, String str6, int i13, long j, List<byte[]> list, DrmInitData drmInitData, Metadata metadata) {
        this.f14id = str;
        this.label = str2;
        this.containerMimeType = str3;
        this.sampleMimeType = str4;
        this.codecs = str5;
        this.bitrate = i;
        this.maxInputSize = i2;
        this.width = i3;
        this.height = i4;
        this.frameRate = f;
        int i14 = i5;
        if (i14 == -1) {
            i14 = 0;
        }
        this.rotationDegrees = i14;
        this.pixelWidthHeightRatio = 1.0f;
        this.projectionData = bArr;
        this.stereoMode = i6;
        this.colorInfo = colorInfo;
        this.channelCount = i7;
        this.sampleRate = i8;
        this.pcmEncoding = i9;
        i14 = i10;
        if (i14 == -1) {
            i14 = 0;
        }
        this.encoderDelay = i14;
        i14 = i11;
        if (i14 == -1) {
            i14 = 0;
        }
        this.encoderPadding = i14;
        this.selectionFlags = i12;
        this.language = str6;
        this.accessibilityChannel = i13;
        this.subsampleOffsetUs = j;
        this.initializationData = list == null ? Collections.emptyList() : list;
        this.drmInitData = drmInitData;
        this.metadata = metadata;
    }

    Format(Parcel parcel) {
        this.f14id = parcel.readString();
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
        this.projectionData = Util.readBoolean(parcel) ? parcel.createByteArray() : null;
        this.stereoMode = parcel.readInt();
        this.colorInfo = (ColorInfo) parcel.readParcelable(ColorInfo.class.getClassLoader());
        this.channelCount = parcel.readInt();
        this.sampleRate = parcel.readInt();
        this.pcmEncoding = parcel.readInt();
        this.encoderDelay = parcel.readInt();
        this.encoderPadding = parcel.readInt();
        this.selectionFlags = parcel.readInt();
        this.language = parcel.readString();
        this.accessibilityChannel = parcel.readInt();
        this.subsampleOffsetUs = parcel.readLong();
        int readInt = parcel.readInt();
        this.initializationData = new ArrayList(readInt);
        for (int i = 0; i < readInt; i++) {
            this.initializationData.add(parcel.createByteArray());
        }
        this.drmInitData = (DrmInitData) parcel.readParcelable(DrmInitData.class.getClassLoader());
        this.metadata = (Metadata) parcel.readParcelable(Metadata.class.getClassLoader());
    }

    public Format copyWithMaxInputSize(int i) {
        int i2 = i;
        return new Format(this.f14id, this.label, this.containerMimeType, this.sampleMimeType, this.codecs, this.bitrate, i2, this.width, this.height, this.frameRate, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, this.encoderDelay, this.encoderPadding, this.selectionFlags, this.language, this.accessibilityChannel, this.subsampleOffsetUs, this.initializationData, this.drmInitData, this.metadata);
    }

    public Format copyWithSubsampleOffsetUs(long j) {
        return new Format(this.f14id, this.label, this.containerMimeType, this.sampleMimeType, this.codecs, this.bitrate, this.maxInputSize, this.width, this.height, this.frameRate, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, this.encoderDelay, this.encoderPadding, this.selectionFlags, this.language, this.accessibilityChannel, j, this.initializationData, this.drmInitData, this.metadata);
    }

    public Format copyWithContainerInfo(String str, String str2, String str3, String str4, int i, int i2, int i3, int i4, String str5) {
        return new Format(str, str2, this.containerMimeType, str3, str4, i, this.maxInputSize, i2, i3, this.frameRate, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, this.encoderDelay, this.encoderPadding, i4, str5, this.accessibilityChannel, this.subsampleOffsetUs, this.initializationData, this.drmInitData, this.metadata);
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x002d  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0034  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x004c  */
    public com.google.android.exoplayer2.Format copyWithManifestFormatInfo(com.google.android.exoplayer2.Format r33) {
        /*
        r32 = this;
        r0 = r32;
        r1 = r33;
        if (r0 != r1) goto L_0x0007;
    L_0x0006:
        return r0;
    L_0x0007:
        r2 = r0.sampleMimeType;
        r2 = com.google.android.exoplayer2.util.MimeTypes.getTrackType(r2);
        r4 = r1.f14id;
        r3 = r1.label;
        if (r3 == 0) goto L_0x0014;
    L_0x0013:
        goto L_0x0016;
    L_0x0014:
        r3 = r0.label;
    L_0x0016:
        r5 = r3;
        r3 = r0.language;
        r6 = 3;
        r7 = 1;
        if (r2 == r6) goto L_0x001f;
    L_0x001d:
        if (r2 != r7) goto L_0x0026;
    L_0x001f:
        r6 = r1.language;
        if (r6 == 0) goto L_0x0026;
    L_0x0023:
        r25 = r6;
        goto L_0x0028;
    L_0x0026:
        r25 = r3;
    L_0x0028:
        r3 = r0.bitrate;
        r6 = -1;
        if (r3 != r6) goto L_0x002f;
    L_0x002d:
        r3 = r1.bitrate;
    L_0x002f:
        r9 = r3;
        r3 = r0.codecs;
        if (r3 != 0) goto L_0x0043;
    L_0x0034:
        r6 = r1.codecs;
        r6 = com.google.android.exoplayer2.util.Util.getCodecsOfType(r6, r2);
        r8 = com.google.android.exoplayer2.util.Util.splitCodecs(r6);
        r8 = r8.length;
        if (r8 != r7) goto L_0x0043;
    L_0x0041:
        r8 = r6;
        goto L_0x0044;
    L_0x0043:
        r8 = r3;
    L_0x0044:
        r3 = r0.frameRate;
        r6 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r6 = (r3 > r6 ? 1 : (r3 == r6 ? 0 : -1));
        if (r6 != 0) goto L_0x0053;
    L_0x004c:
        r6 = 2;
        if (r2 != r6) goto L_0x0053;
    L_0x004f:
        r2 = r1.frameRate;
        r13 = r2;
        goto L_0x0054;
    L_0x0053:
        r13 = r3;
    L_0x0054:
        r2 = r0.selectionFlags;
        r3 = r1.selectionFlags;
        r24 = r2 | r3;
        r1 = r1.drmInitData;
        r2 = r0.drmInitData;
        r30 = com.google.android.exoplayer2.drm.DrmInitData.createSessionCreationData(r1, r2);
        r1 = new com.google.android.exoplayer2.Format;
        r3 = r1;
        r6 = r0.containerMimeType;
        r7 = r0.sampleMimeType;
        r10 = r0.maxInputSize;
        r11 = r0.width;
        r12 = r0.height;
        r14 = r0.rotationDegrees;
        r15 = r0.pixelWidthHeightRatio;
        r2 = r0.projectionData;
        r16 = r2;
        r2 = r0.stereoMode;
        r17 = r2;
        r2 = r0.colorInfo;
        r18 = r2;
        r2 = r0.channelCount;
        r19 = r2;
        r2 = r0.sampleRate;
        r20 = r2;
        r2 = r0.pcmEncoding;
        r21 = r2;
        r2 = r0.encoderDelay;
        r22 = r2;
        r2 = r0.encoderPadding;
        r23 = r2;
        r2 = r0.accessibilityChannel;
        r26 = r2;
        r33 = r1;
        r1 = r0.subsampleOffsetUs;
        r27 = r1;
        r1 = r0.initializationData;
        r29 = r1;
        r1 = r0.metadata;
        r31 = r1;
        r3.<init>(r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23, r24, r25, r26, r27, r29, r30, r31);
        return r33;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.Format.copyWithManifestFormatInfo(com.google.android.exoplayer2.Format):com.google.android.exoplayer2.Format");
    }

    public Format copyWithGaplessInfo(int i, int i2) {
        int i3 = i;
        int i4 = i2;
        return new Format(this.f14id, this.label, this.containerMimeType, this.sampleMimeType, this.codecs, this.bitrate, this.maxInputSize, this.width, this.height, this.frameRate, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, i3, i4, this.selectionFlags, this.language, this.accessibilityChannel, this.subsampleOffsetUs, this.initializationData, this.drmInitData, this.metadata);
    }

    public Format copyWithFrameRate(float f) {
        float f2 = f;
        return new Format(this.f14id, this.label, this.containerMimeType, this.sampleMimeType, this.codecs, this.bitrate, this.maxInputSize, this.width, this.height, f2, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, this.encoderDelay, this.encoderPadding, this.selectionFlags, this.language, this.accessibilityChannel, this.subsampleOffsetUs, this.initializationData, this.drmInitData, this.metadata);
    }

    public Format copyWithDrmInitData(DrmInitData drmInitData) {
        DrmInitData drmInitData2 = drmInitData;
        return new Format(this.f14id, this.label, this.containerMimeType, this.sampleMimeType, this.codecs, this.bitrate, this.maxInputSize, this.width, this.height, this.frameRate, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, this.encoderDelay, this.encoderPadding, this.selectionFlags, this.language, this.accessibilityChannel, this.subsampleOffsetUs, this.initializationData, drmInitData2, this.metadata);
    }

    public Format copyWithMetadata(Metadata metadata) {
        Metadata metadata2 = metadata;
        return new Format(this.f14id, this.label, this.containerMimeType, this.sampleMimeType, this.codecs, this.bitrate, this.maxInputSize, this.width, this.height, this.frameRate, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, this.encoderDelay, this.encoderPadding, this.selectionFlags, this.language, this.accessibilityChannel, this.subsampleOffsetUs, this.initializationData, this.drmInitData, metadata2);
    }

    public Format copyWithBitrate(int i) {
        int i2 = i;
        return new Format(this.f14id, this.label, this.containerMimeType, this.sampleMimeType, this.codecs, i2, this.maxInputSize, this.width, this.height, this.frameRate, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, this.encoderDelay, this.encoderPadding, this.selectionFlags, this.language, this.accessibilityChannel, this.subsampleOffsetUs, this.initializationData, this.drmInitData, this.metadata);
    }

    public int getPixelCount() {
        int i = this.width;
        if (i == -1) {
            return -1;
        }
        int i2 = this.height;
        return i2 == -1 ? -1 : i * i2;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Format(");
        stringBuilder.append(this.f14id);
        String str = ", ";
        stringBuilder.append(str);
        stringBuilder.append(this.label);
        stringBuilder.append(str);
        stringBuilder.append(this.containerMimeType);
        stringBuilder.append(str);
        stringBuilder.append(this.sampleMimeType);
        stringBuilder.append(str);
        stringBuilder.append(this.codecs);
        stringBuilder.append(str);
        stringBuilder.append(this.bitrate);
        stringBuilder.append(str);
        stringBuilder.append(this.language);
        stringBuilder.append(", [");
        stringBuilder.append(this.width);
        stringBuilder.append(str);
        stringBuilder.append(this.height);
        stringBuilder.append(str);
        stringBuilder.append(this.frameRate);
        stringBuilder.append("], [");
        stringBuilder.append(this.channelCount);
        stringBuilder.append(str);
        stringBuilder.append(this.sampleRate);
        stringBuilder.append("])");
        return stringBuilder.toString();
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            String str = this.f14id;
            int i = 0;
            int hashCode = (527 + (str == null ? 0 : str.hashCode())) * 31;
            str = this.containerMimeType;
            hashCode = (hashCode + (str == null ? 0 : str.hashCode())) * 31;
            str = this.sampleMimeType;
            hashCode = (hashCode + (str == null ? 0 : str.hashCode())) * 31;
            str = this.codecs;
            hashCode = (((((((((((hashCode + (str == null ? 0 : str.hashCode())) * 31) + this.bitrate) * 31) + this.width) * 31) + this.height) * 31) + this.channelCount) * 31) + this.sampleRate) * 31;
            str = this.language;
            hashCode = (((hashCode + (str == null ? 0 : str.hashCode())) * 31) + this.accessibilityChannel) * 31;
            DrmInitData drmInitData = this.drmInitData;
            hashCode = (hashCode + (drmInitData == null ? 0 : drmInitData.hashCode())) * 31;
            Metadata metadata = this.metadata;
            hashCode = (hashCode + (metadata == null ? 0 : metadata.hashCode())) * 31;
            str = this.label;
            if (str != null) {
                i = str.hashCode();
            }
            this.hashCode = ((((((((((((((((((((hashCode + i) * 31) + this.maxInputSize) * 31) + ((int) this.subsampleOffsetUs)) * 31) + Float.floatToIntBits(this.frameRate)) * 31) + Float.floatToIntBits(this.pixelWidthHeightRatio)) * 31) + this.rotationDegrees) * 31) + this.stereoMode) * 31) + this.pcmEncoding) * 31) + this.encoderDelay) * 31) + this.encoderPadding) * 31) + this.selectionFlags;
        }
        return this.hashCode;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || Format.class != obj.getClass()) {
            return false;
        }
        Format format = (Format) obj;
        int i = this.hashCode;
        if (i != 0) {
            int i2 = format.hashCode;
            if (!(i2 == 0 || i == i2)) {
                return false;
            }
        }
        if (!(this.bitrate == format.bitrate && this.maxInputSize == format.maxInputSize && this.width == format.width && this.height == format.height && Float.compare(this.frameRate, format.frameRate) == 0 && this.rotationDegrees == format.rotationDegrees && Float.compare(this.pixelWidthHeightRatio, format.pixelWidthHeightRatio) == 0 && this.stereoMode == format.stereoMode && this.channelCount == format.channelCount && this.sampleRate == format.sampleRate && this.pcmEncoding == format.pcmEncoding && this.encoderDelay == format.encoderDelay && this.encoderPadding == format.encoderPadding && this.subsampleOffsetUs == format.subsampleOffsetUs && this.selectionFlags == format.selectionFlags && Util.areEqual(this.f14id, format.f14id) && Util.areEqual(this.label, format.label) && Util.areEqual(this.language, format.language) && this.accessibilityChannel == format.accessibilityChannel && Util.areEqual(this.containerMimeType, format.containerMimeType) && Util.areEqual(this.sampleMimeType, format.sampleMimeType) && Util.areEqual(this.codecs, format.codecs) && Util.areEqual(this.drmInitData, format.drmInitData) && Util.areEqual(this.metadata, format.metadata) && Util.areEqual(this.colorInfo, format.colorInfo) && Arrays.equals(this.projectionData, format.projectionData) && initializationDataEquals(format))) {
            z = false;
        }
        return z;
    }

    public boolean initializationDataEquals(Format format) {
        if (this.initializationData.size() != format.initializationData.size()) {
            return false;
        }
        for (int i = 0; i < this.initializationData.size(); i++) {
            if (!Arrays.equals((byte[]) this.initializationData.get(i), (byte[]) format.initializationData.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static String toLogString(Format format) {
        if (format == null) {
            return "null";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("id=");
        stringBuilder.append(format.f14id);
        stringBuilder.append(", mimeType=");
        stringBuilder.append(format.sampleMimeType);
        if (format.bitrate != -1) {
            stringBuilder.append(", bitrate=");
            stringBuilder.append(format.bitrate);
        }
        if (format.codecs != null) {
            stringBuilder.append(", codecs=");
            stringBuilder.append(format.codecs);
        }
        if (!(format.width == -1 || format.height == -1)) {
            stringBuilder.append(", res=");
            stringBuilder.append(format.width);
            stringBuilder.append("x");
            stringBuilder.append(format.height);
        }
        if (format.frameRate != -1.0f) {
            stringBuilder.append(", fps=");
            stringBuilder.append(format.frameRate);
        }
        if (format.channelCount != -1) {
            stringBuilder.append(", channels=");
            stringBuilder.append(format.channelCount);
        }
        if (format.sampleRate != -1) {
            stringBuilder.append(", sample_rate=");
            stringBuilder.append(format.sampleRate);
        }
        if (format.language != null) {
            stringBuilder.append(", language=");
            stringBuilder.append(format.language);
        }
        if (format.label != null) {
            stringBuilder.append(", label=");
            stringBuilder.append(format.label);
        }
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.f14id);
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
        byte[] bArr = this.projectionData;
        if (bArr != null) {
            parcel.writeByteArray(bArr);
        }
        parcel.writeInt(this.stereoMode);
        parcel.writeParcelable(this.colorInfo, i);
        parcel.writeInt(this.channelCount);
        parcel.writeInt(this.sampleRate);
        parcel.writeInt(this.pcmEncoding);
        parcel.writeInt(this.encoderDelay);
        parcel.writeInt(this.encoderPadding);
        parcel.writeInt(this.selectionFlags);
        parcel.writeString(this.language);
        parcel.writeInt(this.accessibilityChannel);
        parcel.writeLong(this.subsampleOffsetUs);
        i = this.initializationData.size();
        parcel.writeInt(i);
        for (int i2 = 0; i2 < i; i2++) {
            parcel.writeByteArray((byte[]) this.initializationData.get(i2));
        }
        parcel.writeParcelable(this.drmInitData, 0);
        parcel.writeParcelable(this.metadata, 0);
    }
}
