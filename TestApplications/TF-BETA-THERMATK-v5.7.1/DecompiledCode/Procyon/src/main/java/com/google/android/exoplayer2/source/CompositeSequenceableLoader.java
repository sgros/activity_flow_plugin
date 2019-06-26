// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source;

public class CompositeSequenceableLoader implements SequenceableLoader
{
    protected final SequenceableLoader[] loaders;
    
    public CompositeSequenceableLoader(final SequenceableLoader[] loaders) {
        this.loaders = loaders;
    }
    
    @Override
    public boolean continueLoading(final long n) {
        boolean b = false;
        int i;
        boolean b3;
        do {
            final long nextLoadPositionUs = this.getNextLoadPositionUs();
            if (nextLoadPositionUs == Long.MIN_VALUE) {
                return b;
            }
            final SequenceableLoader[] loaders = this.loaders;
            final int length = loaders.length;
            int j = 0;
            i = 0;
            while (j < length) {
                final SequenceableLoader sequenceableLoader = loaders[j];
                final long nextLoadPositionUs2 = sequenceableLoader.getNextLoadPositionUs();
                final boolean b2 = nextLoadPositionUs2 != Long.MIN_VALUE && nextLoadPositionUs2 <= n;
                int n2 = 0;
                Label_0115: {
                    if (nextLoadPositionUs2 != nextLoadPositionUs) {
                        n2 = i;
                        if (!b2) {
                            break Label_0115;
                        }
                    }
                    n2 = (i | (sequenceableLoader.continueLoading(n) ? 1 : 0));
                }
                ++j;
                i = n2;
            }
            b3 = (b |= (i != 0));
        } while (i != 0);
        b = b3;
        return b;
    }
    
    @Override
    public final long getBufferedPositionUs() {
        final SequenceableLoader[] loaders = this.loaders;
        final int length = loaders.length;
        int i = 0;
        long a = Long.MAX_VALUE;
        while (i < length) {
            final long bufferedPositionUs = loaders[i].getBufferedPositionUs();
            long min = a;
            if (bufferedPositionUs != Long.MIN_VALUE) {
                min = Math.min(a, bufferedPositionUs);
            }
            ++i;
            a = min;
        }
        long n = a;
        if (a == Long.MAX_VALUE) {
            n = Long.MIN_VALUE;
        }
        return n;
    }
    
    @Override
    public final long getNextLoadPositionUs() {
        final SequenceableLoader[] loaders = this.loaders;
        final int length = loaders.length;
        int i = 0;
        long a = Long.MAX_VALUE;
        while (i < length) {
            final long nextLoadPositionUs = loaders[i].getNextLoadPositionUs();
            long min = a;
            if (nextLoadPositionUs != Long.MIN_VALUE) {
                min = Math.min(a, nextLoadPositionUs);
            }
            ++i;
            a = min;
        }
        long n = a;
        if (a == Long.MAX_VALUE) {
            n = Long.MIN_VALUE;
        }
        return n;
    }
    
    @Override
    public final void reevaluateBuffer(final long n) {
        final SequenceableLoader[] loaders = this.loaders;
        for (int length = loaders.length, i = 0; i < length; ++i) {
            loaders[i].reevaluateBuffer(n);
        }
    }
}
