package android.support.p001v4.media;

import android.media.MediaMetadata;
import android.os.Parcel;

/* renamed from: android.support.v4.media.MediaMetadataCompatApi21 */
class MediaMetadataCompatApi21 {
    public static void writeToParcel(Object obj, Parcel parcel, int i) {
        ((MediaMetadata) obj).writeToParcel(parcel, i);
    }
}
