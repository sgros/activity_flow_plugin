package com.google.android.exoplayer2.audio;

import android.content.Context;
import android.media.AudioFocusRequest;
import android.media.AudioFocusRequest.Builder;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;

public final class AudioFocusManager {
    private AudioAttributes audioAttributes;
    private AudioFocusRequest audioFocusRequest;
    private int audioFocusState;
    private final AudioManager audioManager;
    private int focusGain;
    private final AudioFocusListener focusListener;
    private final PlayerControl playerControl;
    private boolean rebuildAudioFocusRequest;
    private float volumeMultiplier = 1.0f;

    private class AudioFocusListener implements OnAudioFocusChangeListener {
        private AudioFocusListener() {
        }

        public void onAudioFocusChange(int i) {
            StringBuilder stringBuilder;
            if (i != -3) {
                if (i == -2) {
                    AudioFocusManager.this.audioFocusState = 2;
                } else if (i == -1) {
                    AudioFocusManager.this.audioFocusState = -1;
                } else if (i != 1) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Unknown focus change type: ");
                    stringBuilder.append(i);
                    Log.m18w("AudioFocusManager", stringBuilder.toString());
                    return;
                } else {
                    AudioFocusManager.this.audioFocusState = 1;
                }
            } else if (AudioFocusManager.this.willPauseWhenDucked()) {
                AudioFocusManager.this.audioFocusState = 2;
            } else {
                AudioFocusManager.this.audioFocusState = 3;
            }
            i = AudioFocusManager.this.audioFocusState;
            if (i == -1) {
                AudioFocusManager.this.playerControl.executePlayerCommand(-1);
                AudioFocusManager.this.abandonAudioFocus(true);
            } else if (i != 0) {
                if (i == 1) {
                    AudioFocusManager.this.playerControl.executePlayerCommand(1);
                } else if (i == 2) {
                    AudioFocusManager.this.playerControl.executePlayerCommand(0);
                } else if (i != 3) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Unknown audio focus state: ");
                    stringBuilder.append(AudioFocusManager.this.audioFocusState);
                    throw new IllegalStateException(stringBuilder.toString());
                }
            }
            float f = AudioFocusManager.this.audioFocusState == 3 ? 0.2f : 1.0f;
            if (AudioFocusManager.this.volumeMultiplier != f) {
                AudioFocusManager.this.volumeMultiplier = f;
                AudioFocusManager.this.playerControl.setVolumeMultiplier(f);
            }
        }
    }

    public interface PlayerControl {
        void executePlayerCommand(int i);

        void setVolumeMultiplier(float f);
    }

    private int handleIdle(boolean z) {
        return z ? 1 : -1;
    }

    public AudioFocusManager(Context context, PlayerControl playerControl) {
        this.audioManager = (AudioManager) context.getApplicationContext().getSystemService(MimeTypes.BASE_TYPE_AUDIO);
        this.playerControl = playerControl;
        this.focusListener = new AudioFocusListener();
        this.audioFocusState = 0;
    }

    public float getVolumeMultiplier() {
        return this.volumeMultiplier;
    }

    public int setAudioAttributes(AudioAttributes audioAttributes, boolean z, int i) {
        int i2;
        if (!Util.areEqual(this.audioAttributes, audioAttributes)) {
            this.audioAttributes = audioAttributes;
            this.focusGain = convertAudioAttributesToFocusGain(audioAttributes);
            i2 = this.focusGain;
            boolean z2 = i2 == 1 || i2 == 0;
            Assertions.checkArgument(z2, "Automatic handling of audio focus is only available for USAGE_MEDIA and USAGE_GAME.");
            if (z && (i == 2 || i == 3)) {
                return requestAudioFocus();
            }
        }
        if (i == 1) {
            i2 = handleIdle(z);
        } else {
            i2 = handlePrepare(z);
        }
        return i2;
    }

    public int handlePrepare(boolean z) {
        return z ? requestAudioFocus() : -1;
    }

    public int handleSetPlayWhenReady(boolean z, int i) {
        if (z) {
            return i == 1 ? handleIdle(z) : requestAudioFocus();
        }
        abandonAudioFocus();
        return -1;
    }

    public void handleStop() {
        abandonAudioFocus(true);
    }

    private int requestAudioFocus() {
        int i = 1;
        if (this.focusGain == 0) {
            if (this.audioFocusState != 0) {
                abandonAudioFocus(true);
            }
            return 1;
        }
        int requestAudioFocusV26;
        if (this.audioFocusState == 0) {
            if (Util.SDK_INT >= 26) {
                requestAudioFocusV26 = requestAudioFocusV26();
            } else {
                requestAudioFocusV26 = requestAudioFocusDefault();
            }
            this.audioFocusState = requestAudioFocusV26 == 1 ? 1 : 0;
        }
        requestAudioFocusV26 = this.audioFocusState;
        if (requestAudioFocusV26 == 0) {
            return -1;
        }
        if (requestAudioFocusV26 == 2) {
            i = 0;
        }
        return i;
    }

    private void abandonAudioFocus() {
        abandonAudioFocus(false);
    }

    private void abandonAudioFocus(boolean z) {
        if (this.focusGain != 0 || this.audioFocusState != 0) {
            if (this.focusGain != 1 || this.audioFocusState == -1 || z) {
                if (Util.SDK_INT >= 26) {
                    abandonAudioFocusV26();
                } else {
                    abandonAudioFocusDefault();
                }
                this.audioFocusState = 0;
            }
        }
    }

    private int requestAudioFocusDefault() {
        AudioManager audioManager = this.audioManager;
        AudioFocusListener audioFocusListener = this.focusListener;
        AudioAttributes audioAttributes = this.audioAttributes;
        Assertions.checkNotNull(audioAttributes);
        return audioManager.requestAudioFocus(audioFocusListener, Util.getStreamTypeForAudioUsage(audioAttributes.usage), this.focusGain);
    }

    private int requestAudioFocusV26() {
        if (this.audioFocusRequest == null || this.rebuildAudioFocusRequest) {
            AudioFocusRequest audioFocusRequest = this.audioFocusRequest;
            Builder builder = audioFocusRequest == null ? new Builder(this.focusGain) : new Builder(audioFocusRequest);
            boolean willPauseWhenDucked = willPauseWhenDucked();
            AudioAttributes audioAttributes = this.audioAttributes;
            Assertions.checkNotNull(audioAttributes);
            this.audioFocusRequest = builder.setAudioAttributes(audioAttributes.getAudioAttributesV21()).setWillPauseWhenDucked(willPauseWhenDucked).setOnAudioFocusChangeListener(this.focusListener).build();
            this.rebuildAudioFocusRequest = false;
        }
        return this.audioManager.requestAudioFocus(this.audioFocusRequest);
    }

    private void abandonAudioFocusDefault() {
        this.audioManager.abandonAudioFocus(this.focusListener);
    }

    private void abandonAudioFocusV26() {
        AudioFocusRequest audioFocusRequest = this.audioFocusRequest;
        if (audioFocusRequest != null) {
            this.audioManager.abandonAudioFocusRequest(audioFocusRequest);
        }
    }

    private boolean willPauseWhenDucked() {
        AudioAttributes audioAttributes = this.audioAttributes;
        return audioAttributes != null && audioAttributes.contentType == 1;
    }

    private static int convertAudioAttributesToFocusGain(AudioAttributes audioAttributes) {
        if (audioAttributes == null) {
            return 0;
        }
        String str = "AudioFocusManager";
        switch (audioAttributes.usage) {
            case 0:
                Log.m18w(str, "Specify a proper usage in the audio attributes for audio focus handling. Using AUDIOFOCUS_GAIN by default.");
                return 1;
            case 1:
            case 14:
                return 1;
            case 2:
            case 4:
                return 2;
            case 3:
                return 0;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 12:
            case 13:
                break;
            case 11:
                if (audioAttributes.contentType == 1) {
                    return 2;
                }
                break;
            case 16:
                if (Util.SDK_INT >= 19) {
                    return 4;
                }
                return 2;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unidentified audio usage: ");
                stringBuilder.append(audioAttributes.usage);
                Log.m18w(str, stringBuilder.toString());
                return 0;
        }
        return 3;
    }
}
