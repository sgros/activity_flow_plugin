// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.media.session;

import android.media.session.MediaSession$QueueItem;

class MediaSessionCompatApi21
{
    static class QueueItem
    {
        public static Object getDescription(final Object o) {
            return ((MediaSession$QueueItem)o).getDescription();
        }
        
        public static long getQueueId(final Object o) {
            return ((MediaSession$QueueItem)o).getQueueId();
        }
    }
}
