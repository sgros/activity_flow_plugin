// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.dash.manifest;

import android.net.Uri;
import com.google.android.exoplayer2.source.dash.DashSegmentIndex;
import java.util.Collections;
import java.util.List;
import com.google.android.exoplayer2.Format;

public abstract class Representation
{
    public final String baseUrl;
    public final Format format;
    public final List<Descriptor> inbandEventStreams;
    private final RangedUri initializationUri;
    public final long presentationTimeOffsetUs;
    public final long revisionId;
    
    private Representation(final long revisionId, final Format format, final String baseUrl, final SegmentBase segmentBase, final List<Descriptor> list) {
        this.revisionId = revisionId;
        this.format = format;
        this.baseUrl = baseUrl;
        List<Descriptor> inbandEventStreams;
        if (list == null) {
            inbandEventStreams = Collections.emptyList();
        }
        else {
            inbandEventStreams = Collections.unmodifiableList((List<? extends Descriptor>)list);
        }
        this.inbandEventStreams = inbandEventStreams;
        this.initializationUri = segmentBase.getInitialization(this);
        this.presentationTimeOffsetUs = segmentBase.getPresentationTimeOffsetUs();
    }
    
    public static Representation newInstance(final long n, final Format format, final String s, final SegmentBase segmentBase, final List<Descriptor> list) {
        return newInstance(n, format, s, segmentBase, list, null);
    }
    
    public static Representation newInstance(final long n, final Format format, final String s, final SegmentBase segmentBase, final List<Descriptor> list, final String s2) {
        if (segmentBase instanceof SegmentBase.SingleSegmentBase) {
            return new SingleSegmentRepresentation(n, format, s, (SegmentBase.SingleSegmentBase)segmentBase, list, s2, -1L);
        }
        if (segmentBase instanceof SegmentBase.MultiSegmentBase) {
            return new MultiSegmentRepresentation(n, format, s, (SegmentBase.MultiSegmentBase)segmentBase, list);
        }
        throw new IllegalArgumentException("segmentBase must be of type SingleSegmentBase or MultiSegmentBase");
    }
    
    public abstract String getCacheKey();
    
    public abstract DashSegmentIndex getIndex();
    
    public abstract RangedUri getIndexUri();
    
    public RangedUri getInitializationUri() {
        return this.initializationUri;
    }
    
    public static class MultiSegmentRepresentation extends Representation implements DashSegmentIndex
    {
        private final SegmentBase.MultiSegmentBase segmentBase;
        
        public MultiSegmentRepresentation(final long n, final Format format, final String s, final SegmentBase.MultiSegmentBase segmentBase, final List<Descriptor> list) {
            super(n, format, s, segmentBase, list, null);
            this.segmentBase = segmentBase;
        }
        
        @Override
        public String getCacheKey() {
            return null;
        }
        
        @Override
        public long getDurationUs(final long n, final long n2) {
            return this.segmentBase.getSegmentDurationUs(n, n2);
        }
        
        @Override
        public long getFirstSegmentNum() {
            return this.segmentBase.getFirstSegmentNum();
        }
        
        @Override
        public DashSegmentIndex getIndex() {
            return this;
        }
        
        @Override
        public RangedUri getIndexUri() {
            return null;
        }
        
        @Override
        public int getSegmentCount(final long n) {
            return this.segmentBase.getSegmentCount(n);
        }
        
        @Override
        public long getSegmentNum(final long n, final long n2) {
            return this.segmentBase.getSegmentNum(n, n2);
        }
        
        @Override
        public RangedUri getSegmentUrl(final long n) {
            return this.segmentBase.getSegmentUrl(this, n);
        }
        
        @Override
        public long getTimeUs(final long n) {
            return this.segmentBase.getSegmentTimeUs(n);
        }
        
        @Override
        public boolean isExplicit() {
            return this.segmentBase.isExplicit();
        }
    }
    
    public static class SingleSegmentRepresentation extends Representation
    {
        private final String cacheKey;
        public final long contentLength;
        private final RangedUri indexUri;
        private final SingleSegmentIndex segmentIndex;
        public final Uri uri;
        
        public SingleSegmentRepresentation(final long n, final Format format, final String s, final SegmentBase.SingleSegmentBase singleSegmentBase, final List<Descriptor> list, final String cacheKey, final long contentLength) {
            super(n, format, s, singleSegmentBase, list, null);
            this.uri = Uri.parse(s);
            this.indexUri = singleSegmentBase.getIndex();
            this.cacheKey = cacheKey;
            this.contentLength = contentLength;
            SingleSegmentIndex segmentIndex;
            if (this.indexUri != null) {
                segmentIndex = null;
            }
            else {
                segmentIndex = new SingleSegmentIndex(new RangedUri(null, 0L, contentLength));
            }
            this.segmentIndex = segmentIndex;
        }
        
        @Override
        public String getCacheKey() {
            return this.cacheKey;
        }
        
        @Override
        public DashSegmentIndex getIndex() {
            return this.segmentIndex;
        }
        
        @Override
        public RangedUri getIndexUri() {
            return this.indexUri;
        }
    }
}
