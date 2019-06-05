// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.media.session;

import android.media.session.PlaybackState;
import android.os.Bundle;

class PlaybackStateCompatApi22
{
    public static Bundle getExtras(final Object o) {
        return ((PlaybackState)o).getExtras();
    }
}
