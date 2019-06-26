// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor;

import java.util.Arrays;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.IOException;
import com.google.android.exoplayer2.Format;

public interface TrackOutput
{
    void format(final Format p0);
    
    int sampleData(final ExtractorInput p0, final int p1, final boolean p2) throws IOException, InterruptedException;
    
    void sampleData(final ParsableByteArray p0, final int p1);
    
    void sampleMetadata(final long p0, final int p1, final int p2, final int p3, final CryptoData p4);
    
    public static final class CryptoData
    {
        public final int clearBlocks;
        public final int cryptoMode;
        public final int encryptedBlocks;
        public final byte[] encryptionKey;
        
        public CryptoData(final int cryptoMode, final byte[] encryptionKey, final int encryptedBlocks, final int clearBlocks) {
            this.cryptoMode = cryptoMode;
            this.encryptionKey = encryptionKey;
            this.encryptedBlocks = encryptedBlocks;
            this.clearBlocks = clearBlocks;
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b = true;
            if (this == o) {
                return true;
            }
            if (o != null && CryptoData.class == o.getClass()) {
                final CryptoData cryptoData = (CryptoData)o;
                if (this.cryptoMode != cryptoData.cryptoMode || this.encryptedBlocks != cryptoData.encryptedBlocks || this.clearBlocks != cryptoData.clearBlocks || !Arrays.equals(this.encryptionKey, cryptoData.encryptionKey)) {
                    b = false;
                }
                return b;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return ((this.cryptoMode * 31 + Arrays.hashCode(this.encryptionKey)) * 31 + this.encryptedBlocks) * 31 + this.clearBlocks;
        }
    }
}
