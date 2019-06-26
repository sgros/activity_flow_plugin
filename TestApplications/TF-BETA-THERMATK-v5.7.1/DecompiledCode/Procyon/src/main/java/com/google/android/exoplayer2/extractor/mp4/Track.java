// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.Format;

public final class Track
{
    public final long durationUs;
    public final long[] editListDurations;
    public final long[] editListMediaTimes;
    public final Format format;
    public final int id;
    public final long movieTimescale;
    public final int nalUnitLengthFieldLength;
    private final TrackEncryptionBox[] sampleDescriptionEncryptionBoxes;
    public final int sampleTransformation;
    public final long timescale;
    public final int type;
    
    public Track(final int id, final int type, final long timescale, final long movieTimescale, final long durationUs, final Format format, final int sampleTransformation, final TrackEncryptionBox[] sampleDescriptionEncryptionBoxes, final int nalUnitLengthFieldLength, final long[] editListDurations, final long[] editListMediaTimes) {
        this.id = id;
        this.type = type;
        this.timescale = timescale;
        this.movieTimescale = movieTimescale;
        this.durationUs = durationUs;
        this.format = format;
        this.sampleTransformation = sampleTransformation;
        this.sampleDescriptionEncryptionBoxes = sampleDescriptionEncryptionBoxes;
        this.nalUnitLengthFieldLength = nalUnitLengthFieldLength;
        this.editListDurations = editListDurations;
        this.editListMediaTimes = editListMediaTimes;
    }
    
    public TrackEncryptionBox getSampleDescriptionEncryptionBox(final int n) {
        final TrackEncryptionBox[] sampleDescriptionEncryptionBoxes = this.sampleDescriptionEncryptionBoxes;
        TrackEncryptionBox trackEncryptionBox;
        if (sampleDescriptionEncryptionBoxes == null) {
            trackEncryptionBox = null;
        }
        else {
            trackEncryptionBox = sampleDescriptionEncryptionBoxes[n];
        }
        return trackEncryptionBox;
    }
}
