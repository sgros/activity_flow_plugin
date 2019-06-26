// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text;

import com.google.android.exoplayer2.decoder.Decoder;

public interface SubtitleDecoder extends Decoder<SubtitleInputBuffer, SubtitleOutputBuffer, SubtitleDecoderException>
{
    void setPositionUs(final long p0);
}
