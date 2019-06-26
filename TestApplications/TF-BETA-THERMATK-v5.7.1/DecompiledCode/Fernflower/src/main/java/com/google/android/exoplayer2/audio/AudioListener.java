package com.google.android.exoplayer2.audio;

public interface AudioListener {
   void onAudioAttributesChanged(AudioAttributes var1);

   void onAudioSessionId(int var1);

   void onVolumeChanged(float var1);
}
