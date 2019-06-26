package com.google.android.exoplayer2.extractor.p002ts;

import android.util.SparseArray;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.p002ts.TsPayloadReader.EsInfo;
import com.google.android.exoplayer2.extractor.p002ts.TsPayloadReader.Factory;
import com.google.android.exoplayer2.text.cea.Cea708InitializationData;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* renamed from: com.google.android.exoplayer2.extractor.ts.DefaultTsPayloadReaderFactory */
public final class DefaultTsPayloadReaderFactory implements Factory {
    private final List<Format> closedCaptionFormats;
    private final int flags;

    public DefaultTsPayloadReaderFactory(int i) {
        this(i, Collections.singletonList(Format.createTextSampleFormat(null, MimeTypes.APPLICATION_CEA608, 0, null)));
    }

    public DefaultTsPayloadReaderFactory(int i, List<Format> list) {
        this.flags = i;
        this.closedCaptionFormats = list;
    }

    public SparseArray<TsPayloadReader> createInitialPayloadReaders() {
        return new SparseArray();
    }

    public TsPayloadReader createPayloadReader(int i, EsInfo esInfo) {
        if (i == 2) {
            return new PesReader(new H262Reader(buildUserDataReader(esInfo)));
        }
        if (i == 3 || i == 4) {
            return new PesReader(new MpegAudioReader(esInfo.language));
        }
        TsPayloadReader tsPayloadReader = null;
        if (i == 15) {
            if (!isSet(2)) {
                tsPayloadReader = new PesReader(new AdtsReader(false, esInfo.language));
            }
            return tsPayloadReader;
        } else if (i == 17) {
            if (!isSet(2)) {
                tsPayloadReader = new PesReader(new LatmReader(esInfo.language));
            }
            return tsPayloadReader;
        } else if (i == 21) {
            return new PesReader(new Id3Reader());
        } else {
            if (i == 27) {
                if (!isSet(4)) {
                    tsPayloadReader = new PesReader(new H264Reader(buildSeiReader(esInfo), isSet(1), isSet(8)));
                }
                return tsPayloadReader;
            } else if (i == 36) {
                return new PesReader(new H265Reader(buildSeiReader(esInfo)));
            } else {
                if (i == 89) {
                    return new PesReader(new DvbSubtitleReader(esInfo.dvbSubtitleInfos));
                }
                if (i != 138) {
                    if (i != 129) {
                        if (i != 130) {
                            if (i == 134) {
                                if (!isSet(16)) {
                                    tsPayloadReader = new SectionReader(new SpliceInfoSectionReader());
                                }
                                return tsPayloadReader;
                            } else if (i != 135) {
                                return null;
                            }
                        }
                    }
                    return new PesReader(new Ac3Reader(esInfo.language));
                }
                return new PesReader(new DtsReader(esInfo.language));
            }
        }
    }

    private SeiReader buildSeiReader(EsInfo esInfo) {
        return new SeiReader(getClosedCaptionFormats(esInfo));
    }

    private UserDataReader buildUserDataReader(EsInfo esInfo) {
        return new UserDataReader(getClosedCaptionFormats(esInfo));
    }

    private List<Format> getClosedCaptionFormats(EsInfo esInfo) {
        if (isSet(32)) {
            return this.closedCaptionFormats;
        }
        ParsableByteArray parsableByteArray = new ParsableByteArray(esInfo.descriptorBytes);
        List<Format> list = this.closedCaptionFormats;
        while (parsableByteArray.bytesLeft() > 0) {
            int position = parsableByteArray.getPosition() + parsableByteArray.readUnsignedByte();
            if (parsableByteArray.readUnsignedByte() == 134) {
                list = new ArrayList();
                int readUnsignedByte = parsableByteArray.readUnsignedByte() & 31;
                for (int i = 0; i < readUnsignedByte; i++) {
                    int i2;
                    String str;
                    List buildData;
                    String readString = parsableByteArray.readString(3);
                    int readUnsignedByte2 = parsableByteArray.readUnsignedByte();
                    boolean z = true;
                    Object obj = (readUnsignedByte2 & 128) != 0 ? 1 : null;
                    if (obj != null) {
                        i2 = readUnsignedByte2 & 63;
                        str = MimeTypes.APPLICATION_CEA708;
                    } else {
                        str = MimeTypes.APPLICATION_CEA608;
                        i2 = 1;
                    }
                    byte readUnsignedByte3 = (byte) parsableByteArray.readUnsignedByte();
                    parsableByteArray.skipBytes(1);
                    if (obj != null) {
                        if ((readUnsignedByte3 & 64) == 0) {
                            z = false;
                        }
                        buildData = Cea708InitializationData.buildData(z);
                    } else {
                        buildData = null;
                    }
                    list.add(Format.createTextSampleFormat(null, str, null, -1, 0, readString, i2, null, TimestampAdjuster.DO_NOT_OFFSET, buildData));
                }
            }
            parsableByteArray.setPosition(position);
        }
        return list;
    }

    private boolean isSet(int i) {
        return (i & this.flags) != 0;
    }
}
