// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.hls.playlist;

import java.util.Collections;
import java.util.Map;
import com.google.android.exoplayer2.Format;
import java.util.List;

public final class HlsMasterPlaylist extends HlsPlaylist
{
    public static final HlsMasterPlaylist EMPTY;
    public final List<HlsUrl> audios;
    public final Format muxedAudioFormat;
    public final List<Format> muxedCaptionFormats;
    public final List<HlsUrl> subtitles;
    public final Map<String, String> variableDefinitions;
    public final List<HlsUrl> variants;
    
    static {
        EMPTY = new HlsMasterPlaylist("", Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), null, Collections.emptyList(), false, Collections.emptyMap());
    }
    
    public HlsMasterPlaylist(final String s, final List<String> list, final List<HlsUrl> list2, final List<HlsUrl> list3, final List<HlsUrl> list4, final Format muxedAudioFormat, final List<Format> list5, final boolean b, final Map<String, String> m) {
        super(s, list, b);
        this.variants = Collections.unmodifiableList((List<? extends HlsUrl>)list2);
        this.audios = Collections.unmodifiableList((List<? extends HlsUrl>)list3);
        this.subtitles = Collections.unmodifiableList((List<? extends HlsUrl>)list4);
        this.muxedAudioFormat = muxedAudioFormat;
        List<Format> unmodifiableList;
        if (list5 != null) {
            unmodifiableList = Collections.unmodifiableList((List<? extends Format>)list5);
        }
        else {
            unmodifiableList = null;
        }
        this.muxedCaptionFormats = unmodifiableList;
        this.variableDefinitions = Collections.unmodifiableMap((Map<? extends String, ? extends String>)m);
    }
    
    public static HlsMasterPlaylist createSingleVariantMasterPlaylist(final String s) {
        final List<HlsUrl> singletonList = Collections.singletonList(HlsUrl.createMediaPlaylistHlsUrl(s));
        final List<HlsUrl> emptyList = Collections.emptyList();
        return new HlsMasterPlaylist(null, Collections.emptyList(), singletonList, emptyList, emptyList, null, null, false, Collections.emptyMap());
    }
    
    public static final class HlsUrl
    {
        public final Format format;
        public final String url;
        
        public HlsUrl(final String url, final Format format) {
            this.url = url;
            this.format = format;
        }
        
        public static HlsUrl createMediaPlaylistHlsUrl(final String s) {
            return new HlsUrl(s, Format.createContainerFormat("0", null, "application/x-mpegURL", null, null, -1, 0, null));
        }
    }
}
