// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.support.customtabs;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import java.util.List;
import android.net.Uri;
import android.os.RemoteException;
import android.os.Bundle;
import android.os.IInterface;

public interface ICustomTabsService extends IInterface
{
    Bundle extraCommand(final String p0, final Bundle p1) throws RemoteException;
    
    boolean mayLaunchUrl(final ICustomTabsCallback p0, final Uri p1, final Bundle p2, final List<Bundle> p3) throws RemoteException;
    
    boolean newSession(final ICustomTabsCallback p0) throws RemoteException;
    
    int postMessage(final ICustomTabsCallback p0, final String p1, final Bundle p2) throws RemoteException;
    
    boolean requestPostMessageChannel(final ICustomTabsCallback p0, final Uri p1) throws RemoteException;
    
    boolean updateVisuals(final ICustomTabsCallback p0, final Bundle p1) throws RemoteException;
    
    boolean warmup(final long p0) throws RemoteException;
    
    public abstract static class Stub extends Binder implements ICustomTabsService
    {
        private static final String DESCRIPTOR = "android.support.customtabs.ICustomTabsService";
        static final int TRANSACTION_extraCommand = 5;
        static final int TRANSACTION_mayLaunchUrl = 4;
        static final int TRANSACTION_newSession = 3;
        static final int TRANSACTION_postMessage = 8;
        static final int TRANSACTION_requestPostMessageChannel = 7;
        static final int TRANSACTION_updateVisuals = 6;
        static final int TRANSACTION_warmup = 2;
        
        public Stub() {
            this.attachInterface((IInterface)this, "android.support.customtabs.ICustomTabsService");
        }
        
