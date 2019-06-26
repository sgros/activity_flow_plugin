// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text.dvb;

import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.exoplayer2.text.Subtitle;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.List;
import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;

public final class DvbDecoder extends SimpleSubtitleDecoder
{
    private final DvbParser parser;
    
    public DvbDecoder(final List<byte[]> list) {
        super("DvbDecoder");
        final ParsableByteArray parsableByteArray = new ParsableByteArray(list.get(0));
        this.parser = new DvbParser(parsableByteArray.readUnsignedShort(), parsableByteArray.readUnsignedShort());
    }
    
    @Override
    protected DvbSubtitle decode(final byte[] array, final int n, final boolean b) {
        if (b) {
            this.parser.reset();
        }
        return new DvbSubtitle(this.parser.decode(array, n));
    }
}
