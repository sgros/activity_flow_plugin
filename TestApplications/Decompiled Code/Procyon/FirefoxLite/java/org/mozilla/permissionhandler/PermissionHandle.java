// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.permissionhandler;

import android.support.design.widget.Snackbar;
import android.os.Parcelable;

public interface PermissionHandle
{
    void doActionDirect(final String p0, final int p1, final Parcelable p2);
    
    void doActionGranted(final String p0, final int p1, final Parcelable p2);
    
    void doActionNoPermission(final String p0, final int p1, final Parcelable p2);
    
    void doActionSetting(final String p0, final int p1, final Parcelable p2);
    
    Snackbar makeAskAgainSnackBar(final int p0);
    
    void permissionDeniedToast(final int p0);
    
    void requestPermissions(final int p0);
}
