// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.os.RemoteException;
import android.os.IInterface;

public interface IContentObserver extends IInterface
{
    void onChange(final boolean p0) throws RemoteException;
    
    public abstract static class Stub extends Binder implements IContentObserver
    {
        private static final String DESCRIPTOR = "net.sqlcipher.IContentObserver";
        static final int TRANSACTION_onChange = 1;
        
        public Stub() {
            this.attachInterface((IInterface)this, "net.sqlcipher.IContentObserver");
        }
        
        public static IContentObserver asInterface(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("net.sqlcipher.IContentObserver");
            if (queryLocalInterface != null && queryLocalInterface instanceof IContentObserver) {
                return (IContentObserver)queryLocalInterface;
            }
            return new Proxy(binder);
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n == 1) {
                parcel.enforceInterface("net.sqlcipher.IContentObserver");
                this.onChange(parcel.readInt() != 0);
                return true;
            }
            if (n != 1598968902) {
                return super.onTransact(n, parcel, parcel2, n2);
            }
            parcel2.writeString("net.sqlcipher.IContentObserver");
            return true;
        }
        
        private static class Proxy implements IContentObserver
        {
            private IBinder mRemote;
            
            Proxy(final IBinder mRemote) {
                this.mRemote = mRemote;
            }
            
            public IBinder asBinder() {
                return this.mRemote;
            }
            
            public String getInterfaceDescriptor() {
                return "net.sqlcipher.IContentObserver";
            }
            
            @Override
            public void onChange(final boolean b) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("net.sqlcipher.IContentObserver");
                    obtain.writeInt((int)(b ? 1 : 0));
                    this.mRemote.transact(1, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
        }
    }
}
