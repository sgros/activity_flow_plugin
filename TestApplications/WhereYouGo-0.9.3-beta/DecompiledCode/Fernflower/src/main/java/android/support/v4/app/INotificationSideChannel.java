package android.support.v4.app;

import android.app.Notification;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface INotificationSideChannel extends IInterface {
   void cancel(String var1, int var2, String var3) throws RemoteException;

   void cancelAll(String var1) throws RemoteException;

   void notify(String var1, int var2, String var3, Notification var4) throws RemoteException;

   public abstract static class Stub extends Binder implements INotificationSideChannel {
      private static final String DESCRIPTOR = "android.support.v4.app.INotificationSideChannel";
      static final int TRANSACTION_cancel = 2;
      static final int TRANSACTION_cancelAll = 3;
      static final int TRANSACTION_notify = 1;

      public Stub() {
         this.attachInterface(this, "android.support.v4.app.INotificationSideChannel");
      }

      public static INotificationSideChannel asInterface(IBinder var0) {
         Object var2;
         if (var0 == null) {
            var2 = null;
         } else {
            IInterface var1 = var0.queryLocalInterface("android.support.v4.app.INotificationSideChannel");
            if (var1 != null && var1 instanceof INotificationSideChannel) {
               var2 = (INotificationSideChannel)var1;
            } else {
               var2 = new INotificationSideChannel.Stub.Proxy(var0);
            }
         }

         return (INotificationSideChannel)var2;
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int var1, Parcel var2, Parcel var3, int var4) throws RemoteException {
         boolean var5 = true;
         switch(var1) {
         case 1:
            var2.enforceInterface("android.support.v4.app.INotificationSideChannel");
            String var6 = var2.readString();
            var1 = var2.readInt();
            String var8 = var2.readString();
            Notification var7;
            if (var2.readInt() != 0) {
               var7 = (Notification)Notification.CREATOR.createFromParcel(var2);
            } else {
               var7 = null;
            }

            this.notify(var6, var1, var8, var7);
            break;
         case 2:
            var2.enforceInterface("android.support.v4.app.INotificationSideChannel");
            this.cancel(var2.readString(), var2.readInt(), var2.readString());
            break;
         case 3:
            var2.enforceInterface("android.support.v4.app.INotificationSideChannel");
            this.cancelAll(var2.readString());
            break;
         case 1598968902:
            var3.writeString("android.support.v4.app.INotificationSideChannel");
            break;
         default:
            var5 = super.onTransact(var1, var2, var3, var4);
         }

         return var5;
      }

      private static class Proxy implements INotificationSideChannel {
         private IBinder mRemote;

         Proxy(IBinder var1) {
            this.mRemote = var1;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public void cancel(String var1, int var2, String var3) throws RemoteException {
            Parcel var4 = Parcel.obtain();

            try {
               var4.writeInterfaceToken("android.support.v4.app.INotificationSideChannel");
               var4.writeString(var1);
               var4.writeInt(var2);
               var4.writeString(var3);
               this.mRemote.transact(2, var4, (Parcel)null, 1);
            } finally {
               var4.recycle();
            }

         }

         public void cancelAll(String var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();

            try {
               var2.writeInterfaceToken("android.support.v4.app.INotificationSideChannel");
               var2.writeString(var1);
               this.mRemote.transact(3, var2, (Parcel)null, 1);
            } finally {
               var2.recycle();
            }

         }

         public String getInterfaceDescriptor() {
            return "android.support.v4.app.INotificationSideChannel";
         }

         public void notify(String var1, int var2, String var3, Notification var4) throws RemoteException {
            Parcel var5 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var5.writeInterfaceToken("android.support.v4.app.INotificationSideChannel");
                     var5.writeString(var1);
                     var5.writeInt(var2);
                     var5.writeString(var3);
                  } catch (Throwable var25) {
                     var10000 = var25;
                     var10001 = false;
                     break label170;
                  }

                  if (var4 != null) {
                     try {
                        var5.writeInt(1);
                        var4.writeToParcel(var5, 0);
                     } catch (Throwable var24) {
                        var10000 = var24;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var5.writeInt(0);
                     } catch (Throwable var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     this.mRemote.transact(1, var5, (Parcel)null, 1);
                     break label166;
                  } catch (Throwable var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var26 = var10000;
               var5.recycle();
               throw var26;
            }

            var5.recycle();
         }
      }
   }
}
