// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.mp3;

import com.google.android.exoplayer2.extractor.MpegAudioHeader;
import com.google.android.exoplayer2.extractor.ConstantBitrateSeekMap;

final class ConstantBitrateSeeker extends ConstantBitrateSeekMap implements Seeker
{
    public ConstantBitrateSeeker(final long n, final long n2, final MpegAudioHeader mpegAudioHeader) {
        super(n, n2, mpegAudioHeader.bitrate, mpegAudioHeader.frameSize);
    }
    
    @Override
    public long getDataEndPosition() {
        return -1L;
    }
    
    @Override
    public long getTimeUs(final long n) {
        return this.getTimeUsAtPosition(n);
    }
}
