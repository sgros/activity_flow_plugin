package com.google.android.exoplayer2.source;

public final class DefaultCompositeSequenceableLoaderFactory implements CompositeSequenceableLoaderFactory {
   public SequenceableLoader createCompositeSequenceableLoader(SequenceableLoader... var1) {
      return new CompositeSequenceableLoader(var1);
   }
}
