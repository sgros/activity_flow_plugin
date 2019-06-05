// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.media.session;

import android.os.Bundle;
import android.media.session.PlaybackState$CustomAction;
import java.util.List;
import android.media.session.PlaybackState;

class PlaybackStateCompatApi21
{
    public static long getActions(final Object o) {
        return ((PlaybackState)o).getActions();
    }
    
    public static long getActiveQueueItemId(final Object o) {
        return ((PlaybackState)o).getActiveQueueItemId();
    }
    
    public static long getBufferedPosition(final Object o) {
        return ((PlaybackState)o).getBufferedPosition();
    }
    
    public static List<Object> getCustomActions(final Object o) {
        return (List<Object>)((PlaybackState)o).getCustomActions();
    }
    
    public static CharSequence getErrorMessage(final Object o) {
        return ((PlaybackState)o).getErrorMessage();
    }
    
    public static long getLastPositionUpdateTime(final Object o) {
        return ((PlaybackState)o).getLastPositionUpdateTime();
    }
    
    public static float getPlaybackSpeed(final Object o) {
        return ((PlaybackState)o).getPlaybackSpeed();
    }
    
    public static long getPosition(final Object o) {
        return ((PlaybackState)o).getPosition();
    }
    
    public static int getState(final Object o) {
        return ((PlaybackState)o).getState();
    }
    
    static final class CustomAction
    {
        public static String getAction(final Object o) {
            return ((PlaybackState$CustomAction)o).getAction();
        }
        
        public static Bundle getExtras(final Object o) {
            return ((PlaybackState$CustomAction)o).getExtras();
        }
        
        public static int getIcon(final Object o) {
            return ((PlaybackState$CustomAction)o).getIcon();
        }
        
        public static CharSequence getName(final Object o) {
            return ((PlaybackState$CustomAction)o).getName();
        }
    }
}