        public static ICustomTabsService asInterface(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("android.support.customtabs.ICustomTabsService");
            ICustomTabsService customTabsService;
            if (queryLocalInterface != null && queryLocalInterface instanceof ICustomTabsService) {
                customTabsService = (ICustomTabsService)queryLocalInterface;
            }
            else {
                customTabsService = new Proxy(binder);
            }
            return customTabsService;
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public boolean onTransact(int postMessage, final Parcel parcel, final Parcel parcel2, final int n) throws RemoteException {
            if (postMessage == 1598968902) {
                parcel2.writeString("android.support.customtabs.ICustomTabsService");
                return true;
            }
            final Uri uri = null;
            final Bundle bundle = null;
            final Bundle bundle2 = null;
            Bundle bundle3 = null;
            Bundle bundle4 = null;
            final int n2 = 0;
            final int n3 = 0;
            final int n4 = 0;
            final int n5 = 0;
            final int n6 = 0;
            switch (postMessage) {
                default: {
                    return super.onTransact(postMessage, parcel, parcel2, n);
                }
                case 8: {
                    parcel.enforceInterface("android.support.customtabs.ICustomTabsService");
                    final ICustomTabsCallback interface1 = ICustomTabsCallback.Stub.asInterface(parcel.readStrongBinder());
                    final String string = parcel.readString();
                    if (parcel.readInt() != 0) {
                        bundle4 = (Bundle)Bundle.CREATOR.createFromParcel(parcel);
                    }
                    postMessage = this.postMessage(interface1, string, bundle4);
                    parcel2.writeNoException();
                    parcel2.writeInt(postMessage);
                    return true;
                }
                case 7: {
                    parcel.enforceInterface("android.support.customtabs.ICustomTabsService");
                    final ICustomTabsCallback interface2 = ICustomTabsCallback.Stub.asInterface(parcel.readStrongBinder());
                    Uri uri2 = uri;
                    if (parcel.readInt() != 0) {
                        uri2 = (Uri)Uri.CREATOR.createFromParcel(parcel);
                    }
                    final boolean requestPostMessageChannel = this.requestPostMessageChannel(interface2, uri2);
                    parcel2.writeNoException();
                    postMessage = n6;
                    if (requestPostMessageChannel) {
                        postMessage = 1;
                    }
                    parcel2.writeInt(postMessage);
                    return true;
                }
                case 6: {
                    parcel.enforceInterface("android.support.customtabs.ICustomTabsService");
                    final ICustomTabsCallback interface3 = ICustomTabsCallback.Stub.asInterface(parcel.readStrongBinder());
                    Bundle bundle5 = bundle;
                    if (parcel.readInt() != 0) {
                        bundle5 = (Bundle)Bundle.CREATOR.createFromParcel(parcel);
                    }
                    final boolean updateVisuals = this.updateVisuals(interface3, bundle5);
                    parcel2.writeNoException();
                    postMessage = n2;
                    if (updateVisuals) {
                        postMessage = 1;
                    }
                    parcel2.writeInt(postMessage);
                    return true;
                }
                case 5: {
                    parcel.enforceInterface("android.support.customtabs.ICustomTabsService");
                    final String string2 = parcel.readString();
                    Bundle bundle6 = bundle2;
                    if (parcel.readInt() != 0) {
                        bundle6 = (Bundle)Bundle.CREATOR.createFromParcel(parcel);
                    }
                    final Bundle extraCommand = this.extraCommand(string2, bundle6);
                    parcel2.writeNoException();
                    if (extraCommand != null) {
                        parcel2.writeInt(1);
                        extraCommand.writeToParcel(parcel2, 1);
                    }
                    else {
                        parcel2.writeInt(0);
                    }
                    return true;
                }
                case 4: {
                    parcel.enforceInterface("android.support.customtabs.ICustomTabsService");
                    final ICustomTabsCallback interface4 = ICustomTabsCallback.Stub.asInterface(parcel.readStrongBinder());
                    Uri uri3;
                    if (parcel.readInt() != 0) {
                        uri3 = (Uri)Uri.CREATOR.createFromParcel(parcel);
                    }
                    else {
                        uri3 = null;
                    }
                    if (parcel.readInt() != 0) {
                        bundle3 = (Bundle)Bundle.CREATOR.createFromParcel(parcel);
                    }
                    final boolean mayLaunchUrl = this.mayLaunchUrl(interface4, uri3, bundle3, parcel.createTypedArrayList(Bundle.CREATOR));
                    parcel2.writeNoException();
                    postMessage = n3;
                    if (mayLaunchUrl) {
                        postMessage = 1;
                    }
                    parcel2.writeInt(postMessage);
                    return true;
                }
                case 3: {
                    parcel.enforceInterface("android.support.customtabs.ICustomTabsService");
                    final boolean session = this.newSession(ICustomTabsCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    postMessage = n4;
                    if (session) {
                        postMessage = 1;
                    }
                    parcel2.writeInt(postMessage);
                    return true;
                }
                case 2: {
                    parcel.enforceInterface("android.support.customtabs.ICustomTabsService");
                    final boolean warmup = this.warmup(parcel.readLong());
                    parcel2.writeNoException();
                    postMessage = n5;
                    if (warmup) {
                        postMessage = 1;
                    }
                    parcel2.writeInt(postMessage);
                    return true;
                }
            }
        }
        
        private static class Proxy implements ICustomTabsService
        {
            private IBinder mRemote;
            
            Proxy(final IBinder mRemote) {
                this.mRemote = mRemote;
            }
            
            public IBinder asBinder() {
                return this.mRemote;
            }
            
            @Override
            public Bundle extraCommand(final String s, final Bundle bundle) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("android.support.customtabs.ICustomTabsService");
                    obtain.writeString(s);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    Bundle bundle2;
                    if (obtain2.readInt() != 0) {
                        bundle2 = (Bundle)Bundle.CREATOR.createFromParcel(obtain2);
                    }
                    else {
                        bundle2 = null;
                    }
                    return bundle2;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            public String getInterfaceDescriptor() {
                return "android.support.customtabs.ICustomTabsService";
            }
            
            @Override
            public boolean mayLaunchUrl(final ICustomTabsCallback customTabsCallback, final Uri uri, final Bundle bundle, final List<Bundle> list) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("android.support.customtabs.ICustomTabsService");
                    IBinder binder;
                    if (customTabsCallback != null) {
                        binder = ((IInterface)customTabsCallback).asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    boolean b = true;
                    if (uri != null) {
                        obtain.writeInt(1);
                        uri.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    obtain.writeTypedList((List)list);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() == 0) {
                        b = false;
                    }
                    return b;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public boolean newSession(final ICustomTabsCallback customTabsCallback) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("android.support.customtabs.ICustomTabsService");
                    IBinder binder;
                    if (customTabsCallback != null) {
                        binder = ((IInterface)customTabsCallback).asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    final IBinder mRemote = this.mRemote;
                    boolean b = false;
                    mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        b = true;
                    }
                    return b;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public int postMessage(final ICustomTabsCallback customTabsCallback, final String s, final Bundle bundle) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("android.support.customtabs.ICustomTabsService");
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
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public boolean requestPostMessageChannel(final ICustomTabsCallback customTabsCallback, final Uri uri) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("android.support.customtabs.ICustomTabsService");
                    IBinder binder;
                    if (customTabsCallback != null) {
                        binder = ((IInterface)customTabsCallback).asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    boolean b = true;
                    if (uri != null) {
                        obtain.writeInt(1);
                        uri.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() == 0) {
                        b = false;
                    }
                    return b;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public boolean updateVisuals(final ICustomTabsCallback customTabsCallback, final Bundle bundle) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("android.support.customtabs.ICustomTabsService");
                    IBinder binder;
                    if (customTabsCallback != null) {
                        binder = ((IInterface)customTabsCallback).asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    boolean b = true;
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() == 0) {
                        b = false;
                    }
                    return b;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public boolean warmup(final long n) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("android.support.customtabs.ICustomTabsService");
                    obtain.writeLong(n);
                    final IBinder mRemote = this.mRemote;
                    boolean b = false;
                    mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        b = true;
                    }
                    return b;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
