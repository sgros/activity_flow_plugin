package com.google.android.exoplayer2.audio;

import android.content.Context;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.AudioFocusRequest.Builder;
import android.media.AudioManager.OnAudioFocusChangeListener;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;

public final class AudioFocusManager {
   private AudioAttributes audioAttributes;
   private AudioFocusRequest audioFocusRequest;
   private int audioFocusState;
   private final AudioManager audioManager;
   private int focusGain;
   private final AudioFocusManager.AudioFocusListener focusListener;
   private final AudioFocusManager.PlayerControl playerControl;
   private boolean rebuildAudioFocusRequest;
   private float volumeMultiplier = 1.0F;

   public AudioFocusManager(Context var1, AudioFocusManager.PlayerControl var2) {
      this.audioManager = (AudioManager)var1.getApplicationContext().getSystemService("audio");
      this.playerControl = var2;
      this.focusListener = new AudioFocusManager.AudioFocusListener();
      this.audioFocusState = 0;
   }

   private void abandonAudioFocus() {
      this.abandonAudioFocus(false);
   }

   private void abandonAudioFocus(boolean var1) {
      if (this.focusGain != 0 || this.audioFocusState != 0) {
         if (this.focusGain != 1 || this.audioFocusState == -1 || var1) {
            if (Util.SDK_INT >= 26) {
               this.abandonAudioFocusV26();
            } else {
               this.abandonAudioFocusDefault();
            }

            this.audioFocusState = 0;
         }

      }
   }

   private void abandonAudioFocusDefault() {
      this.audioManager.abandonAudioFocus(this.focusListener);
   }

   private void abandonAudioFocusV26() {
      AudioFocusRequest var1 = this.audioFocusRequest;
      if (var1 != null) {
         this.audioManager.abandonAudioFocusRequest(var1);
      }

   }

   private static int convertAudioAttributesToFocusGain(AudioAttributes var0) {
      if (var0 == null) {
         return 0;
      } else {
         switch(var0.usage) {
         case 0:
            Log.w("AudioFocusManager", "Specify a proper usage in the audio attributes for audio focus handling. Using AUDIOFOCUS_GAIN by default.");
            return 1;
         case 1:
         case 14:
            return 1;
         case 2:
         case 4:
            return 2;
         case 3:
            return 0;
         case 11:
            if (var0.contentType == 1) {
               return 2;
            }
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 12:
         case 13:
            return 3;
         case 15:
         default:
            StringBuilder var1 = new StringBuilder();
            var1.append("Unidentified audio usage: ");
            var1.append(var0.usage);
            Log.w("AudioFocusManager", var1.toString());
            return 0;
         case 16:
            return Util.SDK_INT >= 19 ? 4 : 2;
         }
      }
   }

   private int handleIdle(boolean var1) {
      byte var2;
      if (var1) {
         var2 = 1;
      } else {
         var2 = -1;
      }

      return var2;
   }

   private int requestAudioFocus() {
      int var1 = this.focusGain;
      byte var2 = 1;
      if (var1 == 0) {
         if (this.audioFocusState != 0) {
            this.abandonAudioFocus(true);
         }

         return 1;
      } else {
         byte var4;
         if (this.audioFocusState == 0) {
            if (Util.SDK_INT >= 26) {
               var1 = this.requestAudioFocusV26();
            } else {
               var1 = this.requestAudioFocusDefault();
            }

            if (var1 == 1) {
               var4 = 1;
            } else {
               var4 = 0;
            }

            this.audioFocusState = var4;
         }

         int var3 = this.audioFocusState;
         if (var3 == 0) {
            return -1;
         } else {
            var4 = var2;
            if (var3 == 2) {
               var4 = 0;
            }

            return var4;
         }
      }
   }

   private int requestAudioFocusDefault() {
      AudioManager var1 = this.audioManager;
      AudioFocusManager.AudioFocusListener var2 = this.focusListener;
      AudioAttributes var3 = this.audioAttributes;
      Assertions.checkNotNull(var3);
      return var1.requestAudioFocus(var2, Util.getStreamTypeForAudioUsage(((AudioAttributes)var3).usage), this.focusGain);
   }

