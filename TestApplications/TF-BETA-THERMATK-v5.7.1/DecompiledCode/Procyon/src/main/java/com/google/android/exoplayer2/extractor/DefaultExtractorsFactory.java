// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.extractor.amr.AmrExtractor;
import com.google.android.exoplayer2.extractor.wav.WavExtractor;
import com.google.android.exoplayer2.extractor.ts.PsExtractor;
import com.google.android.exoplayer2.extractor.flv.FlvExtractor;
import com.google.android.exoplayer2.extractor.ts.TsExtractor;
import com.google.android.exoplayer2.extractor.ts.Ac3Extractor;
import com.google.android.exoplayer2.extractor.ts.AdtsExtractor;
import com.google.android.exoplayer2.extractor.mp3.Mp3Extractor;
import com.google.android.exoplayer2.extractor.ogg.OggExtractor;
import com.google.android.exoplayer2.extractor.mp4.Mp4Extractor;
import com.google.android.exoplayer2.extractor.mp4.FragmentedMp4Extractor;
import com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor;
import java.lang.reflect.Constructor;

public final class DefaultExtractorsFactory implements ExtractorsFactory
{
    private static final Constructor<? extends Extractor> FLAC_EXTRACTOR_CONSTRUCTOR;
    private int adtsFlags;
    private int amrFlags;
    private boolean constantBitrateSeekingEnabled;
    private int fragmentedMp4Flags;
    private int matroskaFlags;
    private int mp3Flags;
    private int mp4Flags;
    private int tsFlags;
    private int tsMode;
    
    static {
        Constructor<? extends Extractor> constructor;
        try {
            constructor = Class.forName("com.google.android.exoplayer2.ext.flac.FlacExtractor").asSubclass(Extractor.class).getConstructor((Class<?>[])new Class[0]);
        }
        catch (Exception cause) {
            throw new RuntimeException("Error instantiating FLAC extension", cause);
        }
        catch (ClassNotFoundException ex) {
            constructor = null;
        }
        FLAC_EXTRACTOR_CONSTRUCTOR = constructor;
    }
    
    public DefaultExtractorsFactory() {
        this.tsMode = 1;
    }
    
    @Override
    public Extractor[] createExtractors() {
        synchronized (this) {
            int n;
            if (DefaultExtractorsFactory.FLAC_EXTRACTOR_CONSTRUCTOR == null) {
                n = 12;
            }
            else {
                n = 13;
            }
            final Extractor[] array = new Extractor[n];
            array[0] = new MatroskaExtractor(this.matroskaFlags);
            final FragmentedMp4Extractor fragmentedMp4Extractor = new FragmentedMp4Extractor(this.fragmentedMp4Flags);
            final int n2 = 1;
            array[1] = fragmentedMp4Extractor;
            array[2] = new Mp4Extractor(this.mp4Flags);
            array[3] = new OggExtractor();
            array[4] = new Mp3Extractor(this.mp3Flags | (this.constantBitrateSeekingEnabled ? 1 : 0));
            array[5] = new AdtsExtractor(0L, this.adtsFlags | (this.constantBitrateSeekingEnabled ? 1 : 0));
            array[6] = new Ac3Extractor();
            array[7] = new TsExtractor(this.tsMode, this.tsFlags);
            array[8] = new FlvExtractor();
            array[9] = new PsExtractor();
            array[10] = new WavExtractor();
            final int amrFlags = this.amrFlags;
            int n3;
            if (this.constantBitrateSeekingEnabled) {
                n3 = n2;
            }
            else {
                n3 = 0;
            }
            array[11] = new AmrExtractor(n3 | amrFlags);
            if (DefaultExtractorsFactory.FLAC_EXTRACTOR_CONSTRUCTOR != null) {
                try {
                    array[12] = (Extractor)DefaultExtractorsFactory.FLAC_EXTRACTOR_CONSTRUCTOR.newInstance(new Object[0]);
                }
                catch (Exception cause) {
                    throw new IllegalStateException("Unexpected error creating FLAC extractor", cause);
                }
            }
            return array;
        }
    }
}
