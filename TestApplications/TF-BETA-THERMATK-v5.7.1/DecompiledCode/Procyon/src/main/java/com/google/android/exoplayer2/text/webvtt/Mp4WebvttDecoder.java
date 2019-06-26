// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text.webvtt;

import java.util.List;
import java.util.ArrayList;
import com.google.android.exoplayer2.text.Subtitle;
import java.util.Collections;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;

public final class Mp4WebvttDecoder extends SimpleSubtitleDecoder
{
    private static final int TYPE_payl;
    private static final int TYPE_sttg;
    private static final int TYPE_vttc;
    private final WebvttCue.Builder builder;
    private final ParsableByteArray sampleData;
    
    static {
        TYPE_payl = Util.getIntegerCodeForString("payl");
        TYPE_sttg = Util.getIntegerCodeForString("sttg");
        TYPE_vttc = Util.getIntegerCodeForString("vttc");
    }
    
    public Mp4WebvttDecoder() {
        super("Mp4WebvttDecoder");
        this.sampleData = new ParsableByteArray();
        this.builder = new WebvttCue.Builder();
    }
    
    private static Cue parseVttCueBox(final ParsableByteArray parsableByteArray, final WebvttCue.Builder builder, int i) throws SubtitleDecoderException {
        builder.reset();
        while (i > 0) {
            if (i < 8) {
                throw new SubtitleDecoderException("Incomplete vtt cue box header found.");
            }
            int int1 = parsableByteArray.readInt();
            final int int2 = parsableByteArray.readInt();
            int1 -= 8;
            final String fromUtf8Bytes = Util.fromUtf8Bytes(parsableByteArray.data, parsableByteArray.getPosition(), int1);
            parsableByteArray.skipBytes(int1);
            final int n = i - 8 - int1;
            if (int2 == Mp4WebvttDecoder.TYPE_sttg) {
                WebvttCueParser.parseCueSettingsList(fromUtf8Bytes, builder);
                i = n;
            }
            else {
                i = n;
                if (int2 != Mp4WebvttDecoder.TYPE_payl) {
                    continue;
                }
                WebvttCueParser.parseCueText(null, fromUtf8Bytes.trim(), builder, Collections.emptyList());
                i = n;
            }
        }
        return builder.build();
    }
    
    @Override
    protected Mp4WebvttSubtitle decode(final byte[] array, int int1, final boolean b) throws SubtitleDecoderException {
        this.sampleData.reset(array, int1);
        final ArrayList<Cue> list = new ArrayList<Cue>();
        while (this.sampleData.bytesLeft() > 0) {
            if (this.sampleData.bytesLeft() < 8) {
                throw new SubtitleDecoderException("Incomplete Mp4Webvtt Top Level box header found.");
            }
            int1 = this.sampleData.readInt();
            if (this.sampleData.readInt() == Mp4WebvttDecoder.TYPE_vttc) {
                list.add(parseVttCueBox(this.sampleData, this.builder, int1 - 8));
            }
            else {
                this.sampleData.skipBytes(int1 - 8);
            }
        }
        return new Mp4WebvttSubtitle(list);
    }
}
