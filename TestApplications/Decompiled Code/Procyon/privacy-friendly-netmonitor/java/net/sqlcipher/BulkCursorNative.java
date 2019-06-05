// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher;

import android.os.RemoteException;
import android.os.Bundle;
import java.util.Map;
import android.os.Parcel;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Binder;

public abstract class BulkCursorNative extends Binder implements IBulkCursor
{
    public BulkCursorNative() {
        this.attachInterface((IInterface)this, "android.content.IBulkCursor");
    }
    
    public static IBulkCursor asInterface(final IBinder binder) {
        if (binder == null) {
            return null;
        }
        final IBulkCursor bulkCursor = (IBulkCursor)binder.queryLocalInterface("android.content.IBulkCursor");
        if (bulkCursor != null) {
            return bulkCursor;
        }
        return new BulkCursorProxy(binder);
    }
    
    public IBinder asBinder() {
        return (IBinder)this;
    }
    
    public boolean onTransact(int n, final Parcel parcel, final Parcel parcel2, int length) throws RemoteException {
        final int n2 = 0;
        final int n3 = 0;
        final int n4 = 0;
        Label_0379: {
            switch (n) {
                case 12: {
                    break Label_0379;
                }
                case 11: {
                    break Label_0379;
                }
                case 10: {
                    break Label_0379;
                }
                case 9: {
                    break Label_0379;
                }
                case 8: {
                    break Label_0379;
                }
                case 7: {
                    break Label_0379;
                }
                case 6: {
                    break Label_0379;
                }
                case 5: {
                    break Label_0379;
                }
                case 4: {
                    break Label_0379;
                }
                case 3: {
                    break Label_0379;
                }
                case 2: {
                    break Label_0379;
                }
                case 1: {
                    Label_0401: {
                        break Label_0401;
                        try {
                            parcel.enforceInterface("android.content.IBulkCursor");
                            this.close();
                            parcel2.writeNoException();
                            return true;
                            parcel.enforceInterface("android.content.IBulkCursor");
                            this.onMove(parcel.readInt());
                            parcel2.writeNoException();
                            return true;
                            // iftrue(Label_0377:, n >= length)
                            // iftrue(Label_0427:, window != null)
                            // iftrue(Label_0284:, deleteRow != true)
                            // iftrue(Label_0323:, updateRows != true)
                        Label_0358_Outer:
                            while (true) {
                                n = 1;
                                Label_0323: {
                                    break Label_0323;
                                    Block_5: {
                                        while (true) {
                                            String[] columnNames = null;
                                            Block_4: {
                                                while (true) {
                                                    n = 1;
                                                    Label_0284: {
                                                        break Label_0284;
                                                        parcel.enforceInterface("android.content.IBulkCursor");
                                                        final Bundle extras = this.getExtras();
                                                        parcel2.writeNoException();
                                                        parcel2.writeBundle(extras);
                                                        return true;
                                                        parcel.enforceInterface("android.content.IBulkCursor");
                                                        columnNames = this.getColumnNames();
                                                        parcel2.writeNoException();
                                                        parcel2.writeInt(columnNames.length);
                                                        length = columnNames.length;
                                                        n = n3;
                                                        break Block_4;
                                                    }
                                                    parcel2.writeInt(n);
                                                    return true;
                                                    parcel.enforceInterface("android.content.IBulkCursor");
                                                    final CursorWindow window = this.getWindow(parcel.readInt());
                                                    break Block_5;
                                                    parcel.enforceInterface("android.content.IBulkCursor");
                                                    final boolean deleteRow = this.deleteRow(parcel.readInt());
                                                    parcel2.writeNoException();
                                                    n = n4;
                                                    continue Label_0358_Outer;
                                                }
                                                parcel.enforceInterface("android.content.IBulkCursor");
                                                n = this.count();
                                                parcel2.writeNoException();
                                                parcel2.writeInt(n);
                                                return true;
                                                Label_0427: {
                                                    parcel2.writeNoException();
                                                }
                                                parcel2.writeInt(1);
                                                CursorWindow window = null;
                                                window.writeToParcel(parcel2, 0);
                                                return true;
                                            }
                                            parcel2.writeString(columnNames[n]);
                                            ++n;
                                            continue;
                                        }
                                        parcel.enforceInterface("android.content.IBulkCursor");
                                        final Bundle respond = this.respond(parcel.readBundle(this.getClass().getClassLoader()));
                                        parcel2.writeNoException();
                                        parcel2.writeBundle(respond);
                                        return true;
                                        parcel.enforceInterface("android.content.IBulkCursor");
                                        n = (this.getWantsAllOnMoveCalls() ? 1 : 0);
                                        parcel2.writeNoException();
                                        parcel2.writeInt(n);
                                        return true;
                                    }
                                    parcel2.writeInt(0);
                                    return true;
                                    parcel.enforceInterface("android.content.IBulkCursor");
                                    this.deactivate();
                                    parcel2.writeNoException();
                                    return true;
                                }
                                parcel2.writeInt(n);
                                return true;
                                parcel.enforceInterface("android.content.IBulkCursor");
                                n = this.requery(IContentObserver.Stub.asInterface(parcel.readStrongBinder()), (CursorWindow)CursorWindow.CREATOR.createFromParcel(parcel));
                                parcel2.writeNoException();
                                parcel2.writeInt(n);
                                parcel2.writeBundle(this.getExtras());
                                return true;
                                parcel.enforceInterface("android.content.IBulkCursor");
                                final boolean updateRows = this.updateRows(parcel.readHashMap((ClassLoader)null));
                                parcel2.writeNoException();
                                n = n2;
                                continue;
                            }
                            Label_0377: {
                                return true;
                            }
                        }
                        catch (Exception ex) {
                            DatabaseUtils.writeExceptionToParcel(parcel2, ex);
                            return true;
                        }
                    }
                    break;
                }
            }
        }
        return super.onTransact(n, parcel, parcel2, length);
    }
}
