// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.support.customtabs;

import android.os.IInterface;
import android.app.PendingIntent;
import android.widget.RemoteViews;
import android.os.Parcelable;
import android.graphics.Bitmap;
import android.os.RemoteException;
import java.util.List;
import android.os.Bundle;
import android.net.Uri;
import android.os.IBinder;
import android.content.ComponentName;

public final class CustomTabsSession
{
    private static final String TAG = "CustomTabsSession";
    private final ICustomTabsCallback mCallback;
    private final ComponentName mComponentName;
    private final Object mLock;
    private final ICustomTabsService mService;
    
    CustomTabsSession(final ICustomTabsService mService, final ICustomTabsCallback mCallback, final ComponentName mComponentName) {
        this.mLock = new Object();
        this.mService = mService;
        this.mCallback = mCallback;
        this.mComponentName = mComponentName;
    }
    
    public static CustomTabsSession createDummySessionForTesting(final ComponentName componentName) {
        return new CustomTabsSession(null, new CustomTabsSessionToken.DummyCallback(), componentName);
    }
    
    IBinder getBinder() {
        return ((IInterface)this.mCallback).asBinder();
    }
    
    ComponentName getComponentName() {
        return this.mComponentName;
    }
    
    public boolean mayLaunchUrl(final Uri uri, final Bundle bundle, final List<Bundle> list) {
        try {
            return this.mService.mayLaunchUrl(this.mCallback, uri, bundle, list);
        }
        catch (RemoteException ex) {
            return false;
        }
    }
    
    public int postMessage(final String s, final Bundle bundle) {
        final Object mLock = this.mLock;
        // monitorenter(mLock)
        try {
            try {
                // monitorexit(mLock)
                return this.mService.postMessage(this.mCallback, s, bundle);
            }
            finally {
            }
            // monitorexit(mLock)
        }
        catch (RemoteException ex) {}
    }
    
    public boolean requestPostMessageChannel(final Uri uri) {
        try {
            return this.mService.requestPostMessageChannel(this.mCallback, uri);
        }
        catch (RemoteException ex) {
            return false;
        }
    }
    
    public boolean setActionButton(final Bitmap bitmap, final String s) {
        final Bundle bundle = new Bundle();
        bundle.putParcelable("android.support.customtabs.customaction.ICON", (Parcelable)bitmap);
        bundle.putString("android.support.customtabs.customaction.DESCRIPTION", s);
        final Bundle bundle2 = new Bundle();
        bundle2.putBundle("android.support.customtabs.extra.ACTION_BUTTON_BUNDLE", bundle);
        try {
            return this.mService.updateVisuals(this.mCallback, bundle2);
        }
        catch (RemoteException ex) {
            return false;
        }
    }
    
    public boolean setSecondaryToolbarViews(final RemoteViews remoteViews, final int[] array, final PendingIntent pendingIntent) {
        final Bundle bundle = new Bundle();
        bundle.putParcelable("android.support.customtabs.extra.EXTRA_REMOTEVIEWS", (Parcelable)remoteViews);
        bundle.putIntArray("android.support.customtabs.extra.EXTRA_REMOTEVIEWS_VIEW_IDS", array);
        bundle.putParcelable("android.support.customtabs.extra.EXTRA_REMOTEVIEWS_PENDINGINTENT", (Parcelable)pendingIntent);
        try {
            return this.mService.updateVisuals(this.mCallback, bundle);
        }
        catch (RemoteException ex) {
            return false;
        }
    }
    
    @Deprecated
    public boolean setToolbarItem(final int n, final Bitmap bitmap, final String s) {
        final Bundle bundle = new Bundle();
        bundle.putInt("android.support.customtabs.customaction.ID", n);
        bundle.putParcelable("android.support.customtabs.customaction.ICON", (Parcelable)bitmap);
        bundle.putString("android.support.customtabs.customaction.DESCRIPTION", s);
        final Bundle bundle2 = new Bundle();
        bundle2.putBundle("android.support.customtabs.extra.ACTION_BUTTON_BUNDLE", bundle);
        try {
            return this.mService.updateVisuals(this.mCallback, bundle2);
        }
        catch (RemoteException ex) {
            return false;
        }
    }
}
