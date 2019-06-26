package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.PlaybackParameters;
import java.nio.ByteBuffer;

public interface AudioSink {
   void configure(int var1, int var2, int var3, int var4, int[] var5, int var6, int var7) throws AudioSink.ConfigurationException;

   void disableTunneling();

   void enableTunnelingV21(int var1);

   void flush();

   long getCurrentPositionUs(boolean var1);

   PlaybackParameters getPlaybackParameters();

   boolean handleBuffer(ByteBuffer var1, long var2) throws AudioSink.InitializationException, AudioSink.WriteException;

   void handleDiscontinuity();

   boolean hasPendingData();

   boolean isEnded();

   void pause();

   void play();

   void playToEndOfStream() throws AudioSink.WriteException;

   void reset();

   void setAudioAttributes(AudioAttributes var1);

   void setAuxEffectInfo(AuxEffectInfo var1);

   void setListener(AudioSink.Listener var1);

   PlaybackParameters setPlaybackParameters(PlaybackParameters var1);

   void setVolume(float var1);

   boolean supportsOutput(int var1, int var2);

   public static final class ConfigurationException extends Exception {
      public ConfigurationException(String var1) {
         super(var1);
      }

      public ConfigurationException(Throwable var1) {
         super(var1);
      }
   }

   public static final class InitializationException extends Exception {
      public final int audioTrackState;

      public InitializationException(int var1, int var2, int var3, int var4) {
         StringBuilder var5 = new StringBuilder();
         var5.append("AudioTrack init failed: ");
         var5.append(var1);
         var5.append(", Config(");
         var5.append(var2);
         var5.append(", ");
         var5.append(var3);
         var5.append(", ");
         var5.append(var4);
         var5.append(")");
         super(var5.toString());
         this.audioTrackState = var1;
      }
   }

   public interface Listener {
      void onAudioSessionId(int var1);

      void onPositionDiscontinuity();

      void onUnderrun(int var1, long var2, long var4);
   }

   public static final class WriteException extends Exception {
      public final int errorCode;

      public WriteException(int var1) {
         StringBuilder var2 = new StringBuilder();
         var2.append("AudioTrack write failed: ");
         var2.append(var1);
         super(var2.toString());
         this.errorCode = var1;
      }
   }
}
