// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.audio;

import android.media.AudioFocusRequest$Builder;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import android.media.AudioManager$OnAudioFocusChangeListener;
import com.google.android.exoplayer2.util.Util;
import android.content.Context;
import android.media.AudioManager;
import android.media.AudioFocusRequest;

public final class AudioFocusManager
{
    private AudioAttributes audioAttributes;
    private AudioFocusRequest audioFocusRequest;
    private int audioFocusState;
    private final AudioManager audioManager;
    private int focusGain;
    private final AudioFocusListener focusListener;
    private final PlayerControl playerControl;
    private boolean rebuildAudioFocusRequest;
    private float volumeMultiplier;
    
    public AudioFocusManager(final Context context, final PlayerControl playerControl) {
        this.volumeMultiplier = 1.0f;
        this.audioManager = (AudioManager)context.getApplicationContext().getSystemService("audio");
        this.playerControl = playerControl;
        this.focusListener = new AudioFocusListener();
        this.audioFocusState = 0;
    }
    
    private void abandonAudioFocus() {
        this.abandonAudioFocus(false);
    }
    
    private void abandonAudioFocus(final boolean b) {
        if (this.focusGain == 0 && this.audioFocusState == 0) {
            return;
        }
        if (this.focusGain != 1 || this.audioFocusState == -1 || b) {
            if (Util.SDK_INT >= 26) {
                this.abandonAudioFocusV26();
            }
            else {
                this.abandonAudioFocusDefault();
            }
            this.audioFocusState = 0;
        }
    }
    
    private void abandonAudioFocusDefault() {
        this.audioManager.abandonAudioFocus((AudioManager$OnAudioFocusChangeListener)this.focusListener);
    }
    
    private void abandonAudioFocusV26() {
        final AudioFocusRequest audioFocusRequest = this.audioFocusRequest;
        if (audioFocusRequest != null) {
            this.audioManager.abandonAudioFocusRequest(audioFocusRequest);
        }
    }
    
