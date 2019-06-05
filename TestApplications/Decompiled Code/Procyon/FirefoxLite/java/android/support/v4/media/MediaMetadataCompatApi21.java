// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.media;

import android.media.MediaMetadata;
import android.os.Parcel;

class MediaMetadataCompatApi21
{
    public static void writeToParcel(final Object o, final Parcel parcel, final int n) {
        ((MediaMetadata)o).writeToParcel(parcel, n);
    }
}
