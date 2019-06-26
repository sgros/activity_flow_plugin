package com.coremedia.iso.boxes.sampleentry;

import com.googlecode.mp4parser.AbstractContainerBox;

public abstract class AbstractSampleEntry extends AbstractContainerBox implements SampleEntry {
    protected int dataReferenceIndex = 1;

    protected AbstractSampleEntry(String str) {
        super(str);
    }

    public void setDataReferenceIndex(int i) {
        this.dataReferenceIndex = i;
    }
}
