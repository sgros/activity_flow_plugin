// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text;

import com.google.android.exoplayer2.text.webvtt.WebvttDecoder;
import com.google.android.exoplayer2.text.ssa.SsaDecoder;
import com.google.android.exoplayer2.text.webvtt.Mp4WebvttDecoder;
import com.google.android.exoplayer2.text.ttml.TtmlDecoder;
import com.google.android.exoplayer2.text.subrip.SubripDecoder;
import com.google.android.exoplayer2.text.tx3g.Tx3gDecoder;
import com.google.android.exoplayer2.text.cea.Cea608Decoder;
import com.google.android.exoplayer2.text.cea.Cea708Decoder;
import com.google.android.exoplayer2.text.dvb.DvbDecoder;
import com.google.android.exoplayer2.text.pgs.PgsDecoder;
import com.google.android.exoplayer2.Format;

public interface SubtitleDecoderFactory
{
    public static final SubtitleDecoderFactory DEFAULT = new SubtitleDecoderFactory() {
        @Override
        public SubtitleDecoder createDecoder(final Format format) {
            final String sampleMimeType = format.sampleMimeType;
            int n = 0;
            Label_0272: {
                switch (sampleMimeType.hashCode()) {
                    case 1693976202: {
                        if (sampleMimeType.equals("application/ttml+xml")) {
                            n = 3;
                            break Label_0272;
                        }
                        break;
                    }
                    case 1668750253: {
                        if (sampleMimeType.equals("application/x-subrip")) {
                            n = 4;
                            break Label_0272;
                        }
                        break;
                    }
                    case 1566016562: {
                        if (sampleMimeType.equals("application/cea-708")) {
                            n = 8;
                            break Label_0272;
                        }
                        break;
                    }
                    case 1566015601: {
                        if (sampleMimeType.equals("application/cea-608")) {
                            n = 6;
                            break Label_0272;
                        }
                        break;
                    }
                    case 930165504: {
                        if (sampleMimeType.equals("application/x-mp4-cea-608")) {
                            n = 7;
                            break Label_0272;
                        }
                        break;
                    }
                    case 822864842: {
                        if (sampleMimeType.equals("text/x-ssa")) {
                            n = 1;
                            break Label_0272;
                        }
                        break;
                    }
                    case 691401887: {
                        if (sampleMimeType.equals("application/x-quicktime-tx3g")) {
                            n = 5;
                            break Label_0272;
                        }
                        break;
                    }
                    case -1004728940: {
                        if (sampleMimeType.equals("text/vtt")) {
                            n = 0;
                            break Label_0272;
                        }
                        break;
                    }
                    case -1026075066: {
                        if (sampleMimeType.equals("application/x-mp4-vtt")) {
                            n = 2;
                            break Label_0272;
                        }
                        break;
                    }
                    case -1248334819: {
                        if (sampleMimeType.equals("application/pgs")) {
                            n = 10;
                            break Label_0272;
                        }
                        break;
                    }
                    case -1351681404: {
                        if (sampleMimeType.equals("application/dvbsubs")) {
                            n = 9;
                            break Label_0272;
                        }
                        break;
                    }
                }
                n = -1;
            }
            switch (n) {
                default: {
                    throw new IllegalArgumentException("Attempted to create decoder for unsupported format");
                }
                case 10: {
                    return new PgsDecoder();
                }
                case 9: {
                    return new DvbDecoder(format.initializationData);
                }
                case 8: {
                    return new Cea708Decoder(format.accessibilityChannel, format.initializationData);
                }
                case 6:
                case 7: {
                    return new Cea608Decoder(format.sampleMimeType, format.accessibilityChannel);
                }
                case 5: {
                    return new Tx3gDecoder(format.initializationData);
                }
                case 4: {
                    return new SubripDecoder();
                }
                case 3: {
                    return new TtmlDecoder();
                }
                case 2: {
                    return new Mp4WebvttDecoder();
                }
                case 1: {
                    return new SsaDecoder(format.initializationData);
                }
                case 0: {
                    return new WebvttDecoder();
                }
            }
        }
        
        @Override
        public boolean supportsFormat(final Format format) {
            final String sampleMimeType = format.sampleMimeType;
            return "text/vtt".equals(sampleMimeType) || "text/x-ssa".equals(sampleMimeType) || "application/ttml+xml".equals(sampleMimeType) || "application/x-mp4-vtt".equals(sampleMimeType) || "application/x-subrip".equals(sampleMimeType) || "application/x-quicktime-tx3g".equals(sampleMimeType) || "application/cea-608".equals(sampleMimeType) || "application/x-mp4-cea-608".equals(sampleMimeType) || "application/cea-708".equals(sampleMimeType) || "application/dvbsubs".equals(sampleMimeType) || "application/pgs".equals(sampleMimeType);
        }
    };
    
    SubtitleDecoder createDecoder(final Format p0);
    
    boolean supportsFormat(final Format p0);
}
