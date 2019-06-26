// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.audio;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Context;
import java.util.Arrays;
import android.annotation.TargetApi;

@TargetApi(21)
public final class AudioCapabilities
{
    public static final AudioCapabilities DEFAULT_AUDIO_CAPABILITIES;
    private final int maxChannelCount;
    private final int[] supportedEncodings;
    
    static {
        DEFAULT_AUDIO_CAPABILITIES = new AudioCapabilities(new int[] { 2 }, 8);
    }
    
    public AudioCapabilities(final int[] original, final int maxChannelCount) {
        if (original != null) {
            Arrays.sort(this.supportedEncodings = Arrays.copyOf(original, original.length));
        }
        else {
            this.supportedEncodings = new int[0];
        }
        this.maxChannelCount = maxChannelCount;
    }
    
    public static AudioCapabilities getCapabilities(final Context context) {
        return getCapabilities(context.registerReceiver((BroadcastReceiver)null, new IntentFilter("android.media.action.HDMI_AUDIO_PLUG")));
    }
    
    @SuppressLint({ "InlinedApi" })
    static AudioCapabilities getCapabilities(final Intent intent) {
        if (intent != null && intent.getIntExtra("android.media.extra.AUDIO_PLUG_STATE", 0) != 0) {
            return new AudioCapabilities(intent.getIntArrayExtra("android.media.extra.ENCODINGS"), intent.getIntExtra("android.media.extra.MAX_CHANNEL_COUNT", 8));
        }
        return AudioCapabilities.DEFAULT_AUDIO_CAPABILITIES;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (!(o instanceof AudioCapabilities)) {
            return false;
        }
        final AudioCapabilities audioCapabilities = (AudioCapabilities)o;
        if (!Arrays.equals(this.supportedEncodings, audioCapabilities.supportedEncodings) || this.maxChannelCount != audioCapabilities.maxChannelCount) {
            b = false;
        }
        return b;
    }
    
    public int getMaxChannelCount() {
        return this.maxChannelCount;
    }
    
    @Override
    public int hashCode() {
        return this.maxChannelCount + Arrays.hashCode(this.supportedEncodings) * 31;
    }
    
    public boolean supportsEncoding(final int key) {
        return Arrays.binarySearch(this.supportedEncodings, key) >= 0;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("AudioCapabilities[maxChannelCount=");
        sb.append(this.maxChannelCount);
        sb.append(", supportedEncodings=");
        sb.append(Arrays.toString(this.supportedEncodings));
        sb.append("]");
        return sb.toString();
    }
}
