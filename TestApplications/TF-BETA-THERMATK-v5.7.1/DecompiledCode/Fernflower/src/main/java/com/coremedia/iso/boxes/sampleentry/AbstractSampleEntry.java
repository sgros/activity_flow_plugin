package com.coremedia.iso.boxes.sampleentry;

import com.googlecode.mp4parser.AbstractContainerBox;

public abstract class AbstractSampleEntry extends AbstractContainerBox implements SampleEntry {
   protected int dataReferenceIndex = 1;

   protected AbstractSampleEntry(String var1) {
      super(var1);
   }

   public void setDataReferenceIndex(int var1) {
      this.dataReferenceIndex = var1;
   }
}
