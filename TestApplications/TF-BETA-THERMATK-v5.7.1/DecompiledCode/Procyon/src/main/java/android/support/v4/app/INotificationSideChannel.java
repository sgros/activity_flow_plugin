// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.app;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.app.Notification;
import android.os.RemoteException;
import android.os.IInterface;

public interface INotificationSideChannel extends IInterface
{
    void cancel(final String p0, final int p1, final String p2) throws RemoteException;
    
    void cancelAll(final String p0) throws RemoteException;
    
    void notify(final String p0, final int p1, final String p2, final Notification p3) throws RemoteException;
    
    public abstract static class Stub extends Binder implements INotificationSideChannel
    {
        public static INotificationSideChannel asInterface(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("android.support.v4.app.INotificationSideChannel");
            if (queryLocalInterface != null && queryLocalInterface instanceof INotificationSideChannel) {
                return (INotificationSideChannel)queryLocalInterface;
            }
            return new Proxy(binder);
        }
        
        private static class Proxy implements INotificationSideChannel
        {
            private IBinder mRemote;
            
            Proxy(final IBinder mRemote) {
                this.mRemote = mRemote;
            }
            
            public IBinder asBinder() {
                return this.mRemote;
            }
            
            @Override
            public void cancel(final String s, final int n, final String s2) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("android.support.v4.app.INotificationSideChannel");
                    obtain.writeString(s);
                    obtain.writeInt(n);
                    obtain.writeString(s2);
                    this.mRemote.transact(2, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
            
            @Override
            public void cancelAll(final String s) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("android.support.v4.app.INotificationSideChannel");
                    obtain.writeString(s);
                    this.mRemote.transact(3, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
            
            @Override
            public void notify(final String s, final int n, final String s2, final Notification notification) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("android.support.v4.app.INotificationSideChannel");
                    obtain.writeString(s);
                    obtain.writeInt(n);
                    obtain.writeString(s2);
                    if (notification != null) {
                        obtain.writeInt(1);
                        notification.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(1, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
        }
    }
}
