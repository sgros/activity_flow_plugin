package com.google.android.exoplayer2.source.hls.playlist;

import com.google.android.exoplayer2.upstream.ParsingLoadable;

public interface HlsPlaylistParserFactory {
   ParsingLoadable.Parser createPlaylistParser();

   ParsingLoadable.Parser createPlaylistParser(HlsMasterPlaylist var1);
}
