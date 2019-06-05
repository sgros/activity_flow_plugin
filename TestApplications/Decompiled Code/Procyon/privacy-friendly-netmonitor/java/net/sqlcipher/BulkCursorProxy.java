// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher;

import java.util.Map;
import android.os.IInterface;
import android.os.RemoteException;
import android.os.Parcel;
import android.os.IBinder;
import android.os.Bundle;

final class BulkCursorProxy implements IBulkCursor
{
    private Bundle mExtras;
    private IBinder mRemote;
    
    public BulkCursorProxy(final IBinder mRemote) {
        this.mRemote = mRemote;
        this.mExtras = null;
    }
    
    public IBinder asBinder() {
        return this.mRemote;
    }
    
    @Override
    public void close() throws RemoteException {
        final Parcel obtain = Parcel.obtain();
        final Parcel obtain2 = Parcel.obtain();
        obtain.writeInterfaceToken("android.content.IBulkCursor");
        this.mRemote.transact(12, obtain, obtain2, 0);
        DatabaseUtils.readExceptionFromParcel(obtain2);
        obtain.recycle();
        obtain2.recycle();
    }
    
    @Override
    public int count() throws RemoteException {
        final Parcel obtain = Parcel.obtain();
        final Parcel obtain2 = Parcel.obtain();
        obtain.writeInterfaceToken("android.content.IBulkCursor");
        final boolean transact = this.mRemote.transact(2, obtain, obtain2, 0);
        DatabaseUtils.readExceptionFromParcel(obtain2);
        int int1;
        if (!transact) {
            int1 = -1;
        }
        else {
            int1 = obtain2.readInt();
        }
        obtain.recycle();
        obtain2.recycle();
        return int1;
    }
    
    @Override
    public void deactivate() throws RemoteException {
        final Parcel obtain = Parcel.obtain();
        final Parcel obtain2 = Parcel.obtain();
        obtain.writeInterfaceToken("android.content.IBulkCursor");
        this.mRemote.transact(6, obtain, obtain2, 0);
        DatabaseUtils.readExceptionFromParcel(obtain2);
        obtain.recycle();
        obtain2.recycle();
    }
    
    @Override
    public boolean deleteRow(final int n) throws RemoteException {
        final Parcel obtain = Parcel.obtain();
        final Parcel obtain2 = Parcel.obtain();
        obtain.writeInterfaceToken("android.content.IBulkCursor");
        obtain.writeInt(n);
        final IBinder mRemote = this.mRemote;
        boolean b = false;
        mRemote.transact(5, obtain, obtain2, 0);
        DatabaseUtils.readExceptionFromParcel(obtain2);
        if (obtain2.readInt() == 1) {
            b = true;
        }
        obtain.recycle();
        obtain2.recycle();
        return b;
    }
    
    @Override
    public String[] getColumnNames() throws RemoteException {
        final Parcel obtain = Parcel.obtain();
        final Parcel obtain2 = Parcel.obtain();
        obtain.writeInterfaceToken("android.content.IBulkCursor");
        final IBinder mRemote = this.mRemote;
        int i = 0;
        mRemote.transact(3, obtain, obtain2, 0);
        DatabaseUtils.readExceptionFromParcel(obtain2);
        final int int1 = obtain2.readInt();
        final String[] array = new String[int1];
        while (i < int1) {
            array[i] = obtain2.readString();
            ++i;
        }
        obtain.recycle();
        obtain2.recycle();
        return array;
    }
    
    @Override
    public Bundle getExtras() throws RemoteException {
        if (this.mExtras == null) {
            final Parcel obtain = Parcel.obtain();
            final Parcel obtain2 = Parcel.obtain();
            obtain.writeInterfaceToken("android.content.IBulkCursor");
            this.mRemote.transact(10, obtain, obtain2, 0);
            DatabaseUtils.readExceptionFromParcel(obtain2);
            this.mExtras = obtain2.readBundle(this.getClass().getClassLoader());
            obtain.recycle();
            obtain2.recycle();
        }
        return this.mExtras;
    }
    
