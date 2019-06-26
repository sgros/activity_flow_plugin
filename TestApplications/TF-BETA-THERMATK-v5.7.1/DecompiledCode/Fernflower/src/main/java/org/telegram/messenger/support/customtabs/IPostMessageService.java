package org.telegram.messenger.support.customtabs;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IPostMessageService extends IInterface {
   void onMessageChannelReady(ICustomTabsCallback var1, Bundle var2) throws RemoteException;

   void onPostMessage(ICustomTabsCallback var1, String var2, Bundle var3) throws RemoteException;

   public abstract static class Stub extends Binder implements IPostMessageService {
      private static final String DESCRIPTOR = "android.support.customtabs.IPostMessageService";
      static final int TRANSACTION_onMessageChannelReady = 2;
      static final int TRANSACTION_onPostMessage = 3;

      public Stub() {
         this.attachInterface(this, "android.support.customtabs.IPostMessageService");
      }

      public static IPostMessageService asInterface(IBinder var0) {
         if (var0 == null) {
            return null;
         } else {
            IInterface var1 = var0.queryLocalInterface("android.support.customtabs.IPostMessageService");
            Object var2;
            if (var1 != null && var1 instanceof IPostMessageService) {
               var2 = (IPostMessageService)var1;
            } else {
               var2 = new IPostMessageService.Stub.Proxy(var0);
            }

            return (IPostMessageService)var2;
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int var1, Parcel var2, Parcel var3, int var4) throws RemoteException {
         String var5 = null;
         Bundle var6 = null;
         ICustomTabsCallback var7;
         if (var1 != 2) {
            if (var1 != 3) {
               if (var1 != 1598968902) {
                  return super.onTransact(var1, var2, var3, var4);
               } else {
                  var3.writeString("android.support.customtabs.IPostMessageService");
                  return true;
               }
            } else {
               var2.enforceInterface("android.support.customtabs.IPostMessageService");
               var7 = ICustomTabsCallback.Stub.asInterface(var2.readStrongBinder());
               var5 = var2.readString();
               if (var2.readInt() != 0) {
                  var6 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
               }

               this.onPostMessage(var7, var5, var6);
               var3.writeNoException();
               return true;
            }
         } else {
            var2.enforceInterface("android.support.customtabs.IPostMessageService");
            var7 = ICustomTabsCallback.Stub.asInterface(var2.readStrongBinder());
            var6 = var5;
            if (var2.readInt() != 0) {
               var6 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
            }

            this.onMessageChannelReady(var7, var6);
            var3.writeNoException();
            return true;
         }
      }

      private static class Proxy implements IPostMessageService {
         private IBinder mRemote;

         Proxy(IBinder var1) {
            this.mRemote = var1;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return "android.support.customtabs.IPostMessageService";
         }

         public void onMessageChannelReady(ICustomTabsCallback var1, Bundle var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            label339: {
               Throwable var10000;
               label343: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.customtabs.IPostMessageService");
                  } catch (Throwable var46) {
                     var10000 = var46;
                     var10001 = false;
                     break label343;
                  }

                  IBinder var47;
                  if (var1 != null) {
                     try {
                        var47 = var1.asBinder();
                     } catch (Throwable var45) {
                        var10000 = var45;
                        var10001 = false;
                        break label343;
                     }
                  } else {
                     var47 = null;
                  }

                  try {
                     var3.writeStrongBinder(var47);
                  } catch (Throwable var44) {
                     var10000 = var44;
                     var10001 = false;
                     break label343;
                  }

                  if (var2 != null) {
                     try {
                        var3.writeInt(1);
                        var2.writeToParcel(var3, 0);
                     } catch (Throwable var43) {
                        var10000 = var43;
                        var10001 = false;
                        break label343;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var42) {
                        var10000 = var42;
                        var10001 = false;
                        break label343;
                     }
                  }

                  label321:
                  try {
                     this.mRemote.transact(2, var3, var4, 0);
                     var4.readException();
                     break label339;
                  } catch (Throwable var41) {
                     var10000 = var41;
                     var10001 = false;
                     break label321;
                  }
               }

               Throwable var48 = var10000;
               var4.recycle();
               var3.recycle();
               throw var48;
            }

            var4.recycle();
            var3.recycle();
         }

         public void onPostMessage(ICustomTabsCallback var1, String var2, Bundle var3) throws RemoteException {
            Parcel var4 = Parcel.obtain();
            Parcel var5 = Parcel.obtain();

            label339: {
               Throwable var10000;
               label343: {
                  boolean var10001;
                  try {
                     var4.writeInterfaceToken("android.support.customtabs.IPostMessageService");
                  } catch (Throwable var47) {
                     var10000 = var47;
                     var10001 = false;
                     break label343;
                  }

                  IBinder var48;
                  if (var1 != null) {
                     try {
                        var48 = var1.asBinder();
                     } catch (Throwable var46) {
                        var10000 = var46;
                        var10001 = false;
                        break label343;
                     }
                  } else {
                     var48 = null;
                  }

                  try {
                     var4.writeStrongBinder(var48);
                     var4.writeString(var2);
                  } catch (Throwable var45) {
                     var10000 = var45;
                     var10001 = false;
                     break label343;
                  }

                  if (var3 != null) {
                     try {
                        var4.writeInt(1);
                        var3.writeToParcel(var4, 0);
                     } catch (Throwable var44) {
                        var10000 = var44;
                        var10001 = false;
                        break label343;
                     }
                  } else {
                     try {
                        var4.writeInt(0);
                     } catch (Throwable var43) {
                        var10000 = var43;
                        var10001 = false;
                        break label343;
                     }
                  }

                  label321:
                  try {
                     this.mRemote.transact(3, var4, var5, 0);
                     var5.readException();
                     break label339;
                  } catch (Throwable var42) {
                     var10000 = var42;
                     var10001 = false;
                     break label321;
                  }
               }

               Throwable var49 = var10000;
               var5.recycle();
               var4.recycle();
               throw var49;
            }

            var5.recycle();
            var4.recycle();
         }
      }
   }
}
