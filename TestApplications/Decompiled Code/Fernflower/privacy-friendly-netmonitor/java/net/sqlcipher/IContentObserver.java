package net.sqlcipher;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IContentObserver extends IInterface {
   void onChange(boolean var1) throws RemoteException;

   public abstract static class Stub extends Binder implements IContentObserver {
      private static final String DESCRIPTOR = "net.sqlcipher.IContentObserver";
      static final int TRANSACTION_onChange = 1;

      public Stub() {
         this.attachInterface(this, "net.sqlcipher.IContentObserver");
      }

      public static IContentObserver asInterface(IBinder var0) {
         if (var0 == null) {
            return null;
         } else {
            IInterface var1 = var0.queryLocalInterface("net.sqlcipher.IContentObserver");
            return (IContentObserver)(var1 != null && var1 instanceof IContentObserver ? (IContentObserver)var1 : new IContentObserver.Stub.Proxy(var0));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int var1, Parcel var2, Parcel var3, int var4) throws RemoteException {
         if (var1 != 1) {
            if (var1 != 1598968902) {
               return super.onTransact(var1, var2, var3, var4);
            } else {
               var3.writeString("net.sqlcipher.IContentObserver");
               return true;
            }
         } else {
            var2.enforceInterface("net.sqlcipher.IContentObserver");
            boolean var5;
            if (var2.readInt() != 0) {
               var5 = true;
            } else {
               var5 = false;
            }

            this.onChange(var5);
            return true;
         }
      }

      private static class Proxy implements IContentObserver {
         private IBinder mRemote;

         Proxy(IBinder var1) {
            this.mRemote = var1;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return "net.sqlcipher.IContentObserver";
         }

         public void onChange(boolean var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();

            try {
               var2.writeInterfaceToken("net.sqlcipher.IContentObserver");
               var2.writeInt(var1);
               this.mRemote.transact(1, var2, (Parcel)null, 1);
            } finally {
               var2.recycle();
            }

         }
      }
   }
}
