// 
// Decompiled by Procyon v0.5.34
// 

package com.coremedia.iso.boxes.sampleentry;

import com.googlecode.mp4parser.AbstractContainerBox;

public abstract class AbstractSampleEntry extends AbstractContainerBox implements SampleEntry
{
    protected int dataReferenceIndex;
    
    protected AbstractSampleEntry(final String s) {
        super(s);
        this.dataReferenceIndex = 1;
    }
    
    public void setDataReferenceIndex(final int dataReferenceIndex) {
        this.dataReferenceIndex = dataReferenceIndex;
    }
}
