package net.sqlcipher;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.Map;

final class BulkCursorProxy implements IBulkCursor {
   private Bundle mExtras;
   private IBinder mRemote;

   public BulkCursorProxy(IBinder var1) {
      this.mRemote = var1;
      this.mExtras = null;
   }

   public IBinder asBinder() {
      return this.mRemote;
   }

   public void close() throws RemoteException {
      Parcel var1 = Parcel.obtain();
      Parcel var2 = Parcel.obtain();
      var1.writeInterfaceToken("android.content.IBulkCursor");
      this.mRemote.transact(12, var1, var2, 0);
      DatabaseUtils.readExceptionFromParcel(var2);
      var1.recycle();
      var2.recycle();
   }

   public int count() throws RemoteException {
      Parcel var1 = Parcel.obtain();
      Parcel var2 = Parcel.obtain();
      var1.writeInterfaceToken("android.content.IBulkCursor");
      boolean var3 = this.mRemote.transact(2, var1, var2, 0);
      DatabaseUtils.readExceptionFromParcel(var2);
      int var4;
      if (!var3) {
         var4 = -1;
      } else {
         var4 = var2.readInt();
      }

      var1.recycle();
      var2.recycle();
      return var4;
   }

   public void deactivate() throws RemoteException {
      Parcel var1 = Parcel.obtain();
      Parcel var2 = Parcel.obtain();
      var1.writeInterfaceToken("android.content.IBulkCursor");
      this.mRemote.transact(6, var1, var2, 0);
      DatabaseUtils.readExceptionFromParcel(var2);
      var1.recycle();
      var2.recycle();
   }

   public boolean deleteRow(int var1) throws RemoteException {
      Parcel var2 = Parcel.obtain();
      Parcel var3 = Parcel.obtain();
      var2.writeInterfaceToken("android.content.IBulkCursor");
      var2.writeInt(var1);
      IBinder var4 = this.mRemote;
      boolean var5 = false;
      var4.transact(5, var2, var3, 0);
      DatabaseUtils.readExceptionFromParcel(var3);
      if (var3.readInt() == 1) {
         var5 = true;
      }

      var2.recycle();
      var3.recycle();
      return var5;
   }

   public String[] getColumnNames() throws RemoteException {
      Parcel var1 = Parcel.obtain();
      Parcel var2 = Parcel.obtain();
      var1.writeInterfaceToken("android.content.IBulkCursor");
      IBinder var3 = this.mRemote;
      int var4 = 0;
      var3.transact(3, var1, var2, 0);
      DatabaseUtils.readExceptionFromParcel(var2);
      int var5 = var2.readInt();

      String[] var6;
      for(var6 = new String[var5]; var4 < var5; ++var4) {
         var6[var4] = var2.readString();
      }

      var1.recycle();
      var2.recycle();
      return var6;
   }

   public Bundle getExtras() throws RemoteException {
      if (this.mExtras == null) {
         Parcel var1 = Parcel.obtain();
         Parcel var2 = Parcel.obtain();
         var1.writeInterfaceToken("android.content.IBulkCursor");
         this.mRemote.transact(10, var1, var2, 0);
         DatabaseUtils.readExceptionFromParcel(var2);
         this.mExtras = var2.readBundle(this.getClass().getClassLoader());
         var1.recycle();
         var2.recycle();
      }

      return this.mExtras;
   }

   public boolean getWantsAllOnMoveCalls() throws RemoteException {
      Parcel var1 = Parcel.obtain();
      Parcel var2 = Parcel.obtain();
      var1.writeInterfaceToken("android.content.IBulkCursor");
      IBinder var3 = this.mRemote;
      boolean var4 = false;
      var3.transact(9, var1, var2, 0);
      DatabaseUtils.readExceptionFromParcel(var2);
      int var5 = var2.readInt();
      var1.recycle();
      var2.recycle();
      if (var5 != 0) {
         var4 = true;
      }

      return var4;
   }

   public CursorWindow getWindow(int var1) throws RemoteException {
      Parcel var2 = Parcel.obtain();
      Parcel var3 = Parcel.obtain();
      var2.writeInterfaceToken("android.content.IBulkCursor");
      var2.writeInt(var1);
      this.mRemote.transact(1, var2, var3, 0);
      DatabaseUtils.readExceptionFromParcel(var3);
      CursorWindow var4;
      if (var3.readInt() == 1) {
         var4 = CursorWindow.newFromParcel(var3);
      } else {
         var4 = null;
      }

      var2.recycle();
      var3.recycle();
      return var4;
   }

   public void onMove(int var1) throws RemoteException {
      Parcel var2 = Parcel.obtain();
      Parcel var3 = Parcel.obtain();
      var2.writeInterfaceToken("android.content.IBulkCursor");
      var2.writeInt(var1);
      this.mRemote.transact(8, var2, var3, 0);
      DatabaseUtils.readExceptionFromParcel(var3);
      var2.recycle();
      var3.recycle();
   }

   public int requery(IContentObserver var1, CursorWindow var2) throws RemoteException {
      Parcel var3 = Parcel.obtain();
      Parcel var4 = Parcel.obtain();
      var3.writeInterfaceToken("android.content.IBulkCursor");
      var3.writeStrongInterface(var1);
      var2.writeToParcel(var3, 0);
      boolean var5 = this.mRemote.transact(7, var3, var4, 0);
      DatabaseUtils.readExceptionFromParcel(var4);
      int var6;
      if (!var5) {
         var6 = -1;
      } else {
         var6 = var4.readInt();
         this.mExtras = var4.readBundle(this.getClass().getClassLoader());
      }

      var3.recycle();
      var4.recycle();
      return var6;
   }

   public Bundle respond(Bundle var1) throws RemoteException {
      Parcel var2 = Parcel.obtain();
      Parcel var3 = Parcel.obtain();
      var2.writeInterfaceToken("android.content.IBulkCursor");
      var2.writeBundle(var1);
      this.mRemote.transact(11, var2, var3, 0);
      DatabaseUtils.readExceptionFromParcel(var3);
      var1 = var3.readBundle(this.getClass().getClassLoader());
      var2.recycle();
      var3.recycle();
      return var1;
   }

   public boolean updateRows(Map var1) throws RemoteException {
      Parcel var2 = Parcel.obtain();
      Parcel var3 = Parcel.obtain();
      var2.writeInterfaceToken("android.content.IBulkCursor");
      var2.writeMap(var1);
      IBinder var5 = this.mRemote;
      boolean var4 = false;
      var5.transact(4, var2, var3, 0);
      DatabaseUtils.readExceptionFromParcel(var3);
      if (var3.readInt() == 1) {
         var4 = true;
      }

      var2.recycle();
      var3.recycle();
      return var4;
   }
}
