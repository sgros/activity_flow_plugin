package locus.api.android.features.mapProvider;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IMapTileService extends IInterface {
   MapDataContainer getMapConfigs() throws RemoteException;

   MapDataContainer getMapTile(MapDataContainer var1) throws RemoteException;

   public abstract static class Stub extends Binder implements IMapTileService {
      private static final String DESCRIPTOR = "locus.api.android.features.mapProvider.IMapTileService";
      static final int TRANSACTION_getMapConfigs = 1;
      static final int TRANSACTION_getMapTile = 2;

      public Stub() {
         this.attachInterface(this, "locus.api.android.features.mapProvider.IMapTileService");
      }

      public static IMapTileService asInterface(IBinder var0) {
         Object var2;
         if (var0 == null) {
            var2 = null;
         } else {
            IInterface var1 = var0.queryLocalInterface("locus.api.android.features.mapProvider.IMapTileService");
            if (var1 != null && var1 instanceof IMapTileService) {
               var2 = (IMapTileService)var1;
            } else {
               var2 = new IMapTileService.Stub.Proxy(var0);
            }
         }

         return (IMapTileService)var2;
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int var1, Parcel var2, Parcel var3, int var4) throws RemoteException {
         boolean var5 = true;
         MapDataContainer var6;
         switch(var1) {
         case 1:
            var2.enforceInterface("locus.api.android.features.mapProvider.IMapTileService");
            var6 = this.getMapConfigs();
            var3.writeNoException();
            if (var6 != null) {
               var3.writeInt(1);
               var6.writeToParcel(var3, 1);
            } else {
               var3.writeInt(0);
            }
            break;
         case 2:
            var2.enforceInterface("locus.api.android.features.mapProvider.IMapTileService");
            if (var2.readInt() != 0) {
               var6 = (MapDataContainer)MapDataContainer.CREATOR.createFromParcel(var2);
            } else {
               var6 = null;
            }

            var6 = this.getMapTile(var6);
            var3.writeNoException();
            if (var6 != null) {
               var3.writeInt(1);
               var6.writeToParcel(var3, 1);
            } else {
               var3.writeInt(0);
            }
            break;
         case 1598968902:
            var3.writeString("locus.api.android.features.mapProvider.IMapTileService");
            break;
         default:
            var5 = super.onTransact(var1, var2, var3, var4);
         }

         return var5;
      }

      private static class Proxy implements IMapTileService {
         private IBinder mRemote;

         Proxy(IBinder var1) {
            this.mRemote = var1;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return "locus.api.android.features.mapProvider.IMapTileService";
         }

         public MapDataContainer getMapConfigs() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();
            boolean var5 = false;

            MapDataContainer var3;
            label36: {
               try {
                  var5 = true;
                  var1.writeInterfaceToken("locus.api.android.features.mapProvider.IMapTileService");
                  this.mRemote.transact(1, var1, var2, 0);
                  var2.readException();
                  if (var2.readInt() != 0) {
                     var3 = (MapDataContainer)MapDataContainer.CREATOR.createFromParcel(var2);
                     var5 = false;
                     break label36;
                  }

                  var5 = false;
               } finally {
                  if (var5) {
                     var2.recycle();
                     var1.recycle();
                  }
               }

               var3 = null;
            }

            var2.recycle();
            var1.recycle();
            return var3;
         }

         public MapDataContainer getMapTile(MapDataContainer var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            label196: {
               Throwable var10000;
               label200: {
                  boolean var10001;
                  try {
                     var2.writeInterfaceToken("locus.api.android.features.mapProvider.IMapTileService");
                  } catch (Throwable var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label200;
                  }

                  if (var1 != null) {
                     try {
                        var2.writeInt(1);
                        var1.writeToParcel(var2, 0);
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label200;
                     }
                  } else {
                     try {
                        var2.writeInt(0);
                     } catch (Throwable var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label200;
                     }
                  }

                  try {
                     this.mRemote.transact(2, var2, var3, 0);
                     var3.readException();
                     if (var3.readInt() != 0) {
                        var1 = (MapDataContainer)MapDataContainer.CREATOR.createFromParcel(var3);
                        break label196;
                     }
                  } catch (Throwable var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label200;
                  }

                  var1 = null;
                  break label196;
               }

               Throwable var24 = var10000;
               var3.recycle();
               var2.recycle();
               throw var24;
            }

            var3.recycle();
            var2.recycle();
            return var1;
         }
      }
   }
}
