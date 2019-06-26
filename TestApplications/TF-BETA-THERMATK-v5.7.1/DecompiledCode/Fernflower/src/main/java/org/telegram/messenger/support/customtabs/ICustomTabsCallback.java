package org.telegram.messenger.support.customtabs;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ICustomTabsCallback extends IInterface {
   void extraCallback(String var1, Bundle var2) throws RemoteException;

   void onMessageChannelReady(Bundle var1) throws RemoteException;

   void onNavigationEvent(int var1, Bundle var2) throws RemoteException;

   void onPostMessage(String var1, Bundle var2) throws RemoteException;

   public abstract static class Stub extends Binder implements ICustomTabsCallback {
      private static final String DESCRIPTOR = "android.support.customtabs.ICustomTabsCallback";
      static final int TRANSACTION_extraCallback = 3;
      static final int TRANSACTION_onMessageChannelReady = 4;
      static final int TRANSACTION_onNavigationEvent = 2;
      static final int TRANSACTION_onPostMessage = 5;

      public Stub() {
         this.attachInterface(this, "android.support.customtabs.ICustomTabsCallback");
      }

      public static ICustomTabsCallback asInterface(IBinder var0) {
         if (var0 == null) {
            return null;
         } else {
            IInterface var1 = var0.queryLocalInterface("android.support.customtabs.ICustomTabsCallback");
            Object var2;
            if (var1 != null && var1 instanceof ICustomTabsCallback) {
               var2 = (ICustomTabsCallback)var1;
            } else {
               var2 = new ICustomTabsCallback.Stub.Proxy(var0);
            }

            return (ICustomTabsCallback)var2;
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int var1, Parcel var2, Parcel var3, int var4) throws RemoteException {
         String var5 = null;
         String var6 = null;
         Object var7 = null;
         Bundle var8 = null;
         if (var1 != 2) {
            if (var1 != 3) {
               if (var1 != 4) {
                  if (var1 != 5) {
                     if (var1 != 1598968902) {
                        return super.onTransact(var1, var2, var3, var4);
                     } else {
                        var3.writeString("android.support.customtabs.ICustomTabsCallback");
                        return true;
                     }
                  } else {
                     var2.enforceInterface("android.support.customtabs.ICustomTabsCallback");
                     var6 = var2.readString();
                     if (var2.readInt() != 0) {
                        var8 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
                     }

                     this.onPostMessage(var6, var8);
                     var3.writeNoException();
                     return true;
                  }
               } else {
                  var2.enforceInterface("android.support.customtabs.ICustomTabsCallback");
                  var8 = var5;
                  if (var2.readInt() != 0) {
                     var8 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
                  }

                  this.onMessageChannelReady(var8);
                  var3.writeNoException();
                  return true;
               }
            } else {
               var2.enforceInterface("android.support.customtabs.ICustomTabsCallback");
               var5 = var2.readString();
               var8 = var6;
               if (var2.readInt() != 0) {
                  var8 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
               }

               this.extraCallback(var5, var8);
               var3.writeNoException();
               return true;
            }
         } else {
            var2.enforceInterface("android.support.customtabs.ICustomTabsCallback");
            var1 = var2.readInt();
            var8 = (Bundle)var7;
            if (var2.readInt() != 0) {
               var8 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
            }

            this.onNavigationEvent(var1, var8);
            var3.writeNoException();
            return true;
         }
      }

      private static class Proxy implements ICustomTabsCallback {
         private IBinder mRemote;

         Proxy(IBinder var1) {
            this.mRemote = var1;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public void extraCallback(String var1, Bundle var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.customtabs.ICustomTabsCallback");
                     var3.writeString(var1);
                  } catch (Throwable var24) {
                     var10000 = var24;
                     var10001 = false;
                     break label170;
                  }

                  if (var2 != null) {
                     try {
                        var3.writeInt(1);
                        var2.writeToParcel(var3, 0);
                     } catch (Throwable var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     this.mRemote.transact(3, var3, var4, 0);
                     var4.readException();
                     break label166;
                  } catch (Throwable var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var25 = var10000;
               var4.recycle();
               var3.recycle();
               throw var25;
            }

            var4.recycle();
            var3.recycle();
         }

         public String getInterfaceDescriptor() {
            return "android.support.customtabs.ICustomTabsCallback";
         }

         public void onMessageChannelReady(Bundle var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var2.writeInterfaceToken("android.support.customtabs.ICustomTabsCallback");
                  } catch (Throwable var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label170;
                  }

                  if (var1 != null) {
                     try {
                        var2.writeInt(1);
                        var1.writeToParcel(var2, 0);
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var2.writeInt(0);
                     } catch (Throwable var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     this.mRemote.transact(4, var2, var3, 0);
                     var3.readException();
                     break label166;
                  } catch (Throwable var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var24 = var10000;
               var3.recycle();
               var2.recycle();
               throw var24;
            }

            var3.recycle();
            var2.recycle();
         }

         public void onNavigationEvent(int var1, Bundle var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.customtabs.ICustomTabsCallback");
                     var3.writeInt(var1);
                  } catch (Throwable var24) {
                     var10000 = var24;
                     var10001 = false;
                     break label170;
                  }

                  if (var2 != null) {
                     try {
                        var3.writeInt(1);
                        var2.writeToParcel(var3, 0);
                     } catch (Throwable var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     this.mRemote.transact(2, var3, var4, 0);
                     var4.readException();
                     break label166;
                  } catch (Throwable var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var25 = var10000;
               var4.recycle();
               var3.recycle();
               throw var25;
            }

            var4.recycle();
            var3.recycle();
         }

         public void onPostMessage(String var1, Bundle var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.customtabs.ICustomTabsCallback");
                     var3.writeString(var1);
                  } catch (Throwable var24) {
                     var10000 = var24;
                     var10001 = false;
                     break label170;
                  }

                  if (var2 != null) {
                     try {
                        var3.writeInt(1);
                        var2.writeToParcel(var3, 0);
                     } catch (Throwable var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     this.mRemote.transact(5, var3, var4, 0);
                     var4.readException();
                     break label166;
                  } catch (Throwable var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var25 = var10000;
               var4.recycle();
               var3.recycle();
               throw var25;
            }

            var4.recycle();
            var3.recycle();
         }
      }
   }
}
