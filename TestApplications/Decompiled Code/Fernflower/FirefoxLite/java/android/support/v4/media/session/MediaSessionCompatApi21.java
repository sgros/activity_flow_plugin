package android.support.v4.media.session;

class MediaSessionCompatApi21 {
   static class QueueItem {
      public static Object getDescription(Object var0) {
         return ((android.media.session.MediaSession.QueueItem)var0).getDescription();
      }

      public static long getQueueId(Object var0) {
         return ((android.media.session.MediaSession.QueueItem)var0).getQueueId();
      }
   }
}
