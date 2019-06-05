// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.media;

import java.util.Arrays;

class AudioAttributesImplBase implements AudioAttributesImpl
{
    int mContentType;
    int mFlags;
    int mLegacyStream;
    int mUsage;
    
    AudioAttributesImplBase() {
        this.mUsage = 0;
        this.mContentType = 0;
        this.mFlags = 0;
        this.mLegacyStream = -1;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof AudioAttributesImplBase;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final AudioAttributesImplBase audioAttributesImplBase = (AudioAttributesImplBase)o;
        boolean b3 = b2;
        if (this.mContentType == audioAttributesImplBase.getContentType()) {
            b3 = b2;
            if (this.mFlags == audioAttributesImplBase.getFlags()) {
                b3 = b2;
                if (this.mUsage == audioAttributesImplBase.getUsage()) {
                    b3 = b2;
                    if (this.mLegacyStream == audioAttributesImplBase.mLegacyStream) {
                        b3 = true;
                    }
                }
            }
        }
        return b3;
    }
    
    public int getContentType() {
        return this.mContentType;
    }
    
    public int getFlags() {
        final int mFlags = this.mFlags;
        final int legacyStreamType = this.getLegacyStreamType();
        int n;
        if (legacyStreamType == 6) {
            n = (mFlags | 0x4);
        }
        else {
            n = mFlags;
            if (legacyStreamType == 7) {
                n = (mFlags | 0x1);
            }
        }
        return n & 0x111;
    }
    
    public int getLegacyStreamType() {
        if (this.mLegacyStream != -1) {
            return this.mLegacyStream;
        }
        return AudioAttributesCompat.toVolumeStreamType(false, this.mFlags, this.mUsage);
    }
    
    public int getUsage() {
        return this.mUsage;
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] { this.mContentType, this.mFlags, this.mUsage, this.mLegacyStream });
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AudioAttributesCompat:");
        if (this.mLegacyStream != -1) {
            sb.append(" stream=");
            sb.append(this.mLegacyStream);
            sb.append(" derived");
        }
        sb.append(" usage=");
        sb.append(AudioAttributesCompat.usageToString(this.mUsage));
        sb.append(" content=");
        sb.append(this.mContentType);
        sb.append(" flags=0x");
        sb.append(Integer.toHexString(this.mFlags).toUpperCase());
        return sb.toString();
    }
}
