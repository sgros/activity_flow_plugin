// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.audio;

import android.annotation.TargetApi;
import android.media.AudioAttributes$Builder;

public final class AudioAttributes
{
    public static final AudioAttributes DEFAULT;
    private android.media.AudioAttributes audioAttributesV21;
    public final int contentType;
    public final int flags;
    public final int usage;
    
    static {
        DEFAULT = new Builder().build();
    }
    
    private AudioAttributes(final int contentType, final int flags, final int usage) {
        this.contentType = contentType;
        this.flags = flags;
        this.usage = usage;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (o != null && AudioAttributes.class == o.getClass()) {
            final AudioAttributes audioAttributes = (AudioAttributes)o;
            if (this.contentType != audioAttributes.contentType || this.flags != audioAttributes.flags || this.usage != audioAttributes.usage) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    @TargetApi(21)
    public android.media.AudioAttributes getAudioAttributesV21() {
        if (this.audioAttributesV21 == null) {
            this.audioAttributesV21 = new AudioAttributes$Builder().setContentType(this.contentType).setFlags(this.flags).setUsage(this.usage).build();
        }
        return this.audioAttributesV21;
    }
    
    @Override
    public int hashCode() {
        return ((527 + this.contentType) * 31 + this.flags) * 31 + this.usage;
    }
    
    public static final class Builder
    {
        private int contentType;
        private int flags;
        private int usage;
        
        public Builder() {
            this.contentType = 0;
            this.flags = 0;
            this.usage = 1;
        }
        
        public AudioAttributes build() {
            return new AudioAttributes(this.contentType, this.flags, this.usage, null);
        }
        
        public Builder setContentType(final int contentType) {
            this.contentType = contentType;
            return this;
        }
        
        public Builder setUsage(final int usage) {
            this.usage = usage;
            return this;
        }
    }
}
