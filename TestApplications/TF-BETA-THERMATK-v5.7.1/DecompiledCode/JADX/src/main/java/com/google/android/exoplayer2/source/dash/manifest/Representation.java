package com.google.android.exoplayer2.source.dash.manifest;

import android.net.Uri;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.dash.DashSegmentIndex;
import com.google.android.exoplayer2.source.dash.manifest.SegmentBase.MultiSegmentBase;
import com.google.android.exoplayer2.source.dash.manifest.SegmentBase.SingleSegmentBase;
import java.util.Collections;
import java.util.List;

public abstract class Representation {
    public final String baseUrl;
    public final Format format;
    public final List<Descriptor> inbandEventStreams;
    private final RangedUri initializationUri;
    public final long presentationTimeOffsetUs;
    public final long revisionId;

    public static class MultiSegmentRepresentation extends Representation implements DashSegmentIndex {
        private final MultiSegmentBase segmentBase;

        public String getCacheKey() {
            return null;
        }

        public DashSegmentIndex getIndex() {
            return this;
        }

        public RangedUri getIndexUri() {
            return null;
        }

        public MultiSegmentRepresentation(long j, Format format, String str, MultiSegmentBase multiSegmentBase, List<Descriptor> list) {
            super(j, format, str, multiSegmentBase, list);
            this.segmentBase = multiSegmentBase;
        }

        public RangedUri getSegmentUrl(long j) {
            return this.segmentBase.getSegmentUrl(this, j);
        }

        public long getSegmentNum(long j, long j2) {
            return this.segmentBase.getSegmentNum(j, j2);
        }

        public long getTimeUs(long j) {
            return this.segmentBase.getSegmentTimeUs(j);
        }

        public long getDurationUs(long j, long j2) {
            return this.segmentBase.getSegmentDurationUs(j, j2);
        }

        public long getFirstSegmentNum() {
            return this.segmentBase.getFirstSegmentNum();
        }

        public int getSegmentCount(long j) {
            return this.segmentBase.getSegmentCount(j);
        }

        public boolean isExplicit() {
            return this.segmentBase.isExplicit();
        }
    }

    public static class SingleSegmentRepresentation extends Representation {
        private final String cacheKey;
        public final long contentLength;
        private final RangedUri indexUri;
        private final SingleSegmentIndex segmentIndex;
        public final Uri uri;

        public SingleSegmentRepresentation(long j, Format format, String str, SingleSegmentBase singleSegmentBase, List<Descriptor> list, String str2, long j2) {
            super(j, format, str, singleSegmentBase, list);
            this.uri = Uri.parse(str);
            this.indexUri = singleSegmentBase.getIndex();
            this.cacheKey = str2;
            this.contentLength = j2;
            this.segmentIndex = this.indexUri != null ? null : new SingleSegmentIndex(new RangedUri(null, 0, j2));
        }

        public RangedUri getIndexUri() {
            return this.indexUri;
        }

        public DashSegmentIndex getIndex() {
            return this.segmentIndex;
        }

        public String getCacheKey() {
            return this.cacheKey;
        }
    }

    public abstract String getCacheKey();

    public abstract DashSegmentIndex getIndex();

    public abstract RangedUri getIndexUri();

    public static Representation newInstance(long j, Format format, String str, SegmentBase segmentBase, List<Descriptor> list) {
        return newInstance(j, format, str, segmentBase, list, null);
    }

    public static Representation newInstance(long j, Format format, String str, SegmentBase segmentBase, List<Descriptor> list, String str2) {
        SegmentBase segmentBase2 = segmentBase;
        if (segmentBase2 instanceof SingleSegmentBase) {
            return new SingleSegmentRepresentation(j, format, str, (SingleSegmentBase) segmentBase2, list, str2, -1);
        } else if (segmentBase2 instanceof MultiSegmentBase) {
            return new MultiSegmentRepresentation(j, format, str, (MultiSegmentBase) segmentBase2, list);
        } else {
            throw new IllegalArgumentException("segmentBase must be of type SingleSegmentBase or MultiSegmentBase");
        }
    }

    private Representation(long j, Format format, String str, SegmentBase segmentBase, List<Descriptor> list) {
        List emptyList;
        this.revisionId = j;
        this.format = format;
        this.baseUrl = str;
        if (list == null) {
            emptyList = Collections.emptyList();
        } else {
            emptyList = Collections.unmodifiableList(list);
        }
        this.inbandEventStreams = emptyList;
        this.initializationUri = segmentBase.getInitialization(this);
        this.presentationTimeOffsetUs = segmentBase.getPresentationTimeOffsetUs();
    }

    public RangedUri getInitializationUri() {
        return this.initializationUri;
    }
}
