package android.support.p001v4.media.session;

/* renamed from: android.support.v4.media.session.MediaSessionCompatApi21 */
class MediaSessionCompatApi21 {

    /* renamed from: android.support.v4.media.session.MediaSessionCompatApi21$QueueItem */
    static class QueueItem {
        public static Object getDescription(Object obj) {
            return ((android.media.session.MediaSession.QueueItem) obj).getDescription();
        }

        public static long getQueueId(Object obj) {
            return ((android.media.session.MediaSession.QueueItem) obj).getQueueId();
        }
    }
}