    private static int convertAudioAttributesToFocusGain(final AudioAttributes audioAttributes) {
        if (audioAttributes == null) {
            return 0;
        }
        switch (audioAttributes.usage) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unidentified audio usage: ");
                sb.append(audioAttributes.usage);
                Log.w("AudioFocusManager", sb.toString());
                return 0;
            }
            case 16: {
                if (Util.SDK_INT >= 19) {
                    return 4;
                }
                return 2;
            }
            case 11: {
                if (audioAttributes.contentType == 1) {
                    return 2;
                }
                return 3;
            }
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 12:
            case 13: {
                return 3;
            }
            case 3: {
                return 0;
            }
            case 2:
            case 4: {
                return 2;
            }
            case 1:
            case 14: {
                return 1;
            }
            case 0: {
                Log.w("AudioFocusManager", "Specify a proper usage in the audio attributes for audio focus handling. Using AUDIOFOCUS_GAIN by default.");
                return 1;
            }
        }
    }
    
    private int handleIdle(final boolean b) {
        int n;
        if (b) {
            n = 1;
        }
        else {
            n = -1;
        }
        return n;
    }
    
    private int requestAudioFocus() {
        final int focusGain = this.focusGain;
        final int n = 1;
        if (focusGain == 0) {
            if (this.audioFocusState != 0) {
                this.abandonAudioFocus(true);
            }
            return 1;
        }
        if (this.audioFocusState == 0) {
            int n2;
            if (Util.SDK_INT >= 26) {
                n2 = this.requestAudioFocusV26();
            }
            else {
                n2 = this.requestAudioFocusDefault();
            }
            this.audioFocusState = ((n2 == 1) ? 1 : 0);
        }
        final int audioFocusState = this.audioFocusState;
        if (audioFocusState == 0) {
            return -1;
        }
        int n3 = n;
        if (audioFocusState == 2) {
            n3 = 0;
        }
        return n3;
    }
    
    private int requestAudioFocusDefault() {
        final AudioManager audioManager = this.audioManager;
        final AudioFocusListener focusListener = this.focusListener;
        final AudioAttributes audioAttributes = this.audioAttributes;
        Assertions.checkNotNull(audioAttributes);
        return audioManager.requestAudioFocus((AudioManager$OnAudioFocusChangeListener)focusListener, Util.getStreamTypeForAudioUsage(audioAttributes.usage), this.focusGain);
    }
    
    private int requestAudioFocusV26() {
        if (this.audioFocusRequest == null || this.rebuildAudioFocusRequest) {
            final AudioFocusRequest audioFocusRequest = this.audioFocusRequest;
            AudioFocusRequest$Builder audioFocusRequest$Builder;
            if (audioFocusRequest == null) {
                audioFocusRequest$Builder = new AudioFocusRequest$Builder(this.focusGain);
            }
            else {
                audioFocusRequest$Builder = new AudioFocusRequest$Builder(audioFocusRequest);
            }
            final boolean willPauseWhenDucked = this.willPauseWhenDucked();
            final AudioAttributes audioAttributes = this.audioAttributes;
            Assertions.checkNotNull(audioAttributes);
            this.audioFocusRequest = audioFocusRequest$Builder.setAudioAttributes(audioAttributes.getAudioAttributesV21()).setWillPauseWhenDucked(willPauseWhenDucked).setOnAudioFocusChangeListener((AudioManager$OnAudioFocusChangeListener)this.focusListener).build();
            this.rebuildAudioFocusRequest = false;
        }
        return this.audioManager.requestAudioFocus(this.audioFocusRequest);
    }
    
    private boolean willPauseWhenDucked() {
        final AudioAttributes audioAttributes = this.audioAttributes;
        boolean b = true;
        if (audioAttributes == null || audioAttributes.contentType != 1) {
            b = false;
        }
        return b;
    }
    
    public float getVolumeMultiplier() {
        return this.volumeMultiplier;
    }
    
    public int handlePrepare(final boolean b) {
        int requestAudioFocus;
        if (b) {
            requestAudioFocus = this.requestAudioFocus();
        }
        else {
            requestAudioFocus = -1;
        }
        return requestAudioFocus;
    }
    
    public int handleSetPlayWhenReady(final boolean b, int n) {
        if (!b) {
            this.abandonAudioFocus();
            return -1;
        }
        if (n == 1) {
            n = this.handleIdle(b);
        }
        else {
            n = this.requestAudioFocus();
        }
        return n;
    }
    
    public void handleStop() {
        this.abandonAudioFocus(true);
    }
    
    public int setAudioAttributes(final AudioAttributes audioAttributes, final boolean b, int n) {
        if (!Util.areEqual(this.audioAttributes, audioAttributes)) {
            this.audioAttributes = audioAttributes;
            this.focusGain = convertAudioAttributesToFocusGain(audioAttributes);
            final int focusGain = this.focusGain;
            Assertions.checkArgument(focusGain == 1 || focusGain == 0, "Automatic handling of audio focus is only available for USAGE_MEDIA and USAGE_GAME.");
            if (b && (n == 2 || n == 3)) {
                return this.requestAudioFocus();
            }
        }
        if (n == 1) {
            n = this.handleIdle(b);
        }
        else {
            n = this.handlePrepare(b);
        }
        return n;
    }
    
    private class AudioFocusListener implements AudioManager$OnAudioFocusChangeListener
    {
        public void onAudioFocusChange(int access$100) {
            if (access$100 != -3) {
                if (access$100 != -2) {
                    if (access$100 != -1) {
                        if (access$100 != 1) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Unknown focus change type: ");
                            sb.append(access$100);
                            Log.w("AudioFocusManager", sb.toString());
                            return;
                        }
                        AudioFocusManager.this.audioFocusState = 1;
                    }
                    else {
                        AudioFocusManager.this.audioFocusState = -1;
                    }
                }
                else {
                    AudioFocusManager.this.audioFocusState = 2;
                }
            }
            else if (AudioFocusManager.this.willPauseWhenDucked()) {
                AudioFocusManager.this.audioFocusState = 2;
            }
            else {
                AudioFocusManager.this.audioFocusState = 3;
            }
            access$100 = AudioFocusManager.this.audioFocusState;
            if (access$100 != -1) {
                if (access$100 != 0) {
                    if (access$100 != 1) {
                        if (access$100 != 2) {
                            if (access$100 != 3) {
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("Unknown audio focus state: ");
                                sb2.append(AudioFocusManager.this.audioFocusState);
                                throw new IllegalStateException(sb2.toString());
                            }
                        }
                        else {
                            AudioFocusManager.this.playerControl.executePlayerCommand(0);
                        }
                    }
                    else {
                        AudioFocusManager.this.playerControl.executePlayerCommand(1);
                    }
                }
            }
            else {
                AudioFocusManager.this.playerControl.executePlayerCommand(-1);
                AudioFocusManager.this.abandonAudioFocus(true);
            }
            float volumeMultiplier;
            if (AudioFocusManager.this.audioFocusState == 3) {
                volumeMultiplier = 0.2f;
            }
            else {
                volumeMultiplier = 1.0f;
            }
            if (AudioFocusManager.this.volumeMultiplier != volumeMultiplier) {
                AudioFocusManager.this.volumeMultiplier = volumeMultiplier;
                AudioFocusManager.this.playerControl.setVolumeMultiplier(volumeMultiplier);
            }
        }
    }
    
    public interface PlayerControl
    {
        void executePlayerCommand(final int p0);
        
        void setVolumeMultiplier(final float p0);
    }
}
