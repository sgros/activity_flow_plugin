// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.app;

import androidx.core.app.RemoteActionCompat;
import androidx.versionedparcelable.VersionedParcel;

public final class RemoteActionCompatParcelizer extends androidx.core.app.RemoteActionCompatParcelizer
{
    public static RemoteActionCompat read(final VersionedParcel versionedParcel) {
        return androidx.core.app.RemoteActionCompatParcelizer.read(versionedParcel);
    }
    
    public static void write(final RemoteActionCompat remoteActionCompat, final VersionedParcel versionedParcel) {
        androidx.core.app.RemoteActionCompatParcelizer.write(remoteActionCompat, versionedParcel);
    }
}
