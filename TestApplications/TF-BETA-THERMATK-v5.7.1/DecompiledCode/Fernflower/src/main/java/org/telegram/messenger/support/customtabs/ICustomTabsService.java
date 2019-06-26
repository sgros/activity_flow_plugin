package org.telegram.messenger.support.customtabs;

import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface ICustomTabsService extends IInterface {
   Bundle extraCommand(String var1, Bundle var2) throws RemoteException;

   boolean mayLaunchUrl(ICustomTabsCallback var1, Uri var2, Bundle var3, List var4) throws RemoteException;

   boolean newSession(ICustomTabsCallback var1) throws RemoteException;

   int postMessage(ICustomTabsCallback var1, String var2, Bundle var3) throws RemoteException;

   boolean requestPostMessageChannel(ICustomTabsCallback var1, Uri var2) throws RemoteException;

   boolean updateVisuals(ICustomTabsCallback var1, Bundle var2) throws RemoteException;

   boolean warmup(long var1) throws RemoteException;

   public abstract static class Stub extends Binder implements ICustomTabsService {
      private static final String DESCRIPTOR = "android.support.customtabs.ICustomTabsService";
      static final int TRANSACTION_extraCommand = 5;
      static final int TRANSACTION_mayLaunchUrl = 4;
      static final int TRANSACTION_newSession = 3;
      static final int TRANSACTION_postMessage = 8;
      static final int TRANSACTION_requestPostMessageChannel = 7;
      static final int TRANSACTION_updateVisuals = 6;
      static final int TRANSACTION_warmup = 2;

      public Stub() {
         this.attachInterface(this, "android.support.customtabs.ICustomTabsService");
      }

      public static ICustomTabsService asInterface(IBinder var0) {
         if (var0 == null) {
            return null;
         } else {
            IInterface var1 = var0.queryLocalInterface("android.support.customtabs.ICustomTabsService");
            Object var2;
            if (var1 != null && var1 instanceof ICustomTabsService) {
               var2 = (ICustomTabsService)var1;
            } else {
               var2 = new ICustomTabsService.Stub.Proxy(var0);
            }

            return (ICustomTabsService)var2;
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int var1, Parcel var2, Parcel var3, int var4) throws RemoteException {
         if (var1 != 1598968902) {
            String var5 = null;
            Object var6 = null;
            Object var7 = null;
            Bundle var8 = null;
            Bundle var9 = null;
            byte var10 = 0;
            byte var11 = 0;
            byte var12 = 0;
            byte var13 = 0;
            byte var14 = 0;
            boolean var15;
            byte var16;
            ICustomTabsCallback var19;
            Uri var21;
            switch(var1) {
            case 2:
               var2.enforceInterface("android.support.customtabs.ICustomTabsService");
               var15 = this.warmup(var2.readLong());
               var3.writeNoException();
               var16 = var13;
               if (var15) {
                  var16 = 1;
               }

               var3.writeInt(var16);
               return true;
            case 3:
               var2.enforceInterface("android.support.customtabs.ICustomTabsService");
               var15 = this.newSession(ICustomTabsCallback.Stub.asInterface(var2.readStrongBinder()));
               var3.writeNoException();
               var16 = var12;
               if (var15) {
                  var16 = 1;
               }

               var3.writeInt(var16);
               return true;
            case 4:
               var2.enforceInterface("android.support.customtabs.ICustomTabsService");
               ICustomTabsCallback var18 = ICustomTabsCallback.Stub.asInterface(var2.readStrongBinder());
               if (var2.readInt() != 0) {
                  var21 = (Uri)Uri.CREATOR.createFromParcel(var2);
               } else {
                  var21 = null;
               }

               if (var2.readInt() != 0) {
                  var8 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
               }

               var15 = this.mayLaunchUrl(var18, var21, var8, var2.createTypedArrayList(Bundle.CREATOR));
               var3.writeNoException();
               var16 = var11;
               if (var15) {
                  var16 = 1;
               }

               var3.writeInt(var16);
               return true;
            case 5:
               var2.enforceInterface("android.support.customtabs.ICustomTabsService");
               String var20 = var2.readString();
               var9 = (Bundle)var7;
               if (var2.readInt() != 0) {
                  var9 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
               }

               Bundle var17 = this.extraCommand(var20, var9);
               var3.writeNoException();
               if (var17 != null) {
                  var3.writeInt(1);
                  var17.writeToParcel(var3, 1);
               } else {
                  var3.writeInt(0);
               }

               return true;
            case 6:
               var2.enforceInterface("android.support.customtabs.ICustomTabsService");
               var19 = ICustomTabsCallback.Stub.asInterface(var2.readStrongBinder());
               var9 = (Bundle)var6;
               if (var2.readInt() != 0) {
                  var9 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
               }

               var15 = this.updateVisuals(var19, var9);
               var3.writeNoException();
               var16 = var10;
               if (var15) {
                  var16 = 1;
               }

               var3.writeInt(var16);
               return true;
            case 7:
               var2.enforceInterface("android.support.customtabs.ICustomTabsService");
               var19 = ICustomTabsCallback.Stub.asInterface(var2.readStrongBinder());
               var21 = var5;
               if (var2.readInt() != 0) {
                  var21 = (Uri)Uri.CREATOR.createFromParcel(var2);
               }

               var15 = this.requestPostMessageChannel(var19, var21);
               var3.writeNoException();
               var16 = var14;
               if (var15) {
                  var16 = 1;
               }

               var3.writeInt(var16);
               return true;
            case 8:
               var2.enforceInterface("android.support.customtabs.ICustomTabsService");
               var19 = ICustomTabsCallback.Stub.asInterface(var2.readStrongBinder());
               var5 = var2.readString();
               if (var2.readInt() != 0) {
                  var9 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
               }

               var1 = this.postMessage(var19, var5, var9);
               var3.writeNoException();
               var3.writeInt(var1);
               return true;
            default:
               return super.onTransact(var1, var2, var3, var4);
            }
         } else {
            var3.writeString("android.support.customtabs.ICustomTabsService");
            return true;
         }
      }

      private static class Proxy implements ICustomTabsService {
         private IBinder mRemote;

         Proxy(IBinder var1) {
            this.mRemote = var1;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public Bundle extraCommand(String var1, Bundle var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            Bundle var26;
            label197: {
               Throwable var10000;
               label201: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.customtabs.ICustomTabsService");
                     var3.writeString(var1);
                  } catch (Throwable var24) {
                     var10000 = var24;
                     var10001 = false;
                     break label201;
                  }

                  if (var2 != null) {
                     try {
                        var3.writeInt(1);
                        var2.writeToParcel(var3, 0);
                     } catch (Throwable var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label201;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label201;
                     }
                  }

                  try {
                     this.mRemote.transact(5, var3, var4, 0);
                     var4.readException();
                     if (var4.readInt() != 0) {
                        var26 = (Bundle)Bundle.CREATOR.createFromParcel(var4);
                        break label197;
                     }
                  } catch (Throwable var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label201;
                  }

                  var26 = null;
                  break label197;
               }

               Throwable var25 = var10000;
               var4.recycle();
               var3.recycle();
               throw var25;
            }

            var4.recycle();
            var3.recycle();
            return var26;
         }

         public String getInterfaceDescriptor() {
            return "android.support.customtabs.ICustomTabsService";
         }

         public boolean mayLaunchUrl(ICustomTabsCallback var1, Uri var2, Bundle var3, List var4) throws RemoteException {
            Parcel var5 = Parcel.obtain();
            Parcel var6 = Parcel.obtain();

            boolean var7;
            int var8;
            label565: {
               Throwable var10000;
               label569: {
                  boolean var10001;
                  try {
                     var5.writeInterfaceToken("android.support.customtabs.ICustomTabsService");
                  } catch (Throwable var80) {
                     var10000 = var80;
                     var10001 = false;
                     break label569;
                  }

                  IBinder var81;
                  if (var1 != null) {
                     try {
                        var81 = var1.asBinder();
                     } catch (Throwable var79) {
                        var10000 = var79;
                        var10001 = false;
                        break label569;
                     }
                  } else {
                     var81 = null;
                  }

                  try {
                     var5.writeStrongBinder(var81);
                  } catch (Throwable var78) {
                     var10000 = var78;
                     var10001 = false;
                     break label569;
                  }

                  var7 = true;
                  if (var2 != null) {
                     try {
                        var5.writeInt(1);
                        var2.writeToParcel(var5, 0);
                     } catch (Throwable var77) {
                        var10000 = var77;
                        var10001 = false;
                        break label569;
                     }
                  } else {
                     try {
                        var5.writeInt(0);
                     } catch (Throwable var76) {
                        var10000 = var76;
                        var10001 = false;
                        break label569;
                     }
                  }

                  if (var3 != null) {
                     try {
                        var5.writeInt(1);
                        var3.writeToParcel(var5, 0);
                     } catch (Throwable var75) {
                        var10000 = var75;
                        var10001 = false;
                        break label569;
                     }
                  } else {
                     try {
                        var5.writeInt(0);
                     } catch (Throwable var74) {
                        var10000 = var74;
                        var10001 = false;
                        break label569;
                     }
                  }

                  label542:
                  try {
                     var5.writeTypedList(var4);
                     this.mRemote.transact(4, var5, var6, 0);
                     var6.readException();
                     var8 = var6.readInt();
                     break label565;
                  } catch (Throwable var73) {
                     var10000 = var73;
                     var10001 = false;
                     break label542;
                  }
               }

               Throwable var82 = var10000;
               var6.recycle();
               var5.recycle();
               throw var82;
            }

            if (var8 == 0) {
               var7 = false;
            }

            var6.recycle();
            var5.recycle();
            return var7;
         }

         public boolean newSession(ICustomTabsCallback var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            boolean var4;
            int var5;
            label211: {
               Throwable var10000;
               label215: {
                  boolean var10001;
                  try {
                     var2.writeInterfaceToken("android.support.customtabs.ICustomTabsService");
                  } catch (Throwable var25) {
                     var10000 = var25;
                     var10001 = false;
                     break label215;
                  }

                  IBinder var26;
                  if (var1 != null) {
                     try {
                        var26 = var1.asBinder();
                     } catch (Throwable var24) {
                        var10000 = var24;
                        var10001 = false;
                        break label215;
                     }
                  } else {
                     var26 = null;
                  }

                  try {
                     var2.writeStrongBinder(var26);
                     var26 = this.mRemote;
                  } catch (Throwable var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label215;
                  }

                  var4 = false;

                  label198:
                  try {
                     var26.transact(3, var2, var3, 0);
                     var3.readException();
                     var5 = var3.readInt();
                     break label211;
                  } catch (Throwable var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label198;
                  }
               }

               Throwable var27 = var10000;
               var3.recycle();
               var2.recycle();
               throw var27;
            }

            if (var5 != 0) {
               var4 = true;
            }

            var3.recycle();
            var2.recycle();
            return var4;
         }

         public int postMessage(ICustomTabsCallback var1, String var2, Bundle var3) throws RemoteException {
            Parcel var4 = Parcel.obtain();
            Parcel var5 = Parcel.obtain();

            int var6;
            label339: {
               Throwable var10000;
               label343: {
                  boolean var10001;
                  try {
                     var4.writeInterfaceToken("android.support.customtabs.ICustomTabsService");
                  } catch (Throwable var48) {
                     var10000 = var48;
                     var10001 = false;
                     break label343;
                  }

                  IBinder var49;
                  if (var1 != null) {
                     try {
                        var49 = var1.asBinder();
                     } catch (Throwable var47) {
                        var10000 = var47;
                        var10001 = false;
                        break label343;
                     }
                  } else {
                     var49 = null;
                  }

                  try {
                     var4.writeStrongBinder(var49);
                     var4.writeString(var2);
                  } catch (Throwable var46) {
                     var10000 = var46;
                     var10001 = false;
                     break label343;
                  }

                  if (var3 != null) {
                     try {
                        var4.writeInt(1);
                        var3.writeToParcel(var4, 0);
                     } catch (Throwable var45) {
                        var10000 = var45;
                        var10001 = false;
                        break label343;
                     }
                  } else {
                     try {
                        var4.writeInt(0);
                     } catch (Throwable var44) {
                        var10000 = var44;
                        var10001 = false;
                        break label343;
                     }
                  }

                  label321:
                  try {
                     this.mRemote.transact(8, var4, var5, 0);
                     var5.readException();
                     var6 = var5.readInt();
                     break label339;
                  } catch (Throwable var43) {
                     var10000 = var43;
                     var10001 = false;
                     break label321;
                  }
               }

               Throwable var50 = var10000;
               var5.recycle();
               var4.recycle();
               throw var50;
            }

            var5.recycle();
            var4.recycle();
            return var6;
         }

         public boolean requestPostMessageChannel(ICustomTabsCallback var1, Uri var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            boolean var5;
            int var6;
            label368: {
               Throwable var10000;
               label372: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.customtabs.ICustomTabsService");
                  } catch (Throwable var48) {
                     var10000 = var48;
                     var10001 = false;
                     break label372;
                  }

                  IBinder var49;
                  if (var1 != null) {
                     try {
                        var49 = var1.asBinder();
                     } catch (Throwable var47) {
                        var10000 = var47;
                        var10001 = false;
                        break label372;
                     }
                  } else {
                     var49 = null;
                  }

                  try {
                     var3.writeStrongBinder(var49);
                  } catch (Throwable var46) {
                     var10000 = var46;
                     var10001 = false;
                     break label372;
                  }

                  var5 = true;
                  if (var2 != null) {
                     try {
                        var3.writeInt(1);
                        var2.writeToParcel(var3, 0);
                     } catch (Throwable var45) {
                        var10000 = var45;
                        var10001 = false;
                        break label372;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var44) {
                        var10000 = var44;
                        var10001 = false;
                        break label372;
                     }
                  }

                  label350:
                  try {
                     this.mRemote.transact(7, var3, var4, 0);
                     var4.readException();
                     var6 = var4.readInt();
                     break label368;
                  } catch (Throwable var43) {
                     var10000 = var43;
                     var10001 = false;
                     break label350;
                  }
               }

               Throwable var50 = var10000;
               var4.recycle();
               var3.recycle();
               throw var50;
            }

            if (var6 == 0) {
               var5 = false;
            }

            var4.recycle();
            var3.recycle();
            return var5;
         }

         public boolean updateVisuals(ICustomTabsCallback var1, Bundle var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            boolean var5;
            int var6;
            label368: {
               Throwable var10000;
               label372: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.customtabs.ICustomTabsService");
                  } catch (Throwable var48) {
                     var10000 = var48;
                     var10001 = false;
                     break label372;
                  }

                  IBinder var49;
                  if (var1 != null) {
                     try {
                        var49 = var1.asBinder();
                     } catch (Throwable var47) {
                        var10000 = var47;
                        var10001 = false;
                        break label372;
                     }
                  } else {
                     var49 = null;
                  }

                  try {
                     var3.writeStrongBinder(var49);
                  } catch (Throwable var46) {
                     var10000 = var46;
                     var10001 = false;
                     break label372;
                  }

                  var5 = true;
                  if (var2 != null) {
                     try {
                        var3.writeInt(1);
                        var2.writeToParcel(var3, 0);
                     } catch (Throwable var45) {
                        var10000 = var45;
                        var10001 = false;
                        break label372;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var44) {
                        var10000 = var44;
                        var10001 = false;
                        break label372;
                     }
                  }

                  label350:
                  try {
                     this.mRemote.transact(6, var3, var4, 0);
                     var4.readException();
                     var6 = var4.readInt();
                     break label368;
                  } catch (Throwable var43) {
                     var10000 = var43;
                     var10001 = false;
                     break label350;
                  }
               }

               Throwable var50 = var10000;
               var4.recycle();
               var3.recycle();
               throw var50;
            }

            if (var6 == 0) {
               var5 = false;
            }

            var4.recycle();
            var3.recycle();
            return var5;
         }

         public boolean warmup(long var1) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            boolean var6;
            int var7;
            label80: {
               Throwable var10000;
               label84: {
                  boolean var10001;
                  IBinder var5;
                  try {
                     var3.writeInterfaceToken("android.support.customtabs.ICustomTabsService");
                     var3.writeLong(var1);
                     var5 = this.mRemote;
                  } catch (Throwable var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label84;
                  }

                  var6 = false;

                  label75:
                  try {
                     var5.transact(2, var3, var4, 0);
                     var4.readException();
                     var7 = var4.readInt();
                     break label80;
                  } catch (Throwable var12) {
                     var10000 = var12;
                     var10001 = false;
                     break label75;
                  }
               }

               Throwable var14 = var10000;
               var4.recycle();
               var3.recycle();
               throw var14;
            }

            if (var7 != 0) {
               var6 = true;
            }

            var4.recycle();
            var3.recycle();
            return var6;
         }
      }
   }
}
