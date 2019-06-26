// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ts;

import android.util.SparseArray;
import java.util.Collections;
import java.util.List;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.util.ParsableByteArray;

public interface TsPayloadReader
{
    void consume(final ParsableByteArray p0, final int p1) throws ParserException;
    
    void init(final TimestampAdjuster p0, final ExtractorOutput p1, final TrackIdGenerator p2);
    
    void seek();
    
    public static final class DvbSubtitleInfo
    {
        public final byte[] initializationData;
        public final String language;
        public final int type;
        
        public DvbSubtitleInfo(final String language, final int type, final byte[] initializationData) {
            this.language = language;
            this.type = type;
            this.initializationData = initializationData;
        }
    }
    
    public static final class EsInfo
    {
        public final byte[] descriptorBytes;
        public final List<DvbSubtitleInfo> dvbSubtitleInfos;
        public final String language;
        public final int streamType;
        
        public EsInfo(final int streamType, final String language, final List<DvbSubtitleInfo> list, final byte[] descriptorBytes) {
            this.streamType = streamType;
            this.language = language;
            List<DvbSubtitleInfo> dvbSubtitleInfos;
            if (list == null) {
                dvbSubtitleInfos = Collections.emptyList();
            }
            else {
                dvbSubtitleInfos = Collections.unmodifiableList((List<? extends DvbSubtitleInfo>)list);
            }
            this.dvbSubtitleInfos = dvbSubtitleInfos;
            this.descriptorBytes = descriptorBytes;
        }
    }
    
    public interface Factory
    {
        SparseArray<TsPayloadReader> createInitialPayloadReaders();
        
        TsPayloadReader createPayloadReader(final int p0, final EsInfo p1);
    }
    
    public static final class TrackIdGenerator
    {
        private final int firstTrackId;
        private String formatId;
        private final String formatIdPrefix;
        private int trackId;
        private final int trackIdIncrement;
        
        public TrackIdGenerator(final int n, final int n2) {
            this(Integer.MIN_VALUE, n, n2);
        }
        
        public TrackIdGenerator(final int i, final int firstTrackId, final int trackIdIncrement) {
            String string;
            if (i != Integer.MIN_VALUE) {
                final StringBuilder sb = new StringBuilder();
                sb.append(i);
                sb.append("/");
                string = sb.toString();
            }
            else {
                string = "";
            }
            this.formatIdPrefix = string;
            this.firstTrackId = firstTrackId;
            this.trackIdIncrement = trackIdIncrement;
            this.trackId = Integer.MIN_VALUE;
        }
        
        private void maybeThrowUninitializedError() {
            if (this.trackId != Integer.MIN_VALUE) {
                return;
            }
            throw new IllegalStateException("generateNewId() must be called before retrieving ids.");
        }
        
        public void generateNewId() {
            final int trackId = this.trackId;
            int firstTrackId;
            if (trackId == Integer.MIN_VALUE) {
                firstTrackId = this.firstTrackId;
            }
            else {
                firstTrackId = trackId + this.trackIdIncrement;
            }
            this.trackId = firstTrackId;
            final StringBuilder sb = new StringBuilder();
            sb.append(this.formatIdPrefix);
            sb.append(this.trackId);
            this.formatId = sb.toString();
        }
        
        public String getFormatId() {
            this.maybeThrowUninitializedError();
            return this.formatId;
        }
        
        public int getTrackId() {
            this.maybeThrowUninitializedError();
            return this.trackId;
        }
    }
}
