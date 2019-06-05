// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.media.session;

import android.media.AudioAttributes;
import android.media.session.MediaSession$QueueItem;
import android.media.session.PlaybackState;
import android.media.MediaMetadata;
import android.media.session.MediaController$PlaybackInfo;
import android.media.session.MediaController$Callback;
import java.util.List;
import android.os.Bundle;

class MediaControllerCompatApi21
{
    public static Object createCallback(final Callback callback) {
        return new CallbackProxy(callback);
    }
    
    public interface Callback
    {
        void onAudioInfoChanged(final int p0, final int p1, final int p2, final int p3, final int p4);
        
        void onExtrasChanged(final Bundle p0);
        
        void onMetadataChanged(final Object p0);
        
        void onPlaybackStateChanged(final Object p0);
        
        void onQueueChanged(final List<?> p0);
        
        void onQueueTitleChanged(final CharSequence p0);
        
        void onSessionDestroyed();
        
        void onSessionEvent(final String p0, final Bundle p1);
    }
    
    static class CallbackProxy<T extends Callback> extends MediaController$Callback
    {
        protected final T mCallback;
        
        public CallbackProxy(final T mCallback) {
            this.mCallback = mCallback;
        }
        
        public void onAudioInfoChanged(final MediaController$PlaybackInfo mediaController$PlaybackInfo) {
            ((Callback)this.mCallback).onAudioInfoChanged(mediaController$PlaybackInfo.getPlaybackType(), PlaybackInfo.getLegacyAudioStream(mediaController$PlaybackInfo), mediaController$PlaybackInfo.getVolumeControl(), mediaController$PlaybackInfo.getMaxVolume(), mediaController$PlaybackInfo.getCurrentVolume());
        }
        
        public void onExtrasChanged(final Bundle bundle) {
            MediaSessionCompat.ensureClassLoader(bundle);
            ((Callback)this.mCallback).onExtrasChanged(bundle);
        }
        
        public void onMetadataChanged(final MediaMetadata mediaMetadata) {
            ((Callback)this.mCallback).onMetadataChanged(mediaMetadata);
        }
        
        public void onPlaybackStateChanged(final PlaybackState playbackState) {
            ((Callback)this.mCallback).onPlaybackStateChanged(playbackState);
        }
        
        public void onQueueChanged(final List<MediaSession$QueueItem> list) {
            ((Callback)this.mCallback).onQueueChanged(list);
        }
        
        public void onQueueTitleChanged(final CharSequence charSequence) {
            ((Callback)this.mCallback).onQueueTitleChanged(charSequence);
        }
        
        public void onSessionDestroyed() {
            ((Callback)this.mCallback).onSessionDestroyed();
        }
        
        public void onSessionEvent(final String s, final Bundle bundle) {
            MediaSessionCompat.ensureClassLoader(bundle);
            ((Callback)this.mCallback).onSessionEvent(s, bundle);
        }
    }
    
    public static class PlaybackInfo
    {
        public static AudioAttributes getAudioAttributes(final Object o) {
            return ((MediaController$PlaybackInfo)o).getAudioAttributes();
        }
        
        public static int getLegacyAudioStream(final Object o) {
            return toLegacyStreamType(getAudioAttributes(o));
        }
        
        private static int toLegacyStreamType(final AudioAttributes audioAttributes) {
            if ((audioAttributes.getFlags() & 0x1) == 0x1) {
                return 7;
            }
            if ((audioAttributes.getFlags() & 0x4) == 0x4) {
                return 6;
            }
            switch (audioAttributes.getUsage()) {
                default: {
                    return 3;
                }
                case 13: {
                    return 1;
                }
                case 6: {
                    return 2;
                }
                case 5:
                case 7:
                case 8:
                case 9:
                case 10: {
                    return 5;
                }
                case 4: {
                    return 4;
                }
                case 3: {
                    return 8;
                }
                case 2: {
                    return 0;
                }
                case 1:
                case 11:
                case 12:
                case 14: {
                    return 3;
                }
            }
        }
    }
}
