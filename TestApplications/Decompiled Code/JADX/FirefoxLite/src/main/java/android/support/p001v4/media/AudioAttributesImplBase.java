package android.support.p001v4.media;

import java.util.Arrays;

/* renamed from: android.support.v4.media.AudioAttributesImplBase */
class AudioAttributesImplBase implements AudioAttributesImpl {
    int mContentType = 0;
    int mFlags = 0;
    int mLegacyStream = -1;
    int mUsage = 0;

    AudioAttributesImplBase() {
    }

    public int getLegacyStreamType() {
        if (this.mLegacyStream != -1) {
            return this.mLegacyStream;
        }
        return AudioAttributesCompat.toVolumeStreamType(false, this.mFlags, this.mUsage);
    }

    public int getContentType() {
        return this.mContentType;
    }

    public int getUsage() {
        return this.mUsage;
    }

    public int getFlags() {
        int i = this.mFlags;
        int legacyStreamType = getLegacyStreamType();
        if (legacyStreamType == 6) {
            i |= 4;
        } else if (legacyStreamType == 7) {
            i |= 1;
        }
        return i & 273;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Integer.valueOf(this.mContentType), Integer.valueOf(this.mFlags), Integer.valueOf(this.mUsage), Integer.valueOf(this.mLegacyStream)});
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof AudioAttributesImplBase)) {
            return false;
        }
        AudioAttributesImplBase audioAttributesImplBase = (AudioAttributesImplBase) obj;
        if (this.mContentType == audioAttributesImplBase.getContentType() && this.mFlags == audioAttributesImplBase.getFlags() && this.mUsage == audioAttributesImplBase.getUsage() && this.mLegacyStream == audioAttributesImplBase.mLegacyStream) {
            z = true;
        }
        return z;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("AudioAttributesCompat:");
        if (this.mLegacyStream != -1) {
            stringBuilder.append(" stream=");
            stringBuilder.append(this.mLegacyStream);
            stringBuilder.append(" derived");
        }
        stringBuilder.append(" usage=");
        stringBuilder.append(AudioAttributesCompat.usageToString(this.mUsage));
        stringBuilder.append(" content=");
        stringBuilder.append(this.mContentType);
        stringBuilder.append(" flags=0x");
        stringBuilder.append(Integer.toHexString(this.mFlags).toUpperCase());
        return stringBuilder.toString();
    }
}
