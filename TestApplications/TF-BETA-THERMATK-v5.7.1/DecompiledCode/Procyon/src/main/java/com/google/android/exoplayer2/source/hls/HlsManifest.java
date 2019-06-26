// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.hls;

import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;

public final class HlsManifest
{
    public final HlsMasterPlaylist masterPlaylist;
    public final HlsMediaPlaylist mediaPlaylist;
    
    HlsManifest(final HlsMasterPlaylist masterPlaylist, final HlsMediaPlaylist mediaPlaylist) {
        this.masterPlaylist = masterPlaylist;
        this.mediaPlaylist = mediaPlaylist;
    }
}
