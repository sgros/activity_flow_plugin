package com.google.android.exoplayer2.source.hls.playlist;

import com.google.android.exoplayer2.Format;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class HlsMasterPlaylist extends HlsPlaylist {
   public static final HlsMasterPlaylist EMPTY = new HlsMasterPlaylist("", Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), (Format)null, Collections.emptyList(), false, Collections.emptyMap());
   public final List audios;
   public final Format muxedAudioFormat;
   public final List muxedCaptionFormats;
   public final List subtitles;
   public final Map variableDefinitions;
   public final List variants;

   public HlsMasterPlaylist(String var1, List var2, List var3, List var4, List var5, Format var6, List var7, boolean var8, Map var9) {
      super(var1, var2, var8);
      this.variants = Collections.unmodifiableList(var3);
      this.audios = Collections.unmodifiableList(var4);
      this.subtitles = Collections.unmodifiableList(var5);
      this.muxedAudioFormat = var6;
      List var10;
      if (var7 != null) {
         var10 = Collections.unmodifiableList(var7);
      } else {
         var10 = null;
      }

      this.muxedCaptionFormats = var10;
      this.variableDefinitions = Collections.unmodifiableMap(var9);
   }

   public static HlsMasterPlaylist createSingleVariantMasterPlaylist(String var0) {
      List var2 = Collections.singletonList(HlsMasterPlaylist.HlsUrl.createMediaPlaylistHlsUrl(var0));
      List var1 = Collections.emptyList();
      return new HlsMasterPlaylist((String)null, Collections.emptyList(), var2, var1, var1, (Format)null, (List)null, false, Collections.emptyMap());
   }

   public static final class HlsUrl {
      public final Format format;
      public final String url;

      public HlsUrl(String var1, Format var2) {
         this.url = var1;
         this.format = var2;
      }

      public static HlsMasterPlaylist.HlsUrl createMediaPlaylistHlsUrl(String var0) {
         return new HlsMasterPlaylist.HlsUrl(var0, Format.createContainerFormat("0", (String)null, "application/x-mpegURL", (String)null, (String)null, -1, 0, (String)null));
      }
   }
}
