// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.hls.playlist;

import com.google.android.exoplayer2.upstream.ParsingLoadable;

public interface HlsPlaylistParserFactory
{
    ParsingLoadable.Parser<HlsPlaylist> createPlaylistParser();
    
    ParsingLoadable.Parser<HlsPlaylist> createPlaylistParser(final HlsMasterPlaylist p0);
}
