// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source;

public final class DefaultCompositeSequenceableLoaderFactory implements CompositeSequenceableLoaderFactory
{
    @Override
    public SequenceableLoader createCompositeSequenceableLoader(final SequenceableLoader... array) {
        return new CompositeSequenceableLoader(array);
    }
}
