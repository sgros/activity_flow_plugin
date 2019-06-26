package android.support.v4.media.session;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v4.media.MediaMetadataCompat;
import android.text.TextUtils;
import java.util.List;

public interface IMediaControllerCallback extends IInterface {
   void onEvent(String var1, Bundle var2) throws RemoteException;

   void onExtrasChanged(Bundle var1) throws RemoteException;

   void onMetadataChanged(MediaMetadataCompat var1) throws RemoteException;

   void onPlaybackStateChanged(PlaybackStateCompat var1) throws RemoteException;

   void onQueueChanged(List var1) throws RemoteException;

   void onQueueTitleChanged(CharSequence var1) throws RemoteException;

   void onRepeatModeChanged(int var1) throws RemoteException;

   void onSessionDestroyed() throws RemoteException;

   void onShuffleModeChanged(boolean var1) throws RemoteException;

   void onVolumeInfoChanged(ParcelableVolumeInfo var1) throws RemoteException;

   public abstract static class Stub extends Binder implements IMediaControllerCallback {
      private static final String DESCRIPTOR = "android.support.v4.media.session.IMediaControllerCallback";
      static final int TRANSACTION_onEvent = 1;
      static final int TRANSACTION_onExtrasChanged = 7;
      static final int TRANSACTION_onMetadataChanged = 4;
      static final int TRANSACTION_onPlaybackStateChanged = 3;
      static final int TRANSACTION_onQueueChanged = 5;
      static final int TRANSACTION_onQueueTitleChanged = 6;
      static final int TRANSACTION_onRepeatModeChanged = 9;
      static final int TRANSACTION_onSessionDestroyed = 2;
      static final int TRANSACTION_onShuffleModeChanged = 10;
      static final int TRANSACTION_onVolumeInfoChanged = 8;

      public Stub() {
         this.attachInterface(this, "android.support.v4.media.session.IMediaControllerCallback");
      }

      public static IMediaControllerCallback asInterface(IBinder var0) {
         Object var2;
         if (var0 == null) {
            var2 = null;
         } else {
            IInterface var1 = var0.queryLocalInterface("android.support.v4.media.session.IMediaControllerCallback");
            if (var1 != null && var1 instanceof IMediaControllerCallback) {
               var2 = (IMediaControllerCallback)var1;
            } else {
               var2 = new IMediaControllerCallback.Stub.Proxy(var0);
            }
         }

         return (IMediaControllerCallback)var2;
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int var1, Parcel var2, Parcel var3, int var4) throws RemoteException {
         boolean var5 = true;
         boolean var6;
         Bundle var9;
         switch(var1) {
         case 1:
            var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
            String var8 = var2.readString();
            if (var2.readInt() != 0) {
               var9 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
            } else {
               var9 = null;
            }

            this.onEvent(var8, var9);
            var6 = var5;
            break;
         case 2:
            var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
            this.onSessionDestroyed();
            var6 = var5;
            break;
         case 3:
            var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
            PlaybackStateCompat var12;
            if (var2.readInt() != 0) {
               var12 = (PlaybackStateCompat)PlaybackStateCompat.CREATOR.createFromParcel(var2);
            } else {
               var12 = null;
            }

            this.onPlaybackStateChanged(var12);
            var6 = var5;
            break;
         case 4:
            var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
            MediaMetadataCompat var11;
            if (var2.readInt() != 0) {
               var11 = (MediaMetadataCompat)MediaMetadataCompat.CREATOR.createFromParcel(var2);
            } else {
               var11 = null;
            }

            this.onMetadataChanged(var11);
            var6 = var5;
            break;
         case 5:
            var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
            this.onQueueChanged(var2.createTypedArrayList(MediaSessionCompat.QueueItem.CREATOR));
            var6 = var5;
            break;
         case 6:
            var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
            CharSequence var10;
            if (var2.readInt() != 0) {
               var10 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(var2);
            } else {
               var10 = null;
            }

            this.onQueueTitleChanged(var10);
            var6 = var5;
            break;
         case 7:
            var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
            if (var2.readInt() != 0) {
               var9 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
            } else {
               var9 = null;
            }

            this.onExtrasChanged(var9);
            var6 = var5;
            break;
         case 8:
            var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
            ParcelableVolumeInfo var7;
            if (var2.readInt() != 0) {
               var7 = (ParcelableVolumeInfo)ParcelableVolumeInfo.CREATOR.createFromParcel(var2);
            } else {
               var7 = null;
            }

            this.onVolumeInfoChanged(var7);
            var6 = var5;
            break;
         case 9:
            var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
            this.onRepeatModeChanged(var2.readInt());
            var6 = var5;
            break;
         case 10:
            var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
            if (var2.readInt() != 0) {
               var6 = true;
            } else {
               var6 = false;
            }

            this.onShuffleModeChanged(var6);
            var6 = var5;
            break;
         case 1598968902:
            var3.writeString("android.support.v4.media.session.IMediaControllerCallback");
            var6 = var5;
            break;
         default:
            var6 = super.onTransact(var1, var2, var3, var4);
         }

         return var6;
      }

      private static class Proxy implements IMediaControllerCallback {
         private IBinder mRemote;

         Proxy(IBinder var1) {
            this.mRemote = var1;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return "android.support.v4.media.session.IMediaControllerCallback";
         }

         public void onEvent(String var1, Bundle var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.v4.media.session.IMediaControllerCallback");
                     var3.writeString(var1);
                  } catch (Throwable var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label170;
                  }

