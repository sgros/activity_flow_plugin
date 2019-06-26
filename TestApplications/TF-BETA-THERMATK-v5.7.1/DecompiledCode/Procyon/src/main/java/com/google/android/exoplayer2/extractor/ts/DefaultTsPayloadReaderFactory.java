// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ts;

import android.util.SparseArray;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.text.cea.Cea708InitializationData;
import java.util.ArrayList;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.Collections;
import com.google.android.exoplayer2.Format;
import java.util.List;

public final class DefaultTsPayloadReaderFactory implements Factory
{
    private final List<Format> closedCaptionFormats;
    private final int flags;
    
    public DefaultTsPayloadReaderFactory(final int n) {
        this(n, Collections.singletonList(Format.createTextSampleFormat(null, "application/cea-608", 0, null)));
    }
    
    public DefaultTsPayloadReaderFactory(final int flags, final List<Format> closedCaptionFormats) {
        this.flags = flags;
        this.closedCaptionFormats = closedCaptionFormats;
    }
    
    private SeiReader buildSeiReader(final EsInfo esInfo) {
        return new SeiReader(this.getClosedCaptionFormats(esInfo));
    }
    
    private UserDataReader buildUserDataReader(final EsInfo esInfo) {
        return new UserDataReader(this.getClosedCaptionFormats(esInfo));
    }
    
    private List<Format> getClosedCaptionFormats(final EsInfo esInfo) {
        if (this.isSet(32)) {
            return this.closedCaptionFormats;
        }
        final ParsableByteArray parsableByteArray = new ParsableByteArray(esInfo.descriptorBytes);
        List<Format> closedCaptionFormats = this.closedCaptionFormats;
        while (parsableByteArray.bytesLeft() > 0) {
            final int unsignedByte = parsableByteArray.readUnsignedByte();
            final int unsignedByte2 = parsableByteArray.readUnsignedByte();
            final int position = parsableByteArray.getPosition();
            if (unsignedByte == 134) {
                final ArrayList<Format> list = new ArrayList<Format>();
                final int unsignedByte3 = parsableByteArray.readUnsignedByte();
                int n = 0;
                while (true) {
                    closedCaptionFormats = list;
                    if (n >= (unsignedByte3 & 0x1F)) {
                        break;
                    }
                    final String string = parsableByteArray.readString(3);
                    final int unsignedByte4 = parsableByteArray.readUnsignedByte();
                    boolean b = true;
                    final boolean b2 = (unsignedByte4 & 0x80) != 0x0;
                    int n2;
                    String s;
                    if (b2) {
                        n2 = (unsignedByte4 & 0x3F);
                        s = "application/cea-708";
                    }
                    else {
                        s = "application/cea-608";
                        n2 = 1;
                    }
                    final byte b3 = (byte)parsableByteArray.readUnsignedByte();
                    parsableByteArray.skipBytes(1);
                    List<byte[]> buildData;
                    if (b2) {
                        if ((b3 & 0x40) == 0x0) {
                            b = false;
                        }
                        buildData = Cea708InitializationData.buildData(b);
                    }
                    else {
                        buildData = null;
                    }
                    list.add(Format.createTextSampleFormat(null, s, null, -1, 0, string, n2, null, Long.MAX_VALUE, buildData));
                    ++n;
                }
            }
            parsableByteArray.setPosition(position + unsignedByte2);
        }
        return closedCaptionFormats;
    }
    
    private boolean isSet(final int n) {
        return (n & this.flags) != 0x0;
    }
    
    @Override
    public SparseArray<TsPayloadReader> createInitialPayloadReaders() {
        return (SparseArray<TsPayloadReader>)new SparseArray();
    }
    
    @Override
    public TsPayloadReader createPayloadReader(final int n, final EsInfo esInfo) {
        if (n == 2) {
            return new PesReader(new H262Reader(this.buildUserDataReader(esInfo)));
        }
        if (n == 3 || n == 4) {
            return new PesReader(new MpegAudioReader(esInfo.language));
        }
        final TsPayloadReader tsPayloadReader = null;
        final TsPayloadReader tsPayloadReader2 = null;
        final TsPayloadReader tsPayloadReader3 = null;
        final TsPayloadReader tsPayloadReader4 = null;
        if (n == 15) {
            TsPayloadReader tsPayloadReader5;
            if (this.isSet(2)) {
                tsPayloadReader5 = tsPayloadReader3;
            }
            else {
                tsPayloadReader5 = new PesReader(new AdtsReader(false, esInfo.language));
            }
            return tsPayloadReader5;
        }
        if (n == 17) {
            TsPayloadReader tsPayloadReader6;
            if (this.isSet(2)) {
                tsPayloadReader6 = tsPayloadReader2;
            }
            else {
                tsPayloadReader6 = new PesReader(new LatmReader(esInfo.language));
            }
            return tsPayloadReader6;
        }
        if (n == 21) {
            return new PesReader(new Id3Reader());
        }
        if (n == 27) {
            TsPayloadReader tsPayloadReader7;
            if (this.isSet(4)) {
                tsPayloadReader7 = tsPayloadReader;
            }
            else {
                tsPayloadReader7 = new PesReader(new H264Reader(this.buildSeiReader(esInfo), this.isSet(1), this.isSet(8)));
            }
            return tsPayloadReader7;
        }
        if (n == 36) {
            return new PesReader(new H265Reader(this.buildSeiReader(esInfo)));
        }
        if (n != 89) {
            if (n != 138) {
                if (n != 129) {
                    if (n == 130) {
                        return new PesReader(new DtsReader(esInfo.language));
                    }
                    if (n == 134) {
                        TsPayloadReader tsPayloadReader8;
                        if (this.isSet(16)) {
                            tsPayloadReader8 = tsPayloadReader4;
                        }
                        else {
                            tsPayloadReader8 = new SectionReader(new SpliceInfoSectionReader());
                        }
                        return tsPayloadReader8;
                    }
                    if (n != 135) {
                        return null;
                    }
                }
                return new PesReader(new Ac3Reader(esInfo.language));
            }
            return new PesReader(new DtsReader(esInfo.language));
        }
        return new PesReader(new DvbSubtitleReader(esInfo.dvbSubtitleInfos));
    }
}
