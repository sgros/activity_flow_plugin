package com.google.android.exoplayer2;

public interface RendererCapabilities {
   int getTrackType();

   int supportsFormat(Format var1) throws ExoPlaybackException;

   int supportsMixedMimeTypeAdaptation() throws ExoPlaybackException;
}
