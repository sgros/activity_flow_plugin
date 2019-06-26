// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.ext.ffmpeg;

import com.google.android.exoplayer2.ExoPlayerLibraryInfo;

public final class FfmpegLibrary
{
    static {
        ExoPlayerLibraryInfo.registerModule("goog.exo.ffmpeg");
    }
    
    private FfmpegLibrary() {
    }
    
    private static native String ffmpegGetVersion();
    
    private static native boolean ffmpegHasDecoder(final String p0);
    
    static String getCodecName(final String s, final int n) {
        int n2 = 0;
        Label_0406: {
            switch (s.hashCode()) {
                case 1556697186: {
                    if (s.equals("audio/true-hd")) {
                        n2 = 7;
                        break Label_0406;
                    }
                    break;
                }
                case 1505942594: {
                    if (s.equals("audio/vnd.dts.hd")) {
                        n2 = 9;
                        break Label_0406;
                    }
                    break;
                }
                case 1504891608: {
                    if (s.equals("audio/opus")) {
                        n2 = 11;
                        break Label_0406;
                    }
                    break;
                }
                case 1504831518: {
                    if (s.equals("audio/mpeg")) {
                        n2 = 1;
                        break Label_0406;
                    }
                    break;
                }
                case 1504619009: {
                    if (s.equals("audio/flac")) {
                        n2 = 14;
                        break Label_0406;
                    }
                    break;
                }
                case 1504578661: {
                    if (s.equals("audio/eac3")) {
                        n2 = 5;
                        break Label_0406;
                    }
                    break;
                }
                case 1504470054: {
                    if (s.equals("audio/alac")) {
                        n2 = 15;
                        break Label_0406;
                    }
                    break;
                }
                case 1503095341: {
                    if (s.equals("audio/3gpp")) {
                        n2 = 12;
                        break Label_0406;
                    }
                    break;
                }
                case 187094639: {
                    if (s.equals("audio/raw")) {
                        n2 = 16;
                        break Label_0406;
                    }
                    break;
                }
                case 187078296: {
                    if (s.equals("audio/ac3")) {
                        n2 = 4;
                        break Label_0406;
                    }
                    break;
                }
                case -53558318: {
                    if (s.equals("audio/mp4a-latm")) {
                        n2 = 0;
                        break Label_0406;
                    }
                    break;
                }
                case -432837259: {
                    if (s.equals("audio/mpeg-L2")) {
                        n2 = 3;
                        break Label_0406;
                    }
                    break;
                }
                case -432837260: {
                    if (s.equals("audio/mpeg-L1")) {
                        n2 = 2;
                        break Label_0406;
                    }
                    break;
                }
                case -1003765268: {
                    if (s.equals("audio/vorbis")) {
                        n2 = 10;
                        break Label_0406;
                    }
                    break;
                }
                case -1095064472: {
                    if (s.equals("audio/vnd.dts")) {
                        n2 = 8;
                        break Label_0406;
                    }
                    break;
                }
                case -1606874997: {
                    if (s.equals("audio/amr-wb")) {
                        n2 = 13;
                        break Label_0406;
                    }
                    break;
                }
                case -2123537834: {
                    if (s.equals("audio/eac3-joc")) {
                        n2 = 6;
                        break Label_0406;
                    }
                    break;
                }
            }
            n2 = -1;
        }
        switch (n2) {
            default: {
                return null;
            }
            case 16: {
                if (n == 268435456) {
                    return "pcm_mulaw";
                }
                if (n == 536870912) {
                    return "pcm_alaw";
                }
                return null;
            }
            case 15: {
                return "alac";
            }
            case 14: {
                return "flac";
            }
            case 13: {
                return "amrwb";
            }
            case 12: {
                return "amrnb";
            }
            case 11: {
                return "opus";
            }
            case 10: {
                return "vorbis";
            }
            case 8:
            case 9: {
                return "dca";
            }
            case 7: {
                return "truehd";
            }
            case 5:
            case 6: {
                return "eac3";
            }
            case 4: {
                return "ac3";
            }
            case 1:
            case 2:
            case 3: {
                return "mp3";
            }
            case 0: {
                return "aac";
            }
        }
    }
    
    public static String getVersion() {
        return ffmpegGetVersion();
    }
    
    public static boolean supportsFormat(String codecName, final int n) {
        codecName = getCodecName(codecName, n);
        return codecName != null && ffmpegHasDecoder(codecName);
    }
}