                  if (var2 != null) {
                     try {
                        var3.writeInt(1);
                        var2.writeToParcel(var3, 0);
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     this.mRemote.transact(1, var3, (Parcel)null, 1);
                     break label166;
                  } catch (Throwable var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var24 = var10000;
               var3.recycle();
               throw var24;
            }

            var3.recycle();
         }

         public void onExtrasChanged(Bundle var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var2.writeInterfaceToken("android.support.v4.media.session.IMediaControllerCallback");
                  } catch (Throwable var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label170;
                  }

                  if (var1 != null) {
                     try {
                        var2.writeInt(1);
                        var1.writeToParcel(var2, 0);
                     } catch (Throwable var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var2.writeInt(0);
                     } catch (Throwable var20) {
                        var10000 = var20;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     this.mRemote.transact(7, var2, (Parcel)null, 1);
                     break label166;
                  } catch (Throwable var19) {
                     var10000 = var19;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var23 = var10000;
               var2.recycle();
               throw var23;
            }

            var2.recycle();
         }

         public void onMetadataChanged(MediaMetadataCompat var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var2.writeInterfaceToken("android.support.v4.media.session.IMediaControllerCallback");
                  } catch (Throwable var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label170;
                  }

                  if (var1 != null) {
                     try {
                        var2.writeInt(1);
                        var1.writeToParcel(var2, 0);
                     } catch (Throwable var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var2.writeInt(0);
                     } catch (Throwable var20) {
                        var10000 = var20;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     this.mRemote.transact(4, var2, (Parcel)null, 1);
                     break label166;
                  } catch (Throwable var19) {
                     var10000 = var19;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var23 = var10000;
               var2.recycle();
               throw var23;
            }

            var2.recycle();
         }

         public void onPlaybackStateChanged(PlaybackStateCompat var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var2.writeInterfaceToken("android.support.v4.media.session.IMediaControllerCallback");
                  } catch (Throwable var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label170;
                  }

                  if (var1 != null) {
                     try {
                        var2.writeInt(1);
                        var1.writeToParcel(var2, 0);
                     } catch (Throwable var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var2.writeInt(0);
                     } catch (Throwable var20) {
                        var10000 = var20;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     this.mRemote.transact(3, var2, (Parcel)null, 1);
                     break label166;
                  } catch (Throwable var19) {
                     var10000 = var19;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var23 = var10000;
               var2.recycle();
               throw var23;
            }

            var2.recycle();
         }

         public void onQueueChanged(List var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();

            try {
               var2.writeInterfaceToken("android.support.v4.media.session.IMediaControllerCallback");
               var2.writeTypedList(var1);
               this.mRemote.transact(5, var2, (Parcel)null, 1);
            } finally {
               var2.recycle();
            }

         }

         public void onQueueTitleChanged(CharSequence var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var2.writeInterfaceToken("android.support.v4.media.session.IMediaControllerCallback");
                  } catch (Throwable var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label170;
                  }

                  if (var1 != null) {
                     try {
                        var2.writeInt(1);
                        TextUtils.writeToParcel(var1, var2, 0);
                     } catch (Throwable var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var2.writeInt(0);
                     } catch (Throwable var20) {
                        var10000 = var20;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     this.mRemote.transact(6, var2, (Parcel)null, 1);
                     break label166;
                  } catch (Throwable var19) {
                     var10000 = var19;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var23 = var10000;
               var2.recycle();
               throw var23;
            }

            var2.recycle();
         }

         public void onRepeatModeChanged(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();

            try {
               var2.writeInterfaceToken("android.support.v4.media.session.IMediaControllerCallback");
               var2.writeInt(var1);
               this.mRemote.transact(9, var2, (Parcel)null, 1);
            } finally {
               var2.recycle();
            }

         }

         public void onSessionDestroyed() throws RemoteException {
            Parcel var1 = Parcel.obtain();

            try {
               var1.writeInterfaceToken("android.support.v4.media.session.IMediaControllerCallback");
               this.mRemote.transact(2, var1, (Parcel)null, 1);
            } finally {
               var1.recycle();
            }

         }

         public void onShuffleModeChanged(boolean var1) throws RemoteException {
            byte var2 = 1;
            Parcel var3 = Parcel.obtain();

            label80: {
               Throwable var10000;
               label85: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.v4.media.session.IMediaControllerCallback");
                  } catch (Throwable var10) {
                     var10000 = var10;
                     var10001 = false;
                     break label85;
                  }

                  if (!var1) {
                     var2 = 0;
                  }

                  label75:
                  try {
                     var3.writeInt(var2);
                     this.mRemote.transact(10, var3, (Parcel)null, 1);
                     break label80;
                  } catch (Throwable var9) {
                     var10000 = var9;
                     var10001 = false;
                     break label75;
                  }
               }

               Throwable var4 = var10000;
               var3.recycle();
               throw var4;
            }

            var3.recycle();
         }

         public void onVolumeInfoChanged(ParcelableVolumeInfo var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var2.writeInterfaceToken("android.support.v4.media.session.IMediaControllerCallback");
                  } catch (Throwable var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label170;
                  }

                  if (var1 != null) {
                     try {
                        var2.writeInt(1);
                        var1.writeToParcel(var2, 0);
                     } catch (Throwable var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var2.writeInt(0);
                     } catch (Throwable var20) {
                        var10000 = var20;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     this.mRemote.transact(8, var2, (Parcel)null, 1);
                     break label166;
                  } catch (Throwable var19) {
                     var10000 = var19;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var23 = var10000;
               var2.recycle();
               throw var23;
            }

            var2.recycle();
         }
      }
   }
}
