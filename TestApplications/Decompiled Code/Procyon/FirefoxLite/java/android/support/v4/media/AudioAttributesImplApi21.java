// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.media;

import android.media.AudioAttributes;
import android.annotation.TargetApi;

@TargetApi(21)
class AudioAttributesImplApi21 implements AudioAttributesImpl
{
    AudioAttributes mAudioAttributes;
    int mLegacyStreamType;
    
    AudioAttributesImplApi21() {
        this.mLegacyStreamType = -1;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof AudioAttributesImplApi21 && this.mAudioAttributes.equals((Object)((AudioAttributesImplApi21)o).mAudioAttributes);
    }
    
    @Override
    public int hashCode() {
        return this.mAudioAttributes.hashCode();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("AudioAttributesCompat: audioattributes=");
        sb.append(this.mAudioAttributes);
        return sb.toString();
    }
}
