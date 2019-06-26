// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.support.customtabs;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.os.RemoteException;
import android.os.Bundle;
import android.os.IInterface;

public interface IPostMessageService extends IInterface
{
    void onMessageChannelReady(final ICustomTabsCallback p0, final Bundle p1) throws RemoteException;
    
    void onPostMessage(final ICustomTabsCallback p0, final String p1, final Bundle p2) throws RemoteException;
    
    public abstract static class Stub extends Binder implements IPostMessageService
    {
        private static final String DESCRIPTOR = "android.support.customtabs.IPostMessageService";
        static final int TRANSACTION_onMessageChannelReady = 2;
        static final int TRANSACTION_onPostMessage = 3;
        
        public Stub() {
            this.attachInterface((IInterface)this, "android.support.customtabs.IPostMessageService");
        }
        
        public static IPostMessageService asInterface(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("android.support.customtabs.IPostMessageService");
            IPostMessageService postMessageService;
            if (queryLocalInterface != null && queryLocalInterface instanceof IPostMessageService) {
                postMessageService = (IPostMessageService)queryLocalInterface;
            }
            else {
                postMessageService = new Proxy(binder);
            }
            return postMessageService;
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            final Bundle bundle = null;
            Bundle bundle2 = null;
            if (n == 2) {
                parcel.enforceInterface("android.support.customtabs.IPostMessageService");
                final ICustomTabsCallback interface1 = ICustomTabsCallback.Stub.asInterface(parcel.readStrongBinder());
                Bundle bundle3 = bundle;
                if (parcel.readInt() != 0) {
                    bundle3 = (Bundle)Bundle.CREATOR.createFromParcel(parcel);
                }
                this.onMessageChannelReady(interface1, bundle3);
                parcel2.writeNoException();
                return true;
            }
            if (n == 3) {
                parcel.enforceInterface("android.support.customtabs.IPostMessageService");
                final ICustomTabsCallback interface2 = ICustomTabsCallback.Stub.asInterface(parcel.readStrongBinder());
                final String string = parcel.readString();
                if (parcel.readInt() != 0) {
                    bundle2 = (Bundle)Bundle.CREATOR.createFromParcel(parcel);
                }
                this.onPostMessage(interface2, string, bundle2);
                parcel2.writeNoException();
                return true;
            }
            if (n != 1598968902) {
                return super.onTransact(n, parcel, parcel2, n2);
            }
            parcel2.writeString("android.support.customtabs.IPostMessageService");
            return true;
        }
        
        private static class Proxy implements IPostMessageService
        {
            private IBinder mRemote;
            
            Proxy(final IBinder mRemote) {
                this.mRemote = mRemote;
            }
            
            public IBinder asBinder() {
                return this.mRemote;
            }
            
            public String getInterfaceDescriptor() {
                return "android.support.customtabs.IPostMessageService";
            }
            
            @Override
            public void onMessageChannelReady(final ICustomTabsCallback customTabsCallback, final Bundle bundle) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("android.support.customtabs.IPostMessageService");
                    IBinder binder;
                    if (customTabsCallback != null) {
                        binder = ((IInterface)customTabsCallback).asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void onPostMessage(final ICustomTabsCallback customTabsCallback, final String s, final Bundle bundle) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("android.support.customtabs.IPostMessageService");
                    IBinder binder;
                    if (customTabsCallback != null) {
                        binder = ((IInterface)customTabsCallback).asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    obtain.writeString(s);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
