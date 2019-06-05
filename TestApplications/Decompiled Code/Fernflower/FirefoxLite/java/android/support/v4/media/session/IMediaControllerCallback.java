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
   void onCaptioningEnabledChanged(boolean var1) throws RemoteException;

   void onEvent(String var1, Bundle var2) throws RemoteException;

   void onExtrasChanged(Bundle var1) throws RemoteException;

   void onMetadataChanged(MediaMetadataCompat var1) throws RemoteException;

   void onPlaybackStateChanged(PlaybackStateCompat var1) throws RemoteException;

   void onQueueChanged(List var1) throws RemoteException;

   void onQueueTitleChanged(CharSequence var1) throws RemoteException;

   void onRepeatModeChanged(int var1) throws RemoteException;

   void onSessionDestroyed() throws RemoteException;

   void onSessionReady() throws RemoteException;

   void onShuffleModeChanged(int var1) throws RemoteException;

   void onShuffleModeChangedRemoved(boolean var1) throws RemoteException;

   void onVolumeInfoChanged(ParcelableVolumeInfo var1) throws RemoteException;

   public abstract static class Stub extends Binder implements IMediaControllerCallback {
      public Stub() {
         this.attachInterface(this, "android.support.v4.media.session.IMediaControllerCallback");
      }

      public static IMediaControllerCallback asInterface(IBinder var0) {
         if (var0 == null) {
            return null;
         } else {
            IInterface var1 = var0.queryLocalInterface("android.support.v4.media.session.IMediaControllerCallback");
            return (IMediaControllerCallback)(var1 != null && var1 instanceof IMediaControllerCallback ? (IMediaControllerCallback)var1 : new IMediaControllerCallback.Stub.Proxy(var0));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int var1, Parcel var2, Parcel var3, int var4) throws RemoteException {
         if (var1 != 1598968902) {
            boolean var5 = false;
            boolean var6 = false;
            Object var7 = null;
            Object var8 = null;
            Object var9 = null;
            Object var10 = null;
            Object var11 = null;
            String var12 = null;
            Bundle var14;
            switch(var1) {
            case 1:
               var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
               var12 = var2.readString();
               var14 = (Bundle)var11;
               if (var2.readInt() != 0) {
                  var14 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
               }

               this.onEvent(var12, var14);
               return true;
            case 2:
               var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
               this.onSessionDestroyed();
               return true;
            case 3:
               var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
               PlaybackStateCompat var17 = (PlaybackStateCompat)var10;
               if (var2.readInt() != 0) {
                  var17 = (PlaybackStateCompat)PlaybackStateCompat.CREATOR.createFromParcel(var2);
               }

               this.onPlaybackStateChanged(var17);
               return true;
            case 4:
               var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
               MediaMetadataCompat var16 = (MediaMetadataCompat)var9;
               if (var2.readInt() != 0) {
                  var16 = (MediaMetadataCompat)MediaMetadataCompat.CREATOR.createFromParcel(var2);
               }

               this.onMetadataChanged(var16);
               return true;
            case 5:
               var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
               this.onQueueChanged(var2.createTypedArrayList(MediaSessionCompat.QueueItem.CREATOR));
               return true;
            case 6:
               var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
               CharSequence var15 = (CharSequence)var8;
               if (var2.readInt() != 0) {
                  var15 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(var2);
               }

               this.onQueueTitleChanged(var15);
               return true;
            case 7:
               var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
               var14 = (Bundle)var7;
               if (var2.readInt() != 0) {
                  var14 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
               }

               this.onExtrasChanged(var14);
               return true;
            case 8:
               var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
               ParcelableVolumeInfo var13 = var12;
               if (var2.readInt() != 0) {
                  var13 = (ParcelableVolumeInfo)ParcelableVolumeInfo.CREATOR.createFromParcel(var2);
               }

               this.onVolumeInfoChanged(var13);
               return true;
            case 9:
               var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
               this.onRepeatModeChanged(var2.readInt());
               return true;
            case 10:
               var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
               var6 = var5;
               if (var2.readInt() != 0) {
                  var6 = true;
               }

               this.onShuffleModeChangedRemoved(var6);
               return true;
            case 11:
               var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
               if (var2.readInt() != 0) {
                  var6 = true;
               }

               this.onCaptioningEnabledChanged(var6);
               return true;
            case 12:
               var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
               this.onShuffleModeChanged(var2.readInt());
               return true;
            case 13:
               var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
               this.onSessionReady();
               return true;
            default:
               return super.onTransact(var1, var2, var3, var4);
            }
         } else {
            var3.writeString("android.support.v4.media.session.IMediaControllerCallback");
            return true;
         }
      }

      private static class Proxy implements IMediaControllerCallback {
         private IBinder mRemote;

         Proxy(IBinder var1) {
            this.mRemote = var1;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public void onCaptioningEnabledChanged(boolean var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();

            try {
               var2.writeInterfaceToken("android.support.v4.media.session.IMediaControllerCallback");
               var2.writeInt(var1);
               this.mRemote.transact(11, var2, (Parcel)null, 1);
            } finally {
               var2.recycle();
            }

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

         public void onSessionReady() throws RemoteException {
            Parcel var1 = Parcel.obtain();

            try {
               var1.writeInterfaceToken("android.support.v4.media.session.IMediaControllerCallback");
               this.mRemote.transact(13, var1, (Parcel)null, 1);
            } finally {
               var1.recycle();
            }

         }

         public void onShuffleModeChanged(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();

            try {
               var2.writeInterfaceToken("android.support.v4.media.session.IMediaControllerCallback");
               var2.writeInt(var1);
               this.mRemote.transact(12, var2, (Parcel)null, 1);
            } finally {
               var2.recycle();
            }

         }

         public void onShuffleModeChangedRemoved(boolean var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();

            try {
               var2.writeInterfaceToken("android.support.v4.media.session.IMediaControllerCallback");
               var2.writeInt(var1);
               this.mRemote.transact(10, var2, (Parcel)null, 1);
            } finally {
               var2.recycle();
            }

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
