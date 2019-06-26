// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text;

import java.util.List;
import com.google.android.exoplayer2.decoder.OutputBuffer;

public abstract class SubtitleOutputBuffer extends OutputBuffer implements Subtitle
{
    private long subsampleOffsetUs;
    private Subtitle subtitle;
    
    @Override
    public void clear() {
        super.clear();
        this.subtitle = null;
    }
    
    @Override
    public List<Cue> getCues(final long n) {
        return this.subtitle.getCues(n - this.subsampleOffsetUs);
    }
    
    @Override
    public long getEventTime(final int n) {
        return this.subtitle.getEventTime(n) + this.subsampleOffsetUs;
    }
    
    @Override
    public int getEventTimeCount() {
        return this.subtitle.getEventTimeCount();
    }
    
    @Override
    public int getNextEventTimeIndex(final long n) {
        return this.subtitle.getNextEventTimeIndex(n - this.subsampleOffsetUs);
    }
    
    public void setContent(long timeUs, final Subtitle subtitle, final long n) {
        super.timeUs = timeUs;
        this.subtitle = subtitle;
        timeUs = n;
        if (n == Long.MAX_VALUE) {
            timeUs = super.timeUs;
        }
        this.subsampleOffsetUs = timeUs;
    }
}
