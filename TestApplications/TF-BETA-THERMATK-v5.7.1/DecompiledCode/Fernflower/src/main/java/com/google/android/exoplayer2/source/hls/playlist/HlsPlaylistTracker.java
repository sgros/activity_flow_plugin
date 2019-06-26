package com.google.android.exoplayer2.source.hls.playlist;

import android.net.Uri;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import java.io.IOException;

public interface HlsPlaylistTracker {
   void addListener(HlsPlaylistTracker.PlaylistEventListener var1);

   long getInitialStartTimeUs();

   HlsMasterPlaylist getMasterPlaylist();

   HlsMediaPlaylist getPlaylistSnapshot(HlsMasterPlaylist.HlsUrl var1, boolean var2);

   boolean isLive();

   boolean isSnapshotValid(HlsMasterPlaylist.HlsUrl var1);

   void maybeThrowPlaylistRefreshError(HlsMasterPlaylist.HlsUrl var1) throws IOException;

   void maybeThrowPrimaryPlaylistRefreshError() throws IOException;

   void refreshPlaylist(HlsMasterPlaylist.HlsUrl var1);

   void removeListener(HlsPlaylistTracker.PlaylistEventListener var1);

   void start(Uri var1, MediaSourceEventListener.EventDispatcher var2, HlsPlaylistTracker.PrimaryPlaylistListener var3);

   void stop();

   public interface Factory {
   }

   public interface PlaylistEventListener {
      void onPlaylistChanged();

      boolean onPlaylistError(HlsMasterPlaylist.HlsUrl var1, long var2);
   }

   public static final class PlaylistResetException extends IOException {
      public final String url;

      public PlaylistResetException(String var1) {
         this.url = var1;
      }
   }

   public static final class PlaylistStuckException extends IOException {
      public final String url;

      public PlaylistStuckException(String var1) {
         this.url = var1;
      }
   }

   public interface PrimaryPlaylistListener {
      void onPrimaryPlaylistRefreshed(HlsMediaPlaylist var1);
   }
}
