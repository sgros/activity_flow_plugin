package android.support.p001v4.media.session;

import android.media.session.PlaybackState;
import android.os.Bundle;

/* renamed from: android.support.v4.media.session.PlaybackStateCompatApi22 */
class PlaybackStateCompatApi22 {
    public static Bundle getExtras(Object obj) {
        return ((PlaybackState) obj).getExtras();
    }
}