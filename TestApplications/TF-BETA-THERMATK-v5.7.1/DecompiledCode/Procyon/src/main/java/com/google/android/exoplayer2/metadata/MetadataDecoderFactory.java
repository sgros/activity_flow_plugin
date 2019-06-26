// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.metadata;

import com.google.android.exoplayer2.metadata.icy.IcyDecoder;
import com.google.android.exoplayer2.metadata.scte35.SpliceInfoDecoder;
import com.google.android.exoplayer2.metadata.emsg.EventMessageDecoder;
import com.google.android.exoplayer2.metadata.id3.Id3Decoder;
import com.google.android.exoplayer2.Format;

public interface MetadataDecoderFactory
{
    public static final MetadataDecoderFactory DEFAULT = new MetadataDecoderFactory() {
        @Override
        public MetadataDecoder createDecoder(final Format format) {
            final String sampleMimeType = format.sampleMimeType;
            int n = 0;
            Label_0113: {
                switch (sampleMimeType.hashCode()) {
                    case 1652648887: {
                        if (sampleMimeType.equals("application/x-scte35")) {
                            n = 2;
                            break Label_0113;
                        }
                        break;
                    }
                    case 1154383568: {
                        if (sampleMimeType.equals("application/x-emsg")) {
                            n = 1;
                            break Label_0113;
                        }
                        break;
                    }
                    case -1248341703: {
                        if (sampleMimeType.equals("application/id3")) {
                            n = 0;
                            break Label_0113;
                        }
                        break;
                    }
                    case -1348231605: {
                        if (sampleMimeType.equals("application/x-icy")) {
                            n = 3;
                            break Label_0113;
                        }
                        break;
                    }
                }
                n = -1;
            }
            if (n == 0) {
                return new Id3Decoder();
            }
            if (n == 1) {
                return new EventMessageDecoder();
            }
            if (n == 2) {
                return new SpliceInfoDecoder();
            }
            if (n == 3) {
                return new IcyDecoder();
            }
            throw new IllegalArgumentException("Attempted to create decoder for unsupported format");
        }
        
        @Override
        public boolean supportsFormat(final Format format) {
            final String sampleMimeType = format.sampleMimeType;
            return "application/id3".equals(sampleMimeType) || "application/x-emsg".equals(sampleMimeType) || "application/x-scte35".equals(sampleMimeType) || "application/x-icy".equals(sampleMimeType);
        }
    };
    
    MetadataDecoder createDecoder(final Format p0);
    
    boolean supportsFormat(final Format p0);
}
