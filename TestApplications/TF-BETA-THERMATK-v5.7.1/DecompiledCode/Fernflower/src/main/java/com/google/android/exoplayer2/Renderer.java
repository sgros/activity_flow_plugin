package com.google.android.exoplayer2;

import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.util.MediaClock;
import java.io.IOException;

public interface Renderer extends PlayerMessage.Target {
   void disable();

   void enable(RendererConfiguration var1, Format[] var2, SampleStream var3, long var4, boolean var6, long var7) throws ExoPlaybackException;

   RendererCapabilities getCapabilities();

   MediaClock getMediaClock();

   int getState();

   SampleStream getStream();

   int getTrackType();

   boolean hasReadStreamToEnd();

   boolean isCurrentStreamFinal();

   boolean isEnded();

   boolean isReady();

   void maybeThrowStreamError() throws IOException;

   void render(long var1, long var3) throws ExoPlaybackException;

   void replaceStream(Format[] var1, SampleStream var2, long var3) throws ExoPlaybackException;

   void reset();

   void resetPosition(long var1) throws ExoPlaybackException;

   void setCurrentStreamFinal();

   void setIndex(int var1);

   void setOperatingRate(float var1) throws ExoPlaybackException;

   void start() throws ExoPlaybackException;

   void stop() throws ExoPlaybackException;
}
