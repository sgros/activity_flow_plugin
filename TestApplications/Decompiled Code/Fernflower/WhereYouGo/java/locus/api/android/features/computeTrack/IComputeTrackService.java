package locus.api.android.features.computeTrack;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import locus.api.android.objects.ParcelableContainer;

public interface IComputeTrackService extends IInterface {
   ParcelableContainer computeTrack(ParcelableContainer var1) throws RemoteException;

   String getAttribution() throws RemoteException;

   Intent getIntentForSettings() throws RemoteException;

   int getNumOfTransitPoints() throws RemoteException;

   int[] getTrackTypes() throws RemoteException;

   public abstract static class Stub extends Binder implements IComputeTrackService {
      private static final String DESCRIPTOR = "locus.api.android.features.computeTrack.IComputeTrackService";
      static final int TRANSACTION_computeTrack = 4;
      static final int TRANSACTION_getAttribution = 1;
      static final int TRANSACTION_getIntentForSettings = 3;
      static final int TRANSACTION_getNumOfTransitPoints = 5;
      static final int TRANSACTION_getTrackTypes = 2;

      public Stub() {
         this.attachInterface(this, "locus.api.android.features.computeTrack.IComputeTrackService");
      }

      public static IComputeTrackService asInterface(IBinder var0) {
         Object var2;
         if (var0 == null) {
            var2 = null;
         } else {
            IInterface var1 = var0.queryLocalInterface("locus.api.android.features.computeTrack.IComputeTrackService");
            if (var1 != null && var1 instanceof IComputeTrackService) {
               var2 = (IComputeTrackService)var1;
            } else {
               var2 = new IComputeTrackService.Stub.Proxy(var0);
            }
         }

         return (IComputeTrackService)var2;
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int var1, Parcel var2, Parcel var3, int var4) throws RemoteException {
         boolean var5 = true;
         switch(var1) {
         case 1:
            var2.enforceInterface("locus.api.android.features.computeTrack.IComputeTrackService");
            String var9 = this.getAttribution();
            var3.writeNoException();
            var3.writeString(var9);
            break;
         case 2:
            var2.enforceInterface("locus.api.android.features.computeTrack.IComputeTrackService");
            int[] var8 = this.getTrackTypes();
            var3.writeNoException();
            var3.writeIntArray(var8);
            break;
         case 3:
            var2.enforceInterface("locus.api.android.features.computeTrack.IComputeTrackService");
            Intent var7 = this.getIntentForSettings();
            var3.writeNoException();
            if (var7 != null) {
               var3.writeInt(1);
               var7.writeToParcel(var3, 1);
            } else {
               var3.writeInt(0);
            }
            break;
         case 4:
            var2.enforceInterface("locus.api.android.features.computeTrack.IComputeTrackService");
            ParcelableContainer var6;
            if (var2.readInt() != 0) {
               var6 = (ParcelableContainer)ParcelableContainer.CREATOR.createFromParcel(var2);
            } else {
               var6 = null;
            }

            var6 = this.computeTrack(var6);
            var3.writeNoException();
            if (var6 != null) {
               var3.writeInt(1);
               var6.writeToParcel(var3, 1);
            } else {
               var3.writeInt(0);
            }
            break;
         case 5:
            var2.enforceInterface("locus.api.android.features.computeTrack.IComputeTrackService");
            var1 = this.getNumOfTransitPoints();
            var3.writeNoException();
            var3.writeInt(var1);
            break;
         case 1598968902:
            var3.writeString("locus.api.android.features.computeTrack.IComputeTrackService");
            break;
         default:
            var5 = super.onTransact(var1, var2, var3, var4);
         }

         return var5;
      }

      private static class Proxy implements IComputeTrackService {
         private IBinder mRemote;

         Proxy(IBinder var1) {
            this.mRemote = var1;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public ParcelableContainer computeTrack(ParcelableContainer var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            label196: {
               Throwable var10000;
               label200: {
                  boolean var10001;
                  try {
                     var2.writeInterfaceToken("locus.api.android.features.computeTrack.IComputeTrackService");
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
                     this.mRemote.transact(4, var2, var3, 0);
                     var3.readException();
                     if (var3.readInt() != 0) {
                        var1 = (ParcelableContainer)ParcelableContainer.CREATOR.createFromParcel(var3);
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

         public String getAttribution() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            String var3;
            try {
               var1.writeInterfaceToken("locus.api.android.features.computeTrack.IComputeTrackService");
               this.mRemote.transact(1, var1, var2, 0);
               var2.readException();
               var3 = var2.readString();
            } finally {
               var2.recycle();
               var1.recycle();
            }

            return var3;
         }

         public Intent getIntentForSettings() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();
            boolean var5 = false;

            Intent var3;
            label36: {
               try {
                  var5 = true;
                  var1.writeInterfaceToken("locus.api.android.features.computeTrack.IComputeTrackService");
                  this.mRemote.transact(3, var1, var2, 0);
                  var2.readException();
                  if (var2.readInt() != 0) {
                     var3 = (Intent)Intent.CREATOR.createFromParcel(var2);
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

         public String getInterfaceDescriptor() {
            return "locus.api.android.features.computeTrack.IComputeTrackService";
         }

         public int getNumOfTransitPoints() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            int var3;
            try {
               var1.writeInterfaceToken("locus.api.android.features.computeTrack.IComputeTrackService");
               this.mRemote.transact(5, var1, var2, 0);
               var2.readException();
               var3 = var2.readInt();
            } finally {
               var2.recycle();
               var1.recycle();
            }

            return var3;
         }

         public int[] getTrackTypes() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            int[] var3;
            try {
               var1.writeInterfaceToken("locus.api.android.features.computeTrack.IComputeTrackService");
               this.mRemote.transact(2, var1, var2, 0);
               var2.readException();
               var3 = var2.createIntArray();
            } finally {
               var2.recycle();
               var1.recycle();
            }

            return var3;
         }
      }
   }
}
