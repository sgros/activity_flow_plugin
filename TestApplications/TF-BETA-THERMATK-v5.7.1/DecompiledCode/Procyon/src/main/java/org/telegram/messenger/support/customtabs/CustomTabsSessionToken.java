// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.support.customtabs;

import android.os.IInterface;
import android.os.IBinder;
import androidx.core.app.BundleCompat;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;
import android.os.Bundle;

public class CustomTabsSessionToken
{
    private static final String TAG = "CustomTabsSessionToken";
    private final CustomTabsCallback mCallback;
    private final ICustomTabsCallback mCallbackBinder;
    
    CustomTabsSessionToken(final ICustomTabsCallback mCallbackBinder) {
        this.mCallbackBinder = mCallbackBinder;
        this.mCallback = new CustomTabsCallback() {
            @Override
            public void extraCallback(final String s, final Bundle bundle) {
                try {
                    CustomTabsSessionToken.this.mCallbackBinder.extraCallback(s, bundle);
                }
                catch (RemoteException ex) {
                    Log.e("CustomTabsSessionToken", "RemoteException during ICustomTabsCallback transaction");
                }
            }
            
            @Override
            public void onMessageChannelReady(final Bundle bundle) {
                try {
                    CustomTabsSessionToken.this.mCallbackBinder.onMessageChannelReady(bundle);
                }
                catch (RemoteException ex) {
                    Log.e("CustomTabsSessionToken", "RemoteException during ICustomTabsCallback transaction");
                }
            }
            
            @Override
            public void onNavigationEvent(final int n, final Bundle bundle) {
                try {
                    CustomTabsSessionToken.this.mCallbackBinder.onNavigationEvent(n, bundle);
                }
                catch (RemoteException ex) {
                    Log.e("CustomTabsSessionToken", "RemoteException during ICustomTabsCallback transaction");
                }
            }
            
            @Override
            public void onPostMessage(final String s, final Bundle bundle) {
                try {
                    CustomTabsSessionToken.this.mCallbackBinder.onPostMessage(s, bundle);
                }
                catch (RemoteException ex) {
                    Log.e("CustomTabsSessionToken", "RemoteException during ICustomTabsCallback transaction");
                }
            }
        };
    }
    
    public static CustomTabsSessionToken createDummySessionTokenForTesting() {
        return new CustomTabsSessionToken(new DummyCallback());
    }
    
    public static CustomTabsSessionToken getSessionTokenFromIntent(final Intent intent) {
        final IBinder binder = BundleCompat.getBinder(intent.getExtras(), "android.support.customtabs.extra.SESSION");
        if (binder == null) {
            return null;
        }
        return new CustomTabsSessionToken(ICustomTabsCallback.Stub.asInterface(binder));
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof CustomTabsSessionToken && ((CustomTabsSessionToken)o).getCallbackBinder().equals(((IInterface)this.mCallbackBinder).asBinder());
    }
    
    public CustomTabsCallback getCallback() {
        return this.mCallback;
    }
    
    IBinder getCallbackBinder() {
        return ((IInterface)this.mCallbackBinder).asBinder();
    }
    
    @Override
    public int hashCode() {
        return this.getCallbackBinder().hashCode();
    }
    
    public boolean isAssociatedWith(final CustomTabsSession customTabsSession) {
        return customTabsSession.getBinder().equals(this.mCallbackBinder);
    }
    
    static class DummyCallback extends Stub
    {
        @Override
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public void extraCallback(final String s, final Bundle bundle) {
        }
        
        public void onMessageChannelReady(final Bundle bundle) {
        }
        
        public void onNavigationEvent(final int n, final Bundle bundle) {
        }
        
        public void onPostMessage(final String s, final Bundle bundle) {
        }
    }
}