   private int requestAudioFocusV26() {
      if (this.audioFocusRequest == null || this.rebuildAudioFocusRequest) {
         AudioFocusRequest var1 = this.audioFocusRequest;
         Builder var4;
         if (var1 == null) {
            var4 = new Builder(this.focusGain);
         } else {
            var4 = new Builder(var1);
         }

         boolean var2 = this.willPauseWhenDucked();
         AudioAttributes var3 = this.audioAttributes;
         Assertions.checkNotNull(var3);
         this.audioFocusRequest = var4.setAudioAttributes(((AudioAttributes)var3).getAudioAttributesV21()).setWillPauseWhenDucked(var2).setOnAudioFocusChangeListener(this.focusListener).build();
         this.rebuildAudioFocusRequest = false;
      }

      return this.audioManager.requestAudioFocus(this.audioFocusRequest);
   }

   private boolean willPauseWhenDucked() {
      AudioAttributes var1 = this.audioAttributes;
      boolean var2 = true;
      if (var1 == null || var1.contentType != 1) {
         var2 = false;
      }

      return var2;
   }

   public float getVolumeMultiplier() {
      return this.volumeMultiplier;
   }

   public int handlePrepare(boolean var1) {
      int var2;
      if (var1) {
         var2 = this.requestAudioFocus();
      } else {
         var2 = -1;
      }

      return var2;
   }

   public int handleSetPlayWhenReady(boolean var1, int var2) {
      if (!var1) {
         this.abandonAudioFocus();
         return -1;
      } else {
         if (var2 == 1) {
            var2 = this.handleIdle(var1);
         } else {
            var2 = this.requestAudioFocus();
         }

         return var2;
      }
   }

   public void handleStop() {
      this.abandonAudioFocus(true);
   }

   public int setAudioAttributes(AudioAttributes var1, boolean var2, int var3) {
      if (!Util.areEqual(this.audioAttributes, var1)) {
         this.audioAttributes = var1;
         this.focusGain = convertAudioAttributesToFocusGain(var1);
         int var4 = this.focusGain;
         boolean var5;
         if (var4 != 1 && var4 != 0) {
            var5 = false;
         } else {
            var5 = true;
         }

         Assertions.checkArgument(var5, "Automatic handling of audio focus is only available for USAGE_MEDIA and USAGE_GAME.");
         if (var2 && (var3 == 2 || var3 == 3)) {
            return this.requestAudioFocus();
         }
      }

      if (var3 == 1) {
         var3 = this.handleIdle(var2);
      } else {
         var3 = this.handlePrepare(var2);
      }

      return var3;
   }

   private class AudioFocusListener implements OnAudioFocusChangeListener {
      private AudioFocusListener() {
      }

      // $FF: synthetic method
      AudioFocusListener(Object var2) {
         this();
      }

      public void onAudioFocusChange(int var1) {
         StringBuilder var2;
         if (var1 != -3) {
            if (var1 != -2) {
               if (var1 != -1) {
                  if (var1 != 1) {
                     var2 = new StringBuilder();
                     var2.append("Unknown focus change type: ");
                     var2.append(var1);
                     Log.w("AudioFocusManager", var2.toString());
                     return;
                  }

                  AudioFocusManager.this.audioFocusState = 1;
               } else {
                  AudioFocusManager.this.audioFocusState = -1;
               }
            } else {
               AudioFocusManager.this.audioFocusState = 2;
            }
         } else if (AudioFocusManager.this.willPauseWhenDucked()) {
            AudioFocusManager.this.audioFocusState = 2;
         } else {
            AudioFocusManager.this.audioFocusState = 3;
         }

         var1 = AudioFocusManager.this.audioFocusState;
         if (var1 != -1) {
            if (var1 != 0) {
               if (var1 != 1) {
                  if (var1 != 2) {
                     if (var1 != 3) {
                        var2 = new StringBuilder();
                        var2.append("Unknown audio focus state: ");
                        var2.append(AudioFocusManager.this.audioFocusState);
                        throw new IllegalStateException(var2.toString());
                     }
                  } else {
                     AudioFocusManager.this.playerControl.executePlayerCommand(0);
                  }
               } else {
                  AudioFocusManager.this.playerControl.executePlayerCommand(1);
               }
            }
         } else {
            AudioFocusManager.this.playerControl.executePlayerCommand(-1);
            AudioFocusManager.this.abandonAudioFocus(true);
         }

         float var3;
         if (AudioFocusManager.this.audioFocusState == 3) {
            var3 = 0.2F;
         } else {
            var3 = 1.0F;
         }

         if (AudioFocusManager.this.volumeMultiplier != var3) {
            AudioFocusManager.this.volumeMultiplier = var3;
            AudioFocusManager.this.playerControl.setVolumeMultiplier(var3);
         }

      }
   }

   public interface PlayerControl {
      void executePlayerCommand(int var1);

      void setVolumeMultiplier(float var1);
   }
}