    @Override
    public boolean getWantsAllOnMoveCalls() throws RemoteException {
        final Parcel obtain = Parcel.obtain();
        final Parcel obtain2 = Parcel.obtain();
        obtain.writeInterfaceToken("android.content.IBulkCursor");
        final IBinder mRemote = this.mRemote;
        boolean b = false;
        mRemote.transact(9, obtain, obtain2, 0);
        DatabaseUtils.readExceptionFromParcel(obtain2);
        final int int1 = obtain2.readInt();
        obtain.recycle();
        obtain2.recycle();
        if (int1 != 0) {
            b = true;
        }
        return b;
    }
    
    @Override
    public CursorWindow getWindow(final int n) throws RemoteException {
        final Parcel obtain = Parcel.obtain();
        final Parcel obtain2 = Parcel.obtain();
        obtain.writeInterfaceToken("android.content.IBulkCursor");
        obtain.writeInt(n);
        this.mRemote.transact(1, obtain, obtain2, 0);
        DatabaseUtils.readExceptionFromParcel(obtain2);
        CursorWindow fromParcel;
        if (obtain2.readInt() == 1) {
            fromParcel = CursorWindow.newFromParcel(obtain2);
        }
        else {
            fromParcel = null;
        }
        obtain.recycle();
        obtain2.recycle();
        return fromParcel;
    }
    
    @Override
    public void onMove(final int n) throws RemoteException {
        final Parcel obtain = Parcel.obtain();
        final Parcel obtain2 = Parcel.obtain();
        obtain.writeInterfaceToken("android.content.IBulkCursor");
        obtain.writeInt(n);
        this.mRemote.transact(8, obtain, obtain2, 0);
        DatabaseUtils.readExceptionFromParcel(obtain2);
        obtain.recycle();
        obtain2.recycle();
    }
    
    @Override
    public int requery(final IContentObserver contentObserver, final CursorWindow cursorWindow) throws RemoteException {
        final Parcel obtain = Parcel.obtain();
        final Parcel obtain2 = Parcel.obtain();
        obtain.writeInterfaceToken("android.content.IBulkCursor");
        obtain.writeStrongInterface((IInterface)contentObserver);
        cursorWindow.writeToParcel(obtain, 0);
        final boolean transact = this.mRemote.transact(7, obtain, obtain2, 0);
        DatabaseUtils.readExceptionFromParcel(obtain2);
        int int1;
        if (!transact) {
            int1 = -1;
        }
        else {
            int1 = obtain2.readInt();
            this.mExtras = obtain2.readBundle(this.getClass().getClassLoader());
        }
        obtain.recycle();
        obtain2.recycle();
        return int1;
    }
    
    @Override
    public Bundle respond(Bundle bundle) throws RemoteException {
        final Parcel obtain = Parcel.obtain();
        final Parcel obtain2 = Parcel.obtain();
        obtain.writeInterfaceToken("android.content.IBulkCursor");
        obtain.writeBundle(bundle);
        this.mRemote.transact(11, obtain, obtain2, 0);
        DatabaseUtils.readExceptionFromParcel(obtain2);
        bundle = obtain2.readBundle(this.getClass().getClassLoader());
        obtain.recycle();
        obtain2.recycle();
        return bundle;
    }
    
    @Override
    public boolean updateRows(final Map map) throws RemoteException {
        final Parcel obtain = Parcel.obtain();
        final Parcel obtain2 = Parcel.obtain();
        obtain.writeInterfaceToken("android.content.IBulkCursor");
        obtain.writeMap(map);
        final IBinder mRemote = this.mRemote;
        boolean b = false;
        mRemote.transact(4, obtain, obtain2, 0);
        DatabaseUtils.readExceptionFromParcel(obtain2);
        if (obtain2.readInt() == 1) {
            b = true;
        }
        obtain.recycle();
        obtain2.recycle();
        return b;
    }
}
