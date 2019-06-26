// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.hls.playlist;

import com.google.android.exoplayer2.source.MediaSourceEventListener;
import android.net.Uri;
import java.io.IOException;

public interface HlsPlaylistTracker
{
    void addListener(final PlaylistEventListener p0);
    
    long getInitialStartTimeUs();
    
    HlsMasterPlaylist getMasterPlaylist();
    
    HlsMediaPlaylist getPlaylistSnapshot(final HlsMasterPlaylist.HlsUrl p0, final boolean p1);
    
    boolean isLive();
    
    boolean isSnapshotValid(final HlsMasterPlaylist.HlsUrl p0);
    
    void maybeThrowPlaylistRefreshError(final HlsMasterPlaylist.HlsUrl p0) throws IOException;
    
    void maybeThrowPrimaryPlaylistRefreshError() throws IOException;
    
    void refreshPlaylist(final HlsMasterPlaylist.HlsUrl p0);
    
    void removeListener(final PlaylistEventListener p0);
    
    void start(final Uri p0, final MediaSourceEventListener.EventDispatcher p1, final PrimaryPlaylistListener p2);
    
    void stop();
    
    public interface Factory
    {
    }
    
    public interface PlaylistEventListener
    {
        void onPlaylistChanged();
        
        boolean onPlaylistError(final HlsMasterPlaylist.HlsUrl p0, final long p1);
    }
    
    public static final class PlaylistResetException extends IOException
    {
        public final String url;
        
        public PlaylistResetException(final String url) {
            this.url = url;
        }
    }
    
    public static final class PlaylistStuckException extends IOException
    {
        public final String url;
        
        public PlaylistStuckException(final String url) {
            this.url = url;
        }
    }
    
    public interface PrimaryPlaylistListener
    {
        void onPrimaryPlaylistRefreshed(final HlsMediaPlaylist p0);
    }
}
